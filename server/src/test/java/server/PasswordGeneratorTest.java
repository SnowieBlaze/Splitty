package server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTest {
    private static final String CHARACTER_SET =
         "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:,.<>?";

    @Test
    void testLength(){
        assertTrue(PasswordGenerator.generatePassword().length() == 32);
    }

    @Test
    void testCharacterSet(){
        String password = PasswordGenerator.generatePassword();
        boolean inCharSet = true;
        for(int i = 0; i < password.length(); i++){
            char c = password.charAt(i);
            if(!CHARACTER_SET.contains(String.valueOf(c))){
                inCharSet = false;
            }
        }
        assertTrue(inCharSet);
    }

    @Test
    void randomnessTest(){
        String password1 = PasswordGenerator.generatePassword();
        String password2 = PasswordGenerator.generatePassword();
        assertNotEquals(password1, password2);
    }

}