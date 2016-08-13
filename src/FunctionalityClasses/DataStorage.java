package FunctionalityClasses;

import Model.Pool;
import Model.Tournament;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Panagiotis Bitharis
 */
public class DataStorage {

    private static DataStorage myDataStorage;
    
    /*
    Database related variables
    */
    private Tournament loadedTournament=null;           //this variable holds the object loaded from the trm object file.
    private boolean OparationFromOtherWindowSuccessfull=false;
    private BooleanProperty isLoaded = new SimpleBooleanProperty(false);
    private StringProperty previousScene = new SimpleStringProperty();
    
    private StringProperty whiteName = new SimpleStringProperty();
    private StringProperty redName = new SimpleStringProperty();
    private LinkedList<Pool> pools = new LinkedList<>();

    //To be used by the MsgBox window
    private String errorMessage;
    private boolean isFatal = false;

    private DataStorage() {
    }

    public static DataStorage getInstance() {
        if (myDataStorage == null) {
            myDataStorage = new DataStorage();
        }

        return myDataStorage;
    }

    public String getPreviousScene() {
        return previousScene.get();
    }

    public void setPreviousScene(String previousScene) {
        this.previousScene.set(previousScene);
    }

    public StringProperty previousSceneProperty() {
        return previousScene;
    }

    /**
     * @return the pools
     */
    public LinkedList<Pool> getPools() {
        return pools;
    }

    /**
     * @param pools the pools to set
     */
    public void addPool(Pool p) {
        pools.offer(p);
    }

    public Pool getNextPool() {
        Pool p = null;
        if (!pools.isEmpty()) {
            p = pools.poll();
        }
        return p;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the isFatal
     */
    public boolean isIsFatal() {
        return isFatal;
    }

    /**
     * @param isFatal the isFatal to set
     */
    public void setIsFatal(boolean isFatal) {
        this.isFatal = isFatal;
    }

    public void msgBox(String errorMessage, boolean isErrorFatal) {
        try {
            DataStorage.getInstance().setErrorMessage(errorMessage);
            DataStorage.getInstance().setIsFatal(isErrorFatal);
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/Views/msgBox.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Kendo-cup suite");
            stage.setAlwaysOnTop(true);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(DataStorage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    

    /**
     * @return the OparationFromOtherWindowSuccessfull
     */
    public boolean isOparationFromOtherWindowSuccessfull() {
        return OparationFromOtherWindowSuccessfull;
    }

    /**
     * @param OparationFromOtherWindowSuccessfull the OparationFromOtherWindowSuccessfull to set
     */
    public void setOparationFromOtherWindowSuccessfull(boolean OparationFromOtherWindowSuccessfull) {
        this.OparationFromOtherWindowSuccessfull = OparationFromOtherWindowSuccessfull;
    }

    /**
     * @return the loadedTournament
     */
    public Tournament getLoadedTournament() {
        System.out.println("Tournament Object Loaded in RAM: "+loadedTournament.getTournamentName());
        
        return loadedTournament;
    }

    /**
     * @param loadedTournament the loadedTournament to set
     */
    public void setLoadedTournament(Tournament loadedTournament) {
        
        this.loadedTournament = loadedTournament;
        System.out.println("Loading Tournament object in RAM: "+this.loadedTournament.getTournamentName());
        isLoaded.setValue(true);
    }
    
    public void unloadDeletedTournamentFromMemory(){
        loadedTournament = null;
        isLoaded.setValue(false);
        
    }
    
    public BooleanProperty isTournamentInMemory(){
        return isLoaded;
    }

    

}
