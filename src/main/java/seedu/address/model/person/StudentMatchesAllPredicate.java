package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person} matches every one of the given filters.
 */
public class StudentMatchesAllPredicate implements Predicate<Person> {

    private final List<Predicate<Person>> filters;

    /**
     * Creates a predicate that matches students who pass all {@code filters}.
     *
     * @param filters Filters to combine. Must not be empty.
     */
    public StudentMatchesAllPredicate(List<Predicate<Person>> filters) {
        requireNonNull(filters);
        assert !filters.isEmpty() : "filter needs at least one filter";
        this.filters = List.copyOf(filters);
    }

    @Override
    public boolean test(Person person) {
        return filters.stream().allMatch(filter -> filter.test(person));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof StudentMatchesAllPredicate otherPredicate)) {
            return false;
        }

        return filters.equals(otherPredicate.filters);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("filters", filters).toString();
    }
}
