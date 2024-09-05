package client.scenes;

import client.utils.ConfigClient;
import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TagCtrlTest extends ApplicationTest {
    ServerUtils mockServer = Mockito.mock(ServerUtils.class);
    MainCtrl mockMainCtrl = Mockito.mock(MainCtrl.class);

    Event mockEvent;
    Participant mockParticipant;
    Participant mockParticipant2;
    Expense mockExpense;


    private TagCtrl tagCtrl;

    @BeforeAll
    public static void setupSpec() throws Exception {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("java.awt.headless", "true");
    }

    @Override
    public void start(Stage stage) throws Exception {
        mockServer = Mockito.mock(ServerUtils.class);
        mockMainCtrl = Mockito.mock(MainCtrl.class);

        List<Expense> list = new ArrayList<>();

        mockParticipant = new Participant();
        mockParticipant.setName("testParticipant");
        mockParticipant.setBic("testBic");
        mockParticipant.setIban("testIban");
        mockParticipant.setEmail("testEmail");

        mockParticipant2 = new Participant();
        mockParticipant2.setName("testParticipant2");
        mockParticipant2.setBic("testBic2");
        mockParticipant2.setIban("testIban2");
        mockParticipant2.setEmail("testEmail2");

        mockExpense = new Expense();
        mockExpense.setTitle("Test");
        mockExpense.setPayingParticipant(mockParticipant);
        mockExpense.setParticipants(new ArrayList<>());
        mockExpense.addParticipant(mockParticipant);
        mockExpense.setAmount(10);
        mockExpense.setCurrency("EUR");
        mockExpense.setDateTime("2020-04-01");
        list.add(mockExpense);

        mockEvent = new Event();
        mockEvent.setTitle("Test");
        mockEvent.setParticipants(new ArrayList<>());
        mockEvent.addParticipant(mockParticipant);
        mockEvent.addParticipant(mockParticipant2);
        mockEvent.setInviteCode("testInviteCode");

        Mockito.when(mockServer.getTags()).thenReturn(new LinkedList<>());

        tagCtrl = new TagCtrl(mockServer, mockMainCtrl);
        tagCtrl.setEvent(mockEvent);
        tagCtrl.setExpense(mockExpense);
        tagCtrl.setParticipant(mockParticipant);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/AddEditTags.fxml"));
        loader.setControllerFactory(type -> {
            if (type == TagCtrl.class) {
                return tagCtrl;
            } else {
                throw new RuntimeException("Requested unknown controller type");
            }
        });

        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();


    }

    @Test
    public void testOnAddTag() {
        String newTagName = "New Tag";
        Color newTagColor = Color.RED;
        Tag newTag = new Tag(newTagName, newTagColor.toString());

        Mockito.when(mockServer.addTag(Mockito.any(Tag.class))).thenReturn(newTag);
        Mockito.doNothing().when(mockMainCtrl).showTags(Mockito.any(Event.class), Mockito.any(Expense.class), Mockito.any(Participant.class), Mockito.anyBoolean(), Mockito.any(Tag.class));

        Platform.runLater(() -> {
            TextField nameTextField = lookup("#nameTextField").queryAs(TextField.class);
            ColorPicker colorPicker = lookup("#colorPicker").queryAs(ColorPicker.class);
            Button addButton = lookup("#addButton").queryAs(Button.class);

            nameTextField.setText(newTagName);
            colorPicker.setValue(newTagColor);
            addButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();

        Mockito.verify(mockServer).addTag(Mockito.any(Tag.class));

        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });
    }
    @Test
    public void testOnAddTagWithEmptyTagName() {
        Platform.runLater(() -> {
            TextField nameTextField = lookup("#nameTextField").queryAs(TextField.class);
            Button addButton = lookup("#addButton").queryAs(Button.class);

            nameTextField.setText("");
            addButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();

        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });
    }

    @Test
    public void testOnEditTagWithNoTagSelected() {
        Platform.runLater(() -> {
            Button editButton = lookup("#editButton").queryAs(Button.class);
            editButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();

        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });
    }

    @Test
    public void testOnEditTagWithExistingTagName() {
        String existingTagName = "Existing Tag";
        Tag existingTag = new Tag(existingTagName, Color.RED.toString());

        List<Tag> existingTags = new LinkedList<>();
        existingTags.add(existingTag);
        Mockito.when(mockServer.getTags()).thenReturn(existingTags);

        Platform.runLater(() -> {
            TextField nameTextField = lookup("#nameTextField").queryAs(TextField.class);
            Button editButton = lookup("#editButton").queryAs(Button.class);

            nameTextField.setText(existingTagName);
            editButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();

        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });
    }

    @Test
    public void testOnEditTagWithDifferentTagOnFocus() {
        String existingTagName = "Existing Tag";
        String newTagName = "New Tag";
        Tag existingTag = new Tag(existingTagName, Color.RED.toString());
        Tag newTag = new Tag(newTagName, Color.BLUE.toString());

        List<Tag> existingTags = new LinkedList<>();
        existingTags.add(existingTag);
        existingTags.add(newTag);
        Mockito.when(mockServer.getTags()).thenReturn(existingTags);

        tagCtrl.setTagOnFocus(existingTag);

        Platform.runLater(() -> {
            TextField nameTextField = lookup("#nameTextField").queryAs(TextField.class);
            Button editButton = lookup("#editButton").queryAs(Button.class);

            nameTextField.setText(newTagName);
            editButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();

        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });
    }

    @Test
    public void testOnEditTagWithNullTagOnFocus() {
        Platform.runLater(() -> {
            Button editButton = lookup("#editButton").queryAs(Button.class);
            editButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();

        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });
    }

    @Test
    public void testOnEditTagWithNoChanges() {
        String existingTagName = "Existing Tag";
        Color existingTagColor = Color.RED;
        Tag existingTag = new Tag(existingTagName, existingTagColor.toString());

        List<Tag> existingTags = new LinkedList<>();
        existingTags.add(existingTag);
        Mockito.when(mockServer.getTags()).thenReturn(existingTags);

        tagCtrl.setTagOnFocus(existingTag);

        Platform.runLater(() -> {
            TextField nameTextField = lookup("#nameTextField").queryAs(TextField.class);
            ColorPicker colorPicker = lookup("#colorPicker").queryAs(ColorPicker.class);
            Button editButton = lookup("#editButton").queryAs(Button.class);

            nameTextField.setText(existingTagName);
            colorPicker.setValue(existingTagColor);
            editButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();

        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });
    }

    @Test
    public void testOnDeleteTagWithNullTagOnFocus() {
        Platform.runLater(() -> {
            Button deleteButton = lookup("#deleteButton").queryAs(Button.class);
            deleteButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();

        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });
    }

    @Test
    public void testOnDeleteTagWithValidTagOnFocus() {
        String existingTagName = "Existing Tag";
        Tag existingTag = new Tag(existingTagName, Color.RED.toString());

        List<Tag> existingTags = new LinkedList<>();
        existingTags.add(existingTag);
        Mockito.when(mockServer.getTags()).thenReturn(existingTags);

        tagCtrl.setTagOnFocus(existingTag);

        Platform.runLater(() -> {
            Button deleteButton = lookup("#deleteButton").queryAs(Button.class);
            deleteButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();

        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });

    }

    @Test
    public void testOnSelectTagWithNullTagOnFocus() {
        // Execute the test
        Platform.runLater(() -> {
            Button selectButton = lookup("#selectButton").queryAs(Button.class);
            selectButton.fire();
        });

        // Wait for JavaFX events to be processed
        WaitForAsyncUtils.waitForFxEvents();

        // Interact with the alert dialog
        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });
    }

    @Test
    public void testOnSelectTagWithValidTagOnFocus() {
        String existingTagName = "Existing Tag";
        Tag existingTag = new Tag(existingTagName, Color.RED.toString());

        List<Tag> existingTags = new LinkedList<>();
        existingTags.add(existingTag);
        Mockito.when(mockServer.getTags()).thenReturn(existingTags);

        tagCtrl.setTagOnFocus(existingTag);

        Platform.runLater(() -> {
            Button selectButton = lookup("#selectButton").queryAs(Button.class);
            selectButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();

        Mockito.verify(mockMainCtrl, Mockito.times(1)).showEditExpenseWithTag(Mockito.any(Event.class), Mockito.any(Expense.class), Mockito.any(Tag.class), Mockito.any(Participant.class));
    }

    @Test
    public void testBackToExpense() {
        tagCtrl.setIsExpenseTrue(true);

        Platform.runLater(() -> {
            Button backButton = lookup("#backButton").queryAs(Button.class);
            backButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();

        Mockito.verify(mockMainCtrl, Mockito.times(1)).showAddExpense(Mockito.any(Event.class), Mockito.any(Participant.class));

        tagCtrl.setIsExpenseTrue(false);

        Platform.runLater(() -> {
            Button backButton = lookup("#backButton").queryAs(Button.class);
            backButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();

        Mockito.verify(mockMainCtrl, Mockito.times(1)).showEditExpense(Mockito.any(Event.class), Mockito.any(Expense.class), Mockito.any(Participant.class));
    }
}