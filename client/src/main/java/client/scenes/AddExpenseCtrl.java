package client.scenes;

import client.utils.ConfigClient;
import client.utils.LanguageButtonUtils;
import client.utils.LanguageResourceBundle;
import client.utils.ServerUtils;
import commons.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import com.google.inject.Inject;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddExpenseCtrl{

    private boolean testing = false;

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;
    private Participant participant;
    private Expense expense;
    private String currency;
    private List<Participant> participants;
    private Tag tag;

    private LanguageResourceBundle languageResourceBundle;

    @FXML
    private Text expenseField;                 //Title
    @FXML
    private ChoiceBox<Participant> payerChoiceBox;               //Who paid?
    @FXML
    private TextField titleField;                   //What for?
    @FXML
    private TextField amountField;                  //How much?
    @FXML
    private ChoiceBox currChoiceBox;
    @FXML
    private DatePicker datePicker;                  //When?
    @FXML
    private RadioButton equally;                       //How to split?
    @FXML
    private RadioButton onlySome;
    @FXML
    private GridPane allGridPane;
    @FXML
    private Label tagLabel;                         //Expense Type
    @FXML
    private Button expenseAddButton;
    @FXML
    private Button tagButton;
    @FXML
    private Button expenseAbortButton;
    @FXML
    private Label addExpenseWhoPaid;
    @FXML
    private Label addExpenseForWhat;
    @FXML
    private Label addExpenseHowMuch;
    @FXML
    private Label addExpenseWhen;
    @FXML
    private Label addExpenseHow;
    @FXML
    private Label addExpenseType;
    @FXML
    private Button removeTagButton;

    /**
     * Constructor for AddExpenseCtrl
     * @param server client is on
     * @param mainCtrl of client
     */
    @Inject
    public AddExpenseCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Controller method for abort button
     * Sends back to overviewEvent window
     * @param actionEvent to handle
     */
    public void onAbortClick(ActionEvent actionEvent) {
        clearFields();
        mainCtrl.showEventOverview(event);
    }

    /**
     * Opens the tag scene
     * when the tag button is clicked
     * @param actionEvent -
     */
    public void onTagsClick(ActionEvent actionEvent) {
        mainCtrl.showTags(event, expense, payerChoiceBox.getValue(), true, tag);
    }

    /**
     * Removes a tag from the expense
     */
    public void onTagRemove() {
        tag = null;
        tagLabel.setText("No tag");
        tagLabel.setStyle("-fx-background-color: #F9F9F9");
        removeTagButton.setVisible(false);
    }

    /**
     * Controller class for the ok button
     * Sends back to Overview Event window back with the participant altered
     * @param actionEvent to handle
     */
    public void onAddClick(ActionEvent actionEvent) {
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();
        Event undoEvent = event;
        Expense expense = new Expense();
        expense.setTitle(titleField.getText());
        expense.setPayingParticipant(payerChoiceBox.getSelectionModel().getSelectedItem());
        try {
            expense.setAmount(Double.parseDouble(amountField.getText()));
            expense.setCurrency(currChoiceBox.getSelectionModel().getSelectedItem().toString());
            saveAsEuro(expense);
        } catch (Exception e) {
            Alert alert =  new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("expenseAlertInvalidInputTitle"));
            alert.setHeaderText(bundle.getString("expenseAlertInvalidInputMoneyHeader"));
            alert.setContentText(bundle.getString("expenseAlertInvalidInputMoneyContent"));

            alert.showAndWait();
            return;
        }
        if(equally.isSelected() || participants.size() == event.getParticipants().size()){
            expense.setParticipants(event.getParticipants());
        }
        else{
            if(participants == null || participants.size() == 0){
                Alert alert =  new Alert(Alert.AlertType.ERROR);
                alert.setTitle(bundle.getString("expenseAlertInvalidInputTitle"));
                alert.setHeaderText(bundle.getString("expenseAlertInvalidParticipantHeader"));
                alert.setContentText(bundle.getString("expenseAlertInvalidParticipantContent"));
                alert.showAndWait();
                return;
            }
            else {
                expense.setParticipants(participants);}
        }
        expense.setDateTime(datePicker.getValue().toString());
        addDebts(expense);
        expense.setTag(tag);
        event.addExpense(expense);
        server.persistEvent(event);
        clearFields();
        event = server.getEvent(event.getId());
        if(event != null){
            mainCtrl.showEventOverview(event);
        }
        else{
            mainCtrl.showEventOverview(undoEvent);
        }
    }

    /**
     * @param expense
     */
    public void addDebts(Expense expense){
        for(Participant participant : expense.getParticipants()) {
            if(participant.equals(expense.getPayingParticipant())) {
                continue;
            }
            Debt debt = new Debt(expense.getPayingParticipant(), participant,
                    expense.getAmount() / (expense.getParticipants().size()));
            expense.add(debt);
        }
    }

    /**
     *  converted currency to save to server as EUR
     * @param expense to change to euro
     */

    public void saveAsEuro(Expense expense) throws Exception {
        if(expense.getCurrency().equals("EUR")){
            return;
        }
        Double res = Double.parseDouble(amountField.getText());
        try {
            res *= server.convertRate(datePicker.getValue().toString(),
                    currChoiceBox.getSelectionModel().getSelectedItem().toString(),
                    "EUR");
            expense.setAmount(res);
            expense.setCurrency("EUR");
        }
        catch (Exception e){
            System.out.println("The API currency converter we are using has 100" +
                    " calls/minute and can find historical currency conversion up" +
                    " to one year.");
        }
    }

    /**
     * Clear all fields for the next use
     */
    public void clearFields() {
        payerChoiceBox.getSelectionModel().selectFirst();
        titleField.clear();
        amountField.clear();
        currChoiceBox.getSelectionModel().selectFirst();
        equally.setSelected(true);
        onlySome.setSelected(false);
        allGridPane.getChildren().clear();
    }

    /**
     * Setter for event
     * @param event to set
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Setter for expense
     * @param expense to set
     */
    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    /**
     * Setter for currency
     * @param currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Setter for participants
     * @param participants to set
     */
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    /**
     * Setter for participant
     * @param participant to set
     */
    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    /**
     * Setter for tag
     * @param tag - the tag to be set
     */
    public void setTag(Tag tag) {
        this.tag = tag;
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
            onAddClick(null);
        }
        switch (e.getCode()) {
            case ESCAPE:
                onAbortClick(null);
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
        TextField[] textFields = {titleField, amountField}; // Add all text fields here
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
     * Method to be executed when only some people have to pay
     */
    @FXML
    public void onlySomeChecked() {
        allGridPane.getChildren().clear();
        allGridPane.setVgap(5);
        allGridPane.setHgap(5);
        if (event != null && onlySome.isSelected()) {
            for (int i = 0; i < event.getParticipants().size(); i++) {
                Label nameLabel = new Label(event.getParticipants().get(i).getName());
                nameLabel.setWrapText(true); // Wrap text to prevent truncation
                CheckBox hasParticipated = new CheckBox("");

                // Set fixed column widths
                nameLabel.setMaxWidth(Double.MAX_VALUE);

                GridPane.setFillWidth(nameLabel, true);

                allGridPane.add(hasParticipated, 0, i);
                allGridPane.add(nameLabel, 1, i);

                Participant p = event.getParticipants().get(i);
                hasParticipated.setSelected(false);
                hasParticipated.setOnAction(event -> addRemoveParticipant(p));
            }
        }
    }

    /**
     * Method to be executed when only some people have to pay
     *
     * @param participant to be added/ removed
     */
    @FXML
    public void addRemoveParticipant(Participant participant) {
        if(participants.contains(participant)){
            participants.remove(participant);
        }
        else{
            participants.add(participant);
        }
    }

    /**
     * Initiallizes the currency choice box with the data
     */
    public void initializeCurr() {
        currency = ConfigClient.getCurrency();
        if(currency == null || currency.length() < 1) {
            currChoiceBox.getSelectionModel().selectFirst();
        }
        List<String> currencies = new ArrayList<>();
        currencies.add("EUR");
        currencies.add("USD");
        currencies.add("CHF");
        currencies.add("AUD");
        currChoiceBox.setItems(FXCollections.observableArrayList(currencies));
        if(currencies.contains(currency)){
            currChoiceBox.getSelectionModel().select(currency);
        }
        else {
            currChoiceBox.getSelectionModel().selectFirst();
        }
    }

    /**
     * Initiallizes the fields with the data
     */
    public void initialize() {
        if (event != null){

            languageResourceBundle = languageResourceBundle.getInstance();
            switchTextLanguage();

            payerChoiceBox.setItems(FXCollections.observableArrayList(event.getParticipants()));
            payerChoiceBox.setConverter(new StringConverter<Participant>() {
                @Override
                public String toString(Participant participant) {
                    if (participant != null) {
                        return participant.getName();
                    }
                    else {
                        return "";
                    }
                }
                @Override
                public Participant fromString(String string) {
                    int i = 0;
                    while(event.getParticipants().get(i).getName() != string &&
                            i < event.getParticipants().size()){
                        i++;
                    }
                    return event.getParticipants().get(i);
                }
            } );

            payerChoiceBox.getSelectionModel().select(participant);
            initializeCurr();
            datePicker.setValue(LocalDate.now());
            equally.setSelected(true);
            onlySome.setSelected(false);
            tagLabel.setMinHeight(20);
            tagLabel.setMinWidth(40);
            tagLabel.setAlignment(Pos.CENTER);
            FontAwesomeIconView closeIcon = new FontAwesomeIconView();
            closeIcon.setGlyphName("TIMES");
            closeIcon.setSize("8");
            removeTagButton.setGraphic(closeIcon);
            configureTagInformation();
            this.participants = new ArrayList<>();
        }
    }

    /**
     * Configures the tag label and remove button
     */
    void configureTagInformation() {
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();
        if(tag != null) {
            tagLabel.setText(tag.getType());
            tagLabel.setBackground(Background.fill(Color.web(tag.getColor())));
            if(Color.web(tag.getColor()).getBrightness() < 0.7) {
                tagLabel.setStyle("-fx-text-fill: white");
            }
            else {
                tagLabel.setStyle("-fx-text-fill: black");
            }
            removeTagButton.setVisible(true);
        }
        else {
            tagLabel.setText(bundle.getString("expenseNoTag"));
            tagLabel.setStyle("-fx-background-color: #F9F9F9");
            removeTagButton.setVisible(false);
        }
    }

    /**
     * Switches the language of the text
     */

    public void switchTextLanguage(){
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();


        expenseField.setText(bundle.getString("addExpenseTitle"));
        addExpenseHow.setText(bundle.getString("addExpenseHow"));
        addExpenseForWhat.setText(bundle.getString("addExpenseForWhat"));
        addExpenseHowMuch.setText(bundle.getString("addExpenseHowMuch"));
        addExpenseType.setText(bundle.getString("addExpenseType"));
        addExpenseWhen.setText(bundle.getString("addExpenseWhen"));
        addExpenseWhoPaid.setText(bundle.getString("addExpenseWhoPaid"));
        expenseAddButton.setText(bundle.getString("expenseAddButton"));
        expenseAbortButton.setText(bundle.getString("expenseAbortButton"));
        onlySome.setText(bundle.getString("addExpenseOnlySome"));
        equally.setText(bundle.getString("addExpenseEqually"));
        removeTagButton.setText(bundle.getString("expenseTagButton"));
        tagButton.setText(bundle.getString("expenseTagButton"));


    }

    //For testing

    /**
     * Getter for participants
     * @return participants
     */
    public List<Participant> getParticipants() {
        return participants;
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
        if(!testing && url == null){
            throw new RuntimeException("Invalid icon name");
        }

        Image image = null;

        if(!testing){
            image = new Image(String.valueOf(url));
        }

        ImageView imageView = new ImageView();

        if(!testing){
            imageView.setImage(image);
        }
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        button.setGraphic(imageView);
    }

    /**
     * Setter for testing
     * @param testing - the testing value
     */
    public void setTesting(boolean testing) {
        this.testing = testing;
    }
}
