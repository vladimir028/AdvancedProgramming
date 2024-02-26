package VtorKolokvum_Ispit.DailyTemperatures;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * I partial exam 2016
 */
public class DailyTemperatureTest {
    public static void main(String[] args) throws IOException {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}
class Temperature{
    private int day;
    private List<Double> list;
    private String type;

    public Temperature(String line){
        String [] parts = line.split("\\s+");
        list = new ArrayList<>();
        this.day = Integer.parseInt(parts[0]);
        this.type = String.valueOf(parts[1].charAt(parts[1].length()-1));
        for(int i=1; i<parts.length; i++) {
            list.add(Double.parseDouble(parts[i].substring(0, parts[i].length()-1)));
        }
    }

    public List<Double> getList() {
        return list;
    }

    public void transform(String scale){
        if(scale.equals("C") && this.type.equals("C")){
            return;
        }
        else if(scale.equals("F") && this.type.equals("F")){
            return;
        }
        else if(scale.equals("F") && this.type.equals("C")){
//            frac{T * 9}{5} + 32$
            this.type = "F";
            list = list.stream().mapToDouble(i -> (i*9/5) +32).boxed().collect(Collectors.toList());
        }else{
//            frac{(T - 32) * 5}{9}$
            this.type = "C";
            list = list.stream().mapToDouble(i -> ((i-32))* 5/9).boxed().collect(Collectors.toList());
        }
    }

    public double getMax(){
        return list.stream().mapToDouble(i -> i).max().getAsDouble();
    }
    public double getMin(){
        return list.stream().mapToDouble(i -> i).min().getAsDouble();
    }

    public double getAvg(){
        return list.stream().mapToDouble(i -> i).average().getAsDouble();
    }

    public int count(){
        return list.size();
    }

    public int getDay() {
        return day;
    }

    @Override
    public String toString() {
        return String.format("%3d: Count: %3d Min: %6.2f%s Max: %6.2f%s Avg: %6.2f%s ",day, count(), getMin(),type, getMax(),type, getAvg(),type);
    }

}
class DailyTemperatures {

    List<Temperature> list;

    public DailyTemperatures() {
        list = new ArrayList<>();
    }

    void readTemperatures(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        list = reader.lines()
                .map(line -> new Temperature(line))
                .collect(Collectors.toList());

        reader.close();

    }

    void writeDailyStats(OutputStream outputStream, char scale){
        PrintWriter writer = new PrintWriter(outputStream);

//        DoubleSummaryStatistics statistics = new DoubleSummaryStatistics();

        list = list.stream()
                .sorted(Comparator.comparing(Temperature::getDay))
                .collect(Collectors.toList());

        for(Temperature t : list){
            t.transform(String.valueOf(scale));
            writer.println(t);
        }

        writer.flush();
    }
}