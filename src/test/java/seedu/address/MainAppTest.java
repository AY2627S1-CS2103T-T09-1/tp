package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;

public class MainAppTest {
    private static final String MESSAGE_INITIALIZING =
            "====================[ Initializing TuteeBook3 ]====================";
    private static final String MESSAGE_STARTING = "Starting TuteeBook3 v1.6";
    private static final String MESSAGE_STOPPING =
            "====================[ Stopping TuteeBook3 ]====================";

    private final TestLogHandler logHandler = new TestLogHandler();
    private Logger mainAppLogger;

    @BeforeEach
    public void setUp() throws ClassNotFoundException {
        Class.forName(MainApp.class.getName());
        mainAppLogger = LogsCenter.getLogger(MainApp.class);
        mainAppLogger.setUseParentHandlers(false);
        mainAppLogger.addHandler(logHandler);
    }

    @AfterEach
    public void tearDown() {
        mainAppLogger.removeHandler(logHandler);
        mainAppLogger.setUseParentHandlers(true);
    }

    @Test
    public void appMetadata_currentRelease_usesTuteeBook3Branding() {
        assertEquals("TuteeBook3", MainApp.APP_NAME);
        assertEquals("v1.6", MainApp.VERSION);
    }

    @Test
    public void init_newApplication_logsTuteeBook3Name() throws Exception {
        MainApp mainApp = new MainAppWithoutPreferences();

        mainApp.init();

        assertTrue(logHandler.contains(MESSAGE_INITIALIZING));
    }

    @Test
    public void start_stubUi_logsTuteeBook3Version() {
        MainApp mainApp = new MainApp();
        mainApp.ui = primaryStage -> { };

        mainApp.start(null);

        assertTrue(logHandler.contains(MESSAGE_STARTING));
    }

    @Test
    public void stop_temporaryStorage_logsTuteeBook3Name(@TempDir Path temporaryFolder) {
        MainApp mainApp = new MainApp();
        mainApp.model = new ModelManager();
        mainApp.storage = new StorageManager(
                new JsonAddressBookStorage(temporaryFolder.resolve("addressbook.json")),
                new JsonUserPrefsStorage(temporaryFolder.resolve("preferences.json")));

        mainApp.stop();

        assertTrue(logHandler.contains(MESSAGE_STOPPING));
    }

    private static class MainAppWithoutPreferences extends MainApp {
        @Override
        protected UserPrefs initPrefs(JsonUserPrefsStorage storage) {
            return new UserPrefs();
        }
    }

    private static class TestLogHandler extends Handler {
        private final List<String> messages = new ArrayList<>();

        @Override
        public void publish(LogRecord record) {
            messages.add(record.getMessage());
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }

        private boolean contains(String message) {
            return messages.contains(message);
        }
    }
}
