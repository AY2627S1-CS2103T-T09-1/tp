package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static seedu.address.testutil.TypicalPersons.ALICE;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.scene.control.Label;
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
                .withGuardian("Mary Tan", "91234567")
                .withNotes("Weak in algebra")
                .build();

        robot.interact(() -> {
            PersonCard card = new PersonCard(person, 3);
            assertLabelText(card, "#id", "3. ");
            assertLabelText(card, "#name", person.getName().fullName);
            assertLabelText(card, "#phone", person.getPhone().orElseThrow().value);
            assertLabelText(card, "#school", person.getSchool().value);
            assertLabelText(card, "#level", person.getLevel().name());
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
        });
    }

    @Test
    public void constructor_personWithoutAddress_hidesAddressField(FxRobot robot) {
        Person person = new PersonBuilder(ALICE).withoutAddress().build();

        robot.interact(() -> {
            PersonCard card = new PersonCard(person, 1);
            Label address = (Label) card.getRoot().lookup("#address");
            assertFalse(address.isVisible());
            assertFalse(address.isManaged());
        });
    }

    private static void assertLabelText(PersonCard card, String query, String expectedText) {
        assertEquals(expectedText, ((Label) card.getRoot().lookup(query)).getText());
    }
}
