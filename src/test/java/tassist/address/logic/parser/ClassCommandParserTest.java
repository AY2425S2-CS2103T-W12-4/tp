package tassist.address.logic.parser;

import static tassist.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tassist.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static tassist.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tassist.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tassist.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import tassist.address.commons.core.index.Index;
import tassist.address.logic.commands.ClassCommand;
import tassist.address.model.person.ClassNumber;

public class ClassCommandParserTest {
    private ClassCommandParser parser = new ClassCommandParser();
    private final String nonEmptyClass = "T01";

    @Test
    public void parse_indexSpecified_success() {
        // have Class
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_CLASS + nonEmptyClass;
        ClassCommand expectedCommand = new ClassCommand(INDEX_FIRST_PERSON, new ClassNumber(nonEmptyClass));
        assertParseSuccess(parser, userInput, expectedCommand);

        // no Class
        userInput = targetIndex.getOneBased() + " " + PREFIX_CLASS;
        expectedCommand = new ClassCommand(INDEX_FIRST_PERSON, new ClassNumber("No tutorial assigned"));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClassCommand.MESSAGE_USAGE);

        // no parameters
        assertParseFailure(parser, ClassCommand.COMMAND_WORD, expectedMessage);

        // no index
        assertParseFailure(parser, ClassCommand.COMMAND_WORD + " " + nonEmptyClass, expectedMessage);
    }
}
