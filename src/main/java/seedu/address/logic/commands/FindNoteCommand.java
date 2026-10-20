package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.person.NoteContainsTextPredicate;
import seedu.address.model.person.Person;

/**
 * Lists all students with at least one note containing the given text, and the numbers of the matching notes.
 */
public class FindNoteCommand extends Command {

    public static final String COMMAND_WORD = "findnote";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows all students with a note "
            + "that contains the given text (case-insensitive), and the numbers of the matching notes.\n"
            + "Parameters: TEXT\n"
            + "Example: " + COMMAND_WORD + " algebra";

    public static final String MESSAGE_ONE_STUDENT_FOUND = "1 student with notes containing \"%1$s\":";
    public static final String MESSAGE_STUDENTS_FOUND = "%1$d students with notes containing \"%2$s\":";
    public static final String MESSAGE_STUDENT_LINE = "%1$d. %2$s: %3$s";
    public static final String MESSAGE_NO_STUDENTS_FOUND = "No student has a note containing \"%1$s\". "
            + "Check the spelling, or type list to see all students.";

    private static final String LABEL_ONE_NOTE = "note ";
    private static final String LABEL_MANY_NOTES = "notes ";
    private static final String NOTE_NUMBER_SEPARATOR = ", ";

    private final NoteContainsTextPredicate predicate;

    /**
     * Creates a {@code FindNoteCommand} that lists students matching {@code predicate}.
     */
    public FindNoteCommand(NoteContainsTextPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        List<Person> matchingStudents = model.getFilteredPersonList();
        if (matchingStudents.isEmpty()) {
            return CommandResult.showList(String.format(MESSAGE_NO_STUDENTS_FOUND, predicate.getText()));
        }
        return CommandResult.showList(formatResults(matchingStudents));
    }

    private String formatResults(List<Person> matchingStudents) {
        List<String> lines = new ArrayList<>();
        lines.add(formatHeader(matchingStudents.size()));
        for (int i = 0; i < matchingStudents.size(); i++) {
            Person student = matchingStudents.get(i);
            String noteNumbers = formatNoteNumbers(student);
            lines.add(String.format(MESSAGE_STUDENT_LINE, i + 1, student.getName(), noteNumbers));
        }
        return String.join(System.lineSeparator(), lines);
    }

    private String formatHeader(int studentCount) {
        return studentCount == 1
                ? String.format(MESSAGE_ONE_STUDENT_FOUND, predicate.getText())
                : String.format(MESSAGE_STUDENTS_FOUND, studentCount, predicate.getText());
    }

    private String formatNoteNumbers(Person student) {
        List<Integer> noteNumbers = predicate.getMatchingNoteNumbers(student);
        String label = noteNumbers.size() == 1 ? LABEL_ONE_NOTE : LABEL_MANY_NOTES;
        String numbers = noteNumbers.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(NOTE_NUMBER_SEPARATOR));
        return label + numbers;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindNoteCommand otherFindNoteCommand)) {
            return false;
        }

        return predicate.equals(otherFindNoteCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
