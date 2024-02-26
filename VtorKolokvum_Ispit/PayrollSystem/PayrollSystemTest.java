package VtorKolokvum_Ispit.PayrollSystem;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i=5;i<=10;i++) {
            levels.add("level"+i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: "+ level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });


    }
}

abstract class Employee{
    private String type;
    private String id;
    private String level;

    public Employee(String type, String id, String level) {
        this.type = type;
        this.id = id;
        this.level = level;
    }


    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }


    abstract void setSalary(double hours);
    abstract double getSalary();
}

class HourlyEmployee extends Employee{
    private double hours;
    private double salary;

    public HourlyEmployee(String type, String id, String level, double hours) {
        super(type, id, level);
        this.hours = hours;
        salary = 0;
    }

    @Override
    public String toString() {
//        Employee ID: 157f3d Level: level10 Salary: 2390.72 Regular hours: 40.00 Overtime hours: 23.14
        return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f",getId(), getLevel(), salary, getRegularHours(), getOvertimeHours());
    }

    double getRegularHours(){
        return hours < 40 ? hours : 40;
    }

    double getOvertimeHours(){
        return hours < 40 ? 0 : hours - 40;
    }

    @Override
    void setSalary(double salaryPerHour) {
        if(hours < 40){
            this.salary = hours * salaryPerHour;
        }else{
            this.salary = (40 * salaryPerHour) + ((hours-40) * salaryPerHour * 1.5);
        }
    }

    @Override
    double getSalary() {
        return salary;
    }
}


class FreelanceEmployee extends Employee{
    private List<Integer> ticketPoints;
    double salary;

    public FreelanceEmployee(String type, String id, String level, List<Integer> ticketPoints) {
        super(type, id, level);
        this.ticketPoints = ticketPoints;
        salary = 0;
    }

    public int getTicketPointsSum(){
        return ticketPoints.stream().mapToInt(i -> i).sum();
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d", getId(), getLevel(), salary, ticketPoints.size(), getTicketPointsSum());
    }

    @Override
    void setSalary(double salaryPerTicketPoint) {
        this.salary = getTicketPointsSum() * salaryPerTicketPoint;
    }

    @Override
    double getSalary() {
        return salary;
    }
}
class PayrollSystem {

    List<Employee> employees;
    Map<String,Double> hourlyRateByLevel;
    Map<String,Double> ticketRateByLevel;

    PayrollSystem(Map<String,Double> hourlyRateByLevel, Map<String,Double> ticketRateByLevel){
        employees = new ArrayList<>();
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
    }

    void readEmployees (InputStream is){
        Scanner scanner = new Scanner(is);

        while (scanner.hasNext()){
            String line = scanner.next();
            String [] parts = line.split(";");
            String type = parts[0];
            String id = parts[1];
            String level = parts[2];
            if(type.equals("F")){
                List<Integer> list = new ArrayList<>();
                for(int i =3; i< parts.length; i++){
                    list.add(Integer.parseInt(parts[i]));
                }
                employees.add(new FreelanceEmployee(type, id, level, list));
            }else{
                employees.add(new HourlyEmployee(type, id, level, Double.parseDouble(parts[3])));
            }
        }

    }

    public void setSalary(){
//         this.hourlyRateByLevel = hourlyRateByLevel;
//        this.ticketRateByLevel = ticketRateByLevel;
        for (Employee employee : employees) {
            if(employee.getType().equals("H")){
                double salaryPerHour = hourlyRateByLevel.get(employee.getLevel());
                employee.setSalary(salaryPerHour);
            }else{
                double salaryPerTicketPoint = ticketRateByLevel.get(employee.getLevel());
                employee.setSalary(salaryPerTicketPoint);
            }
        }
    }
    Map<String, Set<Employee>> printEmployeesByLevels (OutputStream os, Set<String> levels){
        PrintWriter writer = new PrintWriter(os);
        Map<String, Set<Employee>> map = new TreeMap<>();
        setSalary();
        for (String level : levels) {
            boolean flag = false;
            Set<Employee> employeeSet = new TreeSet<>(Comparator.comparing(Employee::getSalary).reversed().thenComparing(Employee::getLevel));
            for (Employee employee : employees) {
                if(level.equals(employee.getLevel())) {
                    flag = true;
                    employeeSet.add(employee);
                }
            }
            if(flag) {
//                List<Employee> list = employeeSet.stream().sorted(Comparator.comparing(Employee::getSalary).reversed().thenComparing(Employee::getLevel)).collect(Collectors.toList());
                map.put(level, employeeSet);
            }
        }
        writer.flush();
        return map;
    }


}