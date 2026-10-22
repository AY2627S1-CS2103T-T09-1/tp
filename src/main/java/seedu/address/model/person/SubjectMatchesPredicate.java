package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person} takes the given subject, optionally at the given band.
 */
public class SubjectMatchesPredicate implements Predicate<Person> {

    private final String subjectName;
    private final Optional<Band> band;

    /**
     * Creates a predicate that matches students taking {@code subjectName} at any band.
     */
    public SubjectMatchesPredicate(String subjectName) {
        this(subjectName, Optional.empty());
    }

    /**
     * Creates a predicate that matches students taking {@code subjectName} at {@code band}.
     */
    public SubjectMatchesPredicate(String subjectName, Band band) {
        this(subjectName, Optional.of(band));
    }

    private SubjectMatchesPredicate(String subjectName, Optional<Band> band) {
        requireNonNull(subjectName);
        checkArgument(!subjectName.isBlank());
        this.subjectName = subjectName;
        this.band = band;
    }

    @Override
    public boolean test(Person person) {
        return person.getSubjects().stream().anyMatch(this::matches);
    }

    private boolean matches(Subject subject) {
        boolean isMatchingBand = band.map(filterBand -> filterBand == subject.getBand()).orElse(true);
        return subject.hasName(subjectName) && isMatchingBand;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SubjectMatchesPredicate otherPredicate)) {
            return false;
        }

        return subjectName.equals(otherPredicate.subjectName) && band.equals(otherPredicate.band);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("subjectName", subjectName)
                .add("band", band.map(Band::toString).orElse("any"))
                .toString();
    }
}
