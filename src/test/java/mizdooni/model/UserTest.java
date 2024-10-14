package mizdooni.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class UserTest {
    private User client;
    private Restaurant restaurant;
    private Table table;

    @BeforeEach
    void setup() {
        Address address = new Address("Iran", "Tehran", "Khoone Ali");
        client=new User("ali", "ali1122", "ali@ali.com", address, User.Role.client);

        User manager = new User("akbar", "akbar1234", "akbar@akbar.com", address, User.Role.manager);
        restaurant=new Restaurant("akbar juje", manager, "kababi", LocalTime.now().minusHours(2),
                LocalTime.now().plusHours(2), "khoobe", address, "akbar-juje.png");
        table=new Table(1, restaurant.getId(), 4);
        restaurant.addTable(table);
    }

    @Test
    public void checkPasswordVerifiesPasswordCorrectly() {
        Assertions.assertTrue(client.checkPassword("ali1122"));
        Assertions.assertFalse(client.checkPassword("ali112"));
    }

    @Test
    public void userCanCheckReserveValid() {
        Reservation res1=new Reservation(client, restaurant, table, LocalDateTime.now().minusHours(1));
        client.addReservation(res1);

        Assertions.assertTrue(client.checkReserved(restaurant));
    }

    @Test
    public void userCanNotCheckReserveInFuture() {
        Reservation res1=new Reservation(client, restaurant, table, LocalDateTime.now().plusHours(1));
        client.addReservation(res1);

        Assertions.assertFalse(client.checkReserved(restaurant));
    }

    @Test
    public void userCanNotCheckCancelledReserve() {
        Reservation res1=new Reservation(client, restaurant, table, LocalDateTime.now().minusHours(1));
        client.addReservation(res1);
        res1.cancel();

        Assertions.assertFalse(client.checkReserved(restaurant));
    }

    @Test
    public void userCanOnlyGetValidReserve() {
        Reservation res1=new Reservation(client, restaurant, table, LocalDateTime.now().minusHours(1));
        Reservation res2=new Reservation(client, restaurant, table, LocalDateTime.now().minusHours(3));
        Reservation res3=new Reservation(client, restaurant, table, LocalDateTime.now().minusHours(4));
        Reservation res4=new Reservation(client, restaurant, table, LocalDateTime.now().plusHours(1));
        client.addReservation(res1);
        client.addReservation(res2);
        client.addReservation(res3);
        client.addReservation(res4);
        res1.cancel();

        Assertions.assertNull(client.getReservation(res1.getReservationNumber()));
        Assertions.assertNotNull(client.getReservation(res2.getReservationNumber()));
    }
}
