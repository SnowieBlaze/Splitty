package client.utils;

import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LanguageButtonUtils {

    private static String imagesFolderPath = "client/images/flags/";
    private static String filePath = "config.txt";



    /**
     * Updates the language menu button with the current language and flags
     * @param languageButton the language menu button
     * @param config the config client
     */

    public static void updateLanguageMenuButton(MenuButton languageButton, ConfigClient config) {

        languageButton.setLayoutX(482.0);
        languageButton.setLayoutY(345.0);
        languageButton.setMnemonicParsing(false);
        languageButton.getStyleClass().add("language-menu-button");


        createMenuItems(languageButton, config);
    }

    /**
     * Creates the menu items for the language menu button
     * @param languageButton the language menu button
     * @param config the config client
     */

    private static void createMenuItems(MenuButton languageButton, ConfigClient config) {
        MenuItem questionItem = new MenuItem();
        questionItem.setText("New");
        languageButton.getItems().add(questionItem);
        URL url = LanguageButtonUtils.class.getClassLoader().getResource(imagesFolderPath);
        if (url == null) {
            throw new RuntimeException("Resources folder not found");
        }

        File imagesFolder;
        try {
            imagesFolder = new File(url.toURI());
        } catch (URISyntaxException e) {
            imagesFolder = new File(url.getPath());
        }

        File[] imageFiles = imagesFolder.listFiles((dir, name)
                -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg"));

        if(imageFiles == null){
            throw new RuntimeException(
                    "No image files found in the directory: " + imagesFolderPath);
        }

        for (File imageFile : imageFiles) {
            String imageName = imageFile.getName();
            String imagePath = imagesFolderPath + imageName;
            URL imageUrl = LanguageButtonUtils.class.getClassLoader().getResource(imagePath);
            if (imageUrl == null) {
                throw new RuntimeException("Image file not found: " + imagePath);
            }

            Image image = new Image(imageUrl.toString());
            ImageView imageView = new ImageView(image);

            if(imageName.equals(config.getLanguage() + ".png")){
                languageButton.setGraphic(imageView);
                String menuButtonText = imageName.substring(0, imageName.lastIndexOf('.'));
                languageButton.setText(menuButtonText.toUpperCase());
                continue;
            }

            MenuItem menuItem = new MenuItem();
            menuItem.setGraphic(imageView);
            String menuItemText = imageName.substring(0, imageName.lastIndexOf('.'));
            menuItem.setText(menuItemText.toUpperCase());

            languageButton.getItems().add(menuItem);
        }
    }

    /**
     * Changes the language of the application + the language in config.
     * @param languageButton the language menu button
     * @param config the config client
     * @param languageResourceBundle the language resource bundle
     * @param initializer the initializer
     * @param keys the keys for the config file
     */

    public static void languageMenu(MenuButton languageButton, ConfigClient config,
                                    LanguageResourceBundle languageResourceBundle,
                                    Runnable initializer, String[] keys) {

        MenuItem downloadItem = (MenuItem) languageButton.getItems().get(0);
        downloadItem.setOnAction(event -> {
            downloadButtonAction();
        });

        for (int i = 1; i < languageButton.getItems().size(); i++){
            MenuItem menuItem = (MenuItem) languageButton.getItems().get(i);
            menuItem.setOnAction(event -> {
                String language = menuItem.getText().toLowerCase();
                languageResourceBundle.switchLanguage(language);

                config.setLanguage(language);
                String[] contents = {config.getServerUrl(), config.getEmail(),
                        config.getIban(), config.getBic(),
                        config.getLanguage(), config.getCurrency(),
                        config.getName(), config.getRecentEvents()};

                config.writeToFile(filePath, contents, keys);

                ImageView menuItemImageView = (ImageView) menuItem.getGraphic();
                ImageView menuButtonImageView = (ImageView) languageButton.getGraphic();

                Image tempImage = menuButtonImageView.getImage();
                menuButtonImageView.setImage(menuItemImageView.getImage());
                menuItemImageView.setImage(tempImage);

                String tempText = languageButton.getText();
                languageButton.setText(menuItem.getText());
                menuItem.setText(tempText);

                initializer.run();
            });
        }
    }

    private static void downloadButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        fileChooser.setInitialFileName("languageTemplate.txt");
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try {
                String path = "client/languages/Language_en.properties";
                URL url = LanguageButtonUtils.class.getClassLoader().getResource(path);
                if (url == null) {
                    throw new RuntimeException("Resources folder not found");
                }
                Path languageFilePath = null;

                try {
                    languageFilePath = Paths.get(url.toURI());
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }

                List<String> lines = Files.readAllLines(languageFilePath, StandardCharsets.UTF_8);
                lines.add(0, "# Language file template");
                lines.add(1, "# Please fill in the translations for the following keys");
                lines.add(2,
                        "# Save the file as Language_xx.properties where xx is the language code");
                lines.add(3, "# For example: Language_fr.properties for French");
                lines.add(4, "# The language code should be the same as the flag image name");
                lines.add(5, "# Then email us at oopteam8@gmail.com" +
                                " with this file and and image of the flag");
                Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
