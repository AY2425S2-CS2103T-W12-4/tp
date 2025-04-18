package tassist.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static tassist.address.logic.parser.CliSyntax.PREFIX_REPOSITORY;
import static tassist.address.logic.parser.CliSyntax.PREFIX_REPOSITORY_NAME;
import static tassist.address.logic.parser.CliSyntax.PREFIX_USERNAME;
import static tassist.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static tassist.address.model.person.Repository.MESSAGE_CONSTRAINTS;
import static tassist.address.model.person.Repository.MESSAGE_REPOSITORY_NAME_VALIDITY;
import static tassist.address.model.person.Repository.MESSAGE_USERNAME_VALIDITY;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import tassist.address.commons.core.index.Index;
import tassist.address.logic.Messages;
import tassist.address.logic.commands.exceptions.CommandException;
import tassist.address.model.Model;
import tassist.address.model.person.Person;
import tassist.address.model.person.Repository;
import tassist.address.model.person.StudentId;

/**
 * Updates the Repository of an existing person in the address book.
 */
public class RepoCommand extends Command {

    public static final String COMMAND_WORD = "repo";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the repository of the person identified by the STUDENTID or INDEX. "
            + "Existing URL will be overwritten by the input.\n"
            + "Parameters: STUDENTID or INDEX, "
            + PREFIX_USERNAME + "USERNAME" + PREFIX_REPOSITORY_NAME + "REPOSITORY_NAME or\n"
            + "Parameters: STUDENTID or INDEX, " + PREFIX_REPOSITORY + "REPOSITORY_URL\n"
            + "Example:\n"
            + COMMAND_WORD + " 2 un/Group-4 rn/WealthVault\n"
            + COMMAND_WORD + " AxxxxxxxN un/Tutorial-G08 rn/BestApp or\n"
            + COMMAND_WORD + " 3 r/https://github.com/team4/new.repo\n"
            + COMMAND_WORD + " AxxxxxxxN r/https://github.com/AY2425S2-CS2103T-W12-4/tp";

    public static final String MESSAGE_ADD_REPOSITORY_SUCCESS = "Set repository to student: %1$s";
    public static final String MESSAGE_NO_INDEX_STUDENTID = "Please enter valid index or student Id!";

    //allow multiple people to have the same repository (team repo)
    public static final String MESSAGE_INVALID_USERNAME = "Invalid username!\n"
            + MESSAGE_USERNAME_VALIDITY;
    public static final String MESSAGE_INVALID_REPOSITORY_NAME = "Invalid repository name!\n"
            + MESSAGE_REPOSITORY_NAME_VALIDITY;

    public static final String MESSAGE_INVALID_URL = "Invalid URL!\n"
            + MESSAGE_CONSTRAINTS;

    public static final String MESSAGE_VALID_COMMAND = "Either provide a full repository URL (r/) "
            + "or both username (un/) and repository name (rn/).\n";
    public final String username;
    public final String repositoryName;

    public final Index index;

    public final StudentId studentId;
    public final Repository repositoryUrl;

    /**
     * Constructs a {@code RepoCommand} that assigns a repository URL to a student identified by their student ID.
     *
     * @param studentId of the person in the list to edit the repository of
     * @param username used to update username
     * @param repositoryName used to update repositoryName
     */
    public RepoCommand(StudentId studentId, String username, String repositoryName, Repository repositoryUrl) {
        requireNonNull(studentId);
        this.studentId = studentId;
        this.username = username != null ? username : null;
        this.repositoryName = repositoryName != null ? repositoryName : null;
        this.repositoryUrl = repositoryUrl != null ? repositoryUrl : null;
        this.index = null;
    }

    /**
     * Constructs a {@code RepoCommand} that assigns a repository URL to a student identified by their index.
     *
     * @param index of the person in the list to edit the repository of
     * @param username used to update username
     * @param repositoryName used to update repositoryName
     */
    public RepoCommand(Index index, String username, String repositoryName, Repository repositoryUrl) {
        requireNonNull(index);
        this.studentId = null;
        this.username = username != null ? username : null;
        this.repositoryName = repositoryName != null ? repositoryName : null;
        this.repositoryUrl = repositoryUrl != null ? repositoryUrl : null;
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

        // Defensive check: ensure either full URL or both username and repo name are provided
        if (repositoryUrl == null && (username == null || repositoryName == null)) {
            throw new CommandException(MESSAGE_VALID_COMMAND);
        }

        Repository repo = repositoryUrl != null ? repositoryUrl : new Repository(username, repositoryName);

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getClassNumber(),
                personToEdit.getStudentId(),
                personToEdit.getGithub(),
                personToEdit.getProjectTeam(),
                repo,
                personToEdit.getTags(),
                personToEdit.getProgress());

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_ADD_REPOSITORY_SUCCESS, Messages.format(editedPerson)));
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RepoCommand)) {
            return false;
        }

        RepoCommand e = (RepoCommand) other;
        return Objects.equals(studentId, e.studentId)
                && Objects.equals(index, e.index)
                && Objects.equals(username, e.username)
                && Objects.equals(repositoryName, e.repositoryName)
                && Objects.equals(repositoryUrl, e.repositoryUrl);
    }
}
