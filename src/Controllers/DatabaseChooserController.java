/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Model.Tournament;
import FunctionalityClasses.DataStorage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Panadmin
 */
public class DatabaseChooserController implements Initializable {

    @FXML
    private ListView databasesLV;
    @FXML
    private Button cancelBtn, okBtn;

    private ObservableList<String> tournaments = null;
    private ObjectInputStream input = null;

    
    /**
    *Closes the window and returns to the previous screen
    */
    @FXML
    private void clickExitButton(ActionEvent event) {
        //if this method is called from loadDB should make the check if there was a deletion
        //in which case the Operations is successfull.
        if (DataStorage.getInstance().isOparationFromOtherWindowSuccessfull()) {
            Node node = (Node) event.getSource();
            Stage window = (Stage) node.getScene().getWindow();
            window.close();
        } else {
            DataStorage.getInstance().setOparationFromOtherWindowSuccessfull(false);
            Node node = (Node) event.getSource();
            Stage window = (Stage) node.getScene().getWindow();
            window.close();
        }

    }

    /**
    *We choose from the ListView the database we want
    *We save to memory which database we have selected
    *this database will stay in memory until another one is chosen
    *or the application restarts
     */
    @FXML
    private void loadDB(ActionEvent event) throws ClassNotFoundException, IOException {
        String tournamentName = databasesLV.getSelectionModel().getSelectedItem().toString();
        System.out.println(tournamentName);
        if (!tournamentName.isEmpty()) {
            try {
                input = new ObjectInputStream(Files.newInputStream(Paths.get("src/Tournaments/" + tournamentName)));
                DataStorage.getInstance().setLoadedTournament((Tournament) input.readObject());
                if (!tournamentName.isEmpty()) {
                    DataStorage.getInstance().setOparationFromOtherWindowSuccessfull(true);
                    clickExitButton(event);
                }
            } catch (IOException ex) {
                Logger.getLogger(DatabaseChooserController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                input.close();
            }
        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            tournaments = loadTournamentFiles();
            databasesLV.setItems(tournaments);
        } catch (IOException ex) {
            Logger.getLogger(DatabaseChooserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
    * Read the folder Tournaments and load all the files in an ObservableList
    */
    private ObservableList<String> loadTournamentFiles() throws IOException {
        ObservableList<String> t = FXCollections.observableArrayList();

        Files.walk(Paths.get("src/Tournaments")).forEach(filePath -> {

            if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                System.out.println(filePath.getFileName().toString());
                t.add(filePath.getFileName().toString());
            } else {
                System.out.println("Empty tournaments folder");

            }
        });

        return t;
    }

}
