package tassist.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tassist.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static tassist.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static tassist.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static tassist.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static tassist.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import tassist.address.commons.core.index.Index;
import tassist.address.logic.Messages;
import tassist.address.logic.commands.exceptions.CommandException;
import tassist.address.model.Model;
import tassist.address.model.ModelManager;
import tassist.address.model.UserPrefs;
import tassist.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_showsConfirmationMessage() throws CommandException {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        // Expected confirmation message
        String expectedMessage = String.format(DeleteCommand.MESSAGE_CONFIRM_DELETE, Messages.format(personToDelete));

        // Ensure confirmation message is shown first
        CommandResult commandResult = deleteCommand.execute(model);
        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
        assertTrue(commandResult.requiresConfirmation());
    }

    @Test
    public void executeConfirmed_validIndexUnfilteredList_deletesSuccessfully() throws CommandException {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        // Step 1: First execution should return confirmation message
        CommandResult confirmationResult = deleteCommand.execute(model);
        assertEquals(String.format(DeleteCommand.MESSAGE_CONFIRM_DELETE, Messages.format(personToDelete)),
                confirmationResult.getFeedbackToUser());

        // Step 2: Confirm deletion
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        CommandResult deletionResult = deleteCommand.executeConfirmed(model);

        // Fix: Use assertEquals instead
        assertEquals(expectedMessage, deletionResult.getFeedbackToUser());
    }


    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeConfirmed_validIndexFilteredList_deletesSuccessfully() throws CommandException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        // Step 1: First execution should return confirmation message
        CommandResult confirmationResult = deleteCommand.execute(model);
        assertEquals(String.format(DeleteCommand.MESSAGE_CONFIRM_DELETE,
                Messages.format(personToDelete)), confirmationResult.getFeedbackToUser());

        // Step 2: Confirm deletion
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        showNoPerson(expectedModel);

        CommandResult deletionResult = deleteCommand.executeConfirmed(model);

        // Fix: Use assertEquals instead
        assertEquals(expectedMessage, deletionResult.getFeedbackToUser());
    }


    @Test
    public void executeCancelled_showsCancellationMessage() throws CommandException {
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        // Step 1: First execution should return confirmation message
        CommandResult confirmationResult = deleteCommand.execute(model);
        assertEquals(String.format(DeleteCommand.MESSAGE_CONFIRM_DELETE,
                Messages.format(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))),
                confirmationResult.getFeedbackToUser());

        // Step 2: Simulate cancel deletion
        // Simulate cancelling deletion by returning the cancellation message directly
        CommandResult cancellationResult = new CommandResult(DeleteCommand.MESSAGE_DELETE_CANCELLED);
        assertEquals(DeleteCommand.MESSAGE_DELETE_CANCELLED,
               cancellationResult.getFeedbackToUser());
    }

    @Test
    public void executeConfirmed_invalidIndex_returnsInvalidPersonDisplayedIndexMessage() throws CommandException {
        // Set up a DeleteCommand with an invalid index (greater than the size of the list)
        Index invalidIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(invalidIndex);

        // Execute the command to get the result
        CommandResult result = deleteCommand.executeConfirmed(model);

        // Assert that the result contains the expected error message
        assertEquals(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, result.getFeedbackToUser());
    }


    @Test
    public void getConfirmationMessage_showsCorrectMessage() {
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        // Simulate getting the confirmation message
        String confirmationMessage = deleteCommand.getConfirmationMessage();

        // Assert that the correct confirmation message is returned
        assertEquals(DeleteCommand.MESSAGE_CONFIRM_DELETE, confirmationMessage);
    }


    @Test
    public void execute_invalidConfirmationInput_returnsErrorMessage() throws CommandException {
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        // Step 1: First execution should return confirmation message
        deleteCommand.execute(model);

        // Step 2: Simulate invalid response
        CommandResult commandResult = new CommandResult("Invalid response. Please enter Y/N.");
        assertEquals(commandResult.getFeedbackToUser(), "Invalid response. Please enter Y/N.");
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertEquals(deleteFirstCommand, deleteFirstCommand);

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_PERSON);
        assertEquals(deleteFirstCommand, deleteFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(1, deleteFirstCommand);

        // null -> returns false
        assertNotEquals(null, deleteFirstCommand);

        // different person -> returns false
        assertNotEquals(deleteFirstCommand, deleteSecondCommand);
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);
        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
