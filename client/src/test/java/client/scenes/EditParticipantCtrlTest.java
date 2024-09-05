package client.scenes;


import client.utils.ServerUtils;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.testfx.util.WaitForAsyncUtils;

public class EditParticipantCtrlTest extends ApplicationTest {
    ServerUtils serverMock;
    MainCtrl mainCtrlMock;

    Event mockEvent;

    Participant participant;

    private EditParticipantCtrl editParticipantCtrl;

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
        participant = new Participant();
        participant.setName("Test");
        participant.setIban("Test");
        participant.setBic("Test");
        participant.setEmail("Test");

        mockEvent = new Event();
        mockEvent.setTitle("Test");

        List<Participant> list = new ArrayList<>();
        list.add(participant);

        Mockito.when(serverMock.getEvent(Mockito.anyLong())).thenReturn(mockEvent);

        mockEvent.setParticipants(list);
        mockEvent.setExpenses(new ArrayList<>());
        mockEvent.setInviteCode("testInviteCode");
        editParticipantCtrl = new EditParticipantCtrl(serverMock, mainCtrlMock);
        editParticipantCtrl.setEvent(mockEvent);
        editParticipantCtrl.setParticipant(participant);

        Mockito.when(serverMock.getEvent(Mockito.anyLong())).thenReturn(mockEvent);
        Mockito.doNothing().when(mainCtrlMock).showEventOverview(mockEvent);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/EditParticipant.fxml"));
        loader.setControllerFactory(type -> {
            if (type == EditParticipantCtrl.class) {
                return editParticipantCtrl;
            } else {
                throw new RuntimeException("Requested unknown controller type");
            }
        });

        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testEditParticipant() {
        clickOn("#nameField").write("Test2");
        clickOn("#emailField").write("Test2");
        clickOn("#ibanField").write("Test2");
        clickOn("#bicField").write("Test2");
        clickOn("#participantOkButton");
        Mockito.verify(serverMock, Mockito.times(1)).persistEvent(Mockito.any(Event.class));

        assert(mockEvent.getParticipants().get(0).getName().equals("TestTest2"));
        assert(mockEvent.getParticipants().get(0).getEmail().equals("TestTest2"));
        assert(mockEvent.getParticipants().get(0).getIban().equals("TestTest2"));
        assert(mockEvent.getParticipants().get(0).getBic().equals("TestTest2"));
    }

    @Test
    public void testCancel() {
        clickOn("#participantCancelButton");
        Mockito.verify(mainCtrlMock).showEventOverview(Mockito.any(Event.class));
    }

    @Test
    public void testRemoveEmptyParticipant(){
        mockEvent.getParticipants().clear();
        editParticipantCtrl.setParticipant(null);
        clickOn("#removeParticipantButton");
        Mockito.verify(serverMock, Mockito.times(0)).persistEvent(Mockito.any(Event.class));
    }

    @Test
    public void testInitialize() throws Exception {
        editParticipantCtrl.initialize();

        Field nameField = EditParticipantCtrl.class.getDeclaredField("nameField");
        nameField.setAccessible(true);
        TextField nameTextField = (TextField) nameField.get(editParticipantCtrl);

        Field emailField = EditParticipantCtrl.class.getDeclaredField("emailField");
        emailField.setAccessible(true);
        TextField emailTextField = (TextField) emailField.get(editParticipantCtrl);

        Field ibanField = EditParticipantCtrl.class.getDeclaredField("ibanField");
        ibanField.setAccessible(true);
        TextField ibanTextField = (TextField) ibanField.get(editParticipantCtrl);

        Field bicField = EditParticipantCtrl.class.getDeclaredField("bicField");
        bicField.setAccessible(true);
        TextField bicTextField = (TextField) bicField.get(editParticipantCtrl);

        assert(nameTextField.getText().equals("Test"));
        assert(emailTextField.getText().equals("Test"));
        assert(ibanTextField.getText().equals("Test"));
        assert(bicTextField.getText().equals("Test"));
    }

    @Test
    public void testEmptyInitialize(){
        editParticipantCtrl.setParticipant(null);
        editParticipantCtrl.initialize();

    }

    @Test
    public void testKeyPressed() {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.W, false, true, false, false);
        WaitForAsyncUtils.asyncFx(() -> editParticipantCtrl.keyPressed(keyEvent));
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mainCtrlMock, Mockito.times(1)).closeWindow();

        Mockito.reset(mainCtrlMock);

        KeyEvent keyEvent2 = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.S, false, true, false, false);
        WaitForAsyncUtils.asyncFx(() -> editParticipantCtrl.keyPressed(keyEvent2));
        WaitForAsyncUtils.waitForFxEvents();

        Mockito.reset(mainCtrlMock);

        KeyEvent keyEvent3 = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ESCAPE, false, false, false, false);
        WaitForAsyncUtils.asyncFx(() -> editParticipantCtrl.keyPressed(keyEvent3));
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.reset(mainCtrlMock);

        TextField nameField = lookup("#nameField").queryAs(TextField.class);
        clickOn(nameField);

        KeyEvent keyEvent4 = new KeyEvent(nameField, nameField, KeyEvent.KEY_PRESSED, "", "", KeyCode.TAB, false, false, false, false);
        WaitForAsyncUtils.asyncFx(() -> editParticipantCtrl.keyPressed(keyEvent4));
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.reset(mainCtrlMock);
    }

    @Test
    public void testRemoveParticipant(){
        Participant participant = new Participant();
        participant.setName("Test1");
        participant.setIban("Test1");
        participant.setBic("Test1");
        participant.setEmail("Test1");
        editParticipantCtrl.setParticipant(participant);

        clickOn("#removeParticipantButton");

        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").query();
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            clickOn(okButton);
        });

        Mockito.verify(serverMock, Mockito.times(1)).persistEvent(mockEvent);
    }
}

