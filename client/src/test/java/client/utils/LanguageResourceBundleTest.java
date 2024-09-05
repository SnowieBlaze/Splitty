package client.utils;

import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class LanguageResourceBundleTest {

    @Test
    void getInstance() {
        LanguageResourceBundle instance1 = LanguageResourceBundle.getInstance();
        LanguageResourceBundle instance2 = LanguageResourceBundle.getInstance();
        assertSame(instance1, instance2, "Instances are not the same");
    }

    @Test
    void getResourceBundle() {
        LanguageResourceBundle instance = LanguageResourceBundle.getInstance();
        ResourceBundle resourceBundle = instance.getResourceBundle();
        assertNotNull(resourceBundle, "ResourceBundle is null");
        assertEquals("client.languages.Language", resourceBundle.getBaseBundleName(), "Base bundle name is not correct");
    }

    @Test
    void switchLanguage() {
        LanguageResourceBundle instance = LanguageResourceBundle.getInstance();
        instance.switchLanguage("fr");
        ResourceBundle resourceBundle = instance.getResourceBundle();
        assertEquals(new Locale("en"), resourceBundle.getLocale(), "Locale did not switch correctly");
    }
}