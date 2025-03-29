package tassist.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static tassist.address.logic.parser.CliSyntax.PREFIX_FILE_PATH;

import java.nio.file.Path;

import tassist.address.logic.commands.ImportCommand;

/**
 * Parses input arguments and creates a new ImportCommand object
 */
public class ImportCommandParser implements Parser<ImportCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ImportCommand
     * and returns an ImportCommand object for execution.
     */
    @Override
    public ImportCommand parse(String args) {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_FILE_PATH);

        Path filePath = ParserUtil.parseFilePath(argMultimap.getValue(PREFIX_FILE_PATH)
                .filter(value -> !value.isEmpty())
                .orElse(""));

        return new ImportCommand(filePath);
    }
}
