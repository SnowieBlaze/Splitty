package client.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import commons.*;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.*;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServerUtilsTest {

    ServerUtils serverUtils;

    private WireMockServer wireMockServer;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());

        serverUtils = new ServerUtils();
        serverUtils.setURL("http://localhost:" + wireMockServer.port());
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void sendReminder_OK() throws JsonProcessingException {
        Participant participant = new Participant();
        participant.setId(0);
        participant.setName("Test");
        participant.setEmail("test@example.com");
        participant.setIban("Test");
        participant.setBic("Test");

        ObjectMapper objectMapper = new ObjectMapper();
        String participantJson = objectMapper.writeValueAsString(participant);

        stubFor(post(urlEqualTo("/api/email/"))
                .withQueryParam("creatorEmail", equalTo("ooppteam08@gmail.com"))
                .withQueryParam("amount", equalTo("100.0"))
                .withQueryParam("email", equalTo("test@example.com"))
                .withQueryParam("eventTitle", equalTo("Test Event"))
                .withHeader("Content-Type", equalTo(APPLICATION_JSON))
                .withHeader("Accept", equalTo(APPLICATION_JSON))
                .withRequestBody(equalToJson(participantJson))
                .willReturn(aResponse()
                        .withStatus(200)));

        SwingUtilities.invokeLater(() -> {
            boolean result = serverUtils.sendReminder(participant, 100.0, "test@example.com", "Test Event");
            assertTrue(result);
        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("Task was interrupted");
        }
    }

    @Test
    void getPaymentInstructions_OK() {
        Event event = new Event();

        List<Debt> debts = new ArrayList<>();
        Debt debt = new Debt();
        debts.add(debt);

        stubFor(get(urlEqualTo("/api/debts/event/" + event.getId()))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON)
                        .withBody("[{\"id\":1}]")));

        SwingUtilities.invokeLater(() -> {
            List<Debt> result = serverUtils.getPaymentInstructions(event);
            assertEquals(1, result.size());
            assertEquals(1, result.get(0).getId());
        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("Task was interrupted");
        }
    }

    @Test
    void addEvent_OK() throws JsonProcessingException {
        Event event = new Event();
        event.setTitle("Test Event");

        ObjectMapper objectMapper = new ObjectMapper();
        String eventJson = objectMapper.writeValueAsString(event);

        stubFor(post(urlEqualTo("/api/events"))
                .withRequestBody(equalToJson(eventJson))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", APPLICATION_JSON)
                        .withBody(eventJson)));

        SwingUtilities.invokeLater(() -> {
            Event result = serverUtils.addEvent(event);
            assertEquals("Test Event", result.getTitle());
        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("Task was interrupted");
        }
    }

    @Test
    void getEventByCode_OK() throws JsonProcessingException {
        String inviteCode = "testCode";
        Event event = new Event();
        event.setInviteCode(inviteCode);

        ObjectMapper objectMapper = new ObjectMapper();
        String eventJson = objectMapper.writeValueAsString(event);

        stubFor(get(urlEqualTo("/api/events/" + inviteCode))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON)
                        .withBody(eventJson)));

        SwingUtilities.invokeLater(() -> {
            Event result = serverUtils.getEventByCode(inviteCode);
            assertEquals(inviteCode, result.getInviteCode());
        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("Task was interrupted");
        }
    }

    @Test
    void getTags_OK() {
        List<Tag> tags = new ArrayList<>();
        Tag tag = new Tag();
        tags.add(tag);

        stubFor(get(urlEqualTo("/api/tags"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON)
                        .withBody("[{\"id\":1}]")));

        SwingUtilities.invokeLater(() -> {
            List<Tag> result = serverUtils.getTags();
            assertEquals(1, result.get(0).getId());
        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("Task was interrupted");
        }
    }

    @Test
    void addTag_OK() throws JsonProcessingException {
        Tag tag = new Tag();
        tag.setType("Test Tag");

        ObjectMapper objectMapper = new ObjectMapper();
        String tagJson = objectMapper.writeValueAsString(tag);

        stubFor(post(urlEqualTo("/api/tags/add"))
                .withRequestBody(equalToJson(tagJson))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON)
                        .withBody(tagJson)));

        SwingUtilities.invokeLater(() -> {
            Tag result = serverUtils.addTag(tag);
            assertEquals("Test Tag", result.getType());
        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("Task was interrupted");
        }
    }

    @Test
    void updateTag_OK() throws JsonProcessingException {
        Tag tag = new Tag();
        tag.setType("Updated Tag");

        ObjectMapper objectMapper = new ObjectMapper();
        String tagJson = objectMapper.writeValueAsString(tag);

        stubFor(post(urlEqualTo("/api/tags/update"))
                .withRequestBody(equalToJson(tagJson))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON)
                        .withBody(tagJson)));

        SwingUtilities.invokeLater(() -> {
            Tag result = serverUtils.updateTag(tag);
            assertEquals("Updated Tag", result.getType());
        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("Task was interrupted");
        }
    }

    @Test
    void deleteTag_OK(){
        Tag tag = new Tag();

        stubFor(delete(urlEqualTo("/api/tags/" + tag.getId()))
                .willReturn(aResponse()
                        .withStatus(200)));

        SwingUtilities.invokeLater(() -> {
            Response result = serverUtils.deleteTag(tag);
            assertEquals(200, result.getStatus());
        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("Task was interrupted");
        }
    }

    @Test
    void deleteDebt_OK() {
        Debt debt = new Debt();

        stubFor(delete(urlEqualTo("/api/debts/" + debt.getId()))
                .willReturn(aResponse()
                        .withStatus(200)));

        SwingUtilities.invokeLater(() -> {
            Response result = serverUtils.deleteDebt(debt);
            assertEquals(200, result.getStatus());
        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("Task was interrupted");
        }
    }

    @Test
    void getEvent_OK() throws JsonProcessingException {
        long id = 0;
        Event event = new Event();
        event.setTitle("Test Event");

        ObjectMapper objectMapper = new ObjectMapper();
        String eventJson = objectMapper.writeValueAsString(event);

        stubFor(get(urlEqualTo("/api/events/id/" + id))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON)
                        .withBody(eventJson)));

        SwingUtilities.invokeLater(() -> {
            Event result = serverUtils.getEvent(id);
            assertEquals("Test Event", result.getTitle());
        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("Task was interrupted");
        }
    }

    @Test
    void deleteParticipant_OK() {
        Participant participant = new Participant();

        stubFor(delete(urlEqualTo("/api/participants/" + participant.getId()))
                .willReturn(aResponse()
                        .withStatus(200)));

        SwingUtilities.invokeLater(() -> {
            Response result = serverUtils.deleteParticipant(participant);
            assertEquals(200, result.getStatus());
        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("Task was interrupted");
        }
    }

    @Test
    void persistEvent_OK() throws JsonProcessingException {
        Event event = new Event();
        event.setTitle("Test Event");

        ObjectMapper objectMapper = new ObjectMapper();
        String eventJson = objectMapper.writeValueAsString(event);

        stubFor(put(urlEqualTo("/api/events/persist/" + event.getId()))
                .withRequestBody(equalToJson(eventJson))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON)
                        .withBody(eventJson)));

        SwingUtilities.invokeLater(() -> {
            Event result = serverUtils.persistEvent(event);
            assertEquals("Test Event", result.getTitle());
        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("Task was interrupted");
        }
    }

    @Test
    void sendInvites_OK() throws JsonProcessingException {
        List<String> emails = Arrays.asList("test@example.com");
        ObjectMapper objectMapper = new ObjectMapper();
        String emailsJson = objectMapper.writeValueAsString(emails);
        Event event = new Event();
        event.setInviteCode("testCode");
        String creatorname = "Test Creator";

        stubFor(post(urlEqualTo("/api/email/" + event.getInviteCode()))
                .withQueryParam("creatorName", equalTo(creatorname))
                .withQueryParam("creatorEmail", equalTo(""))
                .withRequestBody(equalToJson(emailsJson))
                .willReturn(aResponse()
                        .withStatus(200)));

        SwingUtilities.invokeLater(() -> {
            boolean result = serverUtils.sendInvites(emails, event, creatorname);
            assertTrue(result);
        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("Task was interrupted");
        }
    }

    @Test
    void sendDefault_OK() throws JsonProcessingException {
        String email = "test@example.com";
        Participant participant = new Participant(0, ConfigClient.getName(),
                email, ConfigClient.getIban(), ConfigClient.getBic());

        ObjectMapper objectMapper = new ObjectMapper();
        String participantJson = objectMapper.writeValueAsString(participant);

        stubFor(post(urlEqualTo("/api/email/default"))
                .withRequestBody(equalToJson(participantJson))
                .willReturn(aResponse()
                        .withStatus(200)));

        SwingUtilities.invokeLater(() -> {
            boolean result = serverUtils.sendDefault(email);
            assertTrue(result);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("Task was interrupted");
        }
    }

    @Test
    void registerEventUpdate_OK() throws InterruptedException {
        Consumer<Event> mockConsumer = mock(Consumer.class);
        Response mockResponse = mock(Response.class);
        Event mockEvent = mock(Event.class);

        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.readEntity(Event.class)).thenReturn(mockEvent);

        Client mockClient = mock(Client.class);

        WebTarget mockWebTarget = mock(WebTarget.class);
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
        Invocation.Builder mockBuilder = mock(Invocation.Builder.class);
        when(mockWebTarget.request(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.accept(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.get(Response.class)).thenReturn(mockResponse);

        serverUtils.registerEventUpdate(mockConsumer);

        Thread.sleep(1000);
        serverUtils.stop();
    }

    @Test
    void deleteExpense_OK() {
        Expense expense = new Expense();

        stubFor(delete(urlEqualTo("/api/expenses/remove/" + 0))
                .willReturn(aResponse()
                        .withStatus(200)));

        SwingUtilities.invokeLater(() -> {
            Response result = serverUtils.deleteExpense(0, expense);
            assertEquals(200, result.getStatus());
        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("Task was interrupted");
        }
    }

    @Test
    void convertRate_OK() {
        String date = "2022-01-01";
        String from = "USD";
        String to = "EUR";

        stubFor(get(urlEqualTo("/api/" + date + "?access_key=488b2c548074f3e5d9e15ba3013a152d&base=" + from + "&symbols=" + to))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON)
                        .withBody("{\"rates\": {\"" + to + "\": 0.85}}")));


        SwingUtilities.invokeLater(() -> {
            Double result = null;
            try {
                result = serverUtils.convertRate(date, from, to);
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
            assertEquals(0.85, result);
        });

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("Task was interrupted");
        }
    }

    @Test
    void convertRate() throws IOException, URISyntaxException {
//        Double d = server.convertRate("2023-08-12", "EUR", "USD");
//        System.out.println(d);
//        Double b = server.convertRate("2020-03-14", "USD", "CHF");
//        System.out.println(b);
        String availableDates = LocalDate.now().toString();
        int y = Integer.parseInt(availableDates.substring(0,4)) - 1;
        availableDates = y + availableDates.substring(4);
        System.out.println(availableDates);
    }
}