package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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

            assertLabelText(".student-detail-heading", "Index: 1");
            assertLabelText("#name", ALICE.getName().fullName);
            assertLabelText("#school", ALICE.getSchool().value);
            assertLabelText("#level", ALICE.getLevel().toString());
            assertLabelText("#subjects", "Mathematics (G1)");
            assertLabelText("#phone", ALICE.getPhone().orElseThrow().value);
            assertLabelText("#guardianName", "Not provided");
            assertLabelText("#guardianPhone", "Not provided");
            assertGridRow("#name", 0);
            assertGridRow("#phone", 1);
            assertGridRow("#school", 2);
            assertGridRow("#level", 3);
            assertGridRow("#subjects", 4);
            assertGridRow("#guardianName", 5);
            assertGridRow("#guardianPhone", 6);
            assertNull(container.lookup("#heading"));
            assertNoteCardTexts("Strong in algebra", "Revise geometry");
        });
    }

    @Test
    public void constructor_studentWithoutNotes_displaysEmptyState(FxRobot robot) {
        robot.interact(() -> {
            StudentDetailPanel panel = new StudentDetailPanel(ALICE, INDEX_FIRST_PERSON, persons);
            container.getChildren().setAll(panel.getRoot());
            container.applyCss();

            assertLabelText("#emptyNotes", "No notes recorded.");
        });
    }

    @Test
    public void constructor_studentWithGuardian_displaysGuardianDetails(FxRobot robot) {
        Person personWithGuardian = new PersonBuilder(ALICE)
                .withGuardian("Mary Tan", "91234567")
                .build();

        robot.interact(() -> {
            StudentDetailPanel panel = new StudentDetailPanel(personWithGuardian, INDEX_FIRST_PERSON, persons);
            container.getChildren().setAll(panel.getRoot());
            container.applyCss();

            assertLabelText("#guardianName", "Mary Tan");
            assertLabelText("#guardianPhone", "91234567");
        });
    }

    @Test
    public void constructor_longValues_enablesTextWrapping(FxRobot robot) {
        robot.interact(() -> {
            Person personWithLongNote = new PersonBuilder(ALICE).withNotes("A long note").build();
            StudentDetailPanel panel = new StudentDetailPanel(personWithLongNote, INDEX_FIRST_PERSON, persons);
            container.getChildren().setAll(panel.getRoot());
            container.applyCss();

            assertLabelWraps("#name");
            assertLabelWraps("#school");
            assertLabelWraps("#level");
            assertLabelWraps("#subjects");
            assertLabelWraps("#phone");
            assertLabelWraps("#guardianName");
            assertLabelWraps("#guardianPhone");
            assertLabelWraps(".student-detail-note-card");
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

            assertNoteCardTexts("New note");
        });
    }

    private void assertLabelText(String query, String expectedText) {
        assertEquals(expectedText, ((Label) container.lookup(query)).getText());
    }

    private void assertLabelWraps(String query) {
        assertTrue(((Label) container.lookup(query)).isWrapText());
    }

    private void assertGridRow(String query, int expectedRow) {
        assertEquals(expectedRow, GridPane.getRowIndex(container.lookup(query)));
    }

    private void assertNoteCardTexts(String... expectedTexts) {
        List<String> noteTexts = container.lookupAll(".student-detail-note-card").stream()
                .map(Label.class::cast)
                .map(Label::getText)
                .toList();
        assertEquals(List.of(expectedTexts), noteTexts);
    }

    private void assertViewportMatchesPanelBackground() {
        Region viewport = (Region) container.lookup(".student-detail-scroll-pane > .viewport");
        Region panel = (Region) container.lookup(".student-detail-panel");
        assertEquals(panel.getBackground(), viewport.getBackground());
    }
}
