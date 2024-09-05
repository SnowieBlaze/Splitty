import static org.junit.jupiter.api.Assertions.*;

import commons.Event;
import jakarta.ws.rs.client.Entity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import org.mockserver.model.Delay;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.messaging.simp.stomp.StompSession;
import utils.Admin;

import java.util.concurrent.TimeUnit;

class AdminTest {

    @Mock
    Client mockClient;

    @Mock
    WebTarget mockWebTarget;

    @Mock
    Invocation.Builder mockBuilder;

    @Mock
    Response mockResponse;
    @Mock
    StompSession mockStompSession;


    Admin admin;

    private ClientAndServer mockServer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockServer = ClientAndServer.startClientAndServer(8080);
        admin = new Admin("http://mocked-server:8080/", mockStompSession);
    }

    @Test
    void setPassword() {
        admin.setPassword("123");
        assertEquals("123", admin.getPassword());
    }

    @Test
    void getPassword() {
        assertEquals(admin.getPassword(), "none set");
    }

    @Test
    void convertUrl(){
        String url = "http://www.google.com";
        String webSocketUrl = admin.convertUrl(url);
        assertEquals("ws://www.google.com/websocket", webSocketUrl);
    }

    /*
    @Test
    void setURL() {
        admin.setURL("http://www.google.com");
        assertEquals("http://www.google.com", admin.getURL());
    }
     */
    //No longer possible to test due to the fact that mockStompSession has no url


    @Test
    void getURL() {
        assertEquals("http://mocked-server:8080/", admin.getURL());
    }

    @Test
    public void testGeneratePassword_Failure() {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/api/admin/")
        ).respond(
                HttpResponse.response()
                        .withStatusCode(400)
                        .withDelay(TimeUnit.SECONDS, 10) //simulate timeout which leads to processing error
        );
        boolean result = admin.generatePassword();
        assertFalse(result);
    }

    @Test
    public void testLoginFailure() {
        Response response = Response.ok().entity("Your response body here").build();
        String responseBody = response.getEntity().toString();
        HttpResponse httpResponse = HttpResponse.response()
                .withStatusCode(response.getStatus())
                .withBody(responseBody);
        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/api/admin/")
        ).respond(
                HttpResponse.response()
                        .withStatusCode(400)
                        .withDelay(TimeUnit.SECONDS, 10) //simulate timeout which leads to processing error
        );
        boolean result = admin.login("random");
        assertFalse(result);
    }

    @AfterEach
    public void tearDown() {
        mockServer.stop();
    }
}