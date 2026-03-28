# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

This project was built from scratch by the UniTasker team. The following thrid-party libraries and references were consulted during development:
- Java SE 17 Standard Library
- JUnit 5
- PlantUML
- SE-EDU AddressBook-Level3

## Product scope

### Target user profile

UniTasker is designed for university students who need to manage multiple courses, assignments, deadlines, and personal tasks simultaneously. These users often:
- juggle academic responsibilities across different modules, each with varying deadlines, 
priorities, and schedules. 
- They require a simple and efficient system to organise their tasks,
keep track of coursework, and stay on top of deadlines.
- prefer a fast, keyboard-driven interface over GUI-heavy applications

### Value proposition

University students often struggle to keep track of tasks and course assessments across different 
platforms such as learning portals, calendars and notes. This fragmented approach leads
to missed deadlines, poor prioritisation, and unnecessary stress. 

UniTasker provides a centralized 
task management solution that consolidates todos, deadlines, events and course information into a 
single platform. Through a simple command-line interface, it allows students to efficiently organise, 
update, and review their tasks and assessments. This helps students to stay on top of their workload
and focus on completing their academic responsibilities.

## Design & implementation

{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}

This section describes the design and implementation of the key components of UniTasker. UML diagrams are provided for highlighted classes and interaction flows

{Add an architecture overview UML + description}

### Deadline Class Diagram

The diagram below shows the Deadline class and its relationships with the calendar, TaskList hierarchy, DeadlineList, and the DateUtils utility class.

![Alt text for UML Diagram](images/deadlineClassDiagram.png)
 *<div align="center"> Figure x - Deadline Class Diagram </div>*


**Key Design Decisions**

### DateUtils.parse() - Sequence Diagram
The following sequence diagram illustrates how a date string entered by the user is parsed, validated, and return as a LocalDateTime. This flow is triggered whenever a timed task, a deadline or event, is added.

![Alt text for UML Diagram](images/DateUtilsSequence.png)
*<div align="center"> Figure x - DateUtils: parse() Sequence Diagram </div>*

Parsing Flow Summary?

**Implementation Note - isLoading Flag**

The IsLoading parameter is set to true when DateUtils.parse() is called from the storage layer (file loading), and false during user input. This allows tasks saved in a previous session to be restored if their dates have now passed, while preventing the user form directly scheduling past tasks interactively.

### TaskValidator Sequence Diagram

Before any task (Todo, Deadline, Event) is added to the system, the AddCommand invokes three sequential validation passes via TaskValidator. The diagram below shows the full interaction.

![Alt text for UML Diagram](images/TaskValidatorSequence.png)
*<div align="center"> Figure x - Task Validator Sequence Diagram </div>*



## User Stories

| Version | As a ...           | I want to ...                                                              | So that I can ...                                                              |
|---------|--------------------|----------------------------------------------------------------------------|--------------------------------------------------------------------------------|
| v1.0    | University Student | create categories for each of my courses                                   | organise my tasks by module                                                    |
| v1.0    | University Student | view a specific category                                                   | focus on tasks related to a single course                                      |
| v1.0    | University Student | assign priority levels to todos in a category                              | identify important todos easily                                                |
| v1.0    | University Student | sort todos within a category by priority                                   | focus on high-priority todos first                                             |
| v1.0    | University Student | track all tasks with a due date                                            | keep track of all my deadlines                                                 |
| v1.0    | University Student | arrange tasks which occur or are due within a certain time period          | prioritise tasks that are due earlier                                          |
| v1.0    | University Student | delete all deadlines within a category                                     | quickly remove deadlines in a category                                         | 
| v1.0    | University Student | have my deadlines sorted by earliest date                                  | easily identify earliest due deadline                                          |
| v1.0    | University Student | track all tasks with a start date and time and end date and time           | keep track of all my events                                                    |
| v1.0    | University Student | have my events sorted by earliest date                                     | easily identify events that happen earliest                                    |
| v1.0    | University Student | track all recurring tasks with a start date and time and end date and time | keep track of all my recurring events                                          |
| v2.0    | University Student | delete all marked tasks                                                    | quickly clean up completed work across categories                              |
| v2.0    | University Student | search for tasks across all categories                                     | quickly find relevant tasks                                                    |
| v2.0    | University Student | customize the maximum tasks permitted per day                              | schedule my tasks without burning myself out                                   |
| v2.0    | University Student | customize the year of my schedule                                          | plan beyond my acedemic years                                                  |
| v2.0    | University Student | customise the duration to add a certain recurring event                    | adjust it based on the event                                                   |
| v2.0    | University Student | have reminders for events and deadlines coming soon                        | plan my time accordingly to complete/attend them                               |
| v2.0    | University Student | have different views of events                                             | so that it is clearer to disntinguish the important ones without having others |


## Non-Functional Requirements

{Give non-functional requirements}

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
