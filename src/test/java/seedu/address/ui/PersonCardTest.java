package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Tests {@link PersonCard}.
 */
@ExtendWith(ApplicationExtension.class)
public class PersonCardTest {

    @Start
    public void start(Stage stage) {
        // Starts the JavaFX toolkit so that cards can be created; no window is needed.
    }

    @Test
    public void constructor_personWithAllDetails_displaysOnlySummaryFields(FxRobot robot) {
        Person person = new PersonBuilder(ALICE)
                .withAddress("12 Example Road")
                .withGuardian("Mary Tan", "91234567")
                .withNotes("Weak in algebra")
                .build();

        robot.interact(() -> {
            PersonCard card = new PersonCard(person, 3);
            assertLabelText(card, "#id", "3. ");
            assertLabelText(card, "#name", person.getName().fullName);
            assertLabelText(card, "#phone", person.getPhone().orElseThrow().value);
            assertLabelText(card, "#school", person.getSchool().value);
            assertLabelText(card, "#address", "12 Example Road");
            assertLabelText(card, "#level", person.getLevel().name());
            assertLabelWraps(card, "#name");
            assertLabelWraps(card, "#phone");
            assertLabelWraps(card, "#school");
            assertLabelWraps(card, "#address");
            assertLabelWraps(card, "#level");
            assertEquals(Pos.TOP_LEFT, ((HBox) card.getRoot().lookup("#name").getParent()).getAlignment());
            VBox cardContents = (VBox) card.getRoot().lookup("#phone").getParent();
            assertEquals(new Insets(12, 14, 12, 18), cardContents.getPadding());
            assertNull(card.getRoot().lookup("#guardian"));
            assertNull(card.getRoot().lookup("#subjects"));
            assertNull(card.getRoot().lookup("#notes"));
        });
    }

    @Test
    public void constructor_personWithoutPhone_hidesPhoneField(FxRobot robot) {
        Person person = new PersonBuilder(ALICE).withoutPhone().build();

        robot.interact(() -> {
            PersonCard card = new PersonCard(person, 1);
            Label phone = (Label) card.getRoot().lookup("#phone");
            assertFalse(phone.isVisible());
            assertFalse(phone.isManaged());
            Label address = (Label) card.getRoot().lookup("#address");
            assertFalse(address.isVisible());
            assertFalse(address.isManaged());
        });
    }

    private static void assertLabelText(PersonCard card, String query, String expectedText) {
        assertEquals(expectedText, ((Label) card.getRoot().lookup(query)).getText());
    }

    private static void assertLabelWraps(PersonCard card, String query) {
        assertTrue(((Label) card.getRoot().lookup(query)).isWrapText());
    }
}
