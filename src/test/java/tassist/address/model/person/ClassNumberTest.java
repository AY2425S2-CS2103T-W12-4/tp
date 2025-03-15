package tassist.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ClassNumberTest {

    @Test
    public void equals() {
        ClassNumber classNumber = new ClassNumber("T01");

        // same object -> returns true
        assertTrue(classNumber.equals(classNumber));

        // same values -> returns true
        ClassNumber classNumberCopy = new ClassNumber(classNumber.value);
        assertTrue(classNumber.equals(classNumberCopy));

        // different types -> returns false
        assertFalse(classNumber.equals(1));

        // null -> returns false
        assertFalse(classNumber.equals(null));

        // different ClassNumber -> returns false
        ClassNumber differentClassNumber = new ClassNumber("T15");
        assertFalse(classNumber.equals(differentClassNumber));
    }
}
