package client.scenes;

import client.utils.LanguageButtonUtils;
import client.utils.LanguageResourceBundle;
import client.utils.ServerUtils;
import commons.Event;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import com.google.inject.Inject;

import java.net.URL;
import java.util.ResourceBundle;

public class AddParticipantCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private LanguageResourceBundle languageResourceBundle;
    private Event event;
    private Participant participant;

    @FXML
    private Label nameLabel;
    @FXML
    private Label participantAddLabel;
    @FXML
    private Button participantCancelButton;
    @FXML
    private Button participantOkButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField ibanField;
    @FXML
    private TextField bicField;

    /**
     * Constructor for AddParticipant
     * @param server
     * @param mainCtrl
     */
    @Inject
    public AddParticipantCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Controller method for cancel button
     * Sends back to overviewEvent window
     * @param actionEvent to handle
     */
    public void cancel(ActionEvent actionEvent) {
        clearFields();
        mainCtrl.showEventOverview(event);
    }

    /**
     * Controller class for the ok button
     * Sends back to Overview Event window back with the participant altered
     * @param actionEvent to handle
     */
    public void ok(ActionEvent actionEvent) {
        if(event != null) {
            Event undoEvent = event;
            Participant participant = new Participant();
            participant.setName(nameField.getText());
            participant.setIban(ibanField.getText());
            participant.setEmail(emailField.getText());
            participant.setBic(bicField.getText());
            //server.addParticipant(participant);
            event.addParticipant(participant);
            clearFields();
            event = server.persistEvent(event);
            if (event != null) {
                mainCtrl.showEventOverview(event);
            } else {
                mainCtrl.showEventOverview(undoEvent);
            }
        }
    }

    /**
     * Clear all fields for the next use
     */
    private void clearFields() {
        nameField.clear();
        emailField.clear();
        ibanField.clear();
        bicField.clear();
    }

    /**
     * Setter for event
     * @param event to set
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Handles the key event pressed
     * @param e the KeyEvent to handle
     */
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getCode() == KeyCode.W) {  //close window
            mainCtrl.closeWindow();
        }
        if (e.isControlDown() && e.getCode() == KeyCode.S) {  //close window
            ok(null);
        }
        switch (e.getCode()) {
            case ESCAPE:
                cancel(null);
                break;
            case TAB:
                moveToNextTextField((TextField) e.getSource());
                break;
            default:
                break;
        }
    }

    /**
     * Moves the focus to the next field
     * @param currentTextField current field where the focus is now
     */
    private void moveToNextTextField(TextField currentTextField) {
        // Find the index of the current text field
        int index = -1;
        TextField[] textFields = {nameField, emailField,
                                  ibanField, bicField }; // Add all text fields here
        for (int i = 0; i < textFields.length; i++) {
            if (textFields[i] == currentTextField) {
                index = i;
                break;
            }
        }

        // Move focus to the next text field
        if (index != -1 && index < textFields.length - 1) {
            textFields[index + 1].requestFocus();
        }
    }


    /**
     * Initiallizes the fields with the participant's data
     */
    public void initialize() {
        languageResourceBundle = LanguageResourceBundle.getInstance();
        switchTextLanguage();
    }

    /**
     * Switches the text language.
     */
    public void switchTextLanguage(){
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();

        nameLabel.setText(bundle.getString("nameLabel"));
        participantAddLabel.setText(bundle.getString("participantAddLabel"));
        participantCancelButton.setText(bundle.getString("participantCancelButton"));
        participantOkButton.setText(bundle.getString("participantOkButton"));

    }

    /**
     * Sets the icon of the chosen button.
     * @param iconName
     * @param button
     */
    @FXML
    public void setIcon(String iconName, Button button) {
        String path = "client/images/icons/" + iconName;
        URL url = LanguageButtonUtils.class.getClassLoader().getResource(path);
        if (url == null) {
            throw new RuntimeException("Resources folder not found");
        }

        Image image = new Image(String.valueOf(url));

        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        button.setGraphic(imageView);
    }
}
