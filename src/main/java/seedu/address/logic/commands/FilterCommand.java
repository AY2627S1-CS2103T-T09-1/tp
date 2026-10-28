package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.predicate.StudentMatchesAllPredicate;

/**
 * Filters the student list to students who match every filter given, ignoring case.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows the students who match every filter "
            + "given, ignoring case.\n"
            + "Parameters: [" + PREFIX_NAME + "NAME] [" + PREFIX_SCHOOL + "SCHOOL] "
            + "[" + PREFIX_LEVEL + "LEVEL] [" + PREFIX_SUBJECT + "SUBJECT [" + PREFIX_BAND + "BAND]] "
            + "[" + PREFIX_PHONE + "PHONE] [" + PREFIX_EMAIL + "EMAIL] [" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_GUARDIAN_NAME + "GUARDIAN_NAME] [" + PREFIX_GUARDIAN_PHONE + "GUARDIAN_PHONE] "
            + "[" + PREFIX_TAG + "TAG] [" + PREFIX_NOTE + "TEXT] "
            + "(at least one, each at most once)\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_LEVEL + "S5 " + PREFIX_SUBJECT + "Mathematics "
            + PREFIX_BAND + "G2";

    public static final String MESSAGE_NO_STUDENTS_FOUND =
            "No students match your search. Check the spelling, or type list to see all students.";

    private static final Logger logger = LogsCenter.getLogger(FilterCommand.class);

    private final StudentMatchesAllPredicate predicate;

    /**
     * Creates a {@code FilterCommand} that shows the students matching {@code predicate}.
     */
    public FilterCommand(StudentMatchesAllPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        int matchCount = model.getFilteredPersonList().size();
        logger.fine("filter matched " + matchCount + " student(s) with " + predicate);
        if (matchCount == 0) {
            return CommandResult.showList(MESSAGE_NO_STUDENTS_FOUND);
        }
        return CommandResult.showList(String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, matchCount));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterCommand otherFilterCommand)) {
            return false;
        }

        return predicate.equals(otherFilterCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
