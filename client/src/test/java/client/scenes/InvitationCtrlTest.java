package client.scenes;


import client.utils.ServerUtils;
import commons.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.api.FxAssert;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;


public class InvitationCtrlTest extends ApplicationTest {

    ServerUtils serverMock;
    MainCtrl mainCtrlMock;

    private InvitationCtrl invitationCtrl;

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
        Mockito.doNothing().when(mainCtrlMock).showStartScreen();
        Mockito.doNothing().when(mainCtrlMock).showEventOverview(Mockito.any(Event.class));
        Mockito.when(serverMock.sendInvites(Mockito.anyList(), Mockito.any(Event.class), Mockito.anyString())).thenReturn(true);

        Event mockEvent = new Event();
        mockEvent.setInviteCode("testInviteCode");
        mockEvent.setTitle("testTitle");

        invitationCtrl = new InvitationCtrl(serverMock, mainCtrlMock);
        invitationCtrl.setEvent(mockEvent);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/Invitation.fxml"));
        loader.setControllerFactory(type -> {
            if (type == InvitationCtrl.class) {
                return invitationCtrl;
            } else {
                throw new RuntimeException("Requested unknown controller type");
            }
        });

        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    public void testInitialize() {
        invitationCtrl.initialize();
        assertEquals("testInviteCode", invitationCtrl.getInviteCodeText());
        assertEquals("testTitle", invitationCtrl.getEventNameText());
    }

    @Test
    public void setInviteCodeTextTest(){
        WaitForAsyncUtils.waitForFxEvents();
        invitationCtrl.setInviteCodeText();
        assertEquals("testInviteCode", invitationCtrl.getInviteCodeText());
    }

    @Test
    public void setEventNameTest(){
        WaitForAsyncUtils.waitForFxEvents();
        invitationCtrl.setEventNameText();
        assertEquals("testTitle", invitationCtrl.getEventNameText());
    }

    @Test
    public void testBackToStart() {
        clickOn("#emailTextArea");
        write("test@example.com");

        clickOn("#goBackButton");

        FxAssert.verifyThat("#emailTextArea", TextInputControlMatchers.hasText(""));

        Mockito.verify(mainCtrlMock, Mockito.times(1)).showStartScreen();
    }

    @Test
    public void testGoToEvent() {
        clickOn("#emailTextArea");
        write("test@example.com");

        clickOn("#goEventButton");

        FxAssert.verifyThat("#emailTextArea", TextInputControlMatchers.hasText(""));

        Mockito.verify(mainCtrlMock, Mockito.times(1)).showEventOverview(Mockito.any(Event.class));
    }

    @Test
    public void testSendInvites() {

        FxRobot robot = new FxRobot();

        clickOn("#emailTextArea");
        write("test@example.com");

        FxAssert.verifyThat("#emailTextArea", TextInputControlMatchers.hasText("test@example.com"));
        Mockito.when(serverMock.sendInvites(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
        clickOn("#sendInvitesButton");

        FxAssert.verifyThat(".dialog-pane", NodeMatchers.isNotNull());
        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        if (dialogPane.getScene().getWindow() instanceof Stage stage) {
            assertEquals("Success", stage.getTitle());
        }
        assertEquals("Invitation emails were sent successfully", dialogPane.getHeaderText());

        FxAssert.verifyThat("#emailTextArea", TextInputControlMatchers.hasText("test@example.com"));

    }

    @Test
    public void testKeyPressed_Enter() {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER, false, false, false, false);
        WaitForAsyncUtils.asyncFx(() -> invitationCtrl.keyPressed(keyEvent));
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testKeyPressed_Escape() {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ESCAPE, false, false, false, false);
        WaitForAsyncUtils.asyncFx(() -> invitationCtrl.keyPressed(keyEvent));
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mainCtrlMock, Mockito.times(1)).showStartScreen();
    }

    @Test
    public void testKeyPressed_W_WithControl() {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.W, false, true, false, false);
        WaitForAsyncUtils.asyncFx(() -> invitationCtrl.keyPressed(keyEvent));
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mainCtrlMock, Mockito.times(1)).closeWindow();
    }
}