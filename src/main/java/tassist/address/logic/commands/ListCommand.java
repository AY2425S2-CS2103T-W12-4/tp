package tassist.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static tassist.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import tassist.address.logic.commands.exceptions.CommandException;
import tassist.address.model.Model;
import tassist.address.model.person.Person;

/**
 * Lists all students in the TAssist system, with optional sorting and filtering.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_SUCCESS = "Listed all students";
    public static final String MESSAGE_LIST_ALL = "Listed all students.";
    public static final String MESSAGE_LIST_FILTERED = "Listed students with filter applied.";
    public static final String MESSAGE_LIST_SORTED = "Listed students with sorting applied.";
    public static final String MESSAGE_LIST_FILTERED_SORTED = "Listed students with filter and sorting applied.";
    public static final String MESSAGE_NO_STUDENTS = "No students found.";
    public static final String MESSAGE_INVALID_SORT = "Invalid sort type. Allowed sort type: name, progress, github.";
    public static final String MESSAGE_INVALID_SORT_ORDER = "Invalid sort order. Allowed sort order: asc, des.";
    public static final String MESSAGE_MISSING_SORT_ORDER = "Please enter sort order. list s/[SORT TYPE] o/[SORT ORDER]"
            + "Allowed sort order: asc,des.";
    public static final String MESSAGE_INVALID_FILTER = "Invalid filter type. Allowed filter type: course, team, "
            + "progress";
    public static final String MESSAGE_INVALID_FILTER_VALUE = "This filter value does not exists.";
    public static final String MESSAGE_NONEXISTENT_FILTER_VALUE = "The '%s' filter value does not exist.";
    public static final String MESSAGE_MISSING_FILTER_VALUE = "Please enter filter value. list f/[FILTER TYPE] "
            + "fv/[FILTER VALUE]";

    public static final List<String> VALID_SORT_ORDERS = Arrays.asList("asc", "des");
    public static final List<String> VALID_SORT_TYPES = Arrays.asList("name", "progress", "github");
    public static final List<String> VALID_FILTER_TYPES = Arrays.asList("course", "team", "progress");

    public final String sortType;
    public final String sortOrder;
    public final String filterType;
    public final String filterValue;

    /**
     * Constructor for ListCommand with sorting and filtering.
     */
    public ListCommand(String sortType, String sortOrder, String filterType, String filterValue) {
        this.sortType = sortType != null ? sortType.toLowerCase() : null;
        this.sortOrder = sortOrder != null ? sortOrder.toLowerCase() : null;
        this.filterType = filterType != null ? filterType.toLowerCase() : null;
        this.filterValue = filterValue != null ? filterValue.toLowerCase() : null;
    }

    /**
     * Constructor for ListCommand without sorting and filtering.
     */
    public ListCommand() {
        this.sortType = null;
        this.sortOrder = null;
        this.filterType = null;
        this.filterValue = null;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        isValidFilterAndSort();
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        List<Person> list = model.getFilteredPersonList();

        boolean hasFilter = filterType != null && filterValue != null;
        boolean hasSort = sortType != null && sortOrder != null;

        if (hasFilter) {
            Predicate<Person> filter = getFilter(model, filterType, filterValue);
            model.updateFilteredPersonList(filter);
        }

        if (hasSort) {
            Comparator<Person> comp = this.getComparator(sortType, sortOrder);
            model.updateSortedPersonList(comp);
        }

        if (list.isEmpty()) {
            return new CommandResult(MESSAGE_NO_STUDENTS);
        }

        String message;
        if (hasFilter && hasSort) {
            message = MESSAGE_LIST_FILTERED_SORTED;
        } else if (hasFilter) {
            message = MESSAGE_LIST_FILTERED;
        } else if (hasSort) {
            message = MESSAGE_LIST_SORTED;
        } else {
            message = MESSAGE_LIST_ALL;
        }
        return new CommandResult(message);
    }

    private void isValidFilterAndSort() throws CommandException {
        if (sortType != null && !VALID_SORT_TYPES.contains(sortType)) {
            throw new CommandException(MESSAGE_INVALID_SORT);
        }

        if (sortOrder != null && !VALID_SORT_ORDERS.contains(sortOrder)) {
            throw new CommandException(MESSAGE_INVALID_SORT_ORDER);
        }

        if (filterType != null && !VALID_FILTER_TYPES.contains(filterType)) {
            throw new CommandException(MESSAGE_INVALID_FILTER);
        }
    }

    private Predicate<Person> getFilter(Model model, String filterType, String filterValue) throws CommandException {
        return switch (filterType) {
        case "course" -> {
            boolean hasCourse = model.getFilteredPersonList().stream().anyMatch(p ->
                    p.getCourse().equalsIgnoreCase(filterValue));
            if (!hasCourse) {
                throw new CommandException(String.format(MESSAGE_NONEXISTENT_FILTER_VALUE, filterValue));
            }
            yield p -> p.getCourse().equalsIgnoreCase(filterValue);
        }
        case "team" -> {
            boolean hasTeam = model.getFilteredPersonList().stream()
                    .anyMatch(p -> p.getProjectTeam().value.equalsIgnoreCase(filterValue));
            if (!hasTeam) {
                throw new CommandException(String.format(MESSAGE_NONEXISTENT_FILTER_VALUE, filterValue));
            }
            yield p -> p.getProjectTeam().value.equalsIgnoreCase(filterValue);
        }
        case "progress" -> {
            int filterProgress;
            try {
                filterProgress = Integer.parseInt(filterValue);
            } catch (NumberFormatException e) {
                throw new CommandException(MESSAGE_INVALID_FILTER_VALUE);
            }
            yield person -> person.getProgress().value <= filterProgress;
        }
        default -> throw new CommandException(MESSAGE_INVALID_FILTER);
        };
    }

    private Comparator<Person> getComparator(String sortType, String sortOrder) throws CommandException {
        Comparator<Person> comparator = switch (sortType) {
        case "name" -> Comparator.comparing(p -> p.getName().fullName.toLowerCase());
        case "progress"-> Comparator.comparing(p -> p.getProgress().value);
        case "github" -> Comparator.comparing(p -> p.getGithub().value.toLowerCase());
        default -> throw new CommandException(MESSAGE_INVALID_SORT);
        };
        return "des".equals(sortOrder) ? comparator.reversed() : comparator;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ListCommand)) {
            return false;
        }

        ListCommand otherCommand = (ListCommand) other;
        return Objects.equals(this.sortType, otherCommand.sortType)
                && Objects.equals(this.sortOrder, otherCommand.sortOrder)
                && Objects.equals(this.filterType, otherCommand.filterType)
                && Objects.equals(this.filterValue, otherCommand.filterValue);
    }

    @Override
    public String toString() {
        return "ListCommand{"
                + "sortType='" + sortType + '\''
                + ", sortOrder='" + sortOrder + '\''
                + ", filterType='" + filterType + '\''
                + ", filterValue='" + filterValue + '\''
                + '}';
    }
}
