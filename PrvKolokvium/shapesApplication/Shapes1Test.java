package PrvKolokvium.shapesApplication;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}
class Shape{
    private String id;
    private List<Integer> list;


    public Shape(String line) {
        String [] parts = line.split("\\s+");
        this.id = parts[0];
        list = new ArrayList<>();
        for(int i=1; i<parts.length; i++){
            list.add(Integer.parseInt(parts[i]));
        }
    }

    public int sum(){
        return list.stream().mapToInt(i -> i).sum();
    }

    public int size(){
        return list.size();
    }

    @Override
    public String toString() {
        return this.id + " " + size() + " " + sum()*4;
    }
}
class ShapesApplication {

    private List<Shape> shapeList;
    private int sum;

    public ShapesApplication() {
        this.shapeList = new ArrayList<>();
        this.sum = 0;
    }

    public int readCanvases(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        shapeList = reader.lines()
                .map(line -> new Shape(line))
                .collect(Collectors.toList());
        for(Shape shape : shapeList){
            sum += shape.size();
        }
        return sum;
    }

    void printLargestCanvasTo (OutputStream outputStream){
        PrintWriter printWriter = new PrintWriter(outputStream);
        shapeList = shapeList.stream().sorted(Comparator.comparing(Shape::sum)).collect(Collectors.toList());

        printWriter.println(shapeList.get(shapeList.size()-1));
        printWriter.flush();
    }
}