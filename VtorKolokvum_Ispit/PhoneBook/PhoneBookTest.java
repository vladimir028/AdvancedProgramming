package VtorKolokvum_Ispit.PhoneBook;

import java.util.*;
import java.util.stream.Collectors;

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}

// Вашиот код овде
class Contact{
    String name;
    String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return name + " " + number;
    }
}
class PhoneBook{

    Map<String, List<Contact>> contactMap;

    public PhoneBook() {
        contactMap = new HashMap<>();
    }

    void addContact(String name, String number) throws DuplicateNumberException {


        contactMap.putIfAbsent(name, new ArrayList<>());
        Contact c = new Contact(name, number);
        List<Contact> contacts = contactMap.get(name);


        boolean exists = contacts.stream().anyMatch(i -> i.getNumber().equals(number));
        if(exists){
            throw new DuplicateNumberException(number);
        }
        contacts.add(c);
    }

    void contactsByNumber(String number){
        boolean exists = contactMap.values().stream().
                flatMap(Collection::stream).
                anyMatch(i -> i.getNumber().contains(number));
        if(!exists){
            System.out.println("NOT FOUND");
        }else{
            contactMap.values().stream().
                    flatMap(Collection::stream).
                    filter(i -> i.getNumber().contains(number)).
                    sorted(Comparator.comparing(Contact::getName).thenComparing(Contact::getNumber)).
                    forEach(System.out::println);
        }

    }

    void contactsByName(String name){
        boolean exists = contactMap.entrySet().stream().
                anyMatch(i -> i.getKey().equals(name));

        if(!exists){
            System.out.println("NOT FOUND");
        }else{
            contactMap.entrySet().stream().
                    filter(i -> i.getKey().equals(name)).
                    forEach(i -> i.getValue().stream().
                            sorted(Comparator.comparing(Contact::getName).thenComparing(Contact::getNumber)).
                            forEach(System.out::println));
        }

    }
}

class DuplicateNumberException extends Exception{

    public DuplicateNumberException(String message) {
        super("Duplicate number: " + message);
    }
}