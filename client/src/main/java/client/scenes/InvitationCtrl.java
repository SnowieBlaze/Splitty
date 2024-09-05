package client.scenes;

import client.utils.ConfigClient;
import client.utils.LanguageButtonUtils;
import client.utils.LanguageResourceBundle;
import client.utils.ServerUtils;
import com.google.inject.Inject;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;


public class InvitationCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private LanguageResourceBundle languageResourceBundle;

    @FXML
    private Text eventNameText;

    @FXML
    private Text invitationSendPeopleText;

    @FXML
    private Text invitationPeopleByEmailText;

    @FXML
    private TextField inviteCodeText;

    @FXML
    private Button sendInvitesButton;

    @FXML
    private Button goEventButton;

    @FXML
    private Button goBackButton;

    @FXML
    private TextArea emailTextArea;

    private Event event;

    private Path filePath = Paths.get("src/main/resources/config.txt").toAbsolutePath();

    /**
     *
     * @param server
     * @param mainCtrl
     */
    @Inject
    public InvitationCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes eventNameText and inviteCodeText based on what they should be.
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    public void initialize(URL location, ResourceBundle resources){
        eventNameText.setText("");
        inviteCodeText.setText("");

    }

    /**
     * initialize method for invitationCtrl
     */
    public void initialize(){
        languageResourceBundle = LanguageResourceBundle.getInstance();
        switchTextLanguage();
        setInviteCodeText();
        setEventNameText();
    }

    /**
     * Switches the text language.
     */

    public void switchTextLanguage(){

        ResourceBundle bundle = languageResourceBundle.getResourceBundle();

        emailTextArea.setPromptText(bundle.getString("emailPromptText"));
        sendInvitesButton.setText(bundle.getString("sendInvitesButton"));
        goEventButton.setText(bundle.getString("goEventButton"));
        goBackButton.setText(bundle.getString("backButton"));
        invitationPeopleByEmailText.setText(bundle.getString("invitationPeopleByEmailText"));
        invitationSendPeopleText.setText(bundle.getString("invitationSendPeopleText"));
    }

    /**
     * Sets inviteCodeText.
     */
    public void setInviteCodeText(){
        inviteCodeText.setText(this.event.getInviteCode());
    }

    /**
     * Sets eventNameText.
     */
    public void setEventNameText(){
        eventNameText.setText(this.event.getTitle());
    }

    /**
     * Sets event.
     * @param event the event for the invitation
     */
    public void setEvent(Event event){
        this.event = event;
    }

    /**
     * Resets texts.
     */
    private void resetFields(){
        eventNameText.setText("");
        inviteCodeText.setText("");
    }

    /**
     * Clears email area.
     */
    private void clearEmail(){
        emailTextArea.clear();
    }

    @FXML
    /**
     * Should send invites.
     */ void sendInvites(){
        List<String> emails = new ArrayList<>();
        Scanner scanner = new Scanner(emailTextArea.getText());
        while(scanner.hasNextLine()){
            String email = scanner.nextLine();
            if(!email.equals("")){
                emails.add(email);
            }
        }
        if(!emails.isEmpty()){
            sendInvitesHelper(emails);
        }
    }

    /**
     * Helper method for send invites
     * sends the request to the server and
     * Handles both outcomes of sending the invites
     * @param emails
     */
    public void sendInvitesHelper(List<String> emails){
        ResourceBundle bundle = LanguageResourceBundle.getInstance().getResourceBundle();
        String name = ConfigClient.getName();
        Boolean response = server.sendInvites(emails, event, name);
        if(response != null){
            if(response){
                Alert alert =  new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(bundle.getString("sendInvitesInfoAlertTitle"));
                alert.setHeaderText(bundle.getString("sendInvitesInfoAlertHeader"));
                alert.showAndWait();
                for(String email : emails){
                    addParticipant(email);
                }
                clearEmail();
            }else{
                Alert alert =  new Alert(Alert.AlertType.ERROR);
                alert.setTitle(bundle.getString("sendInvitesErrorAlertTitle"));
                alert.setHeaderText(bundle.getString("sendInvitesErrorAlertHeader"));
                alert.showAndWait();
            }
        }
    }

    private void addParticipant(String email) {
        Event undoEvent = event;
        Participant participant = new Participant();
        participant.setName(email.substring(0, email.indexOf("@")));
        participant.setEmail(email);
        event.addParticipant(participant);
        server.persistEvent(event);
        event = server.getEvent(event.getId());
        if(event == null){
            event = undoEvent;
        }
    }

    @FXML
    void backToStart(){
        resetFields();
        clearEmail();
        mainCtrl.showStartScreen();
    }

    @FXML
    private void goToEvent(){
        mainCtrl.showEventOverview(this.event);
        resetFields();
        clearEmail();
    }

    /**
     * Method to be called when a key is pressed
     * @param e keyevent to listen
     */
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getCode() == KeyCode.W) {  //close window
            mainCtrl.closeWindow();
        }
        switch (e.getCode()) {
            case ENTER:
                sendInvites();
                break;
            case ESCAPE:
                backToStart();
                break;
            default:
                break;
        }
    }

    //The methods below are only for testing purposes, do not use.


    /**
     * Gets the invite code text.
     * @return the invite code text
     */
    public String getInviteCodeText(){
        return inviteCodeText.getText();
    }

    /**
     * Gets the event name text.
     * @return the event name text
     */
    public String getEventNameText(){
        return eventNameText.getText();
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
