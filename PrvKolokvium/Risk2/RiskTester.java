package PrvKolokvium.Risk2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

//    public void count(){
//        sortPlayers();
//        int countX = 0;
//        int countY = 0;
//        for(int i=0; i<3; i++){
//            if(playerX.get(i) > playerY.get(i)){
//                countX++;
//            }else{
//                countY++;
//            }
//        }
//
//        System.out.println(countX + " " + countY);
//    }

    public Counter count(){
        sortPlayers();
        int countX = 0;
        int countY = 0;
        for(int i=0; i<3; i++){
            if(playerX.get(i) > playerY.get(i)){
                countX++;
            }else{
                countY++;
            }
        }

        return new Counter(countX, countY);
    }
}

class Counter{

    private int countX;
    private int countY;
    public Counter(int countX, int countY) {
        this.countX = countX;
        this.countY = countY;
    }

    public int getCountX() {
        return countX;
    }

    public int getCountY() {
        return countY;
    }

    @Override
    public String toString() {
        return countX + " " + countY;
    }
}


class Risk{
    public void processAttacksData(InputStream in){
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        List<Player> list = reader.lines()
                .map(line -> new Player(line))
                .collect(Collectors.toList());


        for(Player p : list){
            System.out.println(p.count().toString());
        }
    }
}



public class RiskTester {
    public static void main(String[] args) {
        Risk risk = new Risk();
        risk.processAttacksData(System.in);
    }
}