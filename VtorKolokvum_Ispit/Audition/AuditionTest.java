package VtorKolokvum_Ispit.Audition;

import java.util.*;

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}

class Participant{
    String city;
    String code;
    String name;
    int age;
    Set<String> codes;

    public Participant(String city, String code, String name, int age) {
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
        codes = new HashSet<>();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
//        003 Katarina 17
        return code + " " + name + " " + age;
    }

    public void addCode(String code) {
        this.codes.add(code);
    }

    public boolean hasCode(String code) {
        return codes.contains(code);
    }
}

class Audition{

    Map<String, List<Participant>> participantMap;

    public Audition() {
        participantMap = new HashMap<>();
    }

    void addParticpant(String city, String code, String name, int age){
        Participant p = new Participant(city, code, name, age);
        participantMap.putIfAbsent(city, new ArrayList<>());
        List<Participant> participants = participantMap.get(city);
        if(participants.isEmpty()){
            participants.add(p);
        }else {
            for (Participant participant : participants) {
                if (participant.code.equals(code)){
                    return;
                }
            }
            participants.add(p);
        }

    }

    void listByCity(String city){
        List<Participant> participants = participantMap.get(city);
        participants.stream().sorted(Comparator.comparing(Participant::getName).thenComparing(Participant::getAge).thenComparing(Participant::getCode))
                .forEach(System.out::println);
    }
}


// staro reshenie
// // import java.util.*;
// import java.util.stream.Collectors;

// class Participant{
//     String city;
//     String code;
//     String name;
//     int age;

//     public Participant(String city, String code, String name, int age) {
//         this.city = city;
//         this.code = code;
//         this.name = name;
//         this.age = age;
//     }

//     public String getCity() {
//         return city;
//     }

//     public void setCity(String city) {
//         this.city = city;
//     }

//     public String getCode() {
//         return code;
//     }

//     public void setCode(String code) {
//         this.code = code;
//     }

//     public String getName() {
//         return name;
//     }

//     public void setName(String name) {
//         this.name = name;
//     }

//     public int getAge() {
//         return age;
//     }

//     public void setAge(int age) {
//         this.age = age;
//     }

//     @Override
//     public String toString() {
// //        003 Katarina 17
//         return code + " " + name + " " + age;
//     }
// }
// class Audition{

//     Map<Map<String, String>, Participant> participantMap;

//     public Audition() {
//         participantMap = new HashMap<>();
//     }

//     void addParticpant(String city, String code, String name, int age){
//         Map<String, String> tmp = new HashMap<>();
//         tmp.put(city, code);
//         participantMap.putIfAbsent(tmp, new Participant(city, code, name, age));

//     }

//     void listByCity(String city){
// //         Map<String, Participant> finalMap = new HashMap<>();
// //         Set<Map<String, String>> a = participantMap.keySet();
// //         for (Map<String, String> stringStringMap : a) {
// //             Set<String> tmp = stringStringMap.keySet();
// //             for (String s : tmp) {
// //                 if(s.equals(city)){
// //                     finalMap.put(participantMap.get(stringStringMap).getCode(), participantMap.get(stringStringMap));
// //                 }
// //             }
// // //            System.out.println(stringStringMap.keySet());
// //         }
// // //        System.out.println(finalMap.values());
// //         List<Participant> finallll = finalMap.values().stream().sorted(Comparator.comparing(Participant::getName).thenComparing(Participant::getAge)).collect(Collectors.toList());
// //         for (Participant participant : finallll) {
// //             System.out.println(participant);
// //         }
//         Set<Map<String, String>> cityAndCode = participantMap.keySet();
//         List<Map<String, String>> finalMap = cityAndCode.stream().filter(i -> i.keySet().stream().collect(Collectors.joining("")).equals(city)).collect(Collectors.toList());
//         Map<String, Participant> map = new HashMap<>();
//         for (Map<String, String> stringStringMap : finalMap) {
//             map.put(participantMap.get(stringStringMap).getCode(), participantMap.get(stringStringMap));
//         }
//         List<Participant> list = map.values().stream().sorted(Comparator.comparing(Participant::getName).thenComparing(Participant::getAge)).collect(Collectors.toList());
//         list.forEach(System.out::println);
//     }
// }