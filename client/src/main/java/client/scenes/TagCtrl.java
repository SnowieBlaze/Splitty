package client.scenes;

import client.utils.LanguageButtonUtils;
import client.utils.LanguageResourceBundle;
import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TagCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private LanguageResourceBundle languageResourceBundle;
    private Event event;
    private Expense expense;
    private Participant participant;
    private boolean isAddExpense;
    private List<Tag> tags;
    private Tag tagOnFocus;

    @FXML
    private Label tagLabel;
    @FXML
    private ChoiceBox<Tag> tagMenu;
    @FXML
    private Label nameLabel;
    @FXML
    private TextField nameTextField;
    @FXML
    private Label colorLabel;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Button selectButton;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button backButton;

    /**
     * Constructor for the controller
     * with injected server and mainCtrl
     * @param server - the server
     * @param mainCtrl - the mainCtrl
     */
    @Inject
    public TagCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Sets the tag on focus
     * @param tagOnFocus - the tag to be set on focus
     */
    public void setTagOnFocus(Tag tagOnFocus) {
        this.tagOnFocus = tagOnFocus;
    }

    /**
     * Initializes the addEditTag scene
     */
    public void initialize() {
        if(event != null) {
            languageResourceBundle = LanguageResourceBundle.getInstance();
            switchTextLanguage();
            tags = server.getTags();
            if(tagOnFocus == null && !tags.isEmpty()) {
                tagOnFocus = tags.getFirst();
            }
            tagMenu.setItems(FXCollections.observableArrayList(tags));
            tagMenu.setConverter(new StringConverter<Tag>() {
                @Override
                public String toString(Tag tag) {
                    if (tag != null) {
                        return tag.getType();
                    }
                    else {
                        return "";
                    }
                }
                @Override
                public Tag fromString(String s) {
                    return null;
                }
            });
            tagMenu.setValue(tagOnFocus);
            if(tagOnFocus != null) {
                nameTextField.setText(tagOnFocus.getType());
                colorPicker.setValue(Color.web(tagOnFocus.getColor()));
            }
            else {
                nameTextField.setText("");
                colorPicker.setValue(Color.web("#FFFFFF"));
            }
            tagMenu.setOnAction(e -> {
                if(tagMenu.getValue() != null) {
                    tagOnFocus = tagMenu.getValue();
                    nameTextField.setText(tagOnFocus.getType());
                    colorPicker.setValue(Color.web(tagOnFocus.getColor()));
                }
            });
        }
    }

    /**
     * Selects the current tag for the expense
     */
    @FXML
    private void onSelectTag() {
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();
        if(tagOnFocus == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(bundle.getString("tagNoTagTitle"));
            alert.setContentText(bundle.getString("tagSelectTagNoTag"));
            // TODO: translate alert
            alert.showAndWait();
            return;
        }
        if(this.isAddExpense) {
            mainCtrl.showAddExpenseWithTag(event, participant, tagOnFocus);
        }
        else {
            mainCtrl.showEditExpenseWithTag(event, expense, tagOnFocus, participant);
        }
    }

    /**
     * Adds a tag to the server and refreshes the tag scene
     */
    @FXML
    private void onAddTag() {
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();
        if(tags.stream().map(Tag::getType)
            .toList().contains(nameTextField.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(bundle.getString("tagAddTagInvalidName"));
            alert.setContentText(bundle.getString("tagAddTagAlreadyExists"));
            // TODO: translate alert
            alert.showAndWait();
            return;
        }
        if(!nameTextField.getText().isEmpty()) {
            tagOnFocus =
                server.addTag(new Tag(nameTextField.getText(), colorPicker.getValue().toString()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("tagAddTagSuccessTitle"));
            alert.setContentText(bundle.getString("tagAddTagSuccess"));
            alert.showAndWait();
            mainCtrl.showTags(event, expense, participant, isAddExpense, tagOnFocus);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(bundle.getString("tagAddTagInvalidName"));
            alert.setContentText(bundle.getString("tagAddTagNoName"));
            alert.showAndWait();
        }
    }

    /**
     * Updates a tag
     */
    @FXML
    private void onEditTag() {
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();
        if(tagOnFocus == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(bundle.getString("tagNoTagTitle"));
            alert.setContentText(bundle.getString("tagNoTagEditContent"));
            alert.showAndWait();
            return;
        }
        if(tags.stream().map(Tag::getType)
            .toList().contains(nameTextField.getText()) &&
            !tagOnFocus.getType().equals(nameTextField.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(bundle.getString("tagAddTagInvalidName"));
            alert.setContentText(bundle.getString("tagAddTagAlreadyExists"));
            alert.showAndWait();
            return;
        }
        if(tagOnFocus.getType().equals(nameTextField.getText()) &&
            tagOnFocus.getColor().equals(colorPicker.getValue().toString())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(bundle.getString("tagNoChangesTitle"));
            alert.setContentText(bundle.getString("tagEditTagNoChanges"));
            alert.showAndWait();
            return;
        }
        if (!nameTextField.getText().isEmpty()) {
            tagOnFocus.setType(nameTextField.getText());
            tagOnFocus.setColor(colorPicker.getValue().toString());
            Tag updatedTag = server.updateTag(tagOnFocus);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("tagAddTagSuccessTitle"));
            alert.setContentText(bundle.getString("tagEditTagSuccess"));
            alert.showAndWait();
            tagOnFocus = updatedTag;
            if(expense != null) {
                expense = server.getExpense(expense.getId());
            }
            mainCtrl.showTags(event, expense, participant, isAddExpense, tagOnFocus);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(bundle.getString("tagAddTagInvalidName"));
            alert.setContentText(bundle.getString("tagAddTagNoName"));
            alert.showAndWait();
        }
    }

    /**
     * Deletes a tag
     */
    @FXML
    private void onDeleteTag() {
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();
        if(tagOnFocus == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(bundle.getString("tagNoTagTitle"));
            alert.setContentText(bundle.getString("tagNoTagDeletionContent"));
            // TODO: translate alert
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(bundle.getString("tagConfirmationDeleteHeader"));
        alert.setContentText(bundle.getString("tagConfirmationDeleteContent"));
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if(expense != null && expense.getTag() != null &&
                    expense.getTag().equals(tagOnFocus)) {
                    expense.setTag(null);
                }
                event = server.persistEvent(event);
                server.deleteTag(tagOnFocus);
                tagOnFocus = null;
                mainCtrl.showTags(event, expense, participant, isAddExpense, tagOnFocus);
            }
        });
    }

    /**
     * Switches the language of the text.
     */
    public void switchTextLanguage() {

        ResourceBundle bundle = languageResourceBundle.getResourceBundle();
        backButton.setText(bundle.getString("backButton"));
        addButton.setText(bundle.getString("tagAddButton"));
        editButton.setText(bundle.getString("tagEditButton"));
        deleteButton.setText(bundle.getString("tagDeleteButton"));
        selectButton.setText(bundle.getString("tagSelectButton"));
        tagLabel.setText(bundle.getString("tagLabel"));
        nameLabel.setText(bundle.getString("tagNameLabel"));
        colorLabel.setText(bundle.getString("tagColorLabel"));

    }

    /**
     * Sets the current expense
     * @param expense - the expense
     */
    public void setExpense(Expense expense){
        this.expense = expense;
    }

    /**
     * Sets the current event
     * @param event - the event
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Sets the participant that is paying for the expense
     * @param participant - the participant
     */
    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    /**
     * Set whether the previous scene was addExpense
     * @param isAddExpense - whether the previous scene
     * was AddExpense
     */
    public void setIsExpenseTrue(boolean isAddExpense) {
        this.isAddExpense = isAddExpense;
    }


    /**
     * Returns to the previous page
     */
    @FXML
    private void backToExpense() {
        if(this.isAddExpense) {
            mainCtrl.showAddExpense(event, participant);
        }
        else {
            mainCtrl.showEditExpense(event, expense, participant);
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

    /**
     * Method to be called when a key is pressed
     * @param e keyevent to listen
     */
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getCode() == KeyCode.W) {  //close window
            mainCtrl.closeWindow();
        }
        switch (e.getCode()) {
            case ESCAPE:
                backToExpense();
                break;
            default:
                break;
        }
    }
}
