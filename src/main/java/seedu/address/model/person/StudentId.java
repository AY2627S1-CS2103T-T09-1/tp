package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a student's ID (SID) in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidStudentId(int)}
 */
public class StudentId {

    public static final String MESSAGE_CONSTRAINTS =
            "Student IDs should be positive whole numbers without leading zeros, such as 1 or 42";
    public static final String VALIDATION_REGEX = "[1-9]\\d*";
    public static final int FIRST_VALUE = 1;

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
        return test >= FIRST_VALUE;
    }

    /**
     * Returns true if a given string is a valid student ID that fits in an {@code int}.
     */
    public static boolean isValidStudentId(String test) {
        requireNonNull(test);
        if (!test.matches(VALIDATION_REGEX)) {
            return false;
        }
        try {
            Integer.parseInt(test);
            return true;
        } catch (NumberFormatException e) {
            // Matches the regex but is too large for an int
            return false;
        }
    }

    /**
     * Returns the student ID that comes right after this one.
     */
    public StudentId next() {
        return new StudentId(value + 1);
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
