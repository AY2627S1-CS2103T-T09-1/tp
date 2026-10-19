package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that at least one of a {@code Person}'s notes contains the given text,
 * ignoring case and extra spaces.
 */
public class NoteContainsTextPredicate implements Predicate<Person> {

    private final String text;
    private final String comparisonText;

    /**
     * Creates a predicate that matches notes containing {@code text}.
     *
     * @param text Text to search for. Must not be blank.
     */
    public NoteContainsTextPredicate(String text) {
        requireNonNull(text);
        checkArgument(!text.isBlank());
        this.text = text;
        comparisonText = normalize(text);
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean test(Person person) {
        return !getMatchingNoteNumbers(person).isEmpty();
    }

    /**
     * Returns the one-based numbers of {@code person}'s notes that contain the text, in note order.
     *
     * @param person Person whose notes are searched.
     * @return Numbers of the matching notes, or an empty list if none match.
     */
    public List<Integer> getMatchingNoteNumbers(Person person) {
        requireNonNull(person);
        List<Note> notes = person.getNotes();
        List<Integer> noteNumbers = new ArrayList<>();
        for (int i = 0; i < notes.size(); i++) {
            if (normalize(notes.get(i).value).contains(comparisonText)) {
                noteNumbers.add(i + 1);
            }
        }
        return noteNumbers;
    }

    private static String normalize(String value) {
        return value.strip().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NoteContainsTextPredicate otherPredicate)) {
            return false;
        }

        return comparisonText.equals(otherPredicate.comparisonText);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("text", text).toString();
    }
}
