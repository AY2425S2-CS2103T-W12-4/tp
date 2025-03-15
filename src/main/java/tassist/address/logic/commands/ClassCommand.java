package tassist.address.logic.commands;

import static tassist.address.commons.util.CollectionUtil.requireAllNonNull;
import static tassist.address.logic.parser.CliSyntax.PREFIX_CLASS;

import tassist.address.commons.core.index.Index;
import tassist.address.logic.commands.exceptions.CommandException;
import tassist.address.model.Model;
import tassist.address.model.person.ClassNumber;

/**
 * Assigns a student to a tutorial class identified using it's displayed index from the address book.
 */
public class ClassCommand extends Command {

    public static final String COMMAND_WORD = "class";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Assign a tutorial class to the student identified"
            + "by the index. " // will be changed to student id once ready
            + "Existing tutorial class will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_CLASS + " [Tutorial Class Number]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_CLASS + " T01";

    public static final String MESSAGE_ARGUMENTS = "Index: %1$d, Class Number: %2$s";

    private final Index index;
    private final ClassNumber classNumber;

    /**
     * @param index of the student in the filtered student list to edit the tutorial class number
     * @param classNumber of the student to be updated to
     */
    public ClassCommand(Index index, ClassNumber classNumber) {
        requireAllNonNull(index, classNumber);

        this.index = index;
        this.classNumber = classNumber;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException(
                String.format(MESSAGE_ARGUMENTS, index.getOneBased(), classNumber));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ClassCommand)) {
            return false;
        }

        // state check
        ClassCommand otherClassCommand = (ClassCommand) other;
        return index.equals(otherClassCommand.index)
                && classNumber.equals(otherClassCommand.classNumber);
    }
}
