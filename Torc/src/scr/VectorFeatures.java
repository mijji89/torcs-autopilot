package scr;

public class VectorFeatures {
    private Double[] features; 
    private int actionKey; 

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

	/*The method compute the distance between this point and the one passed as parameters*/
    public Double distance(VectorFeatures other) {
		//dobbiamo calcolare la distanza di tutte le features per classificare il campione -> 
        //sommiamo tutte le differenza this - other, eleva la differenza al quadrato, 
        //e restituisce la radice della somma
		Double sum = 0.0;
		for(int i = 0; i<this.features.length; i++){
			sum += Math.pow(this.features[i] - other.features[i], 2);
		}
            return Math.sqrt(sum);
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
