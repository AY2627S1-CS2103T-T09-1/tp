package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Band;
import seedu.address.model.person.Guardian;
import seedu.address.model.person.Level;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.School;
import seedu.address.model.person.Subject;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_SUBJECT_BAND_PAIR =
            "Each sub/ must be immediately followed by one band/.";

    public static final String MESSAGE_GUARDIAN_PAIR =
            "Guardian name (" + PREFIX_GUARDIAN_NAME + ") and guardian phone (" + PREFIX_GUARDIAN_PHONE
            + ") must be given together.";

    public static final String MESSAGE_INVALID_INDEX = "Index must be a positive integer.";

    public static final String MESSAGE_INVALID_NID = "NID must be a positive integer.";

    /**
     * Validates that {@code args} contains no additional arguments.
     *
     * @throws ParseException if additional arguments are present.
     */
    public static void validateNoArguments(String args, String commandUsage) throws ParseException {
        requireNonNull(args);
        requireNonNull(commandUsage);
        if (!args.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, commandUsage));
        }
    }

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not a non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses {@code oneBasedNid}, the number shown before a note, into an {@code Index} and returns it.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given NID is not a positive integer.
     */
    public static Index parseNid(String oneBasedNid) throws ParseException {
        try {
            return parseIndex(oneBasedNid);
        } catch (ParseException pe) {
            throw new ParseException(MESSAGE_INVALID_NID, pe);
        }
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String name} into the {@code Name} of a guardian.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseGuardianName(String name) throws ParseException {
        try {
            return parseName(name);
        } catch (ParseException pe) {
            throw new ParseException(Guardian.MESSAGE_NAME_CONSTRAINTS, pe);
        }
    }

    /**
     * Parses a {@code String phone} into the {@code Phone} of a guardian.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parseGuardianPhone(String phone) throws ParseException {
        try {
            return parsePhone(phone);
        } catch (ParseException pe) {
            throw new ParseException(Guardian.MESSAGE_PHONE_CONSTRAINTS, pe);
        }
    }

    /**
     * Parses the guardian name and phone in {@code arguments} into a {@code Guardian}.
     * Returns an empty {@code Optional} if neither is given.
     *
     * @throws ParseException if only one of them is given, or either is invalid.
     */
    public static Optional<Guardian> parseGuardian(ArgumentMultimap arguments) throws ParseException {
        requireNonNull(arguments);
        Optional<String> guardianName = arguments.getValue(PREFIX_GUARDIAN_NAME);
        Optional<String> guardianPhone = arguments.getValue(PREFIX_GUARDIAN_PHONE);
        if (guardianName.isEmpty() && guardianPhone.isEmpty()) {
            return Optional.empty();
        }
        if (guardianName.isEmpty() || guardianPhone.isEmpty()) {
            throw new ParseException(MESSAGE_GUARDIAN_PAIR);
        }
        return Optional.of(new Guardian(parseGuardianName(guardianName.get()),
                parseGuardianPhone(guardianPhone.get())));
    }

    /**
     * Parses a {@code String school} into a {@code School}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code school} is invalid.
     */
    public static School parseSchool(String school) throws ParseException {
        requireNonNull(school);
        String trimmedSchool = school.trim();
        if (!School.isValidSchool(trimmedSchool)) {
            throw new ParseException(School.MESSAGE_CONSTRAINTS);
        }
        return new School(trimmedSchool);
    }

    /**
     * Parses a {@code String level} into a {@code Level}.
     * Leading and trailing whitespaces will be trimmed, and case is ignored.
     *
     * @throws ParseException if the given {@code level} is invalid.
     */
    public static Level parseLevel(String level) throws ParseException {
        requireNonNull(level);
        try {
            return Level.fromString(level);
        } catch (IllegalArgumentException e) {
            throw new ParseException(Level.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Parses a subject name and its mandatory band, trimming surrounding spaces.
     */
    public static Subject parseSubject(String name, String band) throws ParseException {
        requireNonNull(name);
        requireNonNull(band);
        try {
            return new Subject(name.trim(), Band.fromString(band));
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage(), e);
        }
    }

    /**
     * Parses ordered subject-band pairs and validates their count and uniqueness.
     */
    public static List<Subject> parseSubjects(ArgumentMultimap arguments) throws ParseException {
        if (arguments.getAllValues(PREFIX_SUBJECT).isEmpty()) {
            throw new ParseException("Missing required parameter: sub/.");
        }
        validateSubjectBandPairs(arguments.getPrefixes());
        List<String> names = arguments.getAllValues(PREFIX_SUBJECT);
        if (names.size() > Person.MAX_SUBJECTS) {
            throw new ParseException(Person.MESSAGE_TOO_MANY_SUBJECTS);
        }
        List<String> bands = arguments.getAllValues(PREFIX_BAND);
        List<Subject> subjects = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            subjects.add(ParserUtil.parseSubject(names.get(i), bands.get(i)));
        }
        try {
            Person.validateSubjects(subjects);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage(), e);
        }
        return subjects;
    }

    /**
     * Rejects unmatched or non-adjacent subject and band prefixes.
     */
    private static void validateSubjectBandPairs(List<Prefix> prefixes) throws ParseException {
        for (int i = 0; i < prefixes.size(); i++) {
            if (PREFIX_SUBJECT.equals(prefixes.get(i))) {
                if (i + 1 >= prefixes.size() || !PREFIX_BAND.equals(prefixes.get(i + 1))) {
                    throw new ParseException(MESSAGE_SUBJECT_BAND_PAIR);
                }
                i++;
            } else if (PREFIX_BAND.equals(prefixes.get(i))) {
                throw new ParseException(MESSAGE_SUBJECT_BAND_PAIR);
            }
        }
    }

    /**
     * Parses a {@code String note} into a {@code Note}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code note} is invalid.
     */
    public static Note parseNote(String note) throws ParseException {
        requireNonNull(note);
        String trimmedNote = note.trim();
        if (!Note.isValidNote(trimmedNote)) {
            throw new ParseException(Note.getConstraintMessage(trimmedNote));
        }
        return new Note(trimmedNote);
    }
}
