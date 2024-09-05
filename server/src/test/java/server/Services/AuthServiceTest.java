package server.Services;

import org.junit.jupiter.api.Test;
import server.Services.AuthService;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {
    private AuthService as1;
    private AuthService as2;
    @Test
    void addPasswordAndAuthenticate() {
        as1 = new AuthService();
        String pass = "1*%82@%XQSD";
        as1.addPassword(pass);
        assertTrue(as1.authenticate(pass));
    }

    @Test
    void authenticateEmpty() {
        as2 = new AuthService();
        assertFalse(as2.authenticate("asd"));
    }
}