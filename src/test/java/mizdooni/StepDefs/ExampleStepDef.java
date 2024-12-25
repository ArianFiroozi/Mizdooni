package mizdooni.StepDefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.spring.CucumberContextConfiguration;
import mizdooni.model.*;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@CucumberContextConfiguration
@SpringBootTest()
public class ExampleStepDef {

    private User client;
    private Restaurant restaurant;
    private Table table;
    private Reservation reservationToAdd;
    private boolean isAdded;

    @Given("a client wants to make a reservation")
    public void a_client_wants_to_make_a_reservation() {
        Address address = new Address("Iran", "Tehran", "Khoone Ali");
        client = new User("ali", "ali1122", "ali@ali.com", address, User.Role.client);
        User manager = new User("akbar", "akbar1234", "akbar@akbar.com", address, User.Role.manager);

        restaurant = new Restaurant("akbar juje", manager, "kababi", LocalTime.now().minusHours(2),
                LocalTime.now().plusHours(2), "khoobe", address, "akbar-juje.png");

        table = new Table(1, restaurant.getId(), 4);
        restaurant.addTable(table);
    }

    @When("the client makes a valid reservation")
    public void the_client_makes_a_valid_reservation() {
        reservationToAdd = new Reservation(client, restaurant, table, LocalDateTime.now().minusHours(1));
        client.addReservation(reservationToAdd);
        isAdded = client.checkReserved(restaurant);
    }

    @Then("the reservation should be added")
    public void the_reservation_should_be_added() {
        assertTrue(isAdded, "The reservation was not added.");
    }

    @When("the client makes an invalid reservation")
    public void the_client_makes_an_invalid_reservation() {
        reservationToAdd = new Reservation(client, restaurant, table, LocalDateTime.now().plusHours(19));
        try {
            client.addReservation(reservationToAdd);
            isAdded = client.checkReserved(restaurant);
        } catch (Exception e) {
            isAdded = false;
        }
    }

    @Then("the reservation should not be added")
    public void the_reservation_should_not_be_added() {
        assertFalse(isAdded, "An invalid reservation was added.");
    }
}
