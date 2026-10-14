package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.person.Level;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.School;
import seedu.address.testutil.PersonBuilder;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_LEVEL = "S6";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().get().toString();
    private static final String VALID_SCHOOL = BENSON.getSchool().toString();
    private static final String VALID_LEVEL = BENSON.getLevel().name();

    @Test
    public void toModelType_invalidSchool_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, " ", VALID_LEVEL);
        assertThrows(IllegalValueException.class, School.MESSAGE_CONSTRAINTS, person::toModelType);
    }

    @Test
    public void toModelType_missingSchool_defaultsToUnknown() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, null, VALID_LEVEL);
        assertEquals(new PersonBuilder(BENSON).withSchool(School.DEFAULT_VALUE).build(), person.toModelType());
    }

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_personWithNotes_roundTripsThroughJson() throws Exception {
        Person bensonWithNotes = new Person(BENSON.getName(), BENSON.getPhone().orElse(null), BENSON.getSchool(),
                BENSON.getLevel(),
                List.of("Weak in algebra", "WA2 Math: 36/50"));
        String json = JsonUtil.toJsonString(new JsonAdaptedPerson(bensonWithNotes));

        assertEquals(bensonWithNotes, JsonUtil.fromJsonString(json, JsonAdaptedPerson.class).toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_SCHOOL, VALID_LEVEL);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_PHONE, VALID_SCHOOL, VALID_LEVEL);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_SCHOOL, VALID_LEVEL);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_returnsPersonWithoutPhone() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, null, VALID_SCHOOL, VALID_LEVEL);
        assertEquals(new PersonBuilder(BENSON).withoutPhone().build(), person.toModelType());
    }

    @Test
    public void toModelType_personWithoutPhone_roundTripsThroughJson() throws Exception {
        Person bensonWithoutPhone = new PersonBuilder(BENSON).withoutPhone().build();
        String json = JsonUtil.toJsonString(new JsonAdaptedPerson(bensonWithoutPhone));

        assertFalse(json.contains("phone"));
        assertEquals(bensonWithoutPhone, JsonUtil.fromJsonString(json, JsonAdaptedPerson.class).toModelType());
    }

    @Test
    public void toModelType_invalidLevel_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_SCHOOL, INVALID_LEVEL);
        assertThrows(IllegalValueException.class, Level.MESSAGE_CONSTRAINTS, person::toModelType);
    }

    @Test
    public void toModelType_nullLevel_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_SCHOOL, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Level.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

}
