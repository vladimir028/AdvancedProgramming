package VtorKolokvum_Ispit.MapSorting;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapSortingTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        List<String> l = readMapPairs(scanner);
        if(n==1){
            Map<String, Integer> map = new HashMap<>();
            fillStringIntegerMap(l, map);
            SortedSet<Map.Entry<String, Integer>> s = entriesSortedByValues(map);
            System.out.println(s);
        } else {
            Map<Integer, String> map = new HashMap<>();
            fillIntegerStringMap(l, map);
            SortedSet<Map.Entry<Integer, String>> s = entriesSortedByValues(map);
            System.out.println(s);
        }

    }

    //    private static <T, U extends Comparable<U>> List<Map.Entry<T, U>> entriesSortedByValues(Map<T, U> map) {
//        List<Map.Entry<T, U>> a = map.entrySet().stream()
//                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
//                .collect(Collectors.toList());
////         a.forEach(i -> System.out.println(i));
////        TreeSet<Map.Entry<T, U>>tmp = (TreeSet<Map.Entry<T, U>>) a;
//        return a;
//    }
    private static <K, V extends Comparable<V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> set = new TreeSet<>((a, b) -> {
            int compare = a.getValue().compareTo(b.getValue());
            return compare != 0 ? -compare : 1;
        });

        set.addAll(map.entrySet());
        System.out.println(map);
        return set;
    }

    private static List<String> readMapPairs(Scanner scanner) {
        String line = scanner.nextLine();
        String[] entries = line.split("\\s+");
        return Arrays.asList(entries);
    }

    static void fillStringIntegerMap(List<String> l, Map<String,Integer> map) {
        l.stream()
                .forEach(s -> map.put(s.substring(0, s.indexOf(':')), Integer.parseInt(s.substring(s.indexOf(':') + 1))));
    }

    static void fillIntegerStringMap(List<String> l, Map<Integer, String> map) {
        l.stream()
                .forEach(s -> map.put(Integer.parseInt(s.substring(0, s.indexOf(':'))), s.substring(s.indexOf(':') + 1)));
    }

    //вашиот код овде

}