package client;

import client.utils.ConfigClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigClientTest {

    private final String testFilePath = "src/test/resources/test_config";
    private final String[] expectedConfig = {"generic","blabla@gmail.com",
        "1234567890", "BANK123", "en", "EUR", "alex", "1bA43fD"};
    private final String[] keys = {"serverUrl", "email", "iban", "bic", "language", "currency"};
    private ConfigClient configClient = new ConfigClient(expectedConfig[0], expectedConfig[1],
            expectedConfig[2], expectedConfig[3], expectedConfig[4], expectedConfig[5],
            expectedConfig[6], expectedConfig[7]);

    @Test
    void readFromFile() {
        ConfigClient result = configClient.readFromFile(testFilePath);
        assertNotNull(result);
        assertEquals(expectedConfig[0], result.getServerUrl());
        assertEquals(expectedConfig[1], result.getEmail());
        assertEquals(expectedConfig[2], result.getIban());
        assertEquals(expectedConfig[3], result.getBic());
        assertEquals(expectedConfig[4], result.getLanguage());
        assertEquals(expectedConfig[5], result.getCurrency());
    }

    @Test
    void writeToFile() {
        configClient.writeToFile(testFilePath, expectedConfig, keys);

        ConfigClient result = configClient.readFromFile(testFilePath);


        assertNotNull(result);


        assertEquals(expectedConfig[0], result.getServerUrl());
        assertEquals(expectedConfig[1], result.getEmail());
        assertEquals(expectedConfig[2], result.getIban());
        assertEquals(expectedConfig[3], result.getBic());
        assertEquals(expectedConfig[4], result.getLanguage());
        assertEquals(expectedConfig[5], result.getCurrency());
    }

    @Test
    void setterAndGetter() {
        assertEquals(expectedConfig[0], configClient.getServerUrl());
        assertEquals(expectedConfig[1], configClient.getEmail());
        assertEquals(expectedConfig[2], configClient.getIban());
        assertEquals(expectedConfig[3], configClient.getBic());
        assertEquals(expectedConfig[4], configClient.getLanguage());
        assertEquals(expectedConfig[5], configClient.getCurrency());

        configClient.setServerUrl("1");
        configClient.setEmail("2");
        configClient.setIban("3");
        configClient.setBic("4");
        configClient.setLanguage("5");
        configClient.setCurrency("6");

        assertEquals("1", configClient.getServerUrl());
        assertEquals("2", configClient.getEmail());
        assertEquals("3", configClient.getIban());
        assertEquals("4", configClient.getBic());
        assertEquals("5", configClient.getLanguage());
        assertEquals("6", configClient.getCurrency());
    }
}