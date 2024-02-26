package PrvKolokvium.F1Race;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        try {
            f1Race.readResults(System.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        f1Race.printSorted(System.out);
    }

}

class F1Pilot{
    private String name;
    private List<String> lapTime;
    private static int COUNTER = 1;


    public F1Pilot(String line) {
        String [] parts = line.split("\\s+");
        this.lapTime = new ArrayList<>();
        this.name = parts[0];
        for(int i=1; i< parts.length; i++){
            lapTime.add(parts[i]);
        }
    }

    public String getName() {
        return name;
    }

    public List<String> getLapTime() {
        return lapTime;
    }

//    public String bestTime(){
//        String best = lapTime.get(0);
//        for(int i=1; i<lapTime.size(); i++){
//            if(lapTime.get(i).compareTo(best) < 0){
//                best = lapTime.get(i);
//            }
//        }
//        return best;
//    }

    public String bestTime(){
        return lapTime.stream()
                .min(String::compareTo)
                .orElse(null);
    }

    @Override
    public String toString() {
        return String.format("%d. %-10s%10s", COUNTER++, getName(), bestTime());
    }
}

class F1Race {

    private List<F1Pilot> pilotLis;

    public F1Race() {
        this.pilotLis = new ArrayList<>();
    }

    public void readResults(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        pilotLis = reader.lines()
                .map(line -> new F1Pilot(line))
                .collect(Collectors.toList());
    }


    public void sorted(){
        Comparator<F1Pilot> comparator = Comparator.comparing(F1Pilot::bestTime);
        pilotLis = pilotLis.stream().sorted(comparator).collect(Collectors.toList());
    }
    public void printSorted(PrintStream out) {
        PrintWriter writer = new PrintWriter(out);
        sorted();
        for(F1Pilot pilot:pilotLis){
            writer.println(pilot);
        }
        writer.flush();
    }
    // vashiot kod ovde

}