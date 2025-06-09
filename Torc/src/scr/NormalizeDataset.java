package scr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class NormalizeDataset {
    private File[] trainingset = new File[4];
    String flof="AngleToTrackAxis;CurrentLapTime;Damage;DistanceFromStartLine;DistanceRaced;Speed;ZSpeed;Z;TrackEdgeSensors0;TrackEdgeSensor-90;TrackEdgeSensor+90;TarckEdgeSensor-50;TrackEdgeSensor+30;TrackPosition;action";
    /*Definizione dei vettori minimi e massimi (utili per la normalizzazione) */
	private final Double[] min={-(Math.PI),-0.982,0.0,0.147,0.0,-89.409,-12.224,0.235,-1.0,-1.0,-1.0,-1.0,-1.0,-4.932};
	private final Double[] max={+(Math.PI),280.206,712.0,5784.10,5739.24,233.022,9.581,0.422,200.0,200.0,200.0,200.0,200.0,8.493 };
    private File datasetNormalized= new File ("normalizedDataset.csv"); 
    private BufferedWriter bw; 

    public NormalizeDataset(File f1, File f2, File f3, File f4){
        this.trainingset[0]=f1; 
        this.trainingset[1]=f2; 
        this.trainingset[2]=f3; 
        this.trainingset[3]=f4;
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

    public void writeCSV(VectorFeatures vf){
        try{
            this.bw.append(vf.getFeatures()[0]+";"+vf.getFeatures()[5]+";"+ vf.getFeatures()[8]+";"+ vf.getFeatures()[9]+";"+vf.getFeatures()[10]+";"+ vf.getFeatures()[11]+";"+vf.getFeatures()[12]+";"+vf.getFeatures()[13]+";"+vf.getActionKey());
            this.bw.append('\n');
        }catch(IOException ex){
            System.err.println(ex);
        }
    }

    public static void main(String[] args){
        NormalizeDataset nd= new NormalizeDataset(new File("C:\\Users\\Benedetta\\Desktop\\PROGETTO AI\\ProgettoIA\\Torc\\classes\\datasetBet.csv"), new File("C:\\Users\\Benedetta\\Desktop\\PROGETTO AI\\ProgettoIA\\Torc\\classes\\datasetMic.csv"),new File("C:\\Users\\Benedetta\\Desktop\\PROGETTO AI\\ProgettoIA\\Torc\\classes\\datasetReb.csv"), new File("C:\\Users\\Benedetta\\Desktop\\PROGETTO AI\\ProgettoIA\\Torc\\classes\\datasetAndre.csv"));
        nd.readFromCSV();
        System.out.println("Dataset prodotto!");
    }


}



