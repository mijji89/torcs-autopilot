package scr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che implementa l'algoritmo di ricerca NearestNeighbor, permette di trovare il campione "più simile" alla situazione attuale.
 *
 * - trainingData: Lista di oggetti Point che rappresenta i dati di training normalizzati (con i soli parametri di interese)
 * - flof: stringa che rappresenta le righe dal file di training da non considerare
 */
public class NearestNeighbor {
    private List<Point> trainingData;
    private String flof = "AngleToTrackAxis;Damage;Speed;TrackEdgeSensors0;TrackEdgeSensor-90;TrackEdgeSensor+90;TarckEdgeSensor-50;TrackEdgeSensor+30;TrackPosition;action";
    private KDTree kdtree;
    private int[] classCounts;
    /**
     * Costruttore che legge i dati dal file di training normalizzato
     */
    public NearestNeighbor() {
        this.trainingData = new ArrayList<>();
        this.readPointsFromCSV("../src/normalizedDataset.csv");
        this.classCounts = new int[7];
    }

    /**
     * Legge i dati da un file CSV
     * 
     * @param filename rappresenta il file da cui leggere i dati
     */
    private void readPointsFromCSV(String filename) {
        try {
            BufferedReader bw = new BufferedReader(new FileReader(filename));
            String line;
            bw.readLine();
            while ((line = bw.readLine()) != null) {
                this.trainingData.add(new Point(line));
                System.out.println("Sto leggendo i punti.");
            }
            bw.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
        System.out.println("Totale punti letti: " + this.trainingData.size());
        if (!this.trainingData.isEmpty()) {
            this.kdtree = new KDTree(this.trainingData);
        } else {
            System.err.println("Il dataset è vuoto! KDTree non è stato creato.");
    }
    }
    
    /**
     * Ricerca il Point più "simile" a quello in ingresso sfruttando la distanza euclidea 
     * 
     * @param testPoint punto di cui si deve individuare il più "simile"
     * @return la classe del punto più vicino nel training set, o -1 se il training set è vuoto
     */
    public List<Point> findNearestNeighbor(Point testPoint, int k) {
        return kdtree.kNearestNeighbors(testPoint, k);
    }

    public int classify(Point testPoint, int k){
        List<Point> knn = this.findNearestNeighbor(testPoint, k);
        for (int i=0; i< classCounts.length;i++){
            classCounts[i]=0;
        }
        for (Point point : knn) {
            classCounts[point.classe]++;
            }
        int maxCount = -1;
        int predictedClass = -1;
        for (int i = 0; i < classCounts.length; i++) {
            if (classCounts[i] > maxCount) {
                maxCount = classCounts[i];
                predictedClass = i;
            } 
    }
        return predictedClass;
    }
    /**
     * Funzione che resituisce la lista di Points che rappresenta i dati di training normalizzati 
     * 
     * @return Lista di punti di training
     */
    public List<Point> getTrainingData() {
        return this.trainingData;
    }
}
