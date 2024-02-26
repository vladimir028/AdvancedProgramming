package VtorKolokvum_Ispit.EventCalendar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            LocalDateTime dateTime = LocalDateTime.parse(parts[2], formatter);

            try {
                eventCalendar.addEvent(name, location, dateTime);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }

        LocalDateTime dateTime = LocalDateTime.parse(scanner.nextLine(), formatter);
        eventCalendar.listEvents(dateTime);

        eventCalendar.listByMonth();
    }
}

class Event{
    String name;
    String location;
    LocalDateTime dateTime;

    public Event(String name, String location, LocalDateTime dateTime) {
        this.name = name;
        this.location = location;
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
//        19 Apr, 2012 15:30 at FINKI, Brucoshka Zabava
        return String.format("%s %s, %02d %02d:%02d at %s, %s",
                dateTime.getDayOfMonth(), getMonth(),
                dateTime.getYear(), dateTime.getHour(), dateTime.getMinute(),
                location, name);
    }

    private String getMonth() {
        String month = dateTime.getMonth().toString().substring(0, 3).toLowerCase();
        return Character.toUpperCase(month.charAt(0)) + month.substring(1);
    }
}
class EventCalendar{
    int year;
    Set<Event> events;
    public EventCalendar(int year){
        this.year = year;
        events = new TreeSet<>(Comparator.comparing(Event::getDateTime).thenComparing(Event::getName));
    }

    public void addEvent(String name, String location, LocalDateTime date) throws WrongDateException {
        if(year != date.getYear()){
//            Thu Feb 14 11:00:00 UTC 2013
            throw new WrongDateException(String.format("Wrong date: %s %s %d %02d:%02d:%02d UTC %d",
                    Character.toUpperCase(date.getDayOfWeek().toString().substring(0,3).toLowerCase().charAt(0)) + date.getDayOfWeek().toString().substring(0,3).toLowerCase().substring(1),
                    Character.toUpperCase(date.getMonth().toString().substring(0, 3).toLowerCase().charAt(0)) + date.getMonth().toString().substring(0, 3).toLowerCase().substring(1),
                    date.getDayOfMonth(),
                    date.getHour(),
                    date.getMinute(),
                    date.getSecond(),
                    date.getYear()));
        }
        events.add(new Event(name, location, date));
    }

    public void listEvents(LocalDateTime date) {
        List<Event> a = events.stream()
                .filter(i -> i.dateTime.getMonth().equals(date.getMonth()) && i.dateTime.getDayOfMonth() == date.getDayOfMonth())
                .collect(Collectors.toList());
        if(a.isEmpty()){
            System.out.println("No events on this day!");
            return;
        }
        a.forEach(System.out::println);
    }

    public void listByMonth(){
        TreeMap<Integer, Long> map = events.stream()
                .collect(Collectors.groupingBy(
                        i -> i.dateTime.getMonthValue(),
                        TreeMap::new,
                        Collectors.counting()
                ));

        for(int i = 1; i<=12; i++) {
            map.putIfAbsent(i, 0L);
        }

        map.forEach((key, value) -> System.out.println(String.format("%d : %d", key, value)));
    }

}

class WrongDateException extends Exception{
    public WrongDateException(String message) {
        super(message);
    }
}

class EventNotFound extends Exception{
    public EventNotFound(String message) {
        super(message);
    }
}