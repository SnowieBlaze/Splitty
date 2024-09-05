package client.utils;

import java.io.*;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ConfigClient {
    private static String serverUrl;

    private static String email;

    private static String iban;

    private static String bic;

    private static String language;

    private static String currency;

    private static String name;

    private static String recentEvents;

    private Path filePath = Paths.get("client/src/main/resources/config.txt").toAbsolutePath();



    /**
     * Default constructor just in case.
     */
    public ConfigClient() {

    }

    /**
     * Constructor for ConfigClient class
     *
     * @param serverUrl    -> Url of the server
     * @param email        -> Email of client
     * @param iban         -> iban of client
     * @param bic          -> bic of client
     * @param language     -> currently preferred language of client
     * @param currency     -> currently preferred currency of client
     * @param name         -> name of client
     * @param recentEvents -> recently viewed events of client
     */
    public ConfigClient(String serverUrl, String email, String iban,
                        String bic, String language, String currency,
                        String name, String recentEvents) {
        this.serverUrl = serverUrl;
        this.email = email;
        this.iban = iban;
        this.bic = bic;
        this.language = language;
        this.currency = currency;
        this.name = name;
        this.recentEvents = recentEvents;
    }

    /**
     * Gets serverUrl
     *
     * @return serverUrl from client
     */
    public static String getServerUrl() {
        return serverUrl;
    }

    /**
     * @return email of client
     */
    public static String getEmail() {
        return email;
    }

    /**
     * @return Iban of client
     */
    public static String getIban() {
        return iban;
    }

    /**
     * @return bic of client
     */
    public static String getBic() {
        return bic;
    }

    /**
     * @return currently preferred language of client
     */
    public static String getLanguage() {
        return language;
    }

    /**
     * @return currently preferred currency of client
     */
    public static String getCurrency() {
        return currency;
    }

    /**
     * @param serverUrl new serverUrl for client
     */
    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * @param email new email for client
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param iban new iban for client
     */
    public void setIban(String iban) {
        this.iban = iban;
    }

    /**
     * @param bic new bic for client
     */
    public void setBic(String bic) {
        this.bic = bic;
    }

    /**
     * @param language new language for client
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @param currency new currency for client
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Getter for the name of the client
     *
     * @return name of the client
     */

    public static String getName() {
        return name;
    }

    /**
     * Setter for the name of the client
     *
     * @param name name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the recent events of the client
     *
     * @return recent events of the client
     */

    public static String getRecentEvents() {
        return recentEvents;
    }

    /**
     * Setter for the recent events of the client
     *
     * @param recentEventsNew recent events to set
     */

    public void setRecentEvents(String recentEventsNew) {
        recentEvents = recentEventsNew;
    }

    /**
     * method that reads a file and creates a new ConfigClient in accordance to it.
     *
     * @param path path to the file being read
     * @return null if file not found, new ConfigClient according to specifications if found
     */
    public ConfigClient readFromFile(String path) {
        try {
            InputStream configStream = getClass().getClassLoader().getResourceAsStream(path);
            if (configStream == null) {
                throw new FileNotFoundException("File not found: " + path);
            }
            Scanner configParse = new Scanner(configStream);
            String[] newClient = new String[8];
            int counter = 0;
            while (configParse.hasNextLine()) {
                String[] data = configParse.nextLine().split(": ");
                if (data.length < 2) {
                    newClient[counter] = "";
                    counter++;
                } else {
                    newClient[counter] = data[1];
                    counter++;
                }
            }
            configParse.close();
            if (!newClient[0].equals("null")) {
                ServerUtils.setURL(newClient[0]);
            }
            return new ConfigClient(newClient[0], newClient[1],
                    newClient[2], newClient[3], newClient[4], newClient[5],
                    newClient[6], newClient[7]);
        } catch (FileNotFoundException e) {
            try {
                Path configFilePath = pathAndCreate();

                try (BufferedWriter writer = new
                        BufferedWriter(new FileWriter(configFilePath.toFile()))) {
                    writer.write("serverUrl: http://localhost:8080/\n" +
                            "email: \n" +
                            "iban: null\n" +
                            "bic: null\n" +
                            "language: en\n" +
                            "currency: EUR\n" +
                            "name: null\n");
                }
            } catch (IOException | URISyntaxException exception) {
                System.out.println("An error occurred: " + exception.getMessage());
                exception.printStackTrace();
            }
            return new ConfigClient();
        }
    }

    /**
     * method for creating a file if it doesn't exist(config.txt)
     * @return the path of the file
     * @throws URISyntaxException if the URL is not correct
     * @throws IOException if the file cannot be created
     */

    private Path pathAndCreate() throws URISyntaxException, IOException {
        URL url = getClass().getClassLoader().getResource("");

        if (url == null) {
            throw new FileNotFoundException("Resources folder not found");
        }

        Path resourcesPath = Paths.get(url.toURI());
        Path configFilePath = resourcesPath.resolve("config.txt");

        Files.createFile(configFilePath);
        return configFilePath;
    }

    /**
     * method for writing config to a file at a certain path
     * it should be pretty self-explanatory,
     * creates a BufferedWriter based on file path, then writes every single element of
     * configContent there in the correct format.
     * Params can be replaced with a map of key-value pairs instead of 2 arrays.
     *
     * @param path          the path where config should be written
     * @param configContent the content of the config passed as a
     *                      String array for easy writer handling
     * @param keys          this is just the different words in the config file
     *                      for example the 'email' in email: configContent[1]
     *                      this helps with actually writing the file.
     */
    public void writeToFile(String path, String[] configContent, String[] keys) {
        try {
            URL url = getClass().getClassLoader().getResource("");
            if (url == null) {
                throw new FileNotFoundException("Resources folder not found");
            }
            Path resourcesPath = Paths.get(url.toURI());
            Path configFilePath = resourcesPath.resolve(path);

            try (BufferedWriter writer = new
                    BufferedWriter(new FileWriter(configFilePath.toFile()))) {
                for (int i = 0; i < keys.length; i++) {
                    writer.write(keys[i] + ": " + configContent[i]);
                    writer.newLine();
                }
            }
        } catch (IOException | URISyntaxException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Method for only having unique recent events
     */

    public void uniqueRecentEvents(){
        if(recentEvents == null){
            return;
        }
        String[] recentEventsArray = recentEvents.split(", ");
        StringBuilder uniqueEvents = new StringBuilder();
        for (String event : recentEventsArray) {
            if (!uniqueEvents.toString().contains(event)) {
                uniqueEvents.append(event).append(", ");
            }
        }
        recentEvents = uniqueEvents.toString();
        onlyFiveRecentEvents();
    }

    /**
     * Method for only having the five most recent events
     */
    public void onlyFiveRecentEvents(){
        String[] recentEventsArray = recentEvents.split(", ");
        StringBuilder onlyFiveEvents = new StringBuilder();
        int end = Math.min(5, recentEventsArray.length);
        for (int i = 0; i < end; i++) {
            onlyFiveEvents.append(recentEventsArray[i]).append(", ");
        }
        recentEvents = onlyFiveEvents.toString();

        this.writeToFile("config.txt",
                new String[]{serverUrl, email, iban, bic, language, currency, name, recentEvents},
                new String[]{"serverUrl",
                    "email", "iban", "bic", "language", "currency", "name", "recentEvents"});
    }
}
