---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in `docs/diagrams`. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

The following provides a quick overview of the main components and their interactions.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* provides its functionality through a concrete `{Component Name}Manager` class that implements the corresponding API interface.

For example, the `Logic` component defines its API in `Logic.java` and implements it in `LogicManager.java`. Other components interact with a component through its interface rather than its concrete class, preventing them from coupling to that component's implementation, as illustrated in the following partial class diagram.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` and its parts, such as `CommandBox`, `ResultDisplay`, `PersonListPanel`, `StudentDetailPanel`, and `StatusBarFooter`. `MainWindow` replaces the list panel with a student detail panel when a `view` command succeeds, so only one main content panel is visible at a time. All of these classes inherit from the abstract `UiPart` class, which captures common behavior among classes that represent visible GUI parts.

The `UI` component uses the JavaFX UI framework. The layouts of these UI parts are defined in matching `.fxml` files in `src/main/resources/view`. For example, [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml) specifies the layout of [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java).

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component because it displays `Person` objects from the model.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete -f 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete -f 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, it continues to the end of the diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, the command is passed to an `AddressBookParser` object, which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above for simplicity, the code can require several interactions between the command object and the `Model` to complete the operation.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name, e.g., `AddCommandParser`). The parser uses the other classes shown above to parse the user command and create an `XYZCommand` object (e.g., `AddCommand`). The `AddressBookParser` returns that object as a `Command` object.
* All `XYZCommandParser` classes, such as `AddCommandParser` and `DeleteCommandParser`, implement the `Parser` interface so they can be treated similarly where appropriate, for example during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the `Person` objects selected by the current filter, such as search results, in a separate _filtered_ list. It exposes this list as an unmodifiable `ObservableList<Person>` that the UI can observe and bind to, so the UI updates when the list changes.
* stores a `UserPrefs` object that represents the user’s preferences (currently, just the GUI settings). This is exposed to the outside as a `ReadOnlyUserPrefs` object.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

Each `Person` is one student. The classes that make up a `Person` are shown below.

<img src="images/PersonClassDiagram.png" width="550" />

The diagram does not show these rules, which the classes check when an object is created:

* A student has 1 to 8 subjects, and no two of them have the same name, ignoring case. `Person#validateSubjects` checks both, so the same subject cannot be taken at two bands.
* A note's NID is its position in the student's list of notes. A new note goes at the end, and deleting a note renumbers the notes after it.
* A `Guardian` always has both a name and a phone. A student has a complete guardian or none. See [Guardian details](#guardian-details).
* `Guardian` reuses `Name` and `Phone`, so a guardian's name and phone follow the same rules as a student's.


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* is implemented by `StorageManager`, which delegates the actual JSON file access to `JsonAddressBookStorage` and `JsonUserPrefsStorage` (one class per data file).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Student duplicate handling

The app compares a student's normalized name, school, and level as the strict duplicate key. Name comparison trims surrounding spaces, collapses repeated spaces, and ignores case. School comparison uses the same case- and space-insensitive behavior. Levels are compared by their `Level` values. The original text remains available for display. Phone, address, subject, and band do not contribute to the key. Address is a separate editable contact field and does not distinguish two students for duplicate detection.

`AddCommand` and `EditCommand` first reject an exact key collision with command-specific error text, which `MainWindow` displays in red. The errors recommend adding a distinguishing identifier to the saved name (for example, `Chloe Tan 2`) when the records are different people; otherwise, identical name, school, and level values would make the students indistinguishable in the app. If only the normalized name matches, the commands request confirmation with command-specific prompt text, displayed in amber. Each command also supplies an action-specific instruction on a separate line, followed by the shared cancellation guidance. On confirmation, they recheck the exact key before changing the model. For an edit, the record being edited is excluded from both checks; edits that leave name, school, and level unchanged do not prompt. This prevents a prompt against itself or an unrelated phone, subject, or note change, and prevents a confirmed operation from bypassing the model's invariant.

The model enforces the strict key through `UniquePersonList` for adds, replacements, and bulk resets. JSON loading uses the same check, so same-name records with different schools or levels load, while strict duplicates are rejected. `addnote` and `deletenote` replace a person without changing the key and remain valid. Delete and clear only remove records. Delete and delete-note prompts and feedback include school and level to distinguish same-name students. Sample data enters through the same model list and is subject to the same rule.

#### Design considerations

**Aspect: Which student records count as duplicates**

* **Alternative 1: Reject every same-name record.**
  * Pros: Simple rule, simple model invariant, and a same-name record cannot be added accidentally.
  * Cons: Blocks legitimate students who share a name, including siblings or students at different schools or levels. It provides no way to keep both records.

* **Alternative 2: Allow all duplicates.**
  * Pros: Never blocks a legitimate enrollment, and the data model stays flexible.
  * Cons: A repeated command can silently create indistinguishable records. Tutors can select or edit the wrong student, and the app offers no protection against accidental duplicate records.

* **Alternative 3 (chosen): Allow same-name records only when school or level differs; reject a repeated normalized name, school, and level.**
  * Pros: Supports real students who share a name while blocking the most likely accidental repeat. An amber prompt handles uncertain same-name matches, and the hard model rule prevents an exact key duplicate through add, edit, or data loading. The three fields are required for every student and together distinguish a student's current school-level record better than name alone.
  * Cons: Two different students with the same name at the same school and level cannot both be entered under identical details, because TuteeBook would display them as indistinguishable records and the tutor could select or change the wrong one. The red add/edit error recommends assigning one a distinguishing saved name, such as `Chloe Tan 2`. A student moving to another school or level can be represented by an updated record, but the app does not retain two concurrent records with the same key. Name-only matching can also prompt for unrelated students.

The required subject and band are deliberately excluded from the key. A student can take several subjects and bands, and that list changes as lessons and courses change; using it would allow the same student to be added again merely by changing a subject or band. Subject-band details describe the student's studies, not who the student is. The phone is also excluded because it is optional, can change, and may be shared or unavailable.

Every student record must include a name, school, and level.

Many tutors teach students with common names. School helps distinguish students who attend different schools. Level helps distinguish students at the same school who are in different levels. These fields block many accidental repeat entries while still allowing students with the same name when their school or level differs.

### Student addresses

`Address` is an optional value object. It accepts any nonblank value up to 200 characters and collapses repeated ordinary spaces. The add parser accepts optional `a/ADDRESS`; the edit parser replaces an address, and a blank `a/` removes it. `JsonAdaptedPerson` saves supplied addresses and accepts older JSON records without an address. The student list card shows an address when one is present; the detail panel and command result show `Not provided` when it is absent. Addresses do not contribute to duplicate detection.

### Command confirmations

`ClearCommand`, `DeleteCommand`, and `DeleteNoteCommand` implement `ConfirmationCommand`. Before running one of these commands, `LogicManager` asks it whether a prompt is needed. `ConfirmationManager` stores the command and returns a warning `CommandResult`, which the UI displays in amber. The prompt states that the action is pending, puts the action-specific confirmation instruction on a new line, and displays shared cancellation guidance on another line. The next input confirms the stored command only when trimming leading and trailing spaces produces the exact uppercase word `CONFIRM`. Every other input first clears the pending command, then follows normal command parsing and execution. A red cancellation notice is displayed above the new command's unchanged output; if parsing or execution fails, the cancellation notice and error are both red. If the new command needs confirmation, it replaces the old request. These destructive commands accept `-f` to execute without prompting.

The `delete`, `deletenote`, and `clear` prompts state the consequence directly (for example, `This will delete ...`). The `add` and `edit` prompts instead say that the record has not yet been added or saved, so it is clear that a name-match warning has not already carried out the action.

#### Design considerations

**Aspect: How the user confirms a destructive command**

* **Alternative 1:** Ask for `y` or `n`.
  * Pros: Fast to type and familiar to many command-line users.
  * Cons: A one-letter response is easy to enter accidentally. If other input is blocked while the prompt is pending, the user must enter `n` before they can run a command such as `list` to check an index. If other input runs as a command, `y` and `n` become reserved responses that cannot be used as command names during that prompt.

* **Alternative 2:** Ask for `yes` or `no`.
  * Pros: The response is more explicit than a single letter and reads naturally in a prompt.
  * Cons: Accepting both `y`/`n` and `yes`/`no` adds four confirmation spellings to remember and test. Accepting only `yes`/`no` still reserves those inputs while a prompt is pending.

* **Alternative 3 (current choice):** Ask the user to type the exact uppercase word `CONFIRM`; trim leading and trailing spaces before comparing.
  * Pros: The word makes the destructive intent explicit and is less likely to be entered accidentally. Lowercase `confirm`, `CONFIRM extra`, and `y` do not confirm. Any other input cancels the pending action and follows normal command handling, so the user can run `list` or another command immediately. A new destructive command starts its own confirmation prompt and replaces the old pending action.
  * Cons: It takes longer to type than `y`, requires uppercase, and a mistyped response cancels the pending action before being handled as a command.

The current prompt names the action and tells the user to type uppercase `CONFIRM`; it also says that any other input cancels the request and runs as a command. Prompts use the existing amber warning style. The cancellation notice uses the red error style; successful command output keeps its usual white style beneath it, and an invalid command is shown in red. The design does not accept `yes`/`no` aliases.

### Phone number validation

Students and guardians each have one phone number, stored as a `Phone`. The same rules apply to `p/` and `gp/`.

#### Implementation

1. `ParserUtil#parsePhone` calls `Phone#normalize`, which removes spaces and hyphens, then removes a leading `+65` if the rest is a Singapore number (8 digits starting with 6, 8, or 9). For example, `+65 9123-4567` becomes `91234567`, but `+65 1234 5678` becomes `+6512345678`.
1. `ParserUtil#parsePhone` then calls `Phone#getInvalidReason`, which checks the result with `Phone#isValidPhone`: an optional `+` followed by 3 to 15 digits. If the check fails, it returns a message that names the field and the value as typed, and gives the first problem it finds: the value is empty, has a character that isn't allowed, has a `+` that isn't at the start, or has the wrong number of digits. `ParserUtil` throws a `ParseException` with that message. `ParserUtil#parseGuardianPhone` does the same, with the field named `Guardian phone`.
1. After saving the student, `AddCommand` and `EditCommand` pass the phones given in the command to `Messages#getNonSingaporePhoneWarning`. If any of them is not a Singapore number, the command returns its usual result with `CommandResult#withWarning`, which adds the warning and displays the result in amber without changing what the main panel shows.

`EditCommand` only checks phones from its `EditPersonDescriptor`, so editing another detail of a student who already has an overseas number does not show the warning again.

`JsonAdaptedPerson` and `JsonAdaptedGuardian` also call `Phone#normalize` before `Phone#isValidPhone` when loading the data file, so a phone edited by hand with `+65`, spaces, or hyphens loads in the same form as a typed one. A phone that is still invalid fails with the general `Phone.MESSAGE_CONSTRAINTS`, since the user does not see this message directly.

#### Design considerations

**Aspect: Which phone numbers to accept**

* **Alternative 1:** Accept only Singapore numbers.
  * Pros: Catches typos such as a missing digit.
  * Cons: Blocks real numbers, such as a guardian with an overseas number. The course's PE guidelines use an 8-digit phone limit as an example of validation that can be too strict. Data files from v1.3, which allowed any 3 or more digits, could fail to load.

* **Alternative 2:** Accept any digits without a note.
  * Pros: Simplest to implement.
  * Cons: A typo such as `9123456` is saved without the tutor noticing.

* **Alternative 3 (current choice):** Accept an optional `+` and 3 to 15 digits, and show a warning when the number is not a Singapore number.
  * Pros: Real numbers are never blocked, typos are still pointed out, and v1.3 data files with realistic numbers still load.
  * Cons: An extra warning path in `AddCommand` and `EditCommand`.

15 is the longest phone number allowed by the international standard (E.164), including the country code. 3 is the minimum from v1.3, kept so that existing data files with realistic numbers still load. A v1.3 phone with more than 15 digits no longer loads.

**Aspect: Whether to keep `+65`**

* **Alternative 1 (current choice):** Remove `+65` only when the rest is a Singapore number.
  * Pros: `91234567` and `+65 9123 4567` are saved the same way, so the same number is never stored in two forms. A number that only looks Singaporean after removing `+65` is not changed into a different number.
  * Cons: Overseas numbers keep their country code, so phone numbers are not all stored in the same format.

* **Alternative 2:** Keep numbers exactly as typed, apart from spaces and hyphens.
  * Pros: Simpler.
  * Cons: The same Singapore number could be stored as `91234567` or `+6591234567`, so comparing or searching phone numbers would have to handle both.

### Guardian details

A student can have one guardian, whom the tutor contacts about the student. The guardian is optional, but a guardian always has both a name (`gn/`) and a phone (`gp/`).

#### Implementation

A `Guardian` holds a `Name` and a `Phone`, and its constructor rejects `null` for either. `Person` stores the guardian as a field that is `null` when the student has no guardian, and `Person#getGuardian` returns it as an `Optional<Guardian>`.

`add` creates the guardian in `ParserUtil#parseGuardian`:

1. If neither `gn/` nor `gp/` is given, it returns an empty `Optional`, and the student is added without a guardian.
1. If only one of them is given, it throws a `ParseException` with `ParserUtil.MESSAGE_GUARDIAN_PAIR`.
1. Otherwise, it parses the values with `ParserUtil#parseGuardianName` and `ParserUtil#parseGuardianPhone`. These use the same rules as `n/` and `p/`, but name the field as `Guardian name` or `Guardian phone` in the error message.

`edit` checks the guardian in two places, because some checks need the student's current guardian, which the parser cannot see:

1. `EditCommandParser#parseGuardianForEdit` handles empty values. If both `gn/` and `gp/` are given with no value, it marks the guardian for removal in the `EditPersonDescriptor`. If only one of them is empty, it throws a `ParseException` with `EditCommand.MESSAGE_GUARDIAN_REMOVAL`, which explains how to remove the guardian. Otherwise, it parses and stores the values that were given.
1. `EditCommand#createEditedGuardian` then combines the descriptor with the student's current guardian, as shown below.

<img src="images/GuardianEditActivityDiagram.png" width="750" />

So a student who has a guardian can change just the guardian's name or phone, but a student without one needs both to get one, the same as in `add`.

In the data file, `JsonAdaptedPerson` stores the guardian as a nested `JsonAdaptedGuardian`, and leaves it out when the student has no guardian. A file without a guardian, such as one from before guardians were added, loads as a student without a guardian.

`Person#isSamePerson` compares only names, so the guardian does not affect whether two students count as duplicates.

#### Design considerations

**Aspect: How to store the guardian**

* **Alternative 1:** Two optional fields on `Person`, a guardian name and a guardian phone.
  * Pros: No new class.
  * Cons: A student could end up with only half a guardian, and every command that changes the guardian would need its own check to prevent that.

* **Alternative 2 (current choice):** A `Guardian` class with both fields required, and an optional `Guardian` on `Person`.
  * Pros: A half-filled guardian cannot be created, so the "both or neither" rule is kept in one place. `Guardian` reuses `Name` and `Phone`, so their validation is not duplicated.
  * Cons: One more class, and one more nested object in the data file.

* **Alternative 3:** Guardians as separate entries that several students can share.
  * Pros: A guardian of siblings is updated once for all of them.
  * Cons: Deleting or editing a student would have to handle the guardians they share, and the data file would need references between entries. This serves a low-priority user story, so it is not worth the extra complexity for now.

**Aspect: Whether a guardian can have only a name or only a phone**

* **Alternative 1:** Allow either field on its own.
  * Pros: The tutor can record whatever they know.
  * Cons: A guardian without a phone cannot be contacted, and a phone without a name does not say who answers. The user stories for guardians are about contacting them.

* **Alternative 2 (current choice):** Require both.
  * Pros: Every saved guardian can be contacted and identified.
  * Cons: The tutor must know both before saving a guardian.

**Aspect: How to remove a guardian**

* **Alternative 1 (current choice):** `edit INDEX gn/ gp/` with both values empty.
  * Pros: Works the same way as `edit INDEX p/`, which removes the student's phone, so there is nothing new to learn.
  * Cons: An empty prefix is easy to type by mistake. To limit this, giving only one empty prefix is an error, not a removal.

* **Alternative 2:** A separate prefix or command, such as `removeguardian INDEX`.
  * Pros: Harder to trigger by mistake.
  * Cons: One more command to learn for a rare action.

### Keeping the list after an edit

`edit` keeps the displayed list or any open student record, so the tutor can work down a set of `filter` results by index.

#### Implementation

1. Before replacing the student, `EditCommand` calls `Model#keepPersonInFilteredList` with the edited student.
1. If the list is filtered, `ModelManager` first checks whether the current filter matches the edited student. It then replaces the filter with one that also accepts the edited student: `current.test(p) || p.hasSameStudentKey(editedPerson)`, and returns `true` if the old filter did not match them. If the list is not filtered, it does nothing and returns `false`.
1. `Model#setPerson` then replaces the student in place. Since the new filter accepts the edited student, they stay at the same position in the filtered list, even if they no longer match the search.
1. If `keepPersonInFilteredList` returned `true`, `EditCommand` adds a line to the result saying that the student no longer matches the last search but stays in the list. Any phone warning comes after this line.
1. `EditCommand` returns a `CommandResult` that keeps the current display, so an open `StudentDetailPanel`, which shows the student at its index in the filtered list, shows the updated details.

The filter matches the edited student by the normalized name, school, and level key rather than by name alone or `Person#equals`. This distinguishes two allowed same-name students in different schools or levels. A later command that changes only notes, such as `addnote`, still keeps the student in the list.

Each edit wraps the current filter, so after several edits in a row, every edited student stays in the list, not just the last one. The next `filter` or `list` replaces the whole filter, so the extra rules never build up across searches.

`editnote` and `deletenote` keep the list the same way. Both call `NoteCommandUtil#setPersonKeepingIndex`, which calls `keepPersonInFilteredList` before `setPerson` and adds the same line to the result. While `filter` matches only name and school, changing a note never makes a student stop matching, so this matters once the list can be filtered by note text.

#### Design considerations

**Aspect: What to show after an edit**

* **Alternative 1:** Show the full list, as AB3 does.
  * Pros: Simplest, and the user never sees a student who doesn't match the search.
  * Cons: Index numbers change, so a follow-up such as `edit 2 ...` can silently edit a different student. The tutor has to repeat `filter` after every edit.

* **Alternative 2:** Keep the filter unchanged.
  * Pros: Simple, and other students keep their index numbers.
  * Cons: A student edited so that they no longer match the search disappears. The students after them move up, and an open student record shows a different student.

* **Alternative 3 (current choice):** Keep the filter, plus the edited student.
  * Pros: Every student keeps their index number, and an open student record stays on the same student.
  * Cons: A student who no longer matches the search stays in the list until the next `filter` or `list`. The result box and the User Guide both say so, so the tutor is not left wondering why they are there.

### Filtering students

`filter` shows the students who match every filter the tutor gives, such as `filter lvl/S3 sub/Mathematics band/G2`.

#### Implementation

<img src="images/FilterClassDiagram.png" width="650" />

1. `FilterCommandParser` splits the arguments at the filter prefixes `n/`, `sch/`, `lvl/`, `sub/`, `band/`, `p/`, `gn/`, and `gp/`. It also recognizes `t/` and `nid/`, only to reject them with a message that `filter` cannot search notes, so that they are not read as part of another value.
1. It then checks, in order, that there is no text before the first prefix, that at least one filter is given, that no prefix is repeated, and that `band/` comes with `sub/`.
1. It creates one `Predicate<Person>` for each filter given:

   Prefix | Predicate | Matches when the student's…
   -------|-----------|----------------------------
   `n/` | `NameContainsKeywordsPredicate` | name contains the text
   `sch/` | `SchoolContainsTextPredicate` | school contains the text
   `lvl/` | `LevelMatchesPredicate` | level is that level
   `sub/` [`band/`] | `SubjectMatchesPredicate` | subjects include that subject, at that band if given
   `p/` | `PhoneMatchesPredicate` | own phone is that number
   `gn/` | `GuardianNameContainsTextPredicate` | guardian's name contains the text
   `gp/` | `GuardianPhoneMatchesPredicate` | guardian's phone is that number

1. It wraps the predicates in a `StudentMatchesAllPredicate`, which returns `true` only if every predicate does, and returns a `FilterCommand` holding it.
1. `FilterCommand#execute` passes the predicate to `Model#updateFilteredPersonList` and returns `CommandResult.showList(...)` with the number of students found, or a no-match message.

The partial-match predicates (`n/`, `sch/`, `gn/`) use `StringUtil#containsIgnoreCaseAndSpaces`, which lowercases both strings and squeezes runs of whitespace before comparing. The exact-match filters are parsed first with the same `ParserUtil` methods as `add`, into a `Level`, `Band`, or `Phone`. So `lvl/s3` becomes `S3` and `p/+65 9123 4567` becomes `91234567`, the form in which they are stored, and the predicates can compare values directly. `SubjectMatchesPredicate` compares names with `Subject#hasName`, the same rule `add` uses to detect a repeated subject, and requires the band to belong to that same subject.

#### Design considerations

**Aspect: How to represent the filters**

* **Alternative 1:** One predicate class with an optional field for each filter.
  * Pros: Fewer classes.
  * Cons: Every new filter changes the same constructor, `test`, and `equals`, and each matching rule can only be tested through the whole class.

* **Alternative 2 (current choice):** One predicate class per filter, combined by `StudentMatchesAllPredicate`.
  * Pros: Each matching rule is small and tested on its own. Adding a filter adds one class and one parser branch, without changing the others.
  * Cons: More classes.

**Aspect: Whether filters match part of a value or the whole value**

* **Alternative 1:** Partial matching for every field.
  * Pros: One rule for the tutor to learn.
  * Cons: Short, fixed values become ambiguous: `sub/math` would match both Mathematics and Additional Mathematics, and `p/9123` would match every number containing those digits.

* **Alternative 2 (current choice):** Partial matching for free text (name, school, guardian name), exact matching for fixed values (level, subject, band, phone).
  * Pros: Tutors can type part of a name or school they half remember, while fixed values match only what they mean.
  * Cons: Two rules to learn. The User Guide's filter table says which fields match partially.

**Aspect: How several filters combine**

* **Alternative 1:** A student matches if they match any filter (OR).
  * Pros: Finds more students.
  * Cons: Each extra filter widens the list, but tutors add filters to narrow it, such as all S3 students taking Mathematics.

* **Alternative 2 (current choice):** A student must match every filter (AND).
  * Pros: Each filter narrows the list, which fits planning a lesson for one group.
  * Cons: To see two schools, the tutor runs `filter` twice.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X), but due to a limitation of PlantUML, it continues to the end of the diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo execute:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command is correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* is a private tutor in Singapore who teaches secondary school students (levels S1 to S5)
* manages their tutoring alone, on their own computer
* teaches many students across different schools, levels, and subjects
* contacts both students and their guardians, and often needs to find a student quickly while a guardian is on the phone
* keeps notes on each student, such as weak topics and test results, to tailor lessons
* prefers desktop apps over other types of applications
* can type fast and prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: Keep every student's contact details, guardian contact, school, level, subjects, and lesson notes in one place, and reach any of it with a short typed command, faster than with a mouse-driven app or a notebook.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I can …​                         | So that I can …​                                                            |
| -------- | ------------------------------------------ | ------------------------------ |-----------------------------------------------------------------------------|
| `* * *` | new user | see a list of available commands | learn the app without reading a manual                                      |
| `* * *` | tutor taking a new student | add a student with their name, school, level, and subjects | keep all their details in one place from the start                          |
| `* * *` | tutor | record a student's own phone number | contact the student directly about lessons                                  |
| `* * *` | tutor | record a guardian's name and phone number on a student's record | know who to contact about that student                                      |
| `* * *` | tutor | see the guardian's name and number on the student's record | contact the guardian without searching separately                           |
| `* * *` | tutor | list all my students | check every student I teach without searching for each one                  |
| `* * *` | busy tutor | find a student by part of their name | pull up their record quickly while a guardian is on the phone               |
| `* * *` | busy tutor | view one student's full record on a single screen | avoid piecing it together from several places                               |
| `* * *` | tutor | edit a student's details | keep records correct when they change school, level, or phone number        |
| `* * *` | tutor | remove a student I added by mistake | keep my list accurate                                                       |
| `* * *` | busy tutor | add a short note to a student | tailor the next lesson to their weak topics                                 |
| `* * *` | tutor | delete a note about a student | keep the student's record focused on what matters now                       |
| `* * *` | tutor | have my data saved automatically | avoid losing records when I close the app                                   |
| `* * *` | tutor | exit the app | end my session when I'm done                                                |
| `* * *` | new user | clear all sample students | start with only my own students                                             |
| `* * *` | tutor | record the band each subject is taken at (G1, G2, or G3) | pitch the material at the right difficulty                                  |
| `* *` | tutor receiving unknown calls | find students by their guardian's phone number | identify the caller and see their children's records                        |
| `* *` | tutor planning lessons | filter students by school, level, or subject | prepare lessons for students at the same school, level, or subject together |
| `* *` | careless typist | confirm before a student is deleted | avoid erasing the wrong student's history after mistyping an ID             |
| `* *` | forgetful tutor | be stopped from adding a student with the same name, school, and level as an existing student | avoid duplicate records                                                     |
| `* *` | tutor | edit a note | update a student's progress without deleting and retyping it                |
| `* *` | careless typist | undo my last command | recover from a mistyped delete                                              |
| `* *` | tutor | find students whose notes contain a keyword, such as a topic | group students with the same weakness for revision                          |
| `*` | expert user | use short aliases for commands | type commands faster once I know the app                                    |
| `*` | tutor with students on break | mark a student as inactive instead of deleting them | keep their history in case they come back                                   |
| `*` | tutor teaching siblings | link siblings to the same guardian | update a shared guardian's details once for all their children              |
| `*` | tutor running small groups | create a group and add students to it | handle a group class as one unit                                            |
| `*` | expert user | recall my previous command | fix a mistake in it quickly without retyping                                |
| `*` | careless typist | confirm before a note is deleted | avoid deleting the wrong note after mistyping a note number                 |
| `*` | fast typist | launch the app from the terminal | start the app without switching to the mouse                                |

### Use cases

(For all use cases below, the **System** is the `AddressBook` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC1 - Add a student**

**MSS**

1.  Tutor requests to add a student, giving the name, school, level, at least one subject, each with its subject band (for example Chemistry at G3), and optionally a phone number and guardian details
2.  TuteeBook3 validates the details
3.  TuteeBook3 saves the student
4.  TuteeBook3 shows the student list with the new student selected

    Use case ends.

**Extensions**

* 2a. A required detail is missing or invalid.

    * 2a1. TuteeBook3 shows an error message stating which detail is wrong.

    * 2a2. Tutor enters the corrected details.

      Steps 2a1-2a2 are repeated until the details are valid.

      Use case resumes at step 3.

* 2b. Only one of guardian name and guardian phone is given.

    * 2b1. TuteeBook3 shows an error message that both must be provided together.

    * 2b2. Tutor enters the corrected details.

      Steps 2b1-2b2 are repeated until the details are valid.

      Use case resumes at step 3.

* 2c. More than 8 subjects are given, or the same subject is given more than once (even with a different subject band).

    * 2c1. TuteeBook3 shows an error message stating the problem.

    * 2c2. Tutor enters the corrected details.

      Steps 2c1-2c2 are repeated until the details are valid.

      Use case resumes at step 3.

* 2d. A subject name is invalid, or a subject has no subject band or one that is not G1, G2 or G3.

    * 2d1. TuteeBook3 shows an error message stating the expected format.

    * 2d2. Tutor enters the corrected details.

      Steps 2d1-2d2 are repeated until the details are valid.

      Use case resumes at step 3.

* 2e. The student has the same normalized name, school, and level as an existing student.

    * 2e1. TuteeBook3 shows an error message identifying the existing student and does not add the student.

      Use case ends.

* 2f. Another student has the same normalized name, but a different school or level.

    * 2f1. TuteeBook3 shows an amber prompt with the existing student's school and level.

    * 2f2. Tutor confirms the addition by entering `CONFIRM`.

      Use case resumes at step 3.

    * 2f3. Tutor enters any other input.

      The pending add is cancelled, and TuteeBook3 processes the input as a command.

* 2g. The phone number or guardian phone number is not a Singapore number.

    * 2g1. TuteeBook3 accepts the number, and shows a warning asking Tutor to check it after saving the student.

      Use case resumes at step 3.

---

**Use case: UC2 - Edit a student's details**

**MSS**

1.  Tutor requests to list students, or filters them by name (UC4)
2.  TuteeBook3 shows a list of students
3.  Tutor requests to edit a specific student by index, giving the new values for the details to change
4.  TuteeBook3 validates and updates the student
5.  TuteeBook3 shows the updated details

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. TuteeBook3 shows an error message.

      Use case resumes at step 2.

* 3b. No detail is given to change, or a new value is invalid.

    * 3b1. TuteeBook3 shows an error message.

    * 3b2. Tutor enters the corrected command.

      Steps 3b1-3b2 are repeated until the command is valid.

      Use case resumes at step 4.

* 3c. The edit would give the student the same normalized name, school, and level as another student.

    * 3c1. TuteeBook3 shows an error message and leaves the student unchanged.

      Use case ends.

* 3d. The edited name matches another student, but the school or level is different.

    * 3d1. TuteeBook3 shows an amber prompt with the matching student's school and level.

    * 3d2. Tutor confirms the edit by entering `CONFIRM`.

      Use case resumes at step 4.

    * 3d3. Tutor enters any other input.

      The pending edit is cancelled, and TuteeBook3 processes the input as a command.

* 3e. The student has no guardian, and Tutor gives only one of guardian name and guardian phone.

    * 3e1. TuteeBook3 shows an error message that both are needed to add a guardian, and leaves the student unchanged.

      Use case resumes at step 3.

* 3f. Tutor requests to remove the guardian of a student who has no guardian.

    * 3f1. TuteeBook3 shows an error message that the student has no guardian to remove, and leaves the student unchanged.

      Use case ends.

* 3g. A new phone number or guardian phone number is not a Singapore number.

    * 3g1. TuteeBook3 accepts the number, and shows a warning asking Tutor to check it after updating the student.

      Use case resumes at step 4.

* 4a. The list shows search results, and the updated student no longer matches the search.

    * 4a1. TuteeBook3 keeps the student at the same position in the list, and says that they no longer match the search.

      Use case resumes at step 5.

---

**Use case: UC3 - Delete a student**

**MSS**

1.  Tutor requests to list students
2.  TuteeBook3 shows a list of students
3.  Tutor requests to delete a specific student by index
4.  TuteeBook3 asks Tutor to confirm the deletion
5.  Tutor confirms the deletion
6.  TuteeBook3 deletes the student and all of the student's notes, and shows the remaining roster

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. TuteeBook3 shows an error message.

      Use case resumes at step 2.

* 5a. Tutor enters anything other than the exact uppercase word `CONFIRM` after trimming leading and trailing spaces.

    * 5a1. TuteeBook3 cancels the pending deletion, displays a red cancellation notice, and processes the input as a command.

      Use case resumes at the relevant step for that command.

* 6a. TuteeBook3 fails to save the change.

    * 6a1. TuteeBook3 shows an error message and no data is changed.

      Use case ends.

---

**Use case: UC4 - Find students by name**

**MSS**

1.  Tutor requests to find students, giving part of a student's name
2.  TuteeBook3 shows the list of students whose names contain that part of a name, ignoring case.

    Use case ends.

**Extensions**

* 1a. The name is missing or invalid.

    * 1a1. TuteeBook3 shows an error message.
    * 1a2. Tutor enters the corrected command.

      Steps 1a1-1a2 are repeated until the command is valid.

      Use case resumes at step 2.

* 2a. No students match the name.

  Use case ends.

---
  
## Use case: UC5 - View a student's record

**MSS**

1. Tutor requests to view a specific student by index.
2. TuteeBook3 opens the student detail view, showing all of the student's details, including all notes.

Use case ends.

**Extensions**

- 1a. The given index is invalid.
  - 1a1. TuteeBook3 shows an error message.

  Use case resumes at step 1.

- 2a. The student has no notes.
  - 2a1. TuteeBook3 shows the card with an empty notes section.

  Use case ends.

---

## Use case: UC6 - Find students by a keyword in their notes

**MSS**

1. Tutor requests to find students whose notes contain a keyword.
2. TuteeBook3 shows the matching students, with the notes that match.
3. Tutor requests to view one of them by index.
4. TuteeBook3 shows the student's full record.

Use case ends.

**Extensions**

* 1a. The keyword is blank or invalid.
  * 1a1. TuteeBook3 shows an error message.
  * 1a2. Tutor enters a corrected keyword.

  Steps 1a1-1a2 are repeated until the keyword is valid. Use case resumes at step 2.

* 2a. No student's notes contain the keyword.
  * 2a1. TuteeBook3 informs Tutor that no students match.

  Use case ends.

* 3a. The given index is invalid.
  * 3a1. TuteeBook3 shows an error message.

  Use case resumes at step 2.

---
  
**Use case: UC7 - Add a note to a student**

**MSS**

1.  Tutor requests to list students
2.  TuteeBook3 shows a list of students
3.  Tutor requests to add a note to a specific student in the list
4.  TuteeBook3 adds the note to the student's notes
5.  TuteeBook3 confirms that the note is added

    Use case ends.

**Extensions**

* 2a. The list is empty.

    Use case ends.

* 3a. The given index is invalid.

    * 3a1. TuteeBook3 shows an error message.

      Use case resumes at step 2.

* 3b. The note text is blank or too long.

    * 3b1. TuteeBook3 shows an error message.

    * 3b2. Tutor enters the corrected command.

      Steps 3b1-3b2 are repeated until the command is valid.

      Use case resumes at step 4.

---

**Use case: UC8 - Edit a note**

**MSS**

1.  Tutor views a specific student's record (UC5)
2.  TuteeBook3 shows the student's record with numbered notes
3.  Tutor requests to edit a specific note, giving the student's index, note number and new text
4.  TuteeBook3 validates the text and replaces the note's text
5.  TuteeBook3 shows the updated record and a success message with the note's old and new text

    Use case ends.

**Extensions**

* 1a. Tutor already knows the student's index and the note's number.

    Use case resumes at step 3.

* 2a. The student has no notes.

    Use case ends.

* 3a. The given index is invalid, or the given note number does not exist for that student.

    * 3a1. TuteeBook3 shows an error message.

      Use case resumes at step 3.

* 3b. The new text is blank or too long.

    * 3b1. TuteeBook3 shows an error message.

    * 3b2. Tutor enters the corrected text.

      Steps 3b1-3b2 are repeated until the text is valid.

      Use case resumes at step 4.

* 3c. The new text is the same as the note's current text.

    * 3c1. TuteeBook3 keeps the note as it is and warns that the note was not changed.

      Use case ends.

---

**Use case: UC9 - Delete a note from a student**

**MSS**

1.  Tutor requests to view a specific student by index
2.  TuteeBook3 shows the student's record with numbered notes
3.  Tutor requests to delete a specific note, giving the student's index and NID
4.  TuteeBook3 asks Tutor to confirm deleting the note.
5.  Tutor confirms, and TuteeBook3 deletes the note, renumbers the remaining notes, and shows a success message.

    Use case ends.

**Extensions**

* 1a. The given index is invalid.

    * 1a1. TuteeBook3 shows an error message.

      Use case ends.

* 2a. The student has no notes.

    Use case ends.

* 3a. The given index or NID is invalid, or the student has no such note.

    * 3a1. TuteeBook3 shows an error message.

      Use case resumes at step 2.

* 4a. Tutor cancels the deletion.

    * 4a1. TuteeBook3 keeps the note and reports that no changes were made.

      Use case ends.

---

**Use case: UC10 - View help**

**MSS**

1.  Tutor requests help
2.  TuteeBook3 shows the available commands, examples, and the user guide URL in the feedback box
3.  TuteeBook3 copies the link to the clipboard
4.  Tutor pastes the link into a browser to read the user guide
5.  Tutor reads the user guide

    Use case ends.

**Extensions**

* 1a. Help is already shown in the feedback box.

    * 1a1. TuteeBook3 reuses the feedback box and returns the help content to the top.

      Use case resumes at step 3.

---

**Use case: UC11 - Undo the last command**

**MSS**

1.  Tutor enters a command that changes data, such as deleting a student
2.  TuteeBook3 executes the command
3.  Tutor realizes the command was a mistake and requests to undo
4.  TuteeBook3 reverts the most recent data-changing command
5.  TuteeBook3 shows the restored data

    Use case ends.

**Extensions**

* 3a. There is no data-changing command to undo.

    * 3a1. TuteeBook3 informs Tutor that there is nothing to undo.

      Use case ends.

---

**Use case: UC12 - Clear all students**

**MSS**

1.  Tutor requests to list students
2.  TuteeBook3 shows the students, including the sample students
3.  Tutor requests to clear all students
4.  TuteeBook3 shows an amber warning that all students and notes will be deleted, and asks Tutor to enter `CONFIRM`; any other input cancels the request and runs as a command
5.  Tutor enters `CONFIRM`
6.  TuteeBook3 removes all students and their notes
7.  TuteeBook3 shows an empty roster

    Use case ends.

**Extensions**

* 3a. TuteeBook3 is already empty.

    * 3a1. TuteeBook3 informs Tutor that it is already empty and does not ask for confirmation.

      Use case ends.

* 3b. Tutor requests to clear all students using `clear -f`.

    * 3b1. TuteeBook3 removes all students and their notes without asking for confirmation.
    * 3b2. TuteeBook3 shows an empty roster.

      Use case ends.

* 5a. Tutor enters anything other than the exact uppercase word `CONFIRM` after trimming leading and trailing spaces.

    * 5a1. TuteeBook3 cancels the pending `clear` request, displays a red cancellation notice, and processes the input as a command.

      Use case resumes at the relevant step for that command.

### Non-Functional Requirements

1. Should run on any _mainstream OS_ with only Java `25` installed, without OS-specific libraries or features.
2. Should respond to commands within two seconds with up to 1000 student records.
3. Should prioritize typed commands for tutors who prefer typing, with GUI feedback and keyboard access to all student and note commands.
4. Should support all student and note commands offline, without a team-operated remote server.
5. Should save successful changes in local, human-editable JSON files, without a DBMS, and retain them after restarting.
6. Should reject invalid commands without crashing or changing records, and explain errors and corrections.
7. Should work well at resolutions of 1920 × 1080 or higher at 100% and 125% scaling, and remain usable at 1280 × 720 or higher at 150% scaling.
8. Should keep student and guardian data local and omit personal details from diagnostic logs.
9. Should report save failures and preserve previously saved records.
10. A first-time tutor should be able to add, find, and view a student and add a note within ten minutes using the supplied help.
11. Should serve one tutor, without shared-computer accounts or shared data files between users.
12. Should support manual data-file editing at least as well as AB3: load valid edits and report invalid files without crashing.
13. Should use a primarily object-oriented design.
14. Should use free, permissively licensed dependencies approved by the teaching team, with no user installation; libraries and frameworks must be open source.
15. Should ship without an installer as one JAR (or one ZIP if necessary), no larger than 100 MB.

### Glossary

#### General

* **AB3**: AddressBook-Level3, the sample project that TuteeBook3 is built on.
* **CLI**: Command-line interface. A way of using an app by typing commands instead of clicking.
* **DBMS**: Database management system. Software, such as MySQL, that stores data in a database. TuteeBook3 does not use one.
* **GUI**: Graphical user interface. The windows, lists, and boxes that the user sees and clicks.
* **JAR**: Java Archive. The single file that contains the whole app, which the user runs with Java.
* **JSON**: JavaScript Object Notation. The text format that TuteeBook3 uses to save data in a file that can be read and edited by hand.
* **Mainstream OS**: Windows, Linux, Unix, macOS.

#### Tutoring terms

* **Guardian**: A parent or other adult responsible for a student, whom the tutor contacts about the student's progress.
* **Level**: A student's year of secondary school, from S1 (Secondary 1) to S5 (Secondary 5).
* **Note**: A short piece of text that the tutor attaches to a student, such as "Weak in Geometry" or "WA2 Math: 36/50".
* **Student**: A secondary school student whom the tutor teaches.
* **Subject**: A school subject that the tutor teaches the student, such as "Mathematics" or "Chemistry".
* **Subject band**: The difficulty designation recorded for a subject: G1, G2, or G3. A student may have a different band for each subject, such as Mathematics at G3 and Science at G2.

#### Commands and IDs

* **Filter**: A prefix and value given to the `filter` command, such as `n/Tan`, that narrows down the student list.
* **NID**: The number shown before a note in the student detail view. Notes are numbered 1, 2, 3 in the order they were added. After a note is deleted, the notes after it move up one number, so an NID is not permanent.
* **Prefix**: A short marker ending in `/`, such as `n/` or `sch/`, that labels the value after it in a command.
* **SID**: Student ID, a unique number given to each student when they are added. It is never reused, even after the student is deleted.

#### Screen layout

* **Command box**: The text field at the top of the window where the user types commands.
* **Detail view**: The view that shows everything about one student, including all their notes. It takes the place of the student list, so only one student is shown at a time. The user can return to the student list with `list` or `filter`.
* **Result display**: The area below the command box that tells the user whether their last command worked, and why not if it failed.
* **Student card**: One student's entry in the student list.
* **Student list**: The view that lists students with their main details. It takes the place of the detail view when the user enters `list` or `filter`.

#### Code

* **AddressBook**: The class that holds every `Person` in the app.
* **Logic**: The component that turns what the user typed into a command and runs it.
* **Model**: The component that holds the app's data while it runs and applies every change to it.
* **Parser**: A class that checks what the user typed and turns it into a `Command` object, for example `AddCommandParser` for the `add` command.
* **Person**: The class that stores one student's details. The name is kept from AB3.
* **Storage**: The component that loads the data file when the app starts and saves every change back to it.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the JAR file and copy it into an empty folder.

   1. Double-click the JAR file.<br>
      Expected: The GUI opens with a set of sample contacts. The window size may not be optimal.

1. Saving window preferences

   1. Resize the window to an optimal size. Move the window to a different location. Close the window.

   1. Relaunch the app by double-clicking the JAR file.<br>
       Expected: The most recent window size and location are retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command, with multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: The first contact is deleted from the list. The status message shows the deleted contact's details.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. The status message shows error details.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Adding and editing a guardian

1. Adding a student with a guardian

   1. Test case: `add n/Tim Tan sch/Victoria School lvl/S2 sub/Mathematics band/G1 gn/Mary Tan gp/91234567`<br>
      Expected: Tim Tan is added. Viewing his student details shows guardian Mary Tan with phone number `91234567`.

   1. Test case: `add n/Ken Lim sch/Victoria School lvl/S2 sub/Mathematics band/G1 gn/Mary Lim`<br>
      Expected: No student is added. The status message shows `Guardian name (gn/) and guardian phone (gp/) must be given together.`

   1. Test case: `add n/Ken Lim sch/Victoria School lvl/S2 sub/Mathematics band/G1`<br>
      Expected: Ken Lim is added. Viewing his student details shows `Not provided` for both guardian fields.

1. Editing a guardian

   1. Prerequisites: Tim Tan and Ken Lim were added as above. Use `filter n/Tim` or `filter n/Ken` so that the student is first in the list.

   1. Test case: `edit 1 gp/98765432` on Tim Tan<br>
      Expected: Only the guardian's phone changes. Viewing Tim Tan's student details shows guardian Mary Tan with phone number `98765432`.

   1. Test case: `edit 1 gn/Peter Lim` on Ken Lim<br>
      Expected: Ken Lim is unchanged. The status message shows `This student has no guardian yet. Give both gn/ and gp/ to add one.`

   1. Test case: `edit 1 gn/ gp/91234567` on Tim Tan<br>
      Expected: Tim Tan is unchanged. The status message says that guardian name and guardian phone cannot be empty.

   1. Test case: `edit 1 gn/ gp/` on Tim Tan<br>
      Expected: The guardian is removed, and viewing the student's details shows `Not provided` for both guardian name and guardian phone.

   1. Test case: `edit 1 gn/ gp/` on Ken Lim<br>
      Expected: Ken Lim is unchanged. The status message shows `This student has no guardian to remove.`

### Entering phone numbers

1. Adding a student with a formatted Singapore number

   1. Test case: `add n/Ravi Kumar sch/Victoria School lvl/S2 sub/Mathematics band/G1 p/+65 9123-4567`<br>
      Expected: Ravi Kumar is added with phone number `91234567`. The status message shows no warning about the phone number.

1. Editing the phone number

   1. Prerequisites: Ravi Kumar was added as above. Before each test case, use `filter n/Ravi` so that Ravi Kumar is first in the list.

   1. Test case: `edit 1 p/+44 7911 123456`<br>
      Expected: The phone number is saved as `+447911123456`. The status message is shown in amber, with a warning that phone `+447911123456` doesn't look like a Singapore number.

   1. Test case: `edit 1 sch/Bedok Green Secondary School`<br>
      Expected: The school changes. The status message shows no warning about the phone number.

   1. Test case: `edit 1 p/12`<br>
      Expected: Ravi Kumar is unchanged. The status message says `Phone 12 needs 3 to 15 digits, counting the country code, but has 2.`

### Editing a student in search results

1. Editing a student so they no longer match the search

   1. Prerequisites: Start with the sample data. Enter `filter n/an`, which lists `SAMPLE Chloe Tan`, `SAMPLE Aisha Rahman`, `SAMPLE Daniel Lee`, and `SAMPLE Ethan Kumar`, then `view 1`. Run the test cases in order.

   1. Test case: `edit 1 n/Chloe Lim`<br>
      Expected: The student record stays open and shows the name Chloe Lim. The status message says `Chloe Lim no longer matches your last search, but stays in the list until you enter list or filter.`

   1. Test case: `edit 2 lvl/S4`<br>
      Expected: The student record still shows Chloe Lim, so she is still first in the list even though another student was edited. The status message says `SAMPLE Aisha Rahman` was edited, with no line about the search. Enter `list`, then `filter n/an`: the list shows Aisha Rahman (now `S4`), Daniel Lee, and Ethan Kumar, and Chloe Lim is no longer in the results.

### Saving data

1. Dealing with missing/corrupted data files

   1. _{Explain how to simulate missing or corrupted data files and state the expected behavior.}_

1. _{ more test cases …​ }_
