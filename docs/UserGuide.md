---
layout: page
title: User Guide
---

AddressBook Level 3 (AB3) is a **desktop application for managing contacts, optimized for use through a Command Line Interface (CLI)** while retaining the benefits of a Graphical User Interface (GUI). If you type quickly, AB3 can help you manage contacts faster than traditional GUI applications.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure that Java `25` or later is installed on your computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Open a terminal, `cd` to the folder containing the JAR file, and run `java -jar addressbook.jar`.<br>
   A GUI similar to the one below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type a command in the command box and press Enter to execute it. For example, type **`help`** and press Enter to show the available commands in the feedback box.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 sch/Victoria School lvl/S3 sub/Mathematics band/G1` : Adds a contact named `John Doe` to the Address Book.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

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

* Extraneous parameters for commands that take no parameters, such as `help`, `exit`, and `clear`, are ignored.<br>
  For example, `help 123` is interpreted as `help`.
* The `list` command does not accept additional arguments. For example, `list 3` is rejected.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help: `help`

Shows a one-line summary of each available command and the URL of the TuteeBook3 User Guide directly in the feedback box. You can also show the same help using the **Help** menu or the F1 keyboard shortcut. Repeated requests reuse the feedback box instead of opening additional panels or windows.

Format: `help`


### Adding a person: `add`

Adds a person to the address book.

Format: `add n/NAME [p/PHONE] sch/SCHOOL lvl/LEVEL sub/SUBJECT band/BAND [sub/SUBJECT band/BAND]... [gn/GUARDIAN_NAME gp/GUARDIAN_PHONE]`

* The phone number is optional, since younger students may not have their own phone.
* The guardian is optional. If you add one, give both `gn/GUARDIAN_NAME` and `gp/GUARDIAN_PHONE`. Giving only one of them shows `Guardian name (gn/) and guardian phone (gp/) must be given together.`
* `GUARDIAN_NAME` follows the same rules as `NAME`, and `GUARDIAN_PHONE` follows the same rules as `PHONE`. Error messages for these start with `Guardian name:` or `Guardian phone:`.
* Each student must have at least 1 subject and a maximum of 8 distinct subjects. Each `sub/SUBJECT` must be immediately followed by its `band/BAND`.
* `SUBJECT` accepts 1–40 characters: letters, spaces, and hyphens, with at least one letter. Surrounding spaces are trimmed; entered capitalization and internal spaces are retained for display.
* `BAND` accepts `G1`, `G2`, or `G3`, ignoring case and surrounding spaces. Bands are displayed in uppercase, for example `Mathematics (G1)`.
* A subject name cannot appear twice, even with different bands. Duplicate checks ignore case and repeated internal spaces, so `Additional Mathematics` and `additional  mathematics` are the same subject.
* `LEVEL` must be one of `S1`, `S2`, `S3`, `S4` or `S5`. It is not case-sensitive, so `s3` also works.

Examples:

* `add n/John Doe p/98765432 sch/Victoria School lvl/S3 sub/Mathematics band/G1`
* `add n/Betsy Crowe lvl/S1 sch/Bedok Green Secondary School p/81234567 sub/Mathematics band/G1`
* `add n/Tim Tan sch/Victoria School lvl/S2 sub/Mathematics band/G1` adds a student without a phone number.
* `add n/Chloe Tan sch/Bedok Green Secondary School lvl/S2 sub/Mathematics band/G1 sub/English band/G3` adds a student taking two subjects at different bands.
* `add n/Tim Tan sch/Victoria School lvl/S2 sub/Mathematics band/G1 gn/Mary Tan gp/91234567` adds a student with guardian Mary Tan, shown on the student's card as `Guardian: Mary Tan (91234567)`.

### Listing all persons: `list`

Shows a list of all persons in the address book.

Format: `list`

The command does not accept additional arguments.

### Viewing a student: `view`

Shows only the student at the specified index in the currently displayed list.

Format: `view INDEX`

* The index refers to the index number shown in the displayed student list.
* The index must be a positive integer, such as 1, 2, or 3.
* Enter `list` to return to the full student list.

Example: `view 2` shows the second student in the currently displayed list.

### Editing a person: `edit`

Edits an existing person in the address book.

Format: `edit INDEX [n/NAME] [p/PHONE] [sch/SCHOOL] [lvl/LEVEL] [sub/SUBJECT band/BAND]... [gn/GUARDIAN_NAME] [gp/GUARDIAN_PHONE]`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* Supplying subject-band pairs replaces the entire subject list; omitting them preserves existing subjects. The replacement list must contain at least 1 subject and a maximum of 8 distinct subjects, following the same pairing and validation rules as `add`. Notes are preserved when editing a student.
* You can remove a person's phone number by typing `p/` without specifying any number after it.
* If the student already has a guardian, `gn/` or `gp/` on its own changes just that part of the guardian.
* If the student has no guardian yet, give both `gn/` and `gp/` to add one. Giving only one of them shows `This person has no guardian yet. Give both gn/ and gp/ to add one.`
* You can remove the guardian by typing `gn/ gp/` with no values after either. Leaving only one of them empty, such as `gn/` alone or `gn/ gp/91234567`, is rejected so that the guardian is not removed by mistake. If the student has no guardian, `gn/ gp/` shows `This person has no guardian to remove.`

Examples:
*  `edit 1 p/91234567 sch/Victoria School` Edits the phone number and school of the 1st person to be `91234567` and `Victoria School` respectively.
*  `edit 2 n/Betsy Crower` Edits the name of the 2nd person to be `Betsy Crower`.
*  `edit 3 p/` Removes the phone number of the 3rd person.
*  `edit 1 sub/English band/G2` Replaces the first student's subjects with English at G2.
*  `edit 1 sub/Mathematics band/G2 sub/English band/G3` Replaces the first student's subject list with Mathematics at G2 and English at G3. Include every subject you want to keep, even when changing only one band.
*  `edit 1 lvl/S4` Edits the level of the 1st person to be `S4`.
*  `edit 1 gn/Mary Tan gp/91234567` Sets the guardian of the 1st person to Mary Tan, with phone number `91234567`.
*  `edit 1 gp/98765432` Changes the phone number of the 1st person's existing guardian to `98765432`.
*  `edit 1 gn/ gp/` Removes the guardian of the 1st person.

#### Subject and band errors

The following errors apply to subject-band input in `add` and `edit`. For `edit`, omitting both `sub/` and `band/` leaves subjects unchanged. An empty `sub/` does not remove subjects; every student must have at least one.

| Problem | Message |
| --- | --- |
| No `sub/` in `add`, or `band/` supplied alone in `edit` | `Missing required parameter: sub/.` |
| Missing, misplaced, or repeated `band/` within a pair | `Each sub/ must be immediately followed by one band/.` |
| Empty band or a value other than G1, G2, or G3 | `Band must be one of G1, G2, G3.` |
| Invalid subject name | `Subjects must be 1-40 characters using only letters, spaces and hyphens, with at least one letter.` |
| More than eight subject-band pairs | `A student may have at most 8 subjects.` |
| Repeated subject name | `Subject is repeated: {SUBJECT}.` |

For example, `edit 1 sub/Mathematics band/G1 sub/mathematics band/G2` fails with `Subject is repeated: mathematics.`

### Adding a note to a student: `addnote`

Adds a note to the end of a student's notes.

Format: `addnote INDEX c/TEXT`

* `INDEX` is the index number shown in the displayed student list. It must be a positive integer, such as 1, 2, or 3.
* `TEXT` must be 1 to 500 characters long, including spaces between words. Spaces before and after the text are removed and not counted.
* `TEXT` can contain any characters, including digits, punctuation, and slashes, such as `WA2 Math: 36/50` or `Rajesh s/o Kumar`.
* You can add the same note text more than once.
* New notes appear on the student's card after the existing notes.
* Notes are numbered in the order you added them, such as `1. Weak in algebra`.
* Adding a note does not change which students are shown.
* After `view`, the student you are viewing is number 1, so use `addnote 1` to add a note to them.
* If you edit the data file, each note must follow the same `TEXT` rules. Otherwise, the app starts with an empty student list.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
`TEXT` cannot contain `c/` right after a space, because the app reads it as a second `c/` parameter. For example, `addnote 1 c/Call Mrs Tan c/o school` shows an error. Write `care of` instead of `c/o`.
</div>

Examples:

* `addnote 1 c/Weak in algebra` adds the note `Weak in algebra` to the 1st student in the displayed list.
* `view 2` followed by `addnote 1 c/WA2 Math: 36/50` adds the note to the student shown by `view 2`.

If the command is wrong, `addnote` shows one of these messages and does not change any student. If `INDEX` is not a positive integer, `addnote` reports that first, even if `TEXT` is also wrong.

Problem | Message
--------|--------
`INDEX` is not a positive integer, such as `0` or `abc` | `Index must be a positive integer.`
`INDEX` is larger than the number of students shown | `The person index provided is invalid.`
`TEXT` is empty or only spaces | `Note text cannot be blank.`
`TEXT` is longer than 500 characters | `Note text must be at most 500 characters.`
`c/` appears more than once | `Multiple values specified for the following single-valued field(s): c/`
`INDEX` or `c/TEXT` is missing, or there is no space before `c/` | `Invalid command format!` followed by how to use `addnote`

### Deleting a note from a student: `deletenote`

Deletes one note from a student's notes.

Format: `deletenote INDEX note/NID`

* `INDEX` is the index number shown in the displayed student list. It must be a positive integer, such as 1, 2, or 3.
* `NID` is the number shown before the note on the student's card. It must be a positive integer, such as 1, 2, or 3.
* After you delete a note, the notes after it move up one number. For example, if a student has 3 notes and you delete note 2, the old note 3 becomes note 2. Check the student's card for the new numbers before you delete another note.
* Deleting a note does not change which students are shown.
* After `view`, the student you are viewing is number 1, so use `deletenote 1` to delete one of their notes.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
`deletenote` deletes the note as soon as you press Enter. It does not ask you to confirm, and you cannot undo it. Check `INDEX` and `NID` on the student's card before you press Enter.
</div>

Examples:

* `deletenote 1 note/2` deletes note 2 of the 1st student in the displayed list.
* `view 3` followed by `deletenote 1 note/1` deletes the first note of the student shown by `view 3`.

If the command is wrong, `deletenote` shows one of these messages and does not change any student. If `INDEX` is not a positive integer, `deletenote` reports that first, even if `NID` is also wrong.

Problem | Message
--------|--------
`INDEX` is not a positive integer, such as `0` or `abc` | `Index must be a positive integer.`
There is other text between `INDEX` and `note/`, such as `deletenote 1 abc note/2` | `Index must be a positive integer.`
`INDEX` is larger than the number of students shown | `The person index provided is invalid.`
`NID` is empty or is not a positive integer, such as `0`, `-1`, or `abc` | `NID must be a positive integer.`
The student has no notes | `{NAME} has no notes to delete.`
`NID` is larger than the number of notes the student has | `NID {NID} does not exist. {NAME} has only {COUNT} notes.` (or `1 note` if the student has one note)
`note/` appears more than once | `Multiple values specified for the following single-valued field(s): note/`
`INDEX` or `note/NID` is missing, or there is no space before `note/` | `Invalid command format!` followed by how to use `deletenote`

### Finding students by name: `find`

Shows the students whose names contain the given text.

Format: `find n/NAME`

* `NAME` can be any part of a name, even part of a word. For example, `avi` matches `David Lim`.
* The search is not case-sensitive. For example, `n/DAVID` matches `David Lim`.
* `NAME` is matched as one piece of text, including its spaces. For example, `n/david lim` matches `David Lim`, but `n/lim david` does not.
* Extra spaces in `NAME` are ignored. For example, `n/david   lim` is the same as `n/david lim`.
* Give `n/` only once.
* The search checks names only.
* If no student matches, the student list is empty and a message tells you how to see all students again.
* Enter `list` to return to the full student list.

Examples:
* `find n/tan` shows `Chloe Tan` and `Javier Tan`.
* `find n/avi` shows `David Lim`, `Javier Tan` and `Travis Ng`.<br>
  ![result for 'find n/avi'](images/findAviResult.png)

### Deleting a person: `delete`

Deletes the specified person from the address book.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

### Clearing all entries: `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program: `exit`

Exits the program.

Format: `exit`

### Saving the data

AddressBook automatically saves data after every command. You do not need to save manually.

### Editing the data file

AddressBook data is saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

Each student must have a `subjects` array containing 1–8 subject-band objects, for example:

```json
"subjects" : [
  { "name" : "Mathematics", "band" : "G1" },
  { "name" : "English", "band" : "G3" }
]
```

This is a field within a student record, not a complete data file. The same subject and band restrictions apply to stored data. Files with missing or empty `subjects` arrays cannot be loaded; older records need valid subject-band entries.

A student with a guardian also has a `guardian` object with both a `name` and a `phone`, for example `"guardian" : { "name" : "Mary Tan", "phone" : "91234567" }`. Leave out the `guardian` field for a student without a guardian.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes make the data file invalid, AddressBook starts with an empty address book at the next run. The invalid file remains on disk until you run a command (AddressBook saves after every command). Still, we recommend backing up the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another computer?<br>
**A**: Install the app on the other computer and overwrite the data file it creates with the data file from your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME [p/PHONE] sch/SCHOOL lvl/LEVEL sub/SUBJECT band/BAND [sub/SUBJECT band/BAND]... [gn/GUARDIAN_NAME gp/GUARDIAN_PHONE]` <br> e.g., `add n/James Ho p/62224444 sch/Victoria School lvl/S2 sub/Mathematics band/G1 gn/Mary Ho gp/91234567`
**Add note** | `addnote INDEX c/TEXT`<br> e.g., `addnote 1 c/Weak in algebra`
**Clear** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Delete note** | `deletenote INDEX note/NID`<br> e.g., `deletenote 1 note/2`
**Edit** | `edit INDEX [n/NAME] [p/PHONE] [sch/SCHOOL] [lvl/LEVEL] [sub/SUBJECT band/BAND]... [gn/GUARDIAN_NAME] [gp/GUARDIAN_PHONE]`<br> e.g., `edit 2 sub/Mathematics band/G2 sub/English band/G3`, `edit 1 gn/ gp/`
**Find** | `find n/NAME`<br> e.g., `find n/avi`
**List** | `list`
**Help** | `help`
