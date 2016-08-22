/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Panagiotis Bitharis
 */
public class Pool implements Serializable{
    /*
    * Transient Instance Variables
    * These vaiables will not be save in the object file, they are not Serializables
    * We will use primitve Data Types for this matter
    */
    private transient StringProperty poolIDProperty = new SimpleStringProperty();
    
    
    private ArrayList<Match> matches = new ArrayList<>();
    private String poolID="";
    
    //Constructor
    public Pool(String ID)throws Exception{
        
        if(!ID.isEmpty() && ID.length()<=5){
            poolIDProperty.setValue(ID);
            poolID = ID;
        }else throw new Exception("illegal ID");
  
    }
    
    //Constructor
    public Pool(ArrayList<Match> m, String id){
        matches = m;
        poolIDProperty.setValue(id);
        poolID = id;
    }

    /**
     * @return the poolIDProperty
     */
    public String getPoolIDproperty() {
        if(poolIDProperty==null){
            poolIDProperty=new SimpleStringProperty(poolID);
        }
        return poolIDProperty.get();
    }

    /**
     * @param poolID the poolIDProperty to set
     */
    public void setPoolIDProperty(String poolID) {
        poolIDProperty.setValue(poolID);
        this.poolID = poolID;
    }

    /**
     * @return the matches
     */
    public ArrayList<Match> getMatches() {
        return matches;
    }
    
    public Contestant getPoolWinner(){
        Contestant poolWinner = null;
        poolWinner = matches.get(0).getMatchWinner();
        for(Match m:matches){
            if(poolWinner.getWins() < m.getMatchWinner().getWins()){
                poolWinner = m.getMatchWinner();
            }else if(poolWinner.getWins() == m.getMatchWinner().getWins()){
                if(poolWinner.sumIppons() < m.getMatchWinner().getWins()){
                    poolWinner = m.getMatchWinner();
                }
            }
        }
        return poolWinner;
    }

    /**
     * @return the poolID
     */
    public String getPoolID() {
        return poolID;
    }

    /**
     * @param poolID the poolID to set
     */
    public void setPoolID(String poolID) {
        this.poolID = poolID;
        poolIDProperty.setValue(this.poolID);
    }

    

    
    
    
}
