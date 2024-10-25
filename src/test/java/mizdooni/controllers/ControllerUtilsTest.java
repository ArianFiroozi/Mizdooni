package mizdooni.controllers;

import mizdooni.model.Restaurant;
import mizdooni.response.ResponseException;
import mizdooni.service.RestaurantService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

public class ControllerUtilsTest {
    @Mock
    RestaurantService rs;
    @Mock
    Restaurant restaurant;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void containsKeys_allKeysPresent_returnsTrue() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", "ali");
        params.put("password", "ramze ali");

        Assertions.assertTrue(ControllerUtils.containsKeys(params, "username", "password"));
    }

    @Test
    void containsKeys_someKeysMissing_returnsFalse() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", "ali");
        params.put("password", "ramze ali");

        Assertions.assertFalse(ControllerUtils.containsKeys(params, "username", "password", "email"));
    }

    @Test
    void containsKeys_emptyMap_returnsFalse() {
        Map<String, Object> params = new HashMap<>();
        Assertions.assertFalse(ControllerUtils.containsKeys(params, "username", "password"));
    }

    @Test
    void doExists_valuesExist_returnsTrue() {
        Assertions.assertTrue(ControllerUtils.doExist("username", "password"));
    }

    @Test
    void doExists_blankValue_returnsTrue() {
        Assertions.assertFalse(ControllerUtils.doExist(" ", "password"));
    }

    @Test
    void doExists_nullValue_returnsTrue() {
        Assertions.assertFalse(ControllerUtils.doExist(null, "password"));
    }

    @Test
    void checkRestaurant_getRestaurantReturnsRestaurant_returnsRestaurant() {
        when(rs.getRestaurant(1)).thenReturn(restaurant);
        Assertions.assertEquals(restaurant, ControllerUtils.checkRestaurant(1, rs));
    }

    @Test
    void checkRestaurant_getRestaurantReturnsNull_throwsException() {
        when(rs.getRestaurant(1)).thenReturn(null);
        Assertions.assertThrows(ResponseException.class, ()->{ControllerUtils.checkRestaurant(1, rs);});
    }
}
