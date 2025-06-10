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
    private String flof = "AngleToTrackAxis;Speed;TrackEdgeSensors0;TrackEdgeSensor-90;TrackEdgeSensor+90;TarckEdgeSensor-50;TrackEdgeSensor+30;TrackPosition;action";

    /**
     * Costruttore che legge i dati dal file di training normalizzato
     */
    public NearestNeighbor() {
        this.trainingData = new ArrayList<>();
        this.readPointsFromCSV("../src/normalizedDataset.csv");
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
            while ((line = bw.readLine()) != null) {
                if (line.startsWith(flof)) {
                    continue; // Skip header
                }

                trainingData.add(new Point(line));
                System.out.println("Sto leggendo i punti.");
            }
            bw.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
    /**
     * Ricerca il Point più "simile" a quello in ingresso sfruttando la distanza euclidea 
     * 
     * @param testPoint punto di cui si deve individuare il più "simile"
     * @return la classe del punto più vicino nel training set, o -1 se il training set è vuoto
     */
    public int findNearestNeighbor(Point testPoint) {
        if (trainingData.isEmpty()) {
            System.out.println("training set vuoto");
            return -1; 
        }
        Point nearestNeighbor = trainingData.get(0); 
        double minDistance = testPoint.distance(nearestNeighbor); 

        for (Point point : trainingData) {
            double distance = testPoint.distance(point);
            if (distance < minDistance) {
                minDistance = distance;
                nearestNeighbor = point;
            }
        }
        return nearestNeighbor.getClasse(); 
    }
    
    /**
     * Funzione che resituisce la lista di Points che rappresenta i dati di training normalizzati 
     * 
     * @return Lista di punti di training
     */
    public List<Point> getTrainingData() {
        return trainingData;
    }
}
