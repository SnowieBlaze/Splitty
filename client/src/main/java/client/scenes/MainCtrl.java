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
package client.scenes;

import client.utils.ConfigClient;
import client.utils.LanguageResourceBundle;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    private LanguageResourceBundle languageResourceBundle = LanguageResourceBundle.getInstance();
    private EditParticipantCtrl editParticipantCtrl;
    private Scene editparticipant;

    private AddParticipantCtrl addParticipantCtrl;
    private Scene addparticipant;

    private EventOverviewCtrl eventOverviewCtrl;
    private Scene eventoverview;

    private OpenDebtsCtrl openDebtsCtrl;
    private Scene opendebts;

    private InvitationCtrl invitationCtrl;
    private Scene invitation;

    private StartScreenCtrl startScreenCtrl;
    private Scene startscreen;

    private AddExpenseCtrl addExpenseCtrl;
    private Scene addexpense;

    private EditExpenseCtrl editExpenseCtrl;
    private Scene editexpense;

    private UserSettingsCtrl userSettingsCtrl;
    private Scene usersettings;

    private TagCtrl tagCtrl;
    private Scene tags;

    private StatisticsCtrl statisticsCtrl;
    private Scene statistics;

    /**
     * Initializes stage
     * @param primaryStage
     * @param editparticipant
     * @param addparticipant
     * @param eventoverview
     * @param addexpense
     * @param editexpense
     * @param opendebts
     * @param startscreen
     * @param invitation
     * @param usersettings
     * @param tags
     * @param statistics
     */
    public void initialize(Stage primaryStage,
                           Pair<EditParticipantCtrl, Parent> editparticipant,
                           Pair<AddParticipantCtrl, Parent> addparticipant,
                           Pair<EventOverviewCtrl, Parent> eventoverview,
                           Pair<AddExpenseCtrl, Parent> addexpense,
                           Pair<EditExpenseCtrl, Parent> editexpense,
                           Pair<OpenDebtsCtrl, Parent> opendebts,
                           Pair<StartScreenCtrl, Parent> startscreen,
                           Pair<InvitationCtrl, Parent> invitation,
                           Pair<UserSettingsCtrl, Parent> usersettings,
                           Pair<TagCtrl, Parent> tags,
                           Pair<StatisticsCtrl, Parent> statistics
    ) {

        this.primaryStage = primaryStage;

        this.editParticipantCtrl = editparticipant.getKey();
        this.editparticipant = new Scene(editparticipant.getValue());

        this.addParticipantCtrl = addparticipant.getKey();
        this.addparticipant = new Scene(addparticipant.getValue());

        this.eventOverviewCtrl = eventoverview.getKey();
        this.eventoverview = new Scene(eventoverview.getValue());

        this.addExpenseCtrl = addexpense.getKey();
        this.addexpense = new Scene(addexpense.getValue());

        this.editExpenseCtrl = editexpense.getKey();
        this.editexpense = new Scene(editexpense.getValue());

        this.openDebtsCtrl = opendebts.getKey();
        this.opendebts = new Scene(opendebts.getValue());

        this.startScreenCtrl = startscreen.getKey();
        this.startscreen = new Scene(startscreen.getValue());

        this.invitationCtrl = invitation.getKey();
        this.invitation = new Scene(invitation.getValue());

        this.userSettingsCtrl = usersettings.getKey();
        this.usersettings = new Scene(usersettings.getValue());

        this.tagCtrl = tags.getKey();
        this.tags = new Scene(tags.getValue());

        this.statisticsCtrl = statistics.getKey();
        this.statistics = new Scene(statistics.getValue());

        showStartScreen();
        primaryStage.show();

    }

    /**
     * Shows the starting  screen
     */
    public void showStartScreen() {
        startScreenCtrl.clearFields();
        startScreenCtrl.initialize();
        primaryStage.setScene(startscreen);
        primaryStage.setTitle(languageResourceBundle
                .getResourceBundle().getString("titleStartScreen"));
    }


    /**
     * Switches the scene to the edit participant window
     * @param event takes an event as a parameter for which we edit a participant
     * @param participant takes the participant as a parameter to edit
     */
    public void showEditParticipant(Event event, Participant participant) {
        primaryStage.setScene(editparticipant);
        editParticipantCtrl.setEvent(event);
        editParticipantCtrl.setParticipant(participant);
        editParticipantCtrl.initialize();
        primaryStage.setTitle(languageResourceBundle
                .getResourceBundle().getString("titleEditParticipant"));
    }

    /**
     * Switches the scene to the add participant window
     * @param event - takes an event as a parameter for which we add a participant
     */
    public void showAddParticipant(Event event) {
        primaryStage.setScene(addparticipant);
        addParticipantCtrl.setEvent(event);
        addParticipantCtrl.initialize();
        primaryStage.setTitle(languageResourceBundle
                .getResourceBundle().getString("titleAddParticipant"));
    }

    /**
     * Switches the scene to the event overview window
     * @param event - takes an event as a parameter for which we show the overview
     */

    public void showEventOverview(Event event) {
        eventOverviewCtrl.setEvent(event);
        eventOverviewCtrl.initialize();
        primaryStage.setScene(eventoverview);
        primaryStage.setTitle(languageResourceBundle
                .getResourceBundle().getString("titleEventOverview"));
    }


    /**
     * Switches the scene to the add expense window
     * @param event - takes an event as a parameter for which we add a participant
     * @param participant - that adds expense
     * @param tag - the tag to be set
     */
    public void showAddExpenseWithTag(Event event, Participant participant, Tag tag) {
        addExpenseCtrl.setEvent(event);
        ConfigClient configClient =  new ConfigClient();
        configClient.readFromFile("config.txt");
        addExpenseCtrl.setCurrency(ConfigClient.getCurrency());
        addExpenseCtrl.setParticipant(participant);
        addExpenseCtrl.setTag(tag);
        addExpenseCtrl.initialize();
        primaryStage.setScene(addexpense);
        primaryStage.setTitle(languageResourceBundle
                .getResourceBundle().getString("titleAddExpense"));
    }

    /**
     * Switches the scene to the add expense window
     * @param event - takes an event as a parameter for which we add a participant
     * @param participant - that adds expense
     */
    public void showAddExpense(Event event, Participant participant) {
        addExpenseCtrl.setEvent(event);
        ConfigClient configClient =  new ConfigClient();
        configClient.readFromFile("config.txt");
        addExpenseCtrl.setCurrency(ConfigClient.getCurrency());
        addExpenseCtrl.setParticipant(participant);
        addExpenseCtrl.initialize();
        primaryStage.setScene(addexpense);
        primaryStage.setTitle(languageResourceBundle
                .getResourceBundle().getString("titleAddExpense"));
    }

    /**
     * Switches the scene to the edit participant window
     * @param event takes an event as a parameter for which we edit an expense
     * @param expense takes the expense as a parameter to edit
     * @param participant the current participant
     */
    public void showEditExpense(Event event, Expense expense, Participant participant) {
        primaryStage.setScene(editexpense);
        editExpenseCtrl.setEvent(event);
        editExpenseCtrl.setExpense(expense);
        editExpenseCtrl.setParticipant(participant);
        editExpenseCtrl.setTag(expense.getTag());
        editExpenseCtrl.initialize();
        primaryStage.setTitle(languageResourceBundle
                .getResourceBundle().getString("titleEditExpense"));
    }

    /**
     * Switches the scene to the edit participant window
     * @param event takes an event as a parameter for which we edit an expense
     * @param expense takes the expense as a parameter to edit
     * @param participant the current participant
     * @param tag - the tag to be set
     */
    public void showEditExpenseWithTag(Event event, Expense expense,
                                       Tag tag, Participant participant) {
        primaryStage.setScene(editexpense);
        editExpenseCtrl.setEvent(event);
        editExpenseCtrl.setExpense(expense);
        editExpenseCtrl.setParticipant(participant);
        editExpenseCtrl.setTag(tag);
        editExpenseCtrl.initialize();
        primaryStage.setTitle(languageResourceBundle
                .getResourceBundle().getString("titleEditExpense"));
    }

    /**
     * Switches the scene to the open debts window
     * @param event - takes an event as a parameter for which we show the open debts
     */
    public void showOpenDebts(Event event) {
        openDebtsCtrl.setEvent(event);
        openDebtsCtrl.initialize();
        primaryStage.setScene(opendebts);
        primaryStage.setTitle(languageResourceBundle
                .getResourceBundle().getString("titleOpenDebts"));
    }

    /**
     * Switches the scene to the invitation window
     * @param event - the event for which we show the window
     */

    public void showInvitation(Event event) {
        invitationCtrl.setEvent(event);
        invitationCtrl.initialize();
        primaryStage.setScene(invitation);
        primaryStage.setTitle(languageResourceBundle
                .getResourceBundle().getString("titleInvitation"));
    }

    /**
     * Switches the scene to the User Settings
     */
    public void showUserSettings() {
        userSettingsCtrl.initialize();
        primaryStage.setScene(usersettings);
        primaryStage.setTitle(languageResourceBundle
                .getResourceBundle().getString("titleUserSettings"));
    }

    /**
     * Switches the scene to add/edit tag
     * @param event
     * @param expense
     * @param participant
     * @param isAddExpense
     * @param tag
     */
    public void showTags(Event event, Expense expense,
                         Participant participant, boolean isAddExpense, Tag tag) {
        tagCtrl.setExpense(expense);
        tagCtrl.setEvent(event);
        tagCtrl.setParticipant(participant);
        tagCtrl.setIsExpenseTrue(isAddExpense);
        tagCtrl.setTagOnFocus(tag);
        tagCtrl.initialize();
        primaryStage.setScene(tags);
        primaryStage.setTitle(languageResourceBundle
                .getResourceBundle().getString("titleTag"));
    }

    /**
     * Switches to the statistics scene
     * @param event - the event for the statistics
     */
    public void showStatistics(Event event) {
        statisticsCtrl.setEvent(event);
        statisticsCtrl.initialize();
        primaryStage.setScene(statistics);
        primaryStage.setTitle(languageResourceBundle
                .getResourceBundle().getString("titleStatistics"));
    }

    /**
     * Closes the primary stage and kills the application
     */
    public void closeWindow() {
        primaryStage.close();
        System.exit(0);
    }
}