package tassist.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tassist.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ProjectTeamTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ProjectTeam(null));
    }

    @Test
    public void constructor_invalidStudentId_throwsIllegalArgumentException() {
        String invalidProjectTeam = "";
        assertThrows(IllegalArgumentException.class, () -> new ProjectTeam(invalidProjectTeam));
    }

    @Test
    public void isValidStudentId() {
        // null name
        assertThrows(NullPointerException.class, () -> StudentId.isValidStudentId(null));

        // invalid name
        assertFalse(ProjectTeam.isValidProjectTeam("")); // empty string
        assertFalse(ProjectTeam.isValidProjectTeam(" ")); // a whitespace
        assertFalse(ProjectTeam.isValidProjectTeam("  ")); // double whitespace

        // valid name
        assertTrue(ProjectTeam.isValidProjectTeam("weatl")); // all alphabets
        assertTrue(ProjectTeam.isValidProjectTeam("1389")); // all numbers
        assertTrue(ProjectTeam.isValidProjectTeam("%$")); // special characters
        assertTrue(ProjectTeam.isValidProjectTeam("YOYO1389")); // alphanumeric characters
        assertTrue(ProjectTeam.isValidProjectTeam("$doLla23"));
    }

    @Test
    public void equals() {
        ProjectTeam value = new ProjectTeam("TAssist");

        // same values -> returns true
        assertTrue(value.equals(new ProjectTeam("TAssist")));

        // same object -> returns true
        assertTrue(value.equals(value));

        // null -> returns false
        assertFalse(value.equals(null));

        // different types -> returns false
        assertFalse(value.equals(5.0f));

        // different values -> returns false
        assertFalse(value.equals(new ProjectTeam("TAssists")));
    }
}
