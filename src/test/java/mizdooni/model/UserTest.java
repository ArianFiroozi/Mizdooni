package mizdooni.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Arrays;

public class UserTest {
    private User client;
    private Restaurant restaurant;
    private List<Reservation> validReservations, invalidReservations;

    @BeforeEach
    void setup() {
        Address address = new Address("Iran", "Tehran", "Khoone Ali");
        client=new User("ali", "ali1122", "ali@ali.com", address, User.Role.client);
        User manager = new User("akbar", "akbar1234", "akbar@akbar.com", address, User.Role.manager);
        restaurant=new Restaurant("akbar juje", manager, "kababi", LocalTime.now().minusHours(2),
                                    LocalTime.now().plusHours(2), "khoobe", address, "akbar-juje.png");

        Table table = new Table(1, restaurant.getId(), 4);
        restaurant.addTable(table);

        validReservations=Arrays.asList(
                        new Reservation(client, restaurant, table, LocalDateTime.now().minusHours(1)),
                        new Reservation(client, restaurant, table, LocalDateTime.now().minusHours(3)),
                        new Reservation(client, restaurant, table, LocalDateTime.now().minusHours(4))
                        );
        invalidReservations=Arrays.asList(
                        new Reservation(client, restaurant, table, LocalDateTime.now().plusHours(1)),
                        new Reservation(client, restaurant, table, LocalDateTime.now().plusHours(19))
                        );
    }

    @Test
    public void checkPassword_ValidPassword_OnlyVerifiesCorrectPassword() {
        Assertions.assertTrue(client.checkPassword("ali1122"));
        Assertions.assertFalse(client.checkPassword("ali112"));
    }

    @Test
    public void checkReserved_ValidReservation_returnsTrue() {
        client.addReservation(validReservations.getFirst());

        Assertions.assertTrue(client.checkReserved(restaurant));
    }

    @Test
    public void checkReserved_ReservationInFuture_returnsFalse() {
        client.addReservation(invalidReservations.getFirst());

        Assertions.assertFalse(client.checkReserved(restaurant));
    }

    @Test
    public void checkReserved_CancelledReservation_returnsFalse() {
        client.addReservation(validReservations.getFirst());
        validReservations.getFirst().cancel();

        Assertions.assertFalse(client.checkReserved(restaurant));
    }

    @Test
    public void getReservation_ValidReservations_OnlyReturnsActiveReservations() {
        for (Reservation res : validReservations)
            client.addReservation(res);
        for (Reservation res : invalidReservations)
            client.addReservation(res);
        validReservations.getFirst().cancel();

        Assertions.assertNull(client.getReservation(validReservations.getFirst().getReservationNumber()));
        Assertions.assertNotNull(client.getReservation(validReservations.getLast().getReservationNumber()));
    }
}
