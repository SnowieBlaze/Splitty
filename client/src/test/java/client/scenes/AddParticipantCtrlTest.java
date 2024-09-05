package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.ArrayList;



public class AddParticipantCtrlTest extends ApplicationTest {

    ServerUtils serverMock;

    MainCtrl mainCtrlMock;
    Event mockEvent;

    private AddParticipantCtrl addParticipantCtrl;

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
        serverMock = Mockito.mock(ServerUtils.class);
        mainCtrlMock = Mockito.mock(MainCtrl.class);
        mockEvent = new Event();
        mockEvent.setTitle("Test");
        mockEvent.setParticipants(new ArrayList<>());
        mockEvent.setExpenses(new ArrayList<>());
        mockEvent.setInviteCode("testInviteCode");
        addParticipantCtrl = new AddParticipantCtrl(serverMock, mainCtrlMock);
        addParticipantCtrl.setEvent(mockEvent);

        Mockito.doNothing().when(mainCtrlMock).showEventOverview(mockEvent);
        Mockito.when(serverMock.getEvent(Mockito.anyLong())).thenReturn(mockEvent);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/AddParticipant.fxml"));
        loader.setControllerFactory(type -> {
            if (type == AddParticipantCtrl.class) {
                return addParticipantCtrl;
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
    public void testCancel() {
        clickOn("#participantCancelButton");
        Mockito.verify(mainCtrlMock).showEventOverview(Mockito.any(Event.class));
    }

    @Test
    public void testOk() {

        clickOn("#nameField").write("Test");
        clickOn("#emailField").write("Test");
        clickOn("#ibanField").write("Test");
        clickOn("#bicField").write("Test");

        clickOn("#participantOkButton");
        Mockito.verify(serverMock).persistEvent(Mockito.any(Event.class));
        Mockito.verify(mainCtrlMock).showEventOverview(Mockito.any(Event.class));
    }
    @Test
    public void testInitialize(){
        addParticipantCtrl.initialize();
    }

    @Test
    public void testKeyPressed() {
        press(KeyCode.CONTROL).press(KeyCode.W).release(KeyCode.W).release(KeyCode.CONTROL);
        Mockito.verify(mainCtrlMock, Mockito.times(2)).closeWindow();

        Mockito.reset(mainCtrlMock);

        press(KeyCode.CONTROL).press(KeyCode.S).release(KeyCode.S).release(KeyCode.CONTROL);
        Mockito.verify(mainCtrlMock).showEventOverview(Mockito.any(Event.class));

        Mockito.reset(mainCtrlMock);

        press(KeyCode.ESCAPE);
        Mockito.verify(mainCtrlMock, Mockito.times(2)).showEventOverview(null);

        Mockito.reset(mainCtrlMock);

        clickOn("#nameField");
        press(KeyCode.TAB);
    }
}
