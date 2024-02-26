package VtorKolokvum_Ispit.Student;

import java.util.*;
import java.util.stream.Collectors;

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}
class Student{
    String index;
    List<Integer> points;

    public Student(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
    }

    public String getIndex() {
        return index;
    }

    public List<Integer> getPoints() {
        return points;
    }

//    public double getAsDouble (){
//        return points.stream().map(i -> i*1.0).collect(Collectors.toList());
//    }

    public double getAvg(){
        if(points.size() > 0)
            return (points.stream().mapToDouble(i -> i).sum()) / 10;
        return 0;
    }

    public boolean failed(){
        return points.size() <8;
    }

    public int getYear(){
//        2020-2018
        return 2020 - Integer.parseInt("20"+index.substring(0, 2));
    }

    @Override
    public String toString() {
        return failed() ? String.format("%s NO %.2f", index, getAvg()) : String.format("%s YES %.2f", index, getAvg());
    }

}
class LabExercises {
    List<Student> students;

    public LabExercises() {
        students = new ArrayList<>();
    }

    public void addStudent (Student student){
        students.add(student);
    }

    public void printByAveragePoints (boolean ascending, int n){
        Comparator<Student> comparator = Comparator.comparing(Student::getAvg).thenComparing(Student::getIndex);
        if(!ascending){
            comparator = comparator.reversed();
        }

        students.stream()
                .sorted(comparator)
                .limit(n)
                .forEach(System.out::println);


    }

    public List<Student> failedStudents (){
        return students.stream().filter(Student::failed)
                .sorted(Comparator.comparing(Student::getIndex).thenComparing(Student::getAvg))
                .collect(Collectors.toList());
    }

    public Map<Integer,Double> getStatisticsByYear(){
        students = students.stream().filter(i -> !i.failed()).collect(Collectors.toList());
        return students.stream().collect(Collectors.groupingBy(
                student -> student.getYear(),
                Collectors.averagingDouble(st -> st.getAvg())
        ));
    }
}
