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
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditExpenseCtrl{

    private boolean testing;

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;
    private Expense expense;
    private String currency;
    private List<Participant> participants;
    private Participant participant;
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
    private Button expenseSaveButton;
    @FXML
    private Button expenseDeleteButton;
    @FXML
    private Button expenseAbortButton;
    @FXML
    private Button tagButton;
    @FXML
    private Label editExpenseHowMuch;
    @FXML
    private Label editExpenseForWhat;
    @FXML
    private Label editExpenseWhoPaid;
    @FXML
    private Label editExpenseWhen;
    @FXML
    private Label editExpenseHow;
    @FXML
    private Label editExpenseType;
    @FXML
    private Button removeTagButton;

    /**
     * Constructor for EditExpenseCtrl
     * @param server client is on
     * @param mainCtrl of client
     */
    @Inject
    public EditExpenseCtrl(ServerUtils server, MainCtrl mainCtrl) {
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
        event = server.persistEvent(event);
        mainCtrl.showEventOverview(event);
    }

    /**
     * Opens the tag scene
     * when the tag button is clicked
     * @param actionEvent -
     */
    public void onTagsClick(ActionEvent actionEvent) {
        mainCtrl.showTags(event, expense, payerChoiceBox.getValue(), false, tag);
    }

    /**
     * removes the tag from the expense
     */
    public void onTagRemove() {
        tag = null;
        tagLabel.setText(languageResourceBundle.getResourceBundle().getString("expenseNoTag"));
        tagLabel.setStyle("-fx-background-color: #F9F9F9");
        removeTagButton.setVisible(false);
    }

    /**
     * Controller class for the ok button
     * Sends back to Overview Event window back with the participant altered
     * @param actionEvent to handle
     */
    public void onSaveClick(ActionEvent actionEvent) {
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();
        Event undoEvent = event;
        expense.setTitle(titleField.getText());
        expense.setPayingParticipant(payerChoiceBox.getSelectionModel().getSelectedItem());
        try {
            expense.setAmount(Double.parseDouble(amountField.getText()));
            expense.setCurrency(currChoiceBox.getSelectionModel().getSelectedItem().toString());
            saveAsEuro();
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
            else{
                expense.setParticipants(participants);}
        }
        expense.setDateTime(datePicker.getValue().toString());
        saveDebts(expense);
        expense.setTag(tag);
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
     * @param expense
     */
    public void saveDebts(Expense expense){
        for(Debt debt : expense.getDebts()) {
            server.deleteDebt(debt);
        }
        expense.setDebts(new ArrayList<>());
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
     */

    public void saveAsEuro() throws Exception {
        String availableDates = LocalDate.now().toString();
        int y = Integer.parseInt(availableDates.substring(0,4)) - 1;
        availableDates = y + availableDates.substring(4);
        if(expense.getCurrency().equals("EUR") || expense.getDateTime() == null ||
            expense.getDateTime().compareTo(availableDates) <= 0){
            return;
        }
        Double res = expense.getAmount();
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
     * Controller class for the ok button
     * Sends back to Overview Event window back with the participant altered
     * @param actionEvent to handle
     */
    public void onDeleteClick(ActionEvent actionEvent) {
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();
        if (expense != null){
            Alert alert =  new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(bundle.getString("expenseRemoveAlertTitle"));
            alert.setHeaderText(bundle.getString("expenseRemoveAlertHeader"));
            alert.setContentText(bundle.getString("expenseRemoveAlertContent") +
                    this.expense.getTitle() + "?");


            for(Debt debt : expense.getDebts()) {
                server.deleteDebt(debt);
            }

            if (alert.showAndWait().get() == ButtonType.OK){
                Event undoEvent = event;
                event.removeExpense(expense);
                server.deleteExpense(event.getId(), expense);
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
     * Clear all fields for the next use
     */
    private void clearFields() {
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
            onSaveClick(null);
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
                if(expense.getParticipants().contains(p)){
                    hasParticipated.setSelected(true);
                }
                else{
                    hasParticipated.setSelected(false);
                }
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
     *  converted currency
     * @return converted amount
     */
    public Double convertCurrency() throws Exception {
        String currency = ConfigClient.getCurrency();
        Double res = expense.getAmount();
        try {
            res *= server.convertRate(expense.getDateTime(), expense.getCurrency(), currency);
            DecimalFormat df = new DecimalFormat("#.##");
            res = Double.valueOf(df.format(res));
        }
        catch (Exception e){
            res = expense.getAmount();
        }
        return res;
    }

    /**
     * Initiallizes the payer choice box with the data
     */
    public void initializePayer() {
        if (event != null) {
            payerChoiceBox.setItems(FXCollections.observableArrayList(event.getParticipants()));
            payerChoiceBox.setConverter(new StringConverter<Participant>() {
                @Override
                public String toString(Participant participant) {
                    if (participant != null) {
                        return participant.getName();
                    } else {
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
            });
            payerChoiceBox.setValue(participant);
        }
    }

    /**
     * Initiallizes the currency choice box with the data
     */
    public void initializeCurr() {
        currency = ConfigClient.getCurrency();
        List<String> currencies = new ArrayList<>();
        currencies.add("EUR");
        currencies.add("USD");
        currencies.add("CHF");
        currencies.add("AUD");
        currChoiceBox.setItems(FXCollections.observableArrayList(currencies));
        try{
            if(currency != null &&
                    expense.getCurrency() != currency) {
                amountField.setText("" + convertCurrency());
                currChoiceBox.getSelectionModel().select(currency);
            }
            else{
                amountField.setText("" + expense.getAmount());
                currChoiceBox.getSelectionModel().select(expense.getCurrency());
            }
        }
        catch (Exception e){
            amountField.setText("" + expense.getAmount());
            currChoiceBox.getSelectionModel().select(expense.getCurrency());
            System.out.println("The API currency converter we are using has 100" +
                    " calls/minute and can find historical currency conversion up" +
                    " to one year.");
        }
    }


    /**
     * Initiallizes the fields with the data
     */
    public void initialize() {
        if(event != null){
            languageResourceBundle = LanguageResourceBundle.getInstance();
            switchTextLanguage();
            initializePayer();
            titleField.setText(expense.getTitle());
            initializeCurr();
            if(expense.getDateTime() != null){
                datePicker.setValue(LocalDate.parse(expense.getDateTime()));
            }
            List<Participant> edited = new ArrayList<>();
            for(Participant p: expense.getParticipants()){
                edited.add(p);
            }
            setParticipants(edited);
            if(event.getParticipants().equals(expense.getParticipants())){
                equally.setSelected(true);
                onlySome.setSelected(false);
            }
            else{
                equally.setSelected(false);
                onlySome.setSelected(true);
            }
            configureTagElements();
            onlySomeChecked();
        }
    }

    /**
     * Sets all the icons to their respective buttons
     */
    public void setAllIcons() {
        setIcon("trashicon.png", expenseDeleteButton);
        setIcon("saveicon.png", expenseSaveButton);
        setIcon("aborticon.png", expenseAbortButton);
        setIcon("tagicon.png", tagButton);
    }

    /**
     * Configures the tag label and remove button
     */
    void configureTagElements() {
        tagLabel.setMinHeight(20);
        tagLabel.setMinWidth(40);
        tagLabel.setAlignment(Pos.CENTER);
        FontAwesomeIconView closeIcon = new FontAwesomeIconView();
        closeIcon.setGlyphName("TIMES");
        closeIcon.setSize("8");
        removeTagButton.setGraphic(closeIcon);
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
            tagLabel.setText(languageResourceBundle.getResourceBundle().getString("expenseNoTag"));
            tagLabel.setStyle("-fx-background-color: #F9F9F9");
            removeTagButton.setVisible(false);
        }
    }

    /**
     * Switches the language of the text
     */

    public void switchTextLanguage(){
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();

        expenseField.setText(bundle.getString("editExpenseTitle"));
        editExpenseHowMuch.setText(bundle.getString("editExpenseHowMuch"));
        editExpenseForWhat.setText(bundle.getString("editExpenseForWhat"));
        editExpenseWhoPaid.setText(bundle.getString("editExpenseWhoPaid"));
        editExpenseWhen.setText(bundle.getString("editExpenseWhen"));
        editExpenseHow.setText(bundle.getString("editExpenseHow"));
        editExpenseType.setText(bundle.getString("editExpenseType"));
        expenseSaveButton.setText(bundle.getString("expenseSaveButton"));
        expenseDeleteButton.setText(bundle.getString("expenseDeleteButton"));
        expenseAbortButton.setText(bundle.getString("expenseAbortButton"));
        equally.setText(bundle.getString("editExpenseEqually"));
        onlySome.setText(bundle.getString("editExpenseOnlySome"));
        tagButton.setText(bundle.getString("expenseTagButton"));
    }

    //For testing

    /**
     * Getter for participant list
     * @return participants
     */
    public List<Participant> getParticipants() {
        return participants;
    }

    /**
     * Set testing
     * @param testing - testing
     */
    public void setTesting(boolean testing) {
        this.testing = testing;
    }
}
