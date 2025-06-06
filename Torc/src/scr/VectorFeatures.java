package scr;

import java.util.ArrayList;

public class VectorFeatures {
    private double[] features; 
    private int actionKey; 

    public VectorFeatures(SensorModel sensors, int action){
        features[0]= sensors.getAngleToTrackAxis(); 
        features[1]=sensors.getCurrentLapTime(); 
        features[2]=sensors.getDamage(); 
        features[3]=sensors.getDistanceFromStartLine(); 
        features[4]=sensors.getDistanceRaced(); 
        features[5]=sensors.getSpeed(); 
        features[6]=sensors.getZSpeed(); //per le buche
        features[7]=sensors.getZ(); 
        //Distanza dalle linee di corsia
        features[8]=sensors.getTrackEdgeSensors()[9];//0°
        features[9]=sensors.getTrackEdgeSensors()[0];//-90°
        features[10]=sensors.getTrackEdgeSensors()[18]; //+90°
        features[11]=sensors.getTrackEdgeSensors()[4]; //-50°
        features[12]=sensors.getTrackEdgeSensors()[12]; //+30°
        //Distanza tra l'auto e l'asse della pista
        //0 sono al centro della pista, se sono destra -1, se sono a sinistra +1
        features[13]=sensors.getTrackPosition(); 
        this.actionKey=action; 

       
    }

     @Override
    public String toString(){
        return this.features[0]+";"+this.features[1]+";"+this.features[2]+";"+this.features[3]+";"+this.features[4]+this.features[5]+";"+this.features[6]+";"+this.features[7]+";"+this.features[8]+";"+this.features[9]+this.features[10]+";"+this.features[11]+";"+this.features[12]+";"+this.features[13]+";"+this.actionKey;

    }
    
}
