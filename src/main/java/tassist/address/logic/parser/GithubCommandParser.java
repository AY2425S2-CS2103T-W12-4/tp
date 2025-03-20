package tassist.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static tassist.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tassist.address.logic.parser.CliSyntax.PREFIX_GITHUB;

import tassist.address.commons.exceptions.IllegalValueException;
import tassist.address.logic.commands.GithubCommand;
import tassist.address.logic.parser.exceptions.ParseException;
import tassist.address.model.person.Github;
import tassist.address.model.person.StudentId;

/**
 * Parses input arguments and creates a new {@code GithubCommand} object
 */
public class GithubCommandParser implements Parser<GithubCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code GithubCommand}
     * and returns a {@code GithubCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public GithubCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_GITHUB);

        StudentId studentId;
        try {
            studentId = ParserUtil.parseStudentId(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    GithubCommand.MESSAGE_USAGE), ive);
        }

        if (argMultimap.getValue(PREFIX_GITHUB).isEmpty()) {
            throw new ParseException(String.format(GithubCommand.MESSAGE_EMPTY));
        }

        String github = argMultimap.getValue(PREFIX_GITHUB).orElse("");
        try {
            return new GithubCommand(studentId, new Github(github));
        } catch (IllegalArgumentException e) {
            throw new ParseException(GithubCommand.MESSAGE_INVALID_GITHUB, e);
        }
    }

}
