package VtorKolokvum_Ispit.QuizProcessor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class QuizProcessorTest {
    public static void main(String[] args)  {
        QuizProcessor q = new QuizProcessor();
        try {
            q.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
        } catch (InvalidLength e) {
            System.out.println(e.getMessage());
        }
    }
}


class QuizProcessor {

    Map<String, Double> mapByIndex;
    List<String> correctAnswers;
    List<String> studentAnswers;

    public QuizProcessor() {
        mapByIndex = new LinkedHashMap<>();
        correctAnswers = new ArrayList<>();
        studentAnswers = new ArrayList<>();
    }

    public  Map<String, Double> processAnswers(InputStream is) throws InvalidLength {
        Scanner scanner = new Scanner(is);

        while (scanner.hasNext()) {
            try {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                String index = parts[0];
                String[] correctAns = parts[1].split(",");
                String[] studentAns = parts[2].split(",");

                if (correctAns.length != studentAns.length) {
                    throw new InvalidLength("A quiz must have same number of correct and selected answers");
                }

                List<String> correctAnswers = Arrays.asList(correctAns);
                List<String> studentAnswers = Arrays.asList(studentAns);

                double totalPoints = calculatePoints(correctAnswers, studentAnswers);
                mapByIndex.putIfAbsent(index, totalPoints);
            } catch (InvalidLength e) {
                System.out.println(e.getMessage());
            }
        }

        return mapByIndex;
    }

    private double calculatePoints(List<String> correctAnswers, List<String> studentAnswers) {

        double sum = 0;
        for(int i=0; i<correctAnswers.size(); i++){
            if(correctAnswers.get(i).equals(studentAnswers.get(i))){
                sum += 1;
            }else{
                sum -=0.25;
            }
        }
        return sum;
    }
}

class InvalidLength extends Exception{
    public InvalidLength(String message) {
        super(message);
    }
}