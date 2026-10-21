package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.List;
import java.util.stream.Stream;

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

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_SCHOOL, PREFIX_LEVEL)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_SCHOOL, PREFIX_LEVEL,
                PREFIX_GUARDIAN_NAME, PREFIX_GUARDIAN_PHONE);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        // Phone is optional; a person added without p/ has no phone
        Phone phone = argMultimap.getValue(PREFIX_PHONE).isPresent()
                ? ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get())
                : null;
        School school = ParserUtil.parseSchool(argMultimap.getValue(PREFIX_SCHOOL).get());
        Level level = ParserUtil.parseLevel(argMultimap.getValue(PREFIX_LEVEL).get());

        List<Subject> subjects = ParserUtil.parseSubjects(argMultimap);
        // Guardian is optional; gn/ and gp/ must be given together
        Guardian guardian = ParserUtil.parseGuardian(argMultimap).orElse(null);
        Person person = new Person(name, phone, school, level, subjects, List.of(), guardian);

        return new AddCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Returns the arguments with repeated ordinary spaces collapsed to one.
     */
    private static String normalizeRepeatedSpaces(String args) {
        return args.replaceAll(REPEATED_SPACES_REGEX, SINGLE_SPACE);
    }

}
