package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_SCHOOL = new Prefix("sch/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_SUBJECT = new Prefix("sub/");
    public static final Prefix PREFIX_BAND = new Prefix("band/");
    public static final Prefix PREFIX_LEVEL = new Prefix("lvl/");
    public static final Prefix PREFIX_NOTE = new Prefix("note/");
    public static final Prefix PREFIX_NID = new Prefix("nid/");
    public static final Prefix PREFIX_GUARDIAN_NAME = new Prefix("gn/");
    public static final Prefix PREFIX_GUARDIAN_PHONE = new Prefix("gp/");

    private CliSyntax() {}

}
