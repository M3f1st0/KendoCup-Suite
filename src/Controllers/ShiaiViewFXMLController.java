/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import FunctionalityClasses.DataStorage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author panos
 */
public class ShiaiViewFXMLController implements Initializable {
    @FXML
    private Label whiteNameTxt,redNameTxt;
    
    @FXML
    private GridPane imgContainerWhite, imgContainerRed;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Stage stage = (Stage)whiteNameTxt.getScene().getWindow();
        
    }    
    
}
