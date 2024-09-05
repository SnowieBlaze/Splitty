package scenes;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import utils.Admin;

import javax.swing.*;

public class LoginCtrl {

    private final Admin admin;
    private final MainCtrl mainCtrl;
    @FXML
    private Label errorLabel;
    @FXML
    private Label serverLabel;

    @FXML
    private Button loginButton;
    @FXML
    private Button generateButton;

    @FXML
    private PasswordField passwordField;

    /**
     * Constructor for LoginCtrl for dependency injection
     * @param admin
     * @param mainCtrl
     */
    @Inject
    public LoginCtrl(Admin admin, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.admin = admin;
    }

    /**
     * Code to be run on scene start-up. Simply hides the error message
     */
    public void initialize() {
        passwordField.clear();
        errorLabel.setVisible(false);
        serverLabel.setText("Server: " + admin.getURL());
    }

    @FXML
    void login(ActionEvent event) {
        if(admin.checkNull()){
            admin.initWebSocket();
        }
        errorLabel.setVisible(false); //in case this login attempt follows an unsuccessful one
        String password = passwordField.getText();
        if (admin.login(password)){
            admin.setPassword(password);
            mainCtrl.register();
            mainCtrl.showOverview();
        }
        else {
            errorLabel.setText("Error! Incorrect password or unable to connect to server");
            errorLabel.setVisible(true);
        }
    }

    /**
     * When the user presses enter, it triggers the
     * create or join button
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        if(e.getCode() == KeyCode.ENTER){
            login(null);
        }
        if(e.isControlDown() && e.getCode() == KeyCode.W){
            mainCtrl.closeWindow();
        }
    }

    @FXML
    void generatePassword(ActionEvent event) {
        if(admin.checkNull()){
            admin.initWebSocket();
        }
        errorLabel.setVisible(false);
        if(!admin.generatePassword()){
            errorLabel.setText("Error: Unable to connect to the server. " +
                    "Please make sure the server is running and the URL is correct.");
            errorLabel.setVisible(true);
        }
    }

    /**
     * Method to be called when the admin wants to change server
     * @param actionEvent
     */
    public void changeURL(ActionEvent actionEvent) {
        String url = JOptionPane.showInputDialog(null, "Please enter the url " +
                "of the server you want to connect", admin.getURL());
        if (url != null) {
            admin.setURL(url);
            initialize();
        }
    }

}
