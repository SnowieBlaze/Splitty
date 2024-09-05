package client.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageResourceBundle {
    private static LanguageResourceBundle instance;
    private ResourceBundle resourceBundle;

    private LanguageResourceBundle(String language) {
        if (language == null || language.equals("")) {
            language = "en";
        }
        resourceBundle = ResourceBundle.getBundle("client.languages.Language",
                new Locale(language));
    }

    /**
     * @return the instance of the LanguageResourceBundle
     */

    public static LanguageResourceBundle getInstance() {
        if (instance == null) {
            instance = new LanguageResourceBundle(null);
        }
        return instance;
    }

    /**
     * @return the resource bundle
     */

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
     * Switches the language of the resource bundle
     * @param language the language to switch to
     */
    public void switchLanguage(String language) {
        resourceBundle = ResourceBundle.getBundle("client.languages.Language",
                new Locale(language));
    }
}
