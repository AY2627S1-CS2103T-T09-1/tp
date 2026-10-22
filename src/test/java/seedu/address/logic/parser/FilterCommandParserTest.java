package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.person.GuardianNameContainsTextPredicate;
import seedu.address.model.person.GuardianPhoneMatchesPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.PhoneMatchesPredicate;
import seedu.address.model.person.SchoolContainsTextPredicate;
import seedu.address.model.person.StudentMatchesAllPredicate;

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
        assertParseFailure(parser, " t/algebra", FilterCommandParser.MESSAGE_NOTES_NOT_FILTERABLE);
        assertParseFailure(parser, " t/", FilterCommandParser.MESSAGE_NOTES_NOT_FILTERABLE);
        assertParseFailure(parser, " nid/2", FilterCommandParser.MESSAGE_NOTES_NOT_FILTERABLE);

        // without t/ being recognized, this would search names for "tan t/algebra"
        assertParseFailure(parser, " n/tan t/algebra", FilterCommandParser.MESSAGE_NOTES_NOT_FILTERABLE);
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

    private static Predicate<Person> phone(String phone) {
        return new PhoneMatchesPredicate(new Phone(phone));
    }

    private static Predicate<Person> guardianName(String text) {
        return new GuardianNameContainsTextPredicate(text);
    }

    private static Predicate<Person> guardianPhone(String phone) {
        return new GuardianPhoneMatchesPredicate(new Phone(phone));
    }

    @SafeVarargs
    private static FilterCommand filterCommandOf(Predicate<Person>... filters) {
        return new FilterCommand(new StudentMatchesAllPredicate(List.of(filters)));
    }

}
