package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Band;
import seedu.address.model.person.Subject;

public class JsonAdaptedSubjectTest {

    @Test
    public void toModelType_subjectWithOfficialPunctuation_returnsSubject() throws Exception {
        Subject expected = new Subject("Science (Physics, Chemistry)", Band.G3);
        assertEquals(expected, new JsonAdaptedSubject("Science (Physics, Chemistry)", "G3").toModelType());
    }

    @Test
    public void toModelType_subjectWithUnsupportedCharacter_throwsIllegalValueException() {
        JsonAdaptedSubject adapted = new JsonAdaptedSubject("Maths 2", "G1");
        assertThrows(IllegalValueException.class, Subject.MESSAGE_CONSTRAINTS, adapted::toModelType);
    }

    @Test
    public void toModelType_subjectWithUnicode_throwsIllegalValueException() {
        JsonAdaptedSubject adapted = new JsonAdaptedSubject("Français", "G3");
        assertThrows(IllegalValueException.class, Subject.MESSAGE_CONSTRAINTS, adapted::toModelType);
    }
}
