package tassist.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static tassist.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static tassist.address.model.person.Github.NO_GITHUB;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import tassist.address.commons.core.index.Index;
import tassist.address.logic.Messages;
import tassist.address.logic.commands.exceptions.CommandException;
import tassist.address.model.Model;
import tassist.address.model.person.Github;
import tassist.address.model.person.Person;
import tassist.address.model.person.StudentId;

/**
 * Updates the github of an existing person in the address book.
 */
public class GithubCommand extends Command {

    public static final String COMMAND_WORD = "github";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the GitHub of the person identified "
            + "by the STUDENTID or INDEX. "
            + "Existing GitHub will be overwritten by the input.\n"
            + "Parameters: STUDENTID or INDEX , g/[GITHUB_URL]\n"
            + "Example:\n"
            + COMMAND_WORD + " 2 g/https://github.com/tammzz\n"
            + COMMAND_WORD + " A1234567B g/https://github.com/tammzz\n"
            + "Alternatively, use the command 'github INDEX/STUDENT_ID g/' to remove a student's GitHub.";

    public static final String MESSAGE_ADD_GITHUB_SUCCESS = "Set github to student: %1$s";
    public static final String MESSAGE_DELETE_GITHUB_SUCCESS = "Removed github from Person: %1$s";
    public static final String MESSAGE_DUPLICATE_GITHUB = "Error! This github belongs to another person";
    public static final String MESSAGE_INVALID_GITHUB = "Invalid gitHub URL!";

    private final Index index;
    private final StudentId studentId;
    private final Github github;

    /**
     * Constructs a {@code GithubCommand} that assigns a Github URL to a student identified by their student ID.
     *
     * @param studentId of person
     * @param github of the person to be updated to
     */
    public GithubCommand(StudentId studentId, Github github) {
        requireNonNull(studentId);
        requireNonNull(github);

        this.studentId = studentId;
        this.github = github;
        this.index = null;
    }

    /**
     * Constructs a {@code GithubCommand} that assigns a Github URL to a student identified by their index.
     *
     * @param index of person in list
     * @param github of person to be updated to
     */
    public GithubCommand(Index index, Github github) {
        requireNonNull(index);
        requireNonNull(github);

        this.studentId = null;
        this.github = github;
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person personToEdit = null;

        if (index != null) {
            List<Person> lastShownList = model.getFilteredPersonList();
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            personToEdit = lastShownList.get(index.getZeroBased());
        }
        if (studentId != null) {
            Optional<Person> personOptional = model.getFilteredPersonList().stream().filter(
                    person -> person.getStudentId().equals(studentId)).findFirst();
            if (personOptional.isEmpty()) {
                throw new CommandException(Messages.MESSAGE_PERSON_NOT_FOUND + studentId);
            }
            personToEdit = personOptional.get();
        }
        checkDuplicates(model, personToEdit);

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getClassNumber(),
                personToEdit.getStudentId(),
                github,
                personToEdit.getProjectTeam(),
                personToEdit.getRepository(),
                personToEdit.getTags(),
                personToEdit.getProgress(),
                personToEdit.getTimedEventsList());

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    /**
     * Checks if Github link already belongs to another student
     * @param personToEdit Github will be excluded
     * @throws CommandException
     */
    private void checkDuplicates(Model model, Person personToEdit) throws CommandException {
        List<Github> githubList = model.getFilteredPersonList().stream()
                .filter(person -> !person.equals(personToEdit))
                .map(person -> person.getGithub())
                .filter(g -> !g.value.equals(NO_GITHUB))
                .toList();
        if (!github.value.equals(Github.NO_GITHUB) && githubList.contains(github)) {
            throw new CommandException(MESSAGE_DUPLICATE_GITHUB);
        }
    }

    /**
     * Generates a command execution success message based on whether
     * the github is added to or removed from
     * {@code personToEdit}.
     */
    private String generateSuccessMessage(Person personToEdit) {
        String message = !github.value.equals(NO_GITHUB) ? MESSAGE_ADD_GITHUB_SUCCESS : MESSAGE_DELETE_GITHUB_SUCCESS;
        return String.format(message, Messages.format(personToEdit));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof GithubCommand)) {
            return false;
        }

        GithubCommand e = (GithubCommand) other;
        return Objects.equals(studentId, e.studentId)
                && Objects.equals(index, e.index)
                && Objects.equals(github, e.github);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
                .append("GithubCommand{");
        if (index != null) {
            builder.append("index=").append(index);
        } else if (studentId != null) {
            builder.append("studentId=").append(studentId);
        }
        builder.append(", github=").append(github)
                .append("}");
        return builder.toString();
    }
}
