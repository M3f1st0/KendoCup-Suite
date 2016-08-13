/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import FunctionalityClasses.DataStorage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.InputMismatchException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author panos
 */
public class Cup implements Serializable {
    /*
     * Transient Instance Variables
     * These vaiables will not be saved in the object file, they do not impement Serializable.
     * They will be used to feed the TableView in the UI
     * For every Property a primitve Data Types will be used and saved in the object file *trm
     */
    private transient StringProperty cupnameProperty; //The name of a Cup but also the database name
    private transient IntegerProperty numberOfPoolsProperty; //How many Pools each Cup will be consisted of
    private transient IntegerProperty prefPlayersProperty;  //The preferable number of players per Pool
    private transient IntegerProperty maxPlayersProperty;  //The maximum number of players per Pool
    private transient IntegerProperty numberOfShiaiJosProperty; //The number of ShiaiJo (places where kendo matches are held)
    private transient BooleanProperty isATeamChampionshipProperty = new SimpleBooleanProperty(false); //This variable will be used as a flag to check ig this cup will be a Team championship
    private transient BooleanProperty includedInParticipationList
            = new SimpleBooleanProperty(false); //This vairiable will me used with the checkBoxes in TitlePaneThree (Register Players) in TournamentSetUpView.fxml
    //And will monitor if the contestant will participate in the particular cup.
    //As contestant may participate in more than one cup
    //All cups will be listed in a Table View
    //And the user can click the check boxes of the Cups that the contestant will participate.


    /*
     * Primitive Data Types
     */
    private String cupName = ""; //the database name
    private int numberOfPools = 0;
    private int prefPlayers = 0;
    private int maxPlayers = 0;
    private int numberOfShiaiJos = 0;
    private boolean isATeamChampionship = false;
    private static int teamChampionshipsSelected=0; //this instance variable keeps count how many team competitions are selected inside the 
                                                    //cupSelectorTableView in TournamentSetUpController class. 
                                                    //A player can only participate in ONE team championship
                                                    //therefore this variable should be always 1
                                                    //if a second team championship is selected from the lisy
                                                    //this variable will be incremented and a warning message will be show to the user.

    private ArrayList<Shiaijo> shiaijos = new ArrayList<>();
    private ArrayList<Pool> pools = new ArrayList<>();
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<Contestant> contestants = new ArrayList<>();
    private ArrayList<Match> matches = new ArrayList<>();
    private ArrayList<Club> clubs = new ArrayList<>();

    //Constructor
    public Cup(String cupname, int pref, int max) throws Exception{

        numberOfPoolsProperty = new SimpleIntegerProperty(0);
        numberOfShiaiJosProperty = new SimpleIntegerProperty(0);
        if (!cupname.isEmpty()) {
            cupnameProperty = new SimpleStringProperty(cupname);
            this.cupName = cupname;
        } else {
            throw new Exception("Cup name cannot be empty!");
        }

        if (pref > 2 && pref <= max) {
            prefPlayersProperty = new SimpleIntegerProperty(pref);
            this.prefPlayers = pref;
        } else {
            throw new Exception("The preferred players must always be smaller or equal to maximum players");
        }

        if (max >= pref) {
            maxPlayersProperty = new SimpleIntegerProperty(max);
            this.maxPlayers = max;
        } else {
            throw new Exception("The preferred players must always be smaller or equal to maximum players");
        }
        this.includedInParticipationList.addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                System.out.println(getCupName() + " Included in the Cup : " + t1);
                if(includedInParticipationList.get() && isATeamChampionship){
                    teamChampionshipsSelected++;
                    if(getTeamChampionshipsSelected()>=2){
//                        DataStorage.getInstance().msgBox("ATTENTION! You have selected more than one Team-Competitions.", false);
                          throw new IllegalArgumentException("ATTENTION! You have selected more than one Team-Competitions.");
                    }
                    System.out.println("Number of team cups selected "+getTeamChampionshipsSelected());
                }else if(!includedInParticipationList.get()&& isATeamChampionship){
                    teamChampionshipsSelected--;
                    if(getTeamChampionshipsSelected()>=2){
//                        DataStorage.getInstance().msgBox("ATTENTION! You have selected more than one Team-Competitions.", false);
                        throw new IllegalArgumentException("ATTENTION! You have selected more than one Team-Competitions.");
                    }
                    System.out.println("Number of team cups selected "+getTeamChampionshipsSelected());
                }
            }

        });
    }

    public Cup(String cupname, int pref, int max, int numberOfPools, int numOfShiaijos) throws  Exception{

        if (!cupname.isEmpty()) {
            cupnameProperty = new SimpleStringProperty(cupname);
            this.cupName = cupname;
        } else {
            throw new Exception("Cup name cannot be empty!");
        }

        if (pref > 2 && pref <= max) {
            prefPlayersProperty = new SimpleIntegerProperty(pref);
            this.prefPlayers = pref;
        } else {
            throw new Exception("The preferred players must always be smaller or equal to maximum players");
        }

        if (max >= pref) {
            maxPlayersProperty = new SimpleIntegerProperty(max);
            this.maxPlayers = max;
        } else {
            throw new Exception("The preferred players must always be smaller or equal to maximum players");
        }

        if (numberOfPools >= 1) {
            for (int i = 1; i <= numberOfPools; i++) {
                String ID = "P.";
                ID = ID + i;
                pools.add(new Pool(ID));
            }
            numberOfPoolsProperty = new SimpleIntegerProperty(pools.size());
            this.numberOfPools = pools.size();

        } else {
            throw new Exception("Pool number must be greater than zero ");
        }

        if (numOfShiaijos >= 1) {
            for (int i = 1; i <= numOfShiaijos; i++) {
                String ID = "";
                char c = (char) (i + 64);
                ID = ID + c;
                shiaijos.add(new Shiaijo(ID));
            }
            numberOfShiaiJosProperty = new SimpleIntegerProperty(shiaijos.size());
            this.numberOfShiaiJos = shiaijos.size();
        } else {
            throw new Exception("Shiaijo number must be greater than zero ");
        }
        this.includedInParticipationList.addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                System.out.println(getCupName() + " Included in the Cup : " + t1);
                if(includedInParticipationList.get() && isATeamChampionship){
                    teamChampionshipsSelected++;
                    if(getTeamChampionshipsSelected()>=2){
//                        DataStorage.getInstance().msgBox("ATTENTION! You have selected more than one Team-Competitions.", false);
                        throw new IllegalArgumentException("ATTENTION! You have selected more than one Team-Competitions.");
                    }
                    System.out.println("Number of team cups selected "+getTeamChampionshipsSelected());
                }else if(!includedInParticipationList.get()&& isATeamChampionship){
                    teamChampionshipsSelected--;
                    if(getTeamChampionshipsSelected()>=2){
//                        DataStorage.getInstance().msgBox("ATTENTION! You have selected more than one Team-Competitions.", false);
                        throw new IllegalArgumentException("ATTENTION! You have selected more than one Team-Competitions.");
                    }
                    System.out.println("Number of team cups selected "+getTeamChampionshipsSelected());
                }
            }

        });

    }

    /*
     * Setter methods************************************************************
     */
    /**
     * @param cupnameProperty the cupnameProperty to set
     */
    public void setCupnameProperty(String cupname) throws  Exception{
        if (!cupname.isEmpty()) {
            cupnameProperty.setValue(cupname);
            this.cupName = cupname;
        } else {
            throw new Exception("Cup name cannot be empty!");
        }
    }

    /**
     * @param numberOfPoolsProperty the numberOfPoolsProperty to set
     */
    private void setNumberOfPoolsProperty(int nop) throws Exception{
        pools.clear();
        if (nop >= 1) {
            for (int i = 1; i <= nop; i++) {
                String ID = "P.";
                ID = ID + i;
                pools.add(new Pool(ID));
            }
            numberOfPoolsProperty.setValue(pools.size());
            numberOfPools = pools.size();
        } else {
            throw new Exception("Pool number must be greater than zero ");
        }

    }

    /**
     * @param prefPlayersProperty the prefPlayersProperty to set
     */
    public void setPrefPlayersProperty(int prefPlayers) throws Exception{
        if (prefPlayers > 2 && prefPlayers <= maxPlayers) {
            prefPlayersProperty.set(prefPlayers);
            this.prefPlayers = prefPlayers;
        } else {
            throw new Exception("The preferred players must always be smaller or equal to maximum players");
        }

    }

    /**
     * @param maxPlayersProperty the maxPlayersProperty to set
     */
    public void setMaxPlayersProperty(int maxplayers) throws Exception{
        if (maxplayers >= prefPlayers) {
            maxPlayersProperty.set(maxplayers);
            this.maxPlayers = maxplayers;
        } else {
            throw new Exception("The preferred players must always be smaller or equal to maximum players");
        }

    }

    /**
     * @param numberOfShiaiJosProperty the numberOfShiaiJosProperty to set Each
     * time a new number is defined the arraylist is cleared and repopulated.
     */
    public void setNumberOfShiaiJosProperty(int noShiajo) throws Exception{
        shiaijos.clear();
        if (noShiajo >= 1) {
            for (int i = 1; i <= noShiajo; i++) {
                String ID = "";
                char c = (char) (i + 64);
                ID = ID + c;
                shiaijos.add(new Shiaijo(ID));
            }
            numberOfShiaiJosProperty.set(shiaijos.size());
            numberOfShiaiJos = shiaijos.size();
        } else {
            throw new Exception("Shiaijo number must be greater than zero ");
        }

    }

    /**
     * @param cupName the cupName to set
     */
    public void setCupName(String cupname) throws Exception{
        if (!cupname.isEmpty()) {
            cupnameProperty.setValue(cupname);
            this.cupName = cupname;
        } else {
            throw new Exception("Cup name cannot be empty!");
        }
    }

    /**
     * @param numberOfPools the numberOfPools to set
     */
    public void setNumberOfPools(int nop) throws Exception{
        pools.clear();
        if (nop >= 1) {
            for (int i = 1; i <= nop; i++) {
                String ID = "P.";
                ID = ID + i;
                pools.add(new Pool(ID));
            }
            numberOfPoolsProperty.setValue(pools.size());
            numberOfPools = pools.size();
        } else {
            throw new Exception("Pool number must be greater than zero ");
        }
    }

    /**
     * @param prefPlayers the prefPlayers to set
     */
    public void setPrefPlayers(int prefPlayers) throws Exception{
        if (prefPlayers > 2 && prefPlayers <= maxPlayers) {
            prefPlayersProperty.set(prefPlayers);
            this.prefPlayers = prefPlayers;
        } else {
            throw new Exception("The preferred players must always be smaller or equal to maximum players");
        }
    }

    /**
     * @param maxPlayers the maxPlayers to set
     */
    public void setMaxPlayers(int maxplayers) throws Exception{
        if (maxplayers >= prefPlayers) {
            maxPlayersProperty.set(maxplayers);
            this.maxPlayers = maxplayers;
        } else {
            throw new Exception("The preferred players must always be smaller or equal to maximum players");
        }
    }

    /**
     * @param numberOfShiaiJos the numberOfShiaiJos to set Each time a new
     * number is defined the arraylist is cleared and repopulated.
     */
    public void setNumberOfShiaiJos(int noShiajo) throws Exception{
        shiaijos.clear();
        if (noShiajo >= 1) {
            for (int i = 1; i <= noShiajo; i++) {
                String ID = "";
                char c = (char) (i + 64);
                ID = ID + c;
                shiaijos.add(new Shiaijo(ID));
            }
            numberOfShiaiJosProperty.set(shiaijos.size());
            numberOfShiaiJos = shiaijos.size();
        } else {
            throw new Exception("Shiajo number must be greater than zero ");
        }
    }

    /**
     * @param shiaijos the shiaijos to set
     */
    public void setShiaijos(ArrayList<Shiaijo> shiaijos) throws Exception{
        if (shiaijos.size() > 0 && !shiaijos.isEmpty()) {
            this.shiaijos = shiaijos;
        } else {
            throw new Exception("ShiaiJo Empty List");
        }

    }

    /**
     * @param pools the pools to set
     */
    public void setPools(ArrayList<Pool> pools) throws Exception{
        if (pools.size() > 0 && !pools.isEmpty()) {
            this.pools = pools;
        } else {
            throw new Exception("Pools Empty List");
        }

    }

    /**
     * @param teams the teams to set
     */
    public void setTeams(ArrayList<Team> teams) throws Exception{
        if (teams.size() > 0 && !teams.isEmpty()) {
            this.teams = teams;
        } else {
            throw new Exception("Teams Empty List");
        }

    }

    /**
     * @param contestants the contestants to set
     */
    public void setContestants(ArrayList<Contestant> contestants) throws Exception{
        if (contestants.size() > 0 && !contestants.isEmpty()) {
            this.contestants = contestants;
        } else {
            throw new Exception("Contestants Empty List");
        }
    }

    /**
     * @param matches the matches to set
     */
    public void setMatches(ArrayList<Match> matches) throws Exception{
        if (matches.size() > 0 && !matches.isEmpty()) {
            this.matches = matches;
        } else {
            throw new Exception("Mathces Empty List");
        }
    }

    /**
     * @param clubs the clubs to set
     */
    public void setClubs(ArrayList<Club> clubs) throws Exception{
        if (clubs.size() > 0 && !clubs.isEmpty()) {
            this.clubs = clubs;
        } else {
            throw new Exception("Clubs Empty List");
        }
        
    }

    /**
     * @param isATeamChampionship the isATeamChampionship to set
     */
    public void setIsATeamChampionship(boolean isATeamChampionship) {
        this.isATeamChampionshipProperty.set(isATeamChampionship);
        this.isATeamChampionship = isATeamChampionship;
    }

    /**
     * @param isATeamChampionshipProperty the isATeamChampionshipProperty to set
     */
    public void setIsATeamChampionshipProperty(boolean isATeamChampionship) {
        this.isATeamChampionshipProperty.set(isATeamChampionship);
        this.isATeamChampionship = isATeamChampionship;
    }

    /*
     ***************************************************************************
     */
 /*
     *Getter methods ************************************************************
     */
    public BooleanProperty includedInParticipationListProperty() {
        return includedInParticipationList;
    }

    /**
     * @return the cupnameProperty
     */
    public String getCupnameProperty() {
        return cupnameProperty.get();
    }

    /**
     * @return the numberOfPoolsProperty
     */
    public int getNumberOfPoolsProperty() {
        return numberOfPoolsProperty.get();
    }

    /**
     * @return the prefPlayersProperty
     */
    public int getPrefPlayersProperty() {
        return prefPlayersProperty.get();
    }

    /**
     * @return the maxPlayersProperty
     */
    public int getMaxPlayersProperty() {
        return maxPlayersProperty.get();
    }

    /**
     * @return the numberOfShiaiJosProperty
     */
    public int getNumberOfShiaiJosProperty() {
        return numberOfShiaiJosProperty.get();
    }

    /**
     * @return the cupName
     */
    public String getCupName() {
        return cupName;
    }

    /**
     * @return the numberOfPools
     */
    public int getNumberOfPools() {
        return numberOfPools;
    }

    /**
     * @return the prefPlayers
     */
    public int getPrefPlayers() {
        return prefPlayers;
    }

    /**
     * @return the maxPlayers
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * @return the numberOfShiaiJos
     */
    public int getNumberOfShiaiJos() {
        return numberOfShiaiJos;
    }

    /**
     * @return the shiaijos
     */
    public ArrayList<Shiaijo> getShiaijos() {
        return shiaijos;
    }

    /**
     * @return the pools
     */
    public ArrayList<Pool> getPools() {
        return pools;
    }

    /**
     * @return the teams
     */
    public ArrayList<Team> getTeams() {
        return teams;
    }

    /**
     * @return the contestants
     */
    public ArrayList<Contestant> getContestants() {
        return contestants;
    }

    /**
     * @return the matches
     */
    public ArrayList<Match> getMatches() {
        return matches;
    }

    /**
     * @return the clubs
     */
    public ArrayList<Club> getClubs() {
        return clubs;
    }
    
     /**
     * @return the isATeamChampionshipProperty
     */
    public boolean getIsATeamChampionshipProperty() {
        return isATeamChampionshipProperty.get();
    }

    /**
     * @return the isATeamChampionship
     */
    public boolean isIsATeamChampionship() {
        return isATeamChampionship;
    }
    
    /**
     * @return the teamChampionshipsSelected
     */
    public static int getTeamChampionshipsSelected() {
        return teamChampionshipsSelected;
    }

    /*
     ****************************************************************************
     */
   

}
