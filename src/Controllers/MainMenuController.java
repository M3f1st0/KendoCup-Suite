package Controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import FunctionalityClasses.DataStorage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Panagiotis Bitaris
 */
public class MainMenuController implements Initializable {

    @FXML
    private ImageView imgView;

    /*
    MenuBar, Menu and MenuItems
     */
    @FXML
    private MenuBar myMenuBar;
    @FXML
    private Menu menuFile;
    @FXML
    private MenuItem newDBMenu, OpenDBMenu, exitMenu, connectionSettingsMenu, deleteMenu;

    /*
    * Buttons
    */
    @FXML
    private Button newDBBtn, openDBBtn, tourBtn, exitBtn;

    private StringProperty previousScene = new SimpleStringProperty();

    @FXML
    private void clickNewTournamentAction(ActionEvent event) {
        DataStorage.getInstance().setPreviousScene("/Views/MainMenu.fxml");
        try {

            Stage window = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/NewTournamentView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.setResizable(false);
            window.setFullScreen(false);
            window.setAlwaysOnTop(true);
            window.setTitle("Kendo-Cup Suite");
            window.getIcons().add(new Image("resources/iconkendo.png"));
            window.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void clickExitButton(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage window = (Stage) node.getScene().getWindow();
        window.close();
    }

    @FXML
    private void enterTournament(ActionEvent event) {
        DataStorage.getInstance().setPreviousScene("/Views/MainMenu.fxml");
        try {
            Node node = (Node) event.getSource();
            Stage window = (Stage) node.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ShiaiAdminView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.setResizable(false);
            window.setFullScreen(false);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void tournamentSetup(ActionEvent event) {
        DataStorage.getInstance().setPreviousScene("/Views/MainMenu.fxml");
        try {
            Node node = (Node) event.getSource();
            Stage window = (Stage) node.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/TournamentSetupView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.setResizable(false);
            window.setFullScreen(false);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openTournament(ActionEvent event) {
        DataStorage.getInstance().setPreviousScene("/Views/MainMenu.fxml");
        try {

            Stage window = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DatabaseChooser.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.setResizable(false);
            window.setFullScreen(false);
            window.setAlwaysOnTop(true);
            window.setTitle("Kendo-Cup Suite");
            window.getIcons().add(new Image("resources/iconkendo.png"));
            window.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupControllers();
    }

    private void setupControllers() {
        imgView.setImage(new Image(getClass().getResource("/resources/iconkendo.png").toString()));
        
        //These buttons should be enabled only if a tournament is Loaded in memory
        //We check the Datastorage Class (Singleton) isTournamentInMemory() method.
        openDBBtn.disableProperty().bind(DataStorage.getInstance().isTournamentInMemory().not());
        tourBtn.disableProperty().bind(DataStorage.getInstance().isTournamentInMemory().not());
        
        
        //close the application
        exitMenu.setOnAction(e -> {
            System.exit(0);
        });

        
        connectionSettingsMenu.setOnAction(e -> {
            DataStorage.getInstance().setPreviousScene("/Views/MainMenu.fxml");
            try {

                Stage window = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConnectionSettings.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                window.setScene(scene);
                window.setResizable(false);
                window.setFullScreen(false);
                window.setAlwaysOnTop(true);
                window.setTitle("Kendo-Cup Suite");
                window.getIcons().add(new Image("resources/iconkendo.png"));
                window.showAndWait();

            } catch (IOException ex) {
                Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        deleteMenu.setOnAction(e -> {
            openTournament(e);
            //Check if we have a loaded Tournament in memory and the operations were successfull
            if (DataStorage.getInstance().isTournamentInMemory().getValue() 
                    && DataStorage.getInstance().isOparationFromOtherWindowSuccessfull()) {
                
                //show a dialog box to confirrm deletion
                Alert alert = new Alert(AlertType.NONE, "Are you sure you want to Delete "
                        + DataStorage.getInstance().getLoadedTournament().getTournamentName()
                        + " from your available tournaments?", ButtonType.YES, ButtonType.CANCEL);
                
                alert.setTitle("Kendo-Cup Suite");
                Stage s = (Stage) alert.getDialogPane().getScene().getWindow();
                s.getIcons().add(new Image("resources/iconkendo.png"));
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    Path p = Paths.get("src/Tournaments/" + DataStorage.getInstance().getLoadedTournament().getTournamentName());
                    try {
                        Files.delete(p);
                        DataStorage.getInstance().unloadDeletedTournamentFromMemory();
                    } catch (IOException ex) {
                        Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (alert.getResult() == ButtonType.CANCEL) {
                    alert.close();
                }
            }
        });
    }

}
