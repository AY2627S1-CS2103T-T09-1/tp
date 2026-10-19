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

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

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
| `* *` | forgetful tutor | be stopped by the app from adding students I already added who have the exact same name | avoid duplicate records                                                     |
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

* 2e. The student is a duplicate of an existing student.

    * 2e1. TuteeBook3 shows an error message identifying the existing student and does not add the student.

      Use case ends.

* 2f. The phone number or guardian phone number is not a Singapore number.

    * 2f1. TuteeBook3 accepts the number, and shows a warning asking Tutor to check it after saving the student.

      Use case resumes at step 3.

---

**Use case: UC2 - Edit a student's details**

**MSS**

1.  Tutor requests to list students
2.  TuteeBook3 shows a list of students
3.  Tutor requests to edit a specific student by index, giving the new values for the details to change
4.  TuteeBook3 validates and updates the student
5.  TuteeBook3 shows the updated student record

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

* 3c. The edit would make the student a duplicate of another student.

    * 3c1. TuteeBook3 shows an error message and leaves the student unchanged.

      Use case ends.

* 3d. The student has no guardian, and Tutor gives only one of guardian name and guardian phone.

    * 3d1. TuteeBook3 shows an error message that both are needed to add a guardian, and leaves the student unchanged.

      Use case resumes at step 3.

* 3e. Tutor requests to remove the guardian of a student who has no guardian.

    * 3e1. TuteeBook3 shows an error message that the student has no guardian to remove, and leaves the student unchanged.

      Use case ends.

* 3f. A new phone number or guardian phone number is not a Singapore number.

    * 3f1. TuteeBook3 accepts the number, and shows a warning asking Tutor to check it after updating the student.

      Use case resumes at step 4.

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

* 5a. Tutor cancels the deletion.

    * 5a1. TuteeBook3 discards the pending deletion request and keeps the student.

      Use case ends.

* 5b. Tutor enters any other command instead of confirming or cancelling.

    * 5b1. TuteeBook3 discards the pending deletion request, then processes the new command as normal.

      Use case ends.

* 5c. Tutor's confirmation does not match the pending request.

    * 5c1. TuteeBook3 discards the pending request and shows an error message that there is no matching deletion request.

      Use case resumes at step 3.

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

1.  Tutor requests to view a specific student by index
2.  TuteeBook3 shows the student's record with numbered notes
3.  Tutor requests to edit a specific note, giving the student's index, note number and new text
4.  TuteeBook3 validates the text and replaces the note's text
5.  TuteeBook3 shows the updated record

    Use case ends.

**Extensions**

* 1a. The given index is invalid.

    * 1a1. TuteeBook3 shows an error message.

      Use case ends.

* 2a. The student has no notes.

    Use case ends.

* 3a. The given note number does not exist for that student.

    * 3a1. TuteeBook3 shows an error message.

      Use case resumes at step 2.

* 3b. The new text is blank or too long.

    * 3b1. TuteeBook3 shows an error message.

    * 3b2. Tutor enters the corrected text.

      Steps 3b1-3b2 are repeated until the text is valid.

      Use case resumes at step 4.

---

**Use case: UC9 - Delete a note from a student**

**MSS**

1.  Tutor requests to view a specific student by index
2.  TuteeBook3 shows the student's record with numbered notes
3.  Tutor requests to delete a specific note, giving the student's index and NID
4.  TuteeBook3 deletes the note, renumbers the remaining notes, and shows a confirmation message

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
4.  TuteeBook3 warns that all students and notes will be deleted, and asks Tutor to type `clear` again
5.  Tutor requests to clear all students again
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

* 5a. Tutor enters any other command instead of confirming.

    * 5a1. TuteeBook3 cancels the pending `clear` request and processes the new input as usual.

      Use case ends.

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

* **Filter**: A prefix and value given to the `find` command, such as `lvl/S2`, that narrows down the student list.
* **NID**: The number shown before a note in the student detail view. Notes are numbered 1, 2, 3 in the order they were added. After a note is deleted, the notes after it move up one number, so an NID is not permanent.
* **Prefix**: A short marker ending in `/`, such as `n/` or `sch/`, that labels the value after it in a command.
* **SID**: Student ID, a unique number given to each student when they are added. It is never reused, even after the student is deleted.

#### Screen layout

* **Command box**: The text field at the top of the window where the user types commands.
* **Detail view**: The view that shows everything about one student, including all their notes. It takes the place of the student list, so only one student is shown at a time. The user can return to the student list with `list` or `find`.
* **Result display**: The area below the command box that tells the user whether their last command worked, and why not if it failed.
* **Student card**: One student's entry in the student list.
* **Student list**: The view that lists students with their main details. It takes the place of the detail view when the user enters `list` or `find`.

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

   1. Prerequisites: Tim Tan and Ken Lim were added as above. Use `find n/Tim` or `find n/Ken` so that the student is first in the list.

   1. Test case: `edit 1 gp/98765432` on Tim Tan<br>
      Expected: Only the guardian's phone changes. Viewing Tim Tan's student details shows guardian Mary Tan with phone number `98765432`.

   1. Test case: `edit 1 gn/Peter Lim` on Ken Lim<br>
      Expected: Ken Lim is unchanged. The status message shows `This person has no guardian yet. Give both gn/ and gp/ to add one.`

   1. Test case: `edit 1 gn/ gp/91234567` on Tim Tan<br>
      Expected: Tim Tan is unchanged. The status message says that guardian name and guardian phone cannot be empty.

   1. Test case: `edit 1 gn/ gp/` on Tim Tan<br>
      Expected: The guardian is removed, and viewing the student's details shows `Not provided` for both guardian name and guardian phone.

   1. Test case: `edit 1 gn/ gp/` on Ken Lim<br>
      Expected: Ken Lim is unchanged. The status message shows `This person has no guardian to remove.`

### Entering phone numbers

1. Adding a student with a formatted Singapore number

   1. Test case: `add n/Ravi Kumar sch/Victoria School lvl/S2 sub/Mathematics band/G1 p/+65 9123-4567`<br>
      Expected: Ravi Kumar is added with phone number `91234567`. The status message shows no warning about the phone number.

1. Editing the phone number

   1. Prerequisites: Ravi Kumar was added as above. Before each test case, use `find n/Ravi` so that Ravi Kumar is first in the list.

   1. Test case: `edit 1 p/+44 7911 123456`<br>
      Expected: The phone number is saved as `+447911123456`. The status message is shown in amber, with a warning that phone `+447911123456` doesn't look like a Singapore number.

   1. Test case: `edit 1 sch/Bedok Green Secondary School`<br>
      Expected: The school changes. The status message shows no warning about the phone number.

   1. Test case: `edit 1 p/12`<br>
      Expected: Ravi Kumar is unchanged. The status message says `Phone 12 needs 3 to 15 digits, counting the country code, but has 2.`

### Saving data

1. Dealing with missing/corrupted data files

   1. _{Explain how to simulate missing or corrupted data files and state the expected behavior.}_

1. _{ more test cases …​ }_
