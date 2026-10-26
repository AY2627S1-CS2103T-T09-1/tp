package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.GUARDIAN_NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.GUARDIAN_NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.GUARDIAN_PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.GUARDIAN_PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_GUARDIAN_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_GUARDIAN_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_GUARDIAN_PHONE_MESSAGE;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_LEVEL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_MESSAGE;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_SCHOOL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.LEVEL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.SCHOOL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.SCHOOL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GUARDIAN_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GUARDIAN_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LEVEL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SCHOOL_AMY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Band;
import seedu.address.model.person.Guardian;
import seedu.address.model.person.Level;
import seedu.address.model.person.Name;
import seedu.address.model.person.School;
import seedu.address.model.person.Subject;
import seedu.address.testutil.EditPersonDescriptorBuilder;

public class EditCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_schoolOnly_success() {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withSchool("St. John's Secondary School").build();
        assertParseSuccess(parser, "1 sch/  St. John's Secondary School  ",
                new EditCommand(INDEX_FIRST_PERSON, descriptor));
    }

    @Test
    public void parse_addressSetOrRemoved_success() {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withAddress("12 Example Road").build();
        assertParseSuccess(parser, "1 a/12 Example Road", new EditCommand(INDEX_FIRST_PERSON, descriptor));

        descriptor = new EditPersonDescriptorBuilder().withAddressRemoved().build();
        assertParseSuccess(parser, "1 a/", new EditCommand(INDEX_FIRST_PERSON, descriptor));
    }

    @Test
    public void parse_blankAddressWithSpaces_removesAddress() {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withAddressRemoved().build();
        assertParseSuccess(parser, "1 a/   ", new EditCommand(INDEX_FIRST_PERSON, descriptor));
    }

    @Test
    public void parse_invalidSchool_failure() {
        assertParseFailure(parser, "1 sch/  ", School.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 sch/School sch/Other School",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SCHOOL));
    }

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);

        // removed prefixes being parsed as preamble
        assertParseFailure(parser, "1 e/bob@example.com", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1 a/Clementi Road", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1 t/friend", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1 sub/English", Messages.MESSAGE_SUBJECT_BAND_PAIR);
        assertParseFailure(parser, "1 sub/English band/G4", Band.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 band/G2", "Missing required parameter: sub/."); // band without subject
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC, INVALID_PHONE_MESSAGE); // invalid phone
        assertParseFailure(parser, "1" + INVALID_SCHOOL_DESC, School.MESSAGE_CONSTRAINTS); // invalid school

        // invalid phone followed by valid school
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC + SCHOOL_DESC_AMY, INVALID_PHONE_MESSAGE);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + INVALID_SCHOOL_DESC + VALID_PHONE_AMY,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + SCHOOL_DESC_AMY + NAME_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withSchool(VALID_SCHOOL_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + SCHOOL_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withSchool(VALID_SCHOOL_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        assertParseSuccess(parser, "1 sub/English band/g2", new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withSubjects(new Subject("English", Band.G2)).build()));
        // name
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // school
        userInput = targetIndex.getOneBased() + SCHOOL_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withSchool(VALID_SCHOOL_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_levelOnly_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withLevel(VALID_LEVEL_AMY).build();
        assertParseSuccess(parser, targetIndex.getOneBased() + LEVEL_DESC_AMY,
                new EditCommand(targetIndex, descriptor));
    }

    @Test
    public void parse_invalidLevel_failure() {
        assertParseFailure(parser, "1" + INVALID_LEVEL_DESC, Level.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 " + PREFIX_LEVEL + "S9", Level.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_emptyPhone_removesPhone() {
        Index targetIndex = INDEX_FIRST_PERSON;

        // phone only
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhoneRemoved().build();
        assertParseSuccess(parser, targetIndex.getOneBased() + " " + PREFIX_PHONE,
                new EditCommand(targetIndex, descriptor));

        // phone removed together with another field
        descriptor = new EditPersonDescriptorBuilder().withPhoneRemoved().withName(VALID_NAME_AMY).build();
        assertParseSuccess(parser, targetIndex.getOneBased() + " " + PREFIX_PHONE + NAME_DESC_AMY,
                new EditCommand(targetIndex, descriptor));
    }

    @Test
    public void parse_formattedPhones_savesCleanedPhones() {
        Index targetIndex = INDEX_FIRST_PERSON;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone("91234567").build();
        assertParseSuccess(parser, targetIndex.getOneBased() + " " + PREFIX_PHONE + "9123 4567",
                new EditCommand(targetIndex, descriptor));

        descriptor = new EditPersonDescriptorBuilder().withGuardianPhone("81234567").build();
        assertParseSuccess(parser, targetIndex.getOneBased() + " " + PREFIX_GUARDIAN_PHONE + "+65 8123 4567",
                new EditCommand(targetIndex, descriptor));
    }

    @Test
    public void parse_guardianFields_success() {
        Index targetIndex = INDEX_FIRST_PERSON;

        // guardian name only
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withGuardianName(VALID_GUARDIAN_NAME_AMY).build();
        assertParseSuccess(parser, targetIndex.getOneBased() + GUARDIAN_NAME_DESC_AMY,
                new EditCommand(targetIndex, descriptor));

        // guardian phone only
        descriptor = new EditPersonDescriptorBuilder().withGuardianPhone(VALID_GUARDIAN_PHONE_AMY).build();
        assertParseSuccess(parser, targetIndex.getOneBased() + GUARDIAN_PHONE_DESC_AMY,
                new EditCommand(targetIndex, descriptor));

        // both, with extra spaces
        descriptor = new EditPersonDescriptorBuilder().withGuardianName(VALID_GUARDIAN_NAME_AMY)
                .withGuardianPhone(VALID_GUARDIAN_PHONE_AMY).build();
        assertParseSuccess(parser, targetIndex.getOneBased() + " " + PREFIX_GUARDIAN_NAME + "  "
                + VALID_GUARDIAN_NAME_AMY + "  " + PREFIX_GUARDIAN_PHONE + VALID_GUARDIAN_PHONE_AMY + "  ",
                new EditCommand(targetIndex, descriptor));
    }

    @Test
    public void parse_emptyGuardian_removesGuardian() {
        Index targetIndex = INDEX_FIRST_PERSON;
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withGuardianRemoved().build();
        assertParseSuccess(parser, targetIndex.getOneBased() + " " + PREFIX_GUARDIAN_NAME + " "
                + PREFIX_GUARDIAN_PHONE, new EditCommand(targetIndex, descriptor));

        // guardian removed together with another field
        descriptor = new EditPersonDescriptorBuilder().withGuardianRemoved().withName(VALID_NAME_AMY).build();
        assertParseSuccess(parser, targetIndex.getOneBased() + " " + PREFIX_GUARDIAN_PHONE + "  "
                + PREFIX_GUARDIAN_NAME + NAME_DESC_AMY, new EditCommand(targetIndex, descriptor));
    }

    @Test
    public void parse_guardianPartlyEmpty_failure() {
        String removalMessage = EditCommand.MESSAGE_GUARDIAN_REMOVAL;
        assertParseFailure(parser, "1 " + PREFIX_GUARDIAN_NAME, removalMessage);
        assertParseFailure(parser, "1 " + PREFIX_GUARDIAN_PHONE + "  ", removalMessage);
        assertParseFailure(parser, "1 " + PREFIX_GUARDIAN_NAME + GUARDIAN_PHONE_DESC_AMY, removalMessage);
        assertParseFailure(parser, "1" + GUARDIAN_NAME_DESC_AMY + " " + PREFIX_GUARDIAN_PHONE, removalMessage);
    }

    @Test
    public void parse_invalidGuardian_failure() {
        assertParseFailure(parser, "1" + INVALID_GUARDIAN_NAME_DESC, Guardian.MESSAGE_NAME_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_GUARDIAN_PHONE_DESC, INVALID_GUARDIAN_PHONE_MESSAGE);
        assertParseFailure(parser, "1" + GUARDIAN_NAME_DESC_AMY + GUARDIAN_NAME_DESC_BOB,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_GUARDIAN_NAME));
        assertParseFailure(parser, "1" + GUARDIAN_PHONE_DESC_AMY + GUARDIAN_PHONE_DESC_BOB,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_GUARDIAN_PHONE));
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_repeatedValue_failure()

        // valid followed by invalid
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + INVALID_PHONE_DESC;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple valid fields repeated
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY + SCHOOL_DESC_AMY + PHONE_DESC_AMY + SCHOOL_DESC_AMY
                + PHONE_DESC_BOB + SCHOOL_DESC_BOB;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_SCHOOL));

        // multiple invalid values
        userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + INVALID_SCHOOL_DESC
                + INVALID_PHONE_DESC + INVALID_SCHOOL_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_SCHOOL));
    }
}
