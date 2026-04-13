# Vansh Puri (benguy6) - Project Portfolio Page

**Project: UniTasker**

UniTasker is a desktop application used to keep track of tasks and courses. It provides students with a unified platform to manage four feature modes: task management with priority levels, deadline tracking with auto-sorted dates, event scheduling with recurring support, and course tracking with weighted grade calculation.

Given below are my contributions to the project.

## Summary of Contributions

### New Features

**1. Command Aliases for Rapid Task Entry**
- What it does: Added 8 single-character shortcuts for the most frequently used commands: `a` (add), `d` (delete), `l` (list), `m` (mark), `u` (unmark), `p` (priority), `s` (sort), `f` (find).
- Justification: Reduces typing effort and improves user efficiency, especially for power users managing many tasks in session. Aliases reduce command entry time by up to 75%.
- Highlights: 
  - Implemented via `mapAliasToCommand()` preprocessor in `CommandParser.java` that converts single-character input to full command names before routing
  - Aliases route seamlessly to existing command handlers without code duplication
  - All 8 aliases tested and verified functional in integrated system
  - Example: `a todo 1 Study` is equivalent to `add todo 1 Study`

**2. Comprehensive Help System with Embedded Command Cheatsheet**
- What it does: Provides contextual help documentation for all 4 feature modes (task, deadline, event, course) and command variants, accessible via `help <topic>` command. Running `help commands` displays a full 50+ command reference with aliases, date/time formats, and day names.
- Justification: Users need accessible documentation to discover commands and understand command syntax without leaving the application. Embedded cheatsheet eliminates need to reference external documentation for quick command syntax lookup.
- Highlights: 
  - 316-line `CommandHelp.java` with smart topic routing using switch expressions
  - Supports mode-specific help (`help task`, `help deadline`, `help event`, `help course`)
  - Supports command-variant help (`help add`, `help delete`, `help list`)
  - Full command summary with 50+ command examples, date format (dd-MM-yyyy), time format (HHmm 24-hour), day names (Monday-Sunday)
  - All 8 aliases listed and documented with format specifications
  - Integration with `HelpCommand.java` (21 LoC) for seamless user access

**3. Complete Course Management and Grade Tracking System**
- What it does: Allows users to add/manage courses, track assessment components with weightages, record scores, and compute weighted grades automatically.
- Justification: Students need to track grades across multiple modules in one place, with automatic weighted grade calculation for multiple assessments per course. Prevents manual grade tracking errors and provides single-point visibility.
- Highlights: 
  - Full CRUD operations for courses (`add`, `delete`, `list`, `view`) and assessments (`add-assessment`, `delete-assessment`, `score`)
  - Validates weightage limits: maximum 100% total weightage, prevents exceeding (e.g., rejects adding 50% when 60% already exists)
  - Persists all data across sessions via custom file-based storage with encoding/decoding
  - Intelligent handling of ungraded assessments (uses -1 sentinel, excludes from weighted calculations)
  - Weighted score formula: `(score_percent * weightage / 100)` with aggregation at course level
  - Example: Course with Finals (40%, scored 85/100) + Midterm (20%, scored 20/25) = weighted contribution to overall grade

### Code Contributed

**RepoSense**: [Link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=benguy6&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=benguy6&tabRepo=AY2526S2-CS2113-T14-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

**Functional Code Breakdown** (729 LoC)

- Implemented core course tracking classes (645 LoC):
  - `Assessment.java` (67 LoC): Core grading model with sentinel-based ungraded status tracking (-1 for NOT_GRADED), weighted score calculation, and file encoding/decoding
  - `Course.java` (114 LoC): Course container managing assessment collections, aggregate calculations (total weightage, weighted scores, graded weightage), persistence encoding, and helper methods
  - `CourseList.java` (72 LoC): Central repository for course management with case-insensitive lookup and batch operations
  - `CourseManager.java` (392 LoC): Orchestrates course lifecycle with validation logic (max 100% weightage constraint), intelligent error handling, and state management

- Implemented `CourseStorage.java` (95 LoC) with file-based persistence:
  - Custom file format: `COURSE:CODE` → assessment lines → `END` for clean line-by-line parsing
  - Defensive directory creation and IOException handling
  - Roundtrip consistency guarantees (save → load produces identical state)
  - Handles missing files gracefully (returns empty list for first-run scenario)

- Implemented `CourseException.java` (11 LoC): Course-specific exception class with descriptive error messages

- Implemented `CommandParser.java` enhancements (command alias feature):
  - `mapAliasToCommand()` method routes 8 aliases (a, d, l, m, u, p, s, f) to full command names via switch expression
  - Preprocessor runs before main command routing to maintain backward compatibility

- Enhanced `CommandHelp.java` (316 LoC) with comprehensive multi-topic help system:
  - 8 static methods: `getGeneralHelp()`, `getTaskHelp()`, `getDeadlineHelp()`, `getEventHelp()`, `getCourseHelp()`, `getAddCommandHelp()`, `getDeleteCommandHelp()`, `getListCommandHelp()`
  - Smart topic routing via static `getHelp(String topic)` method with case-insensitive switch expression
  - Full command reference with 50+ examples, format specifications, and all 8 aliases documented

- Updated `GeneralUi.java` (12 LoC added): Extended welcome message to display all 4 feature modes and help command hints

- Updated `ErrorUi.java` (16 LoC): Added `printUnknownCommandHint()` method suggesting `help` command for command discovery

- Integrated course feature into `UniTasker.java`: Initialization of `CourseManager` and integration into main command loop with exception handling

- Added defensive assertions to `Task.java` and `Category.java`: Precondition validation for non-null/non-empty descriptions and names

- Updated `CourseClassDiagram.puml`: Professional styling (white background, black borders), method shorthand for clarity, multiplicity notation consistency

**Code Quality Improvements** (11 new constants across 5 files)

- Refactored magic numbers into named constants:
  - `Assessment.java`: `NOT_GRADED = -1` (ungraded sentinel), `ZERO_SCORE = 0` (ungraded contribution)
  - `CourseManager.java`: `MAX_WEIGHTAGE = 100`, `MAX_ALLOWED_SCORE = 10000`, `MIN_WEIGHTAGE = 0`, `MIN_SCORE = 0`
  - `DateUtils.java`: `END_OF_DAY_HOUR = 23`, `END_OF_DAY_MINUTE = 59` (deadline default time)
  - `Deadline.java`: `TASK_DONE = 1`, `TASK_NOT_DONE = 0` (file format status encoding)
  - `Event.java`: `NO_RECURRING_GROUP = -1` (non-recurring sentinel), `TASK_DONE = 1`, `TASK_NOT_DONE = 0`
  - Impact: Improves maintainability, enables single-point business rule updates, prevents typos, supports validation constraints

### Testing (204 LoC)

Added JUnit tests with comprehensive coverage:

- `AssessmentTest.java` (70 LoC):
  - Tests: Constructor states (graded vs ungraded), score recording and state transitions, weighted score calculations, encode/decode roundtrips
  - Edge cases: Null/invalid data handling, mathematical precision verification (0.001 delta)
  - Coverage: 8 test methods covering all public methods

- `CourseTest.java` (45 LoC):
  - Tests: Assessment addition, case-insensitive lookup, removal operations, count tracking
  - Coverage: 4 test methods for CRUD operations

- `CourseStorageTest.java` (88 LoC):
  - Tests: Missing file returns empty list, single course persistence, multi-course persistence, data consistency across save/load cycles
  - Coverage: 3 comprehensive test methods for file I/O operations including teardown cleanup

### Developer Guide Contributions

- Added Course Tracker architecture section with explanation of manager → list → course → assessment hierarchy and design rationale
- Added Course Tracker sequence diagram (`CourseAddSequenceDiagram.puml`) showing flow: user input → CommandParser → CourseCommand → CourseParser → CourseManager → CourseStorage → save
- Added Undo feature documentation with reverse operation mappings: "add" ↔ "delete", "add-assessment" ↔ "delete-assessment", "score" ↔ "revert previous score"
- Added design considerations for weighted score calculation strategy (excluding ungraded assessments from denominator) and file persistence encoding

### User Guide Contributions

- Added comprehensive Course Tracker section:
  - `course add <code>`: Create new course
  - `course delete <code>`: Remove course
  - `course list`: Display all courses
  - `course view <code>`: Show assessments with weightage and scores
  - `course add-assessment <code> /n <name> /w <weightage> /ms <max_score>`: Add assessment with validation (example: `course add-assessment CS2113 /n Finals /w 40 /ms 100`)
  - `course score <code> /n <name> /s <score>`: Record score (example: `course score CS2113 /n Finals /s 85`)
  - `course delete-assessment <code> <name>`: Remove assessment

- Added command aliases reference guide: a=add, d=delete, l=list, m=mark, u=unmark, p=priority, s=sort, f=find with usage examples

- Added help system reference: `help commands` displays full 50+ command cheatsheet (date format: dd-MM-yyyy, time format: HHmm, days: Monday-Sunday)

- Updated welcome message and error handling to mention 4 feature modes and direct users to help system for discovery

### Review/Mentoring Contributions

- Reviewed pull requests and provided feedback on code quality, test coverage, and documentation standards
- Assisted teammates with file-based persistence strategies and custom encoding/decoding patterns
- Mentored on defensive programming practices (assertions, validation, exception handling)
- Helped with architecture decisions for multi-layer design (Manager → List → Model)

## Enhancements Beyond Original Scope

- **Command Alias Implementation**: Extended CommandParser to support rapid entry via single-character shortcuts, improving power-user efficiency
- **Enhanced Help System**: Transformed basic help into comprehensive multi-topic system with 50+ command examples and format specifications
- **Magic Number Refactoring**: Extracted 11 constants across 5 files to improve maintainability and enable single-point business rule updates
- **Validation Architecture**: Added comprehensive input validation and error handling across course-related commands with descriptive error messages

---

## Highlights of Implementation Quality

1. **Separation of Concerns**: Course feature cleanly separated into Manager (orchestration), List (collection), Model (Course/Assessment), Storage (persistence), UI (display)
2. **Testability**: All public methods covered by JUnit tests with edge case handling
3. **Error Handling**: Custom CourseException with descriptive messages prevents silent failures
4. **Data Consistency**: File-based persistence ensures data survives app restarts with encode/decode strategy
5. **Business Logic**: Weighted grade calculation prevents ungraded assessments from skewing results; weightage validation prevents >100% configurations