/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FunctionalityClasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Panadmin
 */
public class DBHandler {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static String DB_URL;
    private static String USER;
    private static String PASS;   
    private static Connection connection = null;
    private static PreparedStatement stmt = null;
    private static String dbname;
    private static final String DB_PREFIX = "kcs_";//the prefix of all databases created by this aplication

    //Prepared String for prepared Statements.
    private static final String showDatabasesStatement = "show databases  like 'kcs_%';";

    public static boolean connect(String addr, String port, String uname, String pass) {
        boolean connectionEstablished = false;
        try {

            DB_URL = "jdbc:mysql://" + addr + "/";
            USER = uname;
            PASS = pass;
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            if (connection.isClosed()) {
                connectionEstablished = false;
            } else {
                connectionEstablished = true;
            }          
        } catch (SQLException se) {

        } catch (ClassNotFoundException e) {

        }
        System.out.println("Goodbye!");
        return connectionEstablished;
    }//end main

    public static boolean createTournament(String dbName) throws SQLException {
        boolean success = false;
        dbname = "";//clear any previous names in case multiple creation of tournaments
        dbname = dbname + DB_PREFIX + dbName;

        success = createDatabase(dbname);
        success = createTableClubs();
        success = createTableTeams();
        success = createTableShiaiJo();
        success = createTablePools();
        success = createTableContestants();
        return success;

    }

    private static boolean createDatabase(String dbName) {
        boolean success = true;
        try {
            //STEP 4: Execute a query
            System.out.println("Creating database...");
            String sql = "CREATE SCHEMA IF NOT EXISTS " + dbName + " DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            System.out.println("Database created successfully...");
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }
        return success;
    }

    private static boolean createTableClubs() {
        boolean success = true;
        try {

            System.out.println("Creating table 'Clubs'...");

            String sql = "CREATE TABLE IF NOT EXISTS " + dbname + ".`Clubs` (\n"
                    + "  `ID` INT NOT NULL AUTO_INCREMENT,\n"
                    + "  `ClubName` VARCHAR(45) NULL,\n"
                    + "  `Nationality` VARCHAR(45) NULL,\n"
                    + "  PRIMARY KEY (`ID`))\n"
                    + "ENGINE = InnoDB;";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            System.out.println("Table `Clubs` created successfully...");

        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }
        return success;
    }

    private static boolean createTableTeams() {
        boolean success = true;
        try {
            System.out.println("Creating table 'Teams'...");

            String sql = "CREATE TABLE IF NOT EXISTS " + dbname + ".`Teams` (`ID` INT NOT NULL COMMENT '',\n"
                    + "  `TeamName` VARCHAR(45) NOT NULL COMMENT '',\n"
                    + "  `wins` INT NULL DEFAULT 0 COMMENT '',\n"
                    + "  `draws` INT NULL DEFAULT 0 COMMENT '',\n"
                    + "  `loses` INT NULL DEFAULT 0 COMMENT '',\n"
                    + "  PRIMARY KEY (`ID`)  COMMENT '')\n"
                    + "ENGINE = InnoDB;";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            System.out.println("Table `Teams` created successfully...");
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }
        return success;
    }

    private static boolean createTableShiaiJo() {
        boolean success = true;
        try {
            System.out.println("Creating table 'ShiaiJos'...");

            String sql = "CREATE TABLE IF NOT EXISTS " + dbname + ".`Shiaijos` (\n"
                    + "  `ID` CHAR(1) NOT NULL,\n"
                    + "  PRIMARY KEY (`ID`))\n"
                    + "ENGINE = InnoDB;";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            System.out.println("Table `ShiaiJos` created successfully...");
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }
        return success;
    }

    private static boolean createTablePools() {
        boolean success = true;
        try {
            System.out.println("Creating table 'Pools'...");

            String sql = "CREATE TABLE IF NOT EXISTS " + dbname + ".`Pools` (\n"
                    + "  `ID` VARCHAR(5) NOT NULL,\n"
                    + "  `Shiaijos_ID` CHAR(1) NOT NULL,\n"
                    + "  PRIMARY KEY (`ID`),\n"
                    + "  INDEX `fk_Pools_Shiaijos1_idx` (`Shiaijos_ID` ASC),\n"
                    + "  CONSTRAINT `fk_Pools_Shiaijos1`\n"
                    + "    FOREIGN KEY (`Shiaijos_ID`)\n"
                    + "    REFERENCES " + dbname + ".`Shiaijos` (`ID`)\n"
                    + "    ON DELETE NO ACTION\n"
                    + "    ON UPDATE NO ACTION)\n"
                    + "ENGINE = InnoDB;";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            System.out.println("Table `Pools` created successfully...");
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }
        return success;
    }

    private static boolean createTableContestants() {
        boolean success = true;
        try {

            System.out.println("Creating table 'Contestants'...");

            String sql = "CREATE TABLE IF NOT EXISTS `"+dbname+"`.`Contestants` (\n"
                    + "  `ID` INT NOT NULL AUTO_INCREMENT COMMENT '',\n"
                    + "  `FirstName` VARCHAR(45) NOT NULL COMMENT '',\n"
                    + "  `LastName` VARCHAR(45) NOT NULL COMMENT '',\n"
                    + "  `Grade_type` VARCHAR(45) NOT NULL COMMENT '',\n"
                    + "  `Grade_level` TINYINT(1) NOT NULL COMMENT '',\n"
                    + "  `Email` VARCHAR(45) NULL COMMENT '',\n"
                    + "  `Gender` TINYTEXT NOT NULL COMMENT '',\n"
                    + "  `Club_ID` INT NULL COMMENT '',\n"
                    + "  `Teams_ID` INT NULL COMMENT '',\n"
                    + "  `wins` TINYINT(1) NULL DEFAULT 0 COMMENT '',\n"
                    + "  `draws` TINYINT(1) NULL DEFAULT 0 COMMENT '',\n"
                    + "  `loses` TINYINT(1) NULL DEFAULT 0 COMMENT '',\n"
                    + "  `round` VARCHAR(15) NULL COMMENT '',\n"
                    + "  `ippons` TINYINT(1) NULL COMMENT '',\n"
                    + "  `Pools_ID` VARCHAR(5) NOT NULL COMMENT '',\n"
                    + "  PRIMARY KEY (`ID`)  COMMENT '',\n"
                    + "  INDEX `fk_Contestants_Clubs_idx` (`Club_ID` ASC)  COMMENT '',\n"
                    + "  INDEX `fk_Contestants_Teams1_idx` (`Teams_ID` ASC)  COMMENT '',\n"
                    + "  INDEX `fk_Contestants_Pools1_idx` (`Pools_ID` ASC)  COMMENT '',\n"
                    + "  CONSTRAINT `fk_Contestants_Clubs`\n"
                    + "    FOREIGN KEY (`Club_ID`)\n"
                    + "    REFERENCES `KendoCup`.`Clubs` (`ID`)\n"
                    + "    ON DELETE RESTRICT\n"
                    + "    ON UPDATE NO ACTION,\n"
                    + "  CONSTRAINT `fk_Contestants_Teams1`\n"
                    + "    FOREIGN KEY (`Teams_ID`)\n"
                    + "    REFERENCES `KendoCup`.`Teams` (`ID`)\n"
                    + "    ON DELETE NO ACTION\n"
                    + "    ON UPDATE NO ACTION,\n"
                    + "  CONSTRAINT `fk_Contestants_Pools1`\n"
                    + "    FOREIGN KEY (`Pools_ID`)\n"
                    + "    REFERENCES `KendoCup`.`Pools` (`ID`)\n"
                    + "    ON DELETE NO ACTION\n"
                    + "    ON UPDATE NO ACTION)\n"
                    + "ENGINE = InnoDB;";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            System.out.println("Table `Contestants` created successfully...");
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }
        return success;
    }

    public static ObservableList<String> getDatabases() {
        ObservableList<String> databases = FXCollections.observableArrayList();
        int i = 0;
        try {
            stmt = connection.prepareStatement(showDatabasesStatement);
            stmt.executeUpdate();
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                databases.add(rs.getString(1));
                System.out.println(databases.get(i++));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return databases;
    }

//    public static void dropDatabase() {
//        try {
//            String sql = "DROP SCHEMA " + DataStorage.getInstance().getDatabaseInUse() + ";";
//            stmt = connection.prepareStatement(sql);
//            stmt.executeUpdate();
//        } catch (SQLException ex) {
//            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

//    public static void insertIntoShiaiJoTable(String ID) {
//        String insertIntoShiaiJoTable = "INSERT INTO `" + DataStorage.getInstance().getDatabaseInUse() + "`.`shiaijos` (`ID`) VALUES (?);";
//        try {
//            stmt = connection.prepareStatement(insertIntoShiaiJoTable);
//            stmt.setString(1, ID);
//            stmt.executeUpdate();
//        } catch (SQLException ex) {
//            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
}
