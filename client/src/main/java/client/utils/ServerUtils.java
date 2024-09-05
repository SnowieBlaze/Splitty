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
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import commons.*;
import jakarta.ws.rs.ProcessingException;
//import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import javafx.scene.control.Alert;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private static  String server = "http://localhost:8080/";

    /**
     * Setter for URL
     * @param url
     */
    public static void setURL(String url) {
        server = url;
    }

    /**
     * Get debts for an event
     *
     * @param event the event
     * @return a list of debts
     */
    public List<Debt> getPaymentInstructions(Event event) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/debts/event/" + event.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    /**
     * Get all tags
     * @return - a list of the tags
     */
    public List<Tag> getTags() {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/tags")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<>() {
            });
    }

    /**
     * Adds a new tag
     * @param tag - the tag to be added
     * @return the new tag
     */
    public Tag addTag(Tag tag) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/tags/add")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    /**
     * Updates a tag
     * @param tag - the version of the tag
     * @return the tag
     */
    public Tag updateTag(Tag tag) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/tags/update")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    /**
     * Deletes a tag
     * @param tag - the tag to be deleted
     * @return the response from the server
     */
    public Response deleteTag(Tag tag) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(server).path("api/tags/" + tag.getId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .delete();
    }

//    /**
//     * Adds debt to the database
//     *
//     * @param debt the debt to add
//     * @return the debt added
//     */
//    public Debt addDebt(Debt debt) {
//        return ClientBuilder.newClient(new ClientConfig())
//                .target(server).path("api/debts")
//                .request(APPLICATION_JSON)
//                .accept(APPLICATION_JSON)
//                .post(Entity.entity(debt, APPLICATION_JSON), Debt.class);
//    }

    /**
     * Deletes a debt from the database
     *
     * @param debt the debt to delete
     * @return the response from the server
     */

    public Response deleteDebt(Debt debt) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/debts/" + debt.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    /**
     * This should get an event from the database by the id of the event
     *
     * @param id - the id it is looked for
     * @return the event with the specified id
     */
    public Event getEvent(long id) {
        try{
            Response response = ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("/api/events/id/" + id)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(Event.class);
            } else {
                showAlert();
                return  null;
            }
        }
        catch(ProcessingException e){
            showAlert();
            return null;
        }
    }

    /**
     * Method to send an event to the server to be saved
     *
     * @param event to be saved
     * @return the saved event by the server
     */
    public Event addEvent(Event event) {

        try{
            Response response = ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("/api/events")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .post(Entity.json(event));

            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                return response.readEntity(Event.class);
            } else {
                showAlert();
                return null;
            }
        }catch (ProcessingException e){
            showAlert();
            return null;
        }
    }

    /**
     * Method to get event by invite code
     * It uses a client and executes the api from eventController
     * Then if the response is ok it gets the event associated with the response
     * Otherwise throws exception
     *
     * @param inviteCode of event
     * @return event with that inviteCode or an exception
     */
    public Event getEventByCode(String inviteCode) {
        try{
            Response response = ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("/api/events/" + inviteCode)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(Event.class);
            } else {
                return null;
            }
        }catch (ProcessingException e){
            return null;
        }
    }

    /**
     * Deletes a participant from the server
     *
     * @param participant - the participant to delete
     * @return the response from the server
     */

    public Response deleteParticipant(Participant participant) {
        try{
            return ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("api/participants/" + participant.getId())
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .delete();
        }
        catch(ProcessingException e){
            return null;
        }
    }

    /**
     * Persists an event
     *
     * @param event - the event to persist
     * @return the persisted event
     */
    public Event persistEvent(Event event) {
        Entity<Event> entity = Entity.entity(event, APPLICATION_JSON);
        try{
            return ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("api/events/persist/" + event.getId())
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .put(entity, Event.class);
        }
        catch(ProcessingException e) {
            showAlert();
            return null;
        }
    }

    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor();

    /**
     * Registers changes in the event
     * @param consumer - consumer that registers changes
     */
    public void registerEventUpdate(Consumer<Event> consumer) {
        EXEC.submit(() -> {
            while(!Thread.interrupted()) {
                Response res = ClientBuilder.newClient(new ClientConfig())
                        .target(server).path("api/events/update")
                        .request(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .get(Response.class);

                if(res.getStatus() == 204) {
                    continue;
                }
                Event event = res.readEntity(Event.class);
                consumer.accept(event);
            }
        });
    }

    private static final ExecutorService EXEC2 = Executors.newSingleThreadExecutor();

    /**
     * Registers changes in the event for debts
     * @param consumer - consumer that registers changes
     */
    public void registerEventUpdateDebts(Consumer<Event> consumer) {
        EXEC2.submit(() -> {
            while(!Thread.interrupted()) {
                Response res = ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("api/events/update")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get(Response.class);

                if(res.getStatus() == 204) {
                    continue;
                }
                Event event = res.readEntity(Event.class);
                consumer.accept(event);
            }
        });
    }

    private static final ExecutorService EXEC3 = Executors.newSingleThreadExecutor();

    /**
     * Registers changes in the event for statistics
     * @param consumer - consumer that registers changes
     */
    public void registerEventUpdateStats(Consumer<Event> consumer) {
        EXEC2.submit(() -> {
            while(!Thread.interrupted()) {
                Response res = ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("api/events/update")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get(Response.class);

                if(res.getStatus() == 204) {
                    continue;
                }
                Event event = res.readEntity(Event.class);
                consumer.accept(event);
            }
        });
    }

    /**
     * Sends the invite code of the event to the specified emails
     *
     * @param emails list of emails to sent
     * @param event  of which the invite code it sends
     * @param creatorname the persons who send the emails
     * @return true if the emails were sent successfully
     */
    public Boolean sendInvites(List<String> emails, Event event, String creatorname) {
        try{
            Response response = ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("/api/email/" + event.getInviteCode())
                    .queryParam("creatorName", creatorname)
                    .queryParam("creatorEmail", ConfigClient.getEmail())
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .post(Entity.json(emails));
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return true;
            }else if(response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()){
                return false;
            }
            else {
                showAlert();
                return null;
            }
        }
        catch(ProcessingException e){
            showAlert();
            return null;
        }

    }

    /**
     * Sends a default email to the user to check
     * if the credentials are correct
     * @param email email address to sent
     * @return boolean
     */
    public Boolean sendDefault(String email) {
        try{
            Participant participant = new Participant(0, ConfigClient.getName(),
                    email, ConfigClient.getIban(), ConfigClient.getBic());
            Response response = ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("/api/email/default")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .post(Entity.json(participant));
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return true;
            } else if(response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()){
                return false;
            } else {
                showAlert();
                return null;
            }
        }
        catch(ProcessingException e){
            showAlert();
            return null;
        }
    }

    /**
     * Method to send a reminder email to the person who has to pay a debt
     *
     * @param participant the participant of the person who will receive the money
     * @param amount amount that has to be paid
     * @param email email of the person who has to pay
     * @param eventTitle title of the event in which the expense is in
     * @return boolena
     */
    public Boolean sendReminder(Participant participant, double amount,
                                 String email, String eventTitle) {
        try{
            Response response = ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("/api/email/")
                    .queryParam("creatorEmail", ConfigClient.getEmail())
                    .queryParam("amount", amount)
                    .queryParam("email", email)
                    .queryParam("eventTitle", eventTitle)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .post(Entity.json(participant));
            System.out.println(response.getStatus());
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return true;
            }
            else if(response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()){
                return false;
            }
            else {
                showAlert();
                return null;
            }
        }
        catch(ProcessingException e){
            showAlert();
            return null;
        }
    }



    /**
     * Deletes an expense from the server
     *
     * @param eventId - id
     * @param expense - the expense to delete
     * @return the response from the server
     */
    public Response deleteExpense(long eventId, Expense expense) {
        Entity<Expense> entity = Entity.entity(expense, APPLICATION_JSON);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/expenses/remove/" + eventId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(entity);
    }

    /**
     * List of expense to the server
     *
     * @param eventId - event
     * @return the response from the server
     */
    public List<Expense> getExpenses(long eventId) {
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/expenses/event/" + eventId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get();
        return null;
    }

    /**
     * Retrieves the expense by id
     * @param expenseId - the expense id
     * @return the found expense
     */
    public Expense getExpense(long expenseId) {
        Response response = ClientBuilder.newClient(new ClientConfig())
            .target(server).path("/api/expenses/id/" + expenseId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get();
        return response.readEntity(Expense.class);
    }

    /**
     * Persists an expense
     *
     * @param eventId
     * @param expense - the expense to persist
     * @return the persisted expense
     */
    public Expense updateExpense(long eventId, Expense expense) {
        Entity<Expense> entity = Entity.entity(expense, APPLICATION_JSON);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/expenses/event/" + eventId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(entity, Expense.class);
    }

    /**
     * Persists an expense
     *
     * @param expense - the expense to persist
     * @return the persisted expense
     */
    public Expense persistExpense(Expense expense) {
        Entity<Expense> entity = Entity.entity(expense, APPLICATION_JSON);
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/expenses/id/" + expense.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(entity, Expense.class);
    }

    /**
     * This should give converted currency
     *
     * @param date - the date it is looked for
     * @param from currency
     * @param to currency
     * @return the event with the specified id
     */
    public Double convertRate(String date, String from, String to) throws URISyntaxException {
        String path = "rates/"+ date +"/" + from + "/" + to + ".txt";
        URL url = ServerUtils.class.getClassLoader().getResource(path);
        String availableDates = LocalDate.now().toString();
        int y = Integer.parseInt(availableDates.substring(0,4)) - 1;
        availableDates = y + availableDates.substring(4);
        if(date == null || date.compareTo(availableDates) < 0
                || date.compareTo(LocalDate.now().toString()) > 0){
            return 1.0d;
        }
        try{
            File myObj = new File(url.toURI());
            Scanner myReader = new Scanner(myObj);
            String data = "";
            if (myReader.hasNextLine()) {
                data = myReader.nextLine();}
            myReader.close();
            return Double.parseDouble(data);
        } catch (Exception e) {
            String key = "af59b256521aa37eb850";
            String url2 = "https://free.currconv.com/api/v7/convert?q="+ from + "_"+ to +
                    "&compact=ultra&date="+ date +"&apiKey="+ key;
            Response response = ClientBuilder.newClient(new ClientConfig())
                    .target(url2)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                String rate = response.readEntity(Object.class).toString();
                if(rate.contains(to)){
                    rate = rate.split("=")[2].split("}")[0];}
                URL rateUrl = ServerUtils.class.getClassLoader().getResource("");
                Path folderPath = Paths.get(rateUrl.toURI()).resolve("rates/" + date + "/" + from);
                File folders = new File(folderPath.toString());
                folders.mkdirs();
                try{
                    URL newUrl = ServerUtils.class.getClassLoader().getResource("rates/"
                            + date + "/" + from);
                    Path ratesPath = Paths.get(newUrl.toURI());
                    Path rateFilePath = ratesPath.resolve(to+".txt");
                    Files.createFile(rateFilePath);
                    Files.writeString(rateFilePath, rate);
                    return Double.parseDouble(rate);}
                catch (Exception e1){
                    return 1.0d;}
            } else {
                return 1.0d;
            }
        }
    }

    /**
     * Show a pop up window with an alert when the client cannot connect
     * to the server
     */
    public void showAlert(){
        LanguageResourceBundle languageResourceBundle = LanguageResourceBundle.getInstance();
        ResourceBundle bundle = languageResourceBundle.getResourceBundle();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(bundle.getString("serverErrorAlertTitle"));
        alert.setHeaderText(bundle.getString("serverErrorAlertHeader"));
        alert.setContentText(bundle.getString("serverErrorAlertContext"));
        alert.showAndWait();
    }


    /**
     * Stops the client thread
     */
    public void stop() {
        EXEC.shutdownNow();
        EXEC2.shutdownNow();
        EXEC3.shutdownNow();
    }
}
