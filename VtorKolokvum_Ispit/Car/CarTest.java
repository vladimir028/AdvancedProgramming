package VtorKolokvum_Ispit.Car;

import java.util.*;
import java.util.stream.Collectors;

class Car{
    private String manufacturer;
    private String model;
    private int price;
    private float power;

    public Car(String manufacturer, String model, int price, float power) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.price = price;
        this.power = power;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    @Override
    public String toString() {
//        Renault Clio (96KW) 12100
        return String.format("%s %s (%dKW) %d", manufacturer, model, (int)power, price);
    }
}

class CarCollection{
    List<Car> carList;

    public CarCollection() {
        carList = new ArrayList<>();
    }

    public void addCar(Car car){
        carList.add(car);
    }

    public void sortByPrice(boolean ascending){
        Comparator<Car> comparator = Comparator.comparing(Car::getPrice);
        if(ascending){
            carList = carList.stream().sorted(comparator.thenComparing(Car::getPower)).collect(Collectors.toList());
        }else{
            carList =  carList.stream().sorted(comparator.thenComparing(Car::getPower).reversed()).collect(Collectors.toList());
        }
    }

    public List<Car> filterByManufacturer(String manufacturer){
        return carList.stream()
                .filter(i -> i.getManufacturer().toLowerCase().equals(manufacturer.toLowerCase()))
                .sorted(Comparator.comparing(Car::getModel))
                .collect(Collectors.toList());
    }
    public List<Car> getList(){
        return carList;
    }
}
public class CarTest {
    public static void main(String[] args) {
        CarCollection carCollection = new CarCollection();
        String manufacturer = fillCollection(carCollection);
        carCollection.sortByPrice(true);
        System.out.println("=== Sorted By Price ASC ===");
        print(carCollection.getList());
        carCollection.sortByPrice(false);
        System.out.println("=== Sorted By Price DESC ===");
        print(carCollection.getList());
        System.out.printf("=== Filtered By Manufacturer: %s ===\n", manufacturer);
        List<Car> result = carCollection.filterByManufacturer(manufacturer);
        print(result);
    }

    static void print(List<Car> cars) {
        for (Car c : cars) {
            System.out.println(c);
        }
    }

    static String fillCollection(CarCollection cc) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            if(parts.length < 4) return parts[0];
            Car car = new Car(parts[0], parts[1], Integer.parseInt(parts[2]),
                    Float.parseFloat(parts[3]));
            cc.addCar(car);
        }
        scanner.close();
        return "";
    }
}