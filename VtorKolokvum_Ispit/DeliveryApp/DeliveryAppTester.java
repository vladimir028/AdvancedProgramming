package VtorKolokvum_Ispit.DeliveryApp;

import java.util.*;
import java.util.stream.Collectors;

/*
YOUR CODE HERE
DO NOT MODIFY THE interfaces and classes below!!!
*/

class DeliveryPerson{
    String id;
    String name;
    Location currentLocation;
    Location restaurantLocation;
    int ordersDelivered;
    double deliveryFee;


    public DeliveryPerson(String id, String name, Location currentLocation) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        ordersDelivered = 0;
        deliveryFee = 0;
    }

    public double calculateLocationToRestaurant(Location location){
        return currentLocation.distance(location);
    }

    public double getLocationToRestaurant(Location location){
        return calculateLocationToRestaurant(location);
    }

    public void setLocationAfterDelivery(Location location){
        currentLocation = location;
    }

    public String getId() {
        return id;
    }

    public void incrementOrders(){
        this.ordersDelivered += 1;
    }

    public void updateDeliveryFee(double deliveryFee){
        this.deliveryFee += deliveryFee;
    }

    public String getName() {
        return name;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total deliveries: %d Total delivery fee: %.2f Average delivery fee: %.2f",
                id, name, totalDeliveries(), totalDeliveryFee(), avgDeliveryFee());
    }

    public int totalDeliveries(){
        return ordersDelivered;
    }

    public double totalDeliveryFee(){
        return deliveryFee;
    }

    public double avgDeliveryFee(){
        if(ordersDelivered == 0){
            return 0;
        }
        return totalDeliveryFee() / ordersDelivered;
    }

    public double calculateExtraFee(Location location) {
        return location.distance(currentLocation);
    }

    public void setRestaurantLocation(Location location) {
        this.restaurantLocation = location;
    }
    public double calculateTest(){
        return restaurantLocation.distance(currentLocation);
    }


}

class Restaurant{
    String id;
    String name;
    Location location;
    int totalOrders;
    double moneyEarned;

    public Restaurant(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        totalOrders = 0;
        moneyEarned = 0;

    }
    public void setMoneyEarned(double moneyEarned){
        this.moneyEarned += moneyEarned;
    }

    public void incrementOrders(){
        this.totalOrders += 1;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount earned: %.2f Average amount earned: %.2f",
                id, name, totalOrders(), totalAmountEarned(), avgAmountEarned());
    }

    public int totalOrders() {
        return totalOrders;
    }

    public double totalAmountEarned(){
        return moneyEarned;
    }

    public double avgAmountEarned(){
        if(totalOrders == 0){
            return 0;
        }
        return moneyEarned / totalOrders;
    }

}

class User {
    String id;
    String name;
    List<Address> addresses;
    double moneySpent;
    int totalOrders;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        addresses = new ArrayList<>();
        moneySpent = 0;
        totalOrders = 0;
    }

    public String getId() {
        return id;
    }

    public void setMoneySpent(double moneySpent) {
        this.moneySpent += moneySpent;
    }

    public void incrementTotalOrders(){
        this.totalOrders += 1;
    }


    public String getName() {
        return name;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    @Override
    public String toString() {
//            ID: 1 Name: stefan Total orders: 1 Total amount spent: 450.00 Average amount spent: 450.00
        return String.format("ID: %s Name: %s Total orders: %d Total amount spent: %.2f Average amount spent: %.2f",
                id, name, totalOrders(), totalAmountSpent(), avgAmountSpent());
    }

    public int totalOrders() {
        return totalOrders;
    }

    public double totalAmountSpent(){
        return moneySpent;
    }
    public double avgAmountSpent(){
        if(totalOrders == 0){
            return 0;
        }
        return moneySpent / totalOrders;
    }
}
class Address{
    String name;
    Location location;

    public Address(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }
}
class DeliveryApp{
    String name;
    Map<String, DeliveryPerson> deliveryPersonMap;
    Map<String, Restaurant> restaurantMap;
    Map<String, User> userMap;

    public DeliveryApp(String name) {
        this.name = name;
        deliveryPersonMap = new HashMap<>();
        restaurantMap = new HashMap<>();
        userMap = new HashMap<>();
    }

    void registerDeliveryPerson (String id, String name, Location currentLocation){
        deliveryPersonMap.putIfAbsent(id, new DeliveryPerson(id, name, currentLocation));
    }

    void addRestaurant (String id, String name, Location location){
        restaurantMap.putIfAbsent(id, new Restaurant(id, name, location));
    }

    void addUser (String id, String name){
        userMap.putIfAbsent(id, new User(id, name));
    }

    void addAddress (String id, String addressName, Location location){
        userMap.get(id).addresses.add(new Address(addressName, location));
    }

    void orderFood(String userId, String userAddressName, String restaurantId, float cost){

        Restaurant restaurant = restaurantMap.get(restaurantId);
        User user = userMap.get(userId);

        user.setMoneySpent(cost);
        user.incrementTotalOrders();

        restaurant.setMoneyEarned(cost);
        restaurant.incrementOrders();

        Address userLocation = user.addresses.stream().filter(i -> i.name.equals(userAddressName)).findFirst().get();


        deliveryPersonMap.values().stream().forEach(deliveryPerson -> {
            deliveryPerson.setRestaurantLocation(restaurant.location);
        });

        DeliveryPerson deliveryPersonClosestToRestaurant = deliveryPersonMap.values().stream()
                .sorted(Comparator.comparing(DeliveryPerson::calculateTest).thenComparing(DeliveryPerson::totalDeliveries))
                .findFirst()
                .get();



        double extraFee = deliveryPersonClosestToRestaurant.currentLocation.distance(restaurant.location);
//
        deliveryPersonClosestToRestaurant.setLocationAfterDelivery(userLocation.location);
        deliveryPersonClosestToRestaurant.incrementOrders();
//
        double totalFee = 90 + 10 * ((int)extraFee / 10);
        deliveryPersonClosestToRestaurant.updateDeliveryFee(totalFee);

    }

    void printUsers(){
        userMap.values()
                .stream()
                .sorted(Comparator.comparing(User::totalAmountSpent).thenComparing(User::getId).reversed())
                .forEach(System.out::println);
    }

    void printRestaurants(){
        restaurantMap.values()
                .stream()
                .sorted(Comparator.comparing(Restaurant::avgAmountEarned).thenComparing(Restaurant::getId).reversed())
                .forEach(System.out::println);
    }

    void printDeliveryPeople(){
        deliveryPersonMap.values()
                .stream()
                .sorted(Comparator.comparing(DeliveryPerson::totalDeliveryFee).thenComparing(DeliveryPerson::getId).reversed())
                .forEach(System.out::println);
    }
}

interface Location {
    int getX();

    int getY();

    default int distance(Location other) {
        int xDiff = Math.abs(getX() - other.getX());
        int yDiff = Math.abs(getY() - other.getY());
        return xDiff + yDiff;
    }
}

class LocationCreator {
    public static Location create(int x, int y) {

        return new Location() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }
        };
    }
}

public class DeliveryAppTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String appName = sc.nextLine();
        DeliveryApp app = new DeliveryApp(appName);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                app.addUser(id, name);
            } else if (parts[0].equals("registerDeliveryPerson")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.registerDeliveryPerson(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addRestaurant")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addRestaurant(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addAddress")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addAddress(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("orderFood")) {
                String userId = parts[1];
                String userAddressName = parts[2];
                String restaurantId = parts[3];
                float cost = Float.parseFloat(parts[4]);
                app.orderFood(userId, userAddressName, restaurantId, cost);
            } else if (parts[0].equals("printUsers")) {
                app.printUsers();
            } else if (parts[0].equals("printRestaurants")) {
                app.printRestaurants();
            } else {
                app.printDeliveryPeople();
            }

        }
    }
}
