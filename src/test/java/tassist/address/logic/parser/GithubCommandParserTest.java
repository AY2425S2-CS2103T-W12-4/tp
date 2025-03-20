package tassist.address.logic.parser;

import static tassist.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tassist.address.logic.commands.CommandTestUtil.VALID_STUDENTID_BOB;
import static tassist.address.logic.parser.CliSyntax.PREFIX_GITHUB;
import static tassist.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tassist.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import tassist.address.logic.commands.GithubCommand;
import tassist.address.model.person.Github;
import tassist.address.model.person.StudentId;

public class GithubCommandParserTest {
    private GithubCommandParser parser = new GithubCommandParser();
    private final String nonEmptyGithub = "https://github.com/default";

    @Test
    public void parse_studentIdSpecified_success() {
        String targetStudentId = VALID_STUDENTID_BOB;
        String userInput = targetStudentId + " " + PREFIX_GITHUB + nonEmptyGithub;
        GithubCommand expectedCommand = new GithubCommand(
                new StudentId(targetStudentId), new Github(nonEmptyGithub));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, GithubCommand.MESSAGE_USAGE);

        // no parameters
        assertParseFailure(parser, GithubCommand.COMMAND_WORD, expectedMessage);

        // no index
        assertParseFailure(parser, GithubCommand.COMMAND_WORD + " " + nonEmptyGithub, expectedMessage);
    }
}
