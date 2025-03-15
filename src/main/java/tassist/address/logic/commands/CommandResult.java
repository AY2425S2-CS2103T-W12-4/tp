package tassist.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import tassist.address.commons.util.ToStringBuilder;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    private final ConfirmableCommand pendingConfirmation;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.pendingConfirmation = null; // No confirmation required
    }

    /**
     * Constructs a {@code CommandResult} that requires confirmation before execution.
     */
    public CommandResult(String feedbackToUser, ConfirmableCommand pendingConfirmation) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = false;
        this.exit = false;
        this.pendingConfirmation = pendingConfirmation;
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean requiresConfirmation() {
        return pendingConfirmation != null;
    }

    public ConfirmableCommand getPendingConfirmation() {
        return pendingConfirmation;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                       && showHelp == otherCommandResult.showHelp
                       && exit == otherCommandResult.exit
                       && Objects.equals(pendingConfirmation, otherCommandResult.pendingConfirmation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit, pendingConfirmation);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                       .add("feedbackToUser", feedbackToUser)
                       .add("showHelp", showHelp)
                       .add("exit", exit)
                       .add("requiresConfirmation", requiresConfirmation())
                       .toString();
    }
}
