package scr;

public class Point {
    private Double[] features; 
    private int classe;
    
    /*Costruttore per la gestione della lettura dei dati del trainingSet*/
	public Point(String lineCSV){
		String[] parts = lineCSV.split(";");
		int n = parts.length;
		this.features = new Double[8];
		for(int i=0; i<n-1; i++){
			this.features[i] = Double.parseDouble(parts[i].trim());
			//il metodo trim rimuove gli spazi bianchi a inizio e fine stringa
		}
		this.classe = Integer.parseInt(parts[n-1].trim());
	}

    /*The method compute the distance between this point and the one passed as parameters*/
    public Double distance(Point other) {
		//dobbiamo calcolare la distanza di tutte le features per classificare il campione -> 
        //sommiamo tutte le differenza this - other, eleva la differenza al quadrato, 
        //e restituisce la radice della somma
		Double sum = 0.0;
		for(int i = 0; i<this.features.length; i++){
			sum += Math.pow(this.features[i] - other.features[i], 2);
		}
            return Math.sqrt(sum);
    }

	public int getClasse(){
		return this.classe;
	}
}

