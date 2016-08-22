/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import FunctionalityClasses.DataStorage;
import java.io.Serializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Panadmin
 */
public class Shiaijo implements Serializable {
    private transient StringProperty IdProperty = new SimpleStringProperty("");
    private String ID = "";
    
    public Shiaijo(String id)throws NullPointerException{
        if(!id.isEmpty() && id.length()<2){
            IdProperty.setValue(id);
            this.ID = id;
        }else{
            throw new NullPointerException("Empty ID");
            
        }
    }

    /**
     * @return the IdProperty
     */
    public String getIdProperty() {
        if(IdProperty==null){
            IdProperty= new SimpleStringProperty(ID);
        }
        return IdProperty.get();
    }

    /**
     * @param id the IdProperty to set
     */
    public void setIdProperty(String id) throws NullPointerException{
        if(!id.isEmpty() && id.length()<2){
            IdProperty.setValue(id);
            this.ID = id;
        }else{
            throw new NullPointerException("Empty ID");
        }
    } 

    /**
     * @param ID the ID to set
     */
    public void setID(String id) throws NullPointerException{
        if(!id.isEmpty() && id.length()<2){
            IdProperty.setValue(id);
            this.ID = id;
        }else{
            throw new NullPointerException("Empty ID");
        }
    }

    /**
     * @return the ID
     */
    public String getID() {
        return ID;
    }
}
