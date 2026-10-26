package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_GUARDIAN_PAIR;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_SUBJECT_BAND_PAIR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.List;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Guardian;
import seedu.address.model.person.Level;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.School;
import seedu.address.model.person.Subject;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {
    private static final String REPEATED_SPACES_REGEX = " {2,}";
    private static final String SINGLE_SPACE = " ";


    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(normalizeRepeatedSpaces(args), PREFIX_NAME, PREFIX_PHONE,
                        PREFIX_SCHOOL, PREFIX_LEVEL,
                        PREFIX_SUBJECT, PREFIX_BAND, PREFIX_GUARDIAN_NAME, PREFIX_GUARDIAN_PHONE);

        // no parameters, or text before the first prefix, means the user needs the format, so show the usage
        if (args.isBlank() || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyAllPrefixesPresent(PREFIX_NAME, PREFIX_SCHOOL, PREFIX_LEVEL, PREFIX_SUBJECT);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_SCHOOL, PREFIX_LEVEL,
                PREFIX_GUARDIAN_NAME, PREFIX_GUARDIAN_PHONE);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        // Phone is optional; a person added without p/ has no phone
        Phone phone = argMultimap.getValue(PREFIX_PHONE).isPresent()
                ? ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get())
                : null;
        School school = ParserUtil.parseSchool(argMultimap.getValue(PREFIX_SCHOOL).get());
        Level level = ParserUtil.parseLevel(argMultimap.getValue(PREFIX_LEVEL).get());

        argMultimap.verifyEachFollowedBy(PREFIX_SUBJECT, PREFIX_BAND, MESSAGE_SUBJECT_BAND_PAIR);
        List<Subject> subjects = ParserUtil.parseSubjects(
                argMultimap.getAllValues(PREFIX_SUBJECT), argMultimap.getAllValues(PREFIX_BAND));
        // Guardian is optional; gn/ and gp/ must be given together
        argMultimap.verifyGivenTogether(PREFIX_GUARDIAN_NAME, PREFIX_GUARDIAN_PHONE, MESSAGE_GUARDIAN_PAIR);
        Guardian guardian = argMultimap.getValue(PREFIX_GUARDIAN_NAME).isPresent()
                ? ParserUtil.parseGuardian(argMultimap.getValue(PREFIX_GUARDIAN_NAME).get(),
                        argMultimap.getValue(PREFIX_GUARDIAN_PHONE).get())
                : null;
        Person person = new Person(name, phone, school, level, subjects, List.of(), guardian);

        return new AddCommand(person);
    }

    /**
     * Returns the arguments with repeated ordinary spaces collapsed to one.
     */
    private static String normalizeRepeatedSpaces(String args) {
        return args.replaceAll(REPEATED_SPACES_REGEX, SINGLE_SPACE);
    }
}
