package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;

public class JsonAdaptedTagTest {

    @Test
    public void toModelType_validTagName_returnsTag() throws Exception {
        assertEquals(new Tag("exams"), new JsonAdaptedTag("exams").toModelType());
    }

    @Test
    public void toModelType_invalidTagName_throwsIllegalValueException() {
        JsonAdaptedTag tag = new JsonAdaptedTag("exam_prep");
        assertThrows(IllegalValueException.class, Tag.MESSAGE_CONSTRAINTS, tag::toModelType);
        JsonAdaptedTag twoWords = new JsonAdaptedTag("exam prep");
        assertThrows(IllegalValueException.class, Tag.MESSAGE_CONSTRAINTS, twoWords::toModelType);
    }

    @Test
    public void toModelType_nullTagName_throwsIllegalValueException() {
        JsonAdaptedTag tag = new JsonAdaptedTag((String) null);
        assertThrows(IllegalValueException.class, Tag.MESSAGE_CONSTRAINTS, tag::toModelType);
    }

    @Test
    public void constructor_tag_savesTagName() {
        assertEquals("Exams", new JsonAdaptedTag(new Tag("Exams")).getTagName());
    }
}
