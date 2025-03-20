package tassist.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tassist.address.logic.commands.CommandTestUtil.VALID_GITHUB_AMY;
import static tassist.address.logic.commands.CommandTestUtil.VALID_GITHUB_BOB;
import static tassist.address.logic.commands.CommandTestUtil.VALID_STUDENTID_AMY;
import static tassist.address.logic.commands.CommandTestUtil.VALID_STUDENTID_BOB;
import static tassist.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static tassist.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import tassist.address.logic.Messages;
import tassist.address.model.Model;
import tassist.address.model.ModelManager;
import tassist.address.model.UserPrefs;
import tassist.address.model.person.Github;
import tassist.address.model.person.Person;
import tassist.address.model.person.StudentId;
import tassist.address.testutil.PersonBuilder;


/**
 * Contains integration tests (interaction with the Model) and unit tests for GithubCommand.
 */
public class GithubCommandTest {

    private static final String GITHUB_STUB = "https://github.com/githublink";
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validGithubUrl_success() throws Exception {
        Model model = new ModelManager();

        Person originalPerson = new PersonBuilder().withName("Alice").withStudentId("A1239878D").build();
        model.addPerson(originalPerson);

        StudentId validStudentId = new StudentId("A1239878D");
        Github validGithub = new Github("https://github.com/alice123");

        GithubCommand githubCommand = new GithubCommand(validStudentId, validGithub);

        CommandResult result = githubCommand.execute(model);

        Person editedPerson = model.getFilteredPersonList().stream().filter(
                person -> person.getStudentId().equals(validStudentId)).findFirst().get();

        assertEquals(validGithub, editedPerson.getGithub()); // Assert GitHub was updated
        assertEquals(String.format(GithubCommand.MESSAGE_ADD_GITHUB_SUCCESS, Messages.format(editedPerson)),
                result.getFeedbackToUser()); // Assert success message
    }

    @Test
    public void execute_invalidPersonStudentIdUnfilteredList_failure() {
        StudentId invalidStudentId = new StudentId("A9999999A");
        GithubCommand githubCommand = new GithubCommand(invalidStudentId, new Github(VALID_GITHUB_BOB));

        assertCommandFailure(githubCommand, model,
                Messages.MESSAGE_PERSON_NOT_FOUND + invalidStudentId);
    }

    @Test
    public void equals() {
        final GithubCommand standardCommand = new GithubCommand(
                new StudentId(VALID_STUDENTID_AMY), new Github(VALID_GITHUB_AMY));

        // same values -> returns true
        GithubCommand commandWithSameValues = new GithubCommand(
                new StudentId(VALID_STUDENTID_AMY), new Github(VALID_GITHUB_AMY));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new GithubCommand(
                new StudentId(VALID_STUDENTID_BOB), new Github(VALID_GITHUB_AMY))));

        // different github -> returns false
        assertFalse(standardCommand.equals(
                new GithubCommand(new StudentId(VALID_STUDENTID_AMY), new Github(VALID_GITHUB_BOB))));
    }
}
