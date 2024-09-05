package client.scenes;

import client.utils.LanguageButtonUtils;
import client.utils.LanguageResourceBundle;
import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import com.google.inject.Inject;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditParticipantCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private LanguageResourceBundle languageResourceBundle;
    private Event event;
    private Participant participant;
    @FXML
    private Label editParticipantLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Button removeParticipantButton;
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
     * Constructor for EditParticipantCtrl
     * @param server
     * @param mainCtrl
     */
    @Inject
    public EditParticipantCtrl(ServerUtils server, MainCtrl mainCtrl) {
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
        Event undoEvent = event;
        participant.setName(nameField.getText());
        participant.setIban(ibanField.getText());
        participant.setEmail(emailField.getText());
        participant.setBic(bicField.getText());
        clearFields();
        server.persistEvent(event);
        event = server.getEvent(event.getId());
        if(event != null){
            mainCtrl.showEventOverview(event);
        }
        else{
            mainCtrl.showEventOverview(undoEvent);
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
     * Setter for participant
     * @param participant
     */
    public void setParticipant(Participant participant) {
        this.participant = participant;
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
        if (participant != null){
            languageResourceBundle = LanguageResourceBundle.getInstance();
            switchTextLanguage();
            nameField.setText(participant.getName());
            nameField.positionCaret(participant.getName().length());
            emailField.setText(participant.getEmail());
            ibanField.setText(participant.getIban());
            bicField.setText(participant.getBic());
        }

    }

    /**
     * Switches the text language.
     */
    public void switchTextLanguage(){
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();

        editParticipantLabel.setText(bundle.getString("editParticipantLabel"));
        nameLabel.setText(bundle.getString("nameLabel"));
        removeParticipantButton.setText(bundle.getString("removeParticipantButton"));
        participantCancelButton.setText(bundle.getString("participantCancelButton"));
        participantOkButton.setText(bundle.getString("participantOkButton"));

    }

    /**
     * Removes the participant from the event
     * @param actionEvent ...
     */
    public void removeParticipant(ActionEvent actionEvent) {
        if (participant != null){
            ResourceBundle bundle = languageResourceBundle.getResourceBundle();
            Alert alert =  new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(bundle.getString("removeParticipantAlertTitleText"));
            alert.setHeaderText(bundle.getString("removeParticipantAlertHeaderText"));
            alert.setContentText(bundle.getString("removeParticipantAlertContentText")
                    + " " + this.participant.getName());
            if (alert.showAndWait().get() == ButtonType.OK){
                Event undoEvent = event;
                event.removeParticipant(participant);
                server.deleteParticipant(participant);
                updateExpenses();
                event = server.getEvent(event.getId());
                server.persistEvent(event);
                if(event != null){
                    mainCtrl.showEventOverview(event);
                }
                else{
                    mainCtrl.showEventOverview(undoEvent);
                }
            }
        }

    }

    /**
     * update expenses after participant removal
     */
    public void updateExpenses(){
        for(int i=0; i < event.getExpenses().size(); i++){
            Expense e = event.getExpenses().get(i);
            if(e.getPayingParticipant().equals(participant)){
                event.removeExpense(e);
                i--;
            }
            else if(e.getParticipants().contains(participant)){
                List<Participant> updatedExpenses = e.getParticipants();
                updatedExpenses.remove(participant);
                e.setParticipants(updatedExpenses);
            }
        }
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
