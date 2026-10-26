package seedu.address.testutil;

import java.util.List;

import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Address;
import seedu.address.model.person.Level;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.School;
import seedu.address.model.person.Subject;

/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditPersonDescriptorBuilder {

    private EditPersonDescriptor descriptor;

    public EditPersonDescriptorBuilder() {
        descriptor = new EditPersonDescriptor();
    }

    public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
        this.descriptor = new EditPersonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details
     */
    public EditPersonDescriptorBuilder(Person person) {
        descriptor = new EditPersonDescriptor();
        descriptor.setName(person.getName());
        person.getPhone().ifPresentOrElse(descriptor::setPhone, descriptor::setPhoneRemoved);
        descriptor.setSchool(person.getSchool());
        person.getAddress().ifPresent(descriptor::setAddress);
        descriptor.setLevel(person.getLevel());
        descriptor.setSubjects(person.getSubjects());
        person.getGuardian().ifPresent(guardian -> {
            descriptor.setGuardianName(guardian.getName());
            descriptor.setGuardianPhone(guardian.getPhone());
        });
    }

    /**
     * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Marks the {@code Phone} to be removed in the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withPhoneRemoved() {
        descriptor.setPhoneRemoved();
        return this;
    }

    /**
     * Sets the {@code School} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withSchool(String school) {
        descriptor.setSchool(new School(school));
        return this;
    }

    /**
     * Sets the {@code Address} of the descriptor that we are building.
     */
    public EditPersonDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Marks the address to be removed from the descriptor that we are building.
     */
    public EditPersonDescriptorBuilder withAddressRemoved() {
        descriptor.setAddressRemoved();
        return this;
    }

    /**
     * Sets the {@code Level} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withLevel(String level) {
        descriptor.setLevel(Level.fromString(level));
        return this;
    }

    /**
     * Sets the subjects in the descriptor being built.
     */
    public EditPersonDescriptorBuilder withSubjects(Subject... subjects) {
        descriptor.setSubjects(List.of(subjects));
        return this;
    }

    /**
     * Sets the guardian name of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withGuardianName(String guardianName) {
        descriptor.setGuardianName(new Name(guardianName));
        return this;
    }

    /**
     * Sets the guardian phone of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withGuardianPhone(String guardianPhone) {
        descriptor.setGuardianPhone(new Phone(guardianPhone));
        return this;
    }

    /**
     * Marks the guardian to be removed in the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withGuardianRemoved() {
        descriptor.setGuardianRemoved();
        return this;
    }

    public EditPersonDescriptor build() {
        return descriptor;
    }
}
