package client.scenes;

import client.utils.ServerUtils;
import commons.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EditExpenseCtrlTest extends ApplicationTest {

    ServerUtils serverMock;
    MainCtrl mainCtrlMock;
    Event mockEvent;
    Participant mockParticipant;
    Participant mockParticipant2;

    Expense mockExpense;
    private EditExpenseCtrl editExpenseCtrl;

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
        serverMock = Mockito.mock(ServerUtils.class);
        mainCtrlMock = Mockito.mock(MainCtrl.class);

        mockParticipant = new Participant();
        mockParticipant.setName("Test");
        mockParticipant.setEmail("yes");
        mockParticipant.setIban("Test");
        mockParticipant.setBic("Test");

        mockParticipant2 = new Participant();
        mockParticipant2.setName("Test2");
        mockParticipant2.setEmail("yes2");
        mockParticipant2.setIban("Test2");
        mockParticipant2.setBic("Test2");

        mockExpense = new Expense();
        mockExpense.setTitle("Test");
        mockExpense.setPayingParticipant(mockParticipant);
        mockExpense.setParticipants(new ArrayList<>());
        mockExpense.addParticipant(mockParticipant);
        mockExpense.addParticipant(mockParticipant2);
        mockExpense.setAmount(10);
        mockExpense.setCurrency("EUR");
        mockExpense.setDateTime("2020-04-01");


        mockEvent = new Event();
        mockEvent.setTitle("Test");
        mockEvent.setParticipants(new ArrayList<>());
        mockEvent.addParticipant(mockParticipant);
        mockEvent.addParticipant(mockParticipant2);
        mockEvent.setExpenses(new ArrayList<>());
        mockEvent.addExpense(mockExpense);
        mockEvent.setInviteCode("testInviteCode");

        editExpenseCtrl = new EditExpenseCtrl(serverMock, mainCtrlMock);
        editExpenseCtrl.setEvent(mockEvent);
        editExpenseCtrl.setExpense(mockExpense);

        Mockito.doNothing().when(mainCtrlMock).showEventOverview(Mockito.any(Event.class));
        Mockito.when(serverMock.persistEvent(Mockito.any(Event.class))).thenReturn(mockEvent);
        Mockito.when(serverMock.getEvent(Mockito.anyLong())).thenReturn(mockEvent);
        Mockito.when(serverMock.convertRate(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1.0);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/EditExpense.fxml"));
        loader.setControllerFactory(type -> {
            if (type == EditExpenseCtrl.class) {
                return editExpenseCtrl;
            } else {
                return null;
            }
        });

        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();

    }

    @Test
    public void testOnAbortClick() {
        clickOn("#expenseAbortButton");
        Mockito.verify(mainCtrlMock).showEventOverview(Mockito.any(Event.class));
    }

    @Test
    public void testOnSaveClick() {
        interact(() -> {
            TextField titleField = lookup("#titleField").queryAs(TextField.class);
            titleField.setText("Test");

            TextField amountField = lookup("#amountField").queryAs(TextField.class);
            amountField.setText("10");

            ChoiceBox payerChoiceBox = lookup("#currChoiceBox").queryAs(ChoiceBox.class);
            payerChoiceBox.getSelectionModel().select(0);

            DatePicker datePicker = lookup("#datePicker").queryAs(DatePicker.class);
            datePicker.setValue(LocalDate.of(2020, 4, 1));

            RadioButton equally = lookup("#equally").queryAs(RadioButton.class);
            equally.setSelected(true);

            Button expenseSaveButton = lookup("#expenseSaveButton").queryAs(Button.class);
            expenseSaveButton.fire();

            Mockito.verify(serverMock).persistEvent(Mockito.any(Event.class));
            Mockito.verify(mainCtrlMock).showEventOverview(Mockito.any(Event.class));
        });
    }

    @Test
    public void testOnSaveClickOnlySome() {
        interact(() -> {
            TextField titleField = lookup("#titleField").queryAs(TextField.class);
            titleField.setText("Test");

            TextField amountField = lookup("#amountField").queryAs(TextField.class);
            amountField.setText("10");

            ChoiceBox payerChoiceBox = lookup("#currChoiceBox").queryAs(ChoiceBox.class);
            payerChoiceBox.getSelectionModel().select(0);

            DatePicker datePicker = lookup("#datePicker").queryAs(DatePicker.class);
            datePicker.setValue(LocalDate.of(2020, 4, 1));

            RadioButton onlySome = lookup("#onlySome").queryAs(RadioButton.class);
            onlySome.setSelected(true);
            onlySome.getOnAction().handle(new ActionEvent());

            Button expenseSaveButton = lookup("#expenseSaveButton").queryAs(Button.class);
            expenseSaveButton.fire();

            Mockito.verify(serverMock).persistEvent(Mockito.any(Event.class));
            Mockito.verify(mainCtrlMock).showEventOverview(Mockito.any(Event.class));
        });
    }

    @Test
    public void testOnDeleteClick() {
        Platform.runLater(() -> {
            editExpenseCtrl.onDeleteClick(new ActionEvent());
        });
        WaitForAsyncUtils.waitForFxEvents();
        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testKeyPressed() {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.W, false, true, false, false);
        interact(() -> editExpenseCtrl.keyPressed(keyEvent));
        Mockito.verify(mainCtrlMock).closeWindow();
        WaitForAsyncUtils.waitForFxEvents();

        KeyEvent keyEvent2 = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ESCAPE, false, false, false, false);
        interact(() -> editExpenseCtrl.keyPressed(keyEvent2));
        Mockito.verify(mainCtrlMock).showEventOverview(Mockito.any(Event.class));
        WaitForAsyncUtils.waitForFxEvents();

        KeyEvent keyEvent3 = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ESCAPE, false, false, false, false);
        interact(() -> editExpenseCtrl.keyPressed(keyEvent3));
        Mockito.verify(mainCtrlMock, Mockito.times(2)).showEventOverview(Mockito.any(Event.class));
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.reset(mainCtrlMock);



        TextField titleField = lookup("#titleField").queryAs(TextField.class);
        clickOn(titleField);

        KeyEvent keyEvent4 = new KeyEvent(titleField, titleField, KeyEvent.KEY_PRESSED, "", "", KeyCode.TAB, false, false, false, false);
        WaitForAsyncUtils.asyncFx(() -> editExpenseCtrl.keyPressed(keyEvent4));
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.reset(mainCtrlMock);


    }

    @Test
    public void testAddRemoveParticipant() {
        Participant participant = new Participant();
        participant.setName("Test Participant");
        List<Participant> list = new ArrayList<>();
        list.add(participant);

        editExpenseCtrl.setParticipants(list);

        interact(() -> editExpenseCtrl.addRemoveParticipant(participant));

        assertFalse(editExpenseCtrl.getParticipants().contains(participant));

        interact(() -> editExpenseCtrl.addRemoveParticipant(participant));

        assertTrue(editExpenseCtrl.getParticipants().contains(participant));

        editExpenseCtrl.setParticipants(list);
        editExpenseCtrl.setCurrency("EUR");
        editExpenseCtrl.setExpense(new Expense());
    }

    @Test
    public void testSetIconWithInvalidIconName() {
        Button button = new Button();

        assertThrows(RuntimeException.class, () -> {
            editExpenseCtrl.setIcon("invalid_icon_name", button);
        });
    }

    @Test
    public void testSetIconWithValidIconName() {
        editExpenseCtrl.setTesting(true);
        Platform.runLater(() -> {
            editExpenseCtrl.setAllIcons();
        });
    }

    @Test
    public void testConfigureTagElements() {
        Tag mockTag = new Tag();
        mockTag.setType("Test");
        mockTag.setColor("#FFFFFF");

        Platform.runLater(() -> {
            editExpenseCtrl.setTag(mockTag);
            editExpenseCtrl.configureTagElements();
        });

        WaitForAsyncUtils.waitForFxEvents();

        Label tagLabel = lookup("#tagLabel").queryAs(Label.class);
        assertEquals("Test", tagLabel.getText());

        Button removeTagButton = lookup("#removeTagButton").queryAs(Button.class);
        assertTrue(removeTagButton.isVisible());
    }

    @Test
    public void testSaveDebts() {
        Expense mockExpense = new Expense();
        mockExpense.setPayingParticipant(mockParticipant);
        mockExpense.setParticipants(new ArrayList<>());
        mockExpense.addParticipant(mockParticipant);
        mockExpense.addParticipant(mockParticipant2);
        mockExpense.setAmount(10);

        editExpenseCtrl.saveDebts(mockExpense);
    }

    @Test
    public void testOnTagRemove() {
        Tag mockTag = new Tag();
        mockTag.setType("Test");
        mockTag.setColor("#FFFFFF");

        Platform.runLater(() -> {
            editExpenseCtrl.setTag(mockTag);
            editExpenseCtrl.onTagRemove();
        });

        WaitForAsyncUtils.waitForFxEvents();

        Label tagLabel = lookup("#tagLabel").queryAs(Label.class);
        assertEquals("No tag", tagLabel.getText());

        Button removeTagButton = lookup("#removeTagButton").queryAs(Button.class);
        assertFalse(removeTagButton.isVisible());
    }

    @Test
    public void testOnTagsClick() {
        Event mockEvent = new Event();
        Expense mockExpense = new Expense();
        Tag mockTag = new Tag();

        Platform.runLater(() -> {
            editExpenseCtrl.setEvent(mockEvent);
            editExpenseCtrl.setExpense(mockExpense);
            editExpenseCtrl.setTag(mockTag);
            editExpenseCtrl.onTagsClick(null);
        });

        WaitForAsyncUtils.waitForFxEvents();

        Mockito.verify(mainCtrlMock).showTags(mockEvent, mockExpense, null, false, mockTag);
    }

    @Test
    public void testSaveAsEuro() {
        TextField amountField = lookup("#amountField").queryAs(TextField.class);
        ChoiceBox currChoiceBox = lookup("#currChoiceBox").queryAs(ChoiceBox.class);
        DatePicker datePicker = lookup("#datePicker").queryAs(DatePicker.class);

        interact(() -> {
            amountField.setText("10");
            currChoiceBox.getSelectionModel().select("USD");
            datePicker.setValue(LocalDate.of(2020, 4, 1));
        });

        Platform.runLater(() -> {
            editExpenseCtrl.setTesting(true);
            try {
                editExpenseCtrl.saveAsEuro();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(10.0, mockExpense.getAmount());
        assertEquals("EUR", mockExpense.getCurrency());
    }

    @Test
    public void testOnSaveClickWithInvalidAmount() {
        TextField amountField = lookup("#amountField").queryAs(TextField.class);
        interact(() -> {
            amountField.setText("invalid_amount");
        });

        Platform.runLater(() -> {
            editExpenseCtrl.onSaveClick(null);
        });

        WaitForAsyncUtils.waitForFxEvents();

        DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        interact(() -> okButton.fire());

        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testOnSaveClickWithNoParticipants() {
        TextField amountField = lookup("#amountField").queryAs(TextField.class);
        RadioButton onlySome = lookup("#onlySome").queryAs(RadioButton.class);
        ChoiceBox currChoiceBox = lookup("#currChoiceBox").queryAs(ChoiceBox.class);
        DatePicker datePicker = lookup("#datePicker").queryAs(DatePicker.class);

        interact(() -> {
            editExpenseCtrl.setTesting(true);
            amountField.setText("10");
            onlySome.setSelected(true);
            currChoiceBox.getSelectionModel().select("USD");
            datePicker.setValue(LocalDate.of(2020, 4, 1));
            editExpenseCtrl.setParticipants(new ArrayList<>());
        });

        Platform.runLater(() -> {
            editExpenseCtrl.onSaveClick(null);
        });

        WaitForAsyncUtils.waitForFxEvents();

        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();
    }
}