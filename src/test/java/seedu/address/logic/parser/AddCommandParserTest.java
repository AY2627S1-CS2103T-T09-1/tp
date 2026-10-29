package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.GUARDIAN_NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.GUARDIAN_NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.GUARDIAN_PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.GUARDIAN_PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_GUARDIAN_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_GUARDIAN_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_GUARDIAN_PHONE_MESSAGE;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_LEVEL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_MESSAGE;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.LEVEL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.LEVEL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.SCHOOL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.SCHOOL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_EXAMS;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_PAID;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GUARDIAN_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GUARDIAN_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_EXAMS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_PAID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.person.Address;
import seedu.address.model.person.Band;
import seedu.address.model.person.Email;
import seedu.address.model.person.Guardian;
import seedu.address.model.person.Level;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.School;
import seedu.address.model.person.Subject;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class AddCommandParserTest {
    private static final String SUBJECT_DESC = " sub/Mathematics band/G1";
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_invalidSchool_failure() {
        String person = NAME_DESC_BOB + PHONE_DESC_BOB + LEVEL_DESC_BOB;
        String missingSchoolMessage = "Missing required parameter: sch/.";
        assertParseFailure(parser, person + SUBJECT_DESC, missingSchoolMessage);
        assertParseFailure(parser, person + "sch/Victoria School" + SUBJECT_DESC, missingSchoolMessage);
        assertParseFailure(parser, person + " sch/  " + SUBJECT_DESC, School.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, person + SCHOOL_DESC_BOB + " sch/Other School" + SUBJECT_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SCHOOL));
    }

    @Test
    public void parse_schoolWithUnsupportedCharacters_failure() {
        String person = NAME_DESC_BOB + PHONE_DESC_BOB + LEVEL_DESC_BOB;
        for (String school : List.of("École Française", "St Josephs School.",
                "Anglo Chinese School (Independent)", "School & Technology")) {
            assertParseFailure(parser, person + " sch/" + school + SUBJECT_DESC, School.MESSAGE_CONSTRAINTS);
        }
    }

    @Test
    public void parse_nonAsciiName_failure() {
        String details = PHONE_DESC_BOB + LEVEL_DESC_BOB + SCHOOL_DESC_BOB + SUBJECT_DESC;
        assertParseFailure(parser, " n/Zoë Lim" + details, Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB).build();
        String details = NAME_DESC_BOB + PHONE_DESC_BOB + LEVEL_DESC_BOB + SCHOOL_DESC_BOB;
        String[] names = {"Mathematics", "English", "Science", "History", "Geography", "Literature",
            "Physics", "Chemistry"};
        Subject[] subjects = new Subject[names.length];
        String eightSubjectsCommand = details;
        for (int i = 0; i < names.length; i++) {
            subjects[i] = new Subject(names[i], Band.G1);
            eightSubjectsCommand += " sub/" + names[i] + " band/G1";
        }
        assertParseSuccess(parser, eightSubjectsCommand,
                new AddCommand(new PersonBuilder(BOB).withSubjects(subjects).build()));
        Person withSubjects = new PersonBuilder(BOB).withSubjects(
                new Subject("Mathematics", Band.G1), new Subject("Additional Mathematics", Band.G3)).build();
        assertParseSuccess(parser, details + " sub/Mathematics band/g1 sub/Additional Mathematics band/g3",
                new AddCommand(withSubjects));
        assertParseSuccess(parser, details + " sub/" + "A".repeat(40) + " band/G2",
                new AddCommand(new PersonBuilder(BOB)
                        .withSubjects(new Subject("A".repeat(40), Band.G2)).build()));

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + LEVEL_DESC_BOB + SCHOOL_DESC_BOB + NAME_DESC_BOB
                + PHONE_DESC_BOB + SUBJECT_DESC,
                new AddCommand(expectedPerson));

        // fields in a different order
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + LEVEL_DESC_BOB + SCHOOL_DESC_BOB + SUBJECT_DESC,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_phoneMissing_success() {
        Person expectedPerson = new PersonBuilder(BOB).withoutPhone().build();
        assertParseSuccess(parser, NAME_DESC_BOB + LEVEL_DESC_BOB + SCHOOL_DESC_BOB + SUBJECT_DESC,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_addressProvided_success() {
        Person expectedPerson = new PersonBuilder(BOB).withoutPhone().withAddress(VALID_ADDRESS_BOB).build();
        assertParseSuccess(parser, NAME_DESC_BOB + SCHOOL_DESC_BOB + ADDRESS_DESC_BOB + LEVEL_DESC_BOB
                + SUBJECT_DESC, new AddCommand(expectedPerson));
    }

    @Test
    public void parse_blankAddress_failure() {
        assertParseFailure(parser, NAME_DESC_BOB + SCHOOL_DESC_BOB + " a/ " + LEVEL_DESC_BOB + SUBJECT_DESC,
                Address.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_emailProvided_success() {
        Person expectedPerson = new PersonBuilder(BOB).withoutPhone().withEmail(VALID_EMAIL_BOB).build();
        assertParseSuccess(parser, NAME_DESC_BOB + SCHOOL_DESC_BOB + EMAIL_DESC_BOB + LEVEL_DESC_BOB
                + SUBJECT_DESC, new AddCommand(expectedPerson));
    }

    @Test
    public void parse_invalidEmail_failure() {
        assertParseFailure(parser, NAME_DESC_BOB + SCHOOL_DESC_BOB + INVALID_EMAIL_DESC + LEVEL_DESC_BOB
                + SUBJECT_DESC, String.format(Email.MESSAGE_MISSING_AT, "bob!yahoo"));
        // e/ with no value is invalid when adding
        assertParseFailure(parser, NAME_DESC_BOB + SCHOOL_DESC_BOB + " e/ " + LEVEL_DESC_BOB + SUBJECT_DESC,
                Email.MESSAGE_EMPTY);
    }

    @Test
    public void parse_repeatedEmail_failure() {
        assertParseFailure(parser, NAME_DESC_BOB + SCHOOL_DESC_BOB + EMAIL_DESC_AMY + EMAIL_DESC_BOB
                + LEVEL_DESC_BOB + SUBJECT_DESC, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));
    }

    @Test
    public void parse_tagsProvided_success() {
        Person expectedPerson = new PersonBuilder(BOB).withoutPhone()
                .withTags(VALID_TAG_PAID, VALID_TAG_EXAMS).build();
        // tags keep the order they were typed in
        assertParseSuccess(parser, NAME_DESC_BOB + SCHOOL_DESC_BOB + LEVEL_DESC_BOB + SUBJECT_DESC
                + TAG_DESC_PAID + TAG_DESC_EXAMS, new AddCommand(expectedPerson));
    }

    @Test
    public void parse_invalidTag_failure() {
        assertParseFailure(parser, NAME_DESC_BOB + SCHOOL_DESC_BOB + LEVEL_DESC_BOB + SUBJECT_DESC
                + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS);
        // t/ with no value is invalid when adding
        assertParseFailure(parser, NAME_DESC_BOB + SCHOOL_DESC_BOB + LEVEL_DESC_BOB + SUBJECT_DESC + " t/ ",
                Tag.MESSAGE_CONSTRAINTS);
        // a tag is one word; add collapses the repeated spaces before the tag is checked
        assertParseFailure(parser, NAME_DESC_BOB + SCHOOL_DESC_BOB + LEVEL_DESC_BOB + SUBJECT_DESC
                + " t/exam   prep", String.format(Messages.MESSAGE_TAG_HAS_SPACE, "exam prep"));
    }

    @Test
    public void parse_repeatedTag_failure() {
        assertParseFailure(parser, NAME_DESC_BOB + SCHOOL_DESC_BOB + LEVEL_DESC_BOB + SUBJECT_DESC
                + " t/Paid" + TAG_DESC_PAID, String.format(Tag.MESSAGE_DUPLICATE_TAG, VALID_TAG_PAID));
    }

    @Test
    public void parse_repeatedSpacesInTextFields_collapsesSpaces() {
        Person expectedPerson = new PersonBuilder(BOB)
                .withName("John Doe")
                .withoutPhone()
                .withSchool("Victoria School")
                .withLevel("S2")
                .withSubjects(new Subject("Additional Mathematics", Band.G2))
                .withGuardian("Mary Jane", VALID_GUARDIAN_PHONE_BOB)
                .build();
        String args = " n/  John   Doe  sch/  Victoria   School  lvl/S2 sub/  Additional   Mathematics  band/G2 "
                + "gn/  Mary   Jane  gp/" + VALID_GUARDIAN_PHONE_BOB;

        assertParseSuccess(parser, args, new AddCommand(expectedPerson));
    }

    @Test
    public void parse_prefixWithoutSpace_failure() {
        String hint = Messages.MESSAGE_PREFIX_WITHOUT_SPACE_HINT;
        String details = SCHOOL_DESC_BOB + LEVEL_DESC_BOB;

        // no space before p/, so it is read as part of the name
        assertParseFailure(parser, " n/John Doep/98765432" + details + SUBJECT_DESC,
                Name.MESSAGE_CONSTRAINTS + "\n" + hint);

        // no space before gp/, so the guardian phone seems missing
        assertParseFailure(parser, NAME_DESC_BOB + details + SUBJECT_DESC + " gn/Mary Tangp/91234567",
                Guardian.MESSAGE_NAME_CONSTRAINTS + "\n" + hint);

        // no space before gp/, so it is read as part of the phone
        String phoneMessage = String.format(Phone.MESSAGE_INVALID_CHARACTER, ParserUtil.PHONE_LABEL,
                "91234567gp/92345678", "g");
        assertParseFailure(parser, NAME_DESC_BOB + " p/91234567gp/92345678" + details + SUBJECT_DESC,
                phoneMessage + "\n" + hint);

        // no space before band/, so the band seems missing
        assertParseFailure(parser, NAME_DESC_BOB + details + " sub/Englishband/G1",
                Subject.MESSAGE_CONSTRAINTS + "\n" + hint);

        // a surname ending in g before p/ looks like gp/, so the hint names no prefix
        assertParseFailure(parser, " n/Alex Ngp/91234567" + details + SUBJECT_DESC,
                Name.MESSAGE_CONSTRAINTS + "\n" + hint);

        // no space before gn/ after gp/, so the guardian name seems missing
        String guardianPhoneMessage = String.format(Phone.MESSAGE_INVALID_CHARACTER,
                ParserUtil.GUARDIAN_PHONE_LABEL, "91234567gn/Mary Tan", "g");
        assertParseFailure(parser, NAME_DESC_BOB + details + SUBJECT_DESC + " gp/91234567gn/Mary Tan",
                guardianPhoneMessage + "\n" + hint);
    }

    @Test
    public void parse_formattedPhone_savesCleanedPhone() {
        Person expectedPerson = new PersonBuilder(BOB).withPhone("91234567").build();
        assertParseSuccess(parser, NAME_DESC_BOB + " " + PREFIX_PHONE + "+65 9123 4567" + LEVEL_DESC_BOB
                + SCHOOL_DESC_BOB + SUBJECT_DESC, new AddCommand(expectedPerson));
    }

    @Test
    public void parse_withGuardian_success() {
        Person expectedPerson = new PersonBuilder(BOB)
                .withGuardian(VALID_GUARDIAN_NAME_BOB, VALID_GUARDIAN_PHONE_BOB).build();
        String details = NAME_DESC_BOB + PHONE_DESC_BOB + LEVEL_DESC_BOB + SCHOOL_DESC_BOB + SUBJECT_DESC;

        assertParseSuccess(parser, details + GUARDIAN_NAME_DESC_BOB + GUARDIAN_PHONE_DESC_BOB,
                new AddCommand(expectedPerson));

        // guardian before other fields, in reverse order, with extra spaces
        assertParseSuccess(parser, " " + PREFIX_GUARDIAN_PHONE + "  " + VALID_GUARDIAN_PHONE_BOB + "  "
                + PREFIX_GUARDIAN_NAME + "  " + VALID_GUARDIAN_NAME_BOB + "  " + details,
                new AddCommand(expectedPerson));

        // student without a phone can still have a guardian
        assertParseSuccess(parser, NAME_DESC_BOB + LEVEL_DESC_BOB + SCHOOL_DESC_BOB + SUBJECT_DESC
                + GUARDIAN_NAME_DESC_BOB + GUARDIAN_PHONE_DESC_BOB,
                new AddCommand(new PersonBuilder(expectedPerson).withoutPhone().build()));
    }

    @Test
    public void parse_guardianPartlyGiven_failure() {
        String details = NAME_DESC_BOB + PHONE_DESC_BOB + LEVEL_DESC_BOB + SCHOOL_DESC_BOB + SUBJECT_DESC;

        // name only
        assertParseFailure(parser, details + GUARDIAN_NAME_DESC_BOB, Messages.MESSAGE_GUARDIAN_PAIR);

        // phone only
        assertParseFailure(parser, details + GUARDIAN_PHONE_DESC_BOB, Messages.MESSAGE_GUARDIAN_PAIR);
    }

    @Test
    public void parse_invalidGuardian_failure() {
        String details = NAME_DESC_BOB + PHONE_DESC_BOB + LEVEL_DESC_BOB + SCHOOL_DESC_BOB + SUBJECT_DESC;

        // invalid name
        assertParseFailure(parser, details + INVALID_GUARDIAN_NAME_DESC + GUARDIAN_PHONE_DESC_BOB,
                Guardian.MESSAGE_NAME_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, details + GUARDIAN_NAME_DESC_BOB + INVALID_GUARDIAN_PHONE_DESC,
                INVALID_GUARDIAN_PHONE_MESSAGE);

        // empty values
        assertParseFailure(parser, details + " " + PREFIX_GUARDIAN_NAME + GUARDIAN_PHONE_DESC_BOB,
                Guardian.MESSAGE_NAME_CONSTRAINTS);
        assertParseFailure(parser, details + GUARDIAN_NAME_DESC_BOB + " " + PREFIX_GUARDIAN_PHONE + "  ",
                "Guardian phone cannot be empty.");

        // repeated prefixes
        assertParseFailure(parser, details + GUARDIAN_NAME_DESC_AMY + GUARDIAN_NAME_DESC_BOB
                + GUARDIAN_PHONE_DESC_BOB, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_GUARDIAN_NAME));
        assertParseFailure(parser, details + GUARDIAN_NAME_DESC_BOB + GUARDIAN_PHONE_DESC_AMY
                + GUARDIAN_PHONE_DESC_BOB, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_GUARDIAN_PHONE));
    }

    @Test
    public void parse_emptyPhone_failure() {
        // p/ with no number is rejected; leave out p/ to add a student without a phone
        assertParseFailure(parser, NAME_DESC_BOB + " " + PREFIX_PHONE + LEVEL_DESC_BOB + SCHOOL_DESC_BOB + SUBJECT_DESC,
                "Phone cannot be empty.");
        assertParseFailure(parser, NAME_DESC_BOB + LEVEL_DESC_BOB + SCHOOL_DESC_BOB + " " + PREFIX_PHONE + "  "
                + SUBJECT_DESC,
                "Phone cannot be empty.");
    }

    @Test
    public void parse_repeatedValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB + LEVEL_DESC_BOB + SCHOOL_DESC_BOB;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString + SUBJECT_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString + SUBJECT_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple levels
        assertParseFailure(parser, LEVEL_DESC_AMY + validExpectedPersonString + SUBJECT_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_LEVEL));

        // multiple schools
        assertParseFailure(parser, SCHOOL_DESC_AMY + validExpectedPersonString + SUBJECT_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SCHOOL));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_AMY + NAME_DESC_AMY + validExpectedPersonString + SUBJECT_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_PHONE, PREFIX_SCHOOL, PREFIX_LEVEL));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString + SUBJECT_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString + SUBJECT_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC + SUBJECT_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC + SUBJECT_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        // missing subject and band
        assertParseFailure(parser, NAME_DESC_BOB + SCHOOL_DESC_BOB + LEVEL_DESC_BOB,
                "Missing required parameters: sub/, band/.");

        // band given without its subject
        assertParseFailure(parser, NAME_DESC_BOB + SCHOOL_DESC_BOB + LEVEL_DESC_BOB + " band/G1",
                "Missing required parameter: sub/.");

        // missing level prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + SCHOOL_DESC_BOB + SUBJECT_DESC,
                "Missing required parameter: lvl/.");
    }

    @Test
    public void parse_missingName_failure() {
        assertParseFailure(parser, SCHOOL_DESC_BOB + LEVEL_DESC_BOB + PHONE_DESC_BOB + SUBJECT_DESC,
                "Missing required parameter: n/.");
    }

    @Test
    public void parse_missingSchoolAndLevel_failure() {
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + SUBJECT_DESC,
                "Missing required parameters: sch/, lvl/.");
    }

    @Test
    public void parse_allRequiredMissing_failure() {
        assertParseFailure(parser, PHONE_DESC_BOB, "Missing required parameters: n/, sch/, lvl/, sub/, band/.");
    }

    @Test
    public void parse_noParameters_showsUsage() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, PREAMBLE_WHITESPACE, expectedMessage);
    }

    @Test
    public void parse_preambleWithMissingPrefix_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // name typed without n/
        assertParseFailure(parser, VALID_NAME_BOB + SCHOOL_DESC_BOB + LEVEL_DESC_BOB + SUBJECT_DESC,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + SUBJECT_DESC, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, LEVEL_DESC_BOB + SCHOOL_DESC_BOB + INVALID_NAME_DESC + PHONE_DESC_BOB + SUBJECT_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, LEVEL_DESC_BOB + SCHOOL_DESC_BOB + NAME_DESC_BOB + INVALID_PHONE_DESC + SUBJECT_DESC,
                INVALID_PHONE_MESSAGE);

        // invalid level
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_LEVEL_DESC + SCHOOL_DESC_BOB + SUBJECT_DESC,
                Level.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, LEVEL_DESC_BOB + SCHOOL_DESC_BOB + INVALID_NAME_DESC + INVALID_PHONE_DESC
                + SUBJECT_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // removed prefixes are no longer recognized, so their text joins the preceding value
        assertParseFailure(parser, NAME_DESC_BOB + " x/Clementi Road" + PHONE_DESC_BOB + LEVEL_DESC_BOB
                + SCHOOL_DESC_BOB + SUBJECT_DESC,
                Name.MESSAGE_CONSTRAINTS + "\n"
                + Messages.MESSAGE_PREFIX_WITHOUT_SPACE_HINT);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + LEVEL_DESC_BOB
                + SCHOOL_DESC_BOB + SUBJECT_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        String details = NAME_DESC_BOB + SCHOOL_DESC_BOB + LEVEL_DESC_BOB;
        for (String pairs : List.of(" sub/English", " band/G1 sub/English band/G2",
                " sub/English sub/Science band/G1", " sub/English band/G1 band/G2",
                " sub/English p/91234567 band/G1")) {
            assertParseFailure(parser, details + pairs, Messages.MESSAGE_SUBJECT_BAND_PAIR);
        }
        for (String name : List.of("", "---", "Maths2", "A".repeat(41))) {
            assertParseFailure(parser, details + " sub/" + name + " band/G1", Subject.MESSAGE_CONSTRAINTS);
        }
        assertParseFailure(parser, details + " sub/English band/", Band.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, details + " sub/English band/G4", Band.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, details + SUBJECT_DESC.repeat(9), Person.MESSAGE_TOO_MANY_SUBJECTS);
        assertParseFailure(parser, details + SUBJECT_DESC + " sub/mathematics band/G2",
                "Subject is repeated: mathematics.");

    }
}
