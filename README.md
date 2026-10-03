# AddressBook

[![CI status](https://github.com/AY2627S1-CS2103T-T09-1/tp/actions/workflows/gradle.yml/badge.svg)](https://github.com/AY2627S1-CS2103T-T09-1/tp/actions/workflows/gradle.yml)
[![codecov](https://codecov.io/gh/AY2627S1-CS2103T-T09-1/tp/graph/badge.svg?token=IRAZVCSUXY)](https://codecov.io/gh/AY2627S1-CS2103T-T09-1/tp)

AddressBook is a desktop contact-management application for private tutors in Singapore. It is designed for tutors who manage many students across different schools, levels, and subjects, and who prefer fast typed commands to navigating a mouse-driven interface.

The project is evolving from [AddressBook-Level3](https://se-education.org/addressbook-level3/) as part of the NUS CS2103T team project. Its goal is to keep student and guardian contacts, lesson details, and notes in one locally stored record that a tutor can retrieve quickly.

![AddressBook user interface](docs/images/Ui.png)

## Current features

The current version supports the core contact-management workflow:

* Add, edit, and delete contacts.
* Find contacts by name using a case-insensitive search.
* List all saved contacts.
* Store contact data locally in a human-editable JSON file.
* Operate through short typed commands while displaying results in a JavaFX interface.

See the [User Guide](docs/UserGuide.md) for command formats, examples, and the complete behavior of the current release.

## Quick start

### For users

1. Install Java 25.
1. Build the application with `./gradlew shadowJar` on macOS or Linux, or `gradlew.bat shadowJar` on Windows.
1. Run `java -jar build/libs/addressbook.jar`.
1. Type `help` in the command box to view the available commands.

Application data is saved automatically to `data/addressbook.json` relative to the directory from which the JAR is run.

### For developers

Clone the repository, ensure Java 25 is active, and run:

* `./gradlew run` to launch the application.
* `./gradlew check` to compile the project, run checkstyle, and execute all tests.
* `./gradlew clean check coverage` to run the same Gradle checks used by CI.
* `cd .github && ./run-checks.sh` to check Markdown and source files for whitespace, newline, and line-ending issues.

For detailed setup and architecture information, see the [Developer Guide](docs/DeveloperGuide.md) and [Setting Up guide](docs/SettingUp.md).

## Documentation

* [User Guide](docs/UserGuide.md)
* [Developer Guide](docs/DeveloperGuide.md)
* [Project team](docs/AboutUs.md)

## Acknowledgements

This project is based on [AddressBook-Level3](https://github.com/se-edu/addressbook-level3) by the [SE-EDU initiative](https://se-education.org/).

## License

This project is distributed under the [MIT License](LICENSE).
