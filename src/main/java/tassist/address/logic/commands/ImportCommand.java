package tassist.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static tassist.address.logic.parser.CliSyntax.PREFIX_FILE_PATH;
import static tassist.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.io.IOException;
import java.nio.file.Path;

import com.opencsv.exceptions.CsvException;

import tassist.address.commons.exceptions.DataLoadingException;
import tassist.address.commons.util.ToStringBuilder;
import tassist.address.logic.Messages;
import tassist.address.logic.commands.exceptions.CommandException;
import tassist.address.model.Model;
import tassist.address.model.ReadOnlyAddressBook;
import tassist.address.model.util.SampleDataUtil;
import tassist.address.storage.CsvJsonConverter;
import tassist.address.storage.Storage;

/**
 * Imports a CSV file containing a list of students into the address book.
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Imports a CSV file containing a list of students into the address book.\n"
            + "Parameters: FILE_PATH (must be a valid absolute path to the CSV file)\n"
            + "Example (absolute path): " + COMMAND_WORD + " " + PREFIX_FILE_PATH + " /path/to/file.csv";

    public static final String MESSAGE_IMPORT_SUCCESS = "Successfully imported CSV file: %1$s";
    public static final String MESSAGE_IMPORT_FAILURE = "Failed to import CSV file: %1$s";

    private final Path filePath;
    private final Storage storage;

    /**
     * Constructs an ImportCommand with a specified file path.
     *
     * @param filePath The file path of the CSV file to be imported.
     */
    public ImportCommand(Path filePath) {
        this.filePath = filePath;
        this.storage = null;
    }

    /**
     * Constructs an ImportCommand with a specified file path and storage.
     *
     * @param filePath The file path of the CSV file to be imported.
     * @param storage  The storage object used for handling the address book data.
     */
    public ImportCommand(Path filePath, Storage storage) {
        this.filePath = filePath;
        this.storage = storage;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (filePath == null) {
            throw new CommandException(Messages.MESSAGE_INVALID_FILE_PATH);
        }

        ReadOnlyAddressBook newData;

        try {
            CsvJsonConverter converter = new CsvJsonConverter();

            if (isCsvFile(filePath.toString())) {
                Path jsonFilePath = model.getAddressBookFilePath();
                converter.convertCsvToJson(filePath, jsonFilePath);
            } else {
                throw new CommandException(Messages.MESSAGE_INVALID_FILE_PATH); // Invalid file type (not CSV or JSON)
            }

            newData = storage.readAddressBook().orElseGet(SampleDataUtil::getSampleAddressBook);
            model.setAddressBook(newData);
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

            return new CommandResult(generateSuccessMessage());
        } catch (CsvException e) {
            throw new CommandException("Error during file conversion: invalid value", e);
        } catch (IOException e) {
            throw new CommandException("Invalid File Path", e);
        } catch (DataLoadingException e) {
            throw new CommandException("File is corrupted");
        }
    }

    private boolean isCsvFile(String filePath) {
        return filePath.endsWith(".csv");
    }

    /**
     * Generates a command execution success message based on whether
     * the CSV file is imported
     * {@code personToEdit}.
     */
    private String generateSuccessMessage() {
        String message = filePath != null ? MESSAGE_IMPORT_SUCCESS : MESSAGE_IMPORT_FAILURE;
        return message;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ImportCommand)) {
            return false;
        }

        ImportCommand otherImportCommand = (ImportCommand) other;
        return filePath.equals(otherImportCommand.filePath);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("filePath", filePath)
                .toString();
    }

    public Path getFilePath() {
        return filePath;
    }
}
