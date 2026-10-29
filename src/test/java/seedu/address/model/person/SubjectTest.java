package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

public class SubjectTest {

    @Test
    public void constructor_validSample_storesNameAndBand() {
        Subject subject = new Subject("Mathematics", Band.G1);
        assertEquals("Mathematics", subject.getName());
        assertEquals(Band.G1, subject.getBand());
    }

    @Test
    public void constructor_nullBand_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Subject("Mathematics", null));
    }

    @Test
    public void hasName_sameNameIgnoringCaseAndSpaces_returnsTrue() {
        Subject mathematics = new Subject("Mathematics", Band.G1);
        assertTrue(mathematics.hasName("MATHEMATICS"));
        assertTrue(mathematics.hasName("  mathematics "));
        assertTrue(new Subject("Additional Mathematics", Band.G3).hasName("Additional  Mathematics"));
    }

    @Test
    public void hasName_turkishOrAzerbaijaniLocale_ignoresCase() {
        // these languages lowercase I to a dotless i, so PHYSICS must still match Physics there
        Locale originalLocale = Locale.getDefault();
        try {
            for (String languageTag : List.of("tr-TR", "az-AZ")) {
                Locale.setDefault(Locale.forLanguageTag(languageTag));
                assertTrue(new Subject("Physics", Band.G3).hasName("PHYSICS"), languageTag);
            }
        } finally {
            Locale.setDefault(originalLocale);
        }
    }

    @Test
    public void hasName_partialOrDifferentName_returnsFalse() {
        assertFalse(new Subject("Mathematics", Band.G1).hasName("Math"));
        assertFalse(new Subject("Additional Mathematics", Band.G3).hasName("Mathematics"));
    }

    @Test
    public void hasName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Subject("Mathematics", Band.G1).hasName(null));
    }
}
