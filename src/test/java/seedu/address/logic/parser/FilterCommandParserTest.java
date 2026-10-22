package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE_FILTER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.person.Band;
import seedu.address.model.person.GuardianNameContainsTextPredicate;
import seedu.address.model.person.GuardianPhoneMatchesPredicate;
import seedu.address.model.person.Level;
import seedu.address.model.person.LevelMatchesPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Note;
import seedu.address.model.person.NoteContainsTextPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.PhoneMatchesPredicate;
import seedu.address.model.person.SchoolContainsTextPredicate;
import seedu.address.model.person.StudentMatchesAllPredicate;
import seedu.address.model.person.Subject;
import seedu.address.model.person.SubjectMatchesPredicate;

public class FilterCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE);

    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_validName_returnsFilterCommand() {
        FilterCommand expectedFilterCommand = filterCommandOf(name("avi"));
        assertParseSuccess(parser, " n/avi", expectedFilterCommand);

        // space after the prefix
        assertParseSuccess(parser, " n/ avi", expectedFilterCommand);
    }

    @Test
    public void parse_nameWithExtraSpaces_collapsesSpaces() {
        assertParseSuccess(parser, " n/  Chloe \t  Tan  ", filterCommandOf(name("Chloe Tan")));
    }

    @Test
    public void parse_validSchool_returnsFilterCommand() {
        assertParseSuccess(parser, " sch/victoria", filterCommandOf(school("victoria")));
    }

    @Test
    public void parse_schoolWithExtraSpaces_collapsesSpaces() {
        assertParseSuccess(parser, " sch/  Bedok \t Green  ", filterCommandOf(school("Bedok Green")));
    }

    @Test
    public void parse_schoolWithSymbols_returnsFilterCommand() {
        assertParseSuccess(parser, " sch/St. Nicholas Girls’",
                filterCommandOf(school("St. Nicholas Girls’")));
        assertParseSuccess(parser, " sch/Cedar Girls'", filterCommandOf(school("Cedar Girls'")));
    }

    @Test
    public void parse_nameAndSchoolInEitherOrder_returnsSameCommand() {
        FilterCommand expectedFilterCommand = filterCommandOf(name("lim"), school("secondary"));
        assertParseSuccess(parser, " n/lim sch/secondary", expectedFilterCommand);
        assertParseSuccess(parser, " sch/secondary n/lim", expectedFilterCommand);
    }

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", FilterCommandParser.MESSAGE_NO_FILTER);
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        assertParseFailure(parser, " avi", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_textBeforePrefix_throwsParseException() {
        assertParseFailure(parser, " avi n/li", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " victoria sch/school", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_blankName_throwsParseException() {
        assertParseFailure(parser, " n/", FilterCommandParser.MESSAGE_BLANK_NAME);
        assertParseFailure(parser, " n/   ", FilterCommandParser.MESSAGE_BLANK_NAME);
    }

    @Test
    public void parse_blankSchool_throwsParseException() {
        assertParseFailure(parser, " sch/", FilterCommandParser.MESSAGE_BLANK_SCHOOL);
        assertParseFailure(parser, " n/avi sch/   ", FilterCommandParser.MESSAGE_BLANK_SCHOOL);
    }

    @Test
    public void parse_repeatedPrefix_throwsParseException() {
        assertParseFailure(parser, " n/avi n/li", Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));
        assertParseFailure(parser, " sch/victoria sch/raffles",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SCHOOL));
    }

    @Test
    public void parse_notePrefix_throwsParseException() {
        assertParseFailure(parser, " t/algebra", FilterCommandParser.MESSAGE_USE_NOTE_FILTER);
        assertParseFailure(parser, " t/", FilterCommandParser.MESSAGE_USE_NOTE_FILTER);
        assertParseFailure(parser, " nid/2", FilterCommandParser.MESSAGE_USE_NOTE_FILTER);

        // without t/ being recognized, this would search names for "tan t/algebra"
        assertParseFailure(parser, " n/tan t/algebra", FilterCommandParser.MESSAGE_USE_NOTE_FILTER);

        // note text cannot contain another prefix after a space
        assertParseFailure(parser, " note/revise t/f", FilterCommandParser.MESSAGE_USE_NOTE_FILTER);
    }

    @Test
    public void parse_validNote_returnsFilterCommand() {
        assertParseSuccess(parser, " note/algebra", filterCommandOf(note("algebra")));
        assertParseSuccess(parser, " note/  next \t  lesson ", filterCommandOf(note("next lesson")));
        assertParseSuccess(parser, " note/WA2 Math: 36/50", filterCommandOf(note("WA2 Math: 36/50")));
    }

    @Test
    public void parse_blankNote_throwsParseException() {
        assertParseFailure(parser, " note/", FilterCommandParser.MESSAGE_BLANK_NOTE);
        assertParseFailure(parser, " note/   ", FilterCommandParser.MESSAGE_BLANK_NOTE);
    }

    @Test
    public void parse_noteAtMaxLength_returnsFilterCommand() {
        String text = "a".repeat(Note.MAX_LENGTH);
        assertParseSuccess(parser, " note/" + text, filterCommandOf(note(text)));
    }

    @Test
    public void parse_noteOverMaxLength_throwsParseException() {
        String text = "a".repeat(Note.MAX_LENGTH + 1);
        assertParseFailure(parser, " note/" + text, FilterCommandParser.MESSAGE_NOTE_TOO_LONG);
    }

    @Test
    public void parse_repeatedNote_throwsParseException() {
        assertParseFailure(parser, " note/algebra note/geometry",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NOTE_FILTER));
    }

    @Test
    public void parse_noteWithOtherFilters_returnsSameCommand() {
        FilterCommand expectedFilterCommand = filterCommandOf(level(Level.S1), note("next lesson"));
        assertParseSuccess(parser, " lvl/S1 note/next lesson", expectedFilterCommand);
        assertParseSuccess(parser, " note/next lesson lvl/S1", expectedFilterCommand);
    }

    @Test
    public void parse_validLevel_returnsFilterCommand() {
        FilterCommand expectedFilterCommand = filterCommandOf(level(Level.S3));
        assertParseSuccess(parser, " lvl/S3", expectedFilterCommand);
        assertParseSuccess(parser, " lvl/s3", expectedFilterCommand);
        assertParseSuccess(parser, " lvl/  S3 ", expectedFilterCommand);
    }

    @Test
    public void parse_invalidLevel_throwsParseException() {
        assertParseFailure(parser, " lvl/S6", Level.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " lvl/3", Level.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " lvl/Sec 3", Level.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_blankLevel_throwsParseException() {
        assertParseFailure(parser, " lvl/", Level.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_repeatedLevel_throwsParseException() {
        assertParseFailure(parser, " lvl/S1 lvl/S2",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_LEVEL));
    }

    @Test
    public void parse_allFiltersInAnyOrder_returnsSameCommand() {
        FilterCommand expectedFilterCommand = filterCommandOf(name("tan"), school("bedok"), level(Level.S2));
        assertParseSuccess(parser, " n/tan sch/bedok lvl/S2", expectedFilterCommand);
        assertParseSuccess(parser, " lvl/S2 n/tan sch/bedok", expectedFilterCommand);
    }

    @Test
    public void parse_validSubject_returnsFilterCommand() {
        assertParseSuccess(parser, " sub/Mathematics", filterCommandOf(subject("Mathematics")));
        assertParseSuccess(parser, " sub/  Additional Mathematics ",
                filterCommandOf(subject("Additional Mathematics")));
    }

    @Test
    public void parse_subjectWithBand_returnsFilterCommand() {
        FilterCommand expectedFilterCommand = filterCommandOf(subject("Mathematics", Band.G2));
        assertParseSuccess(parser, " sub/Mathematics band/G2", expectedFilterCommand);
        assertParseSuccess(parser, " sub/Mathematics band/g2", expectedFilterCommand);
    }

    @Test
    public void parse_invalidSubject_throwsParseException() {
        assertParseFailure(parser, " sub/Math3", Subject.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " sub/", Subject.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " sub/" + "a".repeat(Subject.MAX_LENGTH + 1), Subject.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidBand_throwsParseException() {
        assertParseFailure(parser, " sub/Mathematics band/G4", Band.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " sub/Mathematics band/", Band.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_bandNotRightAfterSubject_throwsParseException() {
        String expectedMessage = FilterCommandParser.MESSAGE_BAND_NOT_AFTER_SUBJECT;
        assertParseFailure(parser, " band/G2", expectedMessage);
        assertParseFailure(parser, " band/G2 sub/Mathematics", expectedMessage);
        assertParseFailure(parser, " sub/Mathematics n/avi band/G2", expectedMessage);
        assertParseFailure(parser, " band/g2 n/avi sub/mathematics", expectedMessage);
    }

    @Test
    public void parse_repeatedSubjectOrBand_throwsParseException() {
        assertParseFailure(parser, " sub/Mathematics sub/Science",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SUBJECT));
        assertParseFailure(parser, " sub/Mathematics band/G1 band/G2",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_BAND));
    }

    @Test
    public void parse_subjectWithOtherFilters_returnsSameCommand() {
        FilterCommand expectedFilterCommand =
                filterCommandOf(name("tan"), school("bedok"), subject("Mathematics", Band.G1));
        assertParseSuccess(parser, " n/tan sch/bedok sub/Mathematics band/G1", expectedFilterCommand);
        assertParseSuccess(parser, " sub/Mathematics band/G1 n/tan sch/bedok", expectedFilterCommand);
    }

    @Test
    public void parse_validPhone_returnsFilterCommand() {
        FilterCommand expectedFilterCommand = filterCommandOf(phone("91234567"));
        assertParseSuccess(parser, " p/91234567", expectedFilterCommand);
        assertParseSuccess(parser, " p/9123 4567", expectedFilterCommand);
        assertParseSuccess(parser, " p/+65 9123-4567", expectedFilterCommand);

        // overseas numbers keep their country code
        assertParseSuccess(parser, " p/+60 12-345 6789", filterCommandOf(phone("+60123456789")));
    }

    @Test
    public void parse_invalidPhone_throwsParseException() {
        for (String invalidPhone : new String[] {"abc", "12", ""}) {
            String expectedMessage = Phone.getInvalidReason(ParserUtil.PHONE_LABEL, invalidPhone).get();
            assertParseFailure(parser, " p/" + invalidPhone, expectedMessage);
        }
    }

    @Test
    public void parse_validGuardianPhone_returnsFilterCommand() {
        FilterCommand expectedFilterCommand = filterCommandOf(guardianPhone("98765432"));
        assertParseSuccess(parser, " gp/98765432", expectedFilterCommand);
        assertParseSuccess(parser, " gp/+65 9876 5432", expectedFilterCommand);
    }

    @Test
    public void parse_invalidGuardianPhone_throwsParseException() {
        for (String invalidPhone : new String[] {"abc", "12", ""}) {
            String expectedMessage =
                    Phone.getInvalidReason(ParserUtil.GUARDIAN_PHONE_LABEL, invalidPhone).get();
            assertParseFailure(parser, " gp/" + invalidPhone, expectedMessage);
        }
    }

    @Test
    public void parse_validGuardianName_returnsFilterCommand() {
        FilterCommand expectedFilterCommand = filterCommandOf(guardianName("Grace Lim"));
        assertParseSuccess(parser, " gn/Grace Lim", expectedFilterCommand);
        assertParseSuccess(parser, " gn/  Grace \t  Lim ", expectedFilterCommand);
    }

    @Test
    public void parse_blankGuardianName_throwsParseException() {
        assertParseFailure(parser, " gn/", FilterCommandParser.MESSAGE_BLANK_GUARDIAN_NAME);
        assertParseFailure(parser, " gn/   ", FilterCommandParser.MESSAGE_BLANK_GUARDIAN_NAME);
    }

    @Test
    public void parse_repeatedPhoneOrGuardianPrefix_throwsParseException() {
        assertParseFailure(parser, " p/91234567 p/82345678",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));
        assertParseFailure(parser, " gn/amy gn/grace",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_GUARDIAN_NAME));
        assertParseFailure(parser, " gp/92345678 gp/98765432",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_GUARDIAN_PHONE));
    }

    @Test
    public void parse_phoneAndGuardianWithOtherFilters_returnsSameCommand() {
        FilterCommand expectedFilterCommand = filterCommandOf(name("tan"), phone("91234567"),
                guardianName("amy"), guardianPhone("92345678"));
        assertParseSuccess(parser, " n/tan p/91234567 gn/amy gp/92345678", expectedFilterCommand);
        assertParseSuccess(parser, " gp/92345678 gn/amy p/91234567 n/tan", expectedFilterCommand);
    }

    private static Predicate<Person> name(String text) {
        return new NameContainsKeywordsPredicate(List.of(text));
    }

    private static Predicate<Person> school(String text) {
        return new SchoolContainsTextPredicate(text);
    }

    private static Predicate<Person> level(Level level) {
        return new LevelMatchesPredicate(level);
    }

    private static Predicate<Person> subject(String subjectName) {
        return new SubjectMatchesPredicate(subjectName);
    }

    private static Predicate<Person> subject(String subjectName, Band band) {
        return new SubjectMatchesPredicate(subjectName, band);
    }

    private static Predicate<Person> phone(String phone) {
        return new PhoneMatchesPredicate(new Phone(phone));
    }

    private static Predicate<Person> guardianName(String text) {
        return new GuardianNameContainsTextPredicate(text);
    }

    private static Predicate<Person> guardianPhone(String phone) {
        return new GuardianPhoneMatchesPredicate(new Phone(phone));
    }

    private static Predicate<Person> note(String text) {
        return new NoteContainsTextPredicate(text);
    }

    @SafeVarargs
    private static FilterCommand filterCommandOf(Predicate<Person>... filters) {
        return new FilterCommand(new StudentMatchesAllPredicate(List.of(filters)));
    }

}
