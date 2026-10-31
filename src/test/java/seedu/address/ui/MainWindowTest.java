package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.HelpCommand.SHOWING_HELP_MESSAGE;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;

/**
 * Tests help behavior in {@link MainWindow}.
 */
@ExtendWith(ApplicationExtension.class)
public class MainWindowTest {

    private static final String ERROR_MESSAGE = "The data file could not be loaded.\nClose the window now.";

    private MainWindow mainWindow;

    @Start
    public void start(Stage stage) {
        mainWindow = new MainWindow(stage, new LogicStub(), Path.of("data", "addressbook.json"));
        mainWindow.fillInnerParts();
        mainWindow.show();
    }

    @Test
    public void handleHelp_helpRequested_showsCompleteHelpInFeedbackBox(FxRobot robot) {
        robot.interact(() -> {
            mainWindow.handleHelp();
            TextArea resultDisplay = (TextArea) mainWindow.getRoot().getScene()
                    .lookup(".result-display");
            assertEquals(SHOWING_HELP_MESSAGE.lines().toList(),
                    resultDisplay.getText().lines().toList());
        });
    }

    @Test
    public void handleHelp_repeatedRequest_reusesFeedbackBox(FxRobot robot) {
        robot.interact(() -> {
            TextArea originalResultDisplay = (TextArea) mainWindow.getRoot().getScene()
                    .lookup(".result-display");
            mainWindow.handleHelp();
            mainWindow.handleHelp();

            TextArea currentResultDisplay = (TextArea) mainWindow.getRoot().getScene()
                    .lookup(".result-display");
            assertSame(originalResultDisplay, currentResultDisplay);
            assertEquals(1, mainWindow.getRoot().getScene().getRoot()
                    .lookupAll(".result-display").size());
        });
    }

    @Test
    public void showError_multiLineMessage_showsWholeMessageAsErrorInFeedbackBox(FxRobot robot) {
        robot.interact(() -> {
            mainWindow.showError(ERROR_MESSAGE);
            TextArea resultDisplay = (TextArea) mainWindow.getRoot().getScene()
                    .lookup(".result-display");
            assertEquals(ERROR_MESSAGE.lines().toList(), resultDisplay.getText().lines().toList());
            assertTrue(resultDisplay.getStyleClass().contains(ResultDisplay.ERROR_STYLE_CLASS));
            assertFalse(resultDisplay.getStyleClass().contains(ResultDisplay.WARNING_STYLE_CLASS));
        });
    }

    /**
     * A {@code Logic} stub with an empty student list that rejects every command.
     */
    static class LogicStub implements Logic {

        @Override
        public CommandResult execute(String commandText) throws CommandException, ParseException {
            throw new AssertionError("Command execution is not expected in this test.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList();
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
