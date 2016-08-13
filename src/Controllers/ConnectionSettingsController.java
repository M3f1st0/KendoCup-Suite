/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import FunctionalityClasses.ConnectionSettings;
import FunctionalityClasses.DBHandler;
import FunctionalityClasses.DataStorage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author panos
 */
public class ConnectionSettingsController implements Initializable {

    @FXML
    private TextField serverAddressTxt, portNumberTxt, usernameTxt;

    @FXML
    private PasswordField passwordTxt;

    private String addr, port, uname, pass;

    private Path connectionSettingsFile = Paths.get("resources/mysql_conn.conf");
    private ObjectOutputStream output = null;

    @FXML
    public void saveAction(ActionEvent event) {
        addr = serverAddressTxt.getText();
        port = portNumberTxt.getText();
        uname = usernameTxt.getText();
        pass = passwordTxt.getText();

        try {
            ConnectionSettings settings = new ConnectionSettings(addr, port, uname, pass);
            output = new ObjectOutputStream(Files.newOutputStream(connectionSettingsFile));
            output.writeObject(settings);
            if (DBHandler.connect(addr, port, uname, pass)) {
                try {
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource("/Views/MainMenu.fxml"));

                    Scene scene = new Scene(root);

                    stage.setScene(scene);
                    stage.setFullScreen(false);
                    stage.setResizable(false);
                    stage.setTitle("Kendo-Cup Suite");
                    stage.getIcons().add(new Image("resources/iconkendo.png"));
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(ConnectionSettingsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                //load a pop up window that connection has failed
                DataStorage.getInstance().msgBox("Could not connect with MySQL server. Make sure that the values you enter are valid (port, address ,"
                        + "username and password).", true);
            }

        } catch (Exception ex) {
            Logger.getLogger(ConnectionSettingsController.class.getName()).log(Level.SEVERE, null, ex);
        }

//        
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

}
