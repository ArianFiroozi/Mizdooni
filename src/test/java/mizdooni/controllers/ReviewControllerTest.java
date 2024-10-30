package mizdooni.controllers;

import mizdooni.exceptions.*;
import mizdooni.model.Rating;
import mizdooni.model.Restaurant;
import mizdooni.model.Review;
import mizdooni.response.PagedList;
import mizdooni.response.Response;
import mizdooni.response.ResponseException;
import mizdooni.service.RestaurantService;
import mizdooni.service.ReviewService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static mizdooni.controllers.ControllerUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ReviewControllerTest {

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private Restaurant restaurant;

    @Mock
    private Review review;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getReviews_validParams_returnsResponse() {
        when(restaurantService.getRestaurant(1)).thenReturn(restaurant);
        Assertions.assertInstanceOf(Response.class, reviewController.getReviews(1, 1));
    }

    @Test
    void getReviews_nullRestaurant_throwsException() {
        when(restaurantService.getRestaurant(1)).thenReturn(null);
        Assertions.assertThrows(ResponseException.class, ()->{reviewController.getReviews(1, 1);});
    }

    @Test
    void getReviews_wrongRestaurant_throwsException() {
        when(restaurantService.getRestaurant(1)).thenReturn(restaurant);
        Assertions.assertThrows(ResponseException.class, ()->{reviewController.getReviews(2, 1);});
    }

    @Test
    void getReviews_reservationServiceThrowsException_throwsException() {
        when(restaurantService.getRestaurant(1)).thenReturn(null);
        try {
            when(reviewService.getReviews(1, 1)).thenThrow(ResponseException.class);
        }
        catch (Exception e) {
            fail();
        }
        Assertions.assertThrows(ResponseException.class, ()->{reviewController.getReviews(1, 1);});
    }

    @Test
    void addReview_validParams_returnsResponse() {
        int restaurantId = 1;
        when(restaurantService.getRestaurant(1)).thenReturn(restaurant);

        Map<String, Object> params = new HashMap<>();
        params.put("comment", "Great food and service!");
        Map<String, Number> ratingMap = new HashMap<>();
        ratingMap.put("food", 4.5);
        ratingMap.put("service", 4.0);
        ratingMap.put("ambiance", 4.0);
        ratingMap.put("overall", 4.2);
        params.put("rating", ratingMap);

        Response response = reviewController.addReview(restaurantId, params);

        assertEquals(HttpStatus.OK.value(), response.getStatus().value());
        assertEquals("review added successfully", response.getMessage());
    }

    @Test
    void addReview_restaurantNotFound_throwsException() {
        int restaurantId = 1;
        when(restaurantService.getRestaurant(restaurantId)).thenThrow(new ResponseException(HttpStatus.NOT_FOUND, "Restaurant not found"));

        Map<String, Object> params = new HashMap<>();
        params.put("comment", "Great food and service!");
        Map<String, Number> ratingMap = new HashMap<>();
        ratingMap.put("food", 4.5);
        ratingMap.put("service", 4.0);
        ratingMap.put("ambiance", 4.0);
        ratingMap.put("overall", 4.2);
        params.put("rating", ratingMap);

        ResponseException exception = assertThrows(ResponseException.class, () -> {
            reviewController.addReview(restaurantId, params);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Restaurant not found", exception.getMessage());
    }

    @Test
    void addReview_insuffiecientKey_throwsException() {
        int restaurantId = 1;
        when(restaurantService.getRestaurant(restaurantId)).thenReturn(restaurant);

        Map<String, Object> params = new HashMap<>();
        Map<String, Number> ratingMap = new HashMap<>();
        ratingMap.put("food", 4.5);
        ratingMap.put("service", 4.0);
        ratingMap.put("ambiance", 4.0);
        ratingMap.put("overall", 4.2);
        params.put("rating", ratingMap);

        ResponseException exception = assertThrows(ResponseException.class, () -> {
            reviewController.addReview(restaurantId, params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(PARAMS_MISSING, exception.getMessage());
    }

    @Test
    void addReview_parameterMissing_throwsException() {
        int restaurantId = 1;
        when(restaurantService.getRestaurant(restaurantId)).thenReturn(restaurant);

        Map<String, Object> params = new HashMap<>();
        params.put("comment", "Great food and service!");
        Map<String, Number> ratingMap = new HashMap<>();
        ratingMap.put("food", 4.5);
        params.put("rating", ratingMap);

        ResponseException exception = assertThrows(ResponseException.class, () -> {
            reviewController.addReview(restaurantId, params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(PARAMS_BAD_TYPE, exception.getMessage());
    }

    @Test
    void addReview_reviewServiceThrowsException_throwsException() throws UserNotFound, ManagerCannotReview,
            RestaurantNotFound, InvalidReviewRating, UserHasNotReserved{
        int restaurantId = 1;
        when(restaurantService.getRestaurant(restaurantId)).thenReturn(restaurant);

        Map<String, Object> params = new HashMap<>();
        params.put("comment", "Great food and service!");
        Map<String, Number> ratingMap = new HashMap<>();
        ratingMap.put("food", 4.5);
        ratingMap.put("service", 4.0);
        ratingMap.put("ambiance", 4.2);
        ratingMap.put("overall", 4.3);
        params.put("rating", ratingMap);

        RuntimeException serviceException = new RuntimeException("Database error");
        doThrow(serviceException).when(reviewService).addReview(anyInt(), any(Rating.class), anyString());

        ResponseException exception = assertThrows(ResponseException.class, () -> {
            reviewController.addReview(restaurantId, params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(serviceException.getMessage(), exception.getMessage());
    }
}
