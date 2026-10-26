package seedu.address.ui;

import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.stream.IntStream;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Subject;

/**
 * Panel containing the full record of one student.
 */
public class StudentDetailPanel extends UiPart<Region> {

    private static final String FXML = "StudentDetailPanel.fxml";
    private static final String NO_NOTES_MESSAGE = "No notes recorded.";

    private final ObservableList<Person> sourceList;
    private final Index displayedIndex;
    private final ListChangeListener<Person> sourceListListener = unused -> refreshFromSourceList();
    private final WeakListChangeListener<Person> weakSourceListListener =
            new WeakListChangeListener<>(sourceListListener);

    @FXML
    private Label studentIndex;

    @FXML
    private Label name;

    @FXML
    private Label school;

    @FXML
    private Label address;

    @FXML
    private Label level;

    @FXML
    private Label subjects;

    @FXML
    private Label phone;

    @FXML
    private Label guardianName;

    @FXML
    private Label guardianPhone;

    @FXML
    private VBox noteCards;

    /**
     * Creates a panel for {@code person}, using their index in {@code sourceList}.
     */
    public StudentDetailPanel(Person person, Index displayedIndex, ObservableList<Person> sourceList) {
        super(FXML);
        this.sourceList = sourceList;
        this.displayedIndex = displayedIndex;
        showPerson(person);
        sourceList.addListener(weakSourceListListener);
    }

    private void refreshFromSourceList() {
        int zeroBasedIndex = displayedIndex.getZeroBased();
        if (zeroBasedIndex < sourceList.size()) {
            showPerson(sourceList.get(zeroBasedIndex));
        }
    }

    private void showPerson(Person person) {
        studentIndex.setText(String.format("Index: %d", displayedIndex.getOneBased()));
        name.setText(person.getName().fullName);
        school.setText(person.getSchool().value);
        address.setText(person.getAddress().map(Object::toString).orElse(Messages.MESSAGE_NOT_PROVIDED));
        level.setText(person.getLevel().toString());
        subjects.setText(formatSubjects(person));
        phone.setText(person.getPhone().map(Object::toString).orElse(Messages.MESSAGE_NOT_PROVIDED));
        guardianName.setText(person.getGuardian()
                .map(guardian -> guardian.getName().fullName)
                .orElse(Messages.MESSAGE_NOT_PROVIDED));
        guardianPhone.setText(person.getGuardian()
                .map(guardian -> guardian.getPhone().value)
                .orElse(Messages.MESSAGE_NOT_PROVIDED));

        showNotes(person);
    }

    private void showNotes(Person person) {
        if (person.getNotes().isEmpty()) {
            Label emptyNotes = new Label(NO_NOTES_MESSAGE);
            emptyNotes.setId("emptyNotes");
            emptyNotes.getStyleClass().add("student-detail-empty-notes");
            noteCards.getChildren().setAll(emptyNotes);
            return;
        }

        List<Note> notes = person.getNotes();
        noteCards.getChildren().setAll(IntStream.range(0, notes.size())
                .mapToObj(i -> createNoteCard(notes.get(i), i + 1))
                .toList());
    }

    private Label createNoteCard(Note note, int displayedIndex) {
        Label noteCard = new Label(displayedIndex + ". " + note);
        noteCard.setWrapText(true);
        noteCard.setMaxWidth(Double.MAX_VALUE);
        noteCard.getStyleClass().add("student-detail-note-card");
        return noteCard;
    }

    private String formatSubjects(Person person) {
        return person.getSubjects().stream()
                .map(Subject::toString)
                .collect(joining(System.lineSeparator()));
    }
}
