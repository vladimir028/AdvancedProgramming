package VtorKolokvum_Ispit.OnlinePayments;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> onlinePayments.printStudentReport(id, System.out));
    }
}

class Item{
    String name;
    int price;

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%s %d", name, price);
    }
}

class Student{
    String index;
    List<Item> itemList;
    int totalPrice;
    public static double FEE = 0.0114;

    public Student(){
        itemList = new ArrayList<>();
    }

    @Override
    public String toString() {
//        Student: 151020 Net: 13050 Fee: 149 Total: 13199
        return String.format("Student: %s Net: %d Fee: %d Total: %d", index, calculateNet(), calculateFee(), calculateTotal());
    }


    public int calculateNet() {
        return totalPrice;
    }

    public int calculateFee(){
        if((int) Math.round(FEE * calculateNet()) < 3){
            return 3;
        }
        else if ((int) Math.round(FEE * calculateNet()) > 300 ){
            return 300;
        }
        return (int) Math.round(FEE * calculateNet());
    }
    public int calculateTotal(){
        return calculateNet() + calculateFee();
    }

    public void addPrice(int price) {
        this.totalPrice += price;
    }

    public void setIndex(String idx) {
        this.index = idx;
    }
}

class OnlinePayments {

    Map<String, Student> studentMap;

    public OnlinePayments() {
        studentMap = new HashMap<>();
    }

    void readItems (InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        reader.lines()
                .forEach(line -> {
                    String [] parts = line.split(";");
                    String idx = parts[0];
                    String itemName = parts[1];
                    int price = Integer.parseInt(parts[2]);
                    studentMap.putIfAbsent(idx, new Student());
                    Student s = studentMap.get(idx);
                    s.setIndex(idx);
                    s.addPrice(price);
                    studentMap.get(idx).itemList.add(new Item(itemName, price));
                });


    }

    void printStudentReport (String index, OutputStream os) {
        PrintWriter writer = new PrintWriter(os);

        Student student = studentMap.get(index);
        if(student != null){
//            writer.println(student);
            System.out.println(student);
            System.out.println("Items: ");
//            writer.print("Items:");
            List<Item> items = student.itemList.stream().sorted(Comparator.comparing(Item::getPrice).reversed())
                    .collect(Collectors.toList());

            for(int i =0, j=0; i<items.size(); i++){
                System.out.printf("%d. ", ++j);
                System.out.println(items.get(i));
            }

        }
        else{
            System.out.println("Student " + index + " not found!");
        }

        writer.flush();
    }
}