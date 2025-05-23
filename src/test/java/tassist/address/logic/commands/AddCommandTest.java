package tassist.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tassist.address.testutil.Assert.assertThrows;
import static tassist.address.testutil.TypicalPersons.ALICE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tassist.address.commons.core.GuiSettings;
import tassist.address.logic.Messages;
import tassist.address.logic.commands.exceptions.CommandException;
import tassist.address.model.AddressBook;
import tassist.address.model.Model;
import tassist.address.model.ReadOnlyAddressBook;
import tassist.address.model.ReadOnlyUserPrefs;
import tassist.address.model.person.Github;
import tassist.address.model.person.Person;
import tassist.address.model.timedevents.TimedEvent;
import tassist.address.testutil.PersonBuilder;

public class AddCommandTest {

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPerson = new PersonBuilder().build();

        CommandResult commandResult = new AddCommand(validPerson).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPerson)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person validPerson = new PersonBuilder().build();
        AddCommand addCommand = new AddCommand(validPerson);
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_existingEmail_throwsCommandException() {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPerson = new PersonBuilder()
                .withStudentId("A1112222B")
                .withEmail("john@u.nus.edu")
                .build();
        modelStub.personsAdded.add(validPerson);

        Person personWithSameEmail = new PersonBuilder()
                .withStudentId("A3332222B")
                .withEmail("john@u.nus.edu").build();
        AddCommand addCommand = new AddCommand(personWithSameEmail);

        assertThrows(CommandException.class, AddCommand.MESSAGE_EXISTING_EMAIL, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_existingPhone_throwsCommandException() {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPerson = new PersonBuilder()
                .withStudentId("A1112222B")
                .withEmail("john@u.nus.edu")
                .withPhone("92929292")
                .build();
        modelStub.personsAdded.add(validPerson);

        Person personWithSamePhone = new PersonBuilder()
                .withStudentId("A3332222B")
                .withEmail("sarah@u.nus.edu")
                .withPhone("92929292")
                .build();
        AddCommand addCommand = new AddCommand(personWithSamePhone);

        assertThrows(CommandException.class, AddCommand.MESSAGE_EXISTING_PHONE, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_existingGithub_throwsCommandException() {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPerson = new PersonBuilder()
                .withStudentId("A1112222B")
                .withEmail("john@u.nus.edu")
                .withPhone("92929292")
                .withGithub("https://github.com/john")
                .build();
        modelStub.personsAdded.add(validPerson);

        Person personWithSameGithub = new PersonBuilder()
                .withStudentId("A3332222B")
                .withPhone("88883333")
                .withEmail("sarah@u.nus.edu")
                .withGithub("https://github.com/john")
                .build();
        AddCommand addCommand = new AddCommand(personWithSameGithub);

        assertThrows(CommandException.class, AddCommand.MESSAGE_EXISTING_GITHUB, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_defaultGithub_noDuplicationCheck() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPerson = new PersonBuilder()
                .withStudentId("A1112222B")
                .withEmail("john@u.nus.edu")
                .withPhone("92929292")
                .withGithub(Github.NO_GITHUB)
                .build();
        modelStub.personsAdded.add(validPerson);

        Person personWithDefaultGithub = new PersonBuilder()
                .withStudentId("A3332222B")
                .withPhone("88883333")
                .withEmail("sarah@u.nus.edu")
                .withGithub(Github.NO_GITHUB)
                .build();
        AddCommand addCommand = new AddCommand(personWithDefaultGithub);

        // Should not throw exception even though both have NO_GITHUB
        addCommand.execute(modelStub);
        assertTrue(modelStub.personsAdded.contains(personWithDefaultGithub));
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withStudentId("A1111111A").build();
        Person bob = new PersonBuilder().withStudentId("A2222222B").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(ALICE);
        String expected = AddCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateSortedPersonList(Comparator<Person> comparator) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasTimedEvent(TimedEvent timedEvent) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addTimedEvent(TimedEvent timedEvent) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteTimedEvent(TimedEvent timedEvent) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<TimedEvent> getTimedEventList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<TimedEvent> getFilteredTimedEventList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredTimedEventList(Predicate<TimedEvent> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateSortedTimedEventList(Comparator<TimedEvent> comparator) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }

        @Override
        public ObservableList<TimedEvent> getTimedEventList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public ObservableList<TimedEvent> getFilteredTimedEventList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public void updateFilteredTimedEventList(Predicate<TimedEvent> predicate) {
            // No-op for testing
        }

        @Override
        public void updateSortedTimedEventList(Comparator<TimedEvent> comparator) {
            // No-op for testing
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableList(personsAdded);
        }
    }

}
