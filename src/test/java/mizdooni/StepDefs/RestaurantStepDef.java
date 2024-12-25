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

    @Given("some clients submitted reviews")
    public void some_clients_submit_reviews() {
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

    @When("the reviews are valid")
    public void the_client_makes_a_valid_reservation() {
        for (Review review:reviews)
            restaurant.addReview(review);
    }

    @Then("average should be correct")
    public void the_reservation_should_be_added() {
        Assertions.assertEquals(2.4, restaurant.getAverageRating().overall, "Overall Rating Correct");
        Assertions.assertEquals(2.4, restaurant.getAverageRating().food, "Food Rating Correct");
        Assertions.assertEquals(3.0, restaurant.getAverageRating().ambiance, "Ambiance Rating Correct");
        Assertions.assertEquals(1.5, restaurant.getAverageRating().service, "Service Rating Correct");
    }
}
