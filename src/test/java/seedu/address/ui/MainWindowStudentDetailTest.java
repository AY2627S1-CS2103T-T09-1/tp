package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;

/**
 * Tests student detail behavior in {@link MainWindow}.
 */
@ExtendWith(ApplicationExtension.class)
public class MainWindowStudentDetailTest {

    private MainWindow mainWindow;

    @Start
    public void start(Stage stage) {
        mainWindow = new MainWindow(stage, new LogicStub(), Path.of("data", "addressbook.json"));
        mainWindow.fillInnerParts();
        mainWindow.show();
    }

    @Test
    public void execute_viewCommand_replacesListWithStudentDetails(FxRobot robot) {
        enterCommand(robot, "view 1");

        assertEquals("Alice Pauline", ((Label) mainWindow.getRoot().getScene().lookup("#name")).getText());
        assertEquals(1, getContentPlaceholder().getChildren().size());
    }

    @Test
    public void execute_repeatedViewCommand_replacesExistingDetails(FxRobot robot) {
        enterCommand(robot, "view 1");
        enterCommand(robot, "view 2");

        assertEquals("Benson Meier", ((Label) mainWindow.getRoot().getScene().lookup("#name")).getText());
        assertEquals(1, getContentPlaceholder().getChildren().size());
    }

    @Test
    public void execute_listCommand_restoresOriginalListPanel(FxRobot robot) {
        PersonListPanel personListPanel = mainWindow.getPersonListPanel();
        enterCommand(robot, "view 1");

        enterCommand(robot, "list");

        assertSame(personListPanel.getRoot(), getOnlyContentNode());
    }

    @Test
    public void execute_failedCommand_keepsStudentDetails(FxRobot robot) {
        enterCommand(robot, "view 1");
        Node originalDetails = getOnlyContentNode();

        enterCommand(robot, "fail");

        assertSame(originalDetails, getOnlyContentNode());
    }

    @Test
    public void execute_commandWithoutDisplayInstruction_keepsStudentDetails(FxRobot robot) {
        enterCommand(robot, "view 1");
        Node originalDetails = getOnlyContentNode();

        enterCommand(robot, "keep");

        assertSame(originalDetails, getOnlyContentNode());
    }

    private void enterCommand(FxRobot robot, String command) {
        robot.interact(() -> {
            TextField commandTextField =
                    (TextField) mainWindow.getRoot().getScene().lookup("#commandTextField");
            commandTextField.setText(command);
            commandTextField.fireEvent(new ActionEvent());
        });
    }

    private StackPane getContentPlaceholder() {
        return (StackPane) mainWindow.getRoot().getScene().lookup("#contentPanelPlaceholder");
    }

    private Node getOnlyContentNode() {
        return getContentPlaceholder().getChildren().get(0);
    }

    private static class LogicStub implements Logic {

        private final ObservableList<Person> persons = FXCollections.observableArrayList(ALICE, BENSON);

        @Override
        public CommandResult execute(String commandText) throws CommandException, ParseException {
            return switch (commandText) {
                case "view 1" -> CommandResult.showStudent("Viewing Alice", ALICE, INDEX_FIRST_PERSON);
                case "view 2" -> CommandResult.showStudent("Viewing Benson", BENSON, INDEX_SECOND_PERSON);
                case "list" -> CommandResult.showList("Listed all students");
                case "keep" -> new CommandResult("No display change");
                case "fail" -> throw new CommandException("Invalid command");
                default -> throw new AssertionError("Unexpected command: " + commandText);
            };
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return persons;
        }

        @Override
        public GuiSettings getGuiSettings() {
            return new GuiSettings();
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            // No state needs to be stored for this test.
        }
    }
}
