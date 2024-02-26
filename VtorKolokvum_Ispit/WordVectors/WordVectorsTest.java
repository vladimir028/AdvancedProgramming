package VtorKolokvum_Ispit.WordVectors;
//glupo reshenie
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Word vectors test
 */
public class WordVectorsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] words = new String[n];
        List<List<Integer>> vectors = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            words[i] = parts[0];
            List<Integer> vector = Arrays.stream(parts[1].split(":"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            vectors.add(vector);
        }
        n = scanner.nextInt();
        scanner.nextLine();
        List<String> wordsList = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            wordsList.add(scanner.nextLine());
        }
        WordVectors wordVectors = new WordVectors(words, vectors);
        wordVectors.readWords(wordsList);
        n = scanner.nextInt();
        List<Integer> result = wordVectors.slidingWindow(n);
        System.out.println(result.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));
        scanner.close();
    }
}



class WordVectors {

    Map<String, List<Integer>> wordVector;
    Map<String, List<Integer>> mapByWord;


    public WordVectors(String[] words, List<List<Integer>> vectors){
        wordVector = new LinkedHashMap<>();

        if(words.length == vectors.size()){
            for (int i = 0; i<words.length; i++){
                wordVector.putIfAbsent(words[i], vectors.get(i));
            }
        }

    }

    public void readWords(List<String> words){
        mapByWord = new LinkedHashMap<>();
        int i = 0;
        for (String word : words) {
            List<Integer> a = wordVector.get(word);
            if ( a == null){
                List<Integer> tmp = defaultValues();

                if(mapByWord.get(word) == null){
                    mapByWord.put(word, tmp);
                }
                else{
                    mapByWord.put(word+i++, tmp);
                }
                wordVector.putIfAbsent(word, tmp);
                mapByWord.putIfAbsent(word, tmp);
            }else {
                if(mapByWord.get(word) == null) {
                    mapByWord.put(word, a);
                }else{
                    mapByWord.put(word+i++, a);
                }
            }
        }
    }
    private List<Integer> defaultValues(){
        List<Integer> tmp = new ArrayList<>();
        for(int i=0; i<5; i++){
            tmp.add(5);
        }
        return tmp;
    }

    public List<Integer> slidingWindow(int n){
        List<List<Integer>> vectors = mapByWord.values().stream().collect(Collectors.toList());

        List<List<Integer>> tmp = new ArrayList<>();
        List<Integer> finalList = new ArrayList<>();
        int i = 0;

//        System.out.println(mapByWord);
        while (i != vectors.size()){
            tmp.add(vectors.get(i++));
            if(i == n){
                int maxNum = calculateVector(tmp);
//                System.out.println(tmp + " " + maxNum);
                tmp = new ArrayList<>();
                finalList.add(maxNum);
                vectors.remove(vectors.get(0));
                i = 0;
            }
        }

        return finalList;
    }

    private int calculateVector(List<List<Integer>> tmp) {
//        [9, 0, 6, 1, 2], [0, 8, 7, 3, 5], [4, 4, 7, 5, 7]
        List<Integer> flatMap = tmp.stream().flatMap(i -> i.stream()).collect(Collectors.toList());

//        [9, 0, 6, 1, 2,
//        0, 8, 7, 3, 5,
//        4, 4, 7, 5, 7]

//        [5, 5, 5, 5, 5,
//        3, 1, 2, 2, 7,
//        6, 7, 0, 7, 2]

        List<Integer> tmpMap = new ArrayList<>();
        for(int i=0; i<5; i++){
            int sum = 0;
            for(int j =i; j<flatMap.size(); j+=5){
                sum += flatMap.get(j);
            }
            tmpMap.add(sum);
        }
        tmpMap = tmpMap.stream().sorted(Comparator.comparing(integer -> integer)).collect(Collectors.toList());
        Integer a = tmpMap.get(tmpMap.size() - 1);
//        System.out.println(a);
        return a;
    }


}