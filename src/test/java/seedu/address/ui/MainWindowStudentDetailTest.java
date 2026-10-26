package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Path;
import java.util.List;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.ConfirmationManager;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

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
    public void startup_noStudentSelected_showsFixedSplitAndEmptyState(FxRobot robot) {
        GridPane contentGridPane = getContentGridPane();

        assertEquals(2, contentGridPane.getColumnConstraints().size());
        assertEquals(50, contentGridPane.getColumnConstraints().get(0).getPercentWidth());
        assertEquals(50, contentGridPane.getColumnConstraints().get(1).getPercentWidth());
        assertEquals("View a student to see details.", getEmptyStateLabel().getText());
        assertTrue(contentGridPane.lookupAll(".split-pane-divider").isEmpty());
    }

    @Test
    public void execute_viewCommand_showsListBesideStudentDetails(FxRobot robot) {
        Node originalList = mainWindow.getPersonListPanel().getRoot();

        enterCommand(robot, "view 1");

        assertEquals("Alice Pauline", ((Label) getStudentDetailNode().lookup("#name")).getText());
        assertEquals(2, getContentGridPane().getChildren().size());
        assertSame(originalList, getPersonListPanelPlaceholder().getChildren().get(0));
    }

    @Test
    public void execute_repeatedViewCommand_replacesExistingDetails(FxRobot robot) {
        Node originalList = mainWindow.getPersonListPanel().getRoot();
        enterCommand(robot, "view 1");
        enterCommand(robot, "view 2");

        assertEquals("Benson Meier", ((Label) getStudentDetailNode().lookup("#name")).getText());
        assertEquals(2, getContentGridPane().getChildren().size());
        assertSame(originalList, getPersonListPanelPlaceholder().getChildren().get(0));
    }

    @Test
    public void execute_listCommand_restoresOriginalListPanel(FxRobot robot) {
        PersonListPanel personListPanel = mainWindow.getPersonListPanel();
        enterCommand(robot, "view 1");

        enterCommand(robot, "list");

        assertEquals(2, getContentGridPane().getChildren().size());
        assertSame(personListPanel.getRoot(), getPersonListPanelPlaceholder().getChildren().get(0));
        assertEquals("View a student to see details.", getEmptyStateLabel().getText());
    }

    @Test
    public void execute_failedCommand_keepsStudentDetails(FxRobot robot) {
        enterCommand(robot, "view 1");
        Node originalDetails = getStudentDetailNode();

        enterCommand(robot, "fail");

        assertSame(originalDetails, getStudentDetailNode());
    }

    @Test
    public void execute_commandWithoutDisplayInstruction_keepsStudentDetails(FxRobot robot) {
        enterCommand(robot, "view 1");
        Node originalDetails = getStudentDetailNode();

        enterCommand(robot, "keep");

        assertSame(originalDetails, getStudentDetailNode());
    }

    @Test
    public void execute_commandUpdatesViewedStudent_refreshesStudentDetails(FxRobot robot) {
        enterCommand(robot, "view 1");

        enterCommand(robot, "addnote 1 t/New note");

        List<String> noteTexts = mainWindow.getRoot().getScene().getRoot()
                .lookupAll(".student-detail-note-card").stream()
                .map(Label.class::cast)
                .map(Label::getText)
                .toList();
        assertEquals(List.of("1. New note"), noteTexts);
    }

    @Test
    public void execute_editCommand_updatesListCardAndShowsEditedStudent(FxRobot robot) {
        enterCommand(robot, "edit 1");

        List<String> listNames = mainWindow.getPersonListPanel().getRoot().lookupAll("#name").stream()
                .map(Label.class::cast)
                .map(Label::getText)
                .toList();
        assertEquals("Alice Edited", listNames.get(0));
        assertEquals("Alice Edited", ((Label) getStudentDetailNode().lookup("#name")).getText());
    }

    @Test
    public void execute_warningResult_appliesWarningStyle(FxRobot robot) {
        enterCommand(robot, "warn");

        assertTrue(getResultDisplay().getStyleClass().contains(ResultDisplay.WARNING_STYLE_CLASS));
    }

    @Test
    public void execute_standardResultAfterWarning_removesWarningStyle(FxRobot robot) {
        enterCommand(robot, "warn");
        enterCommand(robot, "keep");

        assertFalse(getResultDisplay().getStyleClass().contains(ResultDisplay.WARNING_STYLE_CLASS));
    }

    @Test
    public void execute_cancelledCommand_showsRedNoticeAndKeepsResultColor(FxRobot robot) {
        enterCommand(robot, "cancelled");
        Label cancellationDisplay = (Label) mainWindow.getRoot().getScene().lookup("#cancellationDisplay");
        TextArea resultDisplay = getResultDisplay();
        robot.interact(() -> mainWindow.getRoot().getScene().getRoot().applyCss());

        assertEquals("The 'clear' command has been cancelled.", cancellationDisplay.getText());
        assertTrue(cancellationDisplay.getStyleClass().contains(ResultDisplay.ERROR_STYLE_CLASS));
        assertEquals("Listed students", resultDisplay.getText());
        assertFalse(resultDisplay.getStyleClass().contains(ResultDisplay.WARNING_STYLE_CLASS));
        assertFalse(resultDisplay.getStyleClass().contains(ResultDisplay.ERROR_STYLE_CLASS));

        enterCommand(robot, "replacement");
        assertTrue(cancellationDisplay.getStyleClass().contains(ResultDisplay.ERROR_STYLE_CLASS));
        assertTrue(resultDisplay.getStyleClass().contains(ResultDisplay.WARNING_STYLE_CLASS));

        enterCommand(robot, "cancelled-fail");
        assertTrue(cancellationDisplay.getStyleClass().contains(ResultDisplay.ERROR_STYLE_CLASS));
        assertTrue(resultDisplay.getStyleClass().contains(ResultDisplay.ERROR_STYLE_CLASS));
    }

    @Test
    public void warningAndErrorStyles_defaultStyles_haveDifferentTextColors(FxRobot robot) {
        Label warningLabel = new Label("Warning");
        warningLabel.getStyleClass().add(ResultDisplay.WARNING_STYLE_CLASS);
        Label errorLabel = new Label("Error");
        errorLabel.getStyleClass().add(CommandBox.ERROR_STYLE_CLASS);

        robot.interact(() -> {
            getPersonListPanelPlaceholder().getChildren().setAll(warningLabel, errorLabel);
            mainWindow.getRoot().getScene().getRoot().applyCss();
        });

        assertNotEquals(errorLabel.getTextFill(), warningLabel.getTextFill());
    }

    private void enterCommand(FxRobot robot, String command) {
        robot.interact(() -> {
            TextField commandTextField =
                    (TextField) mainWindow.getRoot().getScene().lookup("#commandTextField");
            commandTextField.setText(command);
            commandTextField.fireEvent(new ActionEvent());
        });
    }

    private GridPane getContentGridPane() {
        return (GridPane) mainWindow.getRoot().getScene().lookup("#contentGridPane");
    }

    private StackPane getPersonListPanelPlaceholder() {
        return (StackPane) mainWindow.getRoot().getScene().lookup("#personListPanelPlaceholder");
    }

    private Node getStudentDetailNode() {
        return getStudentDetailPanelPlaceholder().getChildren().get(0);
    }

    private StackPane getStudentDetailPanelPlaceholder() {
        return (StackPane) mainWindow.getRoot().getScene().lookup("#studentDetailPanelPlaceholder");
    }

    private Label getEmptyStateLabel() {
        return (Label) getStudentDetailNode();
    }

    private TextArea getResultDisplay() {
        return (TextArea) mainWindow.getRoot().getScene().lookup(".result-display");
    }

    private static class LogicStub implements Logic {

        private final ObservableList<Person> persons = FXCollections.observableArrayList(ALICE, BENSON);

        @Override
        public CommandResult execute(String commandText) throws CommandException, ParseException {
            return switch (commandText) {
                case "view 1" -> CommandResult.showStudent("Viewing Alice", ALICE, INDEX_FIRST_PERSON);
                case "view 2" -> CommandResult.showStudent("Viewing Benson", BENSON, INDEX_SECOND_PERSON);
                case "list" -> CommandResult.showList("Listed all students");
                case "addnote 1 t/New note" -> addNoteToFirstPerson();
                case "edit 1" -> editFirstPerson();
                case "keep" -> new CommandResult("No display change");
                case "warn" -> CommandResult.warning("Warning");
                case "cancelled" -> new CommandResult("Listed students").withCancellationNotice(
                        String.format(ConfirmationManager.MESSAGE_CANCELLED_FORMAT, "clear"));
                case "replacement" -> CommandResult.warning("Delete student?"
                        + ConfirmationManager.MESSAGE_CONFIRMATION_SUFFIX)
                        .withCancellationNotice(String.format(ConfirmationManager.MESSAGE_CANCELLED_FORMAT,
                                "clear"));
                case "cancelled-fail" -> throw new CommandException(
                        "The 'clear' command has been cancelled." + System.lineSeparator() + "Invalid command");
                case "fail" -> throw new CommandException("Invalid command");
                default -> throw new AssertionError("Unexpected command: " + commandText);
            };
        }

        private CommandResult addNoteToFirstPerson() {
            Person updatedPerson = new PersonBuilder(persons.get(INDEX_FIRST_PERSON.getZeroBased()))
                    .withNotes("New note")
                    .build();
            persons.set(INDEX_FIRST_PERSON.getZeroBased(), updatedPerson);
            return new CommandResult("Added note");
        }

        private CommandResult editFirstPerson() {
            Person updatedPerson = new PersonBuilder(persons.get(INDEX_FIRST_PERSON.getZeroBased()))
                    .withName("Alice Edited")
                    .build();
            persons.set(INDEX_FIRST_PERSON.getZeroBased(), updatedPerson);
            return CommandResult.showStudent("Edited Alice", updatedPerson, INDEX_FIRST_PERSON);
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
