package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;

public class SampleDataUtilTest {

    @Test
    public void getSampleAddressBook_validSchools_containsSamplePersons() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        assertEquals(6, persons.length);
        assertEquals(Arrays.asList(persons), SampleDataUtil.getSampleAddressBook().getPersonList());
    }
}
