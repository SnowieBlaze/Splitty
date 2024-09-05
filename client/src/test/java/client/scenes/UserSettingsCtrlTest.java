package client.scenes;

import client.utils.ConfigClient;
import client.utils.ServerUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserSettingsCtrlTest extends ApplicationTest {

    ServerUtils serverMock;
    ConfigClient configClientMock;
    MainCtrl mainCtrlMock;

    private UserSettingsCtrl userSettingsCtrl;

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
        configClientMock = new ConfigClient();
        userSettingsCtrl = new UserSettingsCtrl(serverMock, mainCtrlMock, configClientMock);
        userSettingsCtrl.setConfigClient(configClientMock);
        configClientMock.setName("");
        configClientMock.setEmail("");
        configClientMock.setIban("");
        configClientMock.setBic("");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/UserSettings.fxml"));
        loader.setControllerFactory(type -> {
            if (type == UserSettingsCtrl.class) {
                return userSettingsCtrl;
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
        userSettingsCtrl.onCancelClick();
        Mockito.verify(mainCtrlMock).showStartScreen();
    }

    @Test
    public void testConfirm() {
        Platform.runLater(() -> userSettingsCtrl.initialize());
        WaitForAsyncUtils.waitForFxEvents();

        clickOn("#nameField").write("Test");
        clickOn("#emailField").write("Test");
        clickOn("#ibanField").write("Test");
        clickOn("#bicField").write("Test");

        userSettingsCtrl.onConfirmClick();

        assertEquals("Test", configClientMock.getName());
        assertEquals("Test", configClientMock.getEmail());
        assertEquals("Test", configClientMock.getIban());
        assertEquals("Test", configClientMock.getBic());
    }

    @Test
    public void testInitializeAllBranches() throws Exception {
        Platform.runLater(() -> userSettingsCtrl.initialize());

        WaitForAsyncUtils.waitForFxEvents();

        Field nameField = UserSettingsCtrl.class.getDeclaredField("nameField");
        nameField.setAccessible(true);
        TextField nameTextField = (TextField) nameField.get(userSettingsCtrl);

        Field emailField = UserSettingsCtrl.class.getDeclaredField("emailField");
        emailField.setAccessible(true);
        TextField emailTextField = (TextField) emailField.get(userSettingsCtrl);

        Field ibanField = UserSettingsCtrl.class.getDeclaredField("ibanField");
        ibanField.setAccessible(true);
        TextField ibanTextField = (TextField) ibanField.get(userSettingsCtrl);

        Field bicField = UserSettingsCtrl.class.getDeclaredField("bicField");
        bicField.setAccessible(true);
        TextField bicTextField = (TextField) bicField.get(userSettingsCtrl);

        assertEquals("", nameTextField.getText());
        assertEquals("", emailTextField.getText());
        assertEquals("", ibanTextField.getText());
        assertEquals("", bicTextField.getText());
    }

    @Test
    public void testInitializeAllBranchesNonNull() throws Exception {
        configClientMock.setName("Test name");
        configClientMock.setEmail("Test email");
        configClientMock.setIban("Test iban");
        configClientMock.setBic("Test bic");

        Platform.runLater(() -> userSettingsCtrl.initialize());

        WaitForAsyncUtils.waitForFxEvents();

        Field nameField = UserSettingsCtrl.class.getDeclaredField("nameField");
        nameField.setAccessible(true);
        TextField nameTextField = (TextField) nameField.get(userSettingsCtrl);

        Field emailField = UserSettingsCtrl.class.getDeclaredField("emailField");
        emailField.setAccessible(true);
        TextField emailTextField = (TextField) emailField.get(userSettingsCtrl);

        Field ibanField = UserSettingsCtrl.class.getDeclaredField("ibanField");
        ibanField.setAccessible(true);
        TextField ibanTextField = (TextField) ibanField.get(userSettingsCtrl);

        Field bicField = UserSettingsCtrl.class.getDeclaredField("bicField");
        bicField.setAccessible(true);
        TextField bicTextField = (TextField) bicField.get(userSettingsCtrl);

        assertEquals("Test name", nameTextField.getText());
        assertEquals("Test email", emailTextField.getText());
        assertEquals("Test iban", ibanTextField.getText());
        assertEquals("Test bic", bicTextField.getText());
    }


    @Test
    public void testKeyPressed_W_WithControl() {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.W, false, true, false, false);
        WaitForAsyncUtils.asyncFx(() -> userSettingsCtrl.keyPressed(keyEvent));
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mainCtrlMock, Mockito.times(1)).closeWindow();
    }

    @Test
    public void testKeyPressed_S() {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.S, false, false, false, false);
        WaitForAsyncUtils.asyncFx(() -> userSettingsCtrl.keyPressed(keyEvent));
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testKeyPressed_Escape() {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ESCAPE, false, false, false, false);
        WaitForAsyncUtils.asyncFx(() -> userSettingsCtrl.keyPressed(keyEvent));
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testSendDefault() {
        Mockito.when(serverMock.sendDefault(Mockito.anyString())).thenReturn(true);
        Platform.runLater(() -> {
            TextField emailField = lookup("#emailField").queryAs(TextField.class);
            emailField.setText("Test");
        });
        WaitForAsyncUtils.waitForFxEvents();

        Platform.runLater(() -> userSettingsCtrl.sendDefault(new ActionEvent()));
        WaitForAsyncUtils.waitForFxEvents();

        Platform.runLater(() -> {
            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.fire();
        });
        WaitForAsyncUtils.waitForFxEvents();

        Platform.runLater(() -> {
            TextField emailField = lookup("#emailField").queryAs(TextField.class);
            emailField.setText(null);
        });

        WaitForAsyncUtils.waitForFxEvents();

        Platform.runLater(() -> userSettingsCtrl.sendDefault(new ActionEvent()));
        WaitForAsyncUtils.waitForFxEvents();

//        Platform.runLater(() -> {
//            DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
//            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
//            okButton.fire();
//        });
//        WaitForAsyncUtils.waitForFxEvents();

    }

    @Test
    public void testKeyPressed_Tab() {
        TextField nameField = lookup("#nameField").queryAs(TextField.class);
        clickOn(nameField);
        KeyEvent keyEvent = new KeyEvent(nameField, nameField, KeyEvent.KEY_PRESSED, "", "", KeyCode.TAB, false, false, false, false);
        WaitForAsyncUtils.asyncFx(() -> userSettingsCtrl.keyPressed(keyEvent));
        WaitForAsyncUtils.waitForFxEvents();
    }
}
