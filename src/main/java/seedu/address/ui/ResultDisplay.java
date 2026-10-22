package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

/**
 * A UI component that displays the result of a command execution.
 */
public class ResultDisplay extends UiPart<Region> {

    public static final String WARNING_STYLE_CLASS = "warning";
    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "ResultDisplay.fxml";

    @FXML
    private TextArea resultDisplay;

    @FXML
    private Label cancellationDisplay;

    public ResultDisplay() {
        super(FXML);
    }

    public void setFeedbackToUser(String feedbackToUser) {
        setFeedbackToUser(feedbackToUser, false);
    }

    /**
     * Displays feedback and applies warning styling when requested.
     */
    public void setFeedbackToUser(String feedbackToUser, boolean isWarning) {
        setFeedbackToUser(feedbackToUser, isWarning, null, false);
    }

    /**
     * Displays command feedback with optional cancellation notice and error styling.
     */
    public void setFeedbackToUser(String feedbackToUser, boolean isWarning, String cancellationNotice,
            boolean isError) {
        requireNonNull(feedbackToUser);
        cancellationDisplay.setText(cancellationNotice == null ? "" : cancellationNotice);
        cancellationDisplay.setVisible(cancellationNotice != null);
        cancellationDisplay.setManaged(cancellationNotice != null);
        cancellationDisplay.getStyleClass().remove(ERROR_STYLE_CLASS);
        if (cancellationNotice != null) {
            cancellationDisplay.getStyleClass().add(ERROR_STYLE_CLASS);
        }
        resultDisplay.setText(feedbackToUser);
        resultDisplay.getStyleClass().remove(WARNING_STYLE_CLASS);
        resultDisplay.getStyleClass().remove(ERROR_STYLE_CLASS);
        if (isError) {
            resultDisplay.getStyleClass().add(ERROR_STYLE_CLASS);
        } else if (isWarning) {
            resultDisplay.getStyleClass().add(WARNING_STYLE_CLASS);
        }
    }

}
