package scr;

/**
 * La classe rappresenta il vettore di features estratto dai sensori di gioco, utilizzato per il training e la predizione. 
 * 
 * Contiene:
 * - {@code features[]}: un vettore di Double contenente tutti i parametri letti "attualmente" dai sensori
 * - {@code actionKey}: un intero che rappresenta le diverse azioni possibili
 * - {@code minVf[]}: un vettore di Double contenente tutti i minimi valori per ciascun parametro
 * - {@code maxVf[]}: un vettore di Double contenente tutti i massimi valori per ciascun parametro
 */
public class VectorFeatures {
    private Double[] features; 
    private int actionKey; 

	private final Double[] minVf={-(Math.PI),-0.982,0.0,0.166,0.0,-90.099,-15.153,0.214,-1.0,-1.0,-1.0,-1.0,-1.0,-7.42};
	private final Double[] maxVf={+(Math.PI),290.046,79.0,5784.10,11513.0,243.755,10.112,0.429,200.0,200.0,200.0,200.0,200.0,7.702};

    /**
     * Costruttore che costruisce il vectorFeatures con valori dei parametri normalizzati
     * 
     * @param sensors riferimento ai sensori di gioco
     */
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

    /**
     * Costruttore usato in fase di training.
     * Inizializza l'oggetto con i parametri misurati "attualmente" e l'azione
     * effettuata in quell'istante. L'azione può essere: 
     * 
     * - 0 -> accelera 
     * - 1 -> giraSX 
     * - 2 -> giraDX 
     * - 3 -> frena 
     * - 4 -> retromarcia 
     * - 5 -> sinistra + avanti 
     * - 6 -> destra + avanti 
     * 
     * @param sensors riferimento ai sensori di gioco
     * @param action classe d'azione
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
    
    /**
     * Ritorna la classe d'azione 
     * 
     * @return la classe d'azione
     */
    public int getActionKey(){
        return this.actionKey;
    }

    /**
     * Imposta un sottoinsieme delle features rilevanti per la guida: 
     * 
     * @param angleAxis angolo tra la direazione dell'auto e l'asse tangente al tracciato
     * @param  speed velocità
     * @param angleZero distanza tra il sensore in posizione 0° ed il bordo della pista
     * @param angleMinNinenty distanza tra il sensore in posizione -90° ed il bordo della pista
     * @param angleMaxNinety distanza tra il sensore in posizione +90° ed il bordo della pista
     * @param angkeMinFifty distanza tra il sensore in posizione -50° ed il bordo della pista
     * @param angleMaxThirty distanza tra il sensore in posizione +30° ed il bordo della pista
     * @param trackPos distanza tra l'auto e l'asse della pista
     * 
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

    /**
     * Restituisce il vettore di features
     * 
     * @return vettore di features
     */
    public Double[] getFeatures(){
        return this.features;
    }

    /**
     * Permette di trasformare in stringa l'oggetto
     * 
     * @return la stringa rappresentate l'oggetto
     */
    @Override
    public String toString(){
        return this.features[0]+";"+this.features[1]+";"+this.features[2]+";"+this.features[3]+";"+this.features[4]+";"+this.features[5]+";"+this.features[6]+";"+this.features[7]+";"+this.features[8]+";"+this.features[9]+";"+this.features[10]+";"+this.features[11]+";"+this.features[12]+";"+this.features[13]+";"+this.actionKey;
    }
	
	/**
     * Costruttore per la gestione della lettura dei dati del trainingSet
     * 
     * @param lineCSV linea rappresentante il vettore di features, dove ogni campo è separato da ; 
     */
	public VectorFeatures(String lineCSV){
		String[] parts = lineCSV.split(";");
		int n = parts.length;
		this.features = new Double[14];
		for(int i=0; i<n-1; i++){
			this.features[i] = Double.parseDouble(parts[i].trim());
		}
		this.actionKey = Integer.parseInt(parts[n-1].trim());
	}

	  
    
    /**
     * Normalizza i valori del veactor features sfruttando due vettori: 
     * uno contenente i minimi per ogni feature (min[]) e uno contenente i massimi per ogni feature (max[])
     *
     * @param min[] vettore contenente i valori minimi per ogni parametro
     * @param max[] vettore contenente i valori massimi per ogni parametro
     * 
     * @return Il vettore di features normalizzato, dove ogni valore è compreso in un range [0;1]
     */
    public Double[] normalizeMinMax(Double[] min, Double[] max){
        Double[] normalized= new Double[14];
        for(int i=0; i< features.length; i++ ){
            normalized[i]= (features[i]-min[i])/(max[i]-min[i]);
        }
        return normalized;
    }

}
