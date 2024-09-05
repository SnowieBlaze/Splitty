package scenes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.Admin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;


public class OverviewCtrl {
    private final Admin admin;
    private final MainCtrl mainCtrl;
    @FXML
    private Button deleteButton;

    @FXML
    private Button dumpButton;

    @FXML
    private Button importButton;

    @FXML
    private TableView<Event> table;

    @FXML
    private TableColumn<Event, String> titleColumn;

    @FXML
    private TableColumn<Event, String> creationDateColumn;

    @FXML
    private TableColumn<Event, String> lastActivityColumn;

    private  static ObservableList<Event> events = FXCollections.observableArrayList();


    /**
     * Constructor for OverviewCtrl for dependency injection
     * @param admin
     * @param mainCtrl
     */
    @Inject
    public OverviewCtrl(Admin admin, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.admin = admin;
    }

    @FXML
    void delete(ActionEvent event) {
        Event selectedEvent = table.getSelectionModel().getSelectedItem();
        if (selectedEvent != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm event deletion");
            alert.setContentText("Are you sure you want to delete this event?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                admin.deleteEvent(selectedEvent);
                table.getItems().remove(selectedEvent);
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Delete event");
                alert.setHeaderText("Successfully deleted");
                alert.setContentText("Event was deleted successfully. You might have to press" +
                        " refresh to see changes");
                alert.showAndWait();
                refresh();
            }
        }
    }

    @FXML
    void dumpJSON(ActionEvent event) {
        Event selectedEvent = table.getSelectionModel().getSelectedItem();
        if (selectedEvent != null){
            long eventID = selectedEvent.getId();
            Boolean success = admin.jsonDump(eventID);
            if(success){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("JSON dump");
                alert.setHeaderText("Success");
                alert.setContentText("Event " + eventID + " has been dumped succesfully and " +
                        "can be found in the root directory");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("JSON dump");
                alert.setHeaderText("Error");
                alert.setContentText("Event " + eventID + " could not be dumped due to an error");
                alert.showAndWait();
            }
        }
    }

    @FXML
    void importJSON(ActionEvent event) {
        Stage filestage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        String userDir = System.getProperty("user.dir");
        File defaultDirectory = new File(userDir);
        fileChooser.setInitialDirectory(defaultDirectory);
        File selectedFile = fileChooser.showOpenDialog(filestage);
        if (selectedFile != null) {
            try {
                Event e = readEvent(
                        new Scanner(new File( selectedFile.getAbsolutePath())));
                admin.importEvent(e);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        refresh();
    }

    /**
     * Takes a scanner and parses the JSON to Events
     * Every line should be a different event
      * @param scanner to use
     * @return a list of events read
     */
    public Event readEvent(Scanner scanner){
        ObjectMapper objectMapper = new ObjectMapper();
        Event event = null;
        try {
            if(scanner.hasNextLine()){
                String json = scanner.nextLine();
                event = objectMapper.readValue(json, Event.class);
            }
        }
        catch (JsonProcessingException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("JSON dump");
            alert.setHeaderText("Error reading JSON file");
            alert.setContentText("Seems that the JSON file is corrupted or not well formatted. \n" +
                    "Ensure that there is one event per line");
            alert.showAndWait();
        }
        return event;
    }

    /**
     * refreshes the table
     * @param event button clicked
     */
    public void refresh(ActionEvent event){
        refresh();
    }

    /**
     * Refreshes the events in the table
     */
    public void refresh() {
        var eventss = admin.getEvents();
        if(eventss != null){
            events = FXCollections.observableList(eventss);
            table.setItems(events);
        }
    }

    /**
     * goes back to the login page
     * @param event
     */
    @FXML
    void back(ActionEvent event) {
        mainCtrl.showLogin();
    }

    /**
     * When the user presses a key, it triggers the
     * refresh method
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        if(e.isControlDown() && e.getCode() == KeyCode.R){
            refresh(null);
        }
        if(e.isControlDown() && e.getCode() == KeyCode.W){
            mainCtrl.closeWindow();
        }
        if(e.isControlDown() && e.getCode() == KeyCode.UP){
            table.getSelectionModel().selectPrevious();
        }
        if(e.isControlDown() && e.getCode() == KeyCode.DOWN){
            table.getSelectionModel().selectNext();
        }
    }

    void initialize(){
        events = FXCollections.observableArrayList(admin.getEvents());
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        lastActivityColumn.setCellValueFactory(new PropertyValueFactory<>("LastActivityTime"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("DateTime"));
        table.setItems(events);
        table.getSelectionModel().selectFirst();
    }

    void register(){
        admin.registerForEvents("/topic/events", e -> {
            events.add(e);
        });
        admin.registerForEventDeletion("/topic/eventsDelete", e -> {
            events.remove(e);
        });
    }

}
