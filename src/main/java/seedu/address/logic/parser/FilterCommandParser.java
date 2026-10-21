package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.SchoolContainsTextPredicate;
import seedu.address.model.person.StudentMatchesAllPredicate;

/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    public static final String MESSAGE_BLANK_NAME = "The name to search for cannot be blank.";
    public static final String MESSAGE_BLANK_SCHOOL = "The school to search for cannot be blank.";
    public static final String MESSAGE_NO_FILTER = "Give at least one filter, such as "
            + PREFIX_NAME + "NAME or " + PREFIX_SCHOOL + "SCHOOL.";
    public static final String MESSAGE_NOTES_NOT_FILTERABLE = "filter cannot search notes. "
            + "Filter by name or school instead, such as: filter "
            + PREFIX_NAME + "tan " + PREFIX_SCHOOL + "bedok";

    private static final Prefix[] FILTER_PREFIXES = {PREFIX_NAME, PREFIX_SCHOOL};
    // recognized only so that they are rejected, instead of being read as part of another value
    private static final Prefix[] NOTE_PREFIXES = {PREFIX_NOTE, PREFIX_NID};

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, allPrefixes());
        if (hasAny(argMultimap, NOTE_PREFIXES)) {
            throw new ParseException(MESSAGE_NOTES_NOT_FILTERABLE);
        }
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }
        if (!hasAny(argMultimap, FILTER_PREFIXES)) {
            throw new ParseException(MESSAGE_NO_FILTER);
        }
        argMultimap.verifyNoDuplicatePrefixesFor(FILTER_PREFIXES);

        List<Predicate<Person>> filters = new ArrayList<>();
        Optional<String> name = argMultimap.getValue(PREFIX_NAME);
        if (name.isPresent()) {
            String nameText = parseSearchText(name.get(), MESSAGE_BLANK_NAME);
            filters.add(new NameContainsKeywordsPredicate(List.of(nameText)));
        }
        Optional<String> school = argMultimap.getValue(PREFIX_SCHOOL);
        if (school.isPresent()) {
            filters.add(new SchoolContainsTextPredicate(parseSearchText(school.get(), MESSAGE_BLANK_SCHOOL)));
        }
        return new FilterCommand(new StudentMatchesAllPredicate(filters));
    }

    private static Prefix[] allPrefixes() {
        return Stream.concat(Stream.of(FILTER_PREFIXES), Stream.of(NOTE_PREFIXES)).toArray(Prefix[]::new);
    }

    private static boolean hasAny(ArgumentMultimap argMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argMultimap.getValue(prefix).isPresent());
    }

    /**
     * Returns {@code value} with runs of whitespace squeezed to one space and outer spaces removed.
     *
     * @throws ParseException with {@code blankMessage} if nothing is left.
     */
    private static String parseSearchText(String value, String blankMessage) throws ParseException {
        String text = value.replaceAll("\\s+", " ").strip();
        if (text.isEmpty()) {
            throw new ParseException(blankMessage);
        }
        return text;
    }

}
