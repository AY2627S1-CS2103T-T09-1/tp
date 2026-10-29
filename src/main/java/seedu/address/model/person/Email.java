package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Arrays;
import java.util.Optional;

/**
 * Represents a Person's email in the tutor book.
 * Guarantees: immutable; is valid as declared in {@link #isValidEmail(String)}
 */
public class Email {

    public static final int MAX_LENGTH = 254;
    public static final String MESSAGE_EMPTY = "Email cannot be empty.";
    public static final String MESSAGE_TOO_LONG = "Email is %1$d characters long, but can be at most "
            + MAX_LENGTH + ".";
    public static final String MESSAGE_HAS_SPACE = "Email \"%1$s\" contains a space.";
    public static final String MESSAGE_MISSING_AT = "Email \"%1$s\" has no \"@\". "
            + "Use the format local-part@domain, such as chloe.tan@example.com.";
    public static final String MESSAGE_MULTIPLE_AT = "Email \"%1$s\" has more than one \"@\".";
    public static final String MESSAGE_MISSING_LOCAL_PART = "Email \"%1$s\" has nothing before the \"@\".";
    public static final String MESSAGE_MISSING_DOMAIN =
            "Email \"%1$s\" has no domain after the \"@\", such as example.com.";
    public static final String MESSAGE_INVALID_LOCAL_PART_CHARACTER = "Email \"%1$s\" contains \"%2$s\" before "
            + "the \"@\", which isn't allowed. Use only English letters, digits, +, _, . and -.";
    public static final String MESSAGE_MISPLACED_LOCAL_PART_SYMBOL = "The part before \"@\" in email \"%1$s\" "
            + "can't start or end with +, _, . or -, or have two of them in a row.";
    public static final String MESSAGE_INVALID_DOMAIN_CHARACTER = "Email \"%1$s\" contains \"%2$s\" after "
            + "the \"@\", which isn't allowed. Use only English letters, digits, hyphens and periods.";
    public static final String MESSAGE_INVALID_DOMAIN_LABEL = "In the domain of email \"%1$s\", each part "
            + "between periods must start and end with a letter or digit, with single hyphens only.";
    public static final String MESSAGE_SHORT_LAST_DOMAIN_LABEL = "The domain of email \"%1$s\" must end with "
            + "a part that has 2 letters or digits in a row, such as .com.";
    private static final String SPECIAL_CHARACTERS = "+_.-";
    public static final String MESSAGE_CONSTRAINTS = "Emails must be at most " + MAX_LENGTH + " characters, "
            + "be of the format local-part@domain "
            + "and adhere to the following constraints:\n"
            + "1. The local-part should only contain alphanumeric characters and these special characters, excluding "
            + "the parentheses, (" + SPECIAL_CHARACTERS + "). The local-part may not start or end with any special "
            + "characters.\n"
            + "2. The local-part is followed by an '@' and then a domain name. The domain name is made up of domain "
            + "labels separated by periods.\n"
            + "The domain name must:\n"
            + "    - end with a domain label at least 2 characters long\n"
            + "    - have each domain label start and end with alphanumeric characters\n"
            + "    - have each domain label consist of alphanumeric characters, separated only by hyphens, if any.";
    private static final String AT = "@";
    private static final String DOMAIN_LABEL_SEPARATOR = ".";
    private static final String DOMAIN_LABEL_SEPARATOR_REGEX = "\\.";
    // alphanumeric and special characters
    private static final String ALPHANUMERIC_NO_UNDERSCORE = "[^\\W_]+"; // alphanumeric characters except underscore
    private static final String LOCAL_PART_REGEX = "^" + ALPHANUMERIC_NO_UNDERSCORE + "([" + SPECIAL_CHARACTERS + "]"
            + ALPHANUMERIC_NO_UNDERSCORE + ")*";
    private static final String DOMAIN_PART_REGEX = ALPHANUMERIC_NO_UNDERSCORE
            + "(-" + ALPHANUMERIC_NO_UNDERSCORE + ")*";
    private static final String DOMAIN_LAST_PART_REGEX = "(" + DOMAIN_PART_REGEX + "){2,}$"; // At least two chars
    private static final String DOMAIN_REGEX = "(" + DOMAIN_PART_REGEX + "\\.)*" + DOMAIN_LAST_PART_REGEX;
    public static final String VALIDATION_REGEX = LOCAL_PART_REGEX + "@" + DOMAIN_REGEX;
    private static final String LOCAL_PART_ALLOWED_CHARACTERS_REGEX = "[^\\W_]|[" + SPECIAL_CHARACTERS + "]";
    private static final String DOMAIN_ALLOWED_CHARACTERS_REGEX = "[^\\W_]|[.-]";

    public final String value;

    /**
     * Constructs an {@code Email}.
     *
     * @param email A valid email address.
     */
    public Email(String email) {
        requireNonNull(email);
        checkArgument(isValidEmail(email), MESSAGE_CONSTRAINTS);
        value = email;
    }

    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidEmail(String test) {
        return test.codePointCount(0, test.length()) <= MAX_LENGTH && test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if the domain, the part after the "@", contains a period.
     * A domain without one, such as {@code gmail}, is valid but is often missing an ending such as {@code .com}.
     */
    public boolean hasDotInDomain() {
        int atIndex = value.indexOf(AT);
        assert atIndex > 0 && atIndex == value.lastIndexOf(AT) : "A valid email has exactly one @";
        return value.substring(atIndex + 1).contains(DOMAIN_LABEL_SEPARATOR);
    }

    /**
     * Returns why {@code email} is not a valid email, or an empty {@code Optional} if it is valid.
     * Only the first problem found is reported.
     *
     * @param email The email as the user typed it, with surrounding spaces already removed.
     * @return The reason, quoting the email as typed.
     */
    public static Optional<String> getInvalidReason(String email) {
        requireNonNull(email);
        if (isValidEmail(email)) {
            return Optional.empty();
        }
        if (email.isBlank()) {
            return Optional.of(MESSAGE_EMPTY);
        }
        int length = email.codePointCount(0, email.length());
        if (length > MAX_LENGTH) {
            return Optional.of(String.format(MESSAGE_TOO_LONG, length));
        }
        if (email.chars().anyMatch(Character::isWhitespace)) {
            return Optional.of(String.format(MESSAGE_HAS_SPACE, email));
        }

        String[] parts = email.split(AT, -1);
        if (parts.length != 2) {
            String message = parts.length == 1 ? MESSAGE_MISSING_AT : MESSAGE_MULTIPLE_AT;
            return Optional.of(String.format(message, email));
        }
        String localPart = parts[0];
        String domain = parts[1];
        if (localPart.isEmpty()) {
            return Optional.of(String.format(MESSAGE_MISSING_LOCAL_PART, email));
        }
        if (domain.isEmpty()) {
            return Optional.of(String.format(MESSAGE_MISSING_DOMAIN, email));
        }
        Optional<String> localPartReason = getLocalPartInvalidReason(email, localPart);
        return localPartReason.isPresent() ? localPartReason : Optional.of(getDomainInvalidReason(email, domain));
    }

    /**
     * Returns why {@code localPart} is invalid, or an empty {@code Optional} if it is valid.
     */
    private static Optional<String> getLocalPartInvalidReason(String email, String localPart) {
        Optional<String> invalidCharacter = findInvalidCharacter(localPart, LOCAL_PART_ALLOWED_CHARACTERS_REGEX);
        if (invalidCharacter.isPresent()) {
            return Optional.of(String.format(MESSAGE_INVALID_LOCAL_PART_CHARACTER, email, invalidCharacter.get()));
        }
        if (!localPart.matches(LOCAL_PART_REGEX)) {
            return Optional.of(String.format(MESSAGE_MISPLACED_LOCAL_PART_SYMBOL, email));
        }
        return Optional.empty();
    }

    /**
     * Returns why {@code domain} is invalid. The domain must be invalid.
     */
    private static String getDomainInvalidReason(String email, String domain) {
        Optional<String> invalidCharacter = findInvalidCharacter(domain, DOMAIN_ALLOWED_CHARACTERS_REGEX);
        if (invalidCharacter.isPresent()) {
            return String.format(MESSAGE_INVALID_DOMAIN_CHARACTER, email, invalidCharacter.get());
        }
        String[] labels = domain.split(DOMAIN_LABEL_SEPARATOR_REGEX, -1);
        boolean hasInvalidLabel = Arrays.stream(labels).anyMatch(label -> !label.matches(DOMAIN_PART_REGEX));
        if (hasInvalidLabel) {
            return String.format(MESSAGE_INVALID_DOMAIN_LABEL, email);
        }
        String lastLabel = labels[labels.length - 1];
        assert !lastLabel.matches(DOMAIN_LAST_PART_REGEX);
        return String.format(MESSAGE_SHORT_LAST_DOMAIN_LABEL, email);
    }

    /**
     * Returns the first character in {@code text} that {@code allowedCharacterRegex} doesn't match,
     * or an empty {@code Optional} if every character is allowed.
     */
    private static Optional<String> findInvalidCharacter(String text, String allowedCharacterRegex) {
        String invalidCharacters = text.replaceAll(allowedCharacterRegex, "");
        if (invalidCharacters.isEmpty()) {
            return Optional.empty();
        }
        int firstLength = Character.charCount(invalidCharacters.codePointAt(0));
        return Optional.of(invalidCharacters.substring(0, firstLength));
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Email otherEmail)) {
            return false;
        }

        return value.equals(otherEmail.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
