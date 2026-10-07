package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a student's ID (SID) in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidStudentId(int)}
 */
public class StudentId {

    public static final int FIRST_VALUE = 1;
    public static final int MAX_VALUE = 999999;
    public static final String MESSAGE_CONSTRAINTS = "Student IDs should be whole numbers from " + FIRST_VALUE
            + " to " + MAX_VALUE + " without leading zeros, such as 1 or 42";
    public static final String VALIDATION_REGEX = "[1-9]\\d*";

    private static final int MAX_DIGITS = String.valueOf(MAX_VALUE).length();

    public final int value;

    /**
     * Constructs a {@code StudentId}.
     *
     * @param studentId A valid student ID.
     */
    public StudentId(int studentId) {
        checkArgument(isValidStudentId(studentId), MESSAGE_CONSTRAINTS);
        value = studentId;
    }

    /**
     * Returns true if a given number is a valid student ID.
     */
    public static boolean isValidStudentId(int test) {
        return test >= FIRST_VALUE && test <= MAX_VALUE;
    }

    /**
     * Returns true if a given string is a valid student ID.
     * The length is checked before parsing, so values too big for an {@code int} are rejected safely.
     */
    public static boolean isValidStudentId(String test) {
        requireNonNull(test);
        boolean isWellFormed = test.matches(VALIDATION_REGEX) && test.length() <= MAX_DIGITS;
        return isWellFormed && isValidStudentId(Integer.parseInt(test));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof StudentId otherStudentId)) {
            return false;
        }

        return value == otherStudentId.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

}
