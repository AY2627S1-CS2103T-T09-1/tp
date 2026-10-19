package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Guardian;
import seedu.address.model.person.Level;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.School;
import seedu.address.model.person.Subject;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values. "
            + "Use " + PREFIX_PHONE + " with no number to remove the phone, and "
            + PREFIX_GUARDIAN_NAME + " " + PREFIX_GUARDIAN_PHONE + " with no values to remove the guardian.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_SCHOOL + "SCHOOL] "
            + "[" + PREFIX_LEVEL + "LEVEL] "
            + "[" + PREFIX_SUBJECT + "SUBJECT " + PREFIX_BAND + "BAND]... "
            + "[" + PREFIX_GUARDIAN_NAME + "GUARDIAN_NAME] "
            + "[" + PREFIX_GUARDIAN_PHONE + "GUARDIAN_PHONE]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_SCHOOL + "Victoria School "
            + PREFIX_LEVEL + "S3";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited person: %1$s";
    public static final String MESSAGE_NO_LONGER_MATCHES = "%1$s no longer matches your last search, "
            + "but stays in the list until you enter list or find.";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This student already exists in TuteeBook3.";
    public static final String MESSAGE_GUARDIAN_REMOVAL = "Guardian name (" + PREFIX_GUARDIAN_NAME
            + ") and guardian phone (" + PREFIX_GUARDIAN_PHONE + ") cannot be empty. To remove the guardian, use "
            + PREFIX_GUARDIAN_NAME + " " + PREFIX_GUARDIAN_PHONE + " with no values.";
    public static final String MESSAGE_GUARDIAN_INCOMPLETE = "This person has no guardian yet. Give both "
            + PREFIX_GUARDIAN_NAME + " and " + PREFIX_GUARDIAN_PHONE + " to add one.";
    public static final String MESSAGE_NO_GUARDIAN_TO_REMOVE = "This person has no guardian to remove.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        // keep the edited person at the same index, so the list and view panel don't shift
        boolean isOutsideFilter = model.keepPersonInFilteredList(editedPerson);
        model.setPerson(personToEdit, editedPerson);
        String feedback = String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));
        if (isOutsideFilter) {
            feedback += "\n" + String.format(MESSAGE_NO_LONGER_MATCHES, editedPerson.getName());
        }
        CommandResult result = new CommandResult(feedback);
        // only warn about phones typed in this command, not ones the person already had
        return Messages.getNonSingaporePhoneWarning(editPersonDescriptor.getPhone(),
                        editPersonDescriptor.getGuardianPhone())
                .map(result::withWarning)
                .orElse(result);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor)
            throws CommandException {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.isPhoneRemoved()
                ? null
                : editPersonDescriptor.getPhone().orElse(personToEdit.getPhone().orElse(null));
        School updatedSchool = editPersonDescriptor.getSchool().orElse(personToEdit.getSchool());
        Level updatedLevel = editPersonDescriptor.getLevel().orElse(personToEdit.getLevel());

        List<Subject> updatedSubjects = editPersonDescriptor.getSubjects().orElse(personToEdit.getSubjects());
        Guardian updatedGuardian = createEditedGuardian(personToEdit.getGuardian(), editPersonDescriptor);
        return new Person(updatedName, updatedPhone, updatedSchool, updatedLevel,
                updatedSubjects, personToEdit.getNotes(), updatedGuardian);
    }

    /**
     * Returns the guardian after applying the guardian edits in {@code editPersonDescriptor}
     * to {@code currentGuardian}, or null if the person has no guardian afterwards.
     *
     * @throws CommandException if the person has no guardian, and the guardian is to be removed
     *     or only one of name and phone is given.
     */
    private static Guardian createEditedGuardian(Optional<Guardian> currentGuardian,
            EditPersonDescriptor editPersonDescriptor) throws CommandException {
        if (editPersonDescriptor.isGuardianRemoved()) {
            if (currentGuardian.isEmpty()) {
                throw new CommandException(MESSAGE_NO_GUARDIAN_TO_REMOVE);
            }
            return null;
        }
        Optional<Name> guardianName = editPersonDescriptor.getGuardianName();
        Optional<Phone> guardianPhone = editPersonDescriptor.getGuardianPhone();
        if (guardianName.isEmpty() && guardianPhone.isEmpty()) {
            return currentGuardian.orElse(null);
        }
        if (currentGuardian.isPresent()) {
            return new Guardian(guardianName.orElse(currentGuardian.get().getName()),
                    guardianPhone.orElse(currentGuardian.get().getPhone()));
        }
        if (guardianName.isEmpty() || guardianPhone.isEmpty()) {
            throw new CommandException(MESSAGE_GUARDIAN_INCOMPLETE);
        }
        return new Guardian(guardianName.get(), guardianPhone.get());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand otherEditCommand)) {
            return false;
        }

        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private boolean isPhoneRemoved;
        private School school;
        private Level level;
        private List<Subject> subjects;
        private Name guardianName;
        private Phone guardianPhone;
        private boolean isGuardianRemoved;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            isPhoneRemoved = toCopy.isPhoneRemoved;
            setSchool(toCopy.school);
            setLevel(toCopy.level);
            setSubjects(toCopy.subjects);
            setGuardianName(toCopy.guardianName);
            setGuardianPhone(toCopy.guardianPhone);
            isGuardianRemoved = toCopy.isGuardianRemoved;
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, school, level, subjects, guardianName, guardianPhone)
                    || isPhoneRemoved || isGuardianRemoved;
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        /**
         * Marks the phone to be removed from the person.
         */
        public void setPhoneRemoved() {
            this.phone = null;
            this.isPhoneRemoved = true;
        }

        public boolean isPhoneRemoved() {
            return isPhoneRemoved;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setSchool(School school) {
            this.school = school;
        }

        public Optional<School> getSchool() {
            return Optional.ofNullable(school);
        }

        public void setLevel(Level level) {
            this.level = level;
        }

        public Optional<Level> getLevel() {
            return Optional.ofNullable(level);
        }

        /**
         * Sets a defensive copy of the validated replacement subjects.
         */
        public void setSubjects(List<Subject> subjects) {
            if (subjects != null) {
                Person.validateSubjects(subjects);
            }
            this.subjects = subjects == null ? null : List.copyOf(subjects);
        }

        public Optional<List<Subject>> getSubjects() {
            return Optional.ofNullable(subjects);
        }

        public void setGuardianName(Name guardianName) {
            this.guardianName = guardianName;
        }

        public Optional<Name> getGuardianName() {
            return Optional.ofNullable(guardianName);
        }

        public void setGuardianPhone(Phone guardianPhone) {
            this.guardianPhone = guardianPhone;
        }

        public Optional<Phone> getGuardianPhone() {
            return Optional.ofNullable(guardianPhone);
        }

        /**
         * Marks the guardian to be removed from the person.
         */
        public void setGuardianRemoved() {
            this.guardianName = null;
            this.guardianPhone = null;
            this.isGuardianRemoved = true;
        }

        public boolean isGuardianRemoved() {
            return isGuardianRemoved;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor otherEditPersonDescriptor)) {
                return false;
            }

            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && isPhoneRemoved == otherEditPersonDescriptor.isPhoneRemoved
                    && Objects.equals(school, otherEditPersonDescriptor.school)
                    && Objects.equals(level, otherEditPersonDescriptor.level)
                    && Objects.equals(subjects, otherEditPersonDescriptor.subjects)
                    && Objects.equals(guardianName, otherEditPersonDescriptor.guardianName)
                    && Objects.equals(guardianPhone, otherEditPersonDescriptor.guardianPhone)
                    && isGuardianRemoved == otherEditPersonDescriptor.isGuardianRemoved;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("isPhoneRemoved", isPhoneRemoved)
                    .add("school", school)
                    .add("level", level)
                    .add("subjects", subjects)
                    .add("guardianName", guardianName)
                    .add("guardianPhone", guardianPhone)
                    .add("isGuardianRemoved", isGuardianRemoved)
                    .toString();
        }
    }
}
