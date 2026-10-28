package seedu.address.model.person.predicate;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Level;
import seedu.address.model.person.Person;

/**
 * Tests that a {@code Person} is at the given level.
 */
public class LevelMatchesPredicate implements Predicate<Person> {

    private final Level level;

    /**
     * Creates a predicate that matches students at {@code level}.
     */
    public LevelMatchesPredicate(Level level) {
        requireNonNull(level);
        this.level = level;
    }

    @Override
    public boolean test(Person person) {
        return person.getLevel() == level;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof LevelMatchesPredicate otherPredicate)) {
            return false;
        }

        return level == otherPredicate.level;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("level", level).toString();
    }
}
