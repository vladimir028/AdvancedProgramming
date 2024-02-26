package VtorKolokvum_Ispit.Discounts;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Discounts
 */
public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

// Vashiot kod ovde
class Discounts {
    List<Store> storeList;

    public Discounts() {
        storeList = new ArrayList<>();
    }

    public int readStores(InputStream inputStream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        storeList = reader.lines()
                .map(line -> new Store(line))
                .collect(Collectors.toList());

        return storeList.size();
    }

    public List<Store> byAverageDiscount(){
        return storeList.stream().sorted(Comparator.comparing(Store::avgDiscount).reversed()).collect(Collectors.toList()).subList(0,3);
    }

    public List<Store> byTotalDiscount(){
        return storeList.stream().sorted(Comparator.comparing(Store::totalDiscount).thenComparing(Store::getName)).collect(Collectors.toList()).subList(0,3);
    }
}
class Price{

    int discountedPrice;
    int oldPrice;
    public Price( int discountedPrice, int oldPrice) {
        this.discountedPrice = discountedPrice;
        this.oldPrice = oldPrice;
    }

    public int calculateDiscount(){
        double tmp = (totalDiscount() / (oldPrice * 1.0)) * 100;
        return (int) tmp;
    }

    public int totalDiscount(){
        return oldPrice - discountedPrice;
    }

}

class Store{

    String name;
    List<Price> prices;
    public Store(String line) {
        String [] parts = line.split("\\s+");
        this.name = parts[0];
        prices = new ArrayList<>();

        for(int i=1; i<parts.length; i++){
            String [] price = parts[i].split(":");
            prices.add(new Price(Integer.parseInt(price[0]), Integer.parseInt(price[1])));
        }
    }

    public String getName() {
        return name;
    }

    public void calculateDiscount(){
        prices = prices.stream().sorted(Comparator.comparing(Price::calculateDiscount).thenComparing(Price::totalDiscount).reversed()).collect(Collectors.toList());
    }

    public int totalDiscount(){
        return prices.stream().mapToInt(Price::totalDiscount).sum();
    }

    public double avgDiscount() {
        return prices.stream().mapToDouble(Price::calculateDiscount).average().orElse(0.0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name + '\n');
        sb.append(String.format("Average discount: %.1f%%\n", avgDiscount()));
        sb.append(String.format("Total discount: %d\n", totalDiscount()));
        calculateDiscount();
        int i=0;
        for (Price price : prices) {
            if(i != prices.size()-1 )
                sb.append(String.format("%2d%% %d/%d\n", price.calculateDiscount(), price.discountedPrice, price.oldPrice));
            else
                sb.append(String.format("%2d%% %d/%d", price.calculateDiscount(), price.discountedPrice, price.oldPrice));

            i++;
        }
        return sb.toString();
    }

}