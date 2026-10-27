---
layout: page
title: User Guide
---

TuteeBook3 is a **desktop app for private tutors who teach secondary school students (S1 to S5) in Singapore, optimized for use through a Command Line Interface (CLI)** while keeping the benefits of a Graphical User Interface (GUI). It keeps each student's information in one place, so you can look up a student's full record with a short typed command. If you type quickly, TuteeBook3 helps you manage your students faster than a spreadsheet or a traditional GUI app.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure that Java `25` or later is installed on your computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2627S1-CS2103T-T09-1/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for TuteeBook3.

1. Open a terminal, `cd` to the folder containing the JAR file, and run `java -jar TuteeBook3.jar`.<br>
   A GUI similar to the one below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Familiarize yourself with TuteeBook3 by trying its commands using the sample data. Type a command in the command box and press Enter to execute it. For example, type **`help`** and press Enter to show the available commands in the feedback box.<br>
   Some other commands you can try:

   * `list` : Lists all students.

   * `view 1` : Shows the full record of the 1st student in the list.

   * `add n/John Doe p/98765432 sch/Victoria School lvl/S3 sub/Mathematics band/G1` : Adds a student named `John Doe`, who is in S3 at Victoria School and takes Mathematics at G1.

   * `delete 3` : Deletes the 3rd student shown in the current list.

   * `clear` : Asks you to confirm before deleting all students and their notes.

   * `exit` : Exits the app.

1. When you finish trying the commands and before you enter your own student records, type `clear`, then enter `CONFIRM` to remove all sample students. Alternatively, type `clear -f` to remove them immediately without confirmation. Both methods remove every student and note currently stored.

1. Refer to the [Features](#features) section below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  For example, in `n/NAME`, replace `NAME` with a value such as `John Doe`.

* Items in square brackets are optional.<br>
  For example, `edit INDEX [n/NAME] [p/PHONE]` can be used as `edit 1 n/John Doe p/98765432` or as `edit 1 n/John Doe`.

* Parameters can be in any order, except that each `sub/SUBJECT` must be immediately followed by its `band/BAND`.<br>
  For example, `sub/Mathematics band/G1 lvl/S3` is valid, but `sub/Mathematics lvl/S3 band/G1` is not.

* `[sub/SUBJECT band/BAND]...` means that additional subject-band pairs may be supplied.

* Commands that take no parameters, such as `help`, `list`, and `exit`, do not accept additional arguments.<br>
  For example, `help 123` is rejected.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help: `help`

Shows a one-line summary of each available command and the URL of the TuteeBook3 User Guide directly in the feedback box. You can also show the same help using the **Help** menu or the F1 keyboard shortcut. Repeated requests reuse the feedback box instead of opening additional panels or windows.

Format: `help`

The command does not accept additional arguments.

### Adding a person: `add`

Adds a person to the address book.

After the student is added, the full list is shown on the left and the new student's record is shown on the right.

Format: `add n/NAME [p/PHONE] sch/SCHOOL [a/ADDRESS] [e/EMAIL] lvl/LEVEL sub/SUBJECT band/BAND [sub/SUBJECT band/BAND]... [gn/GUARDIAN_NAME gp/GUARDIAN_PHONE] [t/TAG]...`

* `NAME` can contain only letters, digits, and spaces. Symbols such as `/`, `-`, and `'` aren't supported yet, so type `Rajesh s/o Kumar` as `Rajesh so Kumar`.
* The phone number is optional, since younger students may not have their own phone.
* `ADDRESS` is optional. If provided, it can contain any nonblank value up to 200 characters. Repeated ordinary spaces are collapsed to one. The list card hides a missing address, while the detail panel shows `Not provided`.
* `EMAIL` is optional. If provided, it must be at most 254 characters, the longest address that email servers accept, and look like `local-part@domain`, such as `chloe.tan@example.com`. Spaces before and after the email are removed, but an email with a space inside it is rejected. The list card hides a missing email, while the detail panel shows `Not provided`.
  * The local part can contain English letters, digits, and the symbols `+`, `_`, `.`, and `-`. It cannot start or end with a symbol or have two symbols in a row, so `chloe..tan@example.com` and `chloe.-tan@example.com` are rejected.
  * The domain is made up of labels separated by single periods. Each label starts and ends with a letter or digit and can contain single hyphens, such as `my-school`. The last label has at least 2 characters.
* `TAG` is optional, and a student can have any number of tags, such as `t/exams t/paid`. Use tags for anything you want to remember at a glance, such as payment status or exam preparation. Each tag is one word of 1 to 30 English letters or digits, with no spaces. Single hyphens may join parts of the word, such as `t/O-level`, but other symbols are not allowed. To give more than one tag, put `t/` before each one. Tags are matched exactly apart from capitalization, so `exam-prep` and `examprep` are different tags; pick one spelling and keep to it. For longer details, use a note instead. Tags keep the capitalization you enter, but a student cannot have the same tag twice, ignoring case, so `t/Paid t/paid` is rejected. Tags are shown in the order you typed them. The list card hides tags when there are none, while the detail panel shows `Not provided`.
* When you add a student, TuteeBook3 trims leading and trailing spaces and collapses repeated ordinary spaces to one in the name, school, address, subject names, and guardian name. It keeps the capitalization you enter. For example, `add n/John  Doe sch/Victoria  School a/Blk 123 Bedok North  Avenue 2, #04-111 lvl/S3 sub/Additional  Mathematics band/G1` stores `John Doe`, `Victoria School`, `Blk 123 Bedok North Avenue 2, #04-111`, and `Additional Mathematics`.
* `PHONE` is the number you use to reach the student. It can only contain digits, with an optional `+` at the start, and must have 3 to 15 digits, counting the country code. You can type spaces or hyphens (`-`), such as `9123 4567` or `9123-4567`, and they are removed when it is saved. Other dashes, such as `–`, are not allowed.
* A Singapore number (8 digits starting with 6, 8, or 9) is saved as those 8 digits. A leading `+65` is removed, so `+65 9123 4567` is saved as `91234567`. If what follows `+65` isn't a Singapore number, the `+65` is kept.
* For an overseas number, type `+` and the country code, such as `+60 12-345 6789`. For Singapore, type `+65`, not `65`.
* If a number doesn't look like a Singapore number, TuteeBook3 still saves it but shows a warning that names the field, such as `Warning: guardian phone +60123456789 doesn't look like a Singapore number (8 digits starting with 6, 8, or 9, with or without +65). Use edit to fix it if it's wrong.`
* If a number can't be saved, the error message says why, such as `Phone 12 needs 3 to 15 digits, counting the country code, but has 2.` or `Phone 9123.4567 contains ".", which isn't allowed. Use only digits, spaces, hyphens (-), and a + at the start.`
* The guardian is optional. If you add one, give both `gn/GUARDIAN_NAME` and `gp/GUARDIAN_PHONE`. Giving only one of them shows `Guardian name (gn/) and guardian phone (gp/) must be given together.`
* `GUARDIAN_NAME` follows the same rules as `NAME`, and `GUARDIAN_PHONE` follows the same rules as `PHONE`. Error messages for these start with `Guardian name` or `Guardian phone`.

<div markdown="span" class="alert alert-info">:information_source: **Note:**
Each student and guardian has one phone number, which is the number you use to reach them. TuteeBook3 does not support a second phone number or a guardian email address. To keep one, add it as a note on that student with `addnote`, for example `addnote 1 note/Mum's office: 6123 4567`.
</div>

* Each student must have at least 1 subject and a maximum of 8 distinct subjects. Each `sub/SUBJECT` must be immediately followed by its `band/BAND`.
* `SUBJECT` accepts 1–40 characters: letters, spaces, and hyphens, with at least one letter. Surrounding spaces are trimmed and repeated ordinary spaces are collapsed to one; entered capitalization is retained for display.
* `BAND` accepts `G1`, `G2`, or `G3`, ignoring case and surrounding spaces. Bands are displayed in uppercase, for example `Mathematics (G1)`.
* A subject name cannot appear twice, even with different bands. Duplicate checks ignore case and repeated internal spaces, so `Additional Mathematics` and `additional  mathematics` are the same subject.
* `LEVEL` must be one of `S1`, `S2`, `S3`, `S4` or `S5`. It is not case-sensitive, so `s3` also works.

If the command is wrong, `add` shows one of these messages and does not add the student. If there is more than one input problem, `add` reports only the first. It checks for text before the first parameter, missing parameters, repeated parameters, and then invalid values in the order `NAME`, `PHONE`, `SCHOOL`, `ADDRESS`, `EMAIL`, `LEVEL`, subjects, guardian, and tags. After all input is valid, TuteeBook3 checks whether the student is a duplicate.

Problem | Message
--------|--------
There are no parameters at all, or there is text between `add` and the first parameter, such as `add John Doe sch/Victoria School ...` | `Invalid command format!` followed by how to use `add`
`n/`, `sch/`, `lvl/`, or `sub/` is missing, or has no space before it | Every missing one is named, such as `Missing required parameters: sch/, lvl/.`
`n/`, `p/`, `sch/`, `a/`, `e/`, `lvl/`, `gn/`, or `gp/` appears more than once | `Multiple values specified for the following single-valued field(s): p/`
`NAME` is invalid | `Names should only contain alphanumeric characters and spaces, and should not be blank`
`PHONE` or `GUARDIAN_PHONE` is invalid | The message says why, as described above
`SCHOOL` is invalid | `School names must be 1-100 characters and contain at least one letter.`
`ADDRESS` is blank or longer than 200 characters | `Addresses must not be blank and must be at most 200 characters.`
`EMAIL` is invalid or longer than 254 characters | A message starting with `Emails must be at most 254 characters, be of the format local-part@domain`, followed by the rules above
`LEVEL` is not S1 to S5 | `Level must be one of S1, S2, S3, S4, S5.`
A subject or band is wrong | See [Subject and band errors](#subject-and-band-errors)
Only one of `gn/` and `gp/` is given | `Guardian name (gn/) and guardian phone (gp/) must be given together.`
`TAG` contains a space | `Tag "exam prep" contains a space. Each tag is one word; to give more than one tag, put t/ before each, such as t/paid t/exams.`, with the tag you typed in place of `exam prep`
`TAG` is blank, too long, or contains a symbol other than a single hyphen between letters or digits | `Tags must be one word of 1-30 English letters or digits. Single hyphens may join parts of the word, such as O-level, but spaces and other symbols are not allowed. For longer details, use a note instead.`
The same tag is given twice, ignoring case | `Tag "TAG" is given more than once.`, such as `Tag "paid" is given more than once.`
A student with the same normalized name, school, and level already exists | `A student named NAME at SCHOOL, level LEVEL already exists. If this is a different student, enter a distinguishing name, for example Chloe Tan 2.`

TuteeBook3 checks for likely duplicates using the student's name. It ignores capitalization and repeated spaces when comparing names and school names; it compares levels directly. If another student has the same name but a different school or level, `add` shows an amber prompt such as `Student not added yet: NAME is also recorded at SCHOOL (LEVEL).` The next line says `Type CONFIRM (uppercase) to add anyway.` A final line says that any other input cancels the request and runs as a command. If the normalized name, school, and level all match, `add` rejects the new record. TuteeBook3 displays the red error `A student named NAME at SCHOOL, level LEVEL already exists. If this is a different student, enter a distinguishing name, for example Chloe Tan 2.` Two people with the same name, school, and level would otherwise look identical in the app, making it hard to tell whose record you are viewing or changing. If they are different people, give one a distinguishing saved name, such as `Chloe Tan 2`. The comparison does not use phone numbers, subjects, or bands, and it cannot detect every real-world duplicate.

Examples:

* `add n/John Doe p/98765432 sch/Victoria School lvl/S3 sub/Mathematics band/G1`
* `add n/Betsy Crowe lvl/S1 sch/Bedok Green Secondary School p/81234567 sub/Mathematics band/G1`
* `add n/Tim Tan sch/Victoria School lvl/S2 sub/Mathematics band/G1` adds a student without a phone number.
* `add n/Chloe Tan sch/Bedok Green Secondary School lvl/S2 sub/Mathematics band/G1 sub/English band/G3` adds a student taking two subjects at different bands.
* `add n/Mei Lin sch/Bedok Green Secondary School a/Blk 123 Bedok North Avenue 2, #04-111 lvl/S2 sub/Mathematics band/G1` adds a student with an address.
* `add n/Mei Lin sch/Bedok Green Secondary School e/meilin@example.com lvl/S2 sub/Mathematics band/G1` adds a student with an email.
* `add n/Mei Lin sch/Bedok Green Secondary School lvl/S2 sub/Mathematics band/G1 t/exams t/paid` adds a student with the tags `exams` and `paid`.
* `add n/Tim Tan sch/Victoria School lvl/S2 sub/Mathematics band/G1 gn/Mary Tan gp/91234567` adds a student with guardian Mary Tan. Use `view INDEX` to see the guardian details.
* `add n/Aisha Rahman sch/Victoria School lvl/S2 sub/Mathematics band/G1 gn/Farah Rahman gp/+60 12-345 6789` adds a student whose guardian has a Malaysian number. It is saved as `+60123456789`, with a warning that it doesn't look like a Singapore number.

### Listing all persons: `list`

Shows a list of all persons in the address book. Each entry shows the student's index, name, phone number (if provided), school, address (if provided), email (if provided), level, and tags (if any). The right panel returns to `View a student to see details.` Use `view INDEX` to see the remaining details.

Format: `list`

The command does not accept additional arguments.

### Viewing a student: `view`

TuteeBook3 always shows a fixed, equal-width student list on the left and student record panel on the right. Both panels fill the available height down to the status bar, and you cannot move the divider. Student details stay fixed at the top of the right panel. If the notes need more space than the area below, only the notes section scrolls. The list cards are not clickable. The card for the student shown on the right is highlighted. When no student is selected, the right panel says `View a student to see details.`

`view` shows the full record of the student at the specified index on the right.

The detail panel contains the student's name, school, level, subject-band pairs, address, email, tags, student phone number, guardian name, and guardian phone number, followed by the student's notes. Optional fields without a recorded value, such as the address, email, tags, student phone number, or guardian details, are shown as `Not provided`.

Format: `view INDEX`

* The index refers to the index number shown in the displayed student list.
* The index must be a positive integer, such as 1, 2, or 3.
* The current search results remain visible on the left while you view the student.
* Entering `view` again replaces the student record on the right instead of opening another panel.
* Enter `list` to return to the full student list, or `filter` to display new search results. Both commands clear the selected record and show the empty-state message on the right.
* A successful `add`, `edit`, `addnote`, `editnote`, or `deletenote` shows the affected student's record on the right. A successful `delete` or `clear` shows the empty-state message.
* Help and invalid commands leave the student record visible.

Example: `view 2` shows the full record of the second student in the currently displayed list.

### Editing a person: `edit`

Edits an existing person in the address book.

Format: `edit INDEX [n/NAME] [p/PHONE] [sch/SCHOOL] [a/ADDRESS] [e/EMAIL] [lvl/LEVEL] [sub/SUBJECT band/BAND]... [gn/GUARDIAN_NAME] [gp/GUARDIAN_PHONE] [t/TAG]...`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* `a/ADDRESS` replaces the student's address. It follows the same rules as in `add`; use `a/` with no value to remove it. Omitting `a/` keeps the current address.
* `e/EMAIL` replaces the student's email. It follows the same rules as in `add`; use `e/` with no value to remove it. Omitting `e/` keeps the current email.
* Tags given with `t/` replace all of the student's existing tags, following the same rules as in `add`. Include every tag you want to keep. Use a single `t/` with no value to remove all tags. Omitting `t/` keeps the current tags.
* Supplying subject-band pairs replaces the entire subject list; omitting them preserves existing subjects. The replacement list must contain at least 1 subject and a maximum of 8 distinct subjects, following the same pairing and validation rules as `add`. Notes are preserved when editing a student.
* `PHONE` and `GUARDIAN_PHONE` follow the same rules as in `add`, so `p/9123 4567` is saved as `91234567`. The warning about a number not looking like a Singapore number only appears when you type `p/` or `gp/` in that command.
* You can remove a person's phone number by typing `p/` without specifying any number after it.
* The displayed list stays in place, and the edited student's record opens on the right. Fields shown on the list card are updated immediately. If the edited student no longer matches your last `filter`, they stay in the list until you enter `list` or `filter` again. The result box tells you when an edit makes a student stop matching.
* If the student already has a guardian, `gn/` or `gp/` on its own changes just that part of the guardian.
* If the student has no guardian yet, give both `gn/` and `gp/` to add one. Giving only one of them shows `This student has no guardian yet. Give both gn/ and gp/ to add one.`
* You can remove the guardian by typing `gn/ gp/` with no values after either. Leaving only one of them empty, such as `gn/` alone or `gn/ gp/91234567`, is rejected so that the guardian is not removed by mistake. If the student has no guardian, `gn/ gp/` shows `This student has no guardian to remove.`
* If a change to the student's name, school, or level results in a name match with another student, `edit` shows an amber prompt such as `Edit not saved yet: NAME is also recorded at SCHOOL (LEVEL).` The next line says `Type CONFIRM (uppercase) to save this edit anyway.` A final line says that any other input cancels the request and runs as a command. The student being edited is excluded from this check, and changes to other fields do not trigger a name prompt.
* If another student has the same normalized name, school, and level as the proposed record, `edit` rejects the change and leaves the student unchanged. It shows the red error `Another student named NAME at SCHOOL, level LEVEL already exists. If these are different students, edit the name to include an identifier, for example Chloe Tan 2.` Two records with the same name, school, and level would be hard to tell apart in the app. Name and school comparisons ignore capitalization and repeated spaces; levels are compared directly. Subjects, bands, and phone numbers are not part of this duplicate check.

Examples:
*  `edit 1 p/91234567 sch/Victoria School` Edits the phone number and school of the 1st person to be `91234567` and `Victoria School` respectively.
*  `edit 1 p/+65 8123-4567` Edits the phone number of the 1st person to be `81234567`. The `+65`, spaces, and hyphens are removed.
*  `edit 1 gp/+60 12-345 6789` Edits the phone number of the 1st person's guardian to be `+60123456789`, with a warning that it doesn't look like a Singapore number.
*  `edit 2 n/Betsy Crower` Edits the name of the 2nd person to be `Betsy Crower`.
*  `edit 3 p/` Removes the phone number of the 3rd person.
*  `edit 1 sub/English band/G2` Replaces the first student's subjects with English at G2.
*  `edit 1 sub/Mathematics band/G2 sub/English band/G3` Replaces the first student's subject list with Mathematics at G2 and English at G3. Include every subject you want to keep, even when changing only one band.
*  `edit 1 lvl/S4` Edits the level of the 1st person to be `S4`.
*  `edit 1 gn/Mary Tan gp/91234567` Sets the guardian of the 1st person to Mary Tan, with phone number `91234567`.
*  `edit 1 gp/98765432` Changes the phone number of the 1st person's existing guardian to `98765432`.
*  `edit 1 gn/ gp/` Removes the guardian of the 1st person.
*  `edit 1 t/paid t/exams` Replaces the tags of the 1st person with `paid` and `exams`.
*  `edit 1 t/` Removes all tags of the 1st person.

#### Subject and band errors

The following errors apply to subject-band input in `add` and `edit`. For `edit`, omitting both `sub/` and `band/` leaves subjects unchanged. An empty `sub/` does not remove subjects; every student must have at least one.

| Problem | Message |
| --- | --- |
| No `sub/` in `add`, or `band/` supplied alone in `edit` | `Missing required parameter: sub/.` In `add`, any other missing parameters are listed too, such as `Missing required parameters: lvl/, sub/.` |
| Missing, misplaced, or repeated `band/` within a pair | `Each sub/ must be immediately followed by one band/.` |
| Empty band or a value other than G1, G2, or G3 | `Band must be one of G1, G2, G3.` |
| Invalid subject name | `Subjects must be 1-40 characters using only letters, spaces and hyphens, with at least one letter.` |
| More than eight subject-band pairs | `A student may have at most 8 subjects.` |
| Repeated subject name | `Subject is repeated: {SUBJECT}.` |

For example, `edit 1 sub/Mathematics band/G1 sub/mathematics band/G2` fails with `Subject is repeated: mathematics.`

### Adding a note to a student: `addnote`

Adds a note to the end of a student's notes.

Format: `addnote INDEX note/TEXT`

* `INDEX` is the index number shown in the student list or at the top of the student detail view. It must be a positive integer, such as 1, 2, or 3.
* `TEXT` must be 1 to 500 characters long, including spaces between words. Spaces before and after the text are removed and not counted.
* `TEXT` can contain any characters, including digits, punctuation, and slashes, such as `WA2 Math: 36/50`, `Rajesh s/o Kumar`, or `Call Mrs Tan c/o school`.
* Everything after `note/` is the note text, so `TEXT` can contain other prefixes, such as `t/f` or another `note/`. For example, `addnote 1 note/Revise t/f questions` adds the note `Revise t/f questions`.
* You can add the same note text more than once.
* New notes appear in the student detail view after the existing notes.
* Notes are numbered in the order you added them, such as `1. Weak in algebra`.
* Adding a note does not change which students are shown.
* After the note is added, the student's updated record opens on the right.
* After `view`, use the index shown at the top of the student detail view when running `addnote`.
* If you edit the data file, each note must follow the same `TEXT` rules. Otherwise, the app starts with an empty student list.

Examples:

* `addnote 1 note/Weak in algebra` adds the note `Weak in algebra` to the 1st student in the displayed list.
* `view 2` followed by `addnote 2 note/WA2 Math: 36/50` adds the note to the student shown by `view 2`.

If the command is wrong, `addnote` shows one of these messages and does not change any student. If `INDEX` is not a positive integer, `addnote` reports that first, even if `TEXT` is also wrong.

Problem | Message
--------|--------
`INDEX` is not a positive integer, such as `0` or `abc` | `Index must be a positive integer.`
`INDEX` is larger than the number of students shown | `The student index provided is invalid.`
`TEXT` is empty or only spaces | `Note text cannot be blank.`
`TEXT` is longer than 500 characters | `Note text must be at most 500 characters.`
`INDEX` or `note/TEXT` is missing, or there is no space before `note/` | `Invalid command format!` followed by how to use `addnote`

### Editing a note of a student: `editnote`

Replaces the text of one of a student's notes.

Format: `editnote INDEX nid/NID note/TEXT`

* `INDEX` is the index number shown in the student list or at the top of the student detail view. It must be a positive integer, such as 1, 2, or 3.
* `NID` is the number shown before the note in the student detail view. It must be a positive integer, such as 1, 2, or 3. To see a student's notes and their NIDs, use `view INDEX`.
* `TEXT` is the new text of the note. It follows the same rules as in `addnote`: 1 to 500 characters, with spaces before and after the text removed.
* `nid/NID` must come before `note/TEXT`. Everything after `note/` is the note text, so `TEXT` can contain anything, including `t/f`, `nid/`, or another `note/`. For example, `editnote 1 nid/2 note/Revise t/f questions` saves the note `Revise t/f questions`.
* The edited note keeps its NID, and the student's other notes do not change.
* If the new text is the same as the current text, the note stays as it is and `editnote` shows the message `No changes made: note {NID} of {NAME} already says: {TEXT}` in amber. Changing only uppercase and lowercase letters, such as `algebra` to `Algebra`, counts as a change.
* Editing a note does not change which students are shown.
* After the note is edited, the student's updated record opens on the right. If the text is unchanged, the same record opens with the warning.
* After `view`, use the index shown at the top of the student detail view when running `editnote`.

Examples:

* `editnote 1 nid/2 note/Improving in algebra` replaces note 2 of the 1st student in the displayed list with `Improving in algebra`.
* `view 3` followed by `editnote 3 nid/1 note/WA2 Math: 42/50` replaces the first note of the student shown by `view 3`.

If the command is wrong, `editnote` shows one of these messages and does not change any student. When several parts are wrong, `editnote` reports the first one in this table.

Problem | Message
--------|--------
`nid/NID` comes after `note/TEXT`, such as `editnote 1 note/hi nid/2` | `Put nid/NID before note/TEXT. Everything after note/ is read as the note text.`
`INDEX`, `nid/NID`, or `note/TEXT` is missing, or there is no space before `nid/` or `note/` | `Invalid command format!` followed by how to use `editnote`
`INDEX` is not a positive integer, such as `0` or `abc` | `Index must be a positive integer.`
There is other text between `INDEX` and the first prefix, such as `editnote 1 abc nid/2 note/hi` | `Index must be a positive integer.`
`nid/` appears more than once before `note/` | `Multiple values specified for the following single-valued field(s): nid/`
`NID` is empty or is not a positive integer, such as `0`, `-1`, or `abc` | `NID must be a positive integer.`
`TEXT` is empty or only spaces | `Note text cannot be blank.`
`TEXT` is longer than 500 characters | `Note text must be at most 500 characters.`
`INDEX` is larger than the number of students shown | `The student index provided is invalid.`
The student has no notes | `{NAME} at {SCHOOL} ({LEVEL}) has no notes to edit.`
`NID` is larger than the number of notes the student has | `NID {NID} does not exist. {NAME} at {SCHOOL} ({LEVEL}) has only {COUNT} notes.` (or `1 note` if the student has one note)

### Deleting a note from a student: `deletenote`

Deletes one note from a student's notes.

Format: `deletenote [-f] INDEX nid/NID`

* `INDEX` is the index number shown in the student list or at the top of the student detail view. It must be a positive integer, such as 1, 2, or 3.
* `NID` is the number shown before the note in the student detail view. It must be a positive integer, such as 1, 2, or 3. To see a student's notes and their NIDs, use `view INDEX`.
* The command shows an amber prompt. Its first line identifies the note and student: `This will delete note NID from NAME at SCHOOL (LEVEL).` The next line says `Type CONFIRM (uppercase) to delete this note.` A final line says that any other input cancels the request and runs as a command. You can use `deletenote -f INDEX nid/NID` to delete immediately.
* The displayed list and student record remain unchanged while confirmation is pending.
* After you delete a note, the notes after it move up one number. For example, if a student has 3 notes and you delete note 2, the old note 3 becomes note 2. Check the student detail view for the new numbers before you delete another note.
* Deleting a note does not change which students are shown.
* After the deletion is confirmed, or when you use `-f`, the student's updated record opens on the right.
* After `view`, use the index shown at the top of the student detail view when running `deletenote`.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
After you confirm with `CONFIRM`, `deletenote` deletes the note immediately. `deletenote -f` skips confirmation. You cannot undo the deletion. Check `INDEX` and `NID` in the student detail view before you confirm.
</div>

Examples:

* `deletenote 1 nid/2` deletes note 2 of the 1st student in the displayed list.
* `view 3` followed by `deletenote 3 nid/1` deletes the first note of the student shown by `view 3`.

If the command is wrong, `deletenote` shows one of these messages and does not change any student. If `INDEX` is not a positive integer, `deletenote` reports that first, even if `NID` is also wrong.

Problem | Message
--------|--------
`INDEX` is not a positive integer, such as `0` or `abc` | `Index must be a positive integer.`
There is other text between `INDEX` and `nid/`, such as `deletenote 1 abc nid/2` | `Invalid command format!` followed by how to use `deletenote`
`INDEX` is larger than the number of students shown | `The student index provided is invalid.`
`NID` is empty or is not a positive integer, such as `0`, `-1`, or `abc` | `NID must be a positive integer.`
The student has no notes | `{NAME} at {SCHOOL} ({LEVEL}) has no notes to delete.`
`NID` is larger than the number of notes the student has | `NID {NID} does not exist. {NAME} at {SCHOOL} ({LEVEL}) has only {COUNT} notes.` (or `1 note` if the student has one note)
`nid/` appears more than once | `Multiple values specified for the following single-valued field(s): nid/`
`INDEX` or `nid/NID` is missing, or there is no space before `nid/` | `Invalid command format!` followed by how to use `deletenote`

### Filtering students: `filter`

Shows the students who match every filter you give.

Format: `filter [n/NAME] [sch/SCHOOL] [lvl/LEVEL] [sub/SUBJECT [band/BAND]] [p/PHONE] [e/EMAIL] [gn/GUARDIAN_NAME] [gp/GUARDIAN_PHONE] [note/TEXT]`

* Give at least one filter, and each filter at most once.
* A student must match every filter you give. For example, `filter n/tan sch/bedok` shows students whose name contains `tan` and whose school contains `bedok`.

Filter | Matches students whose… | Match
-------|-------------------------|------
`n/NAME` | name contains `NAME` | partial matching
`sch/SCHOOL` | school contains `SCHOOL` | partial matching
`lvl/LEVEL` | level is `LEVEL`, one of `S1` to `S5` | exact matching
`sub/SUBJECT` | subjects include `SUBJECT` | exact matching
`sub/SUBJECT band/BAND` | subjects include `SUBJECT` at `BAND` (`G1` to `G3`) | exact matching
`p/PHONE` | own phone number is `PHONE` | exact matching
`e/EMAIL` | email contains `EMAIL` | partial matching
`gn/GUARDIAN_NAME` | guardian's name contains `GUARDIAN_NAME` | partial matching
`gp/GUARDIAN_PHONE` | guardian's phone number is `GUARDIAN_PHONE` | exact matching
`note/TEXT` | notes include one that contains `TEXT` | partial matching

* A filter with partial matching can match any part of the value, even part of a word. For example, `sch/vic` matches `Victoria School`.
* A filter with exact matching must match the whole value. For example, `lvl/S3` matches only students in S3, `sub/math` does not match `Mathematics`, and `p/9123` does not match `91234567`.
* `band/` must come right after `sub/`, and the band must belong to that subject. For example, a student taking Mathematics at G1 and Science at G2 does not match `sub/Mathematics band/G2`.
* Phone numbers are compared the way they are saved: spaces, hyphens, and a leading `+65` are removed. For example, `p/9123 4567` and `p/+65 9123-4567` both match `91234567`. An overseas number must include its `+` and country code.
* A student without a phone never matches `p/`, a student without an email never matches `e/`, and a student without a guardian never matches `gn/` or `gp/`.
* `note/` searches all of a student's notes. `TEXT` can be up to 500 characters, the same as a note.
* Everything after `note/` is the note text, so put `note/` last. For example, `filter note/revise t/f` finds notes containing `revise t/f`. In `filter note/algebra lvl/S3`, `lvl/S3` is read as part of the note text, so write `filter lvl/S3 note/algebra` instead.
* Filters are not case-sensitive, and extra spaces are ignored.
* Each value is matched as one piece of text, including its spaces: `n/david lim` matches `David Lim`, but `n/lim david` does not.
* If no student matches, the student list is empty and a message tells you how to see all students again.
* Running `filter` clears the selected student record and shows `View a student to see details.` on the right.
* Enter `list` to return to the full student list.

Examples:
* `filter n/c` returns `SAMPLE Chloe Tan` and `SAMPLE Marcus Lim` from the sample student list.<br>
  ![Results for filter c](images/findChloeMarcusResult.png)
* `filter sch/victoria` returns `SAMPLE Daniel Lee`.
* `filter n/lim sch/secondary` returns `SAMPLE Marcus Lim`.
* `filter lvl/s2` returns `SAMPLE Chloe Tan` and `SAMPLE Ethan Kumar`.
* `filter sch/bedok lvl/S2` returns `SAMPLE Chloe Tan`.
* `filter sub/mathematics band/G2` returns `SAMPLE Marcus Lim` and `SAMPLE Daniel Lee`.
* `filter sub/chemistry` returns `SAMPLE Aisha Rahman` and `SAMPLE Natalie Goh`.
* `filter p/9123 4567` returns `SAMPLE Chloe Tan`.
* `filter e/example.com` returns `SAMPLE Chloe Tan`, `SAMPLE Marcus Lim`, and `SAMPLE Natalie Goh`.
* `filter gp/98765432` returns `SAMPLE Marcus Lim`, whose guardian is Grace Lim.
* `filter gn/goh` returns `SAMPLE Natalie Goh`, whose guardian is Michelle Goh.
* `filter note/algebra` returns `SAMPLE Chloe Tan`.
* `filter lvl/S1 note/next lesson` returns `SAMPLE Daniel Lee`.

Problem | Message
--------|--------
No filter is given | `Give at least one filter.` followed by how to use `filter`
There is text before the first filter | `Invalid command format!` followed by how to use `filter`
A filter is empty, such as `sch/` | `The school to search for cannot be blank.` (or `name` or `email`)
A filter appears more than once | `Multiple values specified for the following single-valued field(s): sch/`
`LEVEL` is not one of `S1` to `S5`, or is empty | `Level must be one of S1, S2, S3, S4, S5.`
`SUBJECT` is empty or invalid | `Subjects must be 1-40 characters using only letters, spaces and hyphens, with at least one letter.`
`BAND` is not one of `G1` to `G3`, or is empty | `Band must be one of G1, G2, G3.`
`band/` is not right after `sub/`, or there is no `sub/` | `band/ must come right after sub/, such as: filter sub/Mathematics band/G2`
`PHONE` or `GUARDIAN_PHONE` is empty or not a valid phone number | The same message as `add`, such as `Phone cannot be empty.`
`GUARDIAN_NAME` is empty | `The guardian name to search for cannot be blank.`
`TEXT` after `note/` is empty | `The note text to search for cannot be blank.`
`TEXT` after `note/` is longer than 500 characters | `The note text to search for must be at most 500 characters.`
`nid/` is given | `To filter by notes, use note/, such as: filter note/algebra`

### Deleting a student: `delete`

Deletes a student, together with their guardian details and all of their notes.

Format: `delete [-f] INDEX`

* `INDEX` is the index number shown in the student list or at the top of the student detail view. It must be a positive integer, such as 1, 2, or 3.
* `delete` shows an amber prompt. Its first line identifies the student and says what will happen: `This will delete NAME at SCHOOL (LEVEL) and all their notes.` The next line says `Type CONFIRM (uppercase) to delete this student and all their notes.` A final line says that any other input cancels the request and runs as a command. Use `delete -f INDEX` to delete immediately.
* The displayed list and student record remain unchanged while confirmation is pending.
* After `view`, use the index shown at the top of the student detail view when running `delete`.
* After a student is deleted, the current filter remains active while it still has matches. The index numbers of the remaining matching students may change, so check the list before you run another command that uses an index.
* If you delete the filter's last matching student while other students remain, TuteeBook3 shows the full list and adds `The filtered list is now empty, so all remaining students are shown.` to the result.
* After deletion, the right panel returns to `View a student to see details.`

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
After you confirm with `CONFIRM`, `delete` removes the student and all their notes immediately. `delete -f` skips confirmation. You cannot undo the deletion. Check `INDEX` in the student list before you confirm.
</div>

Examples:

* `list` followed by `delete 2` deletes the 2nd student in the full student list.
* `filter n/Tan` followed by `delete 1` deletes the 1st student in the search results.

If the command is wrong, `delete` shows one of these messages and does not delete any student.

Problem | Message
--------|--------
`INDEX` is missing or is not a positive integer, such as `0`, `-1`, or `abc` | `Invalid format. Usage: delete [-f] INDEX`
There is more than one value, such as `delete 1 2` | `Invalid format. Usage: delete [-f] INDEX`
`INDEX` is larger than the number of students shown | `The student index provided is invalid.`

### Clearing all entries: `clear`

Clears all students and their notes after you confirm the command, or immediately when you use `-f`.

Format: `clear [-f]`

The command accepts only the optional `-f` argument.

* `clear` shows the amber prompt `This will delete all students and notes.` The next line says `Type CONFIRM (uppercase) to clear all students and notes.` A final line says that any other input cancels the request and runs as a command. Your data remains unchanged until you confirm.
* `clear -f` clears all students and notes immediately without asking for confirmation.
* The displayed list and student record remain unchanged while confirmation is pending. After clearing, the empty list remains on the left and `View a student to see details.` appears on the right.
* While a confirmation is pending, only the exact uppercase word `CONFIRM` (with optional leading or trailing spaces) confirms it. Any other input cancels the pending command and is processed normally. A cancellation notice appears in red above the command result; command output keeps its usual color, and command errors appear in red.
* If TuteeBook3 is already empty, `clear` shows `TuteeBook3 is already empty.` without asking you to confirm.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
After you confirm, or use `clear -f`, `clear` deletes every student and note immediately. You cannot undo it.
</div>

### Exiting the program: `exit`

Exits the program.

Format: `exit`

The command does not accept additional arguments.

### Saving the data

TuteeBook3 automatically saves its current data after every successfully executed command. You do not need to save manually.

### Editing the data file

TuteeBook3 data is saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

Each student must have a `subjects` array containing 1–8 subject-band objects, for example:

```json
"subjects" : [
  { "name" : "Mathematics", "band" : "G1" },
  { "name" : "English", "band" : "G3" }
]
```

This is a field within a student record, not a complete data file. The same subject and band restrictions apply to stored data. Files with missing or empty `subjects` arrays cannot be loaded; older records need valid subject-band entries.

A student with a guardian also has a `guardian` object with both a `name` and a `phone`, for example `"guardian" : { "name" : "Mary Tan", "phone" : "91234567" }`. Leave out the `guardian` field for a student without a guardian.

A student's tags are stored in a `tags` array, for example `"tags" : [ "exams", "paid" ]`. Tags in the data file follow the same rules as `TAG`, and a student cannot have the same tag twice. Leave out the `tags` field, or use an empty array, for a student without tags.

Phone numbers in the data file, for both students and guardians, follow the same rules as `PHONE`. They are cleaned up the same way when the app loads the file, so `+65 9123-4567` is loaded as `91234567`. The cleaned number is written back to the file the next time TuteeBook3 saves.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes make the data file invalid, TuteeBook3 starts with an empty book at the next run. The invalid file remains on disk until you successfully run a command that reaches the save step. Still, we recommend backing up the file before editing it.<br>
Furthermore, certain edits can cause TuteeBook3 to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## Glossary

#### Tutoring terms

* **Band**: The standard at which a student takes a subject: G1, G2, or G3. These come from Singapore's Full Subject-Based Banding, where G3 is the most demanding. Each subject has its own band, so a student may take Mathematics at G3 and Science at G2.
* **Guardian**: A parent or other adult responsible for a student, whom the tutor contacts about the student's progress. A student has at most one guardian, recorded with both a name and a phone number.
* **Level**: A student's year of secondary school, from S1 (Secondary 1) to S5 (Secondary 5).
* **Note**: A short piece of text that the tutor attaches to a student, such as "Weak in Geometry" or "WA2 Math: 36/50".
* **Student**: A secondary school student whom the tutor teaches.
* **Subject**: A school subject that the tutor teaches the student, such as "Mathematics" or "Chemistry".
* **Tag**: A short label that the tutor attaches to a student to group or flag them at a glance, such as "paid" or "O-level". A tag is one word, which may contain hyphens. A student can have any number of tags.

#### Commands

* **Case-sensitive**: Treating uppercase and lowercase letters as different. For example, `CONFIRM` is case-sensitive, so `confirm` does not confirm a command. Filters are not case-sensitive, so `n/tan` matches `Tan`.
* **Command**: An instruction that you type in the command box and press Enter to run, such as `list` or `view 1`. It starts with a word that says what to do, such as `add` or `filter`, followed by any parameters.
* **Confirmation prompt**: An amber message that asks you to confirm a command before it runs. `delete`, `deletenote`, and `clear` show one before they delete anything, and `add` and `edit` show one when another student already has the same name. Type `CONFIRM` in uppercase to go ahead. Any other input, including `confirm`, cancels the command and runs as a normal command.
* **Exact matching**: The way the `lvl/`, `sub/`, `band/`, `p/`, and `gp/` filters of `filter` compare values. The whole value must match, ignoring case and extra spaces. For example, `lvl/S3` matches only students in S3, and `sub/math` does not match `Mathematics`.
* **Filter**: A prefix and value given to the `filter` command, such as `n/Tan`, that narrows down the student list.
* **Index**: The number shown next to a student in the list currently on screen. Commands such as `view`, `edit`, and `delete` use it to pick a student. It is not permanent: it can change when the list changes, such as after `add`, `delete`, `filter`, or `list`.
* **NID**: The number shown before a note in the student detail view. Notes are numbered 1, 2, 3 in the order they were added. After a note is deleted, the notes after it move up one number, so an NID is not permanent.
* **Normalized**: Compared after removing spaces at the start and end, treating repeated spaces as one, and ignoring capitalization. For example, `John  Doe` and `john doe` have the same normalized name.
* **Parameter**: A value that you supply in a command, shown in `UPPER_CASE` in this guide, such as `NAME` in `n/NAME`.
* **Partial matching**: The way the `n/`, `sch/`, `gn/`, and `note/` filters of `filter` compare values. The value only needs to contain what you typed, even part of a word, ignoring case. For example, `sch/vic` matches `Victoria School`.
* **Prefix**: A short marker ending in `/`, such as `n/` or `sch/`, that labels the value after it in a command.
* **Warning**: An amber message that flags something that might be a mistake, without stopping the command. For example, TuteeBook3 saves a phone number that doesn't look like a Singapore number, but shows a warning so that you can check it.

#### Screen layout

* **Command box**: The text field at the top of the window where you type commands.
* **Detail view**: The panel on the right of the window that shows everything about one student, including all their notes. It is also called the "detail panel" or "student detail view". `view` shows a student there, and `list` and `filter` clear it.
* **Empty-state message**: The message `View a student to see details.`, shown in the detail view when no student is selected.
* **Feedback box**: See **Result display**.
* **Result box**: See **Result display**.
* **Result display**: The area below the command box that tells you whether your last command worked, and why not if it failed. It is also called the "feedback box" or "result box".
* **Student list**: The panel on the left of the window that lists students with their main details. It shows all students, or the results of your last `filter`.

#### General

* **Alphanumeric**: Made up of only the letters A to Z and the digits 0 to 9. For example, `Rajesh so Kumar` contains only alphanumeric characters and spaces, but `Rajesh s/o Kumar` does not, because `/` is not alphanumeric.
* **CLI**: Command-line interface. A way of using an app by typing commands instead of clicking.
* **Data file**: The file `data/addressbook.json` in the home folder, where TuteeBook3 automatically saves all your students and notes. To change it by hand, see [Editing the data file](#editing-the-data-file).
* **GUI**: Graphical user interface. The windows, lists, and boxes that you see and click.
* **Home folder**: The folder that holds the TuteeBook3 JAR file. Run TuteeBook3 from this folder, as described in [Quick start](#quick-start), so that it saves your data there.
* **JAR**: Java Archive. The single file that contains the whole app, which you run with Java.
* **JSON**: JavaScript Object Notation. The text format that TuteeBook3 uses to save data in a file that can be read and edited by hand.
* **Terminal**: An app where you type commands for your computer to run, such as **Terminal** on macOS or **Command Prompt** on Windows. You use it to start TuteeBook3, as described in [Quick start](#quick-start).

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another computer?<br>
**A**: Install the app on the other computer and overwrite the data file it creates with the data file from your previous TuteeBook3 home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME [p/PHONE] sch/SCHOOL [a/ADDRESS] [e/EMAIL] lvl/LEVEL sub/SUBJECT band/BAND [sub/SUBJECT band/BAND]... [gn/GUARDIAN_NAME gp/GUARDIAN_PHONE] [t/TAG]...` <br> e.g., `add n/James Ho p/62224444 sch/Victoria School a/Blk 123 Bedok North Avenue 2, #04-111 lvl/S2 sub/Mathematics band/G1 gn/Mary Ho gp/91234567 t/paid`
**Add note** | `addnote INDEX note/TEXT`<br> e.g., `addnote 1 note/Weak in algebra`
**Clear** | `clear`, followed by `CONFIRM` to confirm (Alternatively, `clear -f` to clear immediately)
**Delete** | `delete [-f] INDEX`<br> e.g., `delete 3`
**Delete note** | `deletenote [-f] INDEX nid/NID`<br> e.g., `deletenote 1 nid/2`
**Edit** | `edit INDEX [n/NAME] [p/PHONE] [sch/SCHOOL] [a/ADDRESS] [e/EMAIL] [lvl/LEVEL] [sub/SUBJECT band/BAND]... [gn/GUARDIAN_NAME] [gp/GUARDIAN_PHONE] [t/TAG]...`<br> e.g., `edit 2 a/Blk 311 Tampines Street 33, #05-212`, `edit 1 e/`, `edit 2 sub/Mathematics band/G2 sub/English band/G3`, `edit 1 gn/ gp/`, `edit 1 t/exams`, `edit 1 t/`
**Edit note** | `editnote INDEX nid/NID note/TEXT`<br> e.g., `editnote 1 nid/2 note/Improving in algebra`
**Filter** | `filter [n/NAME] [sch/SCHOOL] [lvl/LEVEL] [sub/SUBJECT [band/BAND]] [p/PHONE] [e/EMAIL] [gn/GUARDIAN_NAME] [gp/GUARDIAN_PHONE] [note/TEXT]`<br> e.g., `filter lvl/S5 sub/mathematics band/G2`
**List** | `list`
**Help** | `help`
