package mizdooni.controllers;

import mizdooni.model.Address;
import mizdooni.model.Restaurant;
import mizdooni.model.User;
import mizdooni.service.RestaurantService;
import mizdooni.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;
    @MockBean
    private UserService userService;

    @Test
    void testGetRestaurant() throws Exception {
        int restaurantId = 1;
        Address address = new Address("Iran", "Tehran", "Khoone Ali");
        User user = new User("ali", "ali1122", "ali@ali.com", address, User.Role.client);
        Restaurant restaurant = new Restaurant("1", user, "Italian", LocalTime.of(9, 0), LocalTime.of(22, 0), "Test Description", new Address("Country", "City", "Street"), "image.jpg");
        when(restaurantService.getRestaurant(restaurantId)).thenReturn(restaurant);

        mockMvc.perform(get("/restaurants/{restaurantId}", restaurantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("restaurant found"))
                .andExpect(jsonPath("$.data.name").value("Test Restaurant"));
    }

//    @Test
//    void testGetRestaurants() throws Exception {
//        Address address = new Address("Iran", "Tehran", "Khoone Ali");
//        User user = new User("ali", "ali1122", "ali@ali.com", address, User.Role.client);
//        PagedList<Restaurant> pagedRestaurants = new PagedList<>(List.of(new Restaurant("1", user, "Italian", LocalTime.of(9, 0), LocalTime.of(22, 0), "Test Description", new Address("Country", "City", "Street"), "image.jpg")), 1, 10, 1);
//        when(restaurantService.getRestaurants(eq(1), any())).thenReturn(pagedRestaurants);
//
//        mockMvc.perform(get("/restaurants")
//                        .param("page", "1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("restaurants listed"));
//    }

    @Test
    void testAddRestaurant() throws Exception {
        String requestBody = """
            {
                "name": "New Restaurant",
                "type": "Cafe",
                "description": "Great Place",
                "startTime": "09:00",
                "endTime": "21:00",
                "address": {
                    "country": "Country",
                    "city": "City",
                    "street": "Street"
                }
            }
            """;
        when(restaurantService.addRestaurant(any(), any(), any(), any(), any(), any(), any())).thenReturn(1);

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("restaurant added"))
                .andExpect(jsonPath("$.data").value(1));
    }

    @Test
    void testValidateRestaurantName() throws Exception {
        String restaurantName = "Unique Name";
        when(restaurantService.restaurantExists(restaurantName)).thenReturn(false);

        mockMvc.perform(get("/validate/restaurant-name")
                        .param("data", restaurantName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("restaurant name is available"));
    }

    @Test
    void testGetRestaurantTypes() throws Exception {
        Set<String> types = Set.of("Italian", "Mexican", "Chinese");
        when(restaurantService.getRestaurantTypes()).thenReturn(types);

        mockMvc.perform(get("/restaurants/types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("restaurant types"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").value(List.of("Italian", "Mexican", "Chinese")));
    }

    @Test
    void testGetRestaurantLocations() throws Exception {
        Map<String, Set<String>> locations = Map.of("Country", Set.of("City1", "City2"));
        when(restaurantService.getRestaurantLocations()).thenReturn(locations);

        mockMvc.perform(get("/restaurants/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("restaurant locations"))
                .andExpect(jsonPath("$.data.Country").isArray())
                .andExpect(jsonPath("$.data.Country").value(List.of("City1", "City2")));
    }
}
