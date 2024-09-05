
import commons.Event;
import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.Test;
import scenes.MainCtrl;
import scenes.OverviewCtrl;
import utils.Admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
 public class OverviewCtrlTest {
    /* Tests will not pass in pipeline because it requires the server app to run
    @Test
     void readFromFile() {
         List<Expense> expenses = new ArrayList<>();
         List<Participant> participants = new ArrayList<>();
         List<Event> events = List.of(new Event(1, "test", "Queze2TK",
                 participants, expenses, "2024/03/20 21:00:12"));
         OverviewCtrl controller = new OverviewCtrl(new Admin(), new MainCtrl());
         Event result = controller.readEvent(new Scanner("{\"id\":1,\"title\":\"test\",\"inviteCode\":\"Queze2TK\"," +
                 "\"participants\":[]," + "\"expenses\":[],\"dateTime\":\"2024/03/20 21:00:12\"}"));
         assertTrue(events.get(0).equals(result));
     }

     @Test
     void readFromFileNull() {
         OverviewCtrl controller = new OverviewCtrl(new Admin(), new MainCtrl());
         Event result = controller.readEvent(new Scanner(""));
         assertNull(result);
     }
     */

}
