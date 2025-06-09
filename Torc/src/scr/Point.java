package scr;

/*La classe permette di memorizzare solo i parametri di interesse con la classe d'azione */
public class Point {
    private Double[] features; 
    private int classe;

	/*Costruttore utilizzato per associare una classe d'azione sulla base dei valori dei parametri in ingresso */
	public Point(VectorFeatures vfN, NearestNeighbor nn){
		this.features = new Double[] {vfN.getFeatures()[0], vfN.getFeatures()[5], vfN.getFeatures()[8], vfN.getFeatures()[9], vfN.getFeatures()[10], vfN.getFeatures()[11], vfN.getFeatures()[12], vfN.getFeatures()[13]};
		this.classe = nn.findNearestNeighbor(this);
	}

    /*Costruttore per la gestione della lettura dei dati del trainingSet*/
	public Point(String lineCSV){
		String[] parts = lineCSV.split(";");
		int n = parts.length;
		this.features = new Double[8];
		for(int i=0; i<n-1; i++){
			this.features[i] = Double.parseDouble(parts[i].trim());
		}
		this.classe = Integer.parseInt(parts[n-1].trim());
	}

    /*La funzione permette di calcolare la distanza euclidea tra due punti ad n dimensioni (dove n è il numero di features prese in esame)
	 * Restituisce il valore della distanza.*/
    public Double distance(Point other) {
		Double sum = 0.0;
		for(int i = 0; i<this.features.length; i++){
			sum += Math.pow(this.features[i] - other.features[i], 2);
		}
            return Math.sqrt(sum);
    }

	/*La funzione restituisce la classe d'azione del punto */
	public int getClasse(){
		return this.classe;
	}
}

