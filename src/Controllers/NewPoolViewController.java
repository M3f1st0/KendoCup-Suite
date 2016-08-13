/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Model.Contestant;
import Model.Match;
import Model.Pool;
import FunctionalityClasses.DataStorage;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Panagiotis Bitharis
 */
public class NewPoolViewController implements Initializable {

    @FXML
    private ComboBox cupIDcmb;
    @FXML
    private Button saveBtn, submitBtn;
    @FXML
    private TextField poolIDtxt, whiteIDtxt, whiteFirstNametxt, whiteLastNametxt, redIDtxt, redFirstNametxt, redLastNametxt;
    @FXML
    private TableView matchTable;
    private TableColumn whiteColumn;
    private TableColumn redColumn;

    private String poolID = new String();
    private String cupID = new String();
    private String whiteID = new String();
    private String whiteFName = new String();
    private String whiteLName = new String();
    private String redID = new String();
    private String redFName = new String();
    private String redLName = new String();
    private Pool pool;
    private ArrayList<Match> matchesOL = new ArrayList<>();
    private ArrayList<TextField> textFieldsArray = new ArrayList<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUpControllers();

        //saves only one pool every time and closes the window
        saveBtn.setOnMouseClicked(e -> {
            pool = new Pool(matchesOL, poolID);
            DataStorage.getInstance().addPool(pool);
            Node node = (Node) e.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        });

        submitBtn.setOnMouseClicked(e -> {
            if (!whiteIDtxt.getText().isEmpty() || !redIDtxt.getText().isEmpty()) {
                matchTable.getColumns().removeAll(whiteColumn, redColumn);
                poolID = poolIDtxt.getText();

                addMatchToObservableList();
                ObservableList<Match> obsMathsesOL = FXCollections.observableArrayList(matchesOL);
                matchTable.setItems(obsMathsesOL);

                whiteColumn = new TableColumn("White");
                redColumn = new TableColumn("Red");

                whiteColumn.setCellValueFactory(new PropertyValueFactory("whiteToString"));
                whiteColumn.setPrefWidth(matchTable.getWidth() / 2);

                redColumn.setCellValueFactory(new PropertyValueFactory("redToString"));
                redColumn.setPrefWidth(matchTable.getWidth() / 2);

                matchTable.getColumns().addAll(whiteColumn, redColumn);
                clearTextFields();
            }

        });

    }

    private void addMatchToObservableList() {
        try {
            whiteID = whiteIDtxt.getText();
            whiteFName = whiteFirstNametxt.getText();
            whiteLName = whiteLastNametxt.getText();
            
            redID = redIDtxt.getText();
            redFName = redFirstNametxt.getText();
            redLName = redLastNametxt.getText();
            
            Contestant wContestant = new Contestant(whiteFName, whiteLName, "white", Integer.parseInt(whiteID));
            Contestant rContestant = new Contestant(redFName, redLName, "red", Integer.parseInt(redID));
            Match m = new Match(wContestant, rContestant);
            m.whiteToStringProperty();
            m.redToStringProperty();
            matchesOL.add(m);
        } catch (Exception ex) {
            Logger.getLogger(NewPoolViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void clearTextFields() {
        for (TextField t : textFieldsArray) {
            t.setText("");
        }
    }

    private void setUpControllers() {

        textFieldsArray.add(whiteIDtxt);
        textFieldsArray.add(whiteFirstNametxt);
        textFieldsArray.add(whiteLastNametxt);

        textFieldsArray.add(redIDtxt);
        textFieldsArray.add(redFirstNametxt);
        textFieldsArray.add(redLastNametxt);
    }

}
