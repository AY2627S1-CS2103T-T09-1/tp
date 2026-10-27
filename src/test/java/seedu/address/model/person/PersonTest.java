package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SCHOOL_BOB;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getNotes().remove(0));
        assertThrows(UnsupportedOperationException.class, () -> person.getSubjects().clear());
        assertThrows(IllegalArgumentException.class, Person.MESSAGE_MISSING_SUBJECT, () -> new PersonBuilder()
                .withSubjects().build());
        List<Subject> subjects = new ArrayList<>(person.getSubjects());
        Person copy = new Person(person.getName(), person.getPhone().orElse(null), person.getSchool(),
                person.getAddress().orElse(null), person.getEmail().orElse(null), person.getLevel(), subjects,
                person.getTags());
        subjects.clear();
        assertEquals(person.getSubjects(), copy.getSubjects());
        assertThrows(IllegalArgumentException.class, "Subject is repeated: mathematics.", () -> new PersonBuilder()
                .withSubjects(new Subject("Mathematics", Band.G1), new Subject("mathematics", Band.G2)).build());
    }

    @Test
    public void equals_differentAddress_returnsFalse() {
        Person person = new PersonBuilder().withAddress("12 Example Road").build();
        Person differentAddress = new PersonBuilder(person).withAddress("14 Example Road").build();
        assertFalse(person.equals(differentAddress));
    }

    @Test
    public void equals_differentEmail_returnsFalse() {
        Person person = new PersonBuilder().withEmail("amy@example.com").build();
        Person differentEmail = new PersonBuilder(person).withEmail("bob@example.com").build();
        assertFalse(person.equals(differentEmail));
        assertFalse(person.equals(new PersonBuilder(person).withoutEmail().build()));
    }

    @Test
    public void equals_differentTags_returnsFalse() {
        Person person = new PersonBuilder().withTags("paid").build();
        assertFalse(person.equals(new PersonBuilder(person).withTags("exams").build()));
        assertFalse(person.equals(new PersonBuilder(person).withTags().build()));
        // tags compare ignoring case
        assertEquals(person, new PersonBuilder(person).withTags("Paid").build());
    }

    @Test
    public void getTags_modifySet_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().withTags("paid").build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().clear());
    }

    @Test
    public void getNotes_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().withNotes("Weak in algebra").build();
        assertThrows(UnsupportedOperationException.class, () -> person.getNotes().remove(0));
    }

    @Test
    public void copyWithNotes_newNotes_replacesOnlyNotes() {
        Person person = new PersonBuilder(ALICE).withGuardian("Mary Tan", "91234567")
                .withSubjects(new Subject("Mathematics", Band.G1), new Subject("English", Band.G2))
                .withNotes("Weak in algebra").build();
        Person expectedCopy = new PersonBuilder(person).withNotes("WA2 Math: 36/50", "Weak in algebra")
                .build();

        Person copy = person.copyWithNotes(List.of(new Note("WA2 Math: 36/50"), new Note("Weak in algebra")));

        assertEquals(expectedCopy, copy);
        assertEquals(List.of(new Note("Weak in algebra")), person.getNotes());
    }

    @Test
    public void copyWithNotes_noPhoneNoGuardian_keepsThemEmpty() {
        Person person = new PersonBuilder(ALICE).withoutPhone().withoutGuardian().build();

        Person copy = person.copyWithNotes(List.of(new Note("Weak in algebra")));

        assertTrue(copy.getPhone().isEmpty());
        assertTrue(copy.getGuardian().isEmpty());
    }

    @Test
    public void copyWithNotes_address_keepsAddress() {
        Person person = new PersonBuilder(ALICE).withAddress("12 Example Road").build();
        assertEquals(person.getAddress(), person.copyWithNotes(List.of(new Note("lesson"))).getAddress());
    }

    @Test
    public void copyWithNotes_email_keepsEmail() {
        Person person = new PersonBuilder(ALICE).withEmail("alice@example.com").build();
        assertEquals(person.getEmail(), person.copyWithNotes(List.of(new Note("lesson"))).getEmail());
    }

    @Test
    public void copyWithNotes_tags_keepsTags() {
        Person person = new PersonBuilder(ALICE).withTags("paid", "exams").build();
        assertEquals(person.getTags(), person.copyWithNotes(List.of(new Note("lesson"))).getTags());
    }

    @Test
    public void copyWithNotes_emptyList_removesAllNotes() {
        Person person = new PersonBuilder(ALICE).withNotes("Weak in algebra", "WA2 Math: 36/50").build();

        assertEquals(new PersonBuilder(ALICE).withNotes().build(), person.copyWithNotes(List.of()));
    }

    @Test
    public void copyWithNotes_listChangedAfterCopy_copyUnchanged() {
        List<Note> notes = new ArrayList<>(List.of(new Note("Weak in algebra")));
        Person copy = ALICE.copyWithNotes(notes);

        notes.clear();

        assertEquals(List.of(new Note("Weak in algebra")), copy.getNotes());
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withSchool(VALID_SCHOOL_BOB)
                .build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns true
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSamePerson(editedBob));

        // name has trailing spaces, all other attributes same -> returns true
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertTrue(BOB.isSamePerson(editedBob));
    }

    @Test
    public void hasSameStudentKey_nameSchoolAndLevelMatchAfterNormalization_returnsTrue() {
        String repeatedSpaceName = ALICE.getName().toString().replace(" ", "   ").toLowerCase();
        Person normalizedCopy = new PersonBuilder(ALICE).withName(repeatedSpaceName)
                .withSchool(ALICE.getSchool().toString().toLowerCase()).build();
        assertTrue(ALICE.hasSameStudentKey(normalizedCopy));
    }

    @Test
    public void hasSameStudentKey_schoolOrLevelDiffers_returnsFalse() {
        Person otherSchool = new PersonBuilder(ALICE).withSchool("Victoria School").build();
        Person otherLevel = new PersonBuilder(ALICE).withLevel("S4").build();
        assertFalse(ALICE.hasSameStudentKey(otherSchool));
        assertFalse(ALICE.hasSameStudentKey(otherLevel));
    }

    @Test
    public void equals() {
        Subject maths = new Subject("Mathematics", Band.G1);
        Subject mathsCopy = new Subject(" mathematics ", Band.G1);
        assertEquals(maths, mathsCopy);
        assertEquals(maths.hashCode(), mathsCopy.hashCode());
        assertFalse(maths.equals(new Subject("Mathematics", Band.G2)));
        assertFalse(maths.equals(null));

        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));
        assertEquals(ALICE.hashCode(), aliceCopy.hashCode());

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        assertFalse(ALICE.equals(new PersonBuilder(ALICE)
                .withSubjects(new Subject("Mathematics", Band.G2)).build()));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different school -> returns false
        editedAlice = new PersonBuilder(ALICE).withSchool(VALID_SCHOOL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different notes -> returns false
        editedAlice = new PersonBuilder(ALICE).withNotes("Weak in algebra").build();
        assertFalse(ALICE.equals(editedAlice));

        // different level -> returns false
        editedAlice = new PersonBuilder(ALICE).withLevel("S5").build();
        assertFalse(ALICE.equals(editedAlice));

        // no phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withoutPhone().build();
        assertFalse(ALICE.equals(editedAlice));

        // with guardian -> returns false
        Person aliceWithGuardian = new PersonBuilder(ALICE).withGuardian("Mary Tan", "91234567").build();
        assertFalse(ALICE.equals(aliceWithGuardian));

        // different guardian -> returns false
        assertFalse(aliceWithGuardian.equals(new PersonBuilder(ALICE).withGuardian("Mary Tan", "98765432").build()));

        // same guardian -> returns true
        Person aliceWithSameGuardian = new PersonBuilder(ALICE).withGuardian("Mary Tan", "91234567").build();
        assertTrue(aliceWithGuardian.equals(aliceWithSameGuardian));
        assertEquals(aliceWithGuardian.hashCode(), aliceWithSameGuardian.hashCode());
    }

    @Test
    public void format_noPhone_showsNotProvided() {
        Person aliceWithoutPhone = new PersonBuilder(ALICE).withoutPhone().build();
        assertEquals("Alice Pauline; Student phone: " + Messages.MESSAGE_NOT_PROVIDED
                + "; School: Maplewood Secondary School; Level: S1; Subjects: Mathematics (G1); Address: "
                + Messages.MESSAGE_NOT_PROVIDED + "; Email: " + Messages.MESSAGE_NOT_PROVIDED + "; Tags: "
                + Messages.MESSAGE_NOT_PROVIDED, Messages.format(aliceWithoutPhone));
    }

    @Test
    public void format_withLevel_includesLevel() {
        assertEquals("Alice Pauline; Student phone: 94351253; School: Maplewood Secondary School; Level: S1; "
                + "Subjects: Mathematics (G1); Address: " + Messages.MESSAGE_NOT_PROVIDED + "; Email: "
                + Messages.MESSAGE_NOT_PROVIDED + "; Tags: " + Messages.MESSAGE_NOT_PROVIDED,
                Messages.format(ALICE));
    }

    @Test
    public void format_withGuardian_includesGuardian() {
        Person aliceWithGuardian = new PersonBuilder(ALICE).withGuardian("Mary Tan", "91234567").build();
        assertEquals(Messages.format(ALICE) + "; Guardian name: Mary Tan; Guardian phone: 91234567",
                Messages.format(aliceWithGuardian));
    }

    @Test
    public void format_withTags_includesTagsInOrder() {
        Person aliceWithTags = new PersonBuilder(ALICE).withTags("paid", "exams").build();
        assertEquals("Alice Pauline; Student phone: 94351253; School: Maplewood Secondary School; Level: S1; "
                + "Subjects: Mathematics (G1); Address: " + Messages.MESSAGE_NOT_PROVIDED + "; Email: "
                + Messages.MESSAGE_NOT_PROVIDED + "; Tags: paid, exams", Messages.format(aliceWithTags));
    }

    @Test
    public void format_multipleSubjects_commaSeparated() {
        Person aliceWithTwoSubjects = new PersonBuilder(ALICE)
                .withSubjects(new Subject("Mathematics", Band.G1), new Subject("Science", Band.G2)).build();
        assertEquals("Alice Pauline; Student phone: 94351253; School: Maplewood Secondary School; Level: S1; "
                + "Subjects: Mathematics (G1), Science (G2); Address: " + Messages.MESSAGE_NOT_PROVIDED
                + "; Email: " + Messages.MESSAGE_NOT_PROVIDED + "; Tags: " + Messages.MESSAGE_NOT_PROVIDED,
                Messages.format(aliceWithTwoSubjects));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone="
                + ALICE.getPhone().get() + ", school=" + ALICE.getSchool() + ", address=null, email=null, level="
                + ALICE.getLevel()
                + ", subjects=" + ALICE.getSubjects() + ", tags=" + ALICE.getTags() + ", guardian=null"
                + "}";
        assertEquals(expected, ALICE.toString());
    }
}
