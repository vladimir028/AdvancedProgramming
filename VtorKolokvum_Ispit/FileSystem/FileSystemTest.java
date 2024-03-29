package VtorKolokvum_Ispit.FileSystem;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Partial exam II 2016/2017
 */
public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}

// Your code here
class File implements Comparable<File>{
    String name;
    int size;
    LocalDateTime createdAt;

    public File(String name, int size, LocalDateTime createdAt) {
        this.name = name;
        this.size = size;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
//        %-10[name] %5[size]B %[createdAt]
        return String.format("%-10s %5dB %s", name, size, createdAt);
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public int compareTo(File o) {
        int res = createdAt.compareTo(o.createdAt);
        if(res == 0){
            res = name.compareTo(o.getName());
            if(res == 0){
                res = Math.max(size, o.size);
            }
        }
        return res;
    }
}

class FileSystem {
    Map<Character, List<File>> fileMap;

    public FileSystem() {
        fileMap = new HashMap<>();
    }

    public void addFile(char folder, String name, int size, LocalDateTime createdAt){
        fileMap.putIfAbsent(folder, new ArrayList<>());
        List<File> file = fileMap.get(folder);
        file.add(new File(name, size, createdAt));
    }

    public List<File> findAllHiddenFilesWithSizeLessThen(int size){
        return fileMap.entrySet()
                .stream()
//                .filter(i -> i.getKey().toString().equals("."))
                .flatMap(i -> i.getValue().stream())
                .filter(i -> i.getSize() < size && i.name.charAt(0) == '.')
                .sorted()
                .collect(Collectors.toList());
    }

    public int totalSizeOfFilesFromFolders(List<Character> folders){
        int sum = 0;
        for (Character folder : folders) {
            List<File> file = fileMap.get(folder);
            for (File file1 : file) {
                sum += file1.getSize();
            }
        }
        return sum;
    }

    public Map<Integer, Set<File>> byYear(){
//        Map<Integer, Set<File>> map = new HashMap<>();
//        List<Integer> years = fileMap.values().stream().flatMap(Collection::stream).map(i -> i.getCreatedAt().getYear()).collect(Collectors.toList());
////        System.out.println(years);
//        List<File> files = fileMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
//        for (Integer year : years) {
//            map.putIfAbsent(year, new HashSet<>());
//        }
//        for (File file : files) {
//            map.get(file.getCreatedAt().getYear()).add(file);
//        }
////        System.out.println(map);
//        return map;
        return fileMap.values().stream()
                .flatMap(i -> i.stream())
                .collect(Collectors.groupingBy(
                        file -> file.getCreatedAt().getYear(),
                        Collectors.toCollection(() -> new TreeSet<>())
                ));
    }

    public Map<String, Long> sizeByMonthAndDay(){
//        Map<String, Long> map = new HashMap<>();
//        List<String> dayAndMonth = fileMap.values().stream().flatMap(Collection::stream).map(i -> i.getCreatedAt().getMonth() + "-" + i.getCreatedAt().getDayOfMonth()).collect(Collectors.toList());
//        for (String s : dayAndMonth) {
//            map.put(s, -1l);
//        }
//
//        List<File> files = fileMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
////        files.forEach(i -> System.out.println(i + " " + i.getCreatedAt().getMonth() + '-' + i.getCreatedAt().getDayOfMonth()));
//        Map<String, List<File>> tmp = new HashMap<>();
//        for (File file : files) {
//            tmp.putIfAbsent(file.getCreatedAt().getMonth() + "-" + file.getCreatedAt().getDayOfMonth(), new ArrayList<>());
//            tmp.get(file.getCreatedAt().getMonth() + "-" + file.getCreatedAt().getDayOfMonth()).add(file);
//        }
////        long sum = (long) tmp.values().stream().flatMap(Collection::stream).map(File::getSize).mapToInt(i -> i).sum();
////        System.out.println(sum);
//
//        for (List<File> file : tmp.values()) {
//            long sum = file.stream().mapToLong(i -> i.getSize()).sum();
//            map.put(file.get(0).getCreatedAt().getMonth() + "-" + file.get(0).getCreatedAt().getDayOfMonth(), sum);
//        }
//        return map;
        return fileMap.values().stream()
                .flatMap(i -> i.stream())
                .collect(Collectors.groupingBy(
                        file -> file.getCreatedAt().getMonth() + "-" + file.getCreatedAt().getDayOfMonth(),
                        Collectors.summingLong(i -> i.getSize())
                ));
    }
}