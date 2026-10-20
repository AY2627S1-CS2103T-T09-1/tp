package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
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

    private static Predicate<Person> name(String text) {
        return new NameContainsKeywordsPredicate(List.of(text));
    }

    private static Predicate<Person> school(String text) {
        return new SchoolContainsTextPredicate(text);
    }

    @SafeVarargs
    private static FilterCommand filterCommandOf(Predicate<Person>... filters) {
        return new FilterCommand(new StudentMatchesAllPredicate(List.of(filters)));
    }

}
