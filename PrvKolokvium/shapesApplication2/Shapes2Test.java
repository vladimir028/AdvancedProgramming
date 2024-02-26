package PrvKolokvium.shapesApplication2;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Shapes2Test {

    public static void main(String[] args) throws IOException {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}
class TEST {
    private double sum;
    private List<Double> area;
    private double maxArea;

    public TEST(double sum, List<Double> areaTEST, double maxArea) {
        this.sum = sum;
        this.area = areaTEST;
        this.maxArea = maxArea;
    }

    public boolean moreThanMax() {
        List<Double> list = area;
        for (Double area : list) {
            if (area > maxArea) {
                return false;
            }
        }
        return true;
    }

    public double calculateArea(){
        return sum;
    }
}
class Shape{
    private String id;
    private List<String> type;
    private List<Integer> size;
    private List<Double> area = new ArrayList<>();
    private double maxArea;
    private static double PI = Math.PI;

    public static Shape createInstance(String line, double maxArea) throws IrregularCanvasException {
        String [] parts = line.split("\\s+");
        List<Integer> size = new ArrayList<>();
        List<String> type = new ArrayList<>();
        String id = parts[0];

        for(int i=1; i<parts.length-1; i+=2){
            type.add(parts[i]);
            size.add(Integer.valueOf(parts[i+1]));
        }

        return new Shape(id, type, size, maxArea);
    }

    public Shape(String id, List<String> type, List<Integer> size, double maxArea) throws IrregularCanvasException {
        this.id = id;
        this.type = type;
        this.size = size;
        this.maxArea = maxArea;
        if(!calculateAreaTEST().moreThanMax()){
            throw new IrregularCanvasException(id, maxArea);
        }
    }

    public TEST calculateAreaTEST(){
        double sum = 0;
        for(int i=0; i<size.size(); i++){
            double var = size.get(i) * size.get(i);
            if(type.get(i).equals("C")){
                var = var * PI;
            }
            sum += var;
            area.add(var);
        }
        return new TEST(sum, area, maxArea);
    }

    public double calculateArea(){
        return calculateAreaTEST().calculateArea();
    }


    public int countCircles(){
        return (int) type.stream().filter(i -> i.equals("C")).count();
    }

    public double minArea(){

        return area.stream().mapToDouble(i -> i).min().getAsDouble();
    }
    public double maxArea(){
        return area.stream().mapToDouble(i -> i).max().getAsDouble();
    }
    public double avg(){
        return area.stream().mapToDouble(i -> i).average().getAsDouble();
    }

    public int totalShapes(){
        return type.size();
    }

    @Override
    public String toString() {
//        ID total_shapes total_circles total_squares min_area max_area average_area.
        return String.format("%s %d %d %d %.2f %.2f %.2f", id, totalShapes(), countCircles(), totalShapes()-countCircles(), minArea(), maxArea(), avg());
    }
}

class IrregularCanvasException extends Exception{

    private String id;
    private double maxArea;
    public IrregularCanvasException(String id, double maxArea) {
        this.id = id;
        this.maxArea = maxArea;
    }

    @Override
    public String toString() {
        return String.format("Canvas %s has a shape with area larger than %.2f", id, maxArea);
    }
}

class ShapesApplication{
    private double maxArea;
    private List<Shape> list;
    public ShapesApplication(double maxArea) {
        this.maxArea = maxArea;
        this.list = new ArrayList<>();
    }

    public void readCanvases (InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        list = reader.lines()
                .map(line -> {
                    try {
                        return Shape.createInstance(line, maxArea);
                    } catch (IrregularCanvasException e) {
                        System.out.println(e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        reader.close();
    }

    public void printCanvases (OutputStream os){
        PrintWriter writer = new PrintWriter(os);
        Comparator<Shape> comparator = Comparator.comparing(Shape::calculateArea).reversed();
        list = list.stream().sorted(comparator).collect(Collectors.toList());
        for (Shape shape : list) {
            System.out.println(shape);
        }
        writer.flush();
    }
}