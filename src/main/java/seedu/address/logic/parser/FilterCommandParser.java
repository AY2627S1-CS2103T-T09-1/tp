package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Band;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Subject;
import seedu.address.model.person.predicate.AddressContainsTextPredicate;
import seedu.address.model.person.predicate.EmailContainsTextPredicate;
import seedu.address.model.person.predicate.GuardianNameContainsTextPredicate;
import seedu.address.model.person.predicate.GuardianPhoneMatchesPredicate;
import seedu.address.model.person.predicate.LevelMatchesPredicate;
import seedu.address.model.person.predicate.NameContainsKeywordsPredicate;
import seedu.address.model.person.predicate.NoteContainsTextPredicate;
import seedu.address.model.person.predicate.PhoneMatchesPredicate;
import seedu.address.model.person.predicate.SchoolContainsTextPredicate;
import seedu.address.model.person.predicate.StudentMatchesAllPredicate;
import seedu.address.model.person.predicate.SubjectMatchesPredicate;
import seedu.address.model.person.predicate.TagMatchesPredicate;

/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    public static final String MESSAGE_BLANK_NAME = "The name to search for cannot be blank.";
    public static final String MESSAGE_BLANK_SCHOOL = "The school to search for cannot be blank.";
    public static final String MESSAGE_BLANK_EMAIL = "The email to search for cannot be blank.";
    public static final String MESSAGE_BLANK_ADDRESS = "The address to search for cannot be blank.";
    public static final String MESSAGE_BLANK_GUARDIAN_NAME =
            "The guardian name to search for cannot be blank.";
    public static final String MESSAGE_NO_FILTER =
            "Give at least one filter.\n" + FilterCommand.MESSAGE_USAGE;
    public static final String MESSAGE_BAND_NOT_AFTER_SUBJECT = PREFIX_BAND + " must come right after "
            + PREFIX_SUBJECT + ", such as: filter " + PREFIX_SUBJECT + "Mathematics " + PREFIX_BAND + "G2";
    public static final String MESSAGE_USE_NOTE_FILTER = "To filter by notes, use " + PREFIX_NOTE
            + ", such as: filter " + PREFIX_NOTE + "algebra";
    public static final String MESSAGE_ONE_TAG = "filter takes one tag, so give " + PREFIX_TAG
            + " only once, such as: filter " + PREFIX_TAG + "paid";
    public static final String MESSAGE_FILTER_TAG_HAS_SPACE = "Tag \"%1$s\" contains a space. filter takes one "
            + "tag of one word, such as: filter " + PREFIX_TAG + "paid";
    public static final String MESSAGE_BLANK_NOTE = "The note text to search for cannot be blank.";
    public static final String MESSAGE_NOTE_TOO_LONG = "The note text to search for must be at most "
            + Note.MAX_LENGTH + " characters.";
    public static final String MESSAGE_SEARCH_CANNOT_MATCH =
            "No student's %1$s can contain \"/\", so \"%2$s\" matches no one.";

    private static final Prefix[] FILTER_PREFIXES = {
        PREFIX_NAME, PREFIX_SCHOOL, PREFIX_LEVEL, PREFIX_SUBJECT, PREFIX_BAND,
        PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_GUARDIAN_NAME, PREFIX_GUARDIAN_PHONE,
        PREFIX_TAG, PREFIX_NOTE
    };
    private static final String FIELD_NAME = "name";
    private static final String FIELD_SCHOOL = "school";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_GUARDIAN_NAME = "guardian name";

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        // everything after note/ is note text, as in addnote, so other prefixes there are not split off
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenizeWithTrailingText(args, PREFIX_NOTE, allPrefixes());
        verifyFilterFormat(argMultimap);
        return new FilterCommand(new StudentMatchesAllPredicate(parseFilters(argMultimap)));
    }

    /**
     * Throws a {@code ParseException} if {@code argMultimap} has {@code nid/}, text before the first prefix,
     * no filters, a repeated prefix, or {@code band/} anywhere except right after {@code sub/}.
     */
    private static void verifyFilterFormat(ArgumentMultimap argMultimap) throws ParseException {
        // nid/ is deletenote's and editnote's note index, recognized only to point the user to note/
        if (hasAny(argMultimap, PREFIX_NID)) {
            throw new ParseException(MESSAGE_USE_NOTE_FILTER);
        }
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }
        if (!hasAny(argMultimap, FILTER_PREFIXES)) {
            throw new ParseException(MESSAGE_NO_FILTER);
        }
        // add and edit take several t/, so a repeated t/ gets its own message
        if (argMultimap.getAllValues(PREFIX_TAG).size() > 1) {
            throw new ParseException(MESSAGE_ONE_TAG);
        }
        argMultimap.verifyNoDuplicatePrefixesFor(FILTER_PREFIXES);
        verifyBandFollowsSubject(argMultimap.getPrefixes());
    }

    /**
     * Returns one filter for each filter prefix given in {@code argMultimap}.
     *
     * @throws ParseException if any filter value is invalid.
     */
    private static List<Predicate<Person>> parseFilters(ArgumentMultimap argMultimap) throws ParseException {
        List<Predicate<Person>> filters = new ArrayList<>();
        addFilter(filters, argMultimap, PREFIX_NAME, value ->
                new NameContainsKeywordsPredicate(List.of(
                        parseSlashFreeSearchText(value, MESSAGE_BLANK_NAME, FIELD_NAME))));
        addFilter(filters, argMultimap, PREFIX_SCHOOL, value -> new SchoolContainsTextPredicate(
                parseSlashFreeSearchText(value, MESSAGE_BLANK_SCHOOL, FIELD_SCHOOL)));
        addFilter(filters, argMultimap, PREFIX_LEVEL, value ->
                new LevelMatchesPredicate(ParserUtil.parseLevel(value)));
        addFilter(filters, argMultimap, PREFIX_SUBJECT, value ->
                parseSubjectFilter(value, argMultimap.getValue(PREFIX_BAND)));
        addFilter(filters, argMultimap, PREFIX_PHONE, value ->
                new PhoneMatchesPredicate(ParserUtil.parsePhone(value)));
        addFilter(filters, argMultimap, PREFIX_EMAIL, value -> new EmailContainsTextPredicate(
                parseSlashFreeSearchText(value, MESSAGE_BLANK_EMAIL, FIELD_EMAIL)));
        addFilter(filters, argMultimap, PREFIX_ADDRESS, value ->
                new AddressContainsTextPredicate(parseSearchText(value, MESSAGE_BLANK_ADDRESS)));
        addFilter(filters, argMultimap, PREFIX_GUARDIAN_NAME, value -> new GuardianNameContainsTextPredicate(
                parseSlashFreeSearchText(value, MESSAGE_BLANK_GUARDIAN_NAME, FIELD_GUARDIAN_NAME)));
        addFilter(filters, argMultimap, PREFIX_GUARDIAN_PHONE, value ->
                new GuardianPhoneMatchesPredicate(ParserUtil.parseGuardianPhone(value)));
        addFilter(filters, argMultimap, PREFIX_TAG, FilterCommandParser::parseTagFilter);
        addFilter(filters, argMultimap, PREFIX_NOTE, FilterCommandParser::parseNoteFilter);
        return filters;
    }

    /**
     * Adds the filter that {@code filterParser} builds from the value of {@code prefix}, if it was given.
     */
    private static void addFilter(List<Predicate<Person>> filters, ArgumentMultimap argMultimap,
            Prefix prefix, FilterParser filterParser) throws ParseException {
        Optional<String> value = argMultimap.getValue(prefix);
        if (value.isPresent()) {
            filters.add(filterParser.parse(value.get()));
        }
    }

    private static Prefix[] allPrefixes() {
        return Stream.concat(Stream.of(FILTER_PREFIXES), Stream.of(PREFIX_NID)).toArray(Prefix[]::new);
    }

    /**
     * Throws a {@code ParseException} if {@code band/} is given anywhere except right after {@code sub/}.
     */
    private static void verifyBandFollowsSubject(List<Prefix> prefixes) throws ParseException {
        int bandIndex = prefixes.indexOf(PREFIX_BAND);
        if (bandIndex == -1) {
            return;
        }
        boolean isRightAfterSubject = bandIndex > 0 && PREFIX_SUBJECT.equals(prefixes.get(bandIndex - 1));
        if (!isRightAfterSubject) {
            throw new ParseException(MESSAGE_BAND_NOT_AFTER_SUBJECT);
        }
    }

    /**
     * Returns a filter for students taking {@code subjectName}, at {@code band} if one is given.
     *
     * @throws ParseException if the subject name or the band is invalid.
     */
    private static SubjectMatchesPredicate parseSubjectFilter(String subjectName, Optional<String> band)
            throws ParseException {
        String trimmedName = subjectName.strip();
        if (!Subject.isValidSubject(trimmedName)) {
            throw new ParseException(ParserUtil.withSlashHint(Subject.MESSAGE_CONSTRAINTS, trimmedName));
        }
        if (band.isEmpty()) {
            return new SubjectMatchesPredicate(trimmedName);
        }
        try {
            return new SubjectMatchesPredicate(trimmedName, Band.fromString(band.get()));
        } catch (IllegalArgumentException e) {
            throw new ParseException(ParserUtil.withSlashHint(Band.MESSAGE_CONSTRAINTS, band.get()), e);
        }
    }

    /**
     * Returns a filter for students with a note containing {@code value}.
     *
     * @throws ParseException if the text is blank or longer than a note can be.
     */
    private static NoteContainsTextPredicate parseNoteFilter(String value) throws ParseException {
        String text = parseSearchText(value, MESSAGE_BLANK_NOTE);
        if (text.codePointCount(0, text.length()) > Note.MAX_LENGTH) {
            throw new ParseException(MESSAGE_NOTE_TOO_LONG);
        }
        return new NoteContainsTextPredicate(text);
    }

    /**
     * Returns a filter for students with the tag {@code value}, ignoring case.
     *
     * @throws ParseException if the value is not exactly one valid tag.
     */
    private static TagMatchesPredicate parseTagFilter(String value) throws ParseException {
        String trimmedTag = value.strip();
        // add's message for a space suggests a second t/, but filter takes only one tag
        if (trimmedTag.chars().anyMatch(Character::isWhitespace)) {
            throw new ParseException(String.format(MESSAGE_FILTER_TAG_HAS_SPACE, trimmedTag));
        }
        return new TagMatchesPredicate(ParserUtil.parseTag(trimmedTag));
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

    /**
     * Returns the search text for a field whose values cannot contain {@code /}, such as a name.
     * A {@code /} in such text usually means a prefix with no space before it, and the text cannot match
     * any student, so it is rejected with a hint.
     *
     * @param value The text given after the prefix.
     * @param blankMessage The error to show if {@code value} is blank.
     * @param fieldName The field searched, such as {@code "school"}, used in the error.
     * @throws ParseException if {@code value} is blank or contains {@code /}.
     */
    private static String parseSlashFreeSearchText(String value, String blankMessage, String fieldName)
            throws ParseException {
        String text = parseSearchText(value, blankMessage);
        Optional<String> hint = ParserUtil.getSlashHint(text);
        if (hint.isPresent()) {
            throw new ParseException(String.format(MESSAGE_SEARCH_CANNOT_MATCH, fieldName, text)
                    + "\n" + hint.get());
        }
        return text;
    }

    /**
     * Builds a filter from the text given after a prefix.
     */
    @FunctionalInterface
    private interface FilterParser {
        Predicate<Person> parse(String value) throws ParseException;
    }

}
