package VtorKolokvum_Ispit.OnlineShop;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String id) {
        super(String.format("Product with id %s does not exist in the online shop!", id));
    }
}


class Product {
    String id;
    String name;
    LocalDateTime createdAt;
    double price;
    int quantitySold;
    String category;

    public Product(String category, String id, String name, LocalDateTime createdAt, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
        this.quantitySold = 0;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", price=" + price +
                ", quantitySold=" + quantitySold +
                '}';
    }

    public double totalPrice() {
        return quantitySold * price;
    }
}


class OnlineShop {

    Map<String, Product> productMap;

    OnlineShop() {
        productMap = new HashMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price){
        productMap.putIfAbsent(id, new Product(category, id, name, createdAt, price));
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException{
        Product product = productMap.get(id);
        if(product == null)
            throw new ProductNotFoundException(id);
        product.setQuantitySold(quantity);
        return product.totalPrice();
    }

    Comparator<Product> comparator (COMPARATOR_TYPE comparatorType){

        if(comparatorType == COMPARATOR_TYPE.NEWEST_FIRST){
            return Comparator.comparing(Product::getCreatedAt).reversed();
        }
        else if(comparatorType == COMPARATOR_TYPE.OLDEST_FIRST){
            return Comparator.comparing(Product::getCreatedAt);
        }
        else if(comparatorType == COMPARATOR_TYPE.LOWEST_PRICE_FIRST){
            return Comparator.comparing(Product::getPrice);
        }
        else if(comparatorType == COMPARATOR_TYPE.HIGHEST_PRICE_FIRST){
            return Comparator.comparing(Product::getPrice).reversed();
        }
        else if(comparatorType == COMPARATOR_TYPE.MOST_SOLD_FIRST){
            return Comparator.comparing(Product::getQuantitySold).reversed();
        }
        else if(comparatorType == COMPARATOR_TYPE.LEAST_SOLD_FIRST){
            return Comparator.comparing(Product::getQuantitySold);
        }
        return null;
    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<Product> values = new ArrayList<>(productMap.values());

        if(category != null) {
            Map <String, List<Product>> newMap = mapByCategory(values);
            List<Product> tmp = newMap.get(category);
            values = tmp;
        }
        values = values.stream().sorted(comparator(comparatorType)).collect(Collectors.toList());

        return getByPages(values, pageSize);
    }

    private List<List<Product>> getByPages(List<Product> values, int pageSize) {
        List<Product> productList = new ArrayList<>();
        List<List<Product>> result = new ArrayList<>();
        for(int i=0; i<values.size(); i++){
            productList.add(values.get(i));
            if(productList.size() == pageSize){
                result.add(productList);
                productList = new ArrayList<>();
            }
        }
        if(productList.size() != 0){
            result.add(productList);
        }
        return result;
    }

    private Map<String, List<Product>> mapByCategory(List<Product> values) {
        return values.stream().collect(Collectors.groupingBy(
                i -> i.category,
                Collectors.toList()
        ));
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category=null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}

