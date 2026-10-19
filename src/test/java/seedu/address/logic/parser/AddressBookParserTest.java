package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NID;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteNoteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindNoteCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.NoteContainsTextPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertEquals(new ClearCommand(), parser.parseCommand(ClearCommand.COMMAND_WORD));
        assertEquals(new ClearCommand(), parser.parseCommand(ClearCommand.COMMAND_WORD + "   "));
    }

    @Test
    public void parseCommand_forceOption_returnsForcedClearCommand() throws Exception {
        assertEquals(new ClearCommand(true),
                parser.parseCommand(ClearCommand.COMMAND_WORD + " " + ClearCommand.FORCE_OPTION));
        assertEquals(new ClearCommand(true),
                parser.parseCommand(ClearCommand.COMMAND_WORD + "   " + ClearCommand.FORCE_OPTION + "   "));
    }

    @Test
    public void parseCommand_clearWithArguments_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () ->
                parser.parseCommand(ClearCommand.COMMAND_WORD + " 3"));
        assertThrows(ParseException.class, expectedMessage, () ->
                parser.parseCommand(ClearCommand.COMMAND_WORD + " additional text"));
        assertThrows(ParseException.class, expectedMessage, () ->
                parser.parseCommand(ClearCommand.COMMAND_WORD + " " + ClearCommand.FORCE_OPTION
                        + " " + ClearCommand.FORCE_OPTION));
        assertThrows(ParseException.class, expectedMessage, () ->
                parser.parseCommand(ClearCommand.COMMAND_WORD + " --force"));
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_deleteNote() throws Exception {
        DeleteNoteCommand command = (DeleteNoteCommand) parser.parseCommand(DeleteNoteCommand.COMMAND_WORD
                + " " + INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_NID + "2");
        assertEquals(new DeleteNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(2)), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + "   ") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_exitWithArguments_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExitCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () ->
                parser.parseCommand(ExitCommand.COMMAND_WORD + " 3"));
        assertThrows(ParseException.class, expectedMessage, () ->
                parser.parseCommand(ExitCommand.COMMAND_WORD + " additional text"));
    }

    @Test
    public void parseCommand_find() throws Exception {
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + PREFIX_NAME + "foo bar");
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(List.of("foo bar"))), command);
    }

    @Test
    public void parseCommand_findNote() throws Exception {
        FindNoteCommand command = (FindNoteCommand) parser.parseCommand(
                FindNoteCommand.COMMAND_WORD + " algebra");
        assertEquals(new FindNoteCommand(new NoteContainsTextPredicate("algebra")), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + "   ") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_helpWithArguments_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () ->
                parser.parseCommand(HelpCommand.COMMAND_WORD + " 3"));
        assertThrows(ParseException.class, expectedMessage, () ->
                parser.parseCommand(HelpCommand.COMMAND_WORD + " additional text"));
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
    }

    @Test
    public void parseCommand_listWithArguments_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () ->
                parser.parseCommand(ListCommand.COMMAND_WORD + " 3"));
        assertThrows(ParseException.class, expectedMessage, () ->
                parser.parseCommand(ListCommand.COMMAND_WORD + " additional text"));
    }

    @Test
    public void parseCommand_view() throws Exception {
        ViewCommand command = (ViewCommand) parser.parseCommand(
                ViewCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new ViewCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
