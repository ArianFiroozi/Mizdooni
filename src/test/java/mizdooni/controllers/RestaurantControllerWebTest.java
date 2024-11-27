package mizdooni.controllers;

import mizdooni.model.Address;
import mizdooni.model.Restaurant;
import mizdooni.model.User;
import mizdooni.response.PagedList;
import mizdooni.service.RestaurantService;
import mizdooni.service.UserService;
import org.junit.jupiter.api.RepeatedTest;
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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.eq;
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

    static private Restaurant createRestaurant1(){
        Address address = new Address("Iran", "Tehran", "Khoone Ali");
        User user = new User("ali", "ali1122", "ali@ali.com", address, User.Role.client);
        return new Restaurant("1", user, "Italian", LocalTime.of(9, 0), LocalTime.of(22, 0),
                "Test Description", new Address("Country", "City", "Street"), "image.jpg");
    }

    static private Restaurant createRestaurant2(){
        Address address = new Address("Iran", "Tehran", "Khoone Ali");
        User user2 = new User("alii", "alii1122", "alii@ali.com", address, User.Role.manager);
        return new Restaurant("2", user2, "Mexican", LocalTime.of(10, 0), LocalTime.of(23, 0),
                "Another Description", new Address("Country", "City", "Street"), "image2.jpg");
    }

    @Test
    void getRestaurants_restaurantIsDefined_returnsCorrectAttributes() throws Exception {
        Restaurant restaurant = createRestaurant1();
        int restaurantId = restaurant.getId();
        when(restaurantService.getRestaurant(restaurantId)).thenReturn(restaurant);
        mockMvc.perform(get("/restaurants/{restaurantId}", restaurantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("restaurant found"))
                .andExpect(jsonPath("$.data.name").value("1"));
    }

    @Test
    void getRestaurants_restaurantIsNotDefined_returnsNotFound() throws Exception {
        Restaurant restaurant = createRestaurant1();
        int restaurantId = restaurant.getId();
        when(restaurantService.getRestaurant(restaurantId)).thenReturn(restaurant);
        mockMvc.perform(get("/restaurants/{restaurantId}", 2))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("restaurant not found"));
    }

    @Test
    void getRestaurants_oneRestaurantIsInPageList_returnsCorrectPageList() throws Exception {
        PagedList<Restaurant> pagedRestaurants = new PagedList<Restaurant>(
                List.of(createRestaurant1()),
                1, 10);
        when(restaurantService.getRestaurants(eq(1), any())).thenReturn(pagedRestaurants);
        mockMvc.perform(get("/restaurants")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("restaurants listed"))
                .andExpect(jsonPath("$.data.pageList.length()").value(1))
//                .andExpect(jsonPath("$.data.pageList[0].id").value(1))
                .andExpect(jsonPath("$.data.pageList[0].name").value("1"))
                .andExpect(jsonPath("$.data.pageList[0].type").value("Italian"))
                .andExpect(jsonPath("$.data.pageList[0].description").value("Test Description"))
                .andExpect(jsonPath("$.data.pageList[0].address.country").value("Country"))
                .andExpect(jsonPath("$.data.pageList[0].address.city").value("City"))
                .andExpect(jsonPath("$.data.pageList[0].address.street").value("Street"))
                .andExpect(jsonPath("$.data.pageList[0].image").value("image.jpg"));
    }

    @Test
    void getRestaurants_noRestaurantsAreInPageList_returnsEmptyList() throws Exception {
        PagedList<Restaurant> pagedRestaurants = new PagedList<Restaurant>(
                List.of(),
                1, 10);
        when(restaurantService.getRestaurants(eq(1), any())).thenReturn(pagedRestaurants);
        mockMvc.perform(get("/restaurants")
                        .param("page", "0"));
    }

    @Test
    void getManagerRestaurants_restaurantIsValid_returnsManager() throws Exception {
        Restaurant restaurant1 = createRestaurant1();
        Restaurant restaurant2 = createRestaurant2();
        List<Restaurant> mockRestaurants = List.of(restaurant1, restaurant2);
        when(restaurantService.getManagerRestaurants(eq(1))).thenReturn(mockRestaurants);

        mockMvc.perform(get("/restaurants/manager/{managerId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].managerUsername").value("ali"));
    }

    @Test
    void addRestaurant_parametersCorrect_addsNewRestaurant() throws Exception {
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
    void addRestaurant_insufficientParams_returnsBadRequestError() throws Exception {
        String requestBody = """
            {
                "name": "New Restaurant",
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("parameters missing"));
    }

    @Test
    void validateRestaurantName_nameIsAvailable_returnsOK() throws Exception {
        String restaurantName = "Unique Name";
        when(restaurantService.restaurantExists(restaurantName)).thenReturn(false);
        mockMvc.perform(get("/validate/restaurant-name")
                        .param("data", restaurantName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("restaurant name is available"));
    }

    @Test
    void validateRestaurantName_nameIsNotUnique_returnsNameTakenError() throws Exception {
        String restaurantName = "Not Unique Name";
        when(restaurantService.restaurantExists(restaurantName)).thenReturn(true);
        mockMvc.perform(get("/validate/restaurant-name")
                        .param("data", restaurantName))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("restaurant name is taken"));
    }

    @Test
    void getRestaurantTypes_typesAreSet_returnsRestaurantTypes() throws Exception {
        Set<String> types = Set.of("Italian", "Mexican", "Chinese");
        when(restaurantService.getRestaurantTypes()).thenReturn(types);
        mockMvc.perform(get("/restaurants/types", types))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("restaurant types"))
                .andExpect(jsonPath("$.data", containsInAnyOrder("Chinese", "Italian", "Mexican")));
    }

    @Test
    void getRestaurantLocations_locationsAreSet_returnsRestaurantLocations() throws Exception {
        Map<String, Set<String>> locations = Map.of("Country", Set.of("City1", "City2"));
        when(restaurantService.getRestaurantLocations()).thenReturn(locations);
        mockMvc.perform(get("/restaurants/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("restaurant locations"))
                .andExpect(jsonPath("$.data.Country").isArray())
                .andExpect(jsonPath("$.data.Country", containsInAnyOrder("City1", "City2")));
    }

}
