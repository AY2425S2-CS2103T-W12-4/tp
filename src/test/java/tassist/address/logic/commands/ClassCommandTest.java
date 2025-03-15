package tassist.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tassist.address.logic.commands.ClassCommand.MESSAGE_ARGUMENTS;
import static tassist.address.logic.commands.CommandTestUtil.VALID_CLASS_AMY;
import static tassist.address.logic.commands.CommandTestUtil.VALID_CLASS_BOB;
import static tassist.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static tassist.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static tassist.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static tassist.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import tassist.address.model.Model;
import tassist.address.model.ModelManager;
import tassist.address.model.UserPrefs;
import tassist.address.model.person.ClassNumber;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ClassCommand.
 */
public class ClassCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute() {
        final ClassNumber classNumber = new ClassNumber("T01");

        assertCommandFailure(new ClassCommand(INDEX_FIRST_PERSON, classNumber), model,
                String.format(MESSAGE_ARGUMENTS, INDEX_FIRST_PERSON.getOneBased(), classNumber));
    }

    @Test
    public void equals() {
        final ClassCommand standardCommand = new ClassCommand(INDEX_FIRST_PERSON,
                new ClassNumber(VALID_CLASS_AMY));

        // same values -> returns true
        ClassCommand commandWithSameValues = new ClassCommand(INDEX_FIRST_PERSON,
                new ClassNumber(VALID_CLASS_AMY));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new ClassCommand(INDEX_SECOND_PERSON,
                new ClassNumber(VALID_CLASS_AMY))));

        // different remark -> returns false
        assertFalse(standardCommand.equals(new ClassCommand(INDEX_FIRST_PERSON,
                new ClassNumber(VALID_CLASS_BOB))));
    }
}
