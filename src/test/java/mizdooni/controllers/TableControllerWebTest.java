package mizdooni.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import mizdooni.exceptions.RestaurantNotFound;
import mizdooni.exceptions.UserNotManager;
import mizdooni.model.Address;
import mizdooni.model.Restaurant;
import mizdooni.model.User;
import mizdooni.model.Table;
import mizdooni.service.RestaurantService;
import mizdooni.service.TableService;
import mizdooni.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableController.class)
public class TableControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @MockBean
    private User mockManager;

    @MockBean
    private TableService tableService;
    @MockBean
    private UserService userService;
    private Address address;
    private User user1;
    private User user2;
    private Restaurant restaurant1;
    private Restaurant restaurant2;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        address = new Address("Iran", "Tehran", "Khoone Ali");
        user1 = new User("ali", "ali1122", "ali@ali.com", address, User.Role.manager);
        restaurant1 = new Restaurant("1", user1, "Italian", LocalTime.of(9, 0), LocalTime.of(22, 0), "Test Description", new Address("Country", "City", "Street"), "image.jpg");
        user2 = new User("alii", "ali1122", "alli@ali.com", address, User.Role.client);
        restaurant2 = new Restaurant("2", user2, "Italian", LocalTime.of(9, 0), LocalTime.of(22, 0), "Test Description", new Address("Country", "City", "Street"), "image.jpg");

    }

    @Test
    void getTables_validRestaurant_tableAdded() throws Exception {
        int restaurantId = 1;
        List<Table> tables = List.of(
                new Table(1, restaurantId, 4),
                new Table(2, restaurantId, 6)
        );
        when(restaurantService.getRestaurant(restaurantId)).thenReturn(restaurant1);
        when(tableService.getTables(eq(restaurantId))).thenReturn(tables);
        mockMvc.perform(get("/tables/{restaurantId}", restaurantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("tables listed"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].tableNumber").value(1));
    }

    @Test
    void getTables_invalidRestaurant_notFoundMessage() throws Exception {
        int restaurantId = 2;
        List<Table> tables = List.of(
                new Table(1, restaurantId, 4),
                new Table(2, restaurantId, 6)
        );
        when(restaurantService.getRestaurant(restaurantId)).thenReturn(null);
        when(tableService.getTables(eq(restaurantId))).thenReturn(tables);
        mockMvc.perform(get("/tables/{restaurantId}", restaurantId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("restaurant not found"));
    }

    @Test
    void addTable_validRestaurant_tableAdded() throws Exception {
        int restaurantId = 1;
        Map<String, String> input = Map.of("seatsNumber", "4");
        when(restaurantService.getRestaurant(restaurantId)).thenReturn(restaurant1);
        doNothing().when(tableService).addTable(restaurantId, 4);
        mockMvc.perform(post("/tables/{restaurantId}", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("table added"));
    }

    @Test
    void addTable_invalidRestaurant_notFoundException() throws Exception {
        int restaurantId = 4;
        Map<String, String> input = Map.of("seatsNumber", "4");
        when(restaurantService.getRestaurant(restaurantId)).thenReturn(null);
        doThrow(RestaurantNotFound.class).when(tableService).addTable(restaurantId, 4);
        mockMvc.perform(post("/tables/{restaurantId}", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("restaurant not found"));
    }

    @Test
    void addTable_notManagerUser_notManagerException() throws Exception {
        int restaurantId = 2;
        Map<String, String> input = Map.of("seatsNumber", "4");
        when(restaurantService.getRestaurant(restaurantId)).thenReturn(restaurant2);
        doThrow(UserNotManager.class).when(tableService).addTable(restaurantId, 4);
        mockMvc.perform(post("/tables/{restaurantId}", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("UserNotManager"));
    }
}