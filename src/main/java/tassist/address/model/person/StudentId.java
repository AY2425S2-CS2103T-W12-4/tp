package tassist.address.model.person;

import static java.util.Objects.requireNonNull;
import static tassist.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's StudentId in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidStudentId(String)}
 */
public class StudentId {

    public static final String MESSAGE_CONSTRAINTS =
            "The Student ID must follow the format AXXXXXXXN, where:\n"
                    + "- A is the uppercase letter 'A'.\n"
                    + "- X represents seven digits (0-9).\n"
                    + "- N is any uppercase letter from A to Z.\n"
                    + "Both 'A' and 'N' must be capitalized.";
    public static final String VALIDATION_REGEX = "^A\\d{7}[A-Z]$";

    public final String value;

    /**
     * Constructs an {@code Address}.
     *
     * @param studentId A valid address.
     */
    public StudentId(String studentId) {
        requireNonNull(studentId);
        checkArgument(isValidStudentId(studentId), MESSAGE_CONSTRAINTS);
        value = studentId;
    }

    /**
     * Returns if a given string is a valid studentId.
     */
    public static boolean isValidStudentId(String test) {
        return test.matches(VALIDATION_REGEX);
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
        if (!(other instanceof StudentId)) {
            return false;
        }

        StudentId otherStudentId = (StudentId) other;
        return value.equals(otherStudentId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
