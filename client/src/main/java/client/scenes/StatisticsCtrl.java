package client.scenes;

import client.utils.LanguageButtonUtils;
import client.utils.ServerUtils;
import client.utils.LanguageResourceBundle;
import commons.Event;
import commons.Expense;
import commons.Tag;
import jakarta.inject.Inject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class StatisticsCtrl {

    private boolean testing = false;

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;

    private LanguageResourceBundle languageResourceBundle;

    @FXML
    private Label totalCostLabel;

    @FXML
    private AnchorPane pane;

    @FXML
    private PieChart pieChart;

    @FXML
    private Button backButton;

    /**
     * Constructor for the controller
     * with injected server and mainCtrl
     * @param server - the server
     * @param mainCtrl - the mainCtrl
     */
    @Inject
    public StatisticsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initialize the scene
     */
    public void initialize() {
        if(this.event != null) {
            languageResourceBundle = LanguageResourceBundle.getInstance();
            totalCostLabel.setText(
                    languageResourceBundle.getResourceBundle().getString("totalCostLabel") +
                    String.format("%.2f", event.getExpenses().stream().mapToDouble(
                    Expense::getAmount).sum()) + "$");
            Map<Tag, Double> distribution = getMoneyPerTag();
            pane.getChildren().remove(pieChart);
            pieChart = new PieChart();
            pane.getChildren().add(pieChart);
            ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
            pieChart.setData(data);
            for(Tag tag : distribution.keySet()) {
                PieChart.Data section = new PieChart.Data(tag.getType(), distribution.get(tag));
                data.add(section);
            }
            List<Tag> tags = distribution.keySet().stream().toList();
            for(int i = 0; i < pieChart.getData().size(); i++) {
                pieChart.getData().get(i).getNode()
                    .setStyle("-fx-pie-color: " +
                        colorToHex(Color.web(tags.get(i).getColor())));
            }
            pieChart.setPadding(new javafx.geometry.Insets(0, 0, 100, 0));
            pieChart.setLayoutY(60);
            switchLanguage();
            pieChart.setLegendVisible(false);

            server.registerEventUpdateStats(event -> {
                if(this.event.getId() == event.getId()) {
                    this.event = server.getEvent(event.getId());
                    Platform.runLater(this::initialize);
                }
            });
        }
    }

    /**
     * Retrieves the amount per tag
     * @return - hashmap with the tags
     * and their corresponding amount
     */
    private Map<Tag, Double> getMoneyPerTag() {
        Map<Tag, Double> distribution = new HashMap<>();
        Tag otherTag = new Tag("others", "0xffffffff");
        distribution.put(otherTag, 0.0);
        for(Expense expense : event.getExpenses()) {
            if(expense.getTag() == null || expense.getTag().getType().equals("others")) {
                distribution.put(otherTag, distribution.get(otherTag) + expense.getAmount());
            }
            else {
                if(!distribution.containsKey(expense.getTag())) {
                    distribution.put(expense.getTag(), expense.getAmount());
                }
                else {
                    distribution.put(expense.getTag(),
                        distribution.get(expense.getTag()) + expense.getAmount());
                }
            }
        }
        return distribution;
    }

    /**
     * Convert a web color to a hexadecimal one(for css)
     * @param color - the color to be converted
     * @return the hexadecimal code
     */
    private static String colorToHex(Color color) {
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);

        return String.format("#%02x%02x%02x", r, g, b);
    }

    /**
     * Set the event for the controller
     * @param event - the event to be set
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Returns back to event overview
     */
    @FXML
    public void onBackClick() {
        mainCtrl.showEventOverview(event);
    }

    /**
     * Sets the icon of the chosen button.
     * @param iconName
     * @param button
     */
    @FXML
    public void setIcon(String iconName, Button button) {
        String path = "client/images/icons/" + iconName;
        URL url = LanguageButtonUtils.class.getClassLoader().getResource(path);
        if(!testing && url == null){
            throw new RuntimeException("Invalid icon name");
        }

        Image image = null;

        if(!testing){
            image = new Image(String.valueOf(url));
        }

        ImageView imageView = new ImageView();

        if(!testing){
            imageView.setImage(image);
        }

        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        button.setGraphic(imageView);
    }

    /**
     * Setter for testing
     * @param testing - the testing value
     */
    public void setTesting(boolean testing) {
        this.testing = testing;
    }

    /**
     * Method to be called when a key is pressed
     * @param e keyevent to listen
     */
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getCode() == KeyCode.W) {  //close window
            mainCtrl.closeWindow();
        }
        switch (e.getCode()) {
            case ESCAPE:
                onBackClick();
                break;
            default:
                break;
        }
    }

    /**
     * Method that always updates language on initialize.
     */

    public void switchLanguage(){
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();
        backButton.setText(bundle.getString("backButton"));
    }
}
