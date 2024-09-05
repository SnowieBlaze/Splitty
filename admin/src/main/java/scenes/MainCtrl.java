package scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {


    private Stage primaryStage;
    private LoginCtrl loginCtrl;
    private OverviewCtrl overviewCtrl;
    private Scene login;
    private Scene overview;

    /**
     * Initializes stage
     *
     * @param primaryStage
     * @param login
     * @param overview
     */
    public void initialize(Stage primaryStage, Pair<LoginCtrl, Parent> login,
                           Pair<OverviewCtrl, Parent> overview) {
        this.primaryStage = primaryStage;
        this.loginCtrl = login.getKey();
        this.login = new Scene(login.getValue());
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());
        showLogin();
        primaryStage.show();
    }

    /**
     * Shows events overview for admin
     */
    public void showOverview() {
        primaryStage.setTitle("Overview");
        overviewCtrl.initialize();
        primaryStage.setScene(overview);
    }

    /**
     * Shows login to the server window for the admin
     */
    public void showLogin() {
        primaryStage.setTitle("Login");
        loginCtrl.initialize();
        primaryStage.setScene(login);
    }

    /**
     * Closes the primary stage and kills the application
     */
    public void closeWindow() {
        primaryStage.close();
        System.exit(0);
    }

    /**
     * Register
     */
    public void register(){
        overviewCtrl.register();
    }

}
