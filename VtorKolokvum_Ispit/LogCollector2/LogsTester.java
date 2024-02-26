package VtorKolokvum_Ispit.LogCollector2;

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
    int timestamp;

    public Log(String service_name, String microservice_name, String type, String message, int timestamp) {
        this.service_name = service_name;
        this.microservice_name = microservice_name;
        this.type = type;
        this.message = message;
        this.timestamp = timestamp;
    }

    abstract int calculateSeverity();

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getMicroservice_name() {
        return microservice_name;
    }

    public void setMicroservice_name(String microservice_name) {
        this.microservice_name = microservice_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}

class InfoLog extends Log{

    public InfoLog(String service_name, String microservice_name, String type, String message, int timestamp) {
        super(service_name, microservice_name, type, message, timestamp);
    }

    @Override
    int calculateSeverity() {
        return 0;
    }
}

class WarnLog extends Log{

    public WarnLog(String service_name, String microservice_name, String type, String message, int timestamp) {
        super(service_name, microservice_name, type, message, timestamp);
    }

    @Override
    int calculateSeverity() {
        return message.contains("might cause error") ? 2 : 1;
    }
}

class ErrorLog extends Log{

    public ErrorLog(String service_name, String microservice_name, String type, String message, int timestamp) {
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
class Service{

    String name;
    Map<String, List<Log>> microserviceMap;

    public Service(String name) {
        this.name = name;
        microserviceMap = new HashMap<>();
    }

    @Override
    public String toString() {
        return String.format("Service name: %s Count of microservices: %d Total logs in service: %d Average severity for all logs: %.2f Average number of logs per microservice: %.2f",
                name, microServiceCount(), totalLogs(), avgSeverity(), avgNumberLogsPerMicroservice());
    }


    private int microServiceCount() {
        return microserviceMap.size();
    }


    public int totalLogs(){
        return microserviceMap.values().stream().mapToInt(List::size).sum();
    }
    public double avgNumberLogsPerMicroservice() {
        return 1.0 * totalLogs() / microServiceCount();
    }

    public double avgSeverity() {
        return microserviceMap.values()
                .stream()
                .flatMap(i -> i.stream())
                .mapToInt(i -> i.calculateSeverity())
                .average().orElse(0.0);
    }

    public void printDisplay() {

        microserviceMap.values()
                .stream()
                .flatMap(i -> i.stream())
                .forEach(log -> {
                    System.out.println(String.format("%s|%s [%s] %s%d T:%d",
                            log.service_name,
                            log.microservice_name,
                            log.type,
                            log.message,
                            log.timestamp,
                            log.timestamp));
                });

    }

}

class LogCollector {

    Map<String, Service> serviceMap;

    public LogCollector() {
        serviceMap = new HashMap<>();
    }

    void addLog (String log){
        String [] parts = log.split("\\s+");
        String serviceName = parts[0];
        String microServiceName = parts[1];
        String type = parts[2];
        StringBuilder sb = new StringBuilder();
        for(int i = 3; i<parts.length-1; i++){
            sb.append(parts[i]).append(' ');
        }
        int timestamp = Integer.parseInt(parts[parts.length-1]);

        Log logType = null;
        if(type.equals("INFO")){
            logType = new InfoLog(serviceName, microServiceName, type, sb.toString(), timestamp);
        }
        if (type.equals("WARN")){
            logType = new WarnLog(serviceName, microServiceName, type, sb.toString(), timestamp);
        }
        if (type.equals("ERROR")){
            logType = new ErrorLog(serviceName, microServiceName, type, sb.toString(), timestamp);
        }

        serviceMap.putIfAbsent(serviceName, new Service(serviceName));
        serviceMap.get(serviceName).microserviceMap.putIfAbsent(microServiceName, new ArrayList<>());
        serviceMap.get(serviceName).microserviceMap.get(microServiceName).add(logType);

    }

    void printServicesBySeverity(){
        List<Service> tmp = serviceMap
                .values()
                .stream()
                .sorted(Comparator.comparing(Service::avgSeverity).reversed())
                .collect(Collectors.toList());

        tmp.forEach(System.out::println);
    }

    Map<Integer, Long> getSeverityDistribution (String service, String microservice) {
        List<Log> micros = getService(service, microservice);

        Map<Integer, Long> a = micros.stream().collect(Collectors.groupingBy(
                i -> i.calculateSeverity(),
                Collectors.counting()
        ));
        return a;
    }

    private List<Log> getService(String service, String microservice) {
        Service service1 = serviceMap.get(service);

        List<Log> micros;
        if (microservice != null) {
            micros = service1.microserviceMap.get(microservice);
        } else {
            micros = service1.microserviceMap.values()
                    .stream().flatMap(i -> i.stream())
                    .collect(Collectors.toList());
        }
        return micros;
    }

    Comparator<Log> comparator (String order){
        Comparator<Log> comparator;
        if(order.equals("NEWEST_FIRST")){
            comparator = Comparator.comparing(Log::getTimestamp).reversed();
        }
        else if(order.equals("OLDEST_FIRST")){
            comparator = Comparator.comparing(Log::getTimestamp);
        }
        else if (order.equals("MOST_SEVERE_FIRST")){
            comparator = Comparator.comparing(Log::calculateSeverity).thenComparing(Log::getTimestamp).reversed();
        }
        else{
            comparator = Comparator.comparing(Log::calculateSeverity).thenComparing(Log::getTimestamp);
        }
        return comparator;
    }

    void displayLogs(String service, String microservice, String order) {
        List<Log> logs = getService(service, microservice);

        logs = logs.stream().sorted(comparator(order)).collect(Collectors.toList());
        Map<String, Service> newMap = new HashMap<>();
        newMap.put(service, new Service(service));
        newMap.get(service).microserviceMap.put(microservice, logs);
        newMap.values().forEach(Service::printDisplay);
    }
}
