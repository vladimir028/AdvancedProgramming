package PrvKolokvium.Vesti;

import java.util.*;
import java.util.stream.Collectors;

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


// Vasiot kod ovde
abstract  class NewsItem{
    String title;
    Date date;
    Category category;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public NewsItem(String title, Date date, Category category) {
        this.title = title;
        this.date = date;
        this.category = category;
    }

    public int when() {
        Date now = new Date();
        long ms = now.getTime() - date.getTime();
        return (int) (ms / (1000*60));
    }

    abstract String getTeaser();
}

class TextNewsItem extends NewsItem{
    String text;

    public TextNewsItem(String title, Date date, Category category, String text) {
        super(title, date, category);
        this.text = text;
    }

    @Override
    String getTeaser() {
//        Bраќа String составен од насловот на веста, пред колку минути е
//        објавена веста (цел број минути) и максимум 80 знаци од содржината на веста, сите одделени со нов ред.

        return text.length() < 80 ? String.format("%s\n%d\n%s\n", title,  when(), text) : String.format("%s\n%d\n%s\n", title, when(), text.substring(0,80));
    }
}



class MediaNewsItem extends NewsItem{
    String url;
    int views;

    public MediaNewsItem(String title, Date date, Category category, String url, int views) {
        super(title, date, category);
        this.url = url;
        this.views = views;
    }

    @Override
    String getTeaser() {
//        враќа String составен од насловот на веста, пред колку
//        минути е објавена веста (цел број минути), url-то на веста и бројот на погледи, сите одделени со нов ред.
        return String.format("%s\n%d\n%s\n%d\n", title, when(), url, views);
    }
}

class FrontPage{
    List<NewsItem> news;
    List<Category> categories;

    public FrontPage(Category[] cat) {
        this.news = new ArrayList<>();
        this.categories = List.of(cat);
//        for (Category category : cat) {
//            categories.add(category);
//        }
    }

    void addNewsItem(NewsItem newsItem){
        news.add(newsItem);
    }

    List<NewsItem> listByCategory(Category category){
        return news.stream().filter(i -> i.getCategory().equals(category)).collect(Collectors.toList());
    }

    List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
        List<NewsItem> tmp = news.stream().filter(i -> i.getCategory().getName().equals(category)).collect(Collectors.toList());
        List<Category> test = categories.stream().filter(i -> i.getName().equals(category)).collect(Collectors.toList());
        if(test.isEmpty()){
            throw new CategoryNotFoundException(category);
        }
        return tmp;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (NewsItem newsItem : news) {
            stringBuilder.append(newsItem.getTeaser());
        }
        return stringBuilder.toString();
    }
}

class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException(String message) {
        super(String.format("Category %s was not found", message));
    }
}
class Category{
    String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}