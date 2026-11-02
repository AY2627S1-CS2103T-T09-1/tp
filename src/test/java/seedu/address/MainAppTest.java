package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MainAppTest {

    @Test
    public void appMetadata_currentRelease_usesTuteeBook3Branding() {
        assertEquals("TuteeBook3", MainApp.APP_NAME);
        assertEquals("v1.5", MainApp.VERSION);
    }
}
