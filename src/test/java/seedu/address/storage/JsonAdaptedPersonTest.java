package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.person.Band;
import seedu.address.model.person.Level;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.School;
import seedu.address.model.person.Subject;
import seedu.address.testutil.PersonBuilder;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_LEVEL = "S6";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().get().toString();
    private static final String VALID_SCHOOL = BENSON.getSchool().toString();
    private static final String VALID_LEVEL = BENSON.getLevel().name();
    private static final List<JsonAdaptedSubject> VALID_SUBJECTS =
            BENSON.getSubjects().stream().map(JsonAdaptedSubject::new).toList();

    @Test
    public void toModelType_invalidSchool_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, " ", VALID_LEVEL, VALID_SUBJECTS);
        assertThrows(IllegalValueException.class, School.MESSAGE_CONSTRAINTS, person::toModelType);
    }

    @Test
    public void toModelType_missingSchool_defaultsToUnknown() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, null, VALID_LEVEL, VALID_SUBJECTS);
        assertEquals(new PersonBuilder(BENSON).withSchool(School.DEFAULT_VALUE).build(), person.toModelType());
    }

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_personWithNotes_roundTripsThroughJson() throws Exception {
        Person bensonWithNotes = new PersonBuilder(BENSON)
                .withNotes("Weak in algebra", "WA2 Math: 36/50").build();
        String json = JsonUtil.toJsonString(new JsonAdaptedPerson(bensonWithNotes));

        assertEquals(bensonWithNotes, JsonUtil.fromJsonString(json, JsonAdaptedPerson.class).toModelType());
        assertEquals(bensonWithNotes,
                JsonUtil.fromJsonString(json.replace("G1", "g1"), JsonAdaptedPerson.class).toModelType());
        JsonAdaptedPerson invalidBand = JsonUtil.fromJsonString(json.replace("G1", "G4"), JsonAdaptedPerson.class);
        assertThrows(IllegalValueException.class, Band.MESSAGE_CONSTRAINTS, invalidBand::toModelType);
        JsonAdaptedPerson missingBand = JsonUtil.fromJsonString(
                json.replace("\"band\" : \"G1\"", "\"band\" : null"), JsonAdaptedPerson.class);
        assertThrows(IllegalValueException.class, Band.MESSAGE_CONSTRAINTS, missingBand::toModelType);
        JsonAdaptedPerson oldRecord = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_SCHOOL, VALID_LEVEL, null);
        assertThrows(IllegalValueException.class, Person.MESSAGE_MISSING_SUBJECT, oldRecord::toModelType);
        JsonAdaptedPerson invalidSubject = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_SCHOOL,
                VALID_LEVEL, List.of(new JsonAdaptedSubject("123", "G1")));
        assertThrows(IllegalValueException.class, Subject.MESSAGE_CONSTRAINTS, invalidSubject::toModelType);
        JsonAdaptedPerson repeatedSubject = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_SCHOOL,
                VALID_LEVEL, List.of(new JsonAdaptedSubject("English", "G1"),
                        new JsonAdaptedSubject("english", "G2")));
        assertThrows(IllegalValueException.class, "Subject is repeated: english.", repeatedSubject::toModelType);
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_SCHOOL, VALID_LEVEL, VALID_SUBJECTS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_PHONE, VALID_SCHOOL, VALID_LEVEL, VALID_SUBJECTS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_SCHOOL, VALID_LEVEL, VALID_SUBJECTS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_returnsPersonWithoutPhone() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, null, VALID_SCHOOL, VALID_LEVEL, VALID_SUBJECTS);
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
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_SCHOOL, INVALID_LEVEL,
                VALID_SUBJECTS);
        assertThrows(IllegalValueException.class, Level.MESSAGE_CONSTRAINTS, person::toModelType);
    }

    @Test
    public void toModelType_nullLevel_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_SCHOOL, null, VALID_SUBJECTS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Level.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

}
