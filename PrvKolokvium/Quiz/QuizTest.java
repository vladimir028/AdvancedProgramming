package PrvKolokvium.Quiz;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

abstract class Question{
    private String type;
    private String text;
    private int points;

    public Question(String type, String text, int points) {
        this.type = type;
        this.text = text;
        this.points = points;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    abstract double calculatePoints(String answer);
}

class TrueFalseQuestion extends Question{
    private String answer;

    public TrueFalseQuestion(String type, String text, int points, String answer) {
        super(type, text, points);
        this.answer = answer;
    }

    @Override
    double calculatePoints(String answer) {
        if(this.answer.equals(answer))
            return getPoints();
        return 0;
    }

    @Override
    public String toString() {
        return String.format("True/False Question: %s Points: %d Answer: %s", getText(), getPoints(), answer);
    }
}

class MultipleChoiceQuestion extends Question{
    private String answer;

    public MultipleChoiceQuestion(String type, String text, int points, String answer) {
        super(type, text, points);
        this.answer = answer;
    }

    @Override
    double calculatePoints(String answer) {
        if(this.answer.equals(answer))
            return getPoints();
        return -getPoints()* 0.2;
    }

    @Override
    public String toString() {
        return String.format("Multiple Choice Question: %s Points %d Answer: %s", getText(), getPoints(), answer);
    }
}
class Quiz{
    List<Question> questions;

    public Quiz() {
        this.questions = new ArrayList<>();
    }

    public void addQuestion(String questionData) throws InvalidOperationException {
        String [] parts = questionData.split(";");
        if(parts[0].equals("TF")){
            questions.add(new TrueFalseQuestion(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3]));
        }
        else if(parts[0].equals("MC")){
            if(!(parts[3].equals("A") || parts[3].equals("B") || parts[3].equals("C") || parts[3].equals("D") || parts[3].equals("E"))){
                throw new InvalidOperationException(String.format(parts[3]));
            }
            questions.add(new MultipleChoiceQuestion(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3]));
        }
    }

    public  List<Question> sorted(){
        Comparator<Question> comparator = Comparator.comparing(Question::getPoints).reversed();
        questions = questions.stream().sorted(comparator).collect(Collectors.toList());

        return questions;
    }

    public void printQuiz(OutputStream os){
        PrintWriter writer = new PrintWriter(os);

        sorted();
        for(Question question : questions){
            writer.println(question);
        }

        writer.flush();
    }


    public void answerQuiz (List<String> answers, OutputStream os) throws InvalidOperationException {
        if(answers.size() != questions.size()){
            throw new InvalidOperationException("");
        }

        PrintWriter printWriter = new PrintWriter(os);
        int i=1;
        double sum = 0;
        for(int j=0; j<answers.size(); j++){
            sum += questions.get(j).calculatePoints(answers.get(j));
            printWriter.format("%d. %.2f\n", i++, questions.get(j).calculatePoints(answers.get(j)));
        }

        printWriter.format("Total points: %.2f\n", sum);
        printWriter.flush();
    }
}
class InvalidOperationException extends Exception{

    private String answer;
    public InvalidOperationException(String answer) {
        this.answer = answer;
    }


    public String size(){
        return String.format("Answers and questions must be of same length!");
    }

    public String notValid(){
        return String.format("%s is not allowed option for this question", answer);
    }
}
public class QuizTest {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Quiz quiz = new Quiz();

        int questions = Integer.parseInt(sc.nextLine());

        for (int i=0;i<questions;i++) {
            try {
                quiz.addQuestion(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.notValid());
            }
        }

        List<String> answers = new ArrayList<>();

        int answersCount =  Integer.parseInt(sc.nextLine());

        for (int i=0;i<answersCount;i++) {
            answers.add(sc.nextLine());
        }

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase==1) {
            quiz.printQuiz(System.out);
        } else if (testCase==2) {
            try {
                quiz.answerQuiz(answers, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.size());;
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}
