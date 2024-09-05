package client.scenes;

import client.utils.ConfigClient;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

public class EventOverviewCtrlTest extends ApplicationTest {
    ServerUtils mockServer;
    ConfigClient mockConfig;
    MainCtrl mockMainCtrl;
    Event mockEvent;
    Expense mockExpense;

    Participant mockParticipant;
    Participant mockParticipant2;

    private EventOverviewCtrl eventOverviewCtrl;

    @BeforeAll
    public static void setupSpec() throws Exception {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("java.awt.headless", "true");
    }

    @Override
    public void start(Stage stage) throws Exception{
        mockServer = Mockito.mock(ServerUtils.class);
        mockConfig = Mockito.mock(ConfigClient.class);
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
        mockExpense.setTag(new Tag("Test", "Blue"));
        list.add(mockExpense);

        mockEvent = new Event();
        mockEvent.setTitle("Test");
        mockEvent.setParticipants(new ArrayList<>());
        mockEvent.addParticipant(mockParticipant);
        mockEvent.addParticipant(mockParticipant2);
        mockEvent.setExpenses(list);
        mockEvent.setInviteCode("testInviteCode");

        Mockito.doNothing().when(mockMainCtrl).showInvitation(mockEvent);
        Mockito.doNothing().when(mockMainCtrl).showEditParticipant(mockEvent, mockParticipant);
        Mockito.doNothing().when(mockMainCtrl).showAddParticipant(mockEvent);
        Mockito.when(mockServer.getEvent(Mockito.any(long.class))).thenReturn(mockEvent);
        Mockito.doNothing().when(mockMainCtrl).showAddExpense(mockEvent, mockParticipant);
        Mockito.doNothing().when(mockMainCtrl).showOpenDebts(mockEvent);
        Mockito.doNothing().when(mockMainCtrl).showStartScreen();


        eventOverviewCtrl = new EventOverviewCtrl(mockServer, mockMainCtrl);
        eventOverviewCtrl.setEvent(mockEvent);
        eventOverviewCtrl.setTesting(true);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/EventOverview.fxml"));
        loader.setControllerFactory(type -> {
            if (type == EventOverviewCtrl.class) {
                return eventOverviewCtrl;
            } else {
                throw new RuntimeException("Requested unknown controller type");
            }
        });

        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();

    }

    @Test
    public void testInitialize(){
        Platform.runLater(() -> {
            eventOverviewCtrl.initialize();
        });
    }

    @Test
    public void testOnSendInvites() {
        Platform.runLater(() -> {
            eventOverviewCtrl.onSendInvites(new ActionEvent());
        });
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mockMainCtrl).showInvitation(mockEvent);
    }

    @Test
    public void testOnEditParticipantsClick() {
        Platform.runLater(() -> {
            eventOverviewCtrl.onEditParticipantsClick();
        });
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mockMainCtrl).showEditParticipant(mockEvent, mockParticipant);
    }

    @Test
    public void testOnAddParticipantClick() {
        Platform.runLater(() -> {
            eventOverviewCtrl.onAddParticipantClick();
        });
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mockMainCtrl).showAddParticipant(mockEvent);
    }

//    @Test
//    public void testOnAddExpenseClick() {
//        Platform.runLater(() -> eventOverviewCtrl.onAddExpenseClick());
//        WaitForAsyncUtils.waitForFxEvents();
//
//        Platform.runLater(() -> {
//            Button okButton = lookup(".button").queryAs(Button.class);
//            okButton.fire();
//        });
//        WaitForAsyncUtils.waitForFxEvents();
//
//        Mockito.verify(mockMainCtrl).showAddExpenseWithTag(mockEvent, mockParticipant, null);
//    }

    @Test
    public void testOnSettleDebtsClick() {
        Platform.runLater(() -> {
            eventOverviewCtrl.onSettleDebtsClick();
        });
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mockMainCtrl).showOpenDebts(mockEvent);
    }

    @Test
    public void testOnBackClick() {
        Platform.runLater(() -> {
            eventOverviewCtrl.onBackClick();
        });
        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mockMainCtrl).showStartScreen();
    }

    @Test
    public void testTabPaneFromPersonClick() throws Exception {
        Field participantsMenuField = EventOverviewCtrl.class.getDeclaredField("participantsMenu");
        Field tabPaneFromGridPaneField = EventOverviewCtrl.class.getDeclaredField("tabPaneFromGridPane");

        participantsMenuField.setAccessible(true);
        tabPaneFromGridPaneField.setAccessible(true);

        ChoiceBox participantsMenu = (ChoiceBox) participantsMenuField.get(eventOverviewCtrl);
        GridPane tabPaneFromGridPane = (GridPane) tabPaneFromGridPaneField.get(eventOverviewCtrl);

        Participant selectedParticipant = mockEvent.getParticipants().get(0);
        Platform.runLater(() -> {
            participantsMenu.getSelectionModel().select(selectedParticipant);
        });
        WaitForAsyncUtils.waitForFxEvents();

        Platform.runLater(() -> {
            eventOverviewCtrl.tabPaneFromPersonClick();
        });
        WaitForAsyncUtils.waitForFxEvents();

        int numExpenses = mockEvent.getExpenses().size();
    }

    @Test
    public void testTabPaneAllClick() throws Exception {
        Field tabPaneAllGridPaneField = EventOverviewCtrl.class.getDeclaredField("tabPaneAllGridPane");

        tabPaneAllGridPaneField.setAccessible(true);

        GridPane tabPaneAllGridPane = (GridPane) tabPaneAllGridPaneField.get(eventOverviewCtrl);

        Platform.runLater(() -> {
            eventOverviewCtrl.tabPaneAllClick();
        });
        WaitForAsyncUtils.waitForFxEvents();

        int numExpenses = mockEvent.getExpenses().size();
        assertEquals(numExpenses, tabPaneAllGridPane.getChildren().size() / 3);
    }

    @Test
    public void testEditParticipantEmpty(){
        mockEvent.setParticipants(new ArrayList<>());
        Platform.runLater(() -> {
            eventOverviewCtrl.onEditParticipantsClick();
        });
        WaitForAsyncUtils.waitForFxEvents();

        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });

        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mockMainCtrl).showEventOverview(mockEvent);
    }

    @Test
    public void fromPersonTabNameEmpty(){
        mockEvent.setParticipants(new ArrayList<>());
        Platform.runLater(() -> {
            eventOverviewCtrl.fromPersonTabName();
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void includingPersonTabNameEmpty(){
        mockEvent.setParticipants(new ArrayList<>());
        Platform.runLater(() -> {
            eventOverviewCtrl.tabPaneAllClick();
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testEditTitleAndSaveTitle() {
        Platform.runLater(() -> {
            eventOverviewCtrl.editTitle(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                    true, true, true, true, true, true, null));
        });

        WaitForAsyncUtils.waitForFxEvents();

        TextField titleTextField = lookup(".text-field").queryAs(TextField.class);

        assertNotNull(titleTextField);
        assertTrue(titleTextField.isVisible());

        Platform.runLater(() -> {
            titleTextField.setText("New Title");
            titleTextField.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER, false, false, false, false));
        });

        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("New Title", mockEvent.getTitle());

        Label eventTitleLabel = lookup(".label").queryAs(Label.class);
        assertEquals("New Title", eventTitleLabel.getText());
    }

    @Test
    public void testKeyPressed_W_WithControl() {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.W, false, true, false, false);
        WaitForAsyncUtils.asyncFx(() -> eventOverviewCtrl.keyPressed(keyEvent));
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mockMainCtrl, Mockito.times(1)).closeWindow();
    }

    @Test
    public void testTabPaneIncludingPersonClick() throws Exception {
        Field participantsMenuField = EventOverviewCtrl.class.getDeclaredField("participantsMenu");
        Field tabPaneIncludingGridPaneField = EventOverviewCtrl.class.getDeclaredField("tabPaneIncludingGridPane");

        participantsMenuField.setAccessible(true);
        tabPaneIncludingGridPaneField.setAccessible(true);

        ChoiceBox participantsMenu = (ChoiceBox) participantsMenuField.get(eventOverviewCtrl);
        GridPane tabPaneIncludingGridPane = (GridPane) tabPaneIncludingGridPaneField.get(eventOverviewCtrl);

        Participant selectedParticipant = mockEvent.getParticipants().get(0);
        Platform.runLater(() -> {
            participantsMenu.getSelectionModel().select(selectedParticipant);
        });
        WaitForAsyncUtils.waitForFxEvents();

        Platform.runLater(() -> {
            eventOverviewCtrl.tabPaneIncludingPersonClick();
        });
        WaitForAsyncUtils.waitForFxEvents();

        int numExpenses = (int) mockEvent.getExpenses().stream()
                .filter(expense -> expense.getParticipants().contains(selectedParticipant))
                .count();
    }

    @Test
    public void testConvertCurrency(){
        Platform.runLater(() -> {
            try {
                eventOverviewCtrl.convertCurrency(mockExpense);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testForeignCurrency() throws URISyntaxException {
        mockExpense.setCurrency("USD");
        ConfigClient mockConfig = new ConfigClient();
        mockConfig.setCurrency("EUR");

        Expense convertedExpense = eventOverviewCtrl.foreignCurrency(mockExpense);

        assertEquals(ConfigClient.getCurrency(), convertedExpense.getCurrency());

        double expectedAmount = mockExpense.getAmount() * mockServer.convertRate(mockExpense.getDateTime(), mockExpense.getCurrency(), ConfigClient.getCurrency());
        assertEquals(expectedAmount, convertedExpense.getAmount(), 0.01);

        assertEquals(mockExpense.getTitle(), convertedExpense.getTitle());
        assertEquals(mockExpense.getPayingParticipant(), convertedExpense.getPayingParticipant());
    }

    @Test
    public void changeTitleEmpty(){
        Platform.runLater(() -> {
            eventOverviewCtrl.editTitle(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                    true, true, true, true, true, true, null));
        });

        WaitForAsyncUtils.waitForFxEvents();

        TextField titleTextField = lookup(".text-field").queryAs(TextField.class);

        assertNotNull(titleTextField);
        assertTrue(titleTextField.isVisible());

        Platform.runLater(() -> {
            titleTextField.setText("");
            titleTextField.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER, false, false, false, false));
        });

        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testGetTagLabel() {
        String existingTagName = "Existing Tag";
        String existingTagColor = "#0000FF";
        Tag existingTag = new Tag(existingTagName, existingTagColor);

        List<Expense> expenses = new ArrayList<>();
        Expense expense = new Expense();
        expense.setTag(existingTag);
        expenses.add(expense);

        Event event = new Event();
        event.setExpenses(expenses);

        Platform.runLater(() -> {
            eventOverviewCtrl.setEvent(event);
        });
        WaitForAsyncUtils.waitForFxEvents();

        String finalExistingTagColor = existingTagColor;
        Platform.runLater(() -> {
            Label tagLabel = eventOverviewCtrl.getTagLabel(0);

            assertEquals(existingTagName, tagLabel.getText());
            assertEquals(Color.web(finalExistingTagColor), ((Color) ((BackgroundFill) tagLabel.getBackground().getFills().get(0)).getFill()));
        });
        WaitForAsyncUtils.waitForFxEvents();

        existingTagColor = "#FFFFFF";
        existingTag.setColor(existingTagColor);
        expense.setTag(existingTag);

        Platform.runLater(() -> {
            eventOverviewCtrl.setEvent(event);
        });
        WaitForAsyncUtils.waitForFxEvents();

        String finalExistingTagColor1 = existingTagColor;
        Platform.runLater(() -> {
            Label tagLabel = eventOverviewCtrl.getTagLabel(0);

            assertEquals(existingTagName, tagLabel.getText());
            assertEquals(Color.web(finalExistingTagColor1), ((Color) ((BackgroundFill) tagLabel.getBackground().getFills().get(0)).getFill()));
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testOnAddExpenseClick() {
        Event event = new Event();
        event.setParticipants(new ArrayList<>());
        event.addParticipant(mockParticipant);

        Platform.runLater(() -> {
            eventOverviewCtrl.setEvent(event);
            eventOverviewCtrl.onAddExpenseClick();
        });
        WaitForAsyncUtils.waitForFxEvents();

        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });
        WaitForAsyncUtils.waitForFxEvents();

        Mockito.verify(mockMainCtrl, Mockito.times(1)).showEventOverview(event);

        event.addParticipant(mockParticipant2);

        Platform.runLater(() -> {
            eventOverviewCtrl.setEvent(event);
            eventOverviewCtrl.onAddExpenseClick();
        });
        WaitForAsyncUtils.waitForFxEvents();

        Mockito.verify(mockMainCtrl, Mockito.times(1)).showAddExpenseWithTag(event, mockParticipant, null);
    }

    @Test
    public void testOnStatisticsOpen() {
        Event event = new Event();
        event.setExpenses(new ArrayList<>());

        Platform.runLater(() -> {
            eventOverviewCtrl.setEvent(event);
            eventOverviewCtrl.onStatisticsOpen();
        });
        WaitForAsyncUtils.waitForFxEvents();

        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });
        WaitForAsyncUtils.waitForFxEvents();

        List<Expense> expenses = new ArrayList<>();
        expenses.add(mockExpense);
        event.setExpenses(expenses);

        Platform.runLater(() -> {
            eventOverviewCtrl.setEvent(event);
            eventOverviewCtrl.onStatisticsOpen();
        });
        WaitForAsyncUtils.waitForFxEvents();

        Mockito.verify(mockMainCtrl, Mockito.times(1)).showStatistics(event);
    }

}
