package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;

public class ArgumentMultimapTest {

    private static final String MESSAGE_NOT_PAIRED = "Test message: prefixes are not paired.";

    @Test
    public void verifyEachFollowedBy_noPrefixes_success() {
        assertPaired("");
    }

    @Test
    public void verifyEachFollowedBy_onePair_success() {
        assertPaired(" sub/English band/G1");
    }

    @Test
    public void verifyEachFollowedBy_manyPairs_success() {
        assertPaired(" sub/English band/G1 sub/Math band/G2");
    }

    @Test
    public void verifyEachFollowedBy_otherPrefixesAroundPairs_success() {
        assertPaired(" p/91234567 sub/English band/G1 p/98765432");
    }

    @Test
    public void verifyEachFollowedBy_firstAtEnd_throwsParseException() {
        assertNotPaired(" sub/English");
    }

    @Test
    public void verifyEachFollowedBy_secondBeforeFirst_throwsParseException() {
        assertNotPaired(" band/G1 sub/English band/G2");
    }

    @Test
    public void verifyEachFollowedBy_repeatedFirst_throwsParseException() {
        assertNotPaired(" sub/English sub/Science band/G1");
    }

    @Test
    public void verifyEachFollowedBy_repeatedSecond_throwsParseException() {
        assertNotPaired(" sub/English band/G1 band/G2");
    }

    @Test
    public void verifyEachFollowedBy_otherPrefixInBetween_throwsParseException() {
        assertNotPaired(" sub/English p/91234567 band/G1");
    }

    @Test
    public void verifyEachFollowedBy_missingSpace_throwsParseException() {
        assertNotPaired(" sub/Englishband/G1");
    }

    /**
     * Asserts that every {@code sub/} in {@code args} is immediately followed by one {@code band/}.
     */
    private void assertPaired(String args) {
        ArgumentMultimap argMultimap = tokenize(args);
        assertDoesNotThrow(() -> argMultimap.verifyEachFollowedBy(PREFIX_SUBJECT, PREFIX_BAND,
                MESSAGE_NOT_PAIRED));
    }

    /**
     * Asserts that checking the {@code sub/} and {@code band/} pairs in {@code args} throws the given message.
     */
    private void assertNotPaired(String args) {
        ArgumentMultimap argMultimap = tokenize(args);
        assertThrows(ParseException.class, MESSAGE_NOT_PAIRED, () ->
                argMultimap.verifyEachFollowedBy(PREFIX_SUBJECT, PREFIX_BAND, MESSAGE_NOT_PAIRED));
    }

    private ArgumentMultimap tokenize(String args) {
        return ArgumentTokenizer.tokenize(args, PREFIX_SUBJECT, PREFIX_BAND, PREFIX_PHONE);
    }
}
