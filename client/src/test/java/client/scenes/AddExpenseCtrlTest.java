package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
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

public class AddExpenseCtrlTest extends ApplicationTest {

    ServerUtils serverMock;
    MainCtrl mainCtrlMock;
    Event mockEvent;
    Participant mockParticipant;
    Participant mockParticipant2;
    Expense mockExpense;
    private AddExpenseCtrl addExpenseCtrl;

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
        mockExpense.setAmount(10);
        mockExpense.setCurrency("EUR");
        mockExpense.setParticipants(new ArrayList<>());
        mockExpense.addParticipant(mockParticipant);
        mockExpense.setTag(new Tag("Test", "#0000FF"));


        mockEvent = new Event();
        mockEvent.setTitle("Test");
        mockEvent.setParticipants(new ArrayList<>());
        mockEvent.addParticipant(mockParticipant);
        mockEvent.addParticipant(mockParticipant2);
        mockEvent.setExpenses(new ArrayList<>());
        mockEvent.addExpense(mockExpense);
        mockEvent.setInviteCode("testInviteCode");

        addExpenseCtrl = new AddExpenseCtrl(serverMock, mainCtrlMock);
        addExpenseCtrl.setEvent(mockEvent);

        Mockito.doNothing().when(mainCtrlMock).showEventOverview(Mockito.any(Event.class));
        Mockito.when(serverMock.persistEvent(Mockito.any(Event.class))).thenReturn(mockEvent);
        Mockito.when(serverMock.getEvent(Mockito.anyLong())).thenReturn(mockEvent);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/AddEditExpense.fxml"));
        loader.setControllerFactory(type -> {
            if (type == AddExpenseCtrl.class) {
                return addExpenseCtrl;
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
    public void testOnAddClick() {
        interact(() -> {
            TextField titleField = lookup("#titleField").queryAs(TextField.class);
            titleField.setText("Test");

            TextField amountField = lookup("#amountField").queryAs(TextField.class);
            amountField.setText("10");

            ChoiceBox payerChoiceBox = lookup("#payerChoiceBox").queryAs(ChoiceBox.class);
            payerChoiceBox.getSelectionModel().select(0);

            DatePicker datePicker = lookup("#datePicker").queryAs(DatePicker.class);
            datePicker.setValue(LocalDate.of(2020, 4, 1));

            RadioButton equally = lookup("#equally").queryAs(RadioButton.class);
            equally.setSelected(true);

            Button expenseAddButton = lookup("#expenseAddButton").queryAs(Button.class);
            expenseAddButton.fire();

            Mockito.verify(serverMock).persistEvent(Mockito.any(Event.class));
            Mockito.verify(mainCtrlMock).showEventOverview(Mockito.any(Event.class));
        });
    }

    @Test
    public void testKeyPressed() {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.W, false, true, false, false);
        WaitForAsyncUtils.asyncFx(() -> addExpenseCtrl.keyPressed(keyEvent));
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mainCtrlMock, Mockito.times(1)).closeWindow();

        Mockito.reset(mainCtrlMock);

        KeyEvent keyEvent2 = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.S, false, true, false, false);
        WaitForAsyncUtils.asyncFx(() -> addExpenseCtrl.keyPressed(keyEvent2));
        WaitForAsyncUtils.waitForFxEvents();

        Mockito.reset(mainCtrlMock);

        KeyEvent keyEvent3 = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ESCAPE, false, false, false, false);
        WaitForAsyncUtils.asyncFx(() -> addExpenseCtrl.keyPressed(keyEvent3));
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.reset(mainCtrlMock);

        TextField titleField = lookup("#titleField").queryAs(TextField.class);
        clickOn(titleField);

        KeyEvent keyEvent4 = new KeyEvent(titleField, titleField, KeyEvent.KEY_PRESSED, "", "", KeyCode.TAB, false, false, false, false);
        WaitForAsyncUtils.asyncFx(() -> addExpenseCtrl.keyPressed(keyEvent4));
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.reset(mainCtrlMock);
    }

    @Test
    public void testOnAddClickOnlySome() {
        interact(() -> {
            TextField titleField = lookup("#titleField").queryAs(TextField.class);
            titleField.setText("Test");

            TextField amountField = lookup("#amountField").queryAs(TextField.class);
            amountField.setText("10");

            ChoiceBox payerChoiceBox = lookup("#payerChoiceBox").queryAs(ChoiceBox.class);
            payerChoiceBox.getSelectionModel().select(0);

            DatePicker datePicker = lookup("#datePicker").queryAs(DatePicker.class);
            datePicker.setValue(LocalDate.of(2020, 4, 1));

            RadioButton onlySome = lookup("#onlySome").queryAs(RadioButton.class);
            onlySome.setSelected(true);
            onlySome.getOnAction().handle(new ActionEvent());

            List<Participant> participants = new ArrayList<>();
            participants.add(mockParticipant);
            addExpenseCtrl.setParticipants(participants);

            Button expenseAddButton = lookup("#expenseAddButton").queryAs(Button.class);
            expenseAddButton.fire();

            Mockito.verify(serverMock).persistEvent(Mockito.any(Event.class));
            Mockito.verify(mainCtrlMock).showEventOverview(Mockito.any(Event.class));
        });
    }

    @Test
    public void testAddRemoveParticipant() {
        Participant participant = new Participant();
        participant.setName("Test Participant");
        List<Participant> list = new ArrayList<>();
        list.add(participant);

        addExpenseCtrl.setParticipants(list);

        interact(() -> addExpenseCtrl.addRemoveParticipant(participant));

        assertFalse(addExpenseCtrl.getParticipants().contains(participant));

        interact(() -> addExpenseCtrl.addRemoveParticipant(participant));

        assertTrue(addExpenseCtrl.getParticipants().contains(participant));

        addExpenseCtrl.setParticipant(participant);
        addExpenseCtrl.setCurrency("EUR");
        addExpenseCtrl.setExpense(new Expense());
    }

    @Test
    public void testSetIconWithInvalidIconName() {
        Button button = new Button();

        assertThrows(RuntimeException.class, () -> {
            addExpenseCtrl.setIcon("invalid_icon_name", button);
        });
    }

    @Test
    public void testSetIconWithValidIconName() {
        addExpenseCtrl.setTesting(true);
        Platform.runLater(() -> {
            addExpenseCtrl.setIcon("add.png", new Button());
        });
    }

    @Test
    public void testConfigureTag(){
        Platform.runLater(() -> {
            addExpenseCtrl.setTag(new Tag("Test", "#0000FF"));
            addExpenseCtrl.configureTagInformation();
        });

        addExpenseCtrl.setExpense(new Expense());
        TextField amountField = lookup("#amountField").queryAs(TextField.class);
        ChoiceBox currChoiceBox = lookup("#currChoiceBox").queryAs(ChoiceBox.class);
        DatePicker datePicker = lookup("#datePicker").queryAs(DatePicker.class);

        interact(() -> {
            amountField.setText("10");
            currChoiceBox.getSelectionModel().select("USD");
            datePicker.setValue(LocalDate.of(2020, 4, 1));
        });


        addExpenseCtrl.setTesting(true);

        Platform.runLater(() -> {
            try {
                addExpenseCtrl.saveAsEuro(mockEvent.getExpenses().get(0));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void testTagRemove(){
        Platform.runLater(() -> {
            addExpenseCtrl.onTagRemove();
        });

        Platform.runLater(() -> {
            addExpenseCtrl.onTagsClick(new ActionEvent());
        });
    }
}