package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import seedu.address.logic.commands.HelpCommand;

/**
 * A UI component that displays the result of a command execution.
 */
public class ResultDisplay extends UiPart<Region> {

    private static final String FXML = "ResultDisplay.fxml";

    private final Consumer<String> urlOpener;

    @FXML
    private TextArea resultDisplay;

    @FXML
    private ScrollPane helpDisplay;

    @FXML
    private Text helpText;

    @FXML
    private Hyperlink userGuideLink;

    /**
     * Creates a result display that uses {@code urlOpener} to open links.
     */
    public ResultDisplay(Consumer<String> urlOpener) {
        super(FXML);
        this.urlOpener = requireNonNull(urlOpener);
    }

    /**
     * Shows ordinary command feedback and hides inline help.
     */
    public void setFeedbackToUser(String feedbackToUser) {
        requireNonNull(feedbackToUser);
        resultDisplay.setText(feedbackToUser);
        resultDisplay.setManaged(true);
        resultDisplay.setVisible(true);
        helpDisplay.setManaged(false);
        helpDisplay.setVisible(false);
    }

    /**
     * Shows help in this result display without creating another window or panel.
     */
    public void showHelp() {
        helpText.setText(HelpCommand.HELP_MESSAGE_BEFORE_LINK);
        userGuideLink.setText(HelpCommand.USER_GUIDE_LABEL);
        resultDisplay.setManaged(false);
        resultDisplay.setVisible(false);
        helpDisplay.setManaged(true);
        helpDisplay.setVisible(true);
        helpDisplay.setVvalue(0);
    }

    @FXML
    private void openUserGuide() {
        urlOpener.accept(HelpCommand.USER_GUIDE_URL);
    }

}
