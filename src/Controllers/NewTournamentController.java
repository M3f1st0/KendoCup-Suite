/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Model.Tournament;
import FunctionalityClasses.DataStorage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author panos
 */
public class NewTournamentController implements Initializable {

    private ObjectOutputStream output = null;
    private Path path = null;

    @FXML
    private TextField dbNameTxt;

    /*
     * This method creates a new Tournament Object file which will contains
     * the Cup names (databases) of the event.
     * Each event is one Object file with several database.
     * Each database is a separate Cup. (i.e kyu, Team, 40+, Ladies etc)
     */
    @FXML
    public void saveAction(ActionEvent event) throws SQLException, IOException {
        if (!dbNameTxt.getText().isEmpty()) {
            //the suffix of the object file is .trm
            String tourName = dbNameTxt.getText() + ".trm";
            if (checkForDuplicate(tourName)) {
                try {
                    Tournament tournament = new Tournament(tourName);
                    DataStorage.getInstance().setOparationFromOtherWindowSuccessfull(true);
                    DataStorage.getInstance().setLoadedTournament(tournament);
                    dbNameTxt.clear();
                    output = new ObjectOutputStream(Files.newOutputStream(path));
                    output.writeObject(tournament);
                    DataStorage.getInstance().msgBox("The Tournament " + DataStorage.getInstance().getLoadedTournament().getTournamentName() + " was created succsessfully", false);

                    Node node = (Node) event.getSource();
                    Stage window = (Stage) node.getScene().getWindow();
                    window.close();
                } catch (IOException ex) {
                    Logger.getLogger(NewTournamentController.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    output.close();
                }

            } else {
                //pop up window to inform user
                DataStorage.getInstance().msgBox("The Tournament file was not created. Check that the name does not contain spaces or symbols or that the"
                        + " file does not already exists.", false);
            }
        } else {
            //pop up window to inform user
            DataStorage.getInstance().msgBox("Database name cannot be empty.", false);
        }

    }

    @FXML
    private void clickExitButton(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage window = (Stage) node.getScene().getWindow();
        window.close();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private boolean checkForDuplicate(String filename) {
        boolean isUnique = false;
        path = Paths.get("src/Tournaments/" + filename);
        if (Files.exists(path)) {
            isUnique = false;
        } else {
            isUnique = true;
        }

        return isUnique;
    }

}
