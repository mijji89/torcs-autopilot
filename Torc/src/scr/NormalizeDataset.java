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
    private File[] trainingset = new File[3];
    String flof="AngleToTrackAxis;CurrentLapTime;Damage;DistanceFromStartLine;DistanceRaced;Speed;ZSpeed;Z;TrackEdgeSensors0;TrackEdgeSensor-90;TrackEdgeSensor+90;TarckEdgeSensor-50;TrackEdgeSensor+30;TrackPosition;action";
    /*Definizione dei vettori minimi e massimi (utili per la normalizzazione) */
	private final Double[] min={-0.496,-0.982,0.0,0.155,0.0,-30.443,-15.078,0.226,-1.0,-1.0,-1.0,-1.0,-1.0,-3.701};
	private final Double[] max={0.851,180.176,1.0,5783.960,11504.0,236.944,8.521,0.392,200.0,11.017,11.354,28.767,200.0,5.268};
    private File datasetNormalized= new File ("normalizedDataset.csv"); 
    private BufferedWriter bw; 

    /**
     * Costruttore che inizializza i riferimenti ai file di training raw e prepara il file di output per i dati normalizzati
     * @param f1 Primo file di training raw
     * @param f2 Secondo file di training raw
     * @param f3 Terzo file di training raw
     * @param f4 Quarto file di training raw
     * @param f5 Quinto file di training raw
     */

    public NormalizeDataset(File f1 , File f2, File f3){
        this.trainingset[0]=f1; 
        this.trainingset[1]=f2; 
        this.trainingset[2]=f3; 
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
                while ((line=bf.readLine()) != null){
                    if(!line.startsWith(flof)){
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
    }

    /**
     * Scrive su un file CSV i dati di interesse dopo la normalizzaizone e la classe dell'azione corrispondente
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
     * Metodo main che permette di generare il file "normalizzato" quando viene eseguito 
     */
    public static void main(String[] args){
        NormalizeDataset nd= new NormalizeDataset(  new File("../classes/dataset1Lap.csv"),new File("../classes/dataset2Lap.csv"),new File("../classes/dataset3Lap.csv"));
        nd.readFromCSV();
        System.out.println("Dataset prodotto!");
    }


}



