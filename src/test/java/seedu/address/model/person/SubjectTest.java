package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class SubjectTest {

    @Test
    public void constructor_validSample_storesNameAndBand() {
        Subject subject = new Subject("Mathematics", Band.G1);
        assertEquals("Mathematics", subject.getName());
        assertEquals(Band.G1, subject.getBand());
    }

    @Test
    public void constructor_nullBand_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Subject("Mathematics", null));
    }
}
