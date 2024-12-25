package mizdooni.StepDefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import mizdooni.model.*;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest()
public class RestaurantStepDef {

    private Restaurant restaurant;
    private List<Review> reviews;

    private void setup() {
        Address address = new Address("Iran", "Tehran", "Khoone Ali");
        List<User> users = Arrays.asList(
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
    @Given("some clients submitted reviews")
    public void some_clients_submit_reviews() {
        setup();
    }

    @When("the reviews are valid")
    public void the_reviews_are_valid() {
        for (Review review:reviews)
            restaurant.addReview(review);
    }

    @Then("average should be correct")
    public void average_should_be_correct() {
        Assertions.assertEquals(2.4, restaurant.getAverageRating().overall, "Overall Rating Correct");
        Assertions.assertEquals(2.4, restaurant.getAverageRating().food, "Food Rating Correct");
        Assertions.assertEquals(3.0, restaurant.getAverageRating().ambiance, "Ambiance Rating Correct");
        Assertions.assertEquals(1.5, restaurant.getAverageRating().service, "Service Rating Correct");
    }

    @Given("review added for the first time by user")
    public void review_added_for_the_first_time_by_user() {
        setup();
    }

    @When("review is valid")
    public void review_is_valid() {
        restaurant.addReview(reviews.getFirst());
    }

    @Then("restaurant gets the review")
    public void restaurant_gets_the_review(){
        Assertions.assertTrue(restaurant.getReviews().contains(reviews.getFirst()), "Restaurant Got the Review");
    }

    @Given("user added review before")
    public void user_added_review_before() {
        setup();
        for (Review review:reviews)
            restaurant.addReview(review);
    }

    @When("user adds second review")
    public void user_adds_second_review() {
        restaurant.addReview(reviews.getFirst());
    }

    @Then("restaurant only keeps second review")
    public void restaurant_only_keeps_second_review(){
        for (var review: restaurant.getReviews())
            if (review.getUser()==reviews.getFirst().getUser())
                Assertions.assertEquals(reviews.getFirst(), review,
                        "Restaurant Keeps the Second Review Only");
    }
}
