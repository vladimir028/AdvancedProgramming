package VtorKolokvum_Ispit.MoviesList;

import java.util.*;
import java.util.stream.Collectors;

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde
class Movie{
    String title;
    List<Integer> ratings;
    int max_size;

    public Movie(String title, List<Integer> ratings) {
        this.title = title;
        this.ratings = ratings;
        max_size = 1;
    }

    public int getSize(){
        return ratings.size();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getRatings() {
        return ratings;
    }

    public void setRatings(List<Integer> ratings) {
        this.ratings = ratings;
    }

    @Override
    public String toString() {
//        Story of Women (1989) (6.63) of 8 ratings
        return String.format("%s (%.2f) of %d ratings", title, avgRating(), ratings.size());
    }

    public double avgRating() {
        return ratings.stream().mapToDouble(i -> i).average().orElse(0.0);
    }

    public void addMaxRatingMovie(int size) {
        this.max_size = size;
    }

    public double calculateCoef(){
        return avgRating() * getSize() / max_size;
    }
}

class MoviesList {
    List<Movie> movies;

    public MoviesList() {
        movies = new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings){
        List<Integer> integers = new ArrayList<>();
        integers = Arrays.stream(ratings).boxed().collect(Collectors.toList());
        Movie movie = new Movie(title, integers);
        movies.add(movie);
    }

    public List<Movie> top10ByAvgRating(){
        return movies.stream().sorted(Comparator.comparing(Movie::avgRating).reversed().thenComparing(Movie::getTitle)).limit(10).collect(Collectors.toList());
    }

    public List<Movie> top10ByRatingCoef(){
        Movie maxRatingsMovie = movies.stream().max(Comparator.comparing(Movie::getSize)).get();
        movies.stream().forEach(movie -> movie.addMaxRatingMovie(maxRatingsMovie.getSize()));

        return movies.stream().sorted(Comparator.comparing(Movie::calculateCoef).reversed().thenComparing(Movie::getTitle)).limit(10).collect(Collectors.toList());
    }
}


// reshenie 2
// import java.util.*;
// import java.util.function.Function;
// import java.util.stream.Collectors;

// class Movie{
//     private String title;
//     List<Integer> ratings;

//     public Movie(String title, List<Integer> ratings) {
//         this.title = title;
//         this.ratings = ratings;
//     }

//     public String getTitle() {
//         return title;
//     }

//     public void setTitle(String title) {
//         this.title = title;
//     }

//     public List<Integer> getRatings() {
//         return ratings;
//     }

//     public void setRatings(List<Integer> ratings) {
//         this.ratings = ratings;
//     }

//     public double avgRating(){
//         return ratings.stream().mapToInt(i -> i).average().getAsDouble();
//     }

// //    public double avgCoef(int max){
// //        return avgRating() * ratings.size() / max;
// //    }

//     @Override
//     public String toString() {
// //        Story of Women (1989) (6.63) of 8 ratings
//         return String.format("%s (%.2f) of %d ratings", title, avgRating(), ratings.size());
//     }
// }
// class MoviesList{
//     List<Movie> movieList;
//     int maxSize;
//     public MoviesList() {
//         this.movieList = new ArrayList<>();
//         maxSize = 0;
//     }

//     public void addMovie(String title, int[] ratings){
//         List<Integer> ratingList = new ArrayList<>();
//         ratingList = Arrays.stream(ratings).boxed().collect(Collectors.toList());

//         movieList.add(new Movie(title, ratingList));
//     }

//     public Comparator<Movie> top10(){
//       return Comparator.comparing(Movie::avgRating).reversed().thenComparing(Movie::getTitle);
//     }

//     public int maxSize(){
//         return movieList.stream().map(i -> i.ratings.size()).max(Integer::compareTo).get();
// //        return movieList.stream().map(i -> i.ratings.size()).sorted(Comparator.reverseOrder()).collect(Collectors.toList()).get(0);
//     }

//     public List<Movie> top10ByAvgRating(){
//         List<Movie> tmp = movieList;
//         tmp = movieList.stream().sorted(top10()).limit(10).collect(Collectors.toList());
//         return tmp;
//     }

//     public List<Movie> top10ByRatingCoef(){

//         List<Movie> tmp = movieList;
//         tmp = tmp.stream().sorted(Comparator.comparing(this::calculateRatingCoef).reversed().thenComparing(Movie::getTitle)).limit(10).collect(Collectors.toList());
//         return tmp;
//     }

//     private double calculateRatingCoef(Movie movie){
//         return movie.avgRating() * movie.getRatings().size() / maxSize();
//     }


//     @Override
//     public String toString() {
//         StringBuilder sb = new StringBuilder();
//         for(Movie movie : movieList){
//             sb.append(movie.toString());
//         }
//         return sb.toString();
//     }
// }