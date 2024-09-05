package server.Services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

    private List<String> authorizedPasswords;

    /**
     * Constructor for the AuthService
     */
    public AuthService() {
        this.authorizedPasswords = new ArrayList<>();
    }

    /**
     * Adds the password to the list of authenticated passwords
     * @param password Password to be added
     */
    public void addPassword(String password){
        authorizedPasswords.add(password);
    }

    /**
     * Checks whether the given password is in the lsit of
     * authenticated passwords
     * @param password Password to be checked
     * @return Boolean authenticated
     */
    public boolean authenticate(String password) {
        return authorizedPasswords.contains(password);
    }
}
