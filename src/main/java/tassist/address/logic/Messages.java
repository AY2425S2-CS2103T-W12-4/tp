package tassist.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import tassist.address.logic.parser.Prefix;
import tassist.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_INVALID_TIMED_EVENT_DISPLAYED_INDEX =
            "The timed event index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_PERSON_NOT_FOUND = "No student found with StudentId: ";
    public static final String MESSAGE_INVALID_FILE_PATH = "Invalid file path!";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        /*
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Tutorial Class Number: ")
                .append(person.getClassNumber())
                .append("; StudentId: ")
                .append(person.getStudentId())
                .append("; Github: ")
                .append(person.getGithub())
                .append("; ProjectTeam: ")
                .append(person.getTeam())
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        builder.append("; Progress: ").append(person.getProgress());
        return builder.toString();
         */
        return person.getName().toString();
    }

}
