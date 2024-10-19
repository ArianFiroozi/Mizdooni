package mizdooni.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class RestaurantTest {
    private List<Table> tables;
    private Restaurant restaurant;
    private List<User> users;
    private List<Review> reviews;

    @BeforeEach
    void setup() {
        tables=Arrays.asList(
                new Table(1, 1, 4),
                new Table(2, 1, 2),
                new Table(3, 1, 14)
                );

        Address address = new Address("Iran", "Tehran", "Khoone Ali");
        users=Arrays.asList(
                new User("ali", "ali1122", "ali@ali.com", address, User.Role.client),
                new User("maryam", "ali1122", "ali@ali.com", address, User.Role.client),
                new User("fateme", "ali1122", "ali@ali.com", address, User.Role.client)
                );

        User manager=new User("akbar", "akbar1234", "akbar@akbar.com", address, User.Role.manager);
        restaurant=new Restaurant("akbar juje", manager, "kababi", LocalTime.now().minusHours(2),
                LocalTime.now().plusHours(2), "khoobe", address, "akbar-juje.png");

        Rating badRating=new Rating();
        badRating.ambiance=2.3;
        badRating.food=1.6;
        badRating.service=0.5;
        badRating.overall=1.2;

        Rating goodRating=new Rating();
        goodRating.ambiance=4.4;
        goodRating.food=4.0;
        goodRating.service=3.5;
        goodRating.overall=4.8;

        reviews=Arrays.asList(
                new Review(users.get(0), goodRating, "alaki", LocalDateTime.now()),
                new Review(users.get(0), badRating, "second review", LocalDateTime.now()),
                new Review(users.get(1), badRating, "alaki", LocalDateTime.now()),
                new Review(users.get(2), goodRating, "alaki", LocalDateTime.now())
                );
    }

    @Test
    void getTable_ValidTableNumber_ReturnsCorrectTable() {
        for (Table table:tables)
            restaurant.addTable(table);
        Assertions.assertEquals(tables.getFirst(), restaurant.getTable(1));
    }

    @Test
    void addReview_DuplicatedUser_RemovesPreviousReview() {
        for (Review review:reviews)
            restaurant.addReview(review);
        int reviewCountBefore=restaurant.getReviews().size();
        restaurant.addReview(reviews.getFirst());
        Assertions.assertEquals(reviewCountBefore, restaurant.getReviews().size());

        for (var review: restaurant.getReviews())
            if (review.getUser()==reviews.getFirst().getUser())
                Assertions.assertEquals(reviews.getFirst(), review);
    }

    @Test
    void averageRating_ValidReviews_CalculatesCorrectly() {
        for (Review review:reviews)
            restaurant.addReview(review);
        Assertions.assertEquals(2.4, restaurant.getAverageRating().overall);
        Assertions.assertEquals(2.4, restaurant.getAverageRating().food);
        Assertions.assertEquals(3.0, restaurant.getAverageRating().ambiance);
        Assertions.assertEquals(1.5, restaurant.getAverageRating().service);
    }

    @Test
    void maxSeats_ValidTables_Correct() {
        for (Table table:tables)
            restaurant.addTable(table);
        Assertions.assertEquals(14, restaurant.getMaxSeatsNumber());
    }

    @Test
    void maxSeats_NoTables_ReturnsZero() {
        Assertions.assertEquals(0, restaurant.getMaxSeatsNumber());
    }
}
