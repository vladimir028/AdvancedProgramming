package VtorKolokvum_Ispit.Airports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde

class Airport{
    String name;
    String country;
    String code;
    int passengers;
    Set<Flight> flights;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        flights = new TreeSet<>(Comparator.comparing(Flight::getTo).thenComparing(Flight::getTakeoff));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)\n" +
                "%s\n" +
                "%d", name, code, country, passengers);
    }
}

class Flight{
    String from;
    String to;
    int time; // Bреме на тргнување во минути поминати од 0:00 часот
    int duration; // времетраење на летот во минути
    static int counter = 0;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getTime() {
        return time;
    }

    public int getCounter() {
        return counter;
    }

    public String getTakeoff(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm");
        try {
            Date dateTime = simpleDateFormat.parse(String.valueOf(time));
            simpleDateFormat = new SimpleDateFormat("HH:mm");
            return simpleDateFormat.format(dateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLandingTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm");

        try {
            Date dateTime = simpleDateFormat.parse(String.valueOf(duration + time));
            simpleDateFormat = new SimpleDateFormat("HH:mm");
            return simpleDateFormat.format(dateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDuration(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm");

        try {
            Date dateTime = simpleDateFormat.parse(String.valueOf(duration));
            simpleDateFormat = new SimpleDateFormat("HH:mm");
            String []tmp = simpleDateFormat.format(dateTime).split(":");
            String h = tmp[0];
            String min = tmp[1];

            if(h.startsWith("0")){
                h = h.substring(1, h.length());
            }
            return duration + time < 1440 ? String.format("%sh%sm", h, min) :  String.format("+1d %sh%sm", h, min);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    public String printFlights(int i) {
        return i != -1 ? String.format("%d. %s-%s %s-%s %s", ++counter, from, to, getTakeoff(), getLandingTime(), getDuration()) :
                String.format("%s-%s %s-%s %s", from, to, getTakeoff(), getLandingTime(), getDuration());
    }
}

class Airports{

    Map<String, Airport> airportMap;

    public Airports() {
        airportMap = new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers){
        airportMap.putIfAbsent(code, new Airport(name, country, code, passengers));
    }

    public void addFlights(String from, String to, int time, int duration) {
        Flight flight = new Flight(from, to, time, duration);
        airportMap.get(to).flights.add(flight);
        airportMap.get(from).flights.add(flight);

    }

    public void showFlightsFromAirport(String code){
        Airport airport = airportMap.get(code);
        System.out.println(airport);

        List<Flight> flights = airport.flights.stream().filter(i -> i.from.equals(code)).collect(Collectors.toList());
        flights.forEach(flight -> System.out.println(flight.printFlights(1)));
    }

    public void showDirectFlightsFromTo(String from, String to){
        TreeSet<Flight> flights = airportMap.values().stream().flatMap(i -> i.flights.stream())
                .filter(i -> i.from.equals(from) && i.to.equals(to))
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Flight::getTo).thenComparing(Flight::getTakeoff))));

        if(flights.isEmpty()){
            System.out.println(String.format("No flights from %s to %s", from, to));
        }else{
            flights.forEach(flight -> System.out.println(flight.printFlights(-1)));
        }
    }

    public void showDirectFlightsTo(String to){
        TreeSet<Flight> flights  = airportMap.values().stream().flatMap(i -> i.flights.stream())
                .filter(i -> i.getTo().equals(to))
                .distinct()
//                .collect(Collectors.toSet());
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Flight::getTo).thenComparing(Flight::getTakeoff).thenComparing(Flight::getDuration))));


        flights.forEach(flight -> System.out.println(flight.printFlights(-1)));
    }
}


