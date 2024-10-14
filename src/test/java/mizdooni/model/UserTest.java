package mizdooni.model;

import mizdooni.model.User;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class UserTest {
    @Test
    public void checkPasswordAcceptsCorrectPassword() { //TODO: move setup to beforeEach
        Address address=new Address("Iran", "Tehran", "Khoone Ali");
        User client=new User("ali", "ali1122", "ali@ali.com", address, User.Role.client);
        assertTrue(client.checkPassword("ali1122"));
    }
}
