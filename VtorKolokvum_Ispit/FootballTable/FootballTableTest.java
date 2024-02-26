package VtorKolokvum_Ispit.FootballTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Partial exam II 2016/2017
 */
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here

class Team{
    String name;
    int wins;
    int draws;
    int loses;
    int goalsScored;
    int goalsConceded;
    static int  i = 1;

    public Team(String name) {
        this.name = name;
        wins = 0;
        draws = 0;
        loses = 0;
        goalsScored = 0;
        goalsConceded = 0;
    }

    public int goalDifference (){
        return goalsScored - goalsConceded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWins() {
        return wins;
    }

    public void addWin() {
        this.wins += 1;
    }

    public int getDraws() {
        return draws;
    }

    public void addDraws() {
        this.draws += 1;
    }

    public int getLoses() {
        return loses;
    }

    public void addLoses() {
        this.loses += 1;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public void setGoalsScored(int goalsScored) {
        this.goalsScored += goalsScored;
    }

    public int getGoalsConceded() {
        return goalsConceded;
    }

    public void setGoalsConceded(int goalsConceded) {
        this.goalsConceded += goalsConceded;
    }

    @Override
    public String toString() {
        return String.format("%2d. %-15s%5d%5d%5d%5d%5d", i++, name, matchesPlayed(), wins, draws, loses, totalPoints());
    }

    public int totalPoints() {
        return wins * 3 + draws;
    }

    public int matchesPlayed() {
        return wins + loses + draws;
    }
}
class FootballTable {

    Map<String, Team> mapByTeamName;
    private int i = 1;

    public FootballTable() {
        mapByTeamName = new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals){
        mapByTeamName.putIfAbsent(homeTeam, new Team(homeTeam));
        mapByTeamName.putIfAbsent(awayTeam, new Team(awayTeam));

        Team hTeam = mapByTeamName.get(homeTeam);
        Team aTeam = mapByTeamName.get(awayTeam);

        hTeam.setGoalsScored(homeGoals);
        hTeam.setGoalsConceded(awayGoals);
        aTeam.setGoalsScored(awayGoals);
        aTeam.setGoalsConceded(homeGoals);


        if(homeGoals > awayGoals){
            hTeam.addWin();
            aTeam.addLoses();
        }
        else if (awayGoals > homeGoals){
            aTeam.addWin();
            hTeam.addLoses();
        }else{
            hTeam.addDraws();
            aTeam.addDraws();
        }
    }

    public void printTable(){
        mapByTeamName.values().stream()
                .sorted(Comparator.comparing(Team::totalPoints).thenComparing(Team::goalDifference).reversed().thenComparing(Team::getName))
                .forEach(System.out::println);
    }
}