package mizdooni.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.cglib.core.Local;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class UserTest {
    private User client;
    private Address address;

    @BeforeEach
    void setup() {
        address=new Address("Iran", "Tehran", "Khoone Ali");
        client=new User("ali", "ali1122", "ali@ali.com", address, User.Role.client);
    }

    @Test
    public void checkPasswordVerifiesPasswordCorrectly() {
        Assertions.assertTrue(client.checkPassword("ali1122"));
        Assertions.assertFalse(client.checkPassword("ali112"));
    }

    @Test
    public void userCanReserveValid() {
        User manager=new User("akbar", "akbar1234", "akbar@akbar.com", address, User.Role.manager);
        Restaurant restaurant=new Restaurant("akbar juje", manager, "kababi", LocalTime.now().minusHours(2),
                                            LocalTime.now().plusHours(2), "khoobe", address, "akbar-juje.png");
        Table table1=new Table(1, restaurant.getId(), 4);
        restaurant.addTable(table1);
        Reservation res1=new Reservation(client, restaurant, table1, LocalDateTime.now().minusHours(1)); //TODO: check equal
        client.addReservation(res1);

        System.out.println(client.getReservations());
        System.out.println(res1.getUser());
        Assertions.assertTrue(client.checkReserved(restaurant));
    }
}
