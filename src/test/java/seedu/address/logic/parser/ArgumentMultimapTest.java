package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;

public class ArgumentMultimapTest {

    private static final String MESSAGE_NOT_PAIRED = "Test message: prefixes are not paired.";
    private static final String MESSAGE_NOT_TOGETHER = "Test message: prefixes are not given together.";

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

    @Test
    public void verifyGivenTogether_bothGiven_success() {
        assertGivenTogether(" gn/Mary Tan gp/91234567");
    }

    @Test
    public void verifyGivenTogether_neitherGiven_success() {
        assertGivenTogether(" p/91234567");
    }

    @Test
    public void verifyGivenTogether_reverseOrder_success() {
        assertGivenTogether(" gp/91234567 gn/Mary Tan");
    }

    @Test
    public void verifyGivenTogether_otherPrefixInBetween_success() {
        assertGivenTogether(" gn/Mary Tan p/98765432 gp/91234567");
    }

    @Test
    public void verifyGivenTogether_emptyValues_success() {
        assertGivenTogether(" gn/ gp/91234567");
    }

    @Test
    public void verifyGivenTogether_onlyFirstGiven_throwsParseException() {
        assertNotGivenTogether(" gn/Mary Tan");
    }

    @Test
    public void verifyGivenTogether_onlySecondGiven_throwsParseException() {
        assertNotGivenTogether(" gp/91234567");
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

    /**
     * Asserts that {@code gn/} and {@code gp/} in {@code args} are both given or both left out.
     */
    private void assertGivenTogether(String args) {
        ArgumentMultimap argMultimap = tokenize(args);
        assertDoesNotThrow(() -> argMultimap.verifyGivenTogether(PREFIX_GUARDIAN_NAME, PREFIX_GUARDIAN_PHONE,
                MESSAGE_NOT_TOGETHER));
    }

    /**
     * Asserts that checking {@code gn/} and {@code gp/} in {@code args} throws the given message.
     */
    private void assertNotGivenTogether(String args) {
        ArgumentMultimap argMultimap = tokenize(args);
        assertThrows(ParseException.class, MESSAGE_NOT_TOGETHER, () ->
                argMultimap.verifyGivenTogether(PREFIX_GUARDIAN_NAME, PREFIX_GUARDIAN_PHONE,
                        MESSAGE_NOT_TOGETHER));
    }

    private ArgumentMultimap tokenize(String args) {
        return ArgumentTokenizer.tokenize(args, PREFIX_SUBJECT, PREFIX_BAND, PREFIX_PHONE,
                PREFIX_GUARDIAN_NAME, PREFIX_GUARDIAN_PHONE);
    }
}
