package tassist.address.logic;

import static tassist.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import tassist.address.commons.core.GuiSettings;
import tassist.address.commons.core.LogsCenter;
import tassist.address.logic.commands.Command;
import tassist.address.logic.commands.CommandResult;
import tassist.address.logic.commands.ConfirmableCommand;
import tassist.address.logic.commands.ImportCommand;
import tassist.address.logic.commands.OpenCommand;
import tassist.address.logic.commands.exceptions.CommandException;
import tassist.address.logic.parser.AddressBookParser;
import tassist.address.logic.parser.exceptions.ParseException;
import tassist.address.model.Model;
import tassist.address.model.ReadOnlyAddressBook;
import tassist.address.model.person.Person;
import tassist.address.model.timedevents.TimedEvent;
import tassist.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data to file: %s";
    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file due to permission error: %s";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final AddressBookParser addressBookParser;
    private final OpenCommand.BrowserService browserService;
    private ConfirmableCommand pendingConfirmation = null;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this(model, storage, new OpenCommand.DesktopBrowserService());
    }

    /**
     * Constructs a {@code LogicManager} with the given {@code Model}, {@code Storage}, and {@code BrowserService}.
     */
    public LogicManager(Model model, Storage storage, OpenCommand.BrowserService browserService) {
        this.model = model;
        this.storage = storage;
        this.addressBookParser = new AddressBookParser();
        this.browserService = browserService;
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        if (pendingConfirmation != null) {
            if (commandText.equalsIgnoreCase("Y")) {
                ConfirmableCommand confirmedCommand = pendingConfirmation;
                pendingConfirmation = null;
                return confirmedCommand.executeConfirmed(model);
            } else if (commandText.equalsIgnoreCase("N")) {
                pendingConfirmation = null;
                return new CommandResult("Action cancelled.");
            } else {
                return new CommandResult("Invalid response. Please enter Y/N.");
            }
        }

        CommandResult commandResult;
        Command command = addressBookParser.parseCommand(commandText);

        if (command instanceof OpenCommand) {
            OpenCommand openCommand = (OpenCommand) command;
            if (openCommand.getTargetStudentId() != null) {
                command = new OpenCommand(openCommand.getTargetStudentId(), browserService);
            } else if (openCommand.getTargetIndex() != null) {
                command = new OpenCommand(openCommand.getTargetIndex(), browserService);
            } else {
                // won't reach here, throwing an exception just in case
                throw new CommandException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, OpenCommand.MESSAGE_USAGE));
            }
        } else if (command instanceof ImportCommand) {
            ImportCommand importCommand = (ImportCommand) command;
            command = new ImportCommand(importCommand.getFilePath(), storage);
        }

        commandResult = command.execute(model);

        if (commandResult.requiresConfirmation()) {
            pendingConfirmation = commandResult.getPendingConfirmation();
        }

        try {
            storage.saveAddressBook(model.getAddressBook());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public ObservableList<TimedEvent> getTimedEventList() {
        return model.getTimedEventList();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
