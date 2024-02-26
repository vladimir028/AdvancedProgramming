package PrvKolokvium.Risk;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RiskTester {
    public static void main(String[] args) {

        Risk risk = new Risk();

        System.out.println(risk.processAttacksData(System.in));

    }
}

class Risk{

    private int success = 0;

    public int processAttacksData(InputStream in) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        List<Player> list = reader.lines()
                .map(line -> new Player(line))
                .collect(Collectors.toList());

        for(Player p : list){
            if(p.isFullyAttacked()){
                success++;
            }
        }
        return success;
    }
}

class Player{
    List<Integer> playerX;
    List<Integer> playerY;

    public Player() {
        this.playerX = new ArrayList<>();
        this.playerY = new ArrayList<>();
    }

    public Player(String line) {
        this.playerX = new ArrayList<>();
        this.playerY = new ArrayList<>();
        String [] parts = line.split(";");
        String [] playerXParts = parts[0].split("\\s+");
        String [] playerYParts = parts[1].split("\\s+");

        for(int i=0; i<3; i++){
            playerX.add(Integer.valueOf(playerXParts[i]));
            playerY.add(Integer.valueOf(playerYParts[i]));
        }
    }

    public void sortPlayers() {
        playerX = playerX.stream().sorted().collect(Collectors.toList());
        playerY = playerY.stream().sorted().collect(Collectors.toList());
    }


    public boolean isFullyAttacked() {
        sortPlayers();
        int counter = 0;
        for(int i=0; i<3; i++){
            if(playerX.get(i) > playerY.get(i)){
                counter++;
            }
        }
        return counter==3;
    }
}