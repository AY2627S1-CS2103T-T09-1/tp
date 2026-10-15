package seedu.address.ui;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;

/**
 * A UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label school;
    @FXML
    private Label level;
    @FXML
    private Label subjects;
    @FXML
    private Label notes;
    @FXML
    private Label guardian;

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        person.getPhone().ifPresentOrElse(p -> phone.setText(p.value), () -> {
            phone.setVisible(false);
            phone.setManaged(false);
        });
        person.getGuardian().ifPresentOrElse(g -> guardian.setText("Guardian: " + g), () -> {
            guardian.setVisible(false);
            guardian.setManaged(false);
        });
        school.setText(person.getSchool().value);
        level.setText(person.getLevel().name());
        subjects.setText(person.getSubjects().stream().map(Object::toString).collect(Collectors.joining(", ")));
        notes.setText(formatNotes(person.getNotes()));
    }

    /**
     * Returns {@code notes} as one line per note, each starting with its one-based position,
     * such as "1. Strong in Algebra".
     *
     * @param notes Notes in the order they were added.
     * @return The numbered notes, or an empty string if there are none.
     */
    static String formatNotes(List<Note> notes) {
        return IntStream.range(0, notes.size())
                .mapToObj(i -> (i + 1) + ". " + notes.get(i).value)
                .collect(Collectors.joining("\n"));
    }
}
