package client.scenes;

import client.utils.ConfigClient;
import client.utils.LanguageButtonUtils;
import client.utils.LanguageResourceBundle;
import client.utils.ServerUtils;
import commons.Debt;
import commons.Event;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;

import com.google.inject.Inject;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OpenDebtsCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private LanguageResourceBundle languageResourceBundle;
    private Event event;
    private List<Debt> debts;
    private TitledPane[] titledPanes;
    private TextFlow[] textFlows;
    private FontAwesomeIconView[] envelopeIcons;
    private FontAwesomeIconView[] bankIcons;
    private Button[] buttonReceived;

    @FXML
    private Label openDebtsLabel;

    @FXML
    private Button backButton;

    @FXML
    private GridPane gridPane;

    /**
     * Constructor for the controller
     * with injected server and mainCtrl
     *
     * @param server
     * @param mainCtrl
     */
    @Inject
    public OpenDebtsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes all the debts associated with the current event
    */
    public void initialize() {
        if (event != null) {
            languageResourceBundle = LanguageResourceBundle.getInstance();
            switchTextLanguage();
            gridPane.getChildren().clear();
            gridPane.setAlignment(Pos.CENTER);
            debts = this.getPaymentInstructions();
            titledPanes = new TitledPane[debts.size()];
            textFlows = new TextFlow[debts.size()];
            envelopeIcons = new FontAwesomeIconView[debts.size()];
            bankIcons = new FontAwesomeIconView[debts.size()];
            buttonReceived = new Button[debts.size()];
            for (int i = 0; i < debts.size(); i++) {
                visualizeDebt(i);
            }

            server.registerEventUpdateDebts(event -> {
                if(this.event.getId() == event.getId()) {
                    this.event = server.getEvent(event.getId());
                    Platform.runLater(this::initialize);
                }
            });
        }
    }

    /**
     * Switches the language of the text.
     */

    public void switchTextLanguage(){
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();

        openDebtsLabel.setText(bundle.getString("openDebtsLabel"));
        backButton.setText(bundle.getString("backButton"));
    }

    /**
     * Visualizes a single debt into the gridPane
     * @param i - the index of the debt
     */
    private void visualizeDebt(int i) {
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();
        textFlows[i] = generateTextFlow(debts.get(i));
        textFlows[i].setStyle("-fx-max-height: 30");
        titledPanes[i] = new TitledPane();
        titledPanes[i].setGraphic(textFlows[i]);
        titledPanes[i].setAnimated(true);
        titledPanes[i].setExpanded(false);
        titledPanes[i].setContent(generateExpandableLabel(debts.get(i)));
        gridPane.add(titledPanes[i], 0, i, 1, 1);
        envelopeIcons[i] = new FontAwesomeIconView();
        envelopeIcons[i].setGlyphName("ENVELOPE");
        envelopeIcons[i].setSize("15");
        GridPane.setValignment(envelopeIcons[i], javafx.geometry.VPos.TOP);
        GridPane.setHalignment(envelopeIcons[i], javafx.geometry.HPos.LEFT);
        Tooltip toolTipEnvelope = new Tooltip(bundle.getString("openDebtsEmailAvailable"));
        if(debts.get(i).getPersonPaying().getEmail() == "" || ConfigClient.getEmail() == null
            || ConfigClient.getEmail().equals("")) {
            envelopeIcons[i].setFill(Color.GREY);
            toolTipEnvelope.setText(bundle.getString("openDebtsEmailNotAvailable"));
        }
        Tooltip.install(envelopeIcons[i], toolTipEnvelope);
        GridPane.setMargin(envelopeIcons[i], new Insets(7, 0, 0, 10));
        gridPane.add(envelopeIcons[i], 1, i, 1, 1);
        bankIcons[i] = new FontAwesomeIconView();
        bankIcons[i].setGlyphName("BANK");
        bankIcons[i].setSize("15");
        GridPane.setValignment(bankIcons[i], javafx.geometry.VPos.TOP);
        GridPane.setHalignment(bankIcons[i], javafx.geometry.HPos.LEFT);
        Tooltip toolTipBank = new Tooltip(bundle.getString("openDebtsBankInfoAvailable"));
        if (debts.get(i).getPersonOwing().getBic() == ""
                || debts.get(i).getPersonOwing().getBic() == "null" ||
                debts.get(i).getPersonOwing().getBic() == null
                || debts.get(i).getPersonOwing().getIban() == "null" ||
                debts.get(i).getPersonOwing().getIban() == null
                || debts.get(i).getPersonOwing().getIban() == "") {
            bankIcons[i].setFill(Color.GREY);
            toolTipBank.setText(bundle.getString("openDebtsBankInfoNotAvailable"));
        }
        Tooltip.install(bankIcons[i], toolTipBank);
        GridPane.setMargin(bankIcons[i], new Insets(7, 0, 0, 30));
        gridPane.add(bankIcons[i], 1, i, 1, 1);

        buttonReceived[i] = new Button(bundle.getString("openDebtsMarkReceived"));
        buttonReceived[i].setOnMouseClicked(e -> removeDebt(debts.get(i)));
        GridPane.setValignment(buttonReceived[i], javafx.geometry.VPos.TOP);
        GridPane.setHalignment(buttonReceived[i], javafx.geometry.HPos.LEFT);
        GridPane.setMargin(buttonReceived[i], new Insets(5, 10, 0, 15));
        gridPane.add(buttonReceived[i], 2, i, 1, 1);
    }

    /**
     * Generates the appropriate TextFlow for the debt
     *
     * @param debt - the debt for which we generate TextFlow
     * @return the appropriate TextFlow
     */
    private TextFlow generateTextFlow(Debt debt) {
        TextFlow tf = new TextFlow();
        Text payer = new Text(debt.getPersonPaying().getName());
        payer.setStyle("-fx-font-weight: bold");
        Text gives = new Text(" gives ");
        Text amount = new Text(String.format("%.2f", debt.getAmount()));
        amount.setStyle("-fx-font-weight: bold");
        Text to = new Text(" to ");
        Text receiver = new Text(debt.getPersonOwing().getName());
        receiver.setStyle("-fx-font-weight: bold");
        tf.getChildren().addAll(payer, gives, amount, to, receiver);
        tf.setStyle("-fx-alignment: center-left; -fx-padding: 10");
        return tf;
    }

    /**
     * Generates a label for the expandable part of the debt
     *
     * @param debt - the debt for which we generate the label
     * @return the appropriate label
     */
    private Label generateExpandableLabel(Debt debt) {
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();
        String text = "";
        int size = 0;
        Label label = new Label();
        if (debt.getPersonOwing().getBic() != "" && debt.getPersonOwing().getBic() != "null" &&
                debt.getPersonOwing().getBic() != null
                && debt.getPersonOwing().getIban() != "null" &&
                debt.getPersonOwing().getIban() != null
                && debt.getPersonOwing().getIban() != "") {
            text += bundle.getString("openDebtsBankInfoAvailable") +",\n" +
                    bundle.getString("openDebtsTransferMoneyTo") + ":\n" +
                    bundle.getString("openDebtsAccountHolder") +": " +
                    debt.getPersonOwing().getName() + "\n" +
                    bundle.getString("openDebtsIBAN") + ": " +
                    debt.getPersonOwing().getIban() + "\n" +
                    bundle.getString("openDebtsBIC") + ": "
                    + debt.getPersonOwing().getBic();
            size += 80;
        } else {
            text += bundle.getString("openDebtsBankInfoNotAvailable");
            size += 20;
        }
        text += "\n\n";
        size += 20;
        if (debt.getPersonPaying().getEmail() != "" && ConfigClient.getEmail() != null
                && !ConfigClient.getEmail().equals("")) {
            text += bundle.getString("openDebtsEmailAvailable");
            size += 20;
            Hyperlink hyperlink = new Hyperlink(bundle.getString("openDebtsSendReminder"));
            hyperlink.setOnAction(event -> {
                sendEmail(debt);
            });
            label.setGraphic(hyperlink);
        } else {
            text += bundle.getString("openDebtsEmailNotAvailable");
            size += 20;
        }
        label.setText(text);
        label.setStyle("-fx-min-height: " + size);
        return label;
    }

    private void sendEmail(Debt debt) {
        Boolean response = server.sendReminder(debt.getPersonOwing(), debt.getAmount(),
                debt.getPersonPaying().getEmail(),
                event.getTitle());
        if(response != null){
            if(response){
                ResourceBundle bundle = languageResourceBundle.getResourceBundle();
                Alert alert =  new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(bundle.getString("openDebtsAlertInfoTitleText"));
                alert.setHeaderText(bundle.getString("openDebtsAlertInfoHeaderText"));
                alert.showAndWait();
            }
            else{
                ResourceBundle bundle = languageResourceBundle.getResourceBundle();
                Alert alert =  new Alert(Alert.AlertType.WARNING);
                alert.setTitle(bundle.getString("openDebtsAlertTitleText"));
                alert.setHeaderText(bundle.getString("openDebtsAlertHeaderText"));
                alert.setContentText(bundle.getString("openDebtsAlertContentText"));
                alert.showAndWait();
            }
        }
    }

    /**
     * Shows an alert message for confirmation of settling(deleting) the debt
     * @param debt - the debt to be settled(removed)
     */
    void removeDebt(Debt debt) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();
        alert.setTitle(bundle.getString("removeDebtAlertTitleText"));
        alert.setContentText(bundle.getString("removeDebtAlertContentText"));

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                event.addSettledDebt(new Debt(debt.getPersonPaying(),
                    debt.getPersonOwing(), debt.getAmount()));
                server.persistEvent(event);
                event = server.getEvent(event.getId());
                mainCtrl.showOpenDebts(event);
            }
        });
    }

    /**
     * Sets the current event for which we retrieve debts
     * @param event - the event for which are the debts
     */
    public void setEvent(Event event){
        this.event = event;
    }

    /**
     * The method for going to the previous page(Event overview)
     */
    @FXML
    void backToEvent() {
        mainCtrl.showEventOverview(event);
    }



    /**
     * Retrieves all debts associated with a certain event
     * @return all debts associated with a given event
     */
    public List<Debt> getPaymentInstructions() {
        try {
            return server.getPaymentInstructions(event);
        } catch (WebApplicationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return null;
        }
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
                backToEvent();
                break;
            default:
                break;
        }
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
        if (url == null) {
            throw new RuntimeException("Resources folder not found");
        }

        Image image = new Image(String.valueOf(url));

        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        button.setGraphic(imageView);
    }
}
