package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Note;

public class PersonCardTest {

    @Test
    public void formatNotes_noNotes_returnsEmptyString() {
        assertEquals("", PersonCard.formatNotes(List.of()));
    }

    @Test
    public void formatNotes_oneNote_prefixedWithOne() {
        assertEquals("1. Strong in Algebra", PersonCard.formatNotes(List.of(new Note("Strong in Algebra"))));
    }

    @Test
    public void formatNotes_multipleNotes_numberedInListOrder() {
        List<Note> notes = List.of(new Note("Strong in Algebra"), new Note("Weak in Geometry"),
                new Note("WA2 Math: 36/50"));
        assertEquals("1. Strong in Algebra\n2. Weak in Geometry\n3. WA2 Math: 36/50",
                PersonCard.formatNotes(notes));
    }

    @Test
    public void formatNotes_duplicateNotes_numberedSeparately() {
        List<Note> notes = List.of(new Note("Bring calculator"), new Note("Bring calculator"));
        assertEquals("1. Bring calculator\n2. Bring calculator", PersonCard.formatNotes(notes));
    }

    @Test
    public void formatNotes_symbolsAndMaxLengthNote_keptWhole() {
        String maxLengthText = "a".repeat(Note.MAX_LENGTH);
        List<Note> notes = List.of(new Note("Call Rajesh s/o Kumar"), new Note("Mary-Jane O'Brien +65 9123 4567."),
                new Note("2 hours on 36/50 paper"), new Note(maxLengthText));
        String expected = "1. Call Rajesh s/o Kumar\n"
                + "2. Mary-Jane O'Brien +65 9123 4567.\n"
                + "3. 2 hours on 36/50 paper\n"
                + "4. " + maxLengthText;
        assertEquals(expected, PersonCard.formatNotes(notes));
    }
}
