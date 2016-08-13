/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Panagiotis Bitharis
 */

/*
This class will contain all the respective Cups of an event.
All the information of this class will be saved in to an object file.
Upon New Tournament creation the user will be presented with a window to enter the
names of the Cups (i.e kyu Cup, Dan, Ladies, Team etc)
When the user is done all these information will be saved in the object Tournament
an saved in to a file. 
When the program starts, it will check inside a predefined folder for available Tournament object files
and the user will have the opportunity to open which ever he/she wants.
 */
public class Tournament implements Serializable {

    private String tournamentName;
    private ArrayList<Cup> cups = null;

    public Tournament(String tourName) {
        if (!tourName.isEmpty()) {
            tournamentName = tourName;
        } else {
            throw new IllegalArgumentException("Tournament name is empty.");
        }
    }

    /**
     * @return the tournamentName
     */
    public String getTournamentName() {
        return tournamentName;
    }

    /**
     * @return the cups
     */
    public ArrayList<Cup> getCups() {
        return cups;
    }

    /**
     * @param cups the cups to set
     */
    public void setCups(ArrayList<Cup> cups) {
        this.cups = (!cups.isEmpty()) ? cups : null;
    }

    /*
    - Method that prints the entire structure of the Tournament
     */
    public void printStructure() {
        System.out.println("\n------------------------TOURNAMENT STRUCTURE---------------------------------");
        System.out.println("+ Tournament Name: " + tournamentName);
        System.out.println("++ Printing Cup information");
        for (Cup c : cups) {
            System.out.println("\n++ Cup Name:" + c.getCupName());
            System.out.println("++ Number of Pools: " + c.getNumberOfPools());
            System.out.println("++ Prefered number of Players per pool: " + c.getPrefPlayers());
            System.out.println("++ Maximum number of players per pool: " + c.getMaxPlayers());
            System.out.println("++ Number of available shiaijos: " + c.getNumberOfShiaiJos());
            System.out.println("\n+++ Listing Shiaijos:");
            for (Shiaijo sh : c.getShiaijos()) {
                System.out.println("+++ Shiaijo ID: " + sh.getID());
            }

            System.out.println("\n+++ Listing Teams:");
            for (Team t : c.getTeams()) {
                System.out.println("\n+++ " + t.getTeamName());
                System.out.println("+++ Team is in Cup " + t.getBelongsToCup());
                System.out.println("++++ Printing Team Members:");

                for (Contestant con : t.getTeamMembersAsList()) {
                    if (con != null) {
                        System.out.println("++++ Team Member: " + con.getFirstName() + " " + con.getLastName());
                    }

                }

            }
            System.out.println("\n+++Listing Club names");
            for (Club cl : c.getClubs()) {
                System.out.println("+++ " + cl.getClubName()+" - "+cl.getNationality());
                
            }
            
            System.out.println("\n+++Listing Contestants");
            for(Contestant cont:c.getContestants()){
                System.out.println("Firstname: "+cont.getFirstName()+" Last Name: "+cont.getLastName());
                System.out.println("Club Name: "+cont.getClubName()+" Team: "+cont.getBelongsToTeam());
                System.out.println("Gender: "+cont.getGender()+" Grade: "+cont.getGradeType()+"/"+cont.getGradeLevel());
                
            }
        }

    }
}
