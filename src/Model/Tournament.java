/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;

/**
 *
 * @author Panagiotis Bitharis
 */
/**
 * This class will contain all the respective Cups of an event. All the
 * information of this class will be saved in to an object file. Upon New
 * Tournament creation the user will be presented with a window to enter the
 * names of the Cups (i.e kyu Cup, Dan, Ladies, Team etc) When the user is done
 * all these information will be saved in the object Tournament and saved in to
 * a file. When the program starts, it will check inside a predefined folder for
 * available Tournament object files and the user will have the opportunity to
 * open which ever he/she wants.
 */
public class Tournament implements Serializable {

    /**
     * String tournamentName : Holds information regarding the name of the kendo
     * Event i.e 20th European Kendo Championship ArrayList<Cup> cups : Holds a
     * list of all the respective cups of the kendo event (tournament) i.e Women
     * Individual, Men Individual, Junior Individual etc. boolean
     * isTournamentSetUpCompleted : This boolean flag reveals if the Tournament
     * setup has been completed. If true, no further changes can be made to the
     * tournament object. All information have been save in to the database.
     */
    private String tournamentName;
    private ArrayList<Cup> cups = new ArrayList<>();
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<Club> clubs = new ArrayList<>();
    private ArrayList<Contestant> contestants = new ArrayList<>();
    private boolean isTournamentSetUpCompleted;

    public Tournament(String tourName) {
        if (!tourName.isEmpty()) {
            tournamentName = tourName;
            isTournamentSetUpCompleted = false;
        } else {
            throw new IllegalArgumentException("Tournament name is empty.");
        }
    }
    
    /*********************Getters *********************************************

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
        if (cups.isEmpty()) {
            System.out.println("EMPTY");
        }
        return cups;
    }
    
     /**
     * @return the teams
     */
    public ArrayList<Team> getTeams() {
        return teams;
    }

    /**
     * @return the clubs
     */
    public ArrayList<Club> getClubs() {
        return clubs;
    }

    /**
     * @return the contestants
     */
    public ArrayList<Contestant> getContestants() {
        return contestants;
    }

    /**
     * Searches through the cups ArrayList and returns the cup with name =
     * cupName
     *
     * @param cupName
     * @return
     * @throws Exception
     */
    public Cup getCup(String cupName) throws Exception {
        Cup cup = null;
        if (!cupName.isEmpty()) {
            for (Cup c : cups) {
                if (c.getCupName().equalsIgnoreCase(cupName)) {
                    cup = c;
                    break;
                }
            }
        } else {
            throw new Exception("Cupname is empty String");
        }
        return cup;
    }
    
        /**
     * @return the isTournamentSetUpCompleted
     */
    public boolean isIsTournamentSetUpCompleted() {
        return isTournamentSetUpCompleted;
    }
    //*******************END OF Getters ***************************************
    
    //******************** Setters ********************************************
    /**
     * @param cups the cups to set
     */
    public void setCups(ArrayList<Cup> cups) {
        this.cups = (!cups.isEmpty()) ? cups : null;
    }
    
    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    /**
     * @param teams the teams to set
     */
    public void setTeams(ArrayList<Team> teams) {
        this.teams = (!teams.isEmpty()) ? teams : null;
    }

    /**
     * @param clubs the clubs to set
     */
    public void setClubs(ArrayList<Club> clubs) {
        this.clubs = (!clubs.isEmpty()) ? clubs : null;
    }

    /**
     * @param contestants the contestants to set
     */
    public void setContestants(ArrayList<Contestant> contestants) {
        this.contestants = (!contestants.isEmpty()) ? contestants : null;
    }

    /**
     * @param isTournamentSetUpCompleted the isTournamentSetUpCompleted to set
     */
    public void setIsTournamentSetUpCompleted(boolean isTournamentSetUpCompleted) {
        this.isTournamentSetUpCompleted = isTournamentSetUpCompleted;
    }
    
    //**********************END OF Setters ***************************************



    /**
     * Adds a new cup in the cups ArrayList of the Tournament Class
     *
     * @param cup
     * @param duplicatesAllowed duplicatesAllowed == true a cup with the same
     * name can be added duplicatesAllowed == false not duplicate cups allowed.
     * @throws NullPointerException if the parameter is null;
     */
    public void addCup(Cup cup, boolean duplicatesAllowed) throws DuplicateName, NullPointerException, Exception {

        if (cup != null) {
            if (duplicatesAllowed) {
                getCups().add(cup);
            } else if (!duplicatesAllowed) {
                if (checkDuplicateCup(cup)) {
                    getCups().add(cup);
                } else {
                    throw new DuplicateName("Cup already exists");
                }
            }
        } else {
            throw new NullPointerException("Parameter Cup is Null");
        }

    }

    /**
     * Adds the team in the respective Cup based on the BelongsToCup() property
     * of the team object updates the ArrayList teams.
     *
     * @param team
     * @param duplicatesAllowed duplicatesAllowed == true a team with the same
     * name can be added duplicatesAllowed == false not duplicate teams allowed.
     * @throws Exception
     */
    public void addTeam(Team team, boolean duplicatesAllowed) throws DuplicateName, NullPointerException, Exception {
        if (team != null) {
            if (duplicatesAllowed) {
                //add the team to the arraylist teams
                getTeams().add(team);
                int i = cups.indexOf(getCup(team.getBelongsToCup()));
                //add the team to the teams arraylist of the cup
                cups.get(i).addTeam(team);

            } else if (!duplicatesAllowed) {
                Cup cup = getCup(team.getBelongsToCup());
                if (checkDuplicateTeamName(team, cup)) {
                    getTeams().add(team);
                    int i = cups.indexOf(getCup(team.getBelongsToCup()));
                    //add the team to the teams arraylist of the cup
                    cups.get(i).addTeam(team);
                } else {
                    throw new DuplicateName("Team already exists");
                }
            }

        } else {
            throw new Exception("team object is null");
        }
    }

    /**
     * Adds the club to the tournament and all the cups inside the cups
     * ArrayList
     *
     * @param club
     * @param duplicatesAllowed duplicatesAllowed == true a club with the same
     * name can be added duplicatesAllowed == false not duplicate clubs allowed.
     * @throws Exception
     */
    public void addClub(Club club, boolean duplicatesAllowed) throws DuplicateName, NullPointerException, Exception {
        if (club != null) {
            if (duplicatesAllowed) {
                //add the club to the list of the Tournament Class
                getClubs().add(club);
                //add the clubs to every cup.
                for (Cup cup : cups) {
                    cup.addClub(club);
                }
            } else if (!duplicatesAllowed) {
                if (checkDuplicateClubName(club)) {
                    //add the club to the list of the Tournament Class
                    getClubs().add(club);
                    //add the clubs to every cup.
                    for (Cup c : cups) {
                        c.addClub(club);
                    }
                } else {
                    throw new DuplicateName("Club already exists in the Tournament.");
                }
            }
        } else {
            throw new Exception("Club is null");
        }
    }

    public void addContestant(Contestant c, boolean duplicatesAllowed) throws DuplicateName, NullPointerException, Exception {
        //TODO
        if (c != null) {
            if (duplicatesAllowed) {

                //***************ADDED CODE 21/08/2016***********************
                Iterator<Cup> cupsIterator = getCups().iterator();
                while (cupsIterator.hasNext()) {
                    Cup tmpCup = cupsIterator.next();
                    if (tmpCup.includedInParticipationListProperty().get()) {
                        if (tmpCup.isIsATeamChampionship()) {
                            //if the player is not a member of a team just add the player to the competition
                            if (c.getBelongsToTeamProperty() == null) {

                                Contestant tmpContestant = new Contestant(c.getFirstName(),
                                        c.getLastName(), c.getGradeType(), c.getGradeLevel(),
                                        c.getGender(), c.getClubName());

                                tmpContestant.setBelongsToCup(tmpCup.getCupName());
                                tmpContestant.setBelongsToTeam("lone samurai");
                                tmpCup.addContestant(tmpContestant);
                                contestants.add(tmpContestant);

                            } else {
                                //if the player belongs to a team search to which team he belongs to
                                Iterator<Team> teamsIterator = tmpCup.getTeams().iterator();
                                while (teamsIterator.hasNext()) {
                                    Team tmpTeam = teamsIterator.next();
                                    if (tmpTeam.getTeamName().equalsIgnoreCase(c.getBelongsToTeam())) {
                                        c.setBelongsToCup(tmpCup.getCupName());
                                        if (tmpTeam.getTeamName().equalsIgnoreCase(c.getBelongsToTeam())) {
                                            c.setBelongsToCup(tmpCup.getCupName());
                                            tmpTeam.addToTeam(c);
                                            tmpCup.addContestant(c);
                                            contestants.add(c);
                                            break;
                                        }
                                    }
                                }
                            }
                        } else if (!tmpCup.isIsATeamChampionship()) {
                            Contestant tmpContestant = new Contestant(c.getFirstName(),
                                    c.getLastName(), c.getGradeType(), c.getGradeLevel(),
                                    c.getGender(), c.getClubName());

                            tmpContestant.setBelongsToCup(tmpCup.getCupName());
                            tmpCup.addContestant(tmpContestant);
                            contestants.add(tmpContestant);
                        }
                    }
                }

            } else if (!duplicatesAllowed) {

                Iterator<Cup> cupsIterator = getCups().iterator();
                while (cupsIterator.hasNext()) {
                    Cup tmpCup = cupsIterator.next();
                    if (tmpCup.includedInParticipationListProperty().get()) {
                        if (tmpCup.isIsATeamChampionship()) {
                            //if the player is not a member of a team just add the player to the competition
                            if (c.getBelongsToTeamProperty() == null) {

                                Contestant tmpContestant = new Contestant(c.getFirstName(),
                                        c.getLastName(), c.getGradeType(), c.getGradeLevel(),
                                        c.getGender(), c.getClubName());

                                tmpContestant.setBelongsToCup(tmpCup.getCupName());
                                tmpContestant.setBelongsToTeam("lone samurai");
                                if (checkDuplicateContestant(tmpContestant)) {
                                    tmpCup.addContestant(tmpContestant);
                                    contestants.add(tmpContestant);
                                } else {
                                    throw new NullPointerException("Contestant object is empty");
                                }

                            } else {
                                //if the player belongs to a team search to which team he belongs to
                                Iterator<Team> teamsIterator = tmpCup.getTeams().iterator();
                                while (teamsIterator.hasNext()) {
                                    Team tmpTeam = teamsIterator.next();
                                    if (tmpTeam.getTeamName().equalsIgnoreCase(c.getBelongsToTeam())) {
                                        c.setBelongsToCup(tmpCup.getCupName());
                                        if (tmpTeam.getTeamName().equalsIgnoreCase(c.getBelongsToTeam())) {
                                            c.setBelongsToCup(tmpCup.getCupName());
                                            tmpTeam.addToTeam(c);
                                            if (checkDuplicateContestant(c)) {
                                                tmpCup.addContestant(c);
                                                contestants.add(c);
                                                break;
                                            } else {
                                                throw new NullPointerException("Contestant object is empty");
                                            }

                                        }
                                    }
                                }
                            }
                        } else if (!tmpCup.isIsATeamChampionship()) {
                            Contestant tmpContestant = new Contestant(c.getFirstName(),
                                    c.getLastName(), c.getGradeType(), c.getGradeLevel(),
                                    c.getGender(), c.getClubName());

                            tmpContestant.setBelongsToCup(tmpCup.getCupName());
                            if (checkDuplicateContestant(tmpContestant)) {
                                tmpCup.addContestant(tmpContestant);
                                contestants.add(tmpContestant);
                            } else {
                                throw new NullPointerException("Contestant object is empty");
                            }
                        }
                    }
                }
            } else {
                throw new DuplicateName("Contestant already exists in the cup");
            }
        }

    }
    
        /**
     * @param cup the cup to be removed
     */
    public void removeCup(Cup cup) throws NullPointerException, Exception {
        //TO DO
        if (cup != null) {
            if (cups.size() > 1) {
                cups.remove(cup);
            } else {
                throw new Exception("ArrayList cups cannot be left empty.");
            }

        } else {
            throw new NullPointerException("Parameter Cup is Null");
        }
    }
    
    public void removeContestantFromCup(Contestant contestant) {
        

        //remove the selected object from the ArrayList
        getContestants().remove(contestant);

        //remove the Contestant from the Cup (cups ArrayList)
        Iterator cupIterator = getCups().listIterator();
        while (cupIterator.hasNext()) {
            Cup c = (Cup) cupIterator.next();
            Iterator playersIteratorList = c.getContestants().listIterator();
            while (playersIteratorList.hasNext()) {
                Contestant con = (Contestant) playersIteratorList.next();
                if (con.getFirstName().equalsIgnoreCase(contestant.getFirstName())
                        && con.getLastName().equalsIgnoreCase(contestant.getLastName())
                        && con.getClubName().equalsIgnoreCase(contestant.getClubName())
                        && con.getBelongsToCup().equalsIgnoreCase(contestant.getBelongsToCup())) {
                    playersIteratorList.remove();
                }
            }

            //Remve from team members
            Iterator teamIterator = c.getTeams().listIterator();
            while (teamIterator.hasNext()) {
                Team team = (Team) teamIterator.next();
                Iterator teamMemberList = team.getTeamMembersAsList().iterator();
                while (teamMemberList.hasNext()) {
                    Contestant teamMember = (Contestant) teamMemberList.next();
                    if (teamMember.getFirstName().equalsIgnoreCase(contestant.getFirstName())
                            && teamMember.getLastName().equalsIgnoreCase(contestant.getLastName())
                            && teamMember.getClubName().equalsIgnoreCase(contestant.getClubName())
                            && teamMember.getBelongsToCup().equalsIgnoreCase(contestant.getBelongsToCup())
                            && teamMember.getBelongsToTeam().equalsIgnoreCase(contestant.getBelongsToTeam())) {
                        teamMemberList.remove();
                    }
                }

            }
        }

        Iterator teamIterator = getTeams().listIterator();
        while (teamIterator.hasNext()) {
            Team tmpTeam = (Team) teamIterator.next();
            tmpTeam.getTeamMembersAsList().remove(contestant);

        }

        
    }

    public void removeClubFromCup(Club club) {
        //remove selected item from the ArrayList
        getClubs().remove(club);

        //remove the club from the cups ArrayList
        for (Cup c : getCups()) {
            Iterator it = c.getClubs().listIterator();
            while (it.hasNext()) {
                Club cl = (Club) it.next();
                if (cl.getClubName().equalsIgnoreCase(club.getClubName())) {
                    it.remove();
                }
            }
        }
    }

    public void removeTeamFromCup(Team team) {
      
        //remove the selected object from the ArrayList
        getTeams().remove(team);

        //remove the team from the cups ArrayList
        for (Cup c : getCups()) {
            Iterator it = c.getTeams().listIterator();
            while (it.hasNext()) {
                Team t = (Team) it.next();
                if (t.getTeamName().equalsIgnoreCase(team.getTeamName())
                        && t.getBelongsToCup().equalsIgnoreCase(team.getBelongsToCup())) {
                    it.remove();
                }
            }
        }

        
    }

    /**
     * Checks for a competition with the same name inside the cups ArrayList
     *
     * @param cup
     * @return True if the cup is unique
     * @return False if there is a cup with the same name
     */
    public boolean checkDuplicateCup(Cup cup) {
        boolean isUnique = true;
        if (cup != null && !cups.isEmpty()) {
            for (Cup c : cups) {
                if (c.getCupName().equalsIgnoreCase(cup.getCupName())) {
                    isUnique = false;
                    break;
                }
            }
        } else if (cup != null && cups.isEmpty()) {
            isUnique = true;
        } else {
            throw new NullPointerException("Parameter Cup is Null");
        }

        return isUnique;
    }

    /**
     * Searches through the cups for duplicate team name.
     *
     * @param team
     * @return True if the team is unique
     * @return False if there is a team with the same name
     */
    public boolean checkDuplicateTeamName(Team team, Cup cup) throws Exception {
        boolean isUnique = true;
        if (team != null && cup != null) {
            isUnique = cup.checkDuplicateTeamName(team);
        } else {
            throw new Exception("Argument team or cup is null");
        }
        return isUnique;
    }

    /**
     * Searches the ArrayList clubs of tournament object for a club with the
     * same name and nationality
     *
     * @param club
     * @return True if the club is unique
     * @return False if there is a club with the same name
     */
    public boolean checkDuplicateClubName(Club club) throws Exception {
        boolean isUnique = true;
        if (club != null && !clubs.isEmpty()) {
            for (Club cl : clubs) {
                if (cl.getClubName().equalsIgnoreCase(club.getClubName()) && cl.getNationality().equalsIgnoreCase(club.getNationality())) {
                    isUnique = false;
                    break;
                }
            }
        } else if (club != null && clubs.isEmpty()) {
            isUnique = true;
        } else {
            throw new Exception("Invalid club parameter.");
        }

        return isUnique;
    }

    public boolean checkDuplicateContestant(Contestant contestant) throws Exception {
        printStructure();
        boolean isUnique = true;

        if (contestant != null && !contestants.isEmpty()) {
            for (Contestant cont : contestants) {
                if (cont.getFirstName().equalsIgnoreCase(contestant.getFirstName())
                        && cont.getLastName().equalsIgnoreCase(contestant.getLastName())
                        && cont.getClubName().equalsIgnoreCase(contestant.getClubName())
                        && cont.getGradeLevel().equalsIgnoreCase(contestant.getGradeLevel())
                        && cont.getGradeType().equalsIgnoreCase(contestant.getGradeType())
                        && cont.getBelongsToCup().equalsIgnoreCase(contestant.getBelongsToCup())) {
                    isUnique = false;
                    break;
                }
            }

        } else if (contestant != null && contestants.isEmpty()) {
            isUnique = true;
        } else {
            throw new Exception("Invalid contestant parameter.");
        }

        return isUnique;
    }

    /**
     * Method that prints the entire structure of the Tournament
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
                System.out.println("+++ " + cl.getClubName() + " - " + cl.getNationality());

            }

            System.out.println("\n+++Listing Contestants");
            for (Contestant cont : c.getContestants()) {
                System.out.println("Firstname: " + cont.getFirstName() + " Last Name: " + cont.getLastName());
                System.out.println("Club Name: " + cont.getClubName() + " Team: " + cont.getBelongsToTeam());
                System.out.println("Gender: " + cont.getGender() + " Grade: " + cont.getGradeType() + "/" + cont.getGradeLevel());

            }
        }

    }

   
    /**
     * @param tournamentName the tournamentName to set
     */
    

}
