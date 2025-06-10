package scr;

/**
 * Classe che permette di memorizzare solo i parametri di interesse con la classe d'azione 
 */
public class Point {
    private Double[] features; 
    private int classe;

	/**
	 * Costruttore utilizzato per associare una classe d'azione sulla base dei valori dei parametri in ingresso 
	 * 
	 * @param vfN istanza di VectorFeatures con i parametri normalizzati 
	 * @param nn istanza di NearestNeighbor che permette di trovare il punto più simile
	 */
	public Point(VectorFeatures vfN, NearestNeighbor nn){
		this.features = new Double[] {vfN.getFeatures()[0], vfN.getFeatures()[5], vfN.getFeatures()[8], vfN.getFeatures()[9], vfN.getFeatures()[10], vfN.getFeatures()[11], vfN.getFeatures()[12], vfN.getFeatures()[13]};
		this.classe = nn.findNearestNeighbor(this);
	}

    /**
	 * Costruttore per la gestione della lettura dei dati del trainingSet
	 * 
	 * @param lineCSV linea del file CSV contenente le feature separate da punto e virgola, seguite dalla classe
	 */
	public Point(String lineCSV){
		String[] parts = lineCSV.split(";");
		int n = parts.length;
		this.features = new Double[8];
		for(int i=0; i<n-1; i++){
			this.features[i] = Double.parseDouble(parts[i].trim());
		}
		this.classe = Integer.parseInt(parts[n-1].trim());
	}

    /**
	 * Calcola la distanza euclidea tra due punti nello spazio delle feature
	 * 
	 * @param other punto rispetto al quale calcolare la distanza
	 * @return  distanza euclidea calcolata
	.*/
    public Double distance(Point other) {
		Double sum = 0.0;
		for(int i = 0; i<this.features.length; i++){
			sum += Math.pow(this.features[i] - other.features[i], 2);
		}
            return Math.sqrt(sum);
    }

	/**
	 * Restituisce la classe d'azione del punto 
	 * 
	 * @return la classe d'azione
	 */
	public int getClasse(){
		return this.classe;
	}
}

