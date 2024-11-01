package mizdooni.controllers;

import static mizdooni.controllers.ControllerUtils.DATETIME_FORMATTER;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import mizdooni.model.Reservation;
import mizdooni.model.Restaurant;
import mizdooni.response.Response;
import mizdooni.response.ResponseException;
import mizdooni.service.ReservationService;
import mizdooni.service.RestaurantService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationControllerTest {

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private ReservationService reservationService;

    @Mock
    private Restaurant restaurant;

    @Mock
    private Reservation reservation;

    @InjectMocks
    private ReservationController reservationController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getReservations_validParams_returnsResponse() {
        when(restaurantService.getRestaurant(1)).thenReturn(restaurant);

        Response response = reservationController.getReservations(1, 1, "2023-10-25");
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus().value());
        Assertions.assertEquals("restaurant table reservations", response.getMessage());
    }

    @Test
    void getReservations_nullRestaurant_throwsException() {
        when(restaurantService.getRestaurant(1)).thenReturn(null);
        Assertions.assertThrows(ResponseException.class, ()->{reservationController.getReservations(1, 1, "2023-10-25");});
    }

    @Test
    void getReservations_badDate_throwsException() {
        when(restaurantService.getRestaurant(1)).thenReturn(restaurant);
        Assertions.assertThrows(ResponseException.class, ()->{reservationController.getReservations(1, 1, "2023-0-25");});
    }

    @Test
    void getReservations_nullDate_returnsResponse() {
        when(restaurantService.getRestaurant(1)).thenReturn(restaurant);
        Assertions.assertInstanceOf(Response.class, reservationController.getReservations(1, 1, null));
    }

    @Test
    void getReservations_reservationServiceThrowsException_throwsException() {
        when(restaurantService.getRestaurant(1)).thenReturn(null);
        try {
            LocalDate date=LocalDate.parse("2023-10-25");
            when(reservationService.getReservations(1, 1, date)).thenThrow(ResponseException.class);
        }
        catch (Exception e) {
            fail();
        }
        Assertions.assertThrows(ResponseException.class, ()->{reservationController.getReservations(1, 1, "2023-10-25");});
    }

    @Test
    void getCustomerReservations_validId_returnsResponse() {
        try {
            List<Reservation> reservations = Collections.singletonList(reservation);
            when(reservationService.getCustomerReservations(1)).thenReturn(reservations);
        }
        catch (Exception e) {
            fail();
        }

        Response response = reservationController.getCustomerReservations(1);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus().value());
        Assertions.assertEquals("user reservations", response.getMessage());
    }

    @Test
    void getCustomerReservations_invalidId_throwsException() {
        try {
            when(reservationService.getCustomerReservations(1)).thenThrow(ResponseException.class);
        }
        catch (Exception e) {
            fail();
        }
        Assertions.assertThrows(ResponseException.class, ()->{reservationController.getCustomerReservations(1);});
    }

    @Test
    void getAvailableTimes_validParams_returnsResponse() {
        when(restaurantService.getRestaurant(1)).thenReturn(restaurant);
        LocalDate date=LocalDate.parse("2023-10-25");
        List<LocalTime> availableTimes = Collections.singletonList(LocalTime.parse("10:01"));

        try {
            when(reservationService.getAvailableTimes(1, 1, date)).thenReturn(availableTimes);
        }
        catch (Exception e) {
            fail();
        }

        Response response = reservationController.getAvailableTimes(1, 1, date.toString());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus().value());
        Assertions.assertEquals("available times", response.getMessage());
    }


    @Test
    void getAvailableTimes_invalidRestaurant_throwsException() {
        when(restaurantService.getRestaurant(1)).thenReturn(null);
        LocalDate date=LocalDate.parse("2023-10-25");
        List<LocalTime> availableTimes = Collections.singletonList(LocalTime.parse("10:01"));

        try {
            when(reservationService.getAvailableTimes(1, 1, date)).thenReturn(availableTimes);
        }
        catch (Exception e) {
            fail();
        }

        Assertions.assertThrows(ResponseException.class, ()->reservationController.getAvailableTimes(1, 1, date.toString()));
    }

    @Test
    void getAvailableTimes_invalidDate_throwsException() {
        when(restaurantService.getRestaurant(1)).thenReturn(restaurant);
        LocalDate date=LocalDate.parse("2023-12-25");
        List<LocalTime> availableTimes = Collections.singletonList(LocalTime.parse("10:01"));

        try {
            when(reservationService.getAvailableTimes(1, 1, date)).thenReturn(availableTimes);
        }
        catch (Exception e) {
            fail();
        }

        Assertions.assertThrows(ResponseException.class, ()->reservationController.getAvailableTimes(1, 1, "2023-13-25"));
    }

    @Test
    void getAvailableTimes_reservationServiceThrowsException_throwsException() {
        when(restaurantService.getRestaurant(1)).thenReturn(restaurant);
        LocalDate date=LocalDate.parse("2023-12-25");

        try {
            when(reservationService.getAvailableTimes(1, 1, date)).thenThrow(ResponseException.class);
        }
        catch (Exception e) {
            fail();
        }

        Assertions.assertThrows(ResponseException.class, ()->reservationController.getAvailableTimes(1, 1, date.toString()));
    }

    @Test
    void addReservation_validParams_returnsResponse() {
        Map<String, String> params = new HashMap<>();

        LocalDateTime dateTime = LocalDateTime.parse("2023-10-15 10:01", DATETIME_FORMATTER);
        params.put("people", "10000");
        params.put("datetime", "2023-10-15 10:01");
        when(restaurantService.getRestaurant(1)).thenReturn(restaurant);

        try {
            when(reservationService.reserveTable(1, 10000, dateTime)).thenReturn(reservation);
        }
        catch (Exception e) {
            fail();
        }

        Response response = reservationController.addReservation(1, params);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus().value());
        Assertions.assertEquals("reservation done", response.getMessage());
    }

    @Test
    void addReservation_checkRestaurantsThrowsException_throwsException() {
        Map<String, String> params = new HashMap<>();

        LocalDateTime dateTime = LocalDateTime.parse("2023-10-15 10:01", DATETIME_FORMATTER);
        params.put("people", "10000");
        params.put("datetime", "2023-10-15 10:01");
        when(restaurantService.getRestaurant(1)).thenThrow(ResponseException.class);

        try {
            when(reservationService.reserveTable(1, 10000, dateTime)).thenReturn(reservation);
        }
        catch (Exception e) {
            fail();
        }
        Assertions.assertThrows(ResponseException.class, ()->reservationController.addReservation(1, params));
    }

    @Test
    void addReservation_insufficientKeys_throwsException() {
        Map<String, String> params = new HashMap<>();

        LocalDateTime dateTime = LocalDateTime.parse("2023-10-15 10:01", DATETIME_FORMATTER);
        params.put("datetime", "2023-10-15 10:01");
        when(restaurantService.getRestaurant(1)).thenReturn(restaurant);

        try {
            when(reservationService.reserveTable(1, 10000, dateTime)).thenReturn(reservation);
        }
        catch (Exception e) {
            fail();
        }
        Assertions.assertThrows(ResponseException.class, ()->reservationController.addReservation(1, params));
    }


    @Test
    void addReservation_badDateTimeParam_throwsException() {
        Map<String, String> params = new HashMap<>();

        LocalDateTime dateTime = LocalDateTime.parse("2023-10-15 10:01", DATETIME_FORMATTER);
        params.put("people", "10000");
        params.put("datetime", "2023-20-15 10:01");
        when(restaurantService.getRestaurant(1)).thenReturn(restaurant);

        try {
            when(reservationService.reserveTable(1, 10000, dateTime)).thenReturn(reservation);
        }
        catch (Exception e) {
            fail();
        }
        Assertions.assertThrows(ResponseException.class, ()->reservationController.addReservation(1, params));
    }

    @Test
    void addReservation_reservationServiceThrowsException_throwsException() {
        Map<String, String> params = new HashMap<>();

        LocalDateTime dateTime = LocalDateTime.parse("2023-10-15 10:01", DATETIME_FORMATTER);
        params.put("people", "10000");
        params.put("datetime", "2023-10-15 10:01");
        when(restaurantService.getRestaurant(1)).thenReturn(restaurant);

        try {
            when(reservationService.reserveTable(1, 10000, dateTime)).thenThrow(ResponseException.class);
        }
        catch (Exception e) {
            fail();
        }
        Assertions.assertThrows(ResponseException.class, ()->reservationController.addReservation(1, params));
    }

    @Test
    void cancelReservation_reservationServiceOK_returnsResponse() {
        Response response = reservationController.cancelReservation(1);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus().value());
        Assertions.assertEquals("reservation cancelled", response.getMessage());
    }

    @Test
    void cancelReservation_reservationServiceThrowsException_throwsException() {
        when(reservationController.cancelReservation(1)).thenThrow(ResponseException.class);
        Assertions.assertThrows(ResponseException.class, ()->reservationController.cancelReservation(1));
    }
}
