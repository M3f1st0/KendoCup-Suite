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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;

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

    private Tournament tournament = null;

    @FXML
    public void saveAndContinue(ActionEvent event) {
        Button button = (Button) event.getSource();
        try {
            if (button.getId().equalsIgnoreCase(regCompSaveBtn.getId())) {
                if (!tournament.getCups().isEmpty()) {
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

                    saveDataToFile(tournament);

                    lockedClearTeamArray.set(true);
                    accordion.setExpandedPane(regClubsTP);
                    regClubsTP.setDisable(false);
                    tournament.printStructure();
                } else if (tournament.getTeams().size() >= 1) {

                    saveDataToFile(tournament);

                    lockedClearTeamArray.set(true);
                    accordion.setExpandedPane(regClubsTP);
                    regClubsTP.setDisable(false);
                    tournament.printStructure();
                } else {
                    DataStorage.getInstance().msgBox("You must add at least one element", false);
                }

            } else if (button.getId().equalsIgnoreCase(regClubSaveBtn.getId())) {
                if (tournament.getClubs().size() >= 1) {

                    accordion.setExpandedPane(regPlayersTP);
                    regPlayersTP.setDisable(false);
                    saveDataToFile(tournament);

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

                if (!tournament.isIsTournamentSetUpCompleted()) {
                    //show a dialog box to confirrm deletion
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to write changes to the "
                            + "database? The Tournament cannot be further modified after the chagnes are applied. ", ButtonType.YES, ButtonType.NO);

                    alert.setTitle("Kendo-Cup Suite");
                    Stage s = (Stage) alert.getDialogPane().getScene().getWindow();
                    s.getIcons().add(new Image("resources/iconkendo.png"));
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.YES) {
                        if (tournament.getContestants().size() >= 1) {
                            //save all and close
                            tournament.setIsTournamentSetUpCompleted(true);
                            saveDataToFile(tournament);
                            tournament.printStructure();
                            lockedClearContestantArray.set(true);
                            tournament.printStructure();
                        } else {
                            DataStorage.getInstance().msgBox("You must add at least one element", false);
                        }
                    } else if (alert.getResult() == ButtonType.NO) {
                        alert.close();
                    }
                } else {
                    DataStorage.getInstance().msgBox("The Tournament is already saved in the database.", false);
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
            int poolNumber = Integer.parseInt(numOfPoolsTxt.getText());
            int pPlayers = Integer.parseInt(prefPerPoolTxt.getText());
            int mPlayers = Integer.parseInt(maxPerPoolTxt.getText());
            int shiaijosnum = Integer.parseInt(shiaijoTxt.getText());
            boolean teamCheckBoxChecked = isATeamChkBx.selectedProperty().get();

            Cup cup = new Cup(cupName, pPlayers, mPlayers, poolNumber, shiaijosnum);
            cup.setIsATeamChampionship(teamCheckBoxChecked);

            //add the cup in the tournament object, dont allow duplicates
            tournament.addCup(cup, false);

            //update the TableView
            ObservableList<Cup> observCups = FXCollections.observableList(tournament.getCups());
            cupTableView.setItems(observCups);
            setupCupTableView();
            regCompSaveBtn.setDisable(false);

        } catch (DuplicateName ex) {
            DataStorage.getInstance().msgBox("A Competition with this name already exists", false);
        } catch (NullPointerException ex) {
            DataStorage.getInstance().msgBox(ex.getMessage(), false);
        } catch (IllegalArgumentException ex) {
            DataStorage.getInstance().msgBox("You have entered invalid values or left mandatory fields (*) empty. ", false);
        } catch (Exception ex) {
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
            Team team = new Team(teamName, min, max);
            team.setBelongsToCup(cupname);

            //add the team to the tournament, don't allow duplicates
            tournament.addTeam(team, false);

            //update the TableView
            ObservableList<Team> observTeams = FXCollections.observableList(tournament.getTeams());
            teamTableView.setItems(observTeams);
            setupTeamTableView();

            //clear the fields for the next input
            teamNameTxt.clear();
            minMembersTxt.clear();
            maxMembersTxt.clear();
            cupNameCmb.getSelectionModel().clearSelection();
            regTeamSaveBtn.setDisable(false);

        } catch (DuplicateName ex) {
            DataStorage.getInstance().msgBox("A team with the same name already exists in this competition", false);

        } catch (NullPointerException ex) {
            DataStorage.getInstance().msgBox("You have entered invalid values or left mandatory fields (*) empty. ", false);

        } catch (Exception ex) {
            DataStorage.getInstance().msgBox(ex.getMessage(), false);

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
            Club club = new Club(clubName, clubNationality);

            //add the club to the tournament
            tournament.addClub(club, false);

            //update the TableView
            ObservableList<Club> observClubs = FXCollections.observableArrayList(tournament.getClubs());
            clubTableView.setItems(observClubs);
            setupClubTableView();

            clubNameTxt.clear();
            nationalityTxt.clear();
            regClubSaveBtn.setDisable(false);
        } catch (DuplicateName ex) {
            DataStorage.getInstance().msgBox("A club with the same name already exists in this competition.", false);
        } catch (NullPointerException ex) {
            DataStorage.getInstance().msgBox("You have entered invalid values or left mandatory fields (*) empty. ", false);
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

            if (!teamcmb.getSelectionModel().isEmpty()) {
                teamName = teamcmb.getSelectionModel().getSelectedItem().toString();
                contestant = new Contestant(fname, lname, gradeType, gradeLvl, gender, clubName, teamName);
            } else {

                contestant = new Contestant(fname, lname, gradeType, gradeLvl, gender, clubName);
            }

            tournament.addContestant(contestant, false);

            ObservableList<Contestant> observConts = FXCollections.observableArrayList(tournament.getContestants());
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

        } catch (NumberFormatException nfe) {
            DataStorage.getInstance().msgBox("Illegal number format. Only numbers allowed.No fields should be left empty.", false);
        } catch (IllegalArgumentException iae) {
            DataStorage.getInstance().msgBox("You entered invalid information (i.e value left blank, illegal characters used)", false);
        }catch (NullPointerException ex){
            DataStorage.getInstance().msgBox("You have entered invalid values or left mandatory fields (*) empty. ", false);
        }catch (Exception ex) {
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

        } catch (Exception ex) {
            DataStorage.getInstance().msgBox(ex.getMessage(), false);
        } finally {

        }

    }

    @FXML
    public void clear(ActionEvent event) {
        Button button = (Button) event.getSource();

        if (button.getId().equalsIgnoreCase(clearCupBtn.getId())) {
            try {
                Cup cup = (Cup) cupTableView.getSelectionModel().getSelectedItem();

                //Remove the selected cup from the tournament
                tournament.removeCup(cup);

                //Remove the row from TableView
                ObservableList<Cup> observCups = FXCollections.observableArrayList(tournament.getCups());
                cupTableView.setItems(observCups);
                setupClubTableView();
            } catch (Exception ex) {
                DataStorage.getInstance().msgBox(ex.getMessage(), false);
            }

        } else if (button.getId().equalsIgnoreCase(removeTeamBtn.getId())) {
            //Select an item from the tableview
            Team team = (Team) teamTableView.getSelectionModel().getSelectedItem();

            tournament.removeTeamFromCup(team);

            //Remove the row from TableView
            ObservableList<Team> observTeams = FXCollections.observableList(tournament.getTeams());
            teamTableView.setItems(observTeams);
            setupTeamTableView();

        } else if (button.getId().equalsIgnoreCase(removeClubBtn.getId())) {
            //Select an item from the tableview
            Club club = (Club) clubTableView.getSelectionModel().getSelectedItem();

            tournament.removeClubFromCup(club);

            //Remove the row from TableView
            ObservableList<Club> observClubs = FXCollections.observableList(tournament.getClubs());
            clubTableView.setItems(observClubs);
            setupClubTableView();

        } else if (button.getId().equalsIgnoreCase(removeContestantBtn.getId())) {
            //Select an item from the tableview
            Contestant contestant = (Contestant) playersTableView.getSelectionModel().getSelectedItem();

            tournament.removeContestantFromCup(contestant);

            //Remove the row from TableView
            ObservableList<Contestant> observContestants = FXCollections.observableList(tournament.getContestants());
            playersTableView.setItems(observContestants);
            setupPlayersTableView();
        }

    }

    @FXML
    public void back(ActionEvent event) {
        try {
            Node node = (Node) event.getSource();
            Stage window = (Stage) node.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MainMenu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            window.setMaximized(false);
            window.setResizable(false);
            window.setFullScreen(false);
            window.setScene(scene);
            window.show();

        } catch (IOException ex) {
            Logger.getLogger(MainMenuController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Assign the Tournament loaded in memory to a local variable
        tournament = DataStorage.getInstance().getLoadedTournament();

        //Setup the nosed of the GUI
        setUpControllers();

    }

    private void setUpControllers() {
        //1. Setup the first TitledPane to be always expanded
        accordion.setExpandedPane(regCompetitionsTP);

        //2. Bind clear Buttons with the locks flags
        clearCupBtn.disableProperty().bind(lockedClearCupArray);
        removeTeamBtn.disableProperty().bind(lockedClearTeamArray);
        removeClubBtn.disableProperty().bind(lockedClearClubArray);
        removeContestantBtn.disableProperty().bind(lockedClearContestantArray);

        //3.Setup the TableViews
        if (!tournament.getCups().isEmpty()) {

            ObservableList<Cup> observCups = FXCollections.observableList(tournament.getCups());
            cupTableView.setItems(observCups);
            setupCupTableView();
        }

        if (!tournament.getTeams().isEmpty()) {

            ObservableList<Team> observTeams = FXCollections.observableList(tournament.getTeams());
            teamTableView.setItems(observTeams);
            setupTeamTableView();
        }

        if (!tournament.getClubs().isEmpty()) {

            ObservableList<Club> observClubs = FXCollections.observableArrayList(tournament.getClubs());
            clubTableView.setItems(observClubs);
            setupClubTableView();
        }

        if (!tournament.getContestants().isEmpty()) {
            ObservableList<Contestant> observContestants = FXCollections.observableArrayList(tournament.getContestants());
            playersTableView.setItems(observContestants);
            setupPlayersTableView();
        }

        //4.Setup the clear buttons of each TitledPane
        setupClearButtons();

        //5.If the data are already saved in the Database enable all the title panes and disable the buttons that alter the tournament
        if (tournament.isIsTournamentSetUpCompleted()) {
            DataStorage.getInstance().msgBox("The Tournament is already saved to the database. "
                    + "Opening for viewing purposes only.", false);
            regCompetitionsTP.setDisable(!tournament.isIsTournamentSetUpCompleted());
            regTeamsTP.setDisable(!tournament.isIsTournamentSetUpCompleted());
            regClubsTP.setDisable(!tournament.isIsTournamentSetUpCompleted());
            regPlayersTP.setDisable(!tournament.isIsTournamentSetUpCompleted());

            submitCupBtn.setDisable(tournament.isIsTournamentSetUpCompleted());
            regCompSaveBtn.setDisable(tournament.isIsTournamentSetUpCompleted());
            addTeamBtn.setDisable(tournament.isIsTournamentSetUpCompleted());
            regTeamSaveBtn.setDisable(tournament.isIsTournamentSetUpCompleted());
            regClubSaveBtn.setDisable(tournament.isIsTournamentSetUpCompleted());
            addClubBtn.setDisable(tournament.isIsTournamentSetUpCompleted());
            saveBtn.setDisable(tournament.isIsTournamentSetUpCompleted());
            submitPayerBtn.setDisable(tournament.isIsTournamentSetUpCompleted());
            lockedClearContestantArray.set(tournament.isIsTournamentSetUpCompleted());
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

        ObservableList<Cup> observCups = FXCollections.observableArrayList(tournament.getCups());

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

    private void setupClearButtons() {
        //Check if the Arraylists are empty.
        //If they are empty that means that the user hasn't saved anything yet or 
        //closed the applcation before saving data to the file.
        //Therefore a deletion of a row should be allowed.
        if (tournament.getContestants().isEmpty()) {
            if (tournament.getClubs().isEmpty()) {
                if (tournament.getTeams().isEmpty()) {
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
        for (Club club : tournament.getClubs()) {
            clubnames.add(club.getClubName());
        }
        clubNamecmb.setItems(clubnames);
    }

    public void populateTeamComboBox() {
        teamcmb.setOnMouseClicked(e -> {
            ObservableList<String> teamslist = FXCollections.observableArrayList();
            for (Cup c : tournament.getCups()) {
                if (c.isIsATeamChampionship() && c.getTeamChampionshipsSelected() < 2 && c.includedInParticipationListProperty().get()) {
                    for (Team t : c.getTeams()) {
                        System.out.println(t.getTeamName());
                        teamslist.add(t.getTeamName());
                    }
                }
            }
            teamcmb.setItems(teamslist);
        });

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
            Logger.getLogger(TournamentSetupController.class
                    .getName()).log(Level.SEVERE, null, ex);
            DataStorage.getInstance().msgBox(ex.getMessage(), false);
        } finally {
            try {
                output.close();

            } catch (IOException ex) {
                Logger.getLogger(TournamentSetupController.class
                        .getName()).log(Level.SEVERE, null, ex);
                DataStorage.getInstance().msgBox(ex.getMessage(), false);
            }
        }
    }

}
