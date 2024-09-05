/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import client.scenes.*;
import client.utils.ConfigClient;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private ConfigClient configClient;

    private Path filepath = Paths.get("client/src/main/resources/config.txt").toAbsolutePath();
    private final String filePath = String.valueOf(filepath);


    /**
     * Launches the app
     * @param args
     * @throws URISyntaxException
     * @throws IOException
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    /**
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        configClient = new ConfigClient();
        configClient.readFromFile("config.txt");

        var editparticipant = FXML.load(EditParticipantCtrl.class, "client",
                "scenes", "EditParticipant.fxml");
        var addparticipant = FXML.load(AddParticipantCtrl.class, "client",
                "scenes", "AddParticipant.fxml");
        var eventoverview = FXML.load(EventOverviewCtrl.class, "client",
                "scenes", "EventOverview.fxml");
        var addexpense = FXML.load(AddExpenseCtrl.class, "client",
                "scenes", "AddEditExpense.fxml");
        var editexpense = FXML.load(EditExpenseCtrl.class, "client",
                "scenes", "EditExpense.fxml");
        var opendebts = FXML.load(OpenDebtsCtrl.class, "client", "scenes", "OpenDebts.fxml");
        var startscreen = FXML.load(StartScreenCtrl.class, "client", "scenes", "StartScreen.fxml");
        var invitation = FXML.load(InvitationCtrl.class, "client", "scenes", "Invitation.fxml");
        var addedittags = FXML.load(TagCtrl.class, "client", "scenes", "AddEditTags.fxml");
        var usersettings = FXML.load(
                UserSettingsCtrl.class, "client", "scenes", "UserSettings.fxml");
        var statistics = FXML.load(StatisticsCtrl.class, "client", "scenes", "Statistics.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, editparticipant,
                addparticipant, eventoverview, addexpense, editexpense,
                opendebts, startscreen, invitation, usersettings, addedittags, statistics);

        primaryStage.setOnCloseRequest(e -> {
            eventoverview.getKey().stop();
        });
    }
}