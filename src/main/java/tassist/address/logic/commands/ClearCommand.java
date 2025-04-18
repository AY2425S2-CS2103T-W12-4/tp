package tassist.address.logic.commands;

import static java.util.Objects.requireNonNull;

import tassist.address.model.AddressBook;
import tassist.address.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setAddressBook(new AddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public String toString() {
        return "ClearCommand{}";
    }
}
