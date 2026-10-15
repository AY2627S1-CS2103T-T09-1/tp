package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Tests {@link StudentDetailPanel}.
 */
@ExtendWith(ApplicationExtension.class)
public class StudentDetailPanelTest {

    private StackPane container;
    private ObservableList<Person> persons;

    @Start
    public void start(Stage stage) {
        container = new StackPane();
        persons = FXCollections.observableArrayList(ALICE);
        stage.setScene(new Scene(container));
        stage.getScene().getStylesheets().add(getClass().getResource("/view/DarkTheme.css").toExternalForm());
        stage.show();
    }

    @Test
    public void constructor_studentWithDetails_displaysCompleteRecord(FxRobot robot) {
        Person personWithNotes = new PersonBuilder(ALICE)
                .withNotes("Strong in algebra", "Revise geometry")
                .build();

        robot.interact(() -> {
            StudentDetailPanel panel = new StudentDetailPanel(personWithNotes, INDEX_FIRST_PERSON, persons);
            container.getChildren().setAll(panel.getRoot());
            container.applyCss();

            assertLabelText("#heading", "Student 1 from current results");
            assertLabelText("#name", ALICE.getName().fullName);
            assertLabelText("#school", ALICE.getSchool().value);
            assertLabelText("#level", ALICE.getLevel().toString());
            assertLabelText("#subjects", "Mathematics (G1)");
            assertLabelText("#phone", ALICE.getPhone().orElseThrow().value);
            assertLabelText("#guardianName", "Not provided");
            assertLabelText("#guardianPhone", "Not provided");
            assertLabelText("#notes", "Strong in algebra" + System.lineSeparator() + "Revise geometry");
        });
    }

    @Test
    public void constructor_studentWithoutNotes_displaysEmptyState(FxRobot robot) {
        robot.interact(() -> {
            StudentDetailPanel panel = new StudentDetailPanel(ALICE, INDEX_FIRST_PERSON, persons);
            container.getChildren().setAll(panel.getRoot());
            container.applyCss();

            assertLabelText("#notes", "No notes recorded.");
        });
    }

    @Test
    public void constructor_longValues_enablesTextWrapping(FxRobot robot) {
        robot.interact(() -> {
            StudentDetailPanel panel = new StudentDetailPanel(ALICE, INDEX_FIRST_PERSON, persons);
            container.getChildren().setAll(panel.getRoot());
            container.applyCss();

            assertLabelWraps("#name");
            assertLabelWraps("#school");
            assertLabelWraps("#level");
            assertLabelWraps("#subjects");
            assertLabelWraps("#phone");
            assertLabelWraps("#guardianName");
            assertLabelWraps("#guardianPhone");
            assertLabelWraps("#notes");
            assertViewportMatchesPanelBackground();
        });
    }

    @Test
    public void sourceList_studentReplaced_refreshesDisplayedRecord(FxRobot robot) {
        robot.interact(() -> {
            StudentDetailPanel panel = new StudentDetailPanel(ALICE, INDEX_FIRST_PERSON, persons);
            container.getChildren().setAll(panel.getRoot());
            container.applyCss();
            Person updatedPerson = new PersonBuilder(ALICE).withNotes("New note").build();

            persons.set(INDEX_FIRST_PERSON.getZeroBased(), updatedPerson);

            assertLabelText("#notes", "New note");
        });
    }

    private void assertLabelText(String query, String expectedText) {
        assertEquals(expectedText, ((Label) container.lookup(query)).getText());
    }

    private void assertLabelWraps(String query) {
        assertTrue(((Label) container.lookup(query)).isWrapText());
    }

    private void assertViewportMatchesPanelBackground() {
        Region viewport = (Region) container.lookup(".student-detail-scroll-pane > .viewport");
        Region panel = (Region) container.lookup(".student-detail-panel");
        assertEquals(panel.getBackground(), viewport.getBackground());
    }
}
