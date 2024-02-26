package VtorKolokvum_Ispit.StudentNaprednoProg;

//package mk.ukim.finki.midterm;

import java.util.*;
import java.util.stream.Collectors;


public class CourseTest {

    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Long> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) {
        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                advancedProgrammingCourse.addStudent(new Student(id, name));
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                advancedProgrammingCourse.updateStudent(idNumber, activity, points);
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}

class Student{
    String index;
    String name;
    int midterm1Points;
    int midterm2Points;
    int labPoints;


    public Student(String index, String name) {
        this.index = index;
        this.name = name;
        this.midterm1Points = 0;
        this.midterm2Points = 0;
        this.labPoints = 0;
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public int getMidterm1Points() {
        return midterm1Points;
    }

    public int getMidterm2Points() {
        return midterm2Points;
    }

    public int getLabPoints() {
        return labPoints;
    }

    public double summaryPoints(){
//                 midterm1 * 0.45 + midterm2 * 0.45 + labs.
        return midterm1Points * 0.45 + midterm2Points * 0.45 + labPoints;
    }

    public int grade(){
        if(summaryPoints() < 50){
            return 5;
        }
        else if(summaryPoints() >= 50 && summaryPoints() <60){
            return 6;
        }
        else if (summaryPoints() >= 60 && summaryPoints() <70) {
            return 7;
        }
        else if (summaryPoints() >= 70 && summaryPoints() <80) {
            return 8;
        }
        else if (summaryPoints() >= 80 && summaryPoints() <90) {
            return 9;
        }
        return 10;
    }

    public boolean passed(){
        return grade() > 5;
    }
    public void setMidterm1Points(int midterm1Points) {
        this.midterm1Points = midterm1Points;
    }

    public void setMidterm2Points(int midterm2Points) {
        this.midterm2Points = midterm2Points;
    }

    public void setLabPoints(int labPoints) {
        this.labPoints = labPoints;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s First midterm: %d Second midterm %d Labs: %d Summary points: %.2f Grade: %d"
                , index, name, midterm1Points, midterm2Points, labPoints, summaryPoints(), grade());
    }
}

class AdvancedProgrammingCourse {
    List<Student> students;
    Map<String, Student> studentMap;

    public AdvancedProgrammingCourse() {
        students = new ArrayList<>();
        studentMap = new HashMap<>();
    }

    public void addStudent (Student s){
        students.add(s);
        studentMap.putIfAbsent(s.index, s);
    }
    public void updateStudent (String idNumber, String activity, int points){
        if(activity.equals("midterm1") || activity.equals("midterm2") || activity.equals("labs")){
            Student student = studentMap.get(idNumber);
            if(student != null){
                if(activity.equals("midterm1")){
                    student.setMidterm1Points(points);
                }
                else if(activity.equals("midterm2")){
                    student.setMidterm2Points(points);
                }else{
                    student.setLabPoints(points);
                }
            }
        }else{

        }
    }

    public List<Student> getFirstNStudents (int n){
        return students.stream().sorted(Comparator.comparing(Student::summaryPoints).reversed()).limit(n).collect(Collectors.toList());
    }

    public Map<Integer, Long> getGradeDistribution(){

        List<Integer> grades = studentMap.values().stream().map(Student::grade).collect(Collectors.toList());

        Map<Integer, Long> map = grades.stream().collect(Collectors.groupingBy(
                i -> i,
                Collectors.counting()
        ));
        for(int i=5; i<=10; i++){
            map.putIfAbsent(i, 0L);
        }
        return map;
    }
    public void printStatistics(){
        DoubleSummaryStatistics statistics = new DoubleSummaryStatistics();
        students = students.stream().filter(i -> i.passed()).collect(Collectors.toList());
        for (Student student : students) {
            statistics.accept(student.summaryPoints());
        }

        System.out.println(String.format("Count: %d Min: %.2f Average: %.2f Max: %.2f",
                statistics.getCount(), statistics.getMin(), statistics.getAverage(), statistics.getMax()));
    }




}
