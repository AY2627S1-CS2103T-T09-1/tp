package seedu.address.model.person.predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class TagMatchesPredicateTest {

    @Test
    public void equals() {
        TagMatchesPredicate firstPredicate = new TagMatchesPredicate(new Tag("paid"));
        TagMatchesPredicate secondPredicate = new TagMatchesPredicate(new Tag("unpaid"));

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        assertTrue(firstPredicate.equals(new TagMatchesPredicate(new Tag("paid"))));

        // same tag in a different case -> returns true
        assertTrue(firstPredicate.equals(new TagMatchesPredicate(new Tag("PAID"))));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different tag -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_sameTag_returnsTrue() {
        assertTrue(tagFilter("paid").test(studentTagged("paid")));
    }

    @Test
    public void test_differentCase_returnsTrue() {
        assertTrue(tagFilter("PAID").test(studentTagged("paid")));
        assertTrue(tagFilter("o-level").test(studentTagged("O-Level")));
    }

    @Test
    public void test_oneOfSeveralTags_returnsTrue() {
        assertTrue(tagFilter("exams").test(studentTagged("paid", "exams", "online")));
    }

    @Test
    public void test_partOfTag_returnsFalse() {
        assertFalse(tagFilter("paid").test(studentTagged("unpaid")));
        assertFalse(tagFilter("exam").test(studentTagged("exams")));
    }

    @Test
    public void test_noTags_returnsFalse() {
        assertFalse(tagFilter("paid").test(new PersonBuilder().build()));
    }

    private static TagMatchesPredicate tagFilter(String tagName) {
        return new TagMatchesPredicate(new Tag(tagName));
    }

    private static Person studentTagged(String... tagNames) {
        return new PersonBuilder().withTags(tagNames).build();
    }
}
