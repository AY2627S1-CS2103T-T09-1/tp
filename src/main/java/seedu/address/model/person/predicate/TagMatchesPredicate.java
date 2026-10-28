package seedu.address.model.person.predicate;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person} has the given tag, ignoring case.
 */
public class TagMatchesPredicate implements Predicate<Person> {

    private final Tag tag;

    /**
     * Creates a predicate that matches students with the tag {@code tag}.
     */
    public TagMatchesPredicate(Tag tag) {
        requireNonNull(tag);
        this.tag = tag;
    }

    @Override
    public boolean test(Person person) {
        // Tag#equals ignores case, so t/PAID matches a student tagged paid
        return person.getTags().contains(tag);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagMatchesPredicate otherPredicate)) {
            return false;
        }

        return tag.equals(otherPredicate.tag);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("tag", tag).toString();
    }
}
