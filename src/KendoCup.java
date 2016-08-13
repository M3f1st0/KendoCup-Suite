/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Controllers.ConnectionSettingsController;
import FunctionalityClasses.ConnectionSettings;
import FunctionalityClasses.DBHandler;
import FunctionalityClasses.DataStorage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Panadmin
 */
public class KendoCup extends Application {

    private Path connectionSettingsFile =  Paths.get("src/resources/mysql_conn.conf");

    @Override
    public void start(Stage stage) throws Exception {

        if (Files.exists(connectionSettingsFile)) {
            try {
                //read the file and load
                ObjectInputStream input = new ObjectInputStream(Files.newInputStream(connectionSettingsFile));
                
                ConnectionSettings conn = (ConnectionSettings) input.readObject();

                if (DBHandler.connect(conn.getServerAddress(), conn.getPortNumber(), conn.getUsername(), conn.getPassword())) {
                    Parent mainViewRoot = FXMLLoader.load(getClass().getResource("/Views/MainMenu.fxml"));

                    Scene scene = new Scene(mainViewRoot);

                    stage.setScene(scene);
                    stage.setFullScreen(false);
                    stage.setResizable(false);
                    stage.setTitle("Kendo-Cup Suite");
                    stage.getIcons().add(new Image("resources/iconkendo.png"));
                    stage.show();
                } else {
                    //load a popup window to inform the user.
                    DataStorage.getInstance().msgBox("Could not connect with MySQL server or Database. Please check connection settings.", false);
                    Parent root = FXMLLoader.load(getClass().getResource("Views/ConnectionSettings.fxml"));
                    Scene scene = new Scene(root);

                    stage.setScene(scene);
                    stage.setFullScreen(false);
                    stage.setResizable(false);
                    stage.setTitle("Kendo-Cup Suite");
                    stage.getIcons().add(new Image("resources/iconkendo.png"));
                    stage.show();
                }

            } catch (IOException ex) {
                Logger.getLogger(ConnectionSettingsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (Files.notExists(connectionSettingsFile)) {
            
            DataStorage.getInstance().msgBox("mysql_conn.conf not found", false);
            Parent root = FXMLLoader.load(getClass().getResource("Views/ConnectionSettings.fxml"));
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setFullScreen(false);
            stage.setResizable(false);
            stage.setTitle("Kendo-Cup Suite");
            stage.getIcons().add(new Image("resources/iconkendo.png"));
            stage.show();
        }
        

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
