package scr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *  Classe che ha lo scopo di costruire un training set normalizzato, dove i dati presi dai sensori vengono normalizzati
 * mediante MinMax Scaling. 
 * 
 *La classe contiene: 
 * -  un riferimento ai "raw file" contenenti gli input-output usati in fase di training.
 * - due vettori di Double in cui vengono memorizzati i valori massimi e minimi per ciascun parametro dei sensori
 * - un File in cui verranno inseriti i dati normalizzati
 * - un BufferedWriter per scrivere sul file
 * - una stringa contenente la riga di intestazione da saltare quando si leggono i training dataset "raw" 
 */
public class NormalizeDataset {
    private File[] trainingset = new File[1];
    String flof="AngleToTrackAxis;CurrentLapTime;Damage;DistanceFromStartLine;DistanceRaced;Speed;ZSpeed;Z;TrackEdgeSensors0;TrackEdgeSensor-90;TrackEdgeSensor+90;TarckEdgeSensor-50;TrackEdgeSensor+30;TrackPosition;action";
    /*Definizione dei vettori minimi e massimi (utili per la normalizzazione) */
    private final Double[] min={-(Math.PI),-0.982,0.0,0.0,0.0,0.00,-9.6,0.247,-1.0,-1.0,-1.0,-1.0,-1.0,-0.97};
	private final Double[] max={+(Math.PI),182.98,0.0,5784.10,11513.00,160.0,6.9,0.455,200.0,200.00,200.00,200.0,200.0,1.56};
    private File datasetNormalized= new File ("normalizedDataset.csv"); 
    private BufferedWriter bw; 

    /**
     * Costruttore che inizializza i riferimenti ai file di training raw e prepara il file di output per i dati normalizzati
     * @param f1 Primo file di training raw
     * @param f2 Secondo file di training raw
     * @param f3 Terzo file di training raw
     */

    public NormalizeDataset(File f1){
        this.trainingset[0]=f1; 

        if(datasetNormalized.exists()){
            try {
                this.bw = new BufferedWriter(new FileWriter(datasetNormalized,true));
				this.bw.append("AngleToTrackAxis;Speed;TrackEdgeSensors0;TrackEdgeSensor-90;TrackEdgeSensor+90;TarckEdgeSensor-50;TrackEdgeSensor+30;TrackPosition;action");
                this.bw.write("\n");
            } catch (IOException ex) {
            }
        }
        else{
            try{
				this.bw = new BufferedWriter(new FileWriter(datasetNormalized));
				this.bw.write("AngleToTrackAxis;Speed;TrackEdgeSensors0;TrackEdgeSensor-90;TrackEdgeSensor+90;TarckEdgeSensor-50;TrackEdgeSensor+30;TrackPosition;action");
				this.bw.write("\n");
			}catch(IOException ex){
				System.err.println();
			}

        }
    }

    /**
     * Legge i dati dal training set "raw", li normalizza tramite il MinMax scaling e li scrive su un nuovo file CSV di output
     */
    public void readFromCSV(){
        for(int i=0; i<trainingset.length;i++ ){
            try{
                BufferedReader bf= new BufferedReader(new FileReader(trainingset[i]));
                String line; 
                VectorFeatures vf; 
                while ((line=bf.readLine()) != null ){
                    if(!line.startsWith(flof) && !line.trim().isEmpty()){
                        vf= new VectorFeatures(line);
                        Double[] vfn= vf.normalizeMinMax(this.min, this.max);
                        vf.setFeatures(vfn[0],vfn[5], vfn[8], vfn[9], vfn[10], vfn[11],vfn[12],vfn[13]);
                        writeCSV(vf);
                    }
                    else{
                        continue; 
                    }

                }
            }catch(IOException ex){
                System.err.println(ex);
            }
        }
        try{
            this.bw.close();
        }catch(IOException ex){
                    System.err.println(ex);
        }
    }



    /**
     * Scrive su un file CSV i dati di interesse dopo la normalizzazione e la classe dell'azione corrispondente
     * 
     * @param vf Oggetto VectorFeatures contenente i dati da scrivere
     */
    public void writeCSV(VectorFeatures vf){
        try{
            this.bw.append(vf.getFeatures()[0]+";"+vf.getFeatures()[5]+";"+ vf.getFeatures()[8]+";"+ vf.getFeatures()[9]+";"+vf.getFeatures()[10]+";"+ vf.getFeatures()[11]+";"+vf.getFeatures()[12]+";"+vf.getFeatures()[13]+";"+vf.getActionKey());
            this.bw.append('\n');
        }catch(IOException ex){
            System.err.println(ex);
        }
    }

    /**
     * Metodo close per chiudere il buffered writer
     */
    public void close() {
    if (bw != null) {
        try {
            this.bw.flush();
            this.bw.close();
        } catch (IOException e) {
            System.err.println("Errore chiusura BufferedWriter: " + e.getMessage());
        }
        }
    }


    /**
     * Metodo main che permette di generare il file "normalizzato" quando viene eseguito 
     */
    public static void main(String[] args){
        NormalizeDataset nd= new NormalizeDataset( new File("../classes/datasetLap.csv"));
        nd.readFromCSV();
        System.out.println("Dataset prodotto!");
    }


}



