package mizdooni.controllers;

import mizdooni.model.User;
import mizdooni.response.Response;
import mizdooni.response.ResponseException;
import mizdooni.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;

public class AuthenticationControllerTest {

    @Mock
    UserService userService;

    @Mock
    User user;

    @InjectMocks
    AuthenticationController ac;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void user_nullUser_returnsException() {
        when(userService.getCurrentUser()).thenReturn(null);
        Assertions.assertThrows(ResponseException.class, ac::user);
    }

    @Test
    void user_mockUser_returnsResponse() {
        when(userService.getCurrentUser()).thenReturn(user);
        Assertions.assertInstanceOf(Response.class, ac.user());
    }
}