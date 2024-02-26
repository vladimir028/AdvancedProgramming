package VtorKolokvum_Ispit.Stadium;

import java.util.*;

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}

class Sector{
    String code;
    int capacity;
    List<Boolean> seatTaken;
    public int type;
    public Sector(String code, int capacity) {
        this.code = code;
        this.capacity = capacity;
        seatTaken = new ArrayList<>();
        type = -1;
        for(int i=0; i<capacity; i++){
            seatTaken.add(false);
        }
    }

    public String getCode() {
        return code;
    }

    public void setSeatTakenToTrue(int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        if(seatTaken.get(seat-1)){
            throw new SeatTakenException();
        }
        if(type == 0){
            seatTaken.set(seat-1, true);
            return;
        }
        else if(this.type == -1){
            this.type = type;
        }
        else if(this.type != type){
            throw new SeatNotAllowedException();
        }
        seatTaken.set(seat-1, true);
    }

    public long freeSeats(){
        return seatTaken.stream().filter(i -> !i).count();
    }

    public double calculatePercentage(){
        return 100-((freeSeats() * 1.0 / seatTaken.size()) * 100);
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%", code, freeSeats(), seatTaken.size(), calculatePercentage());
    }
}

class Stadium {
    String name;
    List<Sector> sectors;
    Map<String, Sector> mapBySector;

    public Stadium(String name) {
        this.name = name;
        sectors = new ArrayList<>();
        mapBySector = new HashMap<>();
    }

    void createSectors(String[] sectorNames, int[] sizes){
        for(int i=0; i<sectorNames.length; i++){
            sectors.add(new Sector(sectorNames[i], sizes[i]));
            mapBySector.putIfAbsent(sectorNames[i], new Sector(sectorNames[i], sizes[i]));
        }
    }
    void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
//A;100
//A;12;2
        Sector sector = mapBySector.get(sectorName);
        sector.setSeatTakenToTrue(seat, type);
    }
    void showSectors(){
        mapBySector.values().stream()
                .sorted(Comparator.comparing(Sector::calculatePercentage).thenComparing(Sector::getCode))
                .forEach(System.out::println);
    }
}
class SeatTakenException extends Exception{
    public SeatTakenException() {
    }
}

class SeatNotAllowedException extends Exception{
    public SeatNotAllowedException() {
    }
}