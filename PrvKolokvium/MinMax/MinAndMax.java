package PrvKolokvium.MinMax;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}

class MinMax<T extends Comparable<T>> {
    private T min;
    private T max;
    private List<T> list;
    private int counter = 0;

    public MinMax() {
        this.list = new ArrayList<>();
    }

    public void update(T element){
        if(counter == 0){
            max = element;
            min = element;
            counter++;
        }else{
            if(element.compareTo(min) < 0){
                min = element;
            }else{
                if(element.compareTo(max) > 0){
                    max = element;
                }
            }
        }
//            counter++;
        list.add(element);
    }
    public T max(){
        return max;
    }
    public T min(){
        return min;
    }

    public List<T> distinct(){
        return list.stream().filter(i -> i.compareTo(min) != 0 && i.compareTo(max) != 0).collect(Collectors.toList());
    }

    @Override
    public String toString() {

        return min() + " " + max() + " " + distinct().size() + "\n";
    }
}