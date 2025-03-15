package tassist.address.model.person;

import static java.util.Objects.requireNonNull;
import static tassist.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Student's tutorial class number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidClassNumber(String)}
 */
public class ClassNumber {

    public static final String MESSAGE_CONSTRAINTS =
            "Class numbers should be either 'No tutorial assigned' or of the format 'Txx' (where xx is 01-99).";

    public static final String VALIDATION_REGEX = "^T(0[1-9]|[1-9][0-9])$";

    public final String value;

    /**
     * Constructs a {@code ClassNumber}.
     *
     * @param classNumber A valid class number.
     */
    public ClassNumber(String classNumber) {
        requireNonNull(classNumber);
        checkArgument(isValidClassNumber(classNumber), MESSAGE_CONSTRAINTS);
        value = classNumber;
    }

    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidClassNumber(String test) {
        return test.equals("No tutorial assigned") || test.matches(VALIDATION_REGEX);
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
        if (!(other instanceof ClassNumber)) {
            return false;
        }

        ClassNumber otherClassNumber = (ClassNumber) other;
        return value.equals(otherClassNumber.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
