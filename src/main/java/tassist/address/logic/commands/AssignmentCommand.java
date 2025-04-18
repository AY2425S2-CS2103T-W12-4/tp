package tassist.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static tassist.address.logic.parser.CliSyntax.PREFIX_DATE;
import static tassist.address.logic.parser.CliSyntax.PREFIX_NAME;

import tassist.address.logic.commands.exceptions.CommandException;
import tassist.address.model.Model;
import tassist.address.model.timedevents.Assignment;

/**
 * Adds an assignment to the address book.
 */
public class AssignmentCommand extends Command {

    public static final String COMMAND_WORD = "assignment";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an assignment to the address book.\n"
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_DATE + "DATE\n"
            + "Example:\n"
            + COMMAND_WORD + " "
            + PREFIX_NAME + "CS2103T Project "
            + PREFIX_DATE + "30-01-2025";

    public static final String MESSAGE_SUCCESS = "New assignment added: %1$s";
    public static final String MESSAGE_DUPLICATE_ASSIGNMENT = "This assignment already exists in the address book";

    private final Assignment toAdd;

    /**
     * Creates an AssignmentCommand to add the specified {@code Assignment}
     */
    public AssignmentCommand(Assignment assignment) {
        requireNonNull(assignment);
        toAdd = assignment;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        try {
            if (model.hasTimedEvent(toAdd)) {
                throw new CommandException(MESSAGE_DUPLICATE_ASSIGNMENT);
            }

            model.addTimedEvent(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd.toString()));
        } catch (IllegalArgumentException e) {
            throw new CommandException(e.getMessage());
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AssignmentCommand)) {
            return false;
        }

        AssignmentCommand otherAssignmentCommand = (AssignmentCommand) other;
        return toAdd.equals(otherAssignmentCommand.toAdd);
    }

    @Override
    public String toString() {
        return "AssignmentCommand{toAdd=" + toAdd + "}";
    }
}
