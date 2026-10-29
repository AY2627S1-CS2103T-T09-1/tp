package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;

public class AddressPrefixCheckerTest {

    @Test
    public void verifyNoJoinedPrefix_invalidFieldValueAfterPrefix_allowsAddress() throws ParseException {
        AddressPrefixChecker.verifyNoJoinedPrefix("Blk 1/2p/not-a-phone", PREFIX_PHONE);
    }
}
