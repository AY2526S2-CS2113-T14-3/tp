# User Guide

## Introduction

UniTasker is a desktop app for managing tasks and courses, optimized for use via a
Command Line Interface (CLI).

## Quick Start

{Give steps to get started quickly}

1. Ensure that you have Java 17 or above installed.
2. Down the latest version of `UniTasker` from [here](http://link.to/duke).
3. Copy the file to the folder you want to use as the home folder for your UniTasker
4. Open a command terminal, `cd` into the folder you put the jar file in, and
use the `java -jar UniTasker.jar` command to run the application
5. Type a command in the command box and press Enter to execute it.

## Features 

{Give detailed description of each feature}

### Adding a todo: `todo`
Adds a new item to the list of todo items.

Format: `todo n/TODO_NAME d/DEADLINE`

* The `DEADLINE` can be in a natural language format.
* The `TODO_NAME` cannot contain punctuation.  

Example of usage: 

`todo n/Write the rest of the User Guide d/next week`

`todo n/Refactor the User Guide to remove passive voice d/13/04/2020`

**Add Command**: `add`

Adds a new item to the list.`add` can be used to add the following: `category`, `todo`, `deadline`, `event`, `recurring`

Format: 

add `category` name or,

add [TASKTYPE] [CATEGORYINDEX] [DESCRIPTION] [DATE]

- TASKTYPE : `todo`, `deadline`, `event`, `recurring`
- CATEGORYINDEX: Integer value up to number of categories added
- DESCRIPTION: What the task is about
- DATE: Date & Time, Date only

Examples:
`add deadline 1 Homework /by 25-05-2026`

**Delete Command**: `delete`
Delete an existing item on the list. `delete` can be used to delete the following: `category`, `todo`, `deadline`, `event`, `recurring`

Format:

delete `category` index or,

delete [TASKTYPE] [CATEGORYINDEX] [TASKINDEX]

- TASKTYPE : `todo`, `deadline`, `event`, `recurring`
- CATEGORYINDEX: Integer value up to number of categories added
- TASKINDEX: What the task is about

*Note*: Use `delete deadline/event categoryIndex all` to delete all deadlines/events in specific category

Examples:

`delete deadline 1 1`
`delete deadline 1 all`


**Mark/Unmark Command**: `mark` `unmark`
Mark an existing item on the list. `mark` can be used to mark the following: `category`, `todo`, `deadline`, `event`, `recurring`

Unmark an existing item on the list. `unmark` can be used to unmark the following: `category`, `todo`, `deadline`, `event`, `recurring`


**List Command**: `list`
Creates a list of task. `list` can be used to crate a list on the following: `category`, `todo`, `deadline`, `event`, `recurring`, `range`

List out all tasks based on key word

Format:

list [KEYWORD][CATEGORYINDEX][START][END][TASKTYPE]

- KEYWORD: `category`,`todo`,`deadline`,`event`,`range`,`recurring`, `limit` 
- CATEGORYINDEX: Integer value up to number of categories added
- START: Start date
- END: End date
- TASKTYPE: `deadline`, `event`

Examples:

`list deadline` `list deadline 1` 

`list limit`

`list range 25-04-2026 27-04-2026`
`list range 25-04-2026 27-04-2026 /deadline`

*Note*: 

- *Start and End are only applicable for Deadline and Event*
- *Add tasktype after end if you want to see only deadline or event*


**Limit Command**: `limit` Sets a limit on the following: task,year,...

Allow user to set the limit for the following: `Task`, `Year`

Format:

limit [KEYWORD] [INT]

- KEYWORD: `task`, `year`
- INT: Integer value

Example:

`limit task 5` 

`limit year 2035`

*Note*: Year refers to the furthest year that can be accessed/added to from the list



## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: Copy all the .txt files inside the folder with UniTasker and paste in the same folder where UniTasker.jar
is located in the other computer.

## Command Summary

{Give a 'cheat sheet' of commands here}

* Add todo `todo n/TODO_NAME d/DEADLINE`
