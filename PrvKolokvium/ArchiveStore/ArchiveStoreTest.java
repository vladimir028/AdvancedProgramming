package PrvKolokvium.ArchiveStore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


abstract class Archive{
    private int id;
    private LocalDate dateArchived;

    public Archive(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    abstract public String open(LocalDate date);

    public void archive(LocalDate date){
        dateArchived=date;
    }


    public LocalDate getDateArchived() {
        return dateArchived;
    }
}
class LockedArchive extends Archive{
    private LocalDate dateOpen;

    public LockedArchive(int id,LocalDate dateOpen) {
        super(id);
        this.dateOpen = dateOpen;
    }

    @Override
    public String open( LocalDate date) {
        if(date.isBefore(dateOpen)){
            return String.format("Item %d cannot be opened before %s\n",getId(),dateOpen.toString());
        }

        return String.format("Item %d opened at %s\n",getId(),date.toString());
    }
}
class SpecialArchive extends Archive{
    private int maxOpen;
    private int countOpen;

    public SpecialArchive(int id,int maxOpen) {
        super(id);
        this.maxOpen=maxOpen;
        countOpen=0;
    }

    @Override
    public String open( LocalDate date) {
        ++countOpen;
        if(countOpen>maxOpen){
            return String.format("Item %d cannot be opened more than %d times\n",getId(),maxOpen);
        }
        return String.format("Item %d opened at %s\n",getId(),date.toString());
    }
}

class ArchiveStore{

    StringBuilder log;

    List<Archive> archiveList;
    public ArchiveStore() {
        this.archiveList=new ArrayList<>();
        log=new StringBuilder();
    }
    void archiveItem(Archive item, LocalDate date){
        item.archive(date);
        archiveList.add(item);
        log.append(String.format("Item %d archived at %s\n",item.getId(),date.toString()));

    }
    void openItem(int id, LocalDate date) throws NonExistingItemException {
        Optional<Archive> archive = archiveList.stream().filter(i -> i.getId() == id).findFirst();
        if(archive.isEmpty()){
            throw new NonExistingItemException(id);
        }
        log.append(archive.get().open(date));
    }
    String getLog(){
        return log.toString();
    }
}

class NonExistingItemException extends Exception{
    public NonExistingItemException(int id) {
        super(String.format("Item with id %d doesn't exist",id));
    }
}
public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}