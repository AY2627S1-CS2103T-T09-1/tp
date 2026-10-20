package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class StudentMatchesAllPredicateTest {

    private static final Predicate<Person> NAME_TAN = new NameContainsKeywordsPredicate(List.of("tan"));
    private static final Predicate<Person> SCHOOL_BEDOK = new SchoolContainsTextPredicate("bedok");

    private final Person chloe = new PersonBuilder().withName("Chloe Tan")
            .withSchool("Bedok Green Secondary School").build();

    @Test
    public void test_allFiltersMatch_returnsTrue() {
        assertTrue(new StudentMatchesAllPredicate(List.of(NAME_TAN, SCHOOL_BEDOK)).test(chloe));
    }

    @Test
    public void test_oneFilterFails_returnsFalse() {
        Predicate<Person> schoolRaffles = new SchoolContainsTextPredicate("raffles");
        assertFalse(new StudentMatchesAllPredicate(List.of(NAME_TAN, schoolRaffles)).test(chloe));
    }

    @Test
    public void test_singleFilter_actsLikeThatFilter() {
        StudentMatchesAllPredicate predicate = new StudentMatchesAllPredicate(List.of(SCHOOL_BEDOK));
        assertTrue(predicate.test(chloe));
        assertFalse(predicate.test(new PersonBuilder().withSchool("Victoria School").build()));
    }

    @Test
    public void equals() {
        StudentMatchesAllPredicate firstPredicate =
                new StudentMatchesAllPredicate(List.of(NAME_TAN, SCHOOL_BEDOK));

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same filters -> returns true
        assertTrue(firstPredicate.equals(new StudentMatchesAllPredicate(List.of(NAME_TAN, SCHOOL_BEDOK))));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different filters -> returns false
        assertFalse(firstPredicate.equals(new StudentMatchesAllPredicate(List.of(NAME_TAN))));
    }
}
