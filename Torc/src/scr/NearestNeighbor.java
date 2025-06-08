package scr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NearestNeighbor {
    
    private List<Point> trainingData;
    private String flof = "AngleToTrackAxis;Speed;TrackEdgeSensors0;TrackEdgeSensor-90;TrackEdgeSensor+90;TarckEdgeSensor-50;TrackEdgeSensor+30;TrackPosition;action";

    public NearestNeighbor() {
        this.trainingData = new ArrayList<>();
        this.readPointsFromCSV("normalizedDataset.csv");
    }

    public void readPointsFromCSV(String filename) {
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
    
    public Point findNearestNeighbor(Point testPoint) {
        if (trainingData.isEmpty()) {
            System.out.println("training set vuoto");
            return new Point(" "); // Nessun dato di allenamento disponibile, restituisci un punto vuoto
        }
        
        Point nearestNeighbor = trainingData.get(0); // Imposta il primo punto come punto più vicino iniziale
        double minDistance = testPoint.distance(nearestNeighbor); // Calcola la distanza dal primo punto
        
        // Cerca il punto più vicino
        for (Point point : trainingData) {
            double distance = testPoint.distance(point);
            //System.out.println("Distance to point (" + point.x + ", " + point.y + "): " + distance);
            if (distance < minDistance) {
                minDistance = distance;
                nearestNeighbor = point;
            }
        }
        return nearestNeighbor; //opzione di tornare la classe per gestirla nel simpleDriver come fatto con i tasti
    }
    
    public List<Point> getTrainingData() {
        return trainingData;
    }
}
