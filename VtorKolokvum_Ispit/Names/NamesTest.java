package VtorKolokvum_Ispit.Names;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// vashiot kod ovde
class Names {
    Map<String, Integer> nameMap;

    public Names() {
        nameMap = new TreeMap<>();
    }

    public void addName(String name){

        if(!nameMap.containsKey(name)){
            nameMap.put(name, 1);
        }else{
            Integer val = nameMap.get(name);
            nameMap.put(name, ++val);
        }

    }

    public void printN(int n){
        List<Map.Entry<String, Integer>> filteredMap = nameMap.entrySet().stream().filter(i -> i.getValue() >= n).collect(Collectors.toList());
        for (Map.Entry<String, Integer> map : filteredMap) {
            int uniqueLetters = uniqueLetters(map.getKey());
            System.out.println(String.format("%s (%d) %d", map.getKey(), map.getValue(), uniqueLetters));
        }
    }

    private int uniqueLetters(String name) {
        Set<String> set = new HashSet<>();

        name = name.toLowerCase();
        String[] parts = name.split("");
        for (String part : parts) {
            set.add(part);
        }
        return set.size();
    }

    public String findName(int len, int x){
        List<String> nameList = nameMap.keySet().stream().collect(Collectors.toList());

        List<String> firstFilter = nameList.stream().filter(i -> i.length() < len).collect(Collectors.toList());

        if(firstFilter.size() > x){
            return firstFilter.get(x);
        }else{
            return firstFilter.get(x%firstFilter.size());
        }
    }
}