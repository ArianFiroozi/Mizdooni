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

import java.util.HashMap;
import java.util.Map;

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
    void user_nullUser_throwsException() {
        when(userService.getCurrentUser()).thenReturn(null);
        Assertions.assertThrows(ResponseException.class, ac::user);
    }

    @Test
    void user_mockUser_returnsResponse() {
        when(userService.getCurrentUser()).thenReturn(user);
        Assertions.assertInstanceOf(Response.class, ac.user());
    }

    @Test
    void login_loginSuccessful_returnsResponse() {
        Map<String, String> params = new HashMap<>();
        params.put("username", "ali");
        params.put("password", "ramze ali");

        when(userService.login("ali", "ramze ali")).thenReturn(true);
        when(userService.getCurrentUser()).thenReturn(user);

        Assertions.assertInstanceOf(Response.class, ac.login(params));
    }

    @Test
    void login_insufficientParams_throwsException() {
        Map<String, String> params = new HashMap<>();
        params.put("username", "ali");

        when(userService.login("ali", "ramze ali")).thenReturn(true);
        when(userService.getCurrentUser()).thenReturn(user);

        Assertions.assertThrows(ResponseException.class, ()->{ac.login(params);});
    }

    @Test
    void login_userNotFound_throwsException() {
        Map<String, String> params = new HashMap<>();
        params.put("username", "ali");
        params.put("password", "ramze ali");

        when(userService.login("ali", "ramze ali")).thenReturn(false);
        when(userService.getCurrentUser()).thenReturn(user);

        Assertions.assertThrows(ResponseException.class, ()->{ac.login(params);});
    }

    @Test
    void signup_correctParams_returnsResponse() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", "ali");
        params.put("password", "ramze ali");
        params.put("email", "email@ali.com");
        Map<String, String> address = new HashMap<>();
        address.put("country", "home");
        address.put("city", "home");
        params.put("address", address);
        params.put("role", "client");
        when(userService.login("ali", "ramze ali")).thenReturn(true);

        Assertions.assertInstanceOf(Response.class, ac.signup(params));
    }

    @Test
    void signup_insufficientParams_throwsException() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", "ali");

        Assertions.assertThrows(ResponseException.class, ()->{ac.signup(params);});
    }

    @Test
    void signup_wrongParamFormat_throwsException() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", "ali");
        params.put("password", "ramze ali");
        params.put("email", "email@ali.com");
        params.put("address", "khoone ali");
        params.put("role", "client");

        Assertions.assertThrows(ResponseException.class, ()->{ac.signup(params);});
    }

    @Test
    void signup_partiallyWrongAddress_throwsException() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", "ali");
        params.put("password", "ramze ali");
        params.put("email", "email@ali.com");
        Map<String, String> address = new HashMap<>();
        address.put("country", "home");
        params.put("address", address);
        params.put("role", "client");

        Assertions.assertThrows(ResponseException.class, ()->{ac.signup(params);});
    }

    @Test
    void signup_loginThrowsException_throwsException() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", "ali");
        params.put("password", "ramze ali");
        params.put("email", "email@ali.com");
        Map<String, String> address = new HashMap<>();
        address.put("country", "home");
        address.put("city", "home");
        params.put("address", address);
        params.put("role", "client");
        when(userService.login("ali", "ramze ali")).thenThrow(NullPointerException.class);

        Assertions.assertThrows(ResponseException.class, ()->{ac.signup(params);});
    }

    @Test
    void logout_userServiceLogoutTrue_returnsResponse() {
        when(userService.logout()).thenReturn(true);
        Assertions.assertInstanceOf(Response.class, ac.logout());
    }

    @Test
    void logout_userServiceLogoutFalse_throwsException() {
        when(userService.logout()).thenReturn(false);
        Assertions.assertThrows(ResponseException.class, ()->{ac.logout();});
    }

    @Test
    void validateUsername_userNameOK_returnsResponse() {
        when(userService.usernameExists("ali")).thenReturn(false);
        Assertions.assertInstanceOf(Response.class, ac.validateUsername("ali"));
    }

    @Test
    void validateUsername_userNameExists_throwsException() {
        when(userService.usernameExists("ali")).thenReturn(true);
        Assertions.assertThrows(ResponseException.class, ()->{ac.validateUsername("ali");});
    }


    @Test
    void validateEmail_emailOK_returnsResponse() {
        when(userService.emailExists("email@ali.com")).thenReturn(false);
        Assertions.assertInstanceOf(Response.class, ac.validateEmail("email@ali.com"));
    }

    @Test
    void validateEmail_emailExists_throwsException() {
        when(userService.emailExists("email@ali.com")).thenReturn(true);
        Assertions.assertThrows(ResponseException.class, ()->{ac.validateUsername("email@ali.com");});
    }
    
    @Test
    void validateEmail_badEmailFormat_throwsException() {
        Assertions.assertThrows(ResponseException.class, ()->{ac.validateUsername("emaile ali");});
    }
}