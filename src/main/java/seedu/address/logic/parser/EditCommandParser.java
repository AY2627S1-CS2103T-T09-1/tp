package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_SCHOOL, PREFIX_LEVEL,
                        PREFIX_SUBJECT, PREFIX_BAND, PREFIX_GUARDIAN_NAME, PREFIX_GUARDIAN_PHONE);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_SCHOOL, PREFIX_LEVEL,
                PREFIX_GUARDIAN_NAME, PREFIX_GUARDIAN_PHONE);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            String phone = argMultimap.getValue(PREFIX_PHONE).get();
            // p/ with no number removes the phone
            if (phone.isBlank()) {
                editPersonDescriptor.setPhoneRemoved();
            } else {
                editPersonDescriptor.setPhone(ParserUtil.parsePhone(phone));
            }
        }
        if (argMultimap.getValue(PREFIX_SCHOOL).isPresent()) {
            editPersonDescriptor.setSchool(ParserUtil.parseSchool(argMultimap.getValue(PREFIX_SCHOOL).get()));
        }
        if (argMultimap.getValue(PREFIX_LEVEL).isPresent()) {
            editPersonDescriptor.setLevel(ParserUtil.parseLevel(argMultimap.getValue(PREFIX_LEVEL).get()));
        }

        if (argMultimap.getValue(PREFIX_SUBJECT).isPresent() || argMultimap.getValue(PREFIX_BAND).isPresent()) {
            editPersonDescriptor.setSubjects(ParserUtil.parseSubjects(argMultimap));
        }
        parseGuardianForEdit(argMultimap, editPersonDescriptor);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    /**
     * Sets the guardian edits in {@code argMultimap} on {@code editPersonDescriptor}.
     * {@code gn/} and {@code gp/} both with no value remove the guardian.
     *
     * @throws ParseException if only one of them is empty, or a given value is invalid.
     */
    private static void parseGuardianForEdit(ArgumentMultimap argMultimap,
            EditPersonDescriptor editPersonDescriptor) throws ParseException {
        Optional<String> guardianName = argMultimap.getValue(PREFIX_GUARDIAN_NAME);
        Optional<String> guardianPhone = argMultimap.getValue(PREFIX_GUARDIAN_PHONE);
        boolean isGuardianNameEmpty = guardianName.isPresent() && guardianName.get().isBlank();
        boolean isGuardianPhoneEmpty = guardianPhone.isPresent() && guardianPhone.get().isBlank();

        if (isGuardianNameEmpty && isGuardianPhoneEmpty) {
            editPersonDescriptor.setGuardianRemoved();
            return;
        }
        if (isGuardianNameEmpty || isGuardianPhoneEmpty) {
            throw new ParseException(EditCommand.MESSAGE_GUARDIAN_REMOVAL);
        }
        if (guardianName.isPresent()) {
            editPersonDescriptor.setGuardianName(ParserUtil.parseGuardianName(guardianName.get()));
        }
        if (guardianPhone.isPresent()) {
            editPersonDescriptor.setGuardianPhone(ParserUtil.parseGuardianPhone(guardianPhone.get()));
        }
    }

}
