package client.scenes;

import client.utils.ConfigClient;
import client.utils.ServerUtils;
import commons.Event;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;


public class StartScreenCtrlTest extends ApplicationTest {
    ServerUtils mockServer = Mockito.mock(ServerUtils.class);
    ConfigClient mockConfig = new ConfigClient();
    MainCtrl mockMainCtrl = Mockito.mock(MainCtrl.class);

    private StartScreenCtrl startScreenCtrl;

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

        Event mockEvent = new Event();
        mockEvent.setTitle("Test");
        mockEvent.setParticipants(new ArrayList<>());
        mockEvent.setExpenses(new ArrayList<>());
        mockEvent.setInviteCode("testInviteCode");


        startScreenCtrl = new StartScreenCtrl(mockServer, mockMainCtrl);
        //Mockito.when(mockConfig.readFromFile(Mockito.anyString())).thenReturn(mockConfig);


        Mockito.doNothing().when(mockMainCtrl).showInvitation(Mockito.any(Event.class));
        Mockito.doNothing().when(mockMainCtrl).showEventOverview(Mockito.any(Event.class));
        Mockito.when(mockServer.addEvent(Mockito.any(Event.class))).thenReturn(mockEvent);
        Mockito.when(mockServer.getEventByCode("testInviteCode")).thenReturn(mockEvent);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/StartScreen.fxml"));

        loader.setControllerFactory(type -> {
            if (type == StartScreenCtrl.class) {
                return startScreenCtrl;
            } else {
                throw new RuntimeException("Requested unknown controller type");
            }
        });

        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    public void testClearFields() {
        mockConfig.setRecentEvents("");
        WaitForAsyncUtils.waitForFxEvents();

        clickOn("#newEventText");
        write("Test Event");
        Platform.runLater(() -> {
            startScreenCtrl.clearFields();
        });
    }

    @Test
    public void testCreateEvent() {
        mockConfig.setRecentEvents("");
        WaitForAsyncUtils.waitForFxEvents();

        String eventTitle = "Test Event";
        clickOn("#newEventText");
        write(eventTitle);
        clickOn("#createEventButton");
        Mockito.verify(mockServer).addEvent(Mockito.any(Event.class));
        Mockito.verify(mockMainCtrl).showInvitation(Mockito.any(Event.class));
    }

    @Test
    public void testJoinEvent() {
        mockConfig.setRecentEvents("");
        String inviteCode = "testInviteCode";
        clickOn("#joinEventText");
        write(inviteCode);
        clickOn("#joinEventButton");
        Mockito.verify(mockServer).getEventByCode(inviteCode);
        Mockito.verify(mockMainCtrl).showEventOverview(Mockito.any(Event.class));
    }

    @Test
    public void testOnSettingsClick() {
        mockConfig.setRecentEvents("");
        WaitForAsyncUtils.waitForFxEvents();

        clickOn("#settingsButton");
        Mockito.verify(mockMainCtrl).showUserSettings();
    }

    @Test
    public void testInitialize() {
        mockConfig.setRecentEvents("");
        WaitForAsyncUtils.waitForFxEvents();
        Platform.runLater(() -> {
            startScreenCtrl.initialize();
        });
    }

    @Test
    public void testKeyPressedCreateEvent() {
        mockConfig.setRecentEvents("");
        clickOn("#newEventText").write("Test Event");
        clickOn("#newEventText").push(KeyCode.ENTER);
    }

    @Test
    public void testKeyPressedJoinEvent() {
        mockConfig.setRecentEvents("");
        clickOn("#joinEventText").write("testInviteCode");
        clickOn("#joinEventText").push(KeyCode.ENTER);
    }

    @Test
    public void testCtrlW(){
        mockConfig.setRecentEvents("");
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.W, false, true, false, false);
        WaitForAsyncUtils.asyncFx(() -> startScreenCtrl.keyPressed(keyEvent));
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mockMainCtrl, Mockito.times(1)).closeWindow();
    }

    @Test
    public void deleteEventFromConfig(){
        mockConfig.setRecentEvents("testInviteCode");
        WaitForAsyncUtils.waitForFxEvents();
        Platform.runLater(() -> {
            startScreenCtrl.deleteEventFromConfig("testInviteCode");
        });
    }
}
