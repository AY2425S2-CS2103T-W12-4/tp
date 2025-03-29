package tassist.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import tassist.address.logic.commands.ImportCommand;

public class ImportCommandParserTest {
    private ImportCommandParser parser = new ImportCommandParser();

    @Test
    public void parse_validAbsolutePath_success() {
        String input = "import fp/ /stub/path/to/sample.csv";
        Path expectedPath = Paths.get("/stub/path/to/sample.csv").toAbsolutePath();
        ImportCommand expectedCommand = new ImportCommand(expectedPath);

        ImportCommand command = parser.parse(input);

        assertEquals(expectedCommand, command);
    }

    @Test
    public void parse_validRelativePath_success() {
        String input = "import fp/ data/sample.csv";
        Path expectedPath = Paths.get("data/sample.csv").toAbsolutePath();
        ImportCommand expectedCommand = new ImportCommand(expectedPath);

        ImportCommand command = parser.parse(input);

        assertEquals(expectedCommand, command);
    }
}
