package tassist.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static tassist.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tassist.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static tassist.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static tassist.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static tassist.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tassist.address.logic.commands.exceptions.CommandException;
import tassist.address.model.Model;
import tassist.address.model.ModelManager;
import tassist.address.model.UserPrefs;
import tassist.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_LIST_ALL, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_LIST_ALL, expectedModel);
    }

    @Test
    public void execute_sortByNameAscending_success() {
        ListCommand command = new ListCommand("name", "asc", null, null);
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        List<Person> sortedExpectedList = new ArrayList<>(expectedModel.getFilteredPersonList());
        sortedExpectedList.sort(Comparator.comparing(p -> p.getName().fullName));

        assertCommandSuccess(command, model, ListCommand.MESSAGE_LIST_SORTED, expectedModel);

        List<Person> actualList = model.getFilteredPersonList();
        assertEquals(sortedExpectedList, actualList);
    }

    @Test
    public void execute_sortByProgressDescending_success() {
        ListCommand command = new ListCommand("progress", "des", null, null);
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        expectedModel.updateSortedPersonList(Comparator.comparing(p -> p.getProgress().value,
                Comparator.reverseOrder()));
        assertCommandSuccess(command, model, ListCommand.MESSAGE_LIST_SORTED, expectedModel);

        List<Person> actualList = new ArrayList<>(model.getFilteredPersonList());
        List<Person> expectedList = new ArrayList<>(expectedModel.getFilteredPersonList());
        assertEquals(expectedList, actualList);
    }

    @Test
    public void execute_sortByGithubDescending_success() {
        ListCommand command = new ListCommand("github", "des", null, null);
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        List<Person> sortedList = new ArrayList<>(expectedModel.getFilteredPersonList());
        sortedList.sort(Comparator.comparing(p -> p.getGithub().value, Comparator.reverseOrder()));

        assertCommandSuccess(command, model, ListCommand.MESSAGE_LIST_SORTED, expectedModel);

        List<Person> actualList = new ArrayList<>(model.getFilteredPersonList());
        assertEquals(sortedList, actualList);
    }

    @Test
    public void execute_filterByProgress_success() {
        ListCommand command = new ListCommand(null, null, "progress", "30");
        Predicate<Person> expectedPredicate = person -> person.getProgress().value <= 30;
        expectedModel.updateFilteredPersonList(expectedPredicate);

        assertCommandSuccess(command, model, ListCommand.MESSAGE_LIST_FILTERED, expectedModel);
    }

    @Test
    public void execute_filterByProgress_noMatch() {
        ListCommand command = new ListCommand(null, null, "progress", "-1");
        expectedModel.updateFilteredPersonList(person -> person.getProgress().value <= -1);
        assertCommandSuccess(command, model, ListCommand.MESSAGE_NO_STUDENTS, expectedModel);
    }

    @Test
    public void execute_filterProgressAndSortNameDesc_success() {
        ListCommand command = new ListCommand("name", "des", "progress", "30");
        Predicate<Person> expectedPredicate = person -> person.getProgress().value <= 30;
        expectedModel.updateFilteredPersonList(expectedPredicate);
        expectedModel.updateSortedPersonList(Comparator.comparing(p -> p.getName().fullName,
                Comparator.reverseOrder()));

        assertCommandSuccess(command, model, ListCommand.MESSAGE_LIST_FILTERED_SORTED, expectedModel);

        List<Person> actualList = new ArrayList<>(model.getFilteredPersonList());
        List<Person> expectedList = new ArrayList<>(expectedModel.getFilteredPersonList());
        assertEquals(expectedList, actualList);
    }

    @Test
    public void execute_invalidFilterType_throwsCommandException() {
        ListCommand command = new ListCommand(null, null, "invalidfilter", "value");
        CommandException thrown = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(ListCommand.MESSAGE_INVALID_FILTER, thrown.getMessage());
    }

    @Test
    public void execute_invalidSortType_throwsCommandException() {
        ListCommand command = new ListCommand("invalidtype", "asc", null, null);
        CommandException thrown = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(ListCommand.MESSAGE_INVALID_SORT, thrown.getMessage());
    }

    @Test
    public void execute_invalidSortOrder_throwsCommandException() {
        ListCommand command = new ListCommand("name", "invalidorder", null, null);
        CommandException thrown = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(ListCommand.MESSAGE_INVALID_SORT_ORDER, thrown.getMessage());
    }

    @Test
    public void execute_filterByCourseNoMatch_returnsNoStudents() {
        ListCommand command = new ListCommand(null, null, "course", "placeholder");
        Predicate<Person> expectedPredicate = person -> "placeholder".equals("placeholder");
        //true for placeholder
        expectedModel.updateFilteredPersonList(expectedPredicate);

        assertCommandSuccess(command, model, ListCommand.MESSAGE_LIST_FILTERED, expectedModel);
    }

    @Test
    public void execute_filteredByCourseInvalidValue_throwsCommandException() {
        ListCommand command = new ListCommand(null, null, "course", "invalidValue");
        CommandException thrown = assertThrows(CommandException.class, () -> command.execute(model));
        String message = String.format(ListCommand.MESSAGE_NONEXISTENT_FILTER_VALUE, command.filterValue);
        assertEquals(message, thrown.getMessage());
    }

    @Test
    public void execute_filteredByTeamInvalidValue_throwsCommandException() {
        ListCommand command = new ListCommand(null, null, "team", "invalidValue");
        CommandException thrown = assertThrows(CommandException.class, () -> command.execute(model));
        String message = String.format(ListCommand.MESSAGE_NONEXISTENT_FILTER_VALUE, command.filterValue);
        assertEquals(message, thrown.getMessage());
    }

    @Test
    public void execute_filteredByProgressInvalidValue_throwsCommandException() {
        ListCommand command = new ListCommand(null, null, "progress", "invalidValue");
        CommandException thrown = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(ListCommand.MESSAGE_INVALID_FILTER_VALUE, thrown.getMessage());
    }

    @Test
    public void equals() {
        // Same values
        ListCommand command1 = new ListCommand("name", "asc", "course", "cs2103");
        ListCommand command1Copy = new ListCommand("name", "asc", "course", "cs2103");
        assertEquals(command1, command1);
        assertEquals(command1, command1Copy);

        // Different sort type
        ListCommand command2 = new ListCommand("progress", "asc", "course", "cs2103");
        assertNotEquals(command1, command2);

        // Different sort order
        ListCommand command3 = new ListCommand("name", "des", "course", "cs2103");
        assertNotEquals(command1, command3);

        // Different filter type
        ListCommand command4 = new ListCommand("name", "asc", "team", "teamA");
        assertNotEquals(command1, command4);

        // Different filter value
        ListCommand command5 = new ListCommand("name", "asc", "course", "cs1101s");
        assertNotEquals(command1, command5);

        // Empty parameters (default constructor)
        ListCommand defaultCommand = new ListCommand();
        ListCommand defaultCommandCopy = new ListCommand();
        assertEquals(defaultCommand, defaultCommandCopy);
        assertNotEquals(defaultCommand, command1);

        // different type -> returns false
        assertNotEquals(5, command1);

    }
}
