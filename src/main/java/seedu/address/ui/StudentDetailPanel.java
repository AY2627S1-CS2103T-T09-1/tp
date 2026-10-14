package seedu.address.ui;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.commons.core.index.Index;
import seedu.address.model.person.Person;

/**
 * Panel containing the full record of one student.
 */
public class StudentDetailPanel extends UiPart<Region> {

    private static final String FXML = "StudentDetailPanel.fxml";
    private static final String NOT_PROVIDED_MESSAGE = "Not provided";
    private static final String NO_NOTES_MESSAGE = "No notes recorded.";

    private final ObservableList<Person> sourceList;
    private final Index displayedIndex;
    private final ListChangeListener<Person> sourceListListener = unused -> refreshFromSourceList();
    private final WeakListChangeListener<Person> weakSourceListListener =
            new WeakListChangeListener<>(sourceListListener);

    @FXML
    private Label heading;

    @FXML
    private Label name;

    @FXML
    private Label school;

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
    private Label notes;

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
        heading.setText(String.format("Student %d from current results", displayedIndex.getOneBased()));
        name.setText(person.getName().fullName);
        school.setText(person.getSchool().value);
        level.setText(NOT_PROVIDED_MESSAGE);
        subjects.setText(NOT_PROVIDED_MESSAGE);
        phone.setText(person.getPhone().value);
        guardianName.setText(NOT_PROVIDED_MESSAGE);
        guardianPhone.setText(NOT_PROVIDED_MESSAGE);

        String formattedNotes = String.join(System.lineSeparator(), person.getNotes());
        notes.setText(formattedNotes.isEmpty() ? NO_NOTES_MESSAGE : formattedNotes);
    }
}
