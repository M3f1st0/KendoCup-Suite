/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author panos
 */
public class Match implements Serializable{
    private Contestant white;
    private Contestant red;
    private transient StringProperty whiteToString = new SimpleStringProperty("");
    private transient StringProperty redToString = new SimpleStringProperty("");
    
    public Match(Contestant w, Contestant r){
        white = w;
        red = r;
    }

    /**
     * @return the white
     */
    public Contestant getWhite() {
        return white;
    }

    /**
     * @param white the white to set
     */
    public void setWhite(Contestant white) {
        this.white = white;
    }

    /**
     * @return the red
     */
    public Contestant getRed() {
        return red;
    }

    /**
     * @param red the red to set
     */
    public void setRed(Contestant red) {
        this.red = red;
    }
    
    public StringProperty whiteToStringProperty(){
        StringBuilder s = new StringBuilder();
        s.append(getWhite().getID()+". ");
        s.append(getWhite().getFirstName().charAt(0)+" ");
        s.append(getWhite().getLastName());

        whiteToString.setValue(s.toString());
        
        return whiteToString;

    }
    
    public StringProperty redToStringProperty(){
        
        StringBuilder s = new StringBuilder();
        s.append(getRed().getID()+". ");
        s.append(getRed().getFirstName().charAt(0)+" ");
        s.append(getRed().getLastName());

        redToString.setValue(s.toString());
        
        
        return redToString;

    }
    
    public Contestant getMatchWinner(){
        Contestant winner=null;
        if(white.sumIppons()> red.sumIppons()){
            winner = white;
        }else if(white.sumIppons()< red.sumIppons()){
            winner= red;
        }
        return winner;
    }
    
}
