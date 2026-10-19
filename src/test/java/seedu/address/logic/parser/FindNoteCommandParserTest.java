package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindNoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Note;
import seedu.address.model.person.NoteContainsTextPredicate;

public class FindNoteCommandParserTest {

    private final FindNoteCommandParser parser = new FindNoteCommandParser();

    @Test
    public void parse_validText_returnsFindNoteCommand() {
        assertParseSuccess(parser, " algebra", commandFor("algebra"));
    }

    @Test
    public void parse_textWithExtraSpaces_collapsesSpaces() throws ParseException {
        // compares toString() because equals() already ignores extra spaces
        assertEquals(commandFor("next lesson").toString(), parser.parse("   next \t  lesson  ").toString());
    }

    @Test
    public void parse_phrase_keepsPhraseAsOneText() {
        assertParseSuccess(parser, " before the next lesson", commandFor("before the next lesson"));
    }

    @Test
    public void parse_blankText_throwsParseException() {
        assertParseFailure(parser, "", FindNoteCommandParser.MESSAGE_BLANK_TEXT);
        assertParseFailure(parser, "   ", FindNoteCommandParser.MESSAGE_BLANK_TEXT);
    }

    @Test
    public void parse_textWithSlashesAndPrefixes_treatedAsText() {
        assertParseSuccess(parser, " t/f", commandFor("t/f"));
        assertParseSuccess(parser, " nid/2", commandFor("nid/2"));
        assertParseSuccess(parser, " WA2 Math: 36/50", commandFor("WA2 Math: 36/50"));
    }

    @Test
    public void parse_textAtMaxLength_returnsFindNoteCommand() {
        String text = "a".repeat(Note.MAX_LENGTH);
        assertParseSuccess(parser, " " + text, commandFor(text));
    }

    @Test
    public void parse_textOverMaxLength_throwsParseException() {
        String text = "a".repeat(Note.MAX_LENGTH + 1);
        assertParseFailure(parser, " " + text, FindNoteCommandParser.MESSAGE_TEXT_TOO_LONG);
    }

    private static FindNoteCommand commandFor(String text) {
        return new FindNoteCommand(new NoteContainsTextPredicate(text));
    }
}
