package seedu.address.ui;

import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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
        school.setText(person.getSchool().value);
        level.setText(person.getLevel().name());
        subjects.setText(person.getSubjects().stream().map(Object::toString).collect(Collectors.joining(", ")));      
        notes.setText(person.getNotes().stream()
                .map(note -> note.value)
                .collect(Collectors.joining("\n")));
    }
}
