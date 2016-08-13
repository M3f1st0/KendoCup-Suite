/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Model.Contestant;
import Model.Match;
import Model.Pool;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import Model.SoundPlayer;
import FunctionalityClasses.DataStorage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Panagiotis Bitharis
 */
public class ShiaiAdminViewController implements Initializable {

    @FXML
    ImageView imgViewRBig, imgViewRsmall, imgViewWBig, imgViewWsmall;

    private ArrayList<TextField> textfieldsW = new ArrayList<>();
    private ArrayList<TextField> textfieldsR = new ArrayList<>();

    private ArrayList<IntegerProperty> ipponsArrayW = new ArrayList<>();
    private ArrayList<IntegerProperty> ipponsArrayR = new ArrayList<>();

    private ArrayList<Button> ipponButtonsW = new ArrayList<>();
    private ArrayList<Button> ipponButtonsR = new ArrayList<>();

    private SoundPlayer soundToPlay;
    //white ippons integer property variables
    private IntegerProperty menPropertyWhite = new SimpleIntegerProperty(0),
            kotePropertyWhite = new SimpleIntegerProperty(0),
            douPropertyWhite = new SimpleIntegerProperty(0),
            tsukiPropertyWhite = new SimpleIntegerProperty(0),
            ipponPropertyWhite = new SimpleIntegerProperty(0),
            ipponsTotalPropertyWhite = new SimpleIntegerProperty(0),
            warningPropertyWhite = new SimpleIntegerProperty(0),
            //Red ippons integerproperties variables
            menPropertyRed = new SimpleIntegerProperty(0),
            kotePropertyRed = new SimpleIntegerProperty(0),
            douPropertyRed = new SimpleIntegerProperty(0),
            tsukiPropertyRed = new SimpleIntegerProperty(0),
            ipponPropertyRed = new SimpleIntegerProperty(0),
            ipponsTotalPropertyRed = new SimpleIntegerProperty(0),
            warningPropertyRed = new SimpleIntegerProperty(0);

    private Timer timer;
    private int mm = 0;
    private int ss = 55;
    private int ms = 0;
    private String timeString;

    @FXML
    private Label timerLabel;

    @FXML
    private Button timerBtn, menBtnW, koteBtnW, doBtnW, tsukiBtnW, ipponBtnW, warningBtnW,
            menBtnR, koteBtnR, doBtnR, tsukiBtnR, ipponBtnR, warningBtnR, newPoolBtn, loadBtn, saveBtn, backBtn;

    @FXML
    private TextField menTxtW, koteTxtW, doTxtW, tsukiTxtW, ipponTxtW, warningTxtW, idTxtW, whiteNameTxt;

    @FXML
    private TextField menTxtR, koteTxtR, doTxtR, tsukiTxtR, ipponTxtR, warningTxtR, idTxtR, redNameTxt;

    private Pool pool = null;
    private Match match = null;
    private LinkedList<Match> poolMatches = new LinkedList<>();
    private Contestant white = null, red = null;

    /**
     * Initializes the controller class.
     */
    @FXML
    public void timerButtonAction(ActionEvent event) {
        if (timerBtn.getText().equalsIgnoreCase("Start Timer")) {
            timerBtn.setText("Stop Timer");
            countTime();

        } else {
            timerBtn.setText("Start Timer");
            timer.cancel();

        }
    }

    /*
    *Whenever an Ippon button is pressed, the related ippon is added on the white player 
    *and the Textfields in the adminView are updated with the values.
     */
    @FXML
    public void addpointsWhite(ActionEvent event) throws Exception {
        Node node = (Node) event.getSource();
        if (node.getId().equalsIgnoreCase(menBtnW.getId())) {
            menPropertyWhite.set(menPropertyWhite.getValue() + 1);
            System.out.println("Men Ippon:" + menPropertyWhite.toString());
            white.setMen(menPropertyWhite.get());
        } else if (node.getId().equalsIgnoreCase(koteBtnW.getId())) {
            kotePropertyWhite.set(kotePropertyWhite.getValue() + 1);
            System.out.println("Kote ippon:" + kotePropertyWhite.toString());
            white.setKote(kotePropertyWhite.get());
        } else if (node.getId().equalsIgnoreCase(doBtnW.getId())) {
            douPropertyWhite.set(douPropertyWhite.getValue() + 1);
            System.out.println("Dou ippon:" + douPropertyWhite.toString());
            white.setDou(douPropertyWhite.get());
        } else if (node.getId().equalsIgnoreCase(tsukiBtnW.getId())) {
            tsukiPropertyWhite.set(tsukiPropertyWhite.getValue() + 1);
            System.out.println("Tsuki ippon:" + tsukiPropertyWhite.toString());
            white.setTsuki(tsukiPropertyWhite.get());
        } else if (node.getId().equalsIgnoreCase(ipponBtnW.getId())) {
            ipponPropertyWhite.set(ipponPropertyWhite.getValue() + 1);
            System.out.println("Ippon:" + ipponPropertyWhite.toString());
            white.setIppon(ipponPropertyWhite.get());
        } else if (node.getId().equalsIgnoreCase(warningBtnW.getId())) {
            warningPropertyWhite.set(warningPropertyWhite.getValue() + 1);
            white.setHansoku(warningPropertyWhite.get());
            System.out.println("Warning:" + warningPropertyWhite.toString());
            if (warningPropertyWhite.getValue() % 2 == 0) {
                ipponPropertyRed.set(ipponPropertyRed.getValue() + 1);
                red.setIppon(ipponPropertyRed.get());

            }
        }
    }

    /*
    *Whenever an Ippon button is pressed, the related ippon is added on the red player 
    *and the Textfields in the adminView are updated with the values.
     */
    @FXML
    public void addpointsRed(ActionEvent event) throws Exception {
        Node node = (Node) event.getSource();
        if (node.getId().equalsIgnoreCase(menBtnR.getId())) {
            menPropertyRed.set(menPropertyRed.getValue() + 1);
            System.out.println("Men Ippon:" + menPropertyRed.toString());
            red.setMen(menPropertyRed.get());
        } else if (node.getId().equalsIgnoreCase(koteBtnR.getId())) {
            kotePropertyRed.set(kotePropertyRed.getValue() + 1);
            System.out.println("Kote ippon:" + kotePropertyRed.toString());
            red.setKote(kotePropertyRed.get());
        } else if (node.getId().equalsIgnoreCase(doBtnR.getId())) {
            douPropertyRed.set(douPropertyRed.getValue() + 1);
            System.out.println("Dou ippon:" + douPropertyRed.toString());
            red.setDou(douPropertyRed.get());
        } else if (node.getId().equalsIgnoreCase(tsukiBtnR.getId())) {
            tsukiPropertyRed.set(tsukiPropertyRed.getValue() + 1);
            System.out.println("Tsuki ippon:" + tsukiPropertyRed.toString());
            red.setTsuki(tsukiPropertyRed.get());
        } else if (node.getId().equalsIgnoreCase(ipponBtnR.getId())) {
            ipponPropertyRed.set(ipponPropertyRed.getValue() + 1);
            System.out.println("Ippon:" + ipponPropertyRed.toString());
            red.setIppon(ipponPropertyRed.get());
        } else if (node.getId().equalsIgnoreCase(warningBtnR.getId())) {
            warningPropertyRed.set(warningPropertyRed.getValue() + 1);
            System.out.println("Warning:" + warningPropertyRed.toString());
            red.setHansoku(warningPropertyRed.get());
            if (warningPropertyRed.getValue() % 2 == 0) {
                ipponPropertyWhite.set(ipponPropertyWhite.getValue() + 1);
                white.setIppon(ipponPropertyWhite.get());

            }
        }
    }

    @FXML
    public void backToMainMenu(ActionEvent event) {
        try {
            Node node = (Node) event.getSource();
            Stage window = (Stage) node.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MainMenu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void newPoolAction(ActionEvent event) {
        DataStorage.getInstance().setPreviousScene("/Views/ShiaiAdminView.fxml");
        try {
            //Node node = (Node) event.getSource();
            Stage window = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/NewPoolView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.getIcons().add(new Image(getClass().getResourceAsStream("/resources/iconkendo.png")));
            window.setTitle("Kendo-Cup Suite");
            newPoolBtn.setDisable(true);
            backBtn.setDisable(true);
            window.setAlwaysOnTop(true);
            window.showAndWait();
            enableControls();

            Stage shiaiView = new Stage();
            FXMLLoader shiaiVewLoader = new FXMLLoader(getClass().getResource("/Views/ShiaiViewFXML.fxml"));
            Parent shiaiViewRoot = shiaiVewLoader.load();
            Scene shiaiScene = new Scene(shiaiViewRoot);
            shiaiView.setScene(shiaiScene);
            shiaiView.getIcons().add(new Image(getClass().getResourceAsStream("/resources/iconkendo.png")));
            shiaiView.setTitle("Kendo-Cup Suite");
            shiaiView.show();

            loadNextPool();
            loadPoolMatches();

        } catch (IOException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void loadNextMatchAction(ActionEvent event) {
        loadnextMatch();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUpControllers();

    }

    public void setUpControllers() {
        soundToPlay = new SoundPlayer("src/Resources/bell.mp3");
        imgViewRBig.setImage(new Image(getClass().getResource("/resources/kendoRedtrngl.png").toString()));
        imgViewRsmall.setImage(new Image(getClass().getResource("/resources/kendoRedtrngl.png").toString()));
        imgViewWBig.setImage(new Image(getClass().getResource("/resources/kendoRedtrngl.png").toString()));
        imgViewWsmall.setImage(new Image(getClass().getResource("/resources/kendoRedtrngl.png").toString()));
        putNodesIntoArrayLists();
        bindProperties();
        editIpponsWhite();
        editIpponsRed();
    }

    private void putNodesIntoArrayLists() {
        textfieldsW.add(idTxtW);
        textfieldsW.add(whiteNameTxt);
        textfieldsW.add(menTxtW);
        textfieldsW.add(koteTxtW);
        textfieldsW.add(doTxtW);
        textfieldsW.add(tsukiTxtW);
        textfieldsW.add(ipponTxtW);
        textfieldsW.add(warningTxtW);

        ipponsArrayW.add(menPropertyWhite);
        ipponsArrayW.add(kotePropertyWhite);
        ipponsArrayW.add(douPropertyWhite);
        ipponsArrayW.add(tsukiPropertyWhite);
        ipponsArrayW.add(ipponPropertyWhite);
        ipponsArrayW.add(warningPropertyWhite);

        ipponButtonsW.add(menBtnW);
        ipponButtonsW.add(koteBtnW);
        ipponButtonsW.add(doBtnW);
        ipponButtonsW.add(tsukiBtnW);
        ipponButtonsW.add(ipponBtnW);
        ipponButtonsW.add(warningBtnW);

        //**************RED Controllers*************************//
        textfieldsR.add(idTxtR);
        textfieldsR.add(redNameTxt);
        textfieldsR.add(menTxtR);
        textfieldsR.add(koteTxtR);
        textfieldsR.add(doTxtR);
        textfieldsR.add(tsukiTxtR);
        textfieldsR.add(ipponTxtR);
        textfieldsR.add(warningTxtR);

        ipponsArrayR.add(menPropertyRed);
        ipponsArrayR.add(kotePropertyRed);
        ipponsArrayR.add(douPropertyRed);
        ipponsArrayR.add(tsukiPropertyRed);
        ipponsArrayR.add(ipponPropertyRed);
        ipponsArrayR.add(warningPropertyRed);

        ipponButtonsR.add(menBtnR);
        ipponButtonsR.add(koteBtnR);
        ipponButtonsR.add(doBtnR);
        ipponButtonsR.add(tsukiBtnR);
        ipponButtonsR.add(ipponBtnR);
        ipponButtonsR.add(warningBtnR);

    }

    private void bindProperties() {
        menTxtW.textProperty().bind(menPropertyWhite.asString());
        koteTxtW.textProperty().bind(kotePropertyWhite.asString());
        doTxtW.textProperty().bind(douPropertyWhite.asString());
        tsukiTxtW.textProperty().bind(tsukiPropertyWhite.asString());
        ipponTxtW.textProperty().bind(ipponPropertyWhite.asString());
        warningTxtW.textProperty().bind(warningPropertyWhite.asString());

        menTxtR.textProperty().bind(menPropertyRed.asString());
        koteTxtR.textProperty().bind(kotePropertyRed.asString());
        doTxtR.textProperty().bind(douPropertyRed.asString());
        tsukiTxtR.textProperty().bind(tsukiPropertyRed.asString());
        ipponTxtR.textProperty().bind(ipponPropertyRed.asString());
        warningTxtR.textProperty().bind(warningPropertyRed.asString());
    }

    private void editIpponsWhite() {
        menTxtW.setFocusTraversable(true);
        menTxtW.setOnKeyTyped(e -> {
            if (e.getCharacter().equalsIgnoreCase("-")) {
                if (menPropertyWhite.getValue() >= 1) {
                    menPropertyWhite.set(menPropertyWhite.getValue() - 1);
                    System.out.println(menPropertyWhite.getValue().toString());
                    try {
                        white.setMen(menPropertyWhite.get());
                    } catch (Exception ex) {
                        DataStorage.getInstance().msgBox(ex.getMessage(), false);
                    }
                }
            }
        });

        koteTxtW.setFocusTraversable(true);
        koteTxtW.setOnKeyTyped(e -> {
            if (e.getCharacter().equalsIgnoreCase("-")) {
                if (kotePropertyWhite.getValue() >= 1) {
                    kotePropertyWhite.set(kotePropertyWhite.getValue() - 1);
                    System.out.println(kotePropertyWhite.getValue().toString());
                    try {
                        white.setKote(kotePropertyWhite.get());
                    } catch (Exception ex) {
                        DataStorage.getInstance().msgBox(ex.getMessage(), false);
                    }
                }
            }
        });

        doTxtW.setFocusTraversable(true);
        doTxtW.setOnKeyTyped(e -> {
            if (e.getCharacter().equalsIgnoreCase("-")) {
                if (douPropertyWhite.getValue() >= 1) {
                    douPropertyWhite.set(douPropertyWhite.getValue() - 1);
                    System.out.println(douPropertyWhite.getValue().toString());
                    try {
                        white.setDou(douPropertyWhite.get());
                    } catch (Exception ex) {
                        DataStorage.getInstance().msgBox(ex.getMessage(), false);
                    }
                }
            }
        });

        tsukiTxtW.setFocusTraversable(true);
        tsukiTxtW.setOnKeyTyped(e -> {
            if (e.getCharacter().equalsIgnoreCase("-")) {
                if (tsukiPropertyWhite.getValue() >= 1) {
                    tsukiPropertyWhite.set(tsukiPropertyWhite.getValue() - 1);
                    System.out.println(tsukiPropertyWhite.getValue().toString());
                    try {
                        white.setTsuki(tsukiPropertyWhite.get());
                    } catch (Exception ex) {
                        DataStorage.getInstance().msgBox(ex.getMessage(), false);
                    }
                }
            }
        });

        ipponTxtW.setFocusTraversable(true);
        ipponTxtW.setOnKeyTyped(e -> {
            if (e.getCharacter().equalsIgnoreCase("-")) {
                if (ipponPropertyWhite.getValue() >= 1) {
                    ipponPropertyWhite.set(ipponPropertyWhite.getValue() - 1);
                    System.out.println(ipponPropertyWhite.getValue().toString());
                    try {
                        white.setIppon(ipponPropertyWhite.get());
                    } catch (Exception ex) {
                        DataStorage.getInstance().msgBox(ex.getMessage(), false);
                    }
                }
            }
        });

        warningTxtW.setFocusTraversable(true);
        warningTxtW.setOnKeyTyped(e -> {
            if (e.getCharacter().equalsIgnoreCase("-")) {
                if (warningPropertyWhite.getValue() >= 1) {
                    warningPropertyWhite.set(warningPropertyWhite.getValue() - 1);
                    try {
                        white.setHansoku(warningPropertyWhite.get());
                    } catch (Exception ex) {
                        DataStorage.getInstance().msgBox(ex.getMessage(), false);
                    }
                    if (warningPropertyWhite.getValue() % 2 != 0) {
                        ipponPropertyRed.set(ipponPropertyRed.getValue() - 1);
                        try {
                            red.setIppon(ipponPropertyRed.get());
                        } catch (Exception ex) {
                            DataStorage.getInstance().msgBox(ex.getMessage(), false);
                        }
                    }
                    System.out.println(warningPropertyWhite.getValue().toString());
                }
            }
        });

    }

    private void editIpponsRed() {
        menTxtR.setFocusTraversable(true);
        menTxtR.setOnKeyTyped(e -> {
            if (e.getCharacter().equalsIgnoreCase("-")) {
                if (menPropertyRed.getValue() >= 1) {
                    menPropertyRed.set(menPropertyRed.getValue() - 1);
                    System.out.println(menPropertyRed.getValue().toString());
                    try {
                        red.setMen(menPropertyRed.get());
                    } catch (Exception ex) {
                        DataStorage.getInstance().msgBox(ex.getMessage(), false);
                    }
                }
            }
        });

        koteTxtR.setFocusTraversable(true);
        koteTxtR.setOnKeyTyped(e -> {
            if (e.getCharacter().equalsIgnoreCase("-")) {
                if (kotePropertyRed.getValue() >= 1) {
                    kotePropertyRed.set(kotePropertyRed.getValue() - 1);
                    System.out.println(kotePropertyRed.getValue().toString());
                    try {
                        red.setKote(kotePropertyRed.get());
                    } catch (Exception ex) {
                        DataStorage.getInstance().msgBox(ex.getMessage(), false);
                    }
                }
            }
        });

        doTxtR.setFocusTraversable(true);
        doTxtR.setOnKeyTyped(e -> {
            if (e.getCharacter().equalsIgnoreCase("-")) {
                if (douPropertyRed.getValue() >= 1) {
                    douPropertyRed.set(douPropertyRed.getValue() - 1);
                    System.out.println(douPropertyRed.getValue().toString());
                    try {
                        red.setDou(douPropertyRed.get());
                    } catch (Exception ex) {
                        DataStorage.getInstance().msgBox(ex.getMessage(), false);
                    }
                }
            }
        });

        tsukiTxtR.setFocusTraversable(true);
        tsukiTxtR.setOnKeyTyped(e -> {
            if (e.getCharacter().equalsIgnoreCase("-")) {
                if (tsukiPropertyRed.getValue() >= 1) {
                    tsukiPropertyRed.set(tsukiPropertyRed.getValue() - 1);
                    System.out.println(tsukiPropertyRed.getValue().toString());
                    try {
                        red.setTsuki(tsukiPropertyRed.get());
                    } catch (Exception ex) {
                        DataStorage.getInstance().msgBox(ex.getMessage(), false);
                    }
                }
            }
        });

        ipponTxtR.setFocusTraversable(true);
        ipponTxtR.setOnKeyTyped(e -> {
            if (e.getCharacter().equalsIgnoreCase("-")) {
                if (ipponPropertyRed.getValue() >= 1) {
                    ipponPropertyRed.set(ipponPropertyRed.getValue() - 1);
                    System.out.println(ipponPropertyRed.getValue().toString());
                    try {
                        red.setIppon(ipponPropertyRed.get());
                    } catch (Exception ex) {
                        DataStorage.getInstance().msgBox(ex.getMessage(), false);
                    }
                }
            }
        });

        warningTxtR.setFocusTraversable(true);
        warningTxtR.setOnKeyTyped(e -> {
            if (e.getCharacter().equalsIgnoreCase("-")) {
                if (warningPropertyRed.getValue() >= 1) {
                    warningPropertyRed.set(warningPropertyRed.getValue() - 1);
                    try {
                        red.setHansoku(warningPropertyRed.get());
                    } catch (Exception ex) {
                        DataStorage.getInstance().msgBox(ex.getMessage(), false);
                    }
                    if (warningPropertyRed.getValue() % 2 != 0) {
                        ipponPropertyWhite.set(ipponPropertyWhite.getValue() - 1);
                        try {
                            white.setIppon(ipponPropertyWhite.get());
                        } catch (Exception ex) {
                            DataStorage.getInstance().msgBox(ex.getMessage(), false);
                        }
                    }
                    System.out.println(warningPropertyRed.getValue().toString());
                }
            }
        });

    }

    private void enableControls() {
        for (Button b : ipponButtonsW) {
            b.setDisable(false);
        }

        for (Button b : ipponButtonsR) {
            b.setDisable(false);
        }

        for (TextField t : textfieldsW) {
            t.setDisable(false);
        }

        for (TextField t : textfieldsR) {
            t.setDisable(false);
        }

        timerBtn.setDisable(false);
        newPoolBtn.setDisable(false);
        backBtn.setDisable(false);
        loadBtn.setDisable(false);
        saveBtn.setDisable(false);
    }

    private void countTime() {
        timer = new Timer("Timer", true);
        TimerTask timerTask;
        timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(() -> {

                    if (ms < 1000) {
                        ms++;

                    } else if (ms == 1000) {
                        if (ss < 59) {
                            ss++;
                        } else if (ss == 59) {
                            ss = 0;
                            mm++;
                        }
                        ms = 0;
                    }

                    timeString = String.format("%02d:%02d", mm, ss);
                    //System.out.format("%02d:%02d%n", mm, ss);
                    timerLabel.setText(timeString);
                    if (mm >= 1 && ms == 0) {
                        soundToPlay.play();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1);
    }

    private void loadnextMatch() {
        if (!poolMatches.isEmpty()) {
            match = poolMatches.poll();
            idTxtW.setText(String.valueOf(match.getWhite().getID()));
            whiteNameTxt.setText(match.getWhite().getFirstName().charAt(0) + ". "
                    + match.getWhite().getLastName());

            idTxtR.setText(String.valueOf(match.getRed().getID()));
            redNameTxt.setText(match.getRed().getFirstName().charAt(0) + ". " + 
                    match.getRed().getLastName());
        }

    }

    private void loadNextPool() {
        pool = DataStorage.getInstance().getNextPool();
    }

    private void loadPoolMatches() {
        if (pool != null) {
            ArrayList<Match> tmp = pool.getMatches();
            for (Match m : tmp) {
                poolMatches.offer(m);
            }
        }else{
            System.out.println("Empty pools");
        }

    }
}
