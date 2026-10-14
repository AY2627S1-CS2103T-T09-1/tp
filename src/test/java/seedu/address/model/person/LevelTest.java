package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class LevelTest {

    @Test
    public void fromString_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Level.fromString(null));
    }

    @Test
    public void fromString_validLevel_returnsLevel() {
        assertEquals(Level.S1, Level.fromString("S1"));
        assertEquals(Level.S1, Level.fromString("s1")); // lowercase
        assertEquals(Level.S3, Level.fromString(" S3 ")); // surrounding spaces
        assertEquals(Level.S5, Level.fromString("S5"));
    }

    @Test
    public void fromString_invalidLevel_throwsIllegalArgumentException() {
        assertInvalid(""); // empty
        assertInvalid("S0"); // below range
        assertInvalid("S6"); // above range
        assertInvalid("Sec 1"); // long form
        assertInvalid("G1"); // a band, not a level
    }

    private void assertInvalid(String level) {
        assertThrows(IllegalArgumentException.class, Level.MESSAGE_CONSTRAINTS, () -> Level.fromString(level));
    }
}
