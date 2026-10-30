# Agent guide

Shared instructions for AI coding agents working on this CS2103T team project (tP).
All team members use this file, so keep it short and team-wide.
Read [Maintaining this file](#maintaining-this-file) before you edit it.

## Project

* Brownfield evolution of AddressBook-Level3 (AB3). Contact management stays the primary focus.
* Base package: `seedu.address`. Layers: `ui`, `logic`, `model`, `storage`, `commons`. See `docs/DeveloperGuide.md` for the architecture.

### Current phase: feature freeze (v1.6)

Features are frozen. The work now is fixing leftover bugs, tightening error messages, and cleaning up the UG, DG, and UML diagrams. Source: [week 12 tP tasks](https://nus-cs2103-ay2627-s1.github.io/website/schedule/week12/project.html).

* Dates: the freeze started Fri 30 Oct 2026, 10:00. Final deliverables are due Tue 3 Nov 2026, 14:00 (14:00:01 is late). Push no code for 30 days after that. The PE runs Fri 6 Nov to Sat 7 Nov.
* Do not add commands, prefixes, parameters, or other features. If a fix seems to need one, stop and ask the user.
* Each member may change at most 15% or 150 lines (whichever is higher) of their own `src/main` code during v1.6. Blank lines and comments count. Going over costs at least 2 marks. Tests, docs, and diagrams have no limit.
  * Lines you edit count against your quota, even lines a teammate wrote. Refactoring counts too.
  * Before you change `src/main`, weigh the fix against its line cost and regression risk. Make the smallest change that fixes the bug.
  * In the handoff, report the branch's `src/main` changes from `git diff --stat $(git merge-base upstream/master HEAD) -- src/main`. Members track their running total on the tP Code Dashboard.
* Triage each bug before you touch code:
  1. Should have been fixed in v1.5: fix it now. If the fix costs too many lines, state the limitation in the UG instead.
  1. Worth fixing only in a later version: leave the code, and list it under **Known issues** in the UG if users are likely to hit it. This lowers the severity of a PE report but does not cancel it.
  1. Intended behavior: leave the code, and make sure the UG states the behavior.

### Target user

A private tutor who teaches secondary school students (levels S1 to S5) in Singapore and prefers typing commands to using a mouse.

Persona:

* Tutors many students across different schools, levels, and subjects, and takes on new students from their first enquiry.
* Contacts both students and their guardians, and often needs to find a student quickly while a guardian is on the phone.
* Keeps notes on each student, such as strong and weak topics, test scores, and reminders, to tailor lessons.
* Needs to see one student's full record on one screen before a lesson, and to group students by school, level, or subject when planning.

Value proposition: keep every student's contacts, guardian contacts, school, level, subjects, and lesson notes in one place, and reach any of it with a short typed command.

Design every feature for this user. When a choice affects what users can enter, check it against [Preventing bugs and feature flaws](#preventing-bugs-and-feature-flaws). For example, Singapore names often contain `s/o` or `d/o`.

## Commands

* `./gradlew check`: compile, run checkstyle, and run all tests. Must pass before you commit.
* `./gradlew run`: launch the app.
* `./gradlew shadowJar`: build the single JAR (`build/libs/TuteeBook3.jar`).
* `cd .github && ./run-checks.sh`: check trailing whitespace, missing EOF newlines, and CRLF line endings.
* CI runs `./gradlew check coverage` on Ubuntu, macOS, and Windows.

### Before you push

CI must pass on all three platforms before a change is ready to push. Run what CI runs:

1. `./gradlew clean check coverage` from the repo root.
1. Confirm coverage is still 50% or higher. See [Tests](#tests).
1. `cd .github && ./run-checks.sh`.
1. `git status` and `git diff`: confirm the change contains only what the task needs.

Code that passes on your machine can still fail CI on another OS:

* Build file paths with `Path.of(...)` or `Path.resolve(...)`, never by joining strings with `/` or `\`.
* Do not hard-code `\n` when comparing multi-line output in tests; use `System.lineSeparator()`.
* Do not rely on file name case. Windows and macOS treat `Person.java` and `person.java` as the same file.
* Do not edit the Gradle wrapper files. CI validates the wrapper.

If a check fails, fix the cause. Do not delete tests, add `@Disabled`, add `CHECKSTYLE.OFF` comments, or edit `config/checkstyle/` to make it pass unless the user asks.

## Hard constraints

Never violate these. They come from the [tP constraints](https://nus-cs2103-ay2627-s1.github.io/website/admin/tp-constraints.html).

* Typed commands are the primary input. The GUI gives feedback; it does not replace the CLI.
* Single user. No shared data files and no multi-user features.
* Store data locally in human-editable text files (JSON, like AB3). No DBMS.
* Object-oriented design first.
* Must run on Windows, Linux, and macOS with only Java 25 installed. No OS-specific libraries or paths.
* No installer, no remote server of our own, and one JAR under 100 MB.
* Third-party libraries must be free, open source, permissively licensed, and approved by the teaching team first. Do not add dependencies without asking the user.
* GUI must work at 1920x1080 (100% and 125% scale) and stay usable at 1280x720 (150% scale).
* Every commit leaves the product working. Deliver in small increments.

## Java coding standard

The [se-edu Java coding standard (intermediate)](https://se-education.org/guides/conventions/java/intermediate.html) is a graded component, not a suggestion.
Checkstyle (`config/checkstyle/checkstyle.xml`) catches layout, braces, whitespace, import order, and missing Javadoc.
It does not catch the rules below, so apply them yourself:

* Booleans start with `is`, `has`, `was`, `can`, or similar: `isDone`, `hasTag()`, `setDone(boolean isDone)`.
* Collections use plural names: `List<Person> persons`.
* Methods are verbs; classes are nouns.
* Acronyms are not all caps: `exportHtml()`, not `exportHTML()`.
* Scope decides name length: loop indices may be `i`, `j`; wide-scope names are descriptive.
* Group related constants with a shared prefix: `COLOR_RED`, `COLOR_BLUE`.
* Declare variables in the smallest scope and initialize them at declaration.
* Fields are never public, except `public final` fields in pure data classes.
* Comments use American English.

Rules checkstyle does enforce, for reference while writing code:

* 4-space indent, no tabs. Wrapped lines indent 8 spaces. Lines stay at 110 characters or fewer; 120 is the hard limit.
* K&R braces. Always use braces for `if`, `else`, `for`, and `while` bodies.
* `switch`: indent `case` labels; every `switch` has a `default`; mark intentional fall-through with `// Fallthrough`.
* Break wrapped lines after commas and before operators (including `.`). Keep `=` at the end of the line.
* Imports: no wildcards. Order: static imports, then `java`/`javax`, then `org`, then everything else (`javafx`, `seedu`). Alphabetical within each group, with a blank line between groups.
* Use `TODO`, never `FIXME`.

### Javadoc

* Every public class and every public method needs a header comment. Getters, setters, and `@Override` methods whose parent Javadoc applies are exempt.
* Start the summary with a third-person verb: "Returns ...", "Adds ...".
* Put a blank line between the description and the tags.
* Use `@param` for all parameters or none. End each tag description with a period.

```java
/**
 * Returns the person at the given index in the filtered list.
 *
 * @param index Zero-based index of the person.
 * @return The person at {@code index}.
 */
```

### Tests

* Name test methods `featureUnderTest_testScenario_expectedBehavior`, for example `execute_duplicatePerson_throwsCommandException`.
* Mirror the `src/main` package structure under `src/test`. Reuse builders and typical data in `seedu.address.testutil`.
* Keep JUnit test coverage at 50% or higher. Prioritize the code where a bug would hurt most, such as parsers, commands, and the model.
* Before you add JUnit test cases, list the cases you plan to add and ask the user to confirm.
* Add or update tests with every behavior change. Cover the cases in the [test checklist](#test-checklist).
* When you add or change behavior, also review the existing tests for that code. Update tests that still expect the old behavior, and make sure the tests would fail if a later change broke the new behavior.
* `./gradlew coverage` only writes the JaCoCo report to `build/reports/jacoco/coverage/html/index.html`. The build does not fail when coverage drops below 50%, so open the report and check the **Total** row yourself.

## Code quality

Code quality is graded along with the coding standard. Checkstyle does not check any of these.

* Keep methods short. Aim for under about 30 lines; extract a well-named helper when a method grows past that.
* Avoid deep nesting. Use guard clauses and return early so the happy path stays at the lowest indent level.
* Keep one level of abstraction per method. A method either coordinates steps or does low-level work, not both.
* Split complicated expressions into named intermediate variables.
* Replace magic numbers and strings with named constants: `MAX_TAG_LENGTH`, not `50`.
* Do not reuse a variable for a second purpose.
* Remove duplicated logic by extracting it, not by copy-pasting and tweaking.
* Delete dead code. Do not leave it commented out.
* Never leave a `catch` block empty. Handle the exception, or log it and explain why ignoring it is safe.
* Keep it simple. Do not optimize before there is a measured need.
* Comment what and why, not how. Do not restate what the code already says.

## Preventing bugs and feature flaws

Other teams will test this app and file bugs against it, and every accepted bug costs marks.
Treat this section as correctness requirements, not polish.
Define "correct" from the user's point of view, not from what is easy to parse or store.
Source: [functionality bugs](https://nus-cs2103-ay2627-s1.github.io/website/schedule/week8/project.html#functionality-bugs) and [feature flaws](https://nus-cs2103-ay2627-s1.github.io/website/schedule/week8/project.html#feature-flaws).

### Handle user mistakes without harm

Deliberate sabotage, such as typing a 30-digit phone number on purpose, is not a bug.
A plausible mistake is: a missing space between parameters, a repeated prefix, an extra space, or an empty value.
A plausible mistake must never crash the app, corrupt the data file, or leave the app unusable.

* Do: `add n/John Doep/98765432` shows the name rule and a hint to check for a missing space before a prefix.
* Don't: let the value flow through and fail later with an uncaught exception.
* Do: `delete 99999999999` shows an invalid index error.
* Don't: call `Integer.parseInt` without handling overflow. Numbers from user input follow the same rule as any other mistake.

### Accept realistic values

Define "valid" by what real users need to enter, not by the narrowest data type.

* Limits must be reasonable for the target user.
  * Don't: pick a limit that real users hit without saying so.
  * Do: when a rule is narrow on purpose, state it exactly in the UG and say why.
  * Do: pick a limit no realistic value reaches, and document it in the UG.
* Do not reject symbols that real values contain.
  * Names accept only ASCII letters, digits, and spaces (`Name.VALIDATION_REGEX`), so `Rajesh s/o Kumar`, `Mary-Jane O'Brien`, and `J. Tan` are rejected. The UG and DG document this choice, but testers may still report it as a feature flaw. Do not change the rule without asking the user.
  * Do: when a symbol is hard to parse, improve the parser before banning the symbol.
* Warn rather than block unusual but harmless input.
  * Don't: reject `+65 9123 4567`, `9123-4567` or an overseas number. `ParserUtil.parsePhone` removes spaces, dashes, and a leading `+65` when the rest is a Singapore number.
  * Do: save a number that isn't a Singapore number and add a warning with `CommandResult.withWarning`, as `add` and `edit` do for `p/` and `gp/`.
* Still guard against harmful input. Accepting a value that makes data ambiguous or breaks a later command is also a flaw. Block it or warn about it.

### Keep long values usable

* Don't: let a 100-character name push other fields off screen, or cut it so only the first few characters show.
* Do: wrap long text so the user can read the whole value. Check it at 1280x720.

### Write clear, consistent error messages

Error messages are the riskiest text in the app. Testers trigger every one, and a vague, wrong, or inconsistent message is a bug.

* Name the field and the rule the value broke.
  * Don't: "Invalid input." or "Invalid command format!" for a single bad field.
  * Do: "Email `john@` is missing a domain after `@`."
* Keep messages short and plain: one or two sentences, everyday words, no exclamation marks.
* Do not guess what the user meant. A guess that is wrong misleads the user. When a value could have failed for more than one reason, state the rule broadly and briefly instead of naming one cause.
  * Don't: "Did you mean `S3`?" or a hint that assumes which mistake the user made.
  * Don't: swing the other way and list every sub-rule. `Email.MESSAGE_CONSTRAINTS` is a numbered list today; treat it as a known flaw.
  * Do: "Level must be one of S1, S2, S3, S4, S5."
* Keep wording consistent across fields:
  * Use one sentence pattern for every field, such as `<Field> must ...`, with the field name in the same form each time.
  * Say the same kind of rule the same way. Don't write "1-40 characters" for one field and "at most 200 characters" for another.
  * When several fields share a rule or a phrase, build it from one shared constant or helper instead of copying it. Keep `model` classes independent of `logic.Messages`.
  * Before you change a message, read the messages for similar fields and match them, or change them together.
* When you change a message, update the tests that assert it and any UG text that quotes it. In the UG, describe the rule rather than quote the whole message.
* Tell format errors apart from invalid values. For a `YYYY-MM-DD` date, `2025-13-28` is a format error (the month must be 1 to 12), but `2025-02-30` is an invalid value (February has no 30th). Calling one the other is a bug. A combined "Invalid date or incorrect format" message is acceptable only if the team rules the distinction out of scope.

### Keep commands fast to type

* Don't: use long keywords when short ones would do, case-sensitive keywords with no reason, or hard-to-type symbols.
* Don't: go so short that keywords become cryptic.
* Do: offer a short form and a descriptive form where it helps, the way `git commit -n` and `--no-verify` are the same.

### Match case sensitivity to the real world

* Names and search keywords are not case-sensitive in real life, so they must not be in the app.
* Don't: treat `filter n/alice` and `filter n/Alice` differently.

### Make search useful

* `filter` replaces AB3's `find`. It combines different filters with AND. Text filters such as `n/` and `sch/` match any part of the value, ignoring case and extra spaces. Fields with fixed values, such as `lvl/`, `sub/`, `p/`, and `t/`, must match the whole value.
* The UG's `filter` section is the reference for this behavior. Keep the code and that section in sync.

### Treat duplicate detection as uncertain

* Don't: compare with exact string equality.
* Do: normalize before comparing (trim, collapse spaces, ignore case), as `Name` does for `Person.isSamePerson`.
* Do: when a match is likely but not certain, warn and let the user decide. `add` and `edit` ask for `CONFIRM` when only the name matches, and block a student whose name, school, and level all match.
* Do: state the detector's limits in the UG. Never imply that all duplicates are caught.

### Protect the data file

Support manual edits at least as well as AB3: a correctly edited file loads, and a wrongly edited one may be rejected but must not crash the app.

* Don't: switch to a format a person cannot reasonably edit by hand.
* Don't: promise more editing support in the UG than the app delivers.
* Do: when a field or the JSON format changes, make sure existing data files still load or fail clearly. Add valid, invalid, and old-format test files under `src/test/data`.

### Keep the UG and the app in sync

* A mismatch between the UG and the app is a bug, whichever side is wrong.
* Document every limit, restriction, and default that users can observe.
* Update the UG in the same PR as the behavior change.

### Keep terminal output clean

* Don't: print stack traces, debug output, or alarming messages to the terminal during normal use.
* Do: log through `LogsCenter` at the right level. Do not use `System.out.println` for diagnostics.

### Do not hide behind "out of scope"

A missing or weak feature still counts as a flaw when:

* fixing it is essential for the app to be reasonably useful, or
* a clearly better behavior for the user would take little extra effort.

### Test checklist

For every new or changed command, add tests for:

1. The happy path.
1. Plausible typing mistakes: missing space, missing or repeated prefix, extra whitespace, and empty values.
1. Case variants and realistic symbols, such as `s/o`, `-`, `'`, `+`, and `.`.
1. Boundaries: very long values, zero, negative numbers, and numbers too big for an `int`.
1. Duplicates and near-duplicates.

In each failure test, assert the exact error message and check that the model and data file are unchanged.

## Git conventions

The [se-edu git conventions](https://se-education.org/guides/conventions/git.html) are graded, not optional.

* Subject line: imperative mood ("Add", not "Added"), capitalized, no trailing period. Aim for 50 characters or fewer; 72 is the hard limit.
* Optional scope prefix: `Person class: Remove static imports`.
* A body is optional. If you write one:
  * Separate it from the subject with a blank line.
  * Wrap at 72 characters.
  * Explain what changed and why, not how. Use bullet points if needed.
* Keep each commit to one logical change.
* Branch names are kebab-case. For issue work, use `<issue-number>-<keywords>`, for example `42-add-tag-command`.

### Commit handoff

Agents never change git history or the remote. The member commits and pushes, so RepoSense credits them.

* Do not run `git add`, `git commit`, `git push`, `git merge`, `git rebase`, `gh issue create`, or `gh pr create`. Read-only commands such as `git status`, `git diff`, and `git log` are fine.
* When the work is done and [Before you push](#before-you-push) passes, end with commit instructions:
  1. If the current branch is `master`, start with `git switch -c <issue-number>-<keywords>`. Changes never get committed to `master`. If no issue exists yet, draft its title for the member to create, as in step 1 of [Workflow](#workflow).
  1. Split the changes into commits of one logical change each. Order them so every commit builds and passes tests on its own.
  1. For each commit, give a `git add` command that lists exact paths and a `git commit` command with the full message. Never use `git add .` or `git add -A`.
  1. If one file holds two logical changes, say so and tell the member to split it with `git add -p`.
  1. During the feature freeze, report the `src/main` line count as described in [Current phase](#current-phase-feature-freeze-v16).
  1. End with the `git push` command for the current branch.
  1. If the branch is ready for a PR, draft the PR title and description as described in [Pull requests](#pull-requests).

Example handoff:

```sh
git add src/main/java/seedu/address/model/person/Name.java src/test/java/seedu/address/model/person/NameTest.java
git commit -m "Allow hyphens and apostrophes in names"

git add docs/UserGuide.md
git commit -m "User Guide: Document allowed name characters" -m "Names can contain hyphens and apostrophes, but the User Guide
still says they must be alphanumeric."

git push origin 42-allow-name-symbols
```

### Authorship

RepoSense grades each member on the code they authored, so the human member is the only author.

* Do not add `Co-Authored-By:` trailers for AI tools to commits.
* Do not add AI tool links or "Generated with" lines to commits or PR descriptions.

### Branches

These rules come from [Admin Appendix E](https://nus-cs2103-ay2627-s1.github.io/website/admin/appendixE-gitHub.html). Grading scripts depend on them.

* Create each branch from the team repo's `master`, push it to your own fork, and open the PR from that branch. Never open a PR from `master`.
* Keep every branch after its PR merges. The scripts check that changes went through a branch.
* Update a branch by merging `master` into it. Never rebase or squash; both rewrite commit timestamps that the scripts use to track progress.
* Never force-push a shared branch.

## Pull requests

Follow the [se-edu PR guidelines](https://se-education.org/guides/guidelines/PRs.html) and [GitHub conventions](https://se-education.org/guides/conventions/github.html).

### Workflow

Every change to the team repo goes through an issue, a branch on the member's fork, and a reviewed PR. Source: [week 7 tP tasks](https://nus-cs2103-ay2627-s1.github.io/website/schedule/week7/project.html) and [Admin Appendix E](https://nus-cs2103-ay2627-s1.github.io/website/admin/appendixE-gitHub.html).
Remotes: `upstream` is the team repo, and `origin` is the member's fork. Check with `git remote -v`.

1. Create an issue on the team repo for the task. Assign it to yourself, add `type.*` and `priority.*` labels, and set the milestone, such as `v1.1`.
1. Sync `master` with the team repo: `git switch master`, `git pull upstream master`, then `git push origin master`.
1. Create a branch from `master` named after the issue, such as `12-add-agent-instructions`. See [Branches](#branches).
1. Make the change with JUnit tests for the new or changed behavior, and update existing tests it affects. See [Tests](#tests).
1. Commit following the [Git conventions](#git-conventions), then push the branch to your fork: `git push origin <branch>`.
1. Before the last commit of the issue, check the whole branch for bugs it introduces, not just the latest change:
   * Run `git fetch upstream`, then read `git diff $(git merge-base upstream/master HEAD)`. This covers every commit on the branch plus uncommitted changes.
   * Check each change against [Preventing bugs and feature flaws](#preventing-bugs-and-feature-flaws) and the [test checklist](#test-checklist), and confirm the UG matches the new behavior.
   * Report each bug with its file, line, and the input that triggers it. Fix it on the same branch before the PR is opened.
1. Open a PR from that branch to the team repo's `master`, following the rules below.
1. Get a teammate to review the PR. Resolve every comment before merging.
1. Merge only after approval, following [Merging](#merging).
1. Close the issue, then repeat step 2 so your local repo and fork match the team repo.

At the end of each weekly milestone, right before the milestone is closed, check everything merged during it for bugs:

* Run `git fetch upstream` and list the merged PRs with `gh pr list --repo <team-repo> --state merged --search "milestone:<milestone>"`.
* Review their combined changes on `upstream/master`. Look for bugs that a single-PR check misses: features that conflict with each other, UG and app mismatches, and data files from the previous milestone that no longer load.
* Run the checks in [Before you push](#before-you-push) on an up-to-date `master`.
* Report each bug with its file, line, and the input that triggers it, and draft an issue title for the member to create. Close the milestone only after each bug is fixed or its issue moves to the next milestone.

Each member sends PRs for their own work, including their own part of the documentation. Do not commit a teammate's part for them.

### PR rules

* Keep each PR to one single, standalone, complete change: code, tests, UG, and DG together. Leave out unrelated changes.
* Title: when the PR fully fixes an issue, use the issue title. Otherwise, write it like a commit subject for the whole PR.
* Description:
  * Write `Fixes #123` for a full fix or `Fixes part of #123` for a partial fix.
  * Add before-and-after screenshots for UI changes.
* Assign the PR to the issue's milestone.
* Open the PR as a draft. Mark it ready for review only after CI passes and you have read the full diff for unintended changes.

### Merging

* Never merge a PR while CI is failing.
* Use a merge commit. Do not squash or rebase.
* After merging, close the issue and sync `master` as in step 2 of [Workflow](#workflow).

## Documentation

Applies to `docs/` (User Guide, Developer Guide) and all Markdown in the repo.

* Follow the [se-edu Markdown conventions](https://se-education.org/guides/conventions/markdown.html):
  * Use `*` for bullets, `_` for italics, and `1.` for every ordered list item.
  * Put a blank line before lists, code blocks, and after headings. Put a space after `#`.
  * Do not hard-wrap prose.
* Follow the [Google developer documentation style guide](https://developers.google.com/style):
  * Use second person ("you"), active voice, and present tense.
  * Use sentence case for headings.
  * Use American English spelling, such as "prioritize" and "color". Keep official names as written.
  * Put code, commands, and file names in `code font`. Put UI elements in **bold**.
  * Use the serial comma and descriptive link text (never "click here").
* When a feature changes user-visible behavior, update `docs/UserGuide.md` in the same PR.
* When a change affects design or architecture, update `docs/DeveloperGuide.md` and its diagrams in `docs/diagrams/`.

### Keep it short

Graders and PE testers file bugs against text that is too long, unclear, or repeated. Source: [tP grading](https://nus-cs2103-ay2627-s1.github.io/website/admin/tp-grading.html) and [tP deliverables](https://nus-cs2103-ay2627-s1.github.io/website/admin/tp-deliverables.html).

* Use short sentences and plain words. Cut filler such as "note that", "simply", "in order to", and "please".
* Cut any sentence that does not help the reader use the app or understand the design.
* Say each fact once, where readers look for it first, and link to it from elsewhere. Watch for these repeats:
  * Parameter rules restated in every command section.
  * Error lists that restate a parameter rule.
  * The same warning, tip, or note in several places.
  * The same point made in both prose and a table.
* Keep the UG and DG consistent in terms: one name per concept, matching the glossary and the app.

### User Guide and Developer Guide

* The UG must match the product exactly. Remove anything not implemented, or mark it **Coming soon**.
* UG: give each command a one-line purpose, its format, and one or two examples. Leave out detail that the error message already tells the user.
* DG: aim for comprehensible, not comprehensive. Use code snippets sparingly.
* DG appendices: keep manual testing to the key cases, merge use cases that differ only slightly, and make every NFR measurable and met by the product. _Appendix: Effort_ is optional and at most about one page.

### UML diagrams

* Edit the `.puml` source in `docs/diagrams/` and regenerate its PNG in `docs/images/` with PlantUML. Never edit only the PNG.
* Use textbook UML notation exactly: line styles, arrowheads, multiplicities, and activation bars. A notation error is at least a `severity.Low` bug.
* Make each diagram match the current code. Rename AB3 classes and methods, and delete diagrams, sources, and images that no page uses.
* A sequence diagram shows the internals of at most one component; treat other components as black boxes.
* Be consistent within and across diagrams, such as using activation bars everywhere or nowhere.
* If you leave out details on purpose, say so in a note.
* Keep diagram text about the size of the body text. Split a diagram that is too big to read.

## Working agreements

* Match the patterns and style of the surrounding code. In this codebase, consistency matters more than cleverness.
* Change only what the task needs. Do not reformat, rename, or tidy other lines or files. Extra changes clutter the diff and credit the wrong member in RepoSense.
* Before you call a task done, run the checks in [Before you push](#before-you-push). Report the real result, including failures and their output. Never describe what the code should do in place of running it.
* Finish with the commit instructions described in [Commit handoff](#commit-handoff). Do not commit yourself.

## Maintaining this file

* Add a line only if an agent would get it wrong without it. Do not restate what checkstyle, CI, or the linked guides already enforce.
* Add to an existing section. A new top-level section needs team agreement.
* For deep detail, link to `docs/` instead of copying it here.
* Edit through a PR that one other member reviews. Use subjects like `AGENTS.md: Add tag command conventions`.
* `CLAUDE.md` only imports this file. Put all content here.
