# Elyra User Guide

Elyra is a desktop task manager chatbot that lets you track todos, deadlines, and events using short commands in a chat-style UI.

![Elyra UI](Ui.png)

## Table of contents

- [Quick start](#quick-start)
- [Features](#features)
- [Data storage](#data-storage)
- [Command summary](#command-summary)

## Quick start

1. Ensure you have JDK 17 installed.
2. If you have `Elyra.jar`, run `java -jar Elyra.jar` from the folder that contains it.
3. Otherwise, from the project root, run `.\gradlew run` (Windows) or `./gradlew run` (macOS/Linux).
4. Type a command into the input box and press Enter.

## Features

### Notes on Command format

- Commands are case-insensitive. `LIST`, `List`, and `list` all work.
- Words in `UPPER_CASE` are parameters you provide.
- Task indexes are 1-based and come from the list shown by `list`.
- Date/time must be `yyyy-MM-dd HH:mm` (24-hour), e.g. `2026-02-15 23:59`.
- `find` uses the full text after `find` as one keyword and does a case-insensitive substring match.
- `update` supports these fields: `description` (all tasks), `by` (deadlines), `from`/`to` (events).
- Tasks are shown with tags: `[T]` todo, `[D]` deadline, `[E]` event. Done tasks show `[X]`.

### Add a todo (todo)

Adds a task without date/time.

**Format:** `todo DESCRIPTION`  
**Example:** `todo read CS2103 notes`

### Add a deadline (deadline)

Adds a task with a due date and time.

**Format:** `deadline DESCRIPTION /by yyyy-MM-dd HH:mm`  
**Example:** `deadline iP submission /by 2026-02-15 23:59`

### Add an event (event)

Adds a task with a start and end date/time.

**Format:** `event DESCRIPTION /from yyyy-MM-dd HH:mm /to yyyy-MM-dd HH:mm`  
**Example:** `event project meeting /from 2026-02-20 14:00 /to 2026-02-20 15:30`

### List tasks (list)

Shows all tasks.

**Format:** `list`

### Mark a task as done (mark)

**Format:** `mark INDEX`  
**Example:** `mark 2`

### Mark a task as not done (unmark)

**Format:** `unmark INDEX`  
**Example:** `unmark 2`

### Delete a task (delete)

Removes a task from your list.

**Format:** `delete INDEX`  
**Example:** `delete 3`

### Find tasks by keyword (find)

Finds tasks whose descriptions contain the keyword (case-sensitive).

**Format:** `find KEYWORD`  
**Example:** `find tutorial`

### Update a task (update)

Updates a field in a task.

**Format:** `update INDEX /field FIELD /with VALUE`  
**Examples:** `update 1 /field description /with revise lecture 3`, `update 2 /field by /with 2026-02-16 18:00`,
`update 3 /field from /with 2026-02-20 13:30`

### Exit the app (bye)

Closes Elyra.

**Format:** `bye`

## Data storage

- Your tasks are saved automatically after any command that changes data.
- The data file is `data/elyra.txt` relative to the app folder.
- If the file is corrupted, Elyra starts with an empty task list and shows an error message. However, you may always exit Elyra to re-examine your data file.

## Command summary

| Action | Format | Example |
| --- | --- | --- |
| Add todo | `todo DESCRIPTION` | `todo read CS2103 notes` |
| Add deadline | `deadline DESCRIPTION /by yyyy-MM-dd HH:mm` | `deadline iP submission /by 2026-02-15 23:59` |
| Add event | `event DESCRIPTION /from yyyy-MM-dd HH:mm /to yyyy-MM-dd HH:mm` | `event project meeting /from 2026-02-20 14:00 /to 2026-02-20 15:30` |
| List | `list` | `list` |
| Mark | `mark INDEX` | `mark 2` |
| Unmark | `unmark INDEX` | `unmark 2` |
| Delete | `delete INDEX` | `delete 3` |
| Find | `find KEYWORD` | `find tutorial` |
| Update | `update INDEX /field FIELD /with VALUE` | `update 1 /field description /with revise lecture 3` |
| Exit | `bye` | `bye` |
