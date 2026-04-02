# michaelshyam - Project Portfolio Page

**Project: UniTasker**

UniTasker is a desktop application used to keep track of tasks and courses.

Given below are my contributions to the project.

## Summary of Contributions

**New Features**

- Added the Course Tracker feature
    - What it does: Allows users to add and manage courses, track assessment components with weightages and scores, and compute weighted grades automatically.
    - Justification: Users may need to track their grades across multiple modules. Having a built-in course tracker keeps everything in one place.
    - Highlights: Supports adding/deleting courses, adding/deleting assessments, recording scores, and viewing weighted grade breakdowns. Course data is persisted to `courses.txt` across sessions.

- Added the Undo feature (course commands)
    - What it does: Allows users to reverse the most recent course command that modified data (e.g. `course add`, `course delete`, `course add-assessment`).
    - Justification: Users may accidentally add or delete a course or assessment. Undo provides a quick way to revert mistakes
    - Highlights: Implemented using the Command pattern with a history stack in `UniTasker`. Extended the `Command` interface with default `undo()` and `isUndoable()` methods so existing commands are unaffected.

**Code Contributed**: [RepoSense Link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=t14-3&sort=totalCommits%20dsc&sortWithin=totalCommits%20dsc&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=michaelshyam1&tabRepo=AY2526S2-CS2113-T14-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=functional-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

General contributions:
- Implemented `CourseCommand`, `CourseParser`, `CourseManager`,  `CourseUi`
- Implemented `UndoCommand` and extended `Command` interface with undo support
- Integrated course commands into the main command loop via `AppContainer`
- Refactored `coursestracker` package into existing package structure (`course`, `command`, `storage`, `ui`, `exception`)

**Testing**

Added JUnit tests for the following classes:

- `CourseManager`, `Course`, `Assessment`, `CourseStorage`, `CourseUndoTest`

**Developer Guide Contributions**

- Added implementation details for Course Tracker with a sequence diagram (`CourseAddSequenceDiagram`)
- Added implementation details for Undo feature with a sequence diagram (`UndoSequenceDiagram`) and class diagram (`UndoClassDiagram`)
- Added manual testing instructions for Course Tracker and Undo

**User Guide Contributions**

- Added documentation for the following features:
    - `course`, `undo`

**Review/Mentoring Contributions**
- Reviewed PRs and provided feedback
