package seedu.address.model.person.predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Level;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class LevelMatchesPredicateTest {

    @Test
    public void equals() {
        LevelMatchesPredicate firstPredicate = new LevelMatchesPredicate(Level.S3);
        LevelMatchesPredicate secondPredicate = new LevelMatchesPredicate(Level.S4);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same level -> returns true
        assertTrue(firstPredicate.equals(new LevelMatchesPredicate(Level.S3)));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different level -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_sameLevel_returnsTrue() {
        assertTrue(new LevelMatchesPredicate(Level.S3).test(studentAt("S3")));
    }

    @Test
    public void test_differentLevel_returnsFalse() {
        assertFalse(new LevelMatchesPredicate(Level.S3).test(studentAt("S4")));
    }

    @Test
    public void test_eachLevel_matchesOnlyThatLevel() {
        for (Level filterLevel : Level.values()) {
            LevelMatchesPredicate predicate = new LevelMatchesPredicate(filterLevel);
            for (Level studentLevel : Level.values()) {
                boolean isSameLevel = filterLevel == studentLevel;
                assertEquals(isSameLevel, predicate.test(studentAt(studentLevel.name())),
                        "filter " + filterLevel + " on student at " + studentLevel);
            }
        }
    }

    @Test
    public void toStringMethod() {
        LevelMatchesPredicate predicate = new LevelMatchesPredicate(Level.S2);
        String expected = LevelMatchesPredicate.class.getCanonicalName() + "{level=S2}";
        assertEquals(expected, predicate.toString());
    }

    private static Person studentAt(String level) {
        return new PersonBuilder().withLevel(level).build();
    }
}
