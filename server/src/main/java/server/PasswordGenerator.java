package server;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String CHARACTER_SET =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:,.<>?";

    /**
     * Generates a secure password of length 32,
     * which contains all alphanumerical characters
     * @return String password
     */
    public static String generatePassword() {
        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 32; i++) {
            int randomIndex = random.nextInt(CHARACTER_SET.length());
            char c = CHARACTER_SET.charAt(randomIndex);
            password.append(c);
        }
        return password.toString();
    }
}