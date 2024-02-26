package VtorKolokvum_Ispit.TaskManager;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TasksManagerTest {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        System.out.println("Tasks reading");
        manager.readTasks(System.in);
        System.out.println("By categories with priority");
        manager.printTasks(System.out, true, true);
        System.out.println("-------------------------");
        System.out.println("By categories without priority");
        manager.printTasks(System.out, false, true);
        System.out.println("-------------------------");
        System.out.println("All tasks without priority");
        manager.printTasks(System.out, false, false);
        System.out.println("-------------------------");
        System.out.println("All tasks with priority");
        manager.printTasks(System.out, true, false);
        System.out.println("-------------------------");

    }
}
class DeadlineNotValidException extends Exception{
    public DeadlineNotValidException(LocalDateTime deadline) {
        super(String.format("The deadline %s has already passed", deadline));
    }
}

interface ITask{

    String getCategory();
    String getName();
    String getDescription();
    LocalDateTime getDeadline();
    int getPriority();
}

class SimpleTask implements ITask{
    String category;
    String name;
    String description;

    public SimpleTask(String category, String name, String description) {
        this.category = category;
        this.name = name;
        this.description = description;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public LocalDateTime getDeadline() {
        return LocalDateTime.MAX;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

abstract class TaskDecorator implements ITask{
    ITask iTask;

    public TaskDecorator(ITask iTask) {
        this.iTask = iTask;
    }
}

class PriorityTaskDecorator extends TaskDecorator{
    int priority;

    public PriorityTaskDecorator(ITask iTask, int priority) {
        super(iTask);
        this.priority = priority;
    }

    @Override
    public String getCategory() {
        return iTask.getCategory();
    }

    @Override
    public String getName() {
        return iTask.getName();
    }

    @Override
    public String getDescription() {
        return iTask.getDescription();
    }

    @Override
    public LocalDateTime getDeadline() {
        return iTask.getDeadline();
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
//        TODO: proveri shto kje se sluci ako nemame 0, .length()-1
// provereno: pecati so }, a nas ne ni treba toa
        StringBuilder sb = new StringBuilder();
        sb.append(iTask.toString(), 0, iTask.toString().length()-1);
        // sb.append(iTask.toString());
        sb.append(", priority=" + priority + '}');
        return sb.toString();
    }
}

class TimeTaskDecorator extends TaskDecorator{
    LocalDateTime deadline;

    public TimeTaskDecorator(ITask iTask, LocalDateTime deadline) {
        super(iTask);
        this.deadline = deadline;
    }

    @Override
    public String getCategory() {
        return iTask.getCategory();
    }

    @Override
    public String getName() {
        return iTask.getName();
    }

    @Override
    public String getDescription() {
        return iTask.getDescription();
    }

    @Override
    public LocalDateTime getDeadline() {
        return deadline;
    }

    @Override
    public int getPriority() {
        return iTask.getPriority();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(iTask.toString(), 0, iTask.toString().length()-1);
        sb.append(", deadline=" + deadline + '}');
        return sb.toString();
    }
}

class TaskFactory {

    public static ITask createTask(String line) throws DeadlineNotValidException {
        String [] parts = line.split(",");
//        [категорија][име_на_задача],[oпис],[рок_за_задачата],[приоритет].
        String category = parts[0];
        String name = parts[1];
        String description = parts[2];
        SimpleTask base = new SimpleTask(category, name, description);
        if(parts.length == 3){
            return base;
        }
        else if (parts.length == 4){
            try{
                int priority = Integer.parseInt(parts[3]);
                return new PriorityTaskDecorator(base, priority);
            }
            catch (Exception e ){
                LocalDateTime deadline = LocalDateTime.parse(parts[3]);
                checkDeadline(deadline);
                return new TimeTaskDecorator(base, deadline);
            }
        }
        else {
            LocalDateTime deadline = LocalDateTime.parse(parts[3]);
            checkDeadline(deadline);
            int priority = Integer.parseInt(parts[4]);
            return new PriorityTaskDecorator(new TimeTaskDecorator(base, deadline), priority);
        }
    }

    private static void checkDeadline(LocalDateTime deadline) throws DeadlineNotValidException {
        if(deadline.isBefore(LocalDateTime.of(2020, 06, 02, 0, 0))){
            throw new DeadlineNotValidException(deadline);
        }
    }
}
class TaskManager {

    List<ITask> tasks;

    public TaskManager() {
        tasks = new ArrayList<>();
    }

    public void readTasks(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        tasks = reader.lines()
                .map(line -> {
                    try {
                        return TaskFactory.createTask(line);
                    } catch (DeadlineNotValidException e) {
                        System.out.println(e.getMessage());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
//        System.out.println(tasks);
    }


    public void printTasks(PrintStream out, boolean includePriority, boolean includeCategory) {
        PrintWriter writer = new PrintWriter(out);

        Comparator<ITask> priorityTrue = Comparator.comparing(ITask::getPriority).thenComparing(i -> Duration.between(LocalDateTime.now(), i.getDeadline()));
        Comparator<ITask> priorityFalse = Comparator.comparing(i -> Duration.between(LocalDateTime.now(), i.getDeadline()));

        if(includeCategory){
            TreeMap<String, List<ITask>> map = tasks.stream().collect(Collectors.groupingBy(
                    i -> i.getCategory(),
                    TreeMap::new,
                    Collectors.collectingAndThen(
                            Collectors.toList(),
                            iTasks -> iTasks.stream().sorted(includePriority ? priorityTrue : priorityFalse).collect(Collectors.toList())
                    )
            ));
//            System.out.println(map);
            map.entrySet().forEach(entry -> {
                writer.println(entry.getKey().toUpperCase());
                entry.getValue()
                        .forEach(writer::println);
            });
        }
        else{
            tasks = tasks.stream().sorted(includePriority ? priorityTrue : priorityFalse).collect(Collectors.toList());

            tasks.forEach(i -> writer.println(i));
        }

        writer.flush();
    }
}