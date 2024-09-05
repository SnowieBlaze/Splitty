package server.api;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.Services.AuthService;
import server.PasswordGenerator;


@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AuthService authService;

    /**
     * Generates a password and prints it in the server terminal and
     * adds the generated password to the list of authorized passwords
     * in authService.
     */
    @GetMapping(path = {"", "/"})
    public void generatePassword(){
        String password = PasswordGenerator.generatePassword();
        authService.addPassword(password);
        System.out.println(password);
    }

    /**
     * Authenticates the user based on the provided password
     * @param password The password to be used in login attempt
     * @param response The response object to be manipulated
     * @return String success / fail message
     */
    @PostMapping(path = { "", "/" })
    public String login(@RequestBody String password, HttpServletResponse response) {
        if (authService.authenticate(password)){
            response.setStatus(HttpServletResponse.SC_OK);
            return "Admin authenticated";
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return "Not authenticated";
    }
}
