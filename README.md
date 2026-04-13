# UniTasker - All-in-One Task & Course Management

A comprehensive desktop application for students to manage tasks, deadlines, events, and academic courses. Built with Java and optimized for Command Line Interface (CLI) usage.

## Features

### 4 Management Modes

- **Task Management** - Organize todos across multiple categories with priority levels (0-5)
- **Deadline Manager** - Track assignments with automatic sorting by due date
- **Event Manager** - Schedule events and create recurring weekly meetings
- **Course Tracker** - Manage course assessments with weighted grading and GPA calculation

### Key Capabilities

- **8 Command Aliases** - Fast shortcuts for common operations: `a` (add), `d` (delete), `l` (list), `m` (mark), `u` (unmark), `p` (priority), `s` (sort), `f` (find)
- **Intelligent Defaults** - Deadlines without time automatically default to 23:59
- **Smart Validation** - Course assessment weightage validation (max 100%, min 0%)
- **Data Persistence** - Automatic saving and loading of all tasks and courses
- **Interactive Help** - Run `help commands` to see full command reference with 50+ examples
- **Daily Workload Tracking** - Monitor incomplete and completed tasks per date
- **Recurring Events** - Create weekly recurring meetings with flexible end dates

## Quick Start

### Prerequisites
- Java 17 or above
- Gradle (included with project)

### Running the Application

1. Build the project:
   ```bash
   ./gradlew build
   ```

2. Run the application:
   ```bash
   java -jar build/libs/tp.jar
   ```

3. (Recommended) Start by creating a category:
   ```
   add category School
   ```

### Quick Command Examples

```
# Add tasks
a todo 1 Review notes /p 3
a deadline 1 Project due /by 20-05-2026
a event 1 Meeting /from 22-05-2026 1400 /to 22-05-2026 1530

# List and manage
l todo
m todo 1 1
d todo 1 1

# Course management
course add CS2113
course add-assessment CS2113 /n Midterm /w 60 /ms 100
course score CS2113 /n Midterm /s 95

# Find and search
f exam
help commands
```

## Documentation

For comprehensive usage instructions, see [User Guide](docs/UserGuide.md).

For technical architecture and design, see [Developer Guide](docs/DeveloperGuide.md).

## Setting Up for Development

### IntelliJ Setup

Prerequisites: JDK 17 (exact version), latest IntelliJ IDEA

1. **Set up JDK 17** - Follow [this guide](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk)
2. **Import as Gradle Project** - Follow [this guide](https://se-education.org/guides/tutorials/intellijImportGradleProject.html)
3. **Verify setup** - Locate `src/main/java/seedu/duke/UniTasker.java`, right-click and choose `Run UniTasker.main()`

**File Structure Note:** Keep `src/main/java/` as the root for Java files - this is where Gradle expects to find them.

## Build & Testing

### Build the Project
```bash
./gradlew build
```

### Run Tests
```bash
# Run all JUnit tests
./gradlew test

# Run text-based UI tests
cd text-ui-test
./runtest.sh        # macOS/Linux
runtest.bat         # Windows
```

### Code Quality
- **Checkstyle** - Enforced via Gradle build
- **JUnit** - 90+ unit tests across 10+ test classes
- **Coverage** - Tests for all major components: tasks, deadlines, events, courses, storage, UI

## Project Structure

```
src/main/java/seedu/duke/
├── UniTasker.java              # Main entry point
├── appcontainer/               # Core application state
├── command/                    # Command parsing and execution (includes aliases)
├── task/                       # Task types (Todo, Deadline, Event)
├── tasklist/                   # Task collections and category management
├── course/                     # Course and assessment management
├── storage/                    # Data persistence
├── ui/                         # Command output formatting
├── util/                       # DateUtils, validators, constants
├── calender/                   # Calendar and date tracking
├── exception/                  # Custom exceptions
└── logging/                    # Log configuration
```

## Team

| Member | GitHub |
|--------|--------|
| Rajaram Sushmiithaa | [sushmiithaa](https://github.com/sushmiithaa) |
| Vansh Puri | [benguy6](https://github.com/benguy6) |
| Michael Shyam Wilfred David Samuvel | [michaelshyam1](https://github.com/michaelshyam1) |
| Mark Ng Jian Xiong | [marken9](https://github.com/marken9) |
| Wen Jun Yu | [WenJunYu5984](https://github.com/WenJunYu5984) |

## Continuous Integration

This project uses [GitHub Actions](https://github.com/features/actions) for CI. All commits and PRs trigger automated builds and test runs to verify code quality.

## Architecture

The application follows a Command pattern architecture with:
- **CommandParser** - Routes user input to appropriate command handlers with alias support
- **AppContainer** - Centralized state management for categories, tasks, and courses
- **Storage** - Persistent data layer with file-based storage
- **UI** - Formatted output for all commands and responses

See [Developer Guide](docs/DeveloperGuide.md) for detailed architecture diagrams and design patterns.
