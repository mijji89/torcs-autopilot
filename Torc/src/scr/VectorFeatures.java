package scr;

public class VectorFeatures {
    private Double[] features; 
    private int actionKey; 

	private final Double[] minVf={-(Math.PI),-0.982,0.0,0.173,0.0,-0.01,-14.181,0.228,-1.0,-1.0,-1.0,-1.0,-1.0,-1.10};
	private final Double[] maxVf={+(Math.PI),189.326,3.0,5784.10,11494.20,255.925,9.327,0.441,200.0,200.0,200.0,200.0,200.0,2.998};
    
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
        this.features = this.normalizeMinMax(minVf, maxVf); 

    }

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
        //Distanza tra l'auto e l'asse della pista
        //0 sono al centro della pista, se sono destra -1, se sono a sinistra +1
        this.features[13]=sensors.getTrackPosition(); 
        this.actionKey= action;        
    }
    
    public int getActionKey(){
        return this.actionKey;
    }

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
			//il metodo trim rimuove gli spazi bianchi a inizio e fine stringa
		}
		this.actionKey = Integer.parseInt(parts[n-1].trim());
	}

	  
    
    //La funzione normalizza i valori del veactor features sfruttando due vettori: uno contenente i minimi per ogni feature e uno contenente i massimi per ogni feature
    //Minimi e massimi devono far riferimento ai minimi e massimi generici, non a quelli del training set
    public Double[] normalizeMinMax(Double[] min, Double[] max){
        Double[] normalized= new Double[14];
        for(int i=0; i< features.length; i++ ){
            normalized[i]= (features[i]-min[i])/(max[i]-min[i]);
        }
        return normalized;
    }

}
