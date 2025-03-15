package tassist.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static tassist.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tassist.address.logic.parser.CliSyntax.PREFIX_CLASS;

import tassist.address.commons.core.index.Index;
import tassist.address.commons.exceptions.IllegalValueException;
import tassist.address.logic.commands.ClassCommand;
import tassist.address.logic.parser.exceptions.ParseException;
import tassist.address.model.person.ClassNumber;

/**
 * Parses input arguments and creates a new ClassCommand object
 */
public class ClassCommandParser implements Parser<ClassCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ClassCommand
     * and returns a ClassCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ClassCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_CLASS);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ClassCommand.MESSAGE_USAGE), ive);
        }

        String classNumber = argMultimap.getValue(PREFIX_CLASS)
                .filter(value -> !value.isEmpty())
                .orElse("No tutorial assigned");

        return new ClassCommand(index, new ClassNumber(classNumber));
    }
}
