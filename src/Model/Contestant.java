/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Panadmin
 */
public class Contestant implements Serializable {

    private transient StringProperty firstNameProperty = new SimpleStringProperty();
    private transient StringProperty lastNameProperty = new SimpleStringProperty();
    private transient StringProperty roundProperty = new SimpleStringProperty();
    private transient StringProperty gradeTypeProperty = new SimpleStringProperty();
    private transient StringProperty gradeLevelProperty = new SimpleStringProperty();
    private transient StringProperty genderProperty = new SimpleStringProperty();
    private transient StringProperty clubNameProperty = new SimpleStringProperty();
    private transient StringProperty belongsToTeamProperty = new SimpleStringProperty();
    private transient StringProperty belongsToCupProperty = new SimpleStringProperty();

    private transient IntegerProperty IDProperty = new SimpleIntegerProperty();

    private transient IntegerProperty winsProperty = new SimpleIntegerProperty(),
            drawsProperty = new SimpleIntegerProperty(0),
            losesProperty = new SimpleIntegerProperty(0),
            matchTotalIpponsProperty = new SimpleIntegerProperty(0);

    private transient IntegerProperty menProperty = new SimpleIntegerProperty(0),
            koteProperty = new SimpleIntegerProperty(0),
            douProperty = new SimpleIntegerProperty(0),
            tsukiProperty = new SimpleIntegerProperty(0),
            ipponProperty = new SimpleIntegerProperty(0),
            hansokuProperty = new SimpleIntegerProperty(0);
    private transient StringProperty colorProperty = new SimpleStringProperty();

    private String firstName;
    private String lastName;
    private String round;
    private String gradeType;
    private String gradeLevel;
    private String gender;
    private String clubName;
    private String nationality;
    private String belongsToTeam;
    private String belongsToCup;
    private int ID;
    private int wins, draws, loses, matchTotalIppons;
    private int men, kote, dou, tsuki, ippon, hansoku;
    private transient String color;

    public Contestant() {
    }

    public Contestant(String fname, String lname, String col, int id) throws Exception {
        if (!fname.isEmpty() && !lname.isEmpty() && !col.isEmpty() && id >= 0) {
            firstName = fname;
            firstNameProperty.setValue(fname);

            lastName = lname;
            lastNameProperty.setValue(lname);

            this.color = col;
            colorProperty.setValue(col);

            ID = id;
            IDProperty.setValue(id);
        } else {
            throw new Exception("Empty Fields in player creation are not allowed.");
        }
    }

    public Contestant(String fname, String lname, String grType, String grLvl,
            String gend, String clubname, String team) throws Exception {

        if (!fname.isEmpty() && !lname.isEmpty() && !gend.isEmpty() && !grType.isEmpty() && !grLvl.isEmpty()
                && !clubname.isEmpty()) {
            firstName = fname;
            firstNameProperty.setValue(fname);

            lastName = lname;
            lastNameProperty.setValue(lname);

            gradeType = grType;
            gradeTypeProperty.setValue(grType);

            gradeLevel = grLvl;
            gradeLevelProperty.setValue(grLvl);

            gender = gend;
            genderProperty.setValue(gend);

            this.clubName = clubname;
            clubNameProperty.setValue(clubname);

            belongsToTeam = team;
            belongsToTeamProperty.setValue(team);
        } else {
            throw new Exception("Empty Fields in player creation are not allowed.");
        }
    }

    public Contestant(String fname, String lname, String grType, String grLvl,
            String gend, String clubname) throws Exception {

        if (!fname.isEmpty() && !lname.isEmpty() && !gend.isEmpty() && !grType.isEmpty() && !grLvl.isEmpty()
                && !clubname.isEmpty()) {
            firstName = fname;
            firstNameProperty.setValue(fname);

            lastName = lname;
            lastNameProperty.setValue(lname);

            gradeType = grType;
            gradeTypeProperty.setValue(grType);

            gradeLevel = grLvl;
            gradeLevelProperty.setValue(grLvl);

            gender = gend;
            genderProperty.setValue(gend);

            this.clubName = clubname;
            clubNameProperty.setValue(clubname);

        } else {
            throw new Exception("Empty Fields in player creation are not allowed.");
        }
    }

    /*
    ********************** Getters *********************************************
     */
    public int sumIppons() {
        matchTotalIpponsProperty.set(menProperty.getValue() + koteProperty.getValue() + douProperty.getValue() + tsukiProperty.getValue() + ipponProperty.getValue());
        return matchTotalIpponsProperty.get();
    }

    public String getFirstNameProperty() {
        return firstNameProperty.get();
    }

    public String getLastNameProperty() {
        return lastNameProperty.get();
    }

    public String getRoundProperty() {
        return roundProperty.get();
    }

    public IntegerProperty getIDProperty() {
        return IDProperty;
    }

    public int getWinsProperty() {
        return winsProperty.get();
    }

    public int getDrawsProperty() {
        return drawsProperty.get();
    }

    public int getLosesProperty() {
        return losesProperty.get();
    }

    public int getMatchTotalIpponsProperty() {
        return matchTotalIpponsProperty.get();
    }

    public int getMenProperty() {
        return menProperty.get();
    }

    public int getKoteProperty() {
        return koteProperty.get();
    }

    public int getDouProperty() {
        return douProperty.get();
    }

    public int getTsukiProperty() {
        return tsukiProperty.get();
    }

    public int getIpponProperty() {
        return ipponProperty.get();
    }

    public int getHansokuProperty() {
        return hansokuProperty.get();
    }

    public String getColorProperty() {
        return colorProperty.get();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRound() {
        return round;
    }

    public int getID() {
        return ID;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLoses() {
        return loses;
    }

    public int getMatchTotalIppons() {
        return matchTotalIppons;
    }

    public int getMen() {
        return men;
    }

    public int getKote() {
        return kote;
    }

    public int getDou() {
        return dou;
    }

    public int getTsuki() {
        return tsuki;
    }

    public int getIppon() {
        return ippon;
    }

    public int getHansoku() {
        return hansoku;
    }

    public String getColor() {
        return color;
    }

    public String getGradeTypeProperty() {
        return gradeTypeProperty.get();
    }

    public String getGradeLevelProperty() {
        return gradeLevelProperty.get();
    }

    public String getGenderProperty() {
        return genderProperty.get();
    }

    public String getClubNameProperty() {
        return clubNameProperty.get();
    }

    public String getBelongsToTeamProperty() {
        return belongsToTeamProperty.get();
    }

    public String getGradeType() {
        return gradeType;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public String getGender() {
        return gender;
    }

    public String getClubName() {
        return clubName;
    }

    public String getNationality() {
        return nationality;
    }

    public String getBelongsToTeam() {
        return belongsToTeam;
    }

    public String getBelongsToCupProperty() {
        return belongsToCupProperty.get();
    }

    public String getBelongsToCup() {
        return belongsToCup;
    }

    /*
    ********************* Setters **********************************************
     */
    public void setFirstNameProperty(String firstName) throws Exception {
        if (firstName.isEmpty()) {
            throw new Exception("First name field is empty.");
        } else {
            this.firstNameProperty.setValue(firstName);
            this.firstName = firstName;
        }

    }

    public void setLastNameProperty(String lastName) throws Exception {
        if (lastName.isEmpty()) {
            throw new Exception("Last name field is empty.");
        } else {
            this.lastNameProperty.setValue(lastName);
            this.lastName = lastName;
        }

    }

    public void setRoundProperty(String round) throws Exception {
        if (round.isEmpty()) {
            throw new Exception(" 'Round' field is empty.");
        } else {
            this.roundProperty.setValue(round);
            this.round = round;
        }
    }

    public void setGradeTypeProperty(String gradeType) throws Exception {
        if (gradeType.isEmpty()) {
            throw new Exception(" Grade type field is empty.");
        } else {
            this.gradeTypeProperty.setValue(gradeType);
            this.gradeType = gradeType;
        }

    }

    public void setGradeLevelProperty(String gradeLevel) throws Exception {
        if (gradeLevel.isEmpty()) {
            throw new Exception(" Grade level field is empty.");
        } else {
            this.gradeLevelProperty.setValue(gradeLevel);
            this.gradeLevel = gradeLevel;
        }

    }

    public void setGenderProperty(String gender) throws Exception {
        if (gender.isEmpty()) {
            throw new Exception(" Gender field is empty.");
        } else {
            this.genderProperty.setValue(gender);
            this.gender = gender;
        }

    }

    public void setClubNameProperty(String clubName) throws Exception {
        if (clubName.isEmpty()) {
            throw new Exception(" Gender field is empty.");
        } else {
            this.clubNameProperty.setValue(clubName);
            this.clubName = clubName;
        }

    }

    public void setBelongsToTeamProperty(String belongsToTeam) throws Exception {
        if (belongsToTeam.isEmpty()) {
            throw new Exception(" The player is not a member of a team.");
        } else {
            this.belongsToTeamProperty.setValue(belongsToTeam);
            this.belongsToTeam = belongsToTeam;
        }

    }

    public void setIDProperty(int ID) throws Exception {
        if (ID < 0) {
            throw new Exception(" Invalid ID");
        } else {
            this.IDProperty.set(ID);
            this.ID = ID;
        }

    }

    public void setWinsProperty(int w) throws Exception {
        if (w < 0) {
            throw new Exception(" Wins number is a negative number.");
        } else {
            this.winsProperty.set(w);
            this.wins = w;
        }

    }

    public void setDrawsProperty(int d) throws Exception {
        if (d < 0) {
            throw new Exception(" Draws number is a negative number.");
        } else {
            this.drawsProperty.set(d);
            this.draws = d;
        }

    }

    public void setLosesProperty(int l) throws Exception {
        if (l < 0) {
            throw new Exception(" Defeat number is a negative number.");
        } else {
            this.losesProperty.set(l);
            this.loses = l;
        }
    }

    public void setMenProperty(int men) throws Exception {
        if (men < 0) {
            throw new Exception(" Men number is a negative number.");
        } else {
            this.menProperty.set(men);
            this.men = men;
        }

    }

    public void setKoteProperty(int kote) throws Exception {
        if (kote < 0) {
            throw new Exception(" Kote number is a negative number.");
        } else {
            this.koteProperty.set(kote);
            this.kote = kote;
        }

    }

    public void setDouProperty(int dou) throws Exception {
        if (dou < 0) {
            throw new Exception(" Dou number is a negative number.");
        } else {
            this.douProperty.set(dou);
            this.dou = dou;
        }

    }

    public void setTsukiProperty(int tsuki) throws Exception {
        if (tsuki < 0) {
            throw new Exception(" Tsuki number is a negative number.");
        } else {
            this.tsukiProperty.set(tsuki);
            this.tsuki = tsuki;
        }
    }

    public void setIpponProperty(int ippon) throws Exception {
        if (ippon < 0) {
            throw new Exception(" Ippon number is a negative number.");
        } else {
            this.ipponProperty.set(ippon);
            this.ippon = ippon;
        }

    }

    public void setHansokuProperty(int hansoku) throws Exception {
        if (hansoku < 0) {
            throw new Exception(" hansoku number is a negative number.");
        } else {
            this.hansokuProperty.set(hansoku);
            this.hansoku = hansoku;
        }
    }

    public void setColorProperty(String color) throws Exception {
        if (color.isEmpty()) {
            throw new Exception(" Color string is empty.");
        } else {
            this.colorProperty.set(color);
            this.color = color;
        }

    }

    public void setFirstName(String firstName) throws Exception {
        if (firstName.isEmpty()) {
            throw new Exception("First name field is empty.");
        } else {
            this.firstNameProperty.setValue(firstName);
            this.firstName = firstName;
        }

    }

    public void setLastName(String lastName) throws Exception {
        if (lastName.isEmpty()) {
            throw new Exception("Last name field is empty.");
        } else {
            this.lastNameProperty.setValue(lastName);
            this.lastName = lastName;
        }
    }

    public void setRound(String round) throws Exception {
        if (round.isEmpty()) {
            throw new Exception(" 'Round' field is empty.");
        } else {
            this.roundProperty.setValue(round);
            this.round = round;
        }

    }

    public void setGradeType(String gradeType) throws Exception {
        if (gradeType.isEmpty()) {
            throw new Exception(" Grade type field is empty.");
        } else {
            this.gradeTypeProperty.setValue(gradeType);
            this.gradeType = gradeType;
        }

    }

    public void setGradeLevel(String gradeLevel) throws Exception {
        if (gradeLevel.isEmpty()) {
            throw new Exception(" Grade level field is empty.");
        } else {
            this.gradeLevelProperty.setValue(gradeLevel);
            this.gradeLevel = gradeLevel;
        }
    }

    public void setGender(String gender) throws Exception {
        if (gender.isEmpty()) {
            throw new Exception(" Gender field is empty.");
        } else {
            this.genderProperty.setValue(gender);
            this.gender = gender;
        }
    }

    public void setClubName(String clubName) throws Exception {
        if (clubName.isEmpty()) {
            throw new Exception(" Gender field is empty.");
        } else {
            this.clubNameProperty.setValue(clubName);
            this.clubName = clubName;
        }
    }

    public void setNationality(String nationality) throws Exception {
        if (nationality.isEmpty()) {
            throw new Exception(" Gender field is empty.");
        } else {
            this.nationality = nationality;
        }

    }

    public void setBelongsToTeam(String belongsToTeam) throws Exception {
        if (belongsToTeam.isEmpty()) {
            throw new Exception(" The player is not a member of a team.");
        } else {
            this.belongsToTeamProperty.setValue(belongsToTeam);
            this.belongsToTeam = belongsToTeam;
        }

    }

    public void setID(int ID) throws Exception {
        if (ID < 0) {
            throw new Exception(" Invalid ID");
        } else {
            this.IDProperty.set(ID);
            this.ID = ID;
        }

    }

    public void setWins(int w) throws Exception {
        if (w < 0) {
            throw new Exception(" Wins number is a negative number.");
        } else {
            this.winsProperty.set(w);
            this.wins = w;
        }

    }

    public void setDraws(int d) throws Exception {
        if (d < 0) {
            throw new Exception(" Draws number is a negative number.");
        } else {
            this.drawsProperty.set(d);
            this.draws = d;
        }

    }

    public void setLoses(int l) throws Exception {
        if (l < 0) {
            throw new Exception(" Defeat number is a negative number.");
        } else {
            this.losesProperty.set(l);
            this.loses = l;
        }

    }

    public void setMen(int men) throws Exception {
        if (men < 0) {
            throw new Exception(" Men number is a negative number.");
        } else {
            this.menProperty.set(men);
            this.men = men;
        }

    }

    public void setKote(int kote) throws Exception {
        if (kote < 0) {
            throw new Exception(" Kote number is a negative number.");
        } else {
            this.koteProperty.set(kote);
            this.kote = kote;
        }

    }

    public void setDou(int dou) throws Exception {
        if (dou < 0) {
            throw new Exception(" Dou number is a negative number.");
        } else {
            this.douProperty.set(dou);
            this.dou = dou;
        }

    }

    public void setTsuki(int tsuki) throws Exception {
        if (tsuki < 0) {
            throw new Exception(" Tsuki number is a negative number.");
        } else {
            this.tsukiProperty.set(tsuki);
            this.tsuki = tsuki;
        }

    }

    public void setIppon(int ippon) throws Exception {
        if (ippon < 0) {
            throw new Exception(" Ippon number is a negative number.");
        } else {
            this.ipponProperty.set(ippon);
            this.ippon = ippon;
        }

    }

    public void setHansoku(int hansoku) throws Exception {
        if (hansoku < 0) {
            throw new Exception(" hansoku number is a negative number.");
        } else {
            this.hansokuProperty.set(hansoku);
            this.hansoku = hansoku;
        }

    }

    public void setColor(String color) throws Exception {
        if (color.isEmpty()) {
            throw new Exception(" Color string is empty.");
        } else {
            this.colorProperty.set(color);
            this.color = color;
        }

    }

    public void setBelongsToCup(String belongsToCup) throws Exception {
        if (belongsToCup.isEmpty()) {
            throw new Exception(" The player is not a member of a Cup.");
        } else {
            this.belongsToCupProperty.set(belongsToCup);
            this.belongsToCup = belongsToCup;
        }

    }

    public void setBelongsToCupProperty(String belongsToCup) throws Exception {
        if (belongsToCup.isEmpty()) {
            throw new Exception(" The player is not a member of a Cup.");
        } else {
            this.belongsToCupProperty.set(belongsToCup);
            this.belongsToCup = belongsToCup;
        }

    }

}
