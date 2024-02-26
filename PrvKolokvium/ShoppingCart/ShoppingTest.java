package PrvKolokvium.ShoppingCart;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

abstract class Shopping{
    private String type;
    private String productId;
    private String productName;

    public Shopping(String type, String productId, String productName) {
        this.type = type;
        this.productId = productId;
        this.productName = productName;
    }

    public String getType() {
        return type;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    abstract double calculatePrice();
}

class WholeShopping extends Shopping{
    private int productPrice;
    private int quantity;

    public WholeShopping(String type, String productId, String productName, int productPrice, int quantity) {
        super(type, productId, productName);
        this.productPrice = productPrice;
        this.quantity = quantity;
    }
    @Override
    double calculatePrice() {
        return productPrice * quantity;
    }

    @Override
    public String toString() {
        return String.format("%s - %.2f", getProductId(), calculatePrice());
    }
}

class PieceShopping extends Shopping{
    private int productPrice;
    private double quantity;

    public PieceShopping(String type, String productId, String productName, int productPrice, double quantity) {
        super(type, productId, productName);
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    @Override
    double calculatePrice() {
        return quantity/1000 * productPrice;
    }

    @Override
    public String toString() {
        return String.format("%s - %.2f", getProductId(), calculatePrice());
    }
}
class ShoppingCart{

    private List<Shopping> shoppingList;

    public ShoppingCart() {
        shoppingList = new ArrayList<>();
    }

    public void addItem(String itemData) throws InvalidOperationException {
        String [] parts = itemData.split(";");

        if((parts[4]).equals("0")){
            throw new InvalidOperationException(parts[1]);
        }
        if(parts[0].equals("WS")){
            shoppingList.add(new WholeShopping(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), Integer.parseInt(parts[4])));
        }
        else if(parts[0].equals("PS")) {
            shoppingList.add(new PieceShopping(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), Double.parseDouble(parts[4])));
        }
    }
    public List<Shopping> sorted(){
        Comparator<Shopping> comparator = Comparator.comparing(Shopping::calculatePrice).reversed();
        shoppingList = shoppingList.stream().sorted(comparator).collect(Collectors.toList());

        return shoppingList;
    }
    public void printShoppingCart(OutputStream os){
        sorted();
        PrintWriter writer = new PrintWriter(os);

        for(Shopping s: shoppingList){
            writer.println(s);
        }
        writer.flush();
    }

    public void blackFridayOffer(List<Integer> discountItems, OutputStream os) throws InvalidOperationException {
        if(discountItems.size() == 0){
            throw new InvalidOperationException("");
        }

        PrintWriter printWriter = new PrintWriter(os);


        for(Shopping s: shoppingList){
            double discountPrice = 0;
            for(int i=0; i<discountItems.size(); i++){
                if(Integer.parseInt(s.getProductId()) == discountItems.get(i)){
                    discountPrice = s.calculatePrice() * 0.1;
                    printWriter.format("%s - %.2f\n", discountItems.get(i), discountPrice);
                }
            }
        }

        printWriter.flush();
    }
}
class InvalidOperationException extends Exception{
    String message;
    public InvalidOperationException(String message) {
        this.message = message;
    }

    public String qty(){
        return String.format("The quantity of the product with id %s can not be 0.", message);
    }

    public String empty(){
        return String.format("There are no products with discount.");
    }
}

public class ShoppingTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        int items = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < items; i++) {
            try {
                cart.addItem(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.qty());
            }
        }

        List<Integer> discountItems = new ArrayList<>();
        int discountItemsCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < discountItemsCount; i++) {
            discountItems.add(Integer.parseInt(sc.nextLine()));
        }

        int testCase = Integer.parseInt(sc.nextLine());
        if (testCase == 1) {
            cart.printShoppingCart(System.out);
        }
        else if (testCase == 2) {
            try {
                cart.blackFridayOffer(discountItems, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.empty());
            }
        }
        else {
            System.out.println("Invalid test case");
        }
    }
}