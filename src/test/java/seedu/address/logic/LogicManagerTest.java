package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.LEVEL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.SCHOOL_DESC_AMY;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.AMY;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.PersonBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        assertCommandException(deleteCommand, DeleteCommand.MESSAGE_INVALID_STUDENT_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_listWithArguments_throwsParseException() {
        String invalidListCommand = ListCommand.COMMAND_WORD + " 3";
        String expectedMessage = String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE);
        assertParseException(invalidListCommand, expectedMessage);
    }

    @Test
    public void execute_firstClear_showsConfirmationWithoutChangingModelOrStorage() throws Exception {
        model.addPerson(AMY);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Path addressBookFile = temporaryFolder.resolve("addressBook.json");
        logic.execute(ListCommand.COMMAND_WORD);
        String expectedStoredData = Files.readString(addressBookFile);

        assertCommandSuccess(ClearCommand.COMMAND_WORD,
                CommandResult.warning(ClearCommand.MESSAGE_CONFIRMATION), expectedModel);
        assertEquals(expectedStoredData, Files.readString(addressBookFile));
    }

    @Test
    public void execute_secondConsecutiveClear_clearsAddressBook() throws Exception {
        model.addPerson(AMY);
        logic.execute(ClearCommand.COMMAND_WORD);

        assertCommandSuccess(ClearCommand.COMMAND_WORD,
                CommandResult.showList(ClearCommand.MESSAGE_SUCCESS), new ModelManager());
    }

    @Test
    public void execute_clearThenOtherCommand_cancelsConfirmation() throws Exception {
        model.addPerson(AMY);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        logic.execute(ClearCommand.COMMAND_WORD);
        logic.execute(ListCommand.COMMAND_WORD);

        assertCommandSuccess(ClearCommand.COMMAND_WORD,
                CommandResult.warning(ClearCommand.MESSAGE_CONFIRMATION), expectedModel);
    }

    @Test
    public void execute_clearThenInvalidInput_cancelsConfirmation() throws Exception {
        model.addPerson(AMY);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        logic.execute(ClearCommand.COMMAND_WORD);
        assertParseException("unknownCommand", MESSAGE_UNKNOWN_COMMAND);

        assertCommandSuccess(ClearCommand.COMMAND_WORD,
                CommandResult.warning(ClearCommand.MESSAGE_CONFIRMATION), expectedModel);
    }

    @Test
    public void execute_clearOnEmptyAddressBook_showsEmptyMessageWithoutPendingConfirmation() throws Exception {
        assertCommandSuccess(ClearCommand.COMMAND_WORD,
                new CommandResult(ClearCommand.MESSAGE_EMPTY), model);
        model.addPerson(AMY);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(ClearCommand.COMMAND_WORD,
                CommandResult.warning(ClearCommand.MESSAGE_CONFIRMATION), expectedModel);
    }

    @Test
    public void execute_clearWithEmptyFilteredList_showsConfirmation() throws Exception {
        model.addPerson(AMY);
        model.updateFilteredPersonList(unused -> false);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(unused -> false);

        assertCommandSuccess(ClearCommand.COMMAND_WORD,
                CommandResult.warning(ClearCommand.MESSAGE_CONFIRMATION), expectedModel);
    }

    @Test
    public void execute_clearWithArguments_throwsParseException() {
        String invalidClearCommand = ClearCommand.COMMAND_WORD + " 3";
        String expectedMessage = String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, ClearCommand.MESSAGE_USAGE);
        assertParseException(invalidClearCommand, expectedMessage);
    }

    @Test
    public void execute_exitWithArguments_throwsParseException() {
        String invalidExitCommand = ExitCommand.COMMAND_WORD + " 3";
        String expectedMessage = String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, ExitCommand.MESSAGE_USAGE);
        assertParseException(invalidExitCommand, expectedMessage);
    }

    @Test
    public void execute_helpWithArguments_throwsParseException() {
        String invalidHelpCommand = HelpCommand.COMMAND_WORD + " 3";
        String expectedMessage = String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE);
        assertParseException(invalidHelpCommand, expectedMessage);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION, String.format(
                LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION, String.format(
                LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredPersonList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command and confirms that the result and model match the expected values.
     */
    private void assertCommandSuccess(String inputCommand, CommandResult expectedCommandResult,
            Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedCommandResult, result);
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     *
     * @param e the exception to be thrown by the Storage component
     * @param expectedMessage the message expected inside exception thrown by the Logic component
     */
    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        Path prefPath = temporaryFolder.resolve("ExceptionUserPrefs.json");

        // Inject LogicManager with a JsonAddressBookStorage that throws the IOException e when saving
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(prefPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);

        logic = new LogicManager(model, storage);

        // Triggers the saveAddressBook method by executing an add command
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + SCHOOL_DESC_AMY
                + LEVEL_DESC_AMY + " sub/Mathematics band/G1";
        Person expectedPerson = new PersonBuilder(AMY).build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addPerson(expectedPerson);
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }
}
