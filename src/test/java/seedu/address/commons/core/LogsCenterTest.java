package seedu.address.commons.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LogsCenterTest {

    @Test
    public void getLogger_namedLogger_usesTuteeBook3BaseName() {
        assertEquals("tuteebook3.TestLogger", LogsCenter.getLogger("TestLogger").getName());
    }

    @Test
    public void logFile_defaultPath_usesTuteeBook3Name() {
        assertEquals("tuteebook3.log", LogsCenter.LOG_FILE);
    }
}
