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
 * @author panos
 */
public class Club implements Serializable {

    private transient StringProperty clubNameProperty;
    private transient StringProperty nationalityProperty;
    private String clubName;
    private String nationality;

    public Club(String cName, String nation) throws Exception {
        if (!cName.isEmpty() && !nation.isEmpty()) {
            this.clubName = cName;
            this.nationality = nation;
            clubNameProperty = new SimpleStringProperty(cName);
            nationalityProperty = new SimpleStringProperty(nation);
        } else {
            //DataStorage.getInstance().msgBox("Empty fields", false);
            throw new Exception("Empty fields in club creation are not allowed.");
        }
    }

    /**
     * @return the clubNameProperty
     */
    public String getClubNameProperty() {
        return clubNameProperty.get();
    }

    /**
     * @return the nationalityProperty
     */
    public String getNationalityProperty() {
        return nationalityProperty.get();
    }

    /**
     * @return the clubName
     */
    public String getClubName() {
        return clubName;
    }

    /**
     * @return the nationality
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * @param clubNameProperty the clubNameProperty to set
     */
    public void setClubNameProperty(String cName) throws Exception {
        if (cName.isEmpty()) {
            throw new Exception("The club name string is empty.");
        } else {
            clubNameProperty.setValue(cName);
            this.clubName = cName;
        }

    }

    /**
     * @param nationalityProperty the nationalityProperty to set
     */
    public void setNationalityProperty(String nation) throws Exception {
        if (nation.isEmpty()) {
            throw new Exception("The club nationality string is empty.");
        } else {
            nationalityProperty.setValue(nation);
            this.nationality = nation;
        }

    }

    /**
     * @param clubName the clubName to set
     */
    public void setClubName(String clubName) throws Exception {
        if (clubName.isEmpty()) {
            throw new Exception("The club name string is empty.");
        } else {
            clubNameProperty.setValue(clubName);
            this.clubName = clubName;
        }
    }

    /**
     * @param nationality the nationality to set
     */
    public void setNationality(String nationality) throws Exception {
        if (nationality.isEmpty()) {
            throw new Exception("The club nationality string is empty.");
        } else {
            nationalityProperty.setValue(nationality);
            this.nationality = nationality;
        }
    }

}
