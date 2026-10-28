package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_SUBJECT_BAND_PAIR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

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
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_SCHOOL, PREFIX_ADDRESS, PREFIX_EMAIL,
                        PREFIX_LEVEL, PREFIX_SUBJECT, PREFIX_BAND, PREFIX_GUARDIAN_NAME, PREFIX_GUARDIAN_PHONE,
                        PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_SCHOOL, PREFIX_ADDRESS, PREFIX_EMAIL,
                PREFIX_LEVEL, PREFIX_GUARDIAN_NAME, PREFIX_GUARDIAN_PHONE);

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
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            String address = argMultimap.getValue(PREFIX_ADDRESS).get();
            if (address.isBlank()) {
                editPersonDescriptor.setAddressRemoved();
            } else {
                editPersonDescriptor.setAddress(ParserUtil.parseAddress(address));
            }
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            String email = argMultimap.getValue(PREFIX_EMAIL).get();
            // e/ with no value removes the email
            if (email.isBlank()) {
                editPersonDescriptor.setEmailRemoved();
            } else {
                editPersonDescriptor.setEmail(ParserUtil.parseEmail(email));
            }
        }
        if (argMultimap.getValue(PREFIX_LEVEL).isPresent()) {
            editPersonDescriptor.setLevel(ParserUtil.parseLevel(argMultimap.getValue(PREFIX_LEVEL).get()));
        }

        if (argMultimap.getValue(PREFIX_SUBJECT).isPresent() || argMultimap.getValue(PREFIX_BAND).isPresent()) {
            // checked first so that band/ alone reports the missing sub/
            argMultimap.verifyAllPrefixesPresent(PREFIX_SUBJECT);
            ParserUtil.verifySubjectNamesHaveNoSlash(argMultimap.getAllValues(PREFIX_SUBJECT));
            argMultimap.verifyEachFollowedBy(PREFIX_SUBJECT, PREFIX_BAND, MESSAGE_SUBJECT_BAND_PAIR);
            editPersonDescriptor.setSubjects(ParserUtil.parseSubjects(
                    argMultimap.getAllValues(PREFIX_SUBJECT), argMultimap.getAllValues(PREFIX_BAND)));
        }
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);
        parseGuardianForEdit(argMultimap, editPersonDescriptor);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    /**
     * Parses {@code tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * A single {@code t/} with no value parses into an empty set, which clears the tags.
     *
     * @throws ParseException if any tag is invalid or repeated.
     */
    private static Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagNames = tags.size() == 1 && tags.contains("") ? List.of() : tags;
        return Optional.of(ParserUtil.parseTags(tagNames));
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
