package scr;

/*La classe implementa il vettore di features utilizzato per raccogliere i dati. La classe possiede: 
 * - features[]: un vettore di Double contenente tutti i parametri letti "attualmente" dai sensori
 * - actionKey: un intero che rappresenta le diverse azioni possibili
 * - minVf[]: un vettore di Double contenente tutti i minimi valori per ciascun parametro
 * - maxVf[]: un vettore di Double contenente tutti i massimi valori per ciascun parametro
 */
public class VectorFeatures {
    private Double[] features; 
    private int actionKey; 
	private final Double[] minVf={-(Math.PI),-0.982,0.0,0.173,0.0,-0.01,-14.181,0.228,-1.0,-1.0,-1.0,-1.0,-1.0,-1.10};
	private final Double[] maxVf={+(Math.PI),189.326,3.0,5784.10,11494.20,255.925,9.327,0.441,200.0,200.0,200.0,200.0,200.0,2.998};
    
    /*Costruttore usato per inizializzare l'oggetto con i valori attualmente letti dai sensori normalizzati */
    public VectorFeatures(SensorModel sensors){
        this.features = new Double[14];
        this.features[0]= sensors.getAngleToTrackAxis(); 
        this.features[1]=sensors.getCurrentLapTime(); 
        this.features[2]=sensors.getDamage(); 
        this.features[3]=sensors.getDistanceFromStartLine(); 
        this.features[4]=sensors.getDistanceRaced(); 
        this.features[5]=sensors.getSpeed(); 
        this.features[6]=sensors.getZSpeed(); //per le buche
        this.features[7]=sensors.getZ(); 
        //Distanza dalle linee di corsia
        this.features[8]=sensors.getTrackEdgeSensors()[9];//0°
        this.features[9]=sensors.getTrackEdgeSensors()[0];//-90°
        this.features[10]=sensors.getTrackEdgeSensors()[18]; //+90°
        this.features[11]=sensors.getTrackEdgeSensors()[4]; //-50°
        this.features[12]=sensors.getTrackEdgeSensors()[12]; //+30°
        this.features[13]=sensors.getTrackPosition();
        //normalizzazione
        this.features = this.normalizeMinMax(minVf, maxVf); 

    }

    /*Costruttore usato in fase di training che inizializza l'oggetto con i parametri misurati "attualmente" e l'azione
     * effettuata in quell'istante. L'azione può essere: 
     * - 0 -> accelera 
     * - 1 -> giraSX 
     * - 2 -> giraDX 
     * - 3 -> frena 
     * - 4 -> retromarcia 
     * - 5 -> sinistra + avanti 
     * - 6 -> destra + avanti 
     */
    public VectorFeatures(SensorModel sensors, int action){
        this.features = new Double[14];
        this.features[0]= sensors.getAngleToTrackAxis(); 
        this.features[1]=sensors.getCurrentLapTime(); 
        this.features[2]=sensors.getDamage(); 
        this.features[3]=sensors.getDistanceFromStartLine(); 
        this.features[4]=sensors.getDistanceRaced(); 
        this.features[5]=sensors.getSpeed(); 
        this.features[6]=sensors.getZSpeed(); //per le buche
        this.features[7]=sensors.getZ(); 
        //Distanza dalle linee di corsia
        this.features[8]=sensors.getTrackEdgeSensors()[9];//0°
        this.features[9]=sensors.getTrackEdgeSensors()[0];//-90°
        this.features[10]=sensors.getTrackEdgeSensors()[18]; //+90°
        this.features[11]=sensors.getTrackEdgeSensors()[4]; //-50°
        this.features[12]=sensors.getTrackEdgeSensors()[12]; //+30°
        this.features[13]=sensors.getTrackPosition(); 
        this.actionKey= action;        
    }
    
    /*La funzione ritorna la classe d'azione */
    public int getActionKey(){
        return this.actionKey;
    }

    /*La funzione permette di impostare solo le features di interesse per la guida: 
     * - angleAxis
     * - speed
     * - angleZero
     * - angleMinNinenty
     * - angleMaxNinety
     * - angkeMinFifty
     * - angleMaxThirty
     * - trackPos
     */
    public void setFeatures(Double angleAxis, Double speed, Double angleZero, Double angleMinNinenty , Double angleMaxNinety, Double angleMinFifty, Double angleMaxThirty, Double trackPos ){
        this.features[0]=angleAxis; 
        this.features[5]=speed; 
        this.features[8]=angleZero; 
        this.features[9]=angleMinNinenty; 
        this.features[10]=angleMaxNinety;
        this.features[11]=angleMinFifty;
        this.features[12]=angleMaxThirty;
        this.features[13]=trackPos; 
    }

    public Double[] getFeatures(){
        return this.features;
    }

    @Override
    public String toString(){
        return this.features[0]+";"+this.features[1]+";"+this.features[2]+";"+this.features[3]+";"+this.features[4]+";"+this.features[5]+";"+this.features[6]+";"+this.features[7]+";"+this.features[8]+";"+this.features[9]+";"+this.features[10]+";"+this.features[11]+";"+this.features[12]+";"+this.features[13]+";"+this.actionKey;
    }
	
	/*Costruttore per la gestione della lettura dei dati del trainingSet*/
	public VectorFeatures(String lineCSV){
		String[] parts = lineCSV.split(";");
		int n = parts.length;
		this.features = new Double[14];
		for(int i=0; i<n-1; i++){
			this.features[i] = Double.parseDouble(parts[i].trim());
		}
		this.actionKey = Integer.parseInt(parts[n-1].trim());
	}

	  
    
    /*La funzione normalizza i valori del veactor features sfruttando due vettori: 
     *uno contenente i minimi per ogni feature (min[]) e uno contenente i massimi per ogni feature (max[])*/
    public Double[] normalizeMinMax(Double[] min, Double[] max){
        Double[] normalized= new Double[14];
        for(int i=0; i< features.length; i++ ){
            normalized[i]= (features[i]-min[i])/(max[i]-min[i]);
        }
        return normalized;
    }

}
