package tassist.address.model.person;

import static java.util.Objects.requireNonNull;
import static tassist.address.commons.util.AppUtil.checkArgument;
import static tassist.address.logic.parser.ClassCommandParser.MESSAGE_REMOVE_CLASS;

/**
 * Represents a Student's tutorial class number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidClassNumber(String)}
 */
public class ClassNumber {

    public static final String MESSAGE_CONSTRAINTS =
            "Class numbers should be in the format 'Txx' or 'Rxx' (where xx is 01-99)."
            + "'T' and 'R' must be capitalized.\n\n"
            + "Alternatively, "
            + MESSAGE_REMOVE_CLASS;
    public static final String VALIDATION_REGEX = "^(T|R)(0[1-9]|[1-9][0-9])$";
    public static final String DEFAULT_CLASS = "No tutorial assigned";
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
     * Returns true if a given string is a valid class number.
     */
    public static boolean isValidClassNumber(String test) {
        return test.matches(DEFAULT_CLASS) || test.matches(VALIDATION_REGEX);
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
