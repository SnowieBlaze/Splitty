package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class StatisticsCtrlTest extends ApplicationTest {

    Event mockEvent;
    Expense mockExpense;

    Participant mockParticipant;

    MainCtrl mockMainCtrl;
    ServerUtils mockServerUtils;

    private StatisticsCtrl statisticsCtrl;

    @Override
    public void start(Stage stage) throws Exception{

        mockMainCtrl = Mockito.mock(MainCtrl.class);
        mockServerUtils = Mockito.mock(ServerUtils.class);

        mockParticipant = new Participant();
        mockParticipant.setName("Test Participant");
        mockParticipant.setEmail("Test");
        mockParticipant.setBic("Test");
        mockParticipant.setIban("Test");

        mockExpense = new Expense();
        mockExpense.setAmount(100.0);
        mockExpense.setCurrency("EUR");
        mockExpense.setTitle("Test Expense");
        mockExpense.setTag(new Tag("Test Tag", "#0000FF"));
        mockExpense.setDateTime("2021-12-12");
        mockExpense.setDebts(new ArrayList<>());
        mockExpense.setParticipants(new ArrayList<>());
        mockExpense.addParticipant(mockParticipant);


        mockEvent = new Event();
        mockEvent.setTitle("Test Event");
        mockEvent.setInviteCode("1234");
        mockEvent.setDateTime("2021-12-12");
        mockEvent.setExpenses(new ArrayList<>());
        mockEvent.addExpense(mockExpense);
        mockEvent.setParticipants(new ArrayList<>());
        mockEvent.addParticipant(mockParticipant);

        statisticsCtrl = new StatisticsCtrl(mockServerUtils, mockMainCtrl);
        statisticsCtrl.setEvent(mockEvent);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/Statistics.fxml"));
        loader.setControllerFactory(type -> {
            if (type == StatisticsCtrl.class) {
                return statisticsCtrl;
            } else {
                throw new RuntimeException("Unknown class type: " + type.getName());
            }
        });

        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    public void testInitialize(){
        Platform.runLater(() -> {
            statisticsCtrl.initialize();
        });
    }

    @Test
    public void testBack(){
        Platform.runLater(() -> {
            statisticsCtrl.onBackClick();
        });
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(mockMainCtrl, Mockito.times(1)).showEventOverview(mockEvent);
    }

    @Test
    public void testSetIconWithInvalidIconName() {
        Button button = new Button();

        assertThrows(RuntimeException.class, () -> {
            statisticsCtrl.setIcon("invalid_icon_name", button);
        });
    }

    @Test
    public void testSetIconWithTesting(){
        statisticsCtrl.setTesting(true);
        Button button = new Button();
        Platform.runLater(() ->{
            statisticsCtrl.setIcon("icon_name", button);
        });
    }
}
