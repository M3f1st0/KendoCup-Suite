/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import FunctionalityClasses.DataStorage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author panos
 */
public class Team implements Serializable {

    private transient StringProperty teamNameProperty = new SimpleStringProperty();
    private transient IntegerProperty winsProperty = new SimpleIntegerProperty(0);
    private transient IntegerProperty losesProperty = new SimpleIntegerProperty(0);
    private transient IntegerProperty drawsProperty = new SimpleIntegerProperty(0);
    private transient StringProperty belongsToCupProperty = new SimpleStringProperty();
    private transient IntegerProperty minMembersProperty = new SimpleIntegerProperty();
    private transient IntegerProperty maxMembersProperty = new SimpleIntegerProperty();

    private String teamName = "";
    private int wins = 0;
    private int loses = 0;
    private int draws = 0;
    private String belongsToCup = "";
    private int minMembers;
    private int maxMebmers;
    private final int MAXIMUM_REGISTERED_MEMBERS;
    private int index = 0;

    private ArrayList<Contestant> teamMembers;

    //Constructor
    public Team(String tname, int min, int max) throws Exception {
        if (!tname.isEmpty() && min < max) {
            teamNameProperty.setValue(tname);
            this.teamName = tname;
            minMembersProperty.set(min);
            maxMembersProperty.set(max);
            minMembers = min;
            maxMebmers = max;
            MAXIMUM_REGISTERED_MEMBERS = max + 2;
            teamMembers = new ArrayList<>();

        } else {
            throw new Exception("Empty team name or minimum members > maximum members");
        }
    }

    /*
    ******************Setters**************************************************
     */
    public void setTeamNameProperty(String tname) throws Exception {
        if (!tname.isEmpty()) {
            teamNameProperty.setValue(tname);
            this.teamName = tname;
        } else {
            throw new Exception("Empty team name or minimum members > maximum members");
        }
    }

    public void setWinsProperty(int w) throws Exception {
        if (w >= 0) {
            winsProperty.setValue(w);
            wins = w;
        } else {
            throw new Exception("Wins cannot be negative");
        }
    }

    public void setLosesProperty(int l) throws Exception {
        if (l >= 0) {
            losesProperty.setValue(l);
            loses = l;
        } else {
            throw new Exception("Defeats cannot be negative");
        }
    }

    public void setDrawsProperty(int d) throws Exception {
        if (d >= 0) {
            drawsProperty.setValue(d);
            draws = d;
        } else {
            throw new Exception("wins cannot be negative");
        }
    }

    public void setTeamMembers(ArrayList<Contestant> teamMembersList) throws NullPointerException {
        if (teamMembersList.size() <= MAXIMUM_REGISTERED_MEMBERS) {
            this.teamMembers = teamMembersList;
        } else {
            throw new NullPointerException("Empty List");
        }

    }

    public void setBelongsToCupProperty(String cupName) throws NullPointerException {
        if (!cupName.isEmpty()) {
            belongsToCupProperty.setValue(cupName);
            this.belongsToCup = cupName;
        } else {
            throw new NullPointerException("Cup name is empty string");
        }

    }

    public void setTeamName(String teamName) throws Exception {
        if (!teamName.isEmpty()) {
            teamNameProperty.setValue(teamName);
            this.teamName = teamName;
        } else {
            throw new Exception("Empty team name or minimum members > maximum members");
        }

    }

    public void setWins(int w) throws NullPointerException {
        if (w >= 0) {
            winsProperty.setValue(w);
            wins = w;
        } else {
            throw new NullPointerException("wins cannot be negative");
        }
    }

    public void setLoses(int l) throws NullPointerException {
        if (l >= 0) {
            losesProperty.setValue(l);
            loses = l;
        } else {
            throw new NullPointerException("loses cannot be negative");
        }
    }

    public void setDraws(int d) throws NullPointerException {
        if (d >= 0) {
            drawsProperty.setValue(d);
            draws = d;
        } else {
            throw new NullPointerException("wins cannot be negative");
        }
    }

    public void setBelongsToCup(String cupname) throws NullPointerException{
        if (!cupname.isEmpty()) {
            belongsToCupProperty.setValue(cupname);
            this.belongsToCup = cupname;
        } else {
            throw new NullPointerException("Cup name is empty string");
        }

    }

    public void addToTeam(Contestant c) throws ArrayIndexOutOfBoundsException {
        if (teamMembers.size() < MAXIMUM_REGISTERED_MEMBERS) {
            teamMembers.add(c);
        } else {
            throw new ArrayIndexOutOfBoundsException("The team has reached its maximum number of members.");
        }
    }

    public void removeFromTeam(Contestant c) {
        teamMembers.remove(c);
    }

    /*
     *******************************Getters*************************************
     */
    public String getTeamNameProperty() {
        return teamNameProperty.get();
    }

    public int getWinsProperty() {
        return winsProperty.get();
    }

    public int getLosesProperty() {
        return losesProperty.get();
    }

    public int getDrawsProperty() {
        return drawsProperty.get();
    }

    public String getBelongsToCupProperty() {
        return belongsToCupProperty.get();
    }

    public String getTeamName() {
        return teamName;
    }

    public int getWins() {
        return wins;
    }

    public int getLoses() {
        return loses;
    }

    public int getDraws() {
        return draws;
    }

    public String getBelongsToCup() {
        return belongsToCup;
    }

    public ArrayList<Contestant> getTeamMembersAsList() {
        return teamMembers;
    }

    /**
     * @return the minMembersProperty
     */
    public int getMinMembersProperty() {
        return minMembersProperty.get();
    }

    /**
     * @return the maxMembersProperty
     */
    public int getMaxMembersProperty() {
        return maxMembersProperty.get();
    }

    /**
     * @return the minMembers
     */
    public int getMinMembers() {
        return minMembers;
    }

    /**
     * @return the maxMebmers
     */
    public int getMaxMebmers() {
        return maxMebmers;
    }

}
