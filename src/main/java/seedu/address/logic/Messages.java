package seedu.address.logic;

import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Subject;
import seedu.address.model.tag.Tag;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command.";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format!\n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The student index provided is invalid.";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d student(s) listed!";
    public static final String MESSAGE_NOT_PROVIDED = "Not provided";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_MISSING_PREFIX = "Missing required parameter: %1$s.";
    public static final String MESSAGE_MISSING_PREFIXES = "Missing required parameters: %1$s.";
    public static final String MESSAGE_SUBJECT_BAND_PAIR =
            "Each sub/ must be immediately followed by one band/.";
    public static final String MESSAGE_GUARDIAN_PAIR =
            "Guardian name (" + PREFIX_GUARDIAN_NAME + ") and guardian phone (" + PREFIX_GUARDIAN_PHONE
            + ") must be given together.";
    public static final String MESSAGE_TAG_HAS_SPACE = "Tag \"%1$s\" contains a space. Each tag is one word; "
            + "to give more than one tag, put " + PREFIX_TAG + " before each, such as " + PREFIX_TAG + "paid "
            + PREFIX_TAG + "exams.";
    public static final String MESSAGE_NOT_SINGAPORE_PHONE = "Warning: %1$s doesn't look like a Singapore number "
            + "(8 digits starting with 6, 8, or 9, with or without +65). Use edit to fix it if it's wrong.";
    public static final String MESSAGE_NOT_SINGAPORE_PHONES = "Warning: %1$s don't look like Singapore numbers "
            + "(8 digits starting with 6, 8, or 9, with or without +65). Use edit to fix them if they're wrong.";
    public static final String WARNING_LABEL_PHONE = "phone ";
    public static final String WARNING_LABEL_GUARDIAN_PHONE = "guardian phone ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Returns an error message naming the missing prefixes, in the order given.
     */
    public static String getErrorMessageForMissingPrefixes(Prefix... missingPrefixes) {
        assert missingPrefixes.length > 0;

        String missingFields = Stream.of(missingPrefixes).map(Prefix::toString).collect(Collectors.joining(", "));
        String format = missingPrefixes.length == 1 ? MESSAGE_MISSING_PREFIX : MESSAGE_MISSING_PREFIXES;
        return String.format(format, missingFields);
    }

    /**
     * Returns a warning naming the given phones that are not Singapore numbers, and whose they are,
     * or an empty {@code Optional} if every given phone is a Singapore number.
     *
     * @param phone The student's phone, if one was given.
     * @param guardianPhone The guardian's phone, if one was given.
     */
    public static Optional<String> getNonSingaporePhoneWarning(Optional<Phone> phone,
            Optional<Phone> guardianPhone) {
        List<String> nonSingaporePhones = Stream.of(
                        labelIfNotSingapore(WARNING_LABEL_PHONE, phone),
                        labelIfNotSingapore(WARNING_LABEL_GUARDIAN_PHONE, guardianPhone))
                .flatMap(Optional::stream)
                .toList();
        if (nonSingaporePhones.isEmpty()) {
            return Optional.empty();
        }
        if (nonSingaporePhones.size() == 1) {
            return Optional.of(String.format(MESSAGE_NOT_SINGAPORE_PHONE, nonSingaporePhones.get(0)));
        }
        return Optional.of(String.format(MESSAGE_NOT_SINGAPORE_PHONES, String.join(" and ", nonSingaporePhones)));
    }

    private static Optional<String> labelIfNotSingapore(String label, Optional<Phone> phone) {
        return phone.filter(p -> !p.isSingaporeNumber()).map(p -> label + p);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Student phone: ")
                .append(person.getPhone().map(Phone::toString).orElse(MESSAGE_NOT_PROVIDED))
                .append("; School: ")
                .append(person.getSchool())
                .append("; Level: ")
                .append(person.getLevel())
                .append("; Subjects: ")
                .append(person.getSubjects().stream().map(Subject::toString).collect(Collectors.joining(", ")))
                .append("; Address: ")
                .append(person.getAddress().map(Object::toString).orElse(MESSAGE_NOT_PROVIDED))
                .append("; Email: ")
                .append(person.getEmail().map(Object::toString).orElse(MESSAGE_NOT_PROVIDED));
        if (!person.getTags().isEmpty()) {
            builder.append("; Tags: ").append(formatTags(person.getTags()));
        }
        // labels match the student detail panel
        person.getGuardian().ifPresent(guardian -> builder.append("; Guardian name: ").append(guardian.getName())
                .append("; Guardian phone: ").append(guardian.getPhone()));
        return builder.toString();
    }

    /**
     * Formats {@code tags} for display, in the order they were given.
     */
    public static String formatTags(Set<Tag> tags) {
        return tags.stream().map(tag -> tag.tagName).collect(Collectors.joining(", "));
    }

}
