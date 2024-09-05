package client.scenes;

import client.utils.ConfigClient;
import client.utils.ServerUtils;
import commons.Debt;
import commons.Event;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class OpenDebtsCtrlTest extends ApplicationTest {
    ServerUtils mockServer;
    ConfigClient mockConfig;
    MainCtrl mockMainCtrl;
    Event mockEvent;
    Debt mockDebt;
    Participant mockParticipant;

    private OpenDebtsCtrl openDebtsCtrl;

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
        mockConfig = Mockito.mock(ConfigClient.class);
        mockMainCtrl = Mockito.mock(MainCtrl.class);

        mockEvent = new Event();
        mockEvent.setTitle("Test");
        mockEvent.setParticipants(new ArrayList<>());

        mockParticipant = new Participant();
        mockParticipant.setName("testParticipant");
        mockParticipant.setBic("testBic");
        mockParticipant.setIban("testIban");
        mockParticipant.setEmail("testEmail");

        mockEvent.addParticipant(mockParticipant);

        mockDebt = new Debt();
        mockDebt.setPersonPaying(mockParticipant);
        mockDebt.setPersonOwing(mockParticipant);
        mockDebt.setAmount(100.0);

        ArrayList<Debt> debts = new ArrayList<>();
        debts.add(mockDebt);
        Mockito.when(mockServer.getPaymentInstructions(mockEvent)).thenReturn(debts);

        openDebtsCtrl = new OpenDebtsCtrl(mockServer, mockMainCtrl);
        openDebtsCtrl.setEvent(mockEvent);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/OpenDebts.fxml"));
        loader.setControllerFactory(type -> {
            if (type == OpenDebtsCtrl.class) {
                return openDebtsCtrl;
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
        Platform.runLater(() -> {
            openDebtsCtrl.initialize();
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testBackToEvent() {
        Platform.runLater(() -> {
            openDebtsCtrl.backToEvent();
        });
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mockMainCtrl).showEventOverview(mockEvent);
    }

    @Test
    public void testRemoveDebt() {
        Platform.runLater(() -> {
            openDebtsCtrl.removeDebt(mockDebt);
        });
        interact(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });
       WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testKeyPressed_W_WithControl() {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.W, false, true, false, false);
        WaitForAsyncUtils.asyncFx(() -> openDebtsCtrl.keyPressed(keyEvent));
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mockMainCtrl, Mockito.times(1)).closeWindow();
    }

    @Test
    public void testEscape(){
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ESCAPE, false, false, false, false);
        WaitForAsyncUtils.asyncFx(() -> openDebtsCtrl.keyPressed(keyEvent));
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testGetPaymentInstructionsThrowsWebApplicationException() {
        Mockito.when(mockServer.getPaymentInstructions(mockEvent)).thenThrow(new WebApplicationException("Test exception"));

        Platform.runLater(() -> {
            openDebtsCtrl.getPaymentInstructions();
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