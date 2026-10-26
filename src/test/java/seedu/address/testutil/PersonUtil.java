package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_BAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Person;

/**
 * A utility class for Person.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddCommand(Person person) {
        return AddCommand.COMMAND_WORD + " " + getPersonDetails(person);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + person.getName().fullName + " ");
        person.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE + phone.value + " "));
        sb.append(PREFIX_SCHOOL + person.getSchool().value + " ");
        person.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        sb.append(PREFIX_LEVEL + person.getLevel().name() + " ");
        person.getSubjects().forEach(subject -> sb.append(PREFIX_SUBJECT).append(subject.getName()).append(" ")
                .append(PREFIX_BAND).append(subject.getBand()).append(" "));
        person.getGuardian().ifPresent(guardian -> sb.append(PREFIX_GUARDIAN_NAME)
                .append(guardian.getName().fullName).append(" ")
                .append(PREFIX_GUARDIAN_PHONE).append(guardian.getPhone().value).append(" "));
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        if (descriptor.isPhoneRemoved()) {
            sb.append(PREFIX_PHONE).append(" ");
        }
        descriptor.getSchool().ifPresent(school -> sb.append(PREFIX_SCHOOL).append(school.value).append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        if (descriptor.isAddressRemoved()) {
            sb.append(PREFIX_ADDRESS).append(" ");
        }
        descriptor.getLevel().ifPresent(level -> sb.append(PREFIX_LEVEL).append(level.name()).append(" "));
        descriptor.getSubjects().ifPresent(subjects -> subjects.forEach(subject ->
                sb.append(PREFIX_SUBJECT).append(subject.getName()).append(" ")
                        .append(PREFIX_BAND).append(subject.getBand()).append(" ")));
        descriptor.getGuardianName().ifPresent(name ->
                sb.append(PREFIX_GUARDIAN_NAME).append(name.fullName).append(" "));
        descriptor.getGuardianPhone().ifPresent(phone ->
                sb.append(PREFIX_GUARDIAN_PHONE).append(phone.value).append(" "));
        if (descriptor.isGuardianRemoved()) {
            sb.append(PREFIX_GUARDIAN_NAME).append(" ").append(PREFIX_GUARDIAN_PHONE).append(" ");
        }
        return sb.toString();
    }
}
