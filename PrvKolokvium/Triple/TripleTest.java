package PrvKolokvium.Triple;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.average());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.average());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.average());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
// vasiot kod ovde
class Triple<T extends Number>{
    private T first;
    private T second;
    private T third;
    private List<T> list;

    public Triple(T first, T second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
        list = new ArrayList<>();
        list.add(first);
        list.add(second);
        list.add(third);
    }

    public double max(){
        return  list.stream().mapToDouble(Number::doubleValue).max().getAsDouble();
    }

    public double average(){
        return list.stream().mapToDouble(Number::doubleValue).average().getAsDouble();
    }

    public void sort(){
        list = list.stream().sorted().collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f", Double.parseDouble(String.valueOf(list.get(0))), Double.parseDouble(String.valueOf(list.get(1))), Double.parseDouble(String.valueOf(list.get(2))));
    }
}


