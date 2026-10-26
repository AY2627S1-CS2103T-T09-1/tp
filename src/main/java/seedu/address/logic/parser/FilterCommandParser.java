package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Band;
import seedu.address.model.person.GuardianNameContainsTextPredicate;
import seedu.address.model.person.GuardianPhoneMatchesPredicate;
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

/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    public static final String MESSAGE_BLANK_NAME = "The name to search for cannot be blank.";
    public static final String MESSAGE_BLANK_SCHOOL = "The school to search for cannot be blank.";
    public static final String MESSAGE_BLANK_GUARDIAN_NAME =
            "The guardian name to search for cannot be blank.";
    public static final String MESSAGE_NO_FILTER =
            "Give at least one filter.\n" + FilterCommand.MESSAGE_USAGE;
    public static final String MESSAGE_BAND_NOT_AFTER_SUBJECT = PREFIX_BAND + " must come right after "
            + PREFIX_SUBJECT + ", such as: filter " + PREFIX_SUBJECT + "Mathematics " + PREFIX_BAND + "G2";
    public static final String MESSAGE_USE_NOTE_FILTER = "To filter by notes, use " + PREFIX_NOTE
            + ", such as: filter " + PREFIX_NOTE + "algebra";
    public static final String MESSAGE_BLANK_NOTE = "The note text to search for cannot be blank.";
    public static final String MESSAGE_NOTE_TOO_LONG = "The note text to search for must be at most "
            + Note.MAX_LENGTH + " characters.";

    private static final Prefix[] FILTER_PREFIXES = {
        PREFIX_NAME, PREFIX_SCHOOL, PREFIX_LEVEL, PREFIX_SUBJECT, PREFIX_BAND,
        PREFIX_PHONE, PREFIX_GUARDIAN_NAME, PREFIX_GUARDIAN_PHONE, PREFIX_NOTE
    };
    // deletenote's and editnote's note index prefix, recognized only to point the user to note/
    private static final Prefix[] NOTE_PREFIXES = {PREFIX_NID};

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        // everything after note/ is note text, as in addnote, so other prefixes there are not split off
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenizeWithTrailingText(args, PREFIX_NOTE, allPrefixes());
        if (hasAny(argMultimap, NOTE_PREFIXES)) {
            throw new ParseException(MESSAGE_USE_NOTE_FILTER);
        }
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }
        if (!hasAny(argMultimap, FILTER_PREFIXES)) {
            throw new ParseException(MESSAGE_NO_FILTER);
        }
        argMultimap.verifyNoDuplicatePrefixesFor(FILTER_PREFIXES);
        verifyBandFollowsSubject(argMultimap.getPrefixes());

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
        Optional<String> level = argMultimap.getValue(PREFIX_LEVEL);
        if (level.isPresent()) {
            filters.add(new LevelMatchesPredicate(ParserUtil.parseLevel(level.get())));
        }
        Optional<String> subject = argMultimap.getValue(PREFIX_SUBJECT);
        if (subject.isPresent()) {
            filters.add(parseSubjectFilter(subject.get(), argMultimap.getValue(PREFIX_BAND)));
        }
        Optional<String> phone = argMultimap.getValue(PREFIX_PHONE);
        if (phone.isPresent()) {
            filters.add(new PhoneMatchesPredicate(ParserUtil.parsePhone(phone.get())));
        }
        Optional<String> guardianName = argMultimap.getValue(PREFIX_GUARDIAN_NAME);
        if (guardianName.isPresent()) {
            String guardianNameText = parseSearchText(guardianName.get(), MESSAGE_BLANK_GUARDIAN_NAME);
            filters.add(new GuardianNameContainsTextPredicate(guardianNameText));
        }
        Optional<String> guardianPhone = argMultimap.getValue(PREFIX_GUARDIAN_PHONE);
        if (guardianPhone.isPresent()) {
            Phone guardianPhoneValue = ParserUtil.parseGuardianPhone(guardianPhone.get());
            filters.add(new GuardianPhoneMatchesPredicate(guardianPhoneValue));
        }
        Optional<String> note = argMultimap.getValue(PREFIX_NOTE);
        if (note.isPresent()) {
            filters.add(parseNoteFilter(note.get()));
        }
        return new FilterCommand(new StudentMatchesAllPredicate(filters));
    }

    private static Prefix[] allPrefixes() {
        return Stream.concat(Stream.of(FILTER_PREFIXES), Stream.of(NOTE_PREFIXES)).toArray(Prefix[]::new);
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
            throw new ParseException(Subject.MESSAGE_CONSTRAINTS);
        }
        if (band.isEmpty()) {
            return new SubjectMatchesPredicate(trimmedName);
        }
        try {
            return new SubjectMatchesPredicate(trimmedName, Band.fromString(band.get()));
        } catch (IllegalArgumentException e) {
            throw new ParseException(Band.MESSAGE_CONSTRAINTS, e);
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
