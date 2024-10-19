package mizdooni.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class TableTest {
    private Table table;
    private User client;
    private Restaurant restaurant;

    @BeforeEach
    void setup() {
        table=new Table(1, 1, 4);
        Address address = new Address("Iran", "Tehran", "Khoone Ali");
        client=new User("ali", "ali1122", "ali@ali.com", address, User.Role.client);
        User manager = new User("akbar", "akbar1234", "akbar@akbar.com", address, User.Role.manager);
        restaurant=new Restaurant("akbar juje", manager, "kababi", LocalTime.now().minusHours(2),
                                    LocalTime.now().plusHours(2), "khoobe", address, "akbar-juje.png");

    }

    @Test
    void addReservation_ValidReservation_Correct() {
        Reservation reservation = new Reservation(client, restaurant, table, LocalDateTime.now().plusHours(2));
        table.addReservation(reservation);
        Assertions.assertTrue(table.getReservations().contains(reservation));
    }

    @Test
    void isReserved_ValidReservations_ReturnsCorrect() {
        table.addReservation(new Reservation(client, restaurant, table, LocalDateTime.now().plusHours(2)));

        Assertions.assertTrue(table.isReserved(LocalDateTime.now().plusHours(2)));
        Assertions.assertFalse(table.isReserved(LocalDateTime.now().plusHours(1)));
    }

    @Test
    void isReserved_CancelledReservation_ReturnsFalse() {
        Reservation reservation = new Reservation(client, restaurant, table, LocalDateTime.now().plusHours(2));
        table.addReservation(reservation);
        reservation.cancel();

        Assertions.assertFalse(table.isReserved(LocalDateTime.now().plusHours(2)));
    }
}
