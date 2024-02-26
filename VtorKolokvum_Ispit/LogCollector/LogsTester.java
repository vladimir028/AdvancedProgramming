package VtorKolokvum_Ispit.LogCollector;
import java.util.*;
import java.util.stream.Collectors;

public class LogsTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LogCollector collector = new LogCollector();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("addLog")) {
                collector.addLog(line.replace("addLog ", ""));
            } else if (line.startsWith("printServicesBySeverity")) {
                collector.printServicesBySeverity();
            } else if (line.startsWith("getSeverityDistribution")) {
                String[] parts = line.split("\\s+");
                String service = parts[1];
                String microservice = null;
                if (parts.length == 3) {
                    microservice = parts[2];
                }
                collector.getSeverityDistribution(service, microservice).forEach((k,v)-> System.out.printf("%d -> %d%n", k,v));
            } else if (line.startsWith("displayLogs")){
                String[] parts = line.split("\\s+");
                String service = parts[1];
                String microservice = null;
                String order = null;
                if (parts.length == 4) {
                    microservice = parts[2];
                    order = parts[3];
                } else {
                    order = parts[2];
                }
                System.out.println(line);

                collector.displayLogs(service, microservice, order);
            }
        }
    }
}

abstract class Log{
    String service_name ;
    String microservice_name;
    String type;
    String message;
    Integer timestamp;

    public Log(String service_name, String microservice_name, String type, String message, Integer timestamp) {
        this.service_name = service_name;
        this.microservice_name = microservice_name;
        this.type = type;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getService_name() {
        return service_name;
    }

    public String getMicroservice_name() {
        return microservice_name;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    abstract int calculateSeverity();

}
class InfoLogger extends Log{


    public InfoLogger(String service_name, String microservice_name, String type, String message, Integer timestamp) {
        super(service_name, microservice_name, type, message, timestamp);
    }

    @Override
    int calculateSeverity() {
        return 0;
    }
}

class WarnLogger extends Log {


    public WarnLogger(String service_name, String microservice_name, String type, String message, Integer timestamp) {
        super(service_name, microservice_name, type, message, timestamp);
    }

    @Override
    int calculateSeverity() {
        return message.contains("might cause error") ? 2 : 1;
    }
}

class ErrorLogger extends Log{


    public ErrorLogger(String service_name, String microservice_name, String type, String message, Integer timestamp) {
        super(service_name, microservice_name, type, message, timestamp);
    }

    @Override
    int calculateSeverity() {
        int sum = 3;
        if(message.contains("fatal")){
            sum += 2;
        }
        if(message.contains("exception")){
            sum +=3;
        }
        return sum;
    }
}

class LogCollector{

    Map<String, Map<String, List<Log>>> mapByService;
    List<Log> logList;
    Map<String, List<Log>> printingMap;

    public LogCollector() {
        mapByService = new HashMap<>();
        logList = new ArrayList<>();
        printingMap = new HashMap<>();
    }

    public void addLog(String log) {
        String [] parts = log.split(" ");
        String serviceName = parts[0];
        String microServiceName = parts[1];
        String type = parts[2];
        Integer timestamp = Integer.valueOf(parts[parts.length-1]);
        StringBuilder sb = new StringBuilder();

        for(int i = 3; i< parts.length-1; i++){
            sb.append(parts[i] + " ");
        }


        String message = sb.toString();



        mapByService.putIfAbsent(serviceName, new HashMap<>());
        Map<String, List<Log>> tmpMap = mapByService.get(serviceName);

        tmpMap.putIfAbsent(microServiceName, new ArrayList<>());

        printingMap.putIfAbsent(serviceName, new ArrayList<>());


        if(type.equals("ERROR")){
            logList.add(new ErrorLogger(serviceName, microServiceName, type, message, timestamp));
            tmpMap.get(microServiceName).add(new ErrorLogger(serviceName, microServiceName, type, message, timestamp));
            printingMap.get(serviceName).add(new ErrorLogger(serviceName, microServiceName, type, message, timestamp));
        }
        else if (type.equals("WARN")){
            logList.add(new WarnLogger(serviceName, microServiceName, type, message, timestamp));
            tmpMap.get(microServiceName).add(new WarnLogger(serviceName, microServiceName, type, message, timestamp));
            printingMap.get(serviceName).add(new WarnLogger(serviceName, microServiceName, type, message, timestamp));
        }
        else if (type.equals("INFO")){
            logList.add(new InfoLogger(serviceName, microServiceName, type, message, timestamp));
            tmpMap.get(microServiceName).add(new InfoLogger(serviceName, microServiceName, type, message, timestamp));
            printingMap.get(serviceName).add(new InfoLogger(serviceName, microServiceName, type, message, timestamp));
        }


    }


    void printServicesBySeverity(){

        List<Map.Entry<String, Map<String, List<Log>>>> tmp = mapByService.entrySet()
                .stream()
                .sorted(Comparator.comparing(
                        entry -> entry.getValue().values()
                                .stream().flatMap(i -> i.stream())
                                .mapToInt(i -> i.calculateSeverity())
                                .average().orElse(0.0),
                        Comparator.reverseOrder()
                ))
                .collect(Collectors.toList());

        for (Map.Entry<String, Map<String, List<Log>>> map : tmp) {
            System.out.println(String.format("Service name: %s Count of microservices: %d Total logs in service: %d Average severity for all logs: %.2f Average number of logs per microservice: %.2f",
                    map.getKey(), countMicroservices(map.getValue()), totalLogsService(map.getValue()), avgSeverity(map.getValue()), avgNumLogsPerMicroservice(map.getValue())));
        }
    }

    private double avgNumLogsPerMicroservice(Map<String, List<Log>> map) {
        return 1.0 * totalLogsService(map) / countMicroservices(map);
    }

    private double avgSeverity(Map<String, List<Log>> map) {
        return map.values().stream().flatMap(Collection::stream).mapToInt(i -> i.calculateSeverity()).average().orElse(0.0);
    }

    private int totalLogsService(Map<String, List<Log>> map) {
        return map.values().stream().flatMap(Collection::stream).collect(Collectors.toList()).size();
    }

    private int countMicroservices(Map<String, List<Log>> map) {
        return map.size();
    }

    Map<Integer, Long> getSeverityDistribution (String service, String microservice){

        if(microservice == null || microservice.isEmpty()){
            List<Log> tmp = printingMap.get(service);
            return tmp.stream()
                    .collect(Collectors.groupingBy(
                            i -> i.calculateSeverity(),
                            Collectors.counting()
                    ));
        }
        else{
            return mapByService.get(service).get(microservice)
                    .stream()
                    .collect(Collectors.groupingBy(
                            i -> i.calculateSeverity(),
                            Collectors.counting()
                    ));
        }
    }

    Comparator<Log> comparator (String order){
        if(order.equals("NEWEST_FIRST")) {
            return Comparator.comparing(Log::getTimestamp).reversed();
        }
        else if (order.equals("OLDEST_FIRST")){
            return Comparator.comparing(Log::getTimestamp);
        }
        else if(order.equals("MOST_SEVERE_FIRST")){
            return Comparator.comparing(Log::calculateSeverity).thenComparing(Log::getTimestamp).reversed();
        }
        return Comparator.comparing(Log::calculateSeverity).thenComparing(Log::getTimestamp);
    }
    void displayLogs(String service, String microservice, String order) {
        List<Log> tmp;
        if (microservice == null || microservice.isEmpty()) {

            tmp = printingMap.get(service).stream().sorted(comparator(order)).collect(Collectors.toList());

        }
        else {
            tmp = mapByService.get(service).get(microservice).stream().sorted(comparator(order)).collect(Collectors.toList());
        }
        tmp.forEach(log ->
                System.out.println(String.format("%s|%s [%s] %s%d T:%d",
                        log.getService_name(), log.getMicroservice_name(), log.getType(),
                        log.getMessage(), log.getTimestamp(), log.getTimestamp())));
    }
}