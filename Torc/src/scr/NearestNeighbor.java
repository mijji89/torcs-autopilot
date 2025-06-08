package scr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NearestNeighbor {
    
    private List<Double[]> trainingData;
    
    public NearestNeighbor() {
        this.trainingData = new ArrayList<>();
    }
    /*
    Reading the training set from a file with the name "filename".
    The file needs to be in the form
    x,y,class -> this is the first line of the file
    250,50,2
    250,150,1
    etc (where the specific numbers correspond to the specific feature)
    */
    public void readPointsFromCSV(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("x,y,class")) {
                    continue; // Skip header
                }
                String[] parts = line.split(",");
                double x = Double.parseDouble(parts[0].trim());
                double y = Double.parseDouble(parts[1].trim());
                int cls = Integer.parseInt(parts[2].trim());
                trainingData.add(new Sample(x, y, cls));
                System.out.println("Sto leggendo i punti.");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
public Sample findNearestNeighbor(Sample testPoint) {
    if (trainingData.isEmpty()) {
         System.out.println("training set vuoto");
       
        return new Sample(0, 0, 0); // Nessun dato di allenamento disponibile, restituisci un punto vuoto
    }
    
    Sample nearestNeighbor = trainingData.get(0); // Imposta il primo punto come punto più vicino iniziale
    double minDistance = testPoint.distance(nearestNeighbor); // Calcola la distanza dal primo punto
    
    // Cerca il punto più vicino
    for (Sample point : trainingData) {
        double distance = testPoint.distance(point);
        System.out.println("Distance to point (" + point.x + ", " + point.y + "): " + distance);
        if (distance < minDistance) {
            minDistance = distance;
            nearestNeighbor = point;
        }
    }
    
    return nearestNeighbor;
}
    
    public List<Sample> getTrainingData() {
        return trainingData;
    }
}
