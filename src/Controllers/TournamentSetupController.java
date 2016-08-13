/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Model.Cup;
import Model.Team;
import Model.Tournament;
import FunctionalityClasses.DataStorage;
import Model.Club;
import Model.Contestant;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Panagiotis Bitharis
 */
public class TournamentSetupController implements Initializable {

    @FXML
    private CheckBox isATeamChkBx;

    @FXML
    private ComboBox cupNameCmb, gradeTypecmb, gradeLevelcmb, gendercmb, clubNamecmb, teamcmb;

    @FXML
    private Accordion accordion;

    @FXML
    private TitledPane regCompetitionsTP, regTeamsTP, regClubsTP, regPlayersTP;

    @FXML
    private TextField shiaijoTxt, cupNameTxt, numOfPoolsTxt, prefPerPoolTxt,
            maxPerPoolTxt, teamNameTxt, clubNameTxt, nationalityTxt, fnameTxt, lnameTxt, minMembersTxt, maxMembersTxt;

    @FXML
    private Button clearCupBtn, submitCupBtn, regCompSaveBtn, addTeamBtn,
            regTeamSaveBtn, regClubSaveBtn, addClubBtn, saveBtn, submitPayerBtn,
            removeTeamBtn, removeClubBtn, removeContestantBtn;

    @FXML
    private TableView cupTableView, teamTableView, clubTableView, cupSelectorTableView,
            playersTableView;

    private TableColumn compNameClm;

    private final String FONT_SIZE = "-fx-font-size:12px";

    //Because data inside the Tableviews are interconnected, for simplicity
    //the user will be able to modifiy the contents of a TableView and its respective ArrayList only while
    //the data are no saved yet to the file (bfore s-he presses "Save and Continue"
    private BooleanProperty lockedClearCupArray = new SimpleBooleanProperty(true); //this flag controls whether the clear button in the first TitledTane
    //will be locked or not

    private BooleanProperty lockedClearTeamArray = new SimpleBooleanProperty(true);//this flag controls whether the clear button in the second TitledTane
    //will be locked or not.

    private BooleanProperty lockedClearClubArray = new SimpleBooleanProperty(true);//this flag controls whether the clear button in the third TitledTane
    //will be locked or not.
    private BooleanProperty lockedClearContestantArray = new SimpleBooleanProperty(true);//this flag controls whether the clear button in the fourth TitledTane
    //will be locked or not.
    private ArrayList<Cup> cups = new ArrayList<>();
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<Club> clubs = new ArrayList<>();
    private ArrayList<Contestant> contestants = new ArrayList<>();
    private Tournament tournament = null;

    @FXML
    public void saveAndContinue(ActionEvent event) {
        Button button = (Button) event.getSource();
        try {
            if (button.getId().equalsIgnoreCase(regCompSaveBtn.getId())) {
                if (!cups.isEmpty()) {
                    tournament.setCups(cups);
                    saveDataToFile(tournament);

                    lockedClearCupArray.set(true);
                    accordion.setExpandedPane(regTeamsTP);
                    regTeamsTP.setDisable(false);
                    populateCupNameChoiceBox();
                    if (cupNameCmb.getItems().isEmpty()) {
                        regTeamSaveBtn.setText("Skip");
                        regTeamSaveBtn.setDisable(false);
                    }
                    tournament.printStructure();
                }
            } else if (button.getId().equalsIgnoreCase(regTeamSaveBtn.getId())) {
                if (regTeamSaveBtn.getText().equalsIgnoreCase("Skip")) {
                    tournament.setCups(cups);
                    saveDataToFile(tournament);

                    lockedClearTeamArray.set(true);
                    accordion.setExpandedPane(regClubsTP);
                    regClubsTP.setDisable(false);
                    tournament.printStructure();
                } else if (teams.size() >= 1) {
                    tournament.setCups(cups);
                    saveDataToFile(tournament);

                    lockedClearTeamArray.set(true);
                    accordion.setExpandedPane(regClubsTP);
                    regClubsTP.setDisable(false);
                    tournament.printStructure();
                } else {
                    DataStorage.getInstance().msgBox("You must add at least one element", false);
                }

            } else if (button.getId().equalsIgnoreCase(regClubSaveBtn.getId())) {
                if (clubs.size() >= 1) {

                    accordion.setExpandedPane(regPlayersTP);
                    regPlayersTP.setDisable(false);
                    saveDataToFile(tournament);
                    tournament.setCups(cups);
                    tournament.printStructure();
                    lockedClearClubArray.set(true);

                    populateGradeTypeComboBox();
                    populateGenderComboBox();
                    populateClubComboBox();
                    populateTeamComboBox();
                    setupCupSelectorTableView();
                } else {
                    DataStorage.getInstance().msgBox("You must add at least one element", false);
                }
            } else if (button.getId().equalsIgnoreCase(saveBtn.getId())) {
                if (contestants.size() >= 1) {
                    //save all and close
                    tournament.setCups(cups);
                    saveDataToFile(tournament);
                    tournament.printStructure();
                    lockedClearContestantArray.set(true);
                    tournament.printStructure();
                } else {
                    DataStorage.getInstance().msgBox("You must add at least one element", false);
                }

            }

        } catch (Exception ex) {
            DataStorage.getInstance().msgBox(ex.getMessage(), false);
        }

    }

    @FXML
    public void addCupToTable(ActionEvent event) {

        try {
            String cupName = cupNameTxt.getText().trim().toUpperCase();
            cupName = cupName.replace(' ', '_');
            //Check for duplicate cup name
            if (checkDuplicateCupName(cupName, cups)) {
                DataStorage.getInstance().msgBox("Cup name already exists!", false);
            } else {
                int poolNumber = Integer.parseInt(numOfPoolsTxt.getText());
                int pPlayers = Integer.parseInt(prefPerPoolTxt.getText());
                int mPlayers = Integer.parseInt(maxPerPoolTxt.getText());
                int shiaijosnum = Integer.parseInt(shiaijoTxt.getText());
                boolean b = isATeamChkBx.selectedProperty().get();
                submitFirstTitledPaneDataToCupsArray(cupName, poolNumber, pPlayers, mPlayers, shiaijosnum, b);

                ObservableList<Cup> observCups = FXCollections.observableList(cups);
                cupTableView.setItems(observCups);
                setupCupTableView();

                cupNameTxt.clear();
                prefPerPoolTxt.clear();
                maxPerPoolTxt.clear();
                numOfPoolsTxt.clear();
                shiaijoTxt.clear();
                isATeamChkBx.setSelected(false);
                regCompSaveBtn.setDisable(false);
            }

        } catch (NumberFormatException nfe) {
            DataStorage.getInstance().msgBox("Illegal number format. You inserted characters istead of numbers or some fields are left empty.", false);
        } catch (IllegalArgumentException iae) {
            DataStorage.getInstance().msgBox("You entered invalid information (i.e value left blank, illegal characters used)", false);
        }catch (InputMismatchException e){
            DataStorage.getInstance().msgBox(e.getMessage(), false);
        }catch (Exception ex) {
            DataStorage.getInstance().msgBox(ex.getMessage(), false);
        } finally {
            cupNameTxt.clear();
            prefPerPoolTxt.clear();
            maxPerPoolTxt.clear();
            numOfPoolsTxt.clear();
            shiaijoTxt.clear();
            isATeamChkBx.setSelected(false);
            cupNameTxt.requestFocus();

        }

    }

    @FXML
    public void addTeamToTable(ActionEvent event) {

        try {
            String teamName = teamNameTxt.getText().trim().toUpperCase();
            String cupname = cupNameCmb.getSelectionModel().getSelectedItem().toString();

            int min = Integer.parseInt(minMembersTxt.getText());
            int max = Integer.parseInt(maxMembersTxt.getText());
            if (!teamNameTxt.getText().isEmpty()) {
                if (checkDuplicateTeamName(teamName, cupname)) {
                    DataStorage.getInstance().msgBox("Team name already exists!", false);
                    cupNameCmb.getSelectionModel().clearSelection();

                } else {
                    submitSecondTitledPaneDataToCupsArray(teamName, cupname, min, max);
                    ObservableList<Team> observTeams = FXCollections.observableList(teams);
                    teamTableView.setItems(observTeams);
                    setupTeamTableView();

                    teamNameTxt.clear();
                    minMembersTxt.clear();
                    maxMembersTxt.clear();
                    cupNameCmb.getSelectionModel().clearSelection();
                    regTeamSaveBtn.setDisable(false);
                }
            }

        } catch (NumberFormatException nfe) {
            DataStorage.getInstance().msgBox("Illegal number format. Only numbers allowed.No fields should be left empty.", false);
        } catch (IllegalArgumentException iae) {
            DataStorage.getInstance().msgBox("You entered invalid information (i.e value left blank, illegal characters used)", false);
        } catch (Exception ex) {
            DataStorage.getInstance().msgBox(ex.getLocalizedMessage(), false);
        } finally {
            teamNameTxt.clear();
            minMembersTxt.clear();
            maxMembersTxt.clear();
            cupNameCmb.getSelectionModel().clearSelection();
            teamNameTxt.requestFocus();
        }

    }

    @FXML
    public void addClubToTable(ActionEvent event) {
        try {
            String clubName = clubNameTxt.getText().trim().toUpperCase();
            String clubNationality = nationalityTxt.getText().trim().toUpperCase();

            if (clubName.isEmpty() || clubNationality.isEmpty()) {
                DataStorage.getInstance().msgBox("Fields cannot be empty", false);
            } else if (checkDuplicateClubs(clubName, clubNationality)) {
                Club club = new Club(clubName, clubNationality);
                clubs.add(club);
                submitThirdTitledPaneDataToCupsArray();

                ObservableList<Club> observClubs = FXCollections.observableArrayList(clubs);
                clubTableView.setItems(observClubs);
                setupClubTableView();

                clubNameTxt.clear();
                nationalityTxt.clear();
                regClubSaveBtn.setDisable(false);
            }

        } catch (NumberFormatException nfe) {
            DataStorage.getInstance().msgBox("Illegal number format. Only numbers allowed.No fields should be left empty.", false);
        } catch (IllegalArgumentException iae) {
            DataStorage.getInstance().msgBox("You entered invalid information (i.e value left blank, illegal characters used)", false);
        } catch (Exception ex) {
            DataStorage.getInstance().msgBox(ex.getMessage(), false);
        } finally {
            clubNameTxt.clear();
            nationalityTxt.clear();
            clubNameTxt.requestFocus();

        }

    }

    @FXML
    public void addContestantToTable(ActionEvent event) {
        try {
            Contestant contestant = null;
            String fname = fnameTxt.getText().trim().toUpperCase();
            String lname = lnameTxt.getText().trim().toUpperCase();
            String gradeType = gradeTypecmb.getSelectionModel().getSelectedItem().toString();
            String gradeLvl = gradeLevelcmb.getSelectionModel().getSelectedItem().toString();
            String gender = gendercmb.getSelectionModel().getSelectedItem().toString();
            String clubName = clubNamecmb.getSelectionModel().getSelectedItem().toString();
            String teamName;

            if (fname.isEmpty() || lname.isEmpty() || gradeType.isEmpty() || gradeLvl.isEmpty() || gender.isEmpty()
                    || clubName.isEmpty()) {
                DataStorage.getInstance().msgBox("Fields cannot be empty", false);
            } else {
                if (!teamcmb.getSelectionModel().isEmpty()) {
                    teamName = teamcmb.getSelectionModel().getSelectedItem().toString();
                    contestant = new Contestant(fname, lname, gradeType, gradeLvl, gender, clubName, teamName);
                } else {

                    contestant = new Contestant(fname, lname, gradeType, gradeLvl, gender, clubName);
                }

                submitFourthTitledPaneDataToCupsArray(contestant);

                ObservableList<Contestant> observConts = FXCollections.observableArrayList(contestants);
                playersTableView.setItems(observConts);
                setupPlayersTableView();

                fnameTxt.clear();
                lnameTxt.clear();
                gradeTypecmb.getSelectionModel().clearSelection();
                gradeLevelcmb.getSelectionModel().clearSelection();
                gendercmb.getSelectionModel().clearSelection();
                clubNamecmb.getSelectionModel().clearSelection();
                //teamcmb.getSelectionModel().clearSelection();
                fnameTxt.requestFocus();
            }

        } catch (NumberFormatException nfe) {
            DataStorage.getInstance().msgBox("Illegal number format. Only numbers allowed.No fields should be left empty.", false);
        } catch (IllegalArgumentException iae) {
            DataStorage.getInstance().msgBox("You entered invalid information (i.e value left blank, illegal characters used)", false);
        } catch (Exception ex) {
            DataStorage.getInstance().msgBox(ex.getMessage(), false);
        } finally {
            fnameTxt.clear();
            lnameTxt.clear();
            gradeTypecmb.getSelectionModel().clearSelection();
            gradeLevelcmb.getSelectionModel().clearSelection();
            gendercmb.getSelectionModel().clearSelection();
            clubNamecmb.getSelectionModel().clearSelection();
            teamcmb.getSelectionModel().clearSelection();

        }

    }

    @FXML
    public void chooseGradeTypeAction(ActionEvent event) {
        try {
            ObservableList<String> gradeLevel = FXCollections.observableArrayList();
            if (!gradeLevelcmb.getItems().isEmpty()) {
                gradeLevelcmb.getItems().clear();
            }
            if (!gradeTypecmb.getSelectionModel().isEmpty()) {
                if (gradeTypecmb.getSelectionModel().getSelectedItem().toString().equalsIgnoreCase("Dan")) {
                    gradeLevel.clear();
                    for (int i = 1; i <= 8; i++) {
                        gradeLevel.add(String.valueOf(i));
                    }
                    gradeLevelcmb.getItems().addAll(gradeLevel);

                } else if (gradeTypecmb.getSelectionModel().getSelectedItem().toString().equalsIgnoreCase("Kyu")) {
                    gradeLevel.clear();
                    for (int i = 1; i <= 6; i++) {
                        gradeLevel.add(String.valueOf(i));
                    }
                    gradeLevelcmb.getItems().addAll(gradeLevel);
                }
            }

        }catch (Exception ex) {
            DataStorage.getInstance().msgBox(ex.getMessage(), false);
        } finally {

        }

    }

    @FXML
    public void clear(ActionEvent event) {
        Button button = (Button) event.getSource();

        if (button.getId().equalsIgnoreCase(clearCupBtn.getId())) {
            removeCupFromTournament();

        } else if (button.getId().equalsIgnoreCase(removeTeamBtn.getId())) {
            removeTeamFromCup();

        } else if (button.getId().equalsIgnoreCase(removeClubBtn.getId())) {
            removeClubFromCup();

        } else if (button.getId().equalsIgnoreCase(removeContestantBtn.getId())) {
            removeContestantFromCup();
        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUpControllers();
        setupCupTableView();
        setupTeamTableView();
        setupClubTableView();
        setupPlayersTableView();
        try {
            loadSavedDataFromFile();
        } catch (Exception ex) {
            DataStorage.getInstance().msgBox(ex.getMessage(), false);
        }

        //Check if the Arraylists are empty.
        //If they are empty that means that the user hasn't saved anything yet or 
        //closed the applcation before saving data to the file.
        //Therefore a deletion of a row should be allowed.
        if (contestants.isEmpty()) {
            if (clubs.isEmpty()) {
                if (teams.isEmpty()) {
                    lockedClearContestantArray.set(false);
                    lockedClearClubArray.set(false);
                    lockedClearTeamArray.set(false);
                    lockedClearCupArray.set(false);
                } else {
                    lockedClearContestantArray.set(false);
                    lockedClearClubArray.set(false);
                    lockedClearTeamArray.set(false);
                    lockedClearCupArray.set(true);
                }
            } else {
                lockedClearContestantArray.set(false);
                lockedClearClubArray.set(false);
                lockedClearTeamArray.set(true);
                lockedClearCupArray.set(true);
            }
        } else {
            lockedClearContestantArray.set(false);
            lockedClearClubArray.set(true);
            lockedClearTeamArray.set(true);
            lockedClearCupArray.set(true);
        }
    }

    private void setUpControllers() {
        //1. Setup the first TitledPane to be always expanded
        accordion.setExpandedPane(regCompetitionsTP);

        //2. Assign the Tournament loaded in memory to a local variable
        tournament = DataStorage.getInstance().getLoadedTournament();

        //3 Bind clear Buttons with the locks flags
        clearCupBtn.disableProperty().bind(lockedClearCupArray);
        removeTeamBtn.disableProperty().bind(lockedClearTeamArray);
        removeClubBtn.disableProperty().bind(lockedClearClubArray);
        removeContestantBtn.disableProperty().bind(lockedClearContestantArray);

    }

    private void loadSavedDataFromFile() throws Exception {
        //check if there are data already saved inside the file and load them to the tables
        if (tournament.getCups() != null) {
            //If there are Cups stored inside the files loop through every Cup
            for (Cup c : tournament.getCups()) {
                //create a new cup object
                Cup cup = new Cup(c.getCupName(), c.getPrefPlayers(), c.getMaxPlayers(), c.getNumberOfPools(), c.getNumberOfShiaiJos());
                cup.setIsATeamChampionship(c.isIsATeamChampionship());
                //If there are teams saved inside each Cup inside the file load them to the 
                //cup object
                if (!c.getTeams().isEmpty()) {
                    cup.setTeams(c.getTeams());

                }
                //If there are Clubs saved inside each Cup inside the file load them to the 
                //cup object
                if (!c.getClubs().isEmpty()) {
                    cup.setClubs(c.getClubs());
                }

                if (!c.getContestants().isEmpty()) {
                    cup.setContestants(c.getContestants());
                }

                //Add that object to the arrayList of cups
                //This arrayList will be used to add or remove objects 
                //and to be used for filling local ObservableLists for use with the TableViews
                cups.add(cup);

                //We must also fill the ArrayLists to be used as an parameter to 
                //local observableLists initializations and add or remove objects from here on to
                //these arraylists
                for (Team t : c.getTeams()) {
                    Team team = new Team(t.getTeamName(), t.getMinMembers(), t.getMaxMebmers());
                    team.setBelongsToCup(t.getBelongsToCup());
                    teams.add(team);
                }

                for (Contestant contestant : cup.getContestants()) {
                    Contestant cont = new Contestant(contestant.getFirstName(),
                            contestant.getLastName(), contestant.getGradeType(),
                            contestant.getGradeLevel(), contestant.getGender(),
                            contestant.getClubName(), contestant.getBelongsToTeam());
                    cont.setBelongsToCup(contestant.getBelongsToCup());
                    contestants.add(cont);
                }

            }

            //We must also fill the ArrayLists to be used as an parameter to 
            //local observableLists initializations and add or remove objects from here on to
            //these arraylists
            //We must also fill the ArrayLists to be used as an parameter to 
            //NOTE: We are putting this loop outside the for each loop
            //in the previous section because club names are the same for
            //all cups, there fore we need to load them only once
            for (Club club : cups.get(0).getClubs()) {
                Club cl = new Club(club.getClubName(), club.getNationality());
                clubs.add(cl);
            }
            ObservableList<Cup> observCups = FXCollections.observableList(cups);
            cupTableView.setItems(observCups);
            setupCupTableView();

            ObservableList<Team> observTeams = FXCollections.observableList(teams);
            teamTableView.setItems(observTeams);
            setupTeamTableView();

            ObservableList<Club> observClubs = FXCollections.observableArrayList(clubs);
            clubTableView.setItems(observClubs);
            setupClubTableView();

            ObservableList<Contestant> observContestants = FXCollections.observableArrayList(contestants);
            playersTableView.setItems(observContestants);
            setupPlayersTableView();

            //enable the buttons
            regCompSaveBtn.setDisable(false);
            regTeamSaveBtn.setDisable(false);
            regClubSaveBtn.setDisable(false);

        }
    }

    private void setupCupTableView() {
        TableColumn poolNumClm;
        TableColumn prefPlayersClm;
        TableColumn maxPlayersClm;
        TableColumn shiajoNumberClm;
        TableColumn isATeamCupClm;

        cupTableView.getColumns().clear();

        compNameClm = new TableColumn("Cup Name");
        compNameClm.setCellValueFactory(new PropertyValueFactory("cupnameProperty"));
        compNameClm.setStyle(FONT_SIZE);

        poolNumClm = new TableColumn("Pools");
        poolNumClm.setCellValueFactory(new PropertyValueFactory("numberOfPoolsProperty"));
        poolNumClm.setStyle(FONT_SIZE);

        prefPlayersClm = new TableColumn("pref. pl/Pool");
        prefPlayersClm.setCellValueFactory(new PropertyValueFactory("prefPlayersProperty"));
        prefPlayersClm.setStyle(FONT_SIZE);

        maxPlayersClm = new TableColumn("max. pl/Pool");
        maxPlayersClm.setCellValueFactory(new PropertyValueFactory("maxPlayersProperty"));
        maxPlayersClm.setStyle(FONT_SIZE);

        shiajoNumberClm = new TableColumn("Av. Shiajos");
        shiajoNumberClm.setCellValueFactory(new PropertyValueFactory("numberOfShiaiJosProperty"));
        shiajoNumberClm.setStyle(FONT_SIZE);

        isATeamCupClm = new TableColumn("Team Cup");
        isATeamCupClm.setCellValueFactory(new PropertyValueFactory("isATeamChampionshipProperty"));
        isATeamCupClm.setStyle(FONT_SIZE);

        cupTableView.getColumns().addAll(compNameClm, poolNumClm, prefPlayersClm, maxPlayersClm, shiajoNumberClm, isATeamCupClm);

    }

    private void setupTeamTableView() {
        teamTableView.getColumns().clear();

        TableColumn teamNameClm = new TableColumn("Team");
        teamNameClm.setStyle(FONT_SIZE);
        teamNameClm.setCellValueFactory(new PropertyValueFactory("teamNameProperty"));

        TableColumn cupClm = new TableColumn("Cup");
        cupClm.setStyle(FONT_SIZE);
        cupClm.setCellValueFactory(new PropertyValueFactory("belongsToCupProperty"));

        TableColumn minPlayersClm = new TableColumn("Min Players");
        minPlayersClm.setStyle(FONT_SIZE);
        minPlayersClm.setCellValueFactory(new PropertyValueFactory("minMembersProperty"));

        TableColumn maxPlayersClm = new TableColumn("Max Players");
        maxPlayersClm.setStyle(FONT_SIZE);
        maxPlayersClm.setCellValueFactory(new PropertyValueFactory("maxMembersProperty"));

        teamTableView.getColumns().addAll(teamNameClm, cupClm, minPlayersClm, maxPlayersClm);
    }

    private void setupClubTableView() {
        clubTableView.getColumns().clear();
        TableColumn clubNameColumn = new TableColumn("Club Name");
        clubNameColumn.setCellValueFactory(new PropertyValueFactory("clubNameProperty"));
        clubNameColumn.setStyle(FONT_SIZE);

        TableColumn clubNationalityColumn = new TableColumn("Nationality");
        clubNationalityColumn.setCellValueFactory(new PropertyValueFactory("nationalityProperty"));
        clubNationalityColumn.setStyle(FONT_SIZE);

        clubTableView.getColumns().addAll(clubNameColumn, clubNationalityColumn);
    }

    private void setupCupSelectorTableView() {
        cupSelectorTableView.getColumns().clear();
        TableColumn invitedCol = new TableColumn<Cup, Boolean>();
        invitedCol.setText("Participate");
        invitedCol.setStyle(FONT_SIZE);
        invitedCol.setCellValueFactory(new PropertyValueFactory("includedInParticipationList"));
        invitedCol.setCellFactory(new Callback<TableColumn<Cup, Boolean>, TableCell<Cup, Boolean>>() {

            public TableCell<Cup, Boolean> call(TableColumn<Cup, Boolean> p) {
                return new CheckBoxTableCell<Cup, Boolean>();

            }
        });

        ObservableList<Cup> observCups = FXCollections.observableArrayList(cups);

        cupSelectorTableView.setItems(observCups);
        cupSelectorTableView.getColumns().addAll(compNameClm, invitedCol);
    }

    private void setupPlayersTableView() {
        playersTableView.getColumns().clear();
        TableColumn fnameClm = new TableColumn("First name");
        fnameClm.setCellValueFactory(new PropertyValueFactory("firstNameProperty"));
        fnameClm.setStyle(FONT_SIZE);

        TableColumn lnameClm = new TableColumn("Last name");
        lnameClm.setCellValueFactory(new PropertyValueFactory("lastNameProperty"));
        lnameClm.setStyle(FONT_SIZE);

        TableColumn gradeTypeClm = new TableColumn("Grade type");
        gradeTypeClm.setCellValueFactory(new PropertyValueFactory("gradeTypeProperty"));
        gradeTypeClm.setStyle(FONT_SIZE);

        TableColumn gradeLvlClm = new TableColumn("Grade level");
        gradeLvlClm.setCellValueFactory(new PropertyValueFactory("gradeLevelProperty"));
        gradeLvlClm.setStyle(FONT_SIZE);

        TableColumn genderClm = new TableColumn("Gender");
        genderClm.setCellValueFactory(new PropertyValueFactory("genderProperty"));
        genderClm.setStyle(FONT_SIZE);

        TableColumn clubNameClm = new TableColumn("Club");
        clubNameClm.setCellValueFactory(new PropertyValueFactory("clubNameProperty"));
        clubNameClm.setStyle(FONT_SIZE);

        TableColumn teamNameClm = new TableColumn("Team");
        teamNameClm.setCellValueFactory(new PropertyValueFactory("belongsToTeamProperty"));
        teamNameClm.setStyle(FONT_SIZE);

        TableColumn cupNameClm = new TableColumn("Cup");
        cupNameClm.setCellValueFactory(new PropertyValueFactory("belongsToCupProperty"));
        cupNameClm.setStyle(FONT_SIZE);

        playersTableView.getColumns().addAll(fnameClm, lnameClm, gradeTypeClm,
                gradeLvlClm, genderClm, clubNameClm, teamNameClm, cupNameClm);
    }

    private void populateCupNameChoiceBox() {
        cupNameCmb.getItems().clear();
        for (Cup c : tournament.getCups()) {
            if (c.isIsATeamChampionship()) {
                cupNameCmb.getItems().add(c.getCupName());
            }

        }
    }

    private void populateGradeTypeComboBox() {
        ObservableList<String> gradeTypes = FXCollections.observableArrayList();
        gradeTypes.add("Dan");
        gradeTypes.add("Kyu");
        gradeTypecmb.getItems().addAll(gradeTypes);
    }

    private void populateGenderComboBox() {
        ObservableList<String> gender = FXCollections.observableArrayList();
        gender.add("Female");
        gender.add("Male");

        gendercmb.setItems(gender);
    }

    private void populateClubComboBox() {
        ObservableList<String> clubnames = FXCollections.observableArrayList();
        for (Club club : clubs) {
            clubnames.add(club.getClubName());
        }
        clubNamecmb.setItems(clubnames);
    }

    public void populateTeamComboBox() {
        teamcmb.setOnMouseClicked(e -> {
            ObservableList<String> teamslist = FXCollections.observableArrayList();
            for (Cup c : cups) {
                if (c.isIsATeamChampionship() && Cup.getTeamChampionshipsSelected() < 2 && c.includedInParticipationListProperty().get()) {
                    for (Team t : c.getTeams()) {
                        System.out.println(t.getTeamName());
                        teamslist.add(t.getTeamName());
                    }
                }
            }
            teamcmb.setItems(teamslist);
        });

    }

    private boolean checkDuplicateCupName(String s, ArrayList<Cup> cups) {
        boolean found = false;
        for (Cup c : cups) {
            if (c.getCupName().equalsIgnoreCase(s)) {
                found = true;
                break;
            }
        }
        return found;
    }

    private boolean checkDuplicateTeamName(String s, String cup) {
        boolean found = false;
        for (Cup c : cups) {
            for (Team t : c.getTeams()) {
                if (t.getTeamName().equalsIgnoreCase(s) && t.getBelongsToCup().equalsIgnoreCase(cup)) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    private boolean checkDuplicateClubs(String clubName, String clubNationality) {
        boolean isUnique = true;
        for (Club cl : clubs) {
            if (cl.getClubName().equalsIgnoreCase(clubName)
                    && cl.getNationality().equalsIgnoreCase(clubNationality)) {
                DataStorage.getInstance().msgBox(" Duplicate Entry. Club already exists.", false);
                isUnique = false;
                break;
            }
        }
        return isUnique;
    }

    private boolean checkDuplicateContestant(Contestant contestant) {
        boolean isUnique = false;

        if (!contestants.isEmpty()) {
            for (Contestant cont : contestants) {
                if (cont.getFirstName().equalsIgnoreCase(contestant.getFirstName())
                        && cont.getLastName().equalsIgnoreCase(contestant.getLastName())
                        && cont.getClubName().equalsIgnoreCase(contestant.getClubName())
                        && cont.getGradeLevel().equalsIgnoreCase(contestant.getGradeLevel())
                        && cont.getGradeType().equalsIgnoreCase(contestant.getGradeType())
                        && cont.getBelongsToCup().equalsIgnoreCase(contestant.getBelongsToCup())) {
                    isUnique = false;
                    break;
                } else {
                    isUnique = true;

                }
            }
        } else {
            isUnique = true;
        }

        return isUnique;
    }

    private void submitFirstTitledPaneDataToCupsArray(String cupName, int noOfPools, 
            int pPlayers, int mPlayers, int noShiaiJos, boolean isTeamCup) throws Exception {
        Cup c = new Cup(cupName, pPlayers, mPlayers, noOfPools, noShiaiJos);
        c.setIsATeamChampionship(isTeamCup);
        cups.add(c);
    }

    private void submitSecondTitledPaneDataToCupsArray(String teamName, String cupName, int min, int max) throws Exception {

        //saving the team in a local Observable List that will be used with TableView
        Team t = new Team(teamName, min, max);
        t.setBelongsToCupProperty(cupName);
        teams.add(t);

        for (Cup c : cups) {
            if (c.getCupName().equalsIgnoreCase(cupName)) {
                //inserting the team to the Tournament structure             
                c.getTeams().add(t);

            }
        }

    }

    private void submitThirdTitledPaneDataToCupsArray() throws Exception {
        for (Cup c : cups) {
            c.setClubs(clubs);
        }
    }

    private void submitFourthTitledPaneDataToCupsArray(Contestant c) throws Exception {

        Iterator<Cup> cupsIterator = cups.iterator();
        while (cupsIterator.hasNext()) {
            Cup tmpCup = cupsIterator.next();
            if (tmpCup.includedInParticipationListProperty().get()) {
                if (tmpCup.isIsATeamChampionship()) {
                    //if the player is not a member of a team just add the player to the competition
                    if (c.getBelongsToTeamProperty() == null) {

                        Contestant tmpContestant = new Contestant(c.getFirstName(),
                                c.getLastName(), c.getGradeType(), c.getGradeLevel(),
                                c.getGender(), c.getClubName());

                        tmpContestant.setBelongsToCup(tmpCup.getCupName());
                        tmpContestant.setBelongsToTeam("lone samurai");

                        if (checkDuplicateContestant(tmpContestant)) {
                            contestants.add(tmpContestant);
                            tmpCup.getContestants().add(tmpContestant);
                        } else {
                            DataStorage.getInstance().msgBox("Duplicate Record!" + tmpContestant.getFirstName()
                                    + " " + tmpContestant.getLastName() + " in " + tmpContestant.getBelongsToCup()
                                    + ".", false);
                        }
                    } else {
                        //if the player belongs to a team search to which team he belongs to
                        Iterator<Team> teamsIterator = tmpCup.getTeams().iterator();
                        while (teamsIterator.hasNext()) {
                            Team tmpTeam = teamsIterator.next();
                            if (tmpTeam.getTeamName().equalsIgnoreCase(c.getBelongsToTeam())) {
                                c.setBelongsToCup(tmpCup.getCupName());
                                if (checkDuplicateContestant(c)) {

                                    try {
                                        tmpTeam.addToTeam(c);
                                        tmpCup.getContestants().add(c);
                                        contestants.add(c);
                                        break;
                                    } catch (ArrayIndexOutOfBoundsException aiobe) {
                                        DataStorage.getInstance().msgBox("The team you are placing the player is full. ", false);
                                    }

                                } else {
                                    DataStorage.getInstance().msgBox("Duplicate Record!" + c.getFirstName()
                                            + " " + c.getLastName() + " in " + c.getBelongsToCup()
                                            + ".", false);
                                    break;
                                }
                            }
                        }

                    }

                } else if (!tmpCup.isIsATeamChampionship()) {
                    Contestant tmpContestant = new Contestant(c.getFirstName(),
                            c.getLastName(), c.getGradeType(), c.getGradeLevel(),
                            c.getGender(), c.getClubName());

                    tmpContestant.setBelongsToCup(tmpCup.getCupName());

                    if (checkDuplicateContestant(tmpContestant)) {
                        tmpCup.getContestants().add(tmpContestant);
                        contestants.add(tmpContestant);
                    } else {
                        DataStorage.getInstance().msgBox("Duplicate Record!" + tmpContestant.getFirstName()
                                + " " + tmpContestant.getLastName() + " in " + tmpContestant.getBelongsToCup()
                                + ".", false);
                    }

                }
            }
        }

    }

    public static class CheckBoxTableCell<S, T> extends TableCell<S, T> {

        private final CheckBox checkBox;

        private ObservableValue<T> ov;

        public CheckBoxTableCell() {

            this.checkBox = new CheckBox();
            this.checkBox.setAlignment(Pos.CENTER);
            setAlignment(Pos.CENTER);
            setGraphic(checkBox);

        }

        @Override
        public void updateItem(T item, boolean empty) {

            super.updateItem(item, empty);

            if (empty) {

                setText(null);

                setGraphic(null);

            } else {
                setGraphic(checkBox);
                if (ov instanceof BooleanProperty) {
                    checkBox.selectedProperty().unbindBidirectional((BooleanProperty) ov);
                }
                ov = getTableColumn().getCellObservableValue(getIndex());

                if (ov instanceof BooleanProperty) {
                    checkBox.selectedProperty().bindBidirectional((BooleanProperty) ov);
                }
            }
        }
    }

    private void saveDataToFile(Tournament t) {
        ObjectOutputStream output = null;
        try {
            Path path = Paths.get("src/Tournaments/" + t.getTournamentName());
            output = new ObjectOutputStream(Files.newOutputStream(path));
            output.writeObject(t);
        } catch (IOException ex) {
            Logger.getLogger(TournamentSetupController.class.getName()).log(Level.SEVERE, null, ex);
            DataStorage.getInstance().msgBox(ex.getMessage(), false);
        } finally {
            try {
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(TournamentSetupController.class.getName()).log(Level.SEVERE, null, ex);
                DataStorage.getInstance().msgBox(ex.getMessage(), false);
            }
        }
    }

    private void removeContestantFromCup() {
        //Select an item from the tableview
        Contestant contestant = (Contestant) playersTableView.getSelectionModel().getSelectedItem();

        //remove the selected object from the ArrayList
        contestants.remove(contestant);

        //remove the Contestant from the Cup (cups ArrayList)
        Iterator cupIterator = cups.listIterator();
        while (cupIterator.hasNext()) {
            Cup c = (Cup) cupIterator.next();
            Iterator playersIteratorList = c.getContestants().listIterator();
            while (playersIteratorList.hasNext()) {
                Contestant con = (Contestant) playersIteratorList.next();
                if (con.getFirstName().equalsIgnoreCase(contestant.getFirstName())
                        && con.getLastName().equalsIgnoreCase(contestant.getLastName())
                        && con.getClubName().equalsIgnoreCase(contestant.getClubName())
                        && con.getBelongsToCup().equalsIgnoreCase(contestant.getBelongsToCup())) {
                    playersIteratorList.remove();
                }
            }

            //Remve from team members
            Iterator teamIterator = c.getTeams().listIterator();
            while (teamIterator.hasNext()) {
                Team team = (Team) teamIterator.next();
                Iterator teamMemberList = team.getTeamMembersAsList().iterator();
                while (teamMemberList.hasNext()) {
                    Contestant teamMember = (Contestant) teamMemberList.next();
                    if (teamMember.getFirstName().equalsIgnoreCase(contestant.getFirstName())
                            && teamMember.getLastName().equalsIgnoreCase(contestant.getLastName())
                            && teamMember.getClubName().equalsIgnoreCase(contestant.getClubName())
                            && teamMember.getBelongsToCup().equalsIgnoreCase(contestant.getBelongsToCup())
                            && teamMember.getBelongsToTeam().equalsIgnoreCase(contestant.getBelongsToTeam())) {
                        teamMemberList.remove();
                    }
                }

            }
        }

        Iterator teamIterator = teams.listIterator();
        while (teamIterator.hasNext()) {
            Team tmpTeam = (Team) teamIterator.next();
            tmpTeam.getTeamMembersAsList().remove(contestant);

        }

        //Remove the row from TableView
        ObservableList<Contestant> observContestants = FXCollections.observableList(contestants);
        playersTableView.setItems(observContestants);
        setupPlayersTableView();
    }

    private void removeClubFromCup() {
        System.out.println("DELETE club");
        //Select an item from the tableview
        Club club = (Club) clubTableView.getSelectionModel().getSelectedItem();

        //remove selected item from the ArrayList
        clubs.remove(club);

        //remove the club from the cups ArrayList
        for (Cup c : cups) {
            Iterator it = c.getClubs().listIterator();
            while (it.hasNext()) {
                Club cl = (Club) it.next();
                if (cl.getClubName().equalsIgnoreCase(club.getClubName())) {
                    it.remove();
                }
            }
        }

        //Remove the row from TableView
        ObservableList<Club> observClubs = FXCollections.observableList(clubs);
        clubTableView.setItems(observClubs);
        setupClubTableView();
    }

    private void removeTeamFromCup() {
        //Select an item from the tableview
        Team team = (Team) teamTableView.getSelectionModel().getSelectedItem();

        //remove the selected object from the ArrayList
        teams.remove(team);

        //remove the team from the cups ArrayList
        for (Cup c : cups) {
            Iterator it = c.getTeams().listIterator();
            while (it.hasNext()) {
                Team t = (Team) it.next();
                if (t.getTeamName().equalsIgnoreCase(team.getTeamName())
                        && t.getBelongsToCup().equalsIgnoreCase(team.getBelongsToCup())) {
                    it.remove();
                }
            }
        }

        //Remove the row from TableView
        ObservableList<Team> observTeams = FXCollections.observableList(teams);
        teamTableView.setItems(observTeams);
        setupTeamTableView();
    }

    private void removeCupFromTournament() {
        if (cups.size() > 1) {
            System.out.println("DELETE cup");
            //Select an item from the tableview
            Cup cup = (Cup) cupTableView.getSelectionModel().getSelectedItem();

            //Remove the selected cup from the cups ArrayList
            cups.remove(cup);

            //Remove the row from TableView
            ObservableList<Cup> observCups = FXCollections.observableArrayList(cups);
            cupTableView.setItems(observCups);
            setupClubTableView();
        } else {
            DataStorage.getInstance().msgBox("Illegal Operation. You cannot remove all items from the table", false);
        }
    }

}
