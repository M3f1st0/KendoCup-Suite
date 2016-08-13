/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import FunctionalityClasses.DataStorage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Panadmin
 */
public class MsgBoxController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label errorLbl;

    @FXML
    private ImageView imgV;

    private final Image imgFatal = new Image(getClass().getResourceAsStream("/resources/fatal.png"));
    private final Image imgNotice = new Image(getClass().getResourceAsStream("/resources/notice.png"));

    @FXML
    private void closeWindow(ActionEvent event) {
        if (!DataStorage.getInstance().isIsFatal()) {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        } else if (DataStorage.getInstance().isIsFatal()) {
            System.exit(1);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (DataStorage.getInstance().isIsFatal()) {
            imgV.setImage(imgFatal);
        } else if (!DataStorage.getInstance().isIsFatal()) {
            imgV.setImage(imgNotice);
        }
        errorLbl.setText(DataStorage.getInstance().getErrorMessage());
        
    }

}
