package scr;

/**
 * Classe che rappresenta un singolo punto (campione) del dataset, contenente solo le features di interesse
 */
public class Point {
    Double[] features; 
    int classe;

	/**
	 * Costruttore utilizzato per associare una classe d'azione sulla base dei valori dei parametri in ingresso 
	 * 
	 * @param vfN istanza di VectorFeatures con i parametri normalizzati 
	 * @param nn istanza di NearestNeighbor usata per classificare il punto 
	 * @param k numero di vicini da considerare per il K-NN
	 */
	public Point(VectorFeatures vfN, NearestNeighbor nn, int k){
		this.features = new Double[] {vfN.getFeatures()[0],vfN.getFeatures()[5], vfN.getFeatures()[8], vfN.getFeatures()[9], vfN.getFeatures()[10], vfN.getFeatures()[11], vfN.getFeatures()[12], vfN.getFeatures()[13]};
		this.classe = nn.classify(this,k);
	}

    /**
	 * Costruttore  usato per leggere un punto dal file CSV del training set.
	 * Ogni riga contiene le feature separate da punto e virgola, seguite dalla classe d'azione
	 * 
	 * @param lineCSV riga del file CSV contenete le feature e la classe
	 */
	public Point(String lineCSV){
		String[] parts = lineCSV.split(";");
		int n = parts.length;
		System.out.println(n);
		this.features = new Double[n-1];
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
	*/
    public double distance(Point other) {
		double sum = 0.0;
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

