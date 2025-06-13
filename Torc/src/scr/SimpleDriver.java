package scr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.SwingUtilities;

/**
 * La classe implementa la guida sia manuale che autonoma, gestendo in maniera automatica i freni e le marce.
 * 
 * 
 * - {@code training}: flag booleana che permette di abilitare o meno la modalità di training
 * - {@code pressed}: mantiene il carattere corrispondente al tasto premuto (w-a-s-d-q-e-r)
 * - {@code gearUp, gearDown}: array di soglie per il cambio marci automatico
 * - {@code bw}: BufferedWriter che consente di scrivere sul file CSV
 * - {@code vfN}: vettore delle features normalizzate
 * - {@code nn}: classificatore NearestNeighbor utilizzata per predirre l'azione da effettuare
 * - {@code clutch}:valore corrente della frizione (inizialmente a 0)
 * - {@code file}: riferimento al file CSV per il salvataggio dei dati di training
 */
public class SimpleDriver extends Controller {
	//Flag booleana che mi permette di leggere o meno i valori di tastiera
	private boolean training = false;   
	private char pressed;  
	
	final int[] gearUp = { 5000, 6000, 6000, 6500, 7000, 0 };
	final int[] gearDown = { 0, 2500, 3000, 3000, 3500, 3500 };

	/* Costanti di accelerazione e di frenata */
	final float maxSpeedDist = 70;
	final float maxSpeed = 150;
	final float sin5 = (float) 0.08716;
	final float cos5 = (float) 0.99619;

	/* Costanti di sterzata */
	final float steerLock = (float) 0.785398;
	final float steerSensitivityOffset = (float) 80.0;
	final float wheelSensitivityCoeff = 1;

	/* Costanti del filtro ABS */
	final float wheelRadius[] = { (float) 0.3179, (float) 0.3179, (float) 0.3276, (float) 0.3276 };
	final float absSlip = (float) 2.0;
	final float absRange = (float) 3.0;
	final float absMinSpeed = (float) 3.0;

	/* Costanti da stringere */
	final float clutchMax = (float) 0.5;
	final float clutchDelta = (float) 0.05;
	final float clutchRange = (float) 0.82;
	final float clutchDeltaTime = (float) 0.02;
	final float clutchDeltaRaced = 10;
	final float clutchDec = (float) 0.01;
	final float clutchMaxModifier = (float) 1.3;
	final float clutchMaxTime = (float) 1.5;

	BufferedWriter bw; 
	VectorFeatures vfN;
	NearestNeighbor nn = new NearestNeighbor();
	private float clutch = 0;

	File file = new File("dataset3Lap.csv");

	/**
	 * Costruttore che, se in modalità addestramento, permette di: 
	 * 
	 * - Inizializzare il BufferedWriter se il file su cui scrivere non esiste
	 * - Continuare a scrivere sul file  se questo esiste
	 * - Lanciare l'interfaccia grafica che permette la raccolta dei dati in ambo i casi
	 */
	public SimpleDriver(){
		if (training & !file.exists()){
			try{
				this.bw = new BufferedWriter(new FileWriter(file));
				this.bw.write("AngleToTrackAxis;CurrentLapTime;Damage;DistanceFromStartLine;DistanceRaced;Speed;ZSpeed;Z;TrackEdgeSensors0;TrackEdgeSensor-90;TrackEdgeSensor+90;TarckEdgeSensor-50;TrackEdgeSensor+30;TrackPosition;action");
				this.bw.write("\n");
			}catch(IOException ex){
				System.err.println();
			}
			SwingUtilities.invokeLater(() -> new ContinuousCharReaderUI(this));
		}else if (training & file.exists()){
            try {
                this.bw = new BufferedWriter(new FileWriter(file,true));
				this.bw.append("\n");
				this.bw.append("AngleToTrackAxis;CurrentLapTime;Damage;DistanceFromStartLine;DistanceRaced;Speed;ZSpeed;Z;TrackEdgeSensors0;TrackEdgeSensor-90;TrackEdgeSensor+90;TarckEdgeSensor-50;TrackEdgeSensor+30;TrackPosition;action");
				this.bw.append("\n");
			} catch (IOException ex) {
            }
			SwingUtilities.invokeLater(() -> new ContinuousCharReaderUI(this));
		}
	}

	/**
	 * Imposta la variabile pressed facendola corrispondere con il carattere associato al pulsante premuto 
	 * 
	 * @param ch carattere corrispondente al pulsante premuto (w-a-s-d-q-e-r)
	 */
	public  void setPressed(char ch){
		this.pressed=ch; 	
	}

	/**
	 * Scrive una nuova riga vuota sul CSV di training.
	 * Viene chiamata automaticamente quando si riavvia una partita
	 */
	public void reset() {
        try {
            this.bw.append("\n");
        } catch (IOException ex) {
        }
		System.out.println("Restarting the race!");

	}

	/**
	 * Scrive una nuova riga vuota sul CSV di training. 
	 * Viene automaticamente chiamata quando la partita viene chiusa. 
	 */
	public void shutdown() {
        try {
            this.bw.append("\n");
        } catch (IOException ex) {
        }
		System.out.println("Bye bye!");
	
	}

	/**
	 * Gestisce la marcia in maniera automatica 
	 * 
	 * @param sensors riferimento ai sensori di gioco
	 */
	private int getGear(SensorModel sensors) {
		int gear = sensors.getGear();
		double rpm = sensors.getRPM();
		// Se la marcia è 0 (N) o -1 (R) restituisce semplicemente 1
		if (gear < 1)
			return 1;
		// Se il valore di RPM dell'auto è maggiore di quello suggerito
		// sale di marcia rispetto a quella attuale
		if (gear < 6 && rpm >= gearUp[gear - 1])
			return gear + 1;
		else
		// Se il valore di RPM dell'auto è inferiore a quello suggerito
		// scala la marcia rispetto a quella attuale
		if (gear > 1 && rpm <= gearDown[gear - 1])
			return gear - 1;
		else // Altrimenti mantenere l'attuale
			return gear;
	}

	/**
	 * Gestisce la creazione di un'azione in risposta alla lettura dei sensori.
	 * 
	 * - Se {@code training} è {@code true}, consente il controllo manuale tramite tastiera
	 * - Se {@code training} è {@code false}, predice l'azione da eseguire usando NearestNeighbor
	 * 
	 * @param sensors riferimento ai sensori del gioco
	 * @return azione da applicare
	 */
	public Action control(SensorModel sensors) {
		if (!training){
			vfN = new VectorFeatures(sensors);
			Point p = new Point(vfN, nn);		
			Action act = new Action();
			act = predictAction(p.getClasse(), sensors, clutch);
			return act;
		}
		else{
			VectorFeatures vf= null; 
			switch (this.pressed) {
				case 'w': 
					//accelera
					vf = new VectorFeatures(sensors, 0); 
					break;
				case 'a': 
					//sinistra
					vf = new VectorFeatures(sensors,1 ); 
					break;
				case 'd': 
					//destra
					vf = new VectorFeatures(sensors,2 ); 
					break;
				case 's': 
					//frena
					vf = new VectorFeatures(sensors,3 ); 
					break;
				case 'r': 
					//retromarcia
					vf = new VectorFeatures(sensors,4 ); 
					break;
				case 'q': 
					//sinistra avanti
					vf = new VectorFeatures(sensors,5 ); 
					break;
				case 'e': 
					//destra avanti
					vf = new VectorFeatures(sensors,6 ); 
					break;
				default:
					vf = new VectorFeatures(sensors, -1);
					break; 	
			}
			try{
				this.bw.append(vf.toString());
				this.bw.append("\n");
			}catch(IOException ex){
				System.err.println();
			}
			Action act= ManualControl(vf,sensors, clutch);
			return act;
		}
	}

	/**
	 * Associa a ciascuna classe passata ({@code pClasse}) l'azione da effettuare 
	 * 
	 * @param pClasse classe dell'azione da effettuare
	 * @param sensor riferimento ai sensori di gioco
	 * @param currClutch valore della frizione corrente
	 * 
	 * @return l'azione da applicare
	 */
	public Action predictAction (int pClasse, SensorModel sensor, float currclutch){
		int gear= getGear(sensor); 
		float clutch= clutching(sensor, currclutch);

		Action act = new Action();
		act.gear = gear;
		act.clutch = clutch;

		switch(pClasse){
			case 0:
				//accelera
				act.accelerate = 1.0;
				act.brake = 0.0;
				act.steering = 0.0;
				break;
			case 1:
				//sinistra
				act.accelerate = 0.7;
				act.brake = 0.0;
				act.steering = 0.7;
				break;
			case 2:
				//destra
				act.accelerate = 0.7;
				act.brake = 0.0;
				act.steering = -0.7;
				break;
			case 3:
				//frena
				act.accelerate = 0.0;
				act.brake = 1.0;
				act.steering = 0;
				break;
			case 4:
				//retromarcia
				act.gear = -1;
				act.accelerate = 1.0; 
				act.brake = 0.0;
				act.steering = 0;
				break;
			case 5:
				//avanti sinistra
				act.accelerate = 0.8; 
				act.brake = 0.0;
				act.steering = 0.4;
				break;
			case 6:
				//avanti destra
				act.accelerate = 0.8; 
				act.brake = 0.0;
				act.steering = -0.4;
				break;
			case -1:
				//default
				act.accelerate = 0.0;
				act.brake = 0.0;
				act.steering = 0;
				break;
		}
		return act;
	}

	/**
	 * Sulla base delL'ActionKey contenuto nel {@code vectorFeatures} (vf), associa una risposta 
	 * La frizione e la marcia vengono comunque gestite automaticamente.
	 * 
	 * @param vf vettore delle features
	 * @param sensor riferimento ai sensori di giocoà
	 * @param currClutch valore attuale della frizione
	 * 
	 * @return azione da effettuare sulla base dell'ActionKey
	 */
	public 	Action ManualControl(VectorFeatures vf, SensorModel sensor, float currclutch){
		//Frizione e freno continuano ad essere gestiti in automatico
		int gear= getGear(sensor); 
		float clutch= clutching(sensor, currclutch);
		int actionClass = vf.getActionKey();
		Action azione = new Action();
		azione.gear = gear;
		azione.clutch = clutch;
		switch(actionClass){
			case 0:
				//accelera
				azione.accelerate = 1.0;
				azione.brake = 0.0;
				azione.steering = 0.0;
				break;
			case 1:
				//sinistra
				azione.accelerate = 0.7;
				azione.brake = 0.0;
				azione.steering = 0.7;
				break;
			case 2:
				//destra
				azione.accelerate = 0.7;
				azione.brake = 0.0;
				azione.steering = -0.7;
				break;
			case 3:
				//frena
				azione.accelerate = 0.0;
				azione.brake = 1.0;
				azione.steering = 0;
				break;
			case 4:
				//retromarcia
				azione.gear = -1;
				azione.accelerate = 1.0; 
				azione.brake = 0.0;
				azione.steering = 0;
				break;
			case 5:
				//avanti sinistra
				azione.accelerate = 0.8; 
				azione.brake = 0.0;
				azione.steering = 0.4;
				break;
			case 6:
				//avanti destra
				azione.accelerate = 0.8; 
				azione.brake = 0.0;
				azione.steering = -0.4;
				break;
			case -1:
				//default
				azione.accelerate = 0.0;
				azione.brake = 0.0;
				azione.steering = 0;
				break;
		}
		return azione;
	}

	/**
	 * Gestisce in maniera automatica la frizione
	 * 
	 * @param sensors riferimento ai sensori di gioco
	 * @param clutch valore corrente della frizione
	 * 
	 * @return il valore della frizione da impostare 
	 */
	float clutching(SensorModel sensors, float clutch) {
		float maxClutch = clutchMax;
		// Controlla se la situazione attuale è l'inizio della gara
		if (sensors.getCurrentLapTime() < clutchDeltaTime && getStage() == Stage.RACE
				&& sensors.getDistanceRaced() < clutchDeltaRaced)
			clutch = maxClutch;
		// Regolare il valore attuale della frizione
		if (clutch > 0) {
			double delta = clutchDelta;
			if (sensors.getGear() < 2) {
				// Applicare un'uscita più forte della frizione quando la marcia è una e la corsa è appena iniziata.
				delta /= 2;
				maxClutch *= clutchMaxModifier;
				if (sensors.getCurrentLapTime() < clutchMaxTime)
					clutch = maxClutch;
			}
			// Controllare che la frizione non sia più grande dei valori massimi
			clutch = Math.min(maxClutch, clutch);
			// Se la frizione non è al massimo valore, diminuisce abbastanza rapidamente
			if (clutch != maxClutch) {
				clutch -= delta;
				clutch = Math.max((float) 0.0, clutch);
			}
			// Se la frizione è al valore massimo, diminuirla molto lentamente.
			else
				clutch -= clutchDec;
		}
		return clutch;
	}

	/**
	 * Inizializza i 19 angoli dei sensori con valori tra -90 e +90 gradi.
	 * 
	 * @return array contente i valori degli angoli
	 */
	public float[] initAngles() {
		float[] angles = new float[19];
		/*
		 * set angles as
		 * {-90,-75,-60,-45,-30,-20,-15,-10,-5,0,5,10,15,20,30,45,60,75,90}
		 */
		for (int i = 0; i < 5; i++) {
			angles[i] = -90 + i * 15;
			angles[18 - i] = 90 - i * 15;
		}
		for (int i = 5; i < 9; i++) {
			angles[i] = -20 + (i - 5) * 5;
			angles[18 - i] = 20 - (i - 5) * 5;
		}
		angles[9] = 0;
		return angles;
	}
}
