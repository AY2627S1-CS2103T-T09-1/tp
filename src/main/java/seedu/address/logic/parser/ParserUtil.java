package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.Messages;
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

    public static final String MESSAGE_INVALID_INDEX = "Index must be a positive integer.";

    public static final String MESSAGE_INVALID_NID = "NID must be a positive integer.";

    public static final String PHONE_LABEL = "Phone";

    public static final String GUARDIAN_PHONE_LABEL = "Guardian phone";

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
     * Leading and trailing whitespaces will be trimmed, and spaces, hyphens, and a leading +65 are removed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        return parseLabelledPhone(phone, PHONE_LABEL);
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
     * Leading and trailing whitespaces will be trimmed, and spaces, hyphens, and a leading +65 are removed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parseGuardianPhone(String phone) throws ParseException {
        return parseLabelledPhone(phone, GUARDIAN_PHONE_LABEL);
    }

    /**
     * Parses {@code phone} into a {@code Phone}, naming it {@code label} in the error message.
     */
    private static Phone parseLabelledPhone(String phone, String label) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        Optional<String> invalidReason = Phone.getInvalidReason(label, trimmedPhone);
        if (invalidReason.isPresent()) {
            throw new ParseException(invalidReason.get());
        }
        return new Phone(Phone.normalize(trimmedPhone));
    }

    /**
     * Parses a guardian's {@code name} and {@code phone} into a {@code Guardian}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} or {@code phone} is invalid.
     */
    public static Guardian parseGuardian(String name, String phone) throws ParseException {
        return new Guardian(parseGuardianName(name), parseGuardianPhone(phone));
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
     * Parses subject names and their bands into subjects, and validates their count and uniqueness.
     * The band at each position in {@code bands} belongs to the subject name at the same position in
     * {@code names}.
     *
     * @throws ParseException if {@code names} and {@code bands} differ in size, there are too many subjects,
     *     or a subject is invalid or repeated.
     */
    public static List<Subject> parseSubjects(List<String> names, List<String> bands) throws ParseException {
        requireNonNull(names);
        requireNonNull(bands);
        if (names.size() != bands.size()) {
            throw new ParseException(Messages.MESSAGE_SUBJECT_BAND_PAIR);
        }
        if (names.size() > Person.MAX_SUBJECTS) {
            throw new ParseException(Person.MESSAGE_TOO_MANY_SUBJECTS);
        }
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
