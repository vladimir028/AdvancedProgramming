package VtorKolokvum_Ispit.StudentRecords;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * January 2016 Exam problem 1
 */
public class StudentRecordsTest {
    public static void main(String[] args) {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}

// your code here
class StudentRecords{
    Map<String, Student> mapByIndex;

    public StudentRecords() {
        mapByIndex = new HashMap<>();
    }

    public int readRecords(InputStream inputStream){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        bufferedReader.lines()
                .forEach(line -> {
                    String [] parts = line.split("\\s+");
                    String index = parts[0];
                    String program = parts[1];
                    List<Integer> grades = new ArrayList<>();
                    IntStream.range(2, parts.length)
                            .forEach(i -> grades.add(Integer.valueOf(parts[i])));

                    Student student = new Student(index, program, grades);
                    mapByIndex.putIfAbsent(index, student);
                });
        return mapByIndex.size();
    }

    public void writeTable(OutputStream outputStream){

        TreeMap<String, List<Student>> mapByProgram = mapByIndex.values()
                .stream()
                .collect(Collectors.groupingBy(
                        student -> student.program,
                        TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                students -> {
                                    students.sort(Comparator.comparing(Student::avgGrade).reversed().thenComparing(Student::getIndex));
                                    return students;
                                }
                        )
                ));
        PrintWriter writer = new PrintWriter(outputStream);

        mapByProgram.keySet().forEach(
                key -> {
                    writer.println(key);
                    List<Student> students = mapByProgram.get(key);
                    students.forEach(writer::println);
                }
        );

        writer.flush();
    }

    public void writeDistribution(OutputStream outputStream){
        PrintWriter writer = new PrintWriter(outputStream);


        Map<String, List<Integer>> test = mapByIndex.values()
                .stream()
                .collect(Collectors.groupingBy(
                        Student::getProgram,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                students -> {
                                    List<Integer> a = students.stream().map(Student::getGrades).flatMap(Collection::stream).collect(Collectors.toList());
                                    return a;
                                }
                        )
                ));

        Map<String, Map<Integer, Long>> finalMap = new HashMap<>();


        test.keySet().forEach(key -> {
            List<Integer> totalGradesPerProgram = test.get(key);
            Map<Integer, Long> countGrade = totalGradesPerProgram.stream()
                    .collect(Collectors.groupingBy(
                            i -> i.intValue(),
                            Collectors.counting()
                    ));

            finalMap.putIfAbsent(key, countGrade);
        });

        LinkedHashMap<String, Map<Integer, Long>> collect = finalMap.entrySet()
                .stream()
                .sorted(Comparator.comparing(entry -> entry.getValue().get(10), Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
        collect.keySet()
                .forEach(
                        key -> {
                            writer.println(key);
                            Map<Integer, Long> tmp = collect.get(key);
                            tmp.keySet().forEach(
                                    gradeKey -> {
                                        writer.println(String.format("%2d | %s(%d)", gradeKey, countStars(tmp.get(gradeKey)), tmp.get(gradeKey)));
                                    }
                            );
                        }
                );

        writer.flush();
    }

    private String countStars(Long totalGrades) {
        StringBuilder sb = new StringBuilder();

        for(int i=0; i<Math.ceil(totalGrades/10.0); i++){
            sb.append("*");
        }
        return sb.toString();
    }
}

class Student{
    String index;
    String program;
    List<Integer> grades;

    public Student(String index, String program, List<Integer> grades) {
        this.index = index;
        this.program = program;
        this.grades = grades;
    }

    @Override
    public String toString() {
        return String.format("%s %.2f", index, avgGrade());
    }

    public String getIndex() {
        return index;
    }

    public String getProgram() {
        return program;
    }

    public List<Integer> getGrades() {
        return grades;
    }

    public double avgGrade() {
        return grades.stream().mapToInt(i -> i).average().orElse(0.0);
    }
}