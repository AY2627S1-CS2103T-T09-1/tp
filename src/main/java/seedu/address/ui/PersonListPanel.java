package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private static final PseudoClass DISPLAYED_PSEUDO_CLASS = PseudoClass.getPseudoClass("displayed");

    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);
    private Person displayedPerson;

    @FXML
    private ListView<Person> personListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList) {
        super(FXML);
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new PersonListViewCell());
        personListView.setFocusTraversable(false);
    }

    /**
     * Highlights the person whose details are displayed without changing the list selection.
     */
    public void highlightPerson(Person person) {
        displayedPerson = person;
        personListView.getSelectionModel().clearSelection();
        personListView.refresh();
        personListView.scrollTo(person);
    }

    /**
     * Removes the displayed-person highlight.
     */
    public void clearHighlight() {
        displayedPerson = null;
        personListView.getSelectionModel().clearSelection();
        personListView.refresh();
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        PersonListViewCell() {
            setMaxHeight(Region.USE_PREF_SIZE);
            addEventFilter(MouseEvent.MOUSE_PRESSED, MouseEvent::consume);
        }

        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);
            boolean isDisplayed = !empty && person != null && person.equals(displayedPerson);
            pseudoClassStateChanged(DISPLAYED_PSEUDO_CLASS, isDisplayed);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                Region card = new PersonCard(person, getIndex() + 1).getRoot();
                card.prefWidthProperty().bind(widthProperty());
                setGraphic(card);
            }
        }
    }

}
