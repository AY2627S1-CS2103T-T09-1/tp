package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

import seedu.address.logic.Messages;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Band;
import seedu.address.model.person.Note;
import seedu.address.model.person.Subject;

/** Checks address values for a likely parameter prefix joined to the address by a missing space. */
public final class AddressPrefixChecker {

    private AddressPrefixChecker() {
    }

    /**
     * Throws an error if {@code address} contains a recognized prefix without a preceding space, and the text
     * after that prefix is a valid value for its field. Prefix matching ignores case so that a mistyped uppercase
     * prefix can be reported clearly. Prefixes not accepted by the current command are not considered.
     *
     * @param address The address value to inspect.
     * @param allowedPrefixes Prefixes accepted by the current command.
     * @throws ParseException if a likely joined prefix is found.
     */
    public static void verifyNoJoinedPrefix(String address, Prefix... allowedPrefixes) throws ParseException {
        requireNonNull(address);
        requireNonNull(allowedPrefixes);
        Prefix[] prefixes = Arrays.stream(allowedPrefixes)
                .filter(prefix -> !PREFIX_ADDRESS.equals(prefix))
                .sorted(Comparator.comparingInt((Prefix prefix) -> prefix.toString().length()).reversed())
                .toArray(Prefix[]::new);

        for (int slashIndex = 1; slashIndex < address.length(); slashIndex++) {
            if (address.charAt(slashIndex) != '/') {
                continue;
            }
            int currentSlashIndex = slashIndex;
            Optional<Prefix> possiblePrefix = Arrays.stream(prefixes)
                    .filter(prefix -> isPrefixBeforeSlash(address, currentSlashIndex, prefix))
                    .filter(prefix -> looksLikeFieldValue(prefix, address.substring(currentSlashIndex + 1)))
                    .findFirst();
            if (possiblePrefix.isPresent()) {
                Prefix prefix = possiblePrefix.get();
                int prefixStart = slashIndex - prefix.toString().length() + 1;
                String enteredPrefix = address.substring(prefixStart, slashIndex + 1);
                if (!enteredPrefix.equals(prefix.toString())) {
                    throw new ParseException(String.format(Messages.MESSAGE_PREFIX_CASE_SENSITIVE,
                            enteredPrefix, prefix));
                }
                throw new ParseException(String.format(Messages.MESSAGE_MISSING_SPACE_BEFORE_PREFIX, prefix));
            }
        }
    }

    private static boolean isPrefixBeforeSlash(String address, int slashIndex, Prefix prefix) {
        String prefixText = prefix.toString();
        int prefixStart = slashIndex - prefixText.length() + 1;
        return prefixStart >= 0 && address.regionMatches(true, prefixStart,
                prefixText, 0, prefixText.length() - 1);
    }

    private static boolean looksLikeFieldValue(Prefix prefix, String value) {
        try {
            if (PREFIX_NAME.equals(prefix)) {
                ParserUtil.parseName(value);
            } else if (PREFIX_PHONE.equals(prefix)) {
                ParserUtil.parsePhone(value);
            } else if (PREFIX_SCHOOL.equals(prefix)) {
                ParserUtil.parseSchool(value);
            } else if (PREFIX_EMAIL.equals(prefix)) {
                ParserUtil.parseEmail(value);
            } else if (PREFIX_LEVEL.equals(prefix)) {
                ParserUtil.parseLevel(value);
            } else if (PREFIX_SUBJECT.equals(prefix)) {
                return Subject.isValidSubject(value.strip());
            } else if (PREFIX_BAND.equals(prefix)) {
                Band.fromString(value);
            } else if (PREFIX_GUARDIAN_NAME.equals(prefix)) {
                ParserUtil.parseGuardianName(value);
            } else if (PREFIX_GUARDIAN_PHONE.equals(prefix)) {
                ParserUtil.parseGuardianPhone(value);
            } else if (PREFIX_TAG.equals(prefix)) {
                ParserUtil.parseTag(value);
            } else if (PREFIX_NOTE.equals(prefix)) {
                return Note.isValidNote(value);
            } else {
                return false;
            }
            return true;
        } catch (ParseException | IllegalArgumentException e) {
            return false;
        }
    }
}
