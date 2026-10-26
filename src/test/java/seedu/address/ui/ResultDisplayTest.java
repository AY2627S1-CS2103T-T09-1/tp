package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * Tests that {@link ResultDisplay} cannot be edited from the keyboard.
 */
@ExtendWith(ApplicationExtension.class)
public class ResultDisplayTest {

    private static final String FEEDBACK = "Unknown command. Type help to see all commands.";
    private static final int CARET_POSITION = 8;
    private static final KeyCode[] DELETE_KEYS = {KeyCode.BACK_SPACE, KeyCode.DELETE};

    private ResultDisplay resultDisplay;

    @Start
    public void start(Stage stage) {
        // the display is shown in a window so that its built-in keyboard shortcuts are active
        resultDisplay = new ResultDisplay();
        stage.setScene(new Scene(resultDisplay.getRoot()));
        stage.show();
    }

    @Test
    public void deleteKeys_anyModifier_leaveFeedbackUnchanged(FxRobot robot) {
        robot.interact(() -> {
            resultDisplay.setFeedbackToUser(FEEDBACK);
            TextArea textArea = getTextArea();
            for (KeyCode key : DELETE_KEYS) {
                pressAndAssertUnchanged(textArea, key, false, false, false);
                pressAndAssertUnchanged(textArea, key, false, false, true);
                pressAndAssertUnchanged(textArea, key, false, true, false);
                pressAndAssertUnchanged(textArea, key, true, false, false);
            }
        });
    }

    @Test
    public void setFeedbackToUser_afterBlockedDelete_updatesFeedback(FxRobot robot) {
        robot.interact(() -> {
            resultDisplay.setFeedbackToUser(FEEDBACK);
            TextArea textArea = getTextArea();
            pressAndAssertUnchanged(textArea, KeyCode.BACK_SPACE, false, false, true);

            resultDisplay.setFeedbackToUser("Listed all students.");
            assertEquals("Listed all students.", textArea.getText());
        });
    }

    @Test
    public void selectAll_readOnlyFeedback_selectsWholeText(FxRobot robot) {
        robot.interact(() -> {
            resultDisplay.setFeedbackToUser(FEEDBACK);
            TextArea textArea = getTextArea();
            textArea.selectAll();
            assertEquals(FEEDBACK, textArea.getSelectedText());
        });
    }

    @Test
    public void selectAllShortcut_notBlocked_selectsWholeText(FxRobot robot) {
        robot.interact(() -> {
            resultDisplay.setFeedbackToUser(FEEDBACK);
            TextArea textArea = getTextArea();
            textArea.deselect();
            boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");
            // Cmd+A on macOS, Ctrl+A elsewhere
            textArea.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.A,
                    false, !isMac, false, isMac));
            assertEquals(FEEDBACK, textArea.getSelectedText());
        });
    }

    private TextArea getTextArea() {
        return (TextArea) resultDisplay.getRoot().lookup("#resultDisplay");
    }

    /**
     * Presses {@code key} with the given modifiers inside the display,
     * and checks that the feedback is unchanged.
     */
    private static void pressAndAssertUnchanged(TextArea textArea, KeyCode key, boolean isControlDown,
            boolean isAltDown, boolean isMetaDown) {
        textArea.positionCaret(CARET_POSITION);
        textArea.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", key,
                false, isControlDown, isAltDown, isMetaDown));
        String keyDescription = key + " (ctrl=" + isControlDown + ", alt=" + isAltDown
                + ", meta=" + isMetaDown + ")";
        assertEquals(FEEDBACK, textArea.getText(), "feedback changed after " + keyDescription);
    }
}
