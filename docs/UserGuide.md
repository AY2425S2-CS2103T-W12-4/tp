---
layout: page
title: User Guide
---

TAssist is a **desktop application** for Teaching Assistants (TAs) to easily track and manage student information. It is optimized for use via a **Command Line Interface** (CLI), complemented by a user-friendly Graphical User Interface (GUI). If you can type fast, TAssist helps you complete student management tasks more quickly than traditional GUI-based apps.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------
## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

2. Download the latest `.jar` file from [here](https://github.com/AY2425S2-CS2103T-W12-4/tp/releases).

3. Copy the file to a folder you want to use as the _home folder_ for TAssist.

4. Open the Command Prompt (Windows) or Terminal (Mac/Linux)

5. Navigate to the folder where you saved the TAssist1.3.jar file using `cd` command<br>
   Example (Windows): `cd C:\Users\YourName\Downloads`. <br>
   Example (Mac/Linux): `cd /Users/YourName/Downloads`.

6. Type this command and press enter to run the application.<br>
    ```java -jar TAssist1.3jar```

7. A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png) 
 
8. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
    Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com s/A0135246R` : Adds a contact named `John Doe` to the student list.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

9. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------
## Tips

1. TAssist allows users to personalize the appearance of the application by switching between different color themes.<br>
   Users can press the `F2` key to cycle through the available themes. <br>
   Available themes: Dark, Bright, Pink

2. Viewing Upcoming Events Calendar: `F3`
   TAssist provides a calendar-style view to help you visualize upcoming assignments and timed events. Events are grouped and displayed by their due dates, along with the list of students assigned to each.
    * Press the F3 key to open the calendar-style event viewer.
    * Display includes: Assignment names, Event type (e.g., assignment), Assigned students, Dates grouped chronologically
    * An empty calendar will be shown if there are no current Assignments.

--------------------------------------------------------------------------------------------------------------------


## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Opens the User Guide in your default browser and displays a message containing the link. <br>
If the browser does not open automatically, you can manually copy and paste the link into your browser.
![help message](images/helpMessage.png)

Format: `help`

### Adding a student: `add`

Adds a student to the student list.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL s/STUDENT_ID [g/GITHUB_URL] [pt/TEAM] [c/CLASS_NUMBER] [t/TAG]…​ [pr/PROGRESS]`

<div markdown="span" class="alert alert-primary">
:bulb: <strong>Tip:</strong> A student can have any number of tags (including 0)
</div>

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com s/A0000000B pt/ProjectTeam1 c/T01 t/friends t/owesMoney pr/50`
* `add n/Betsy Crowe t/friend e/betsycrowe@example.com s/A0123456U g/https://github.com/betsy p/1234567 t/LifeScienceMajor`

### Listing all students : `list`

Shows a list of all students in the student list.

Format: `list [f/FILTER_TYPE fv/FILTER_VALUE] [s/SORT_TYPE o/SORT_ORDER]` <br>
All parameters are optional. Filters and sorting can be used together or independently.

#### Filter Options
`FILTER_TYPE:`<br>
* progress: Filters students whose progress is less than or equal to the provided value. 
* team: Filters by existing team names.
* course: (Not yet implemented) Will filter by existing course codes.

`FILTER_VALUE:`<br>
* PROGRESS: an integer between 0 and 100.
* TEAM: must match an existing team name. 
* COURSE: (Not yet implemented) must match an existing course value.

#### Sort Options
`SORT_TYPE:`<br>
* name
* progress
* github

`SORT_ORDER:`<br>
* asc — Ascending (A → Z or lowest → highest). 
* des — Descending (Z → A or highest → lowest).

Examples:
* `list`<br>
  Displays all students.
* `list f/progress fv/60` <br>
  Displays students with progress ≤ 60.
* `list s/github o/asc`<br>
  Displays all students, sorted by Github username in ascending lexicographical order.
* `list f/progress fv/50 s/name o/des`<br>
  Displays students with progress ≤ 50, sorted by name in descending lexicographical order.
* `list f/team fv/Bang Bang`<br>
  Displays students with the Team name 'Bang Bang', matched case-insensitively.

### Editing a student : `edit`

Edits an existing student in the student list.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [c/_NUMBER] [s/STUDENTID] [g/GITHUB_URL] [pt/TEAM] [c/CLASS_NUMBER] [t/TAG]…​ [pr/PROGRESS]`

* Edits the student at the specified `INDEX`. The index refers to the index number shown in the displayed student list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the valid input values.
* When editing tags, the existing tags of the student will be removed i.e adding of tags is not cumulative.
* You can remove all the student’s tags by typing `t/` without
    specifying any tags after it.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com`<br>
    Edits the phone number and email address of the 1st student to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` <br>
    Edits the name of the 2nd student to be `Betsy Crower` and clears all existing tags.

### Locating students by name: `find`

Finds students whose names contain any of the given keywords, or whose student ID matches exactly.

Format: `find NAME [MORE_NAMES]` or `find STUDENT_ID`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the names does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only full words will be matched e.g. `Han` will not match `Hans`
* Students matching at least one name will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`
* If a valid student ID is entered (e.g. `A1234567B`), it will return the student with an exact match on that ID.

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find john alice'](images/UserGuideFindCommand1.png)
* `find A1234567B` returns the student with that exact student ID

### Assigning or Removing a tutorial class: `class`

Assigns or removes a **tutorial/recitation class** for a student identified by either their displayed index or student ID

Format: `class INDEX c/CLASS_NUMBER` or `class STUDENT_ID c/CLASS_NUMBER`

* Must be either `Txx`or `Rxx` where xx is integer from 01 to 99 (e.g., T01, T15, R05, R99)
    * Note: 'T' and 'R' must be uppercase.
    * 'T' and 'R' represent Tutorial and Recitation respectively.
* Assigns or updates the class of the student at the specified `INDEX` or matching `STUDENT_ID`
* The index refers to the index number shown in the displayed student list.
* To remove a class assignment, leave `c/` empty (i.e. `c/` with no value)
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `class 1 c/T01` <br>
   Assigns class T01 to the 1st student in the list.
* `class 2 c/` <br>
   Removes the class from the 2nd student.
* `class A1234567B c/R05`<br>
   Assigns class R05 to the student with student ID `A1234567B`.

### Editing a student GitHub Link: `github`

Updates the GitHub URL of a student, identified by either their displayed index or student ID.

Format: `github INDEX g/GITHUB_URL` or `github STUDENT_ID g/GITHUB_URL`

* Edits the `GITHUB_URL` of the student at the specified `INDEX` or matching `STUDENT_ID`.
* The index refers to the index number shown in the displayed student list.
* The index **must be a positive integer** 1, 2, 3, …​
* The GitHub URL must be a valid URL (e.g. starts with `https://github.com/`)

Examples:
* `github 2 g/https://github.com/alice` <br>
  Updates the 2nd student's GitHub link to `https://github.com/alice`.
* `github A1234567B g/https://github.com/bob-dev`
  Updates the GitHub link for the student with student ID `A1234567B`.

### Opening a student's GitHub page: `open`

Opens the GitHub page of a student, identified by either their displayed **index** or **student ID**, in your default web browser.

Format: `open INDEX` or `open STUDENT_ID`

* Opens the `GITHUB_URL` of the student at the specified `INDEX` or matching `STUDENT_ID`.
* The index refers to the index number shown in the displayed student list.
* The index **must be a positive integer** 1, 2, 3, …​

Example:
* `list` followed by `open 1` opens the GitHub page of the first student shown in the list.
* `open A1234567B` 
  Opens the GitHub page of the student with student ID `A1234567B`.

### Adding a new assignment/timed event: `assignment`

Adds a timed event (such as an assignment, project, or deadline) to the event list, which can be used to track important upcoming dates.

Format: `assignment n/NAME d/DATE`

* `n/NAME`: The title or description of the assignment/event.
* `d/DATE`: The deadline for the assignment/event.
* Accepted date formats: `dd-MM-yyyy`, `dd-MM-yy`, or `dd-MM` (defaults to current year)
* The date must be a valid future date.

Example: 
* `assignment n/CS2103T Project d/30-01-2025` <br>
  Adds a timed event named "CS2103T Project" with deadline on January 30, 2025.
* `assignment n/Quiz 1 d/10-04` <br>
  Adds an event named "Quiz 1" with the deadline on April 10 of the current year.

### Viewing the timed event list: `view`

Lists all timed events in the system.

Format: `view`

### Assigning a timed event/assignment: `assign`

Assigns a timed event using index in time event list to one or more students identified by their displayed index, student ID, or class number.

Format: `assign TIMED_EVENT_INDEX STUDENT_INDEX` or `assign TIMED_EVENT_INDEX STUDENT_ID` or `assign TIMED_EVENT_INDEX CLASS_NUMBER`  

* `TIMED_EVENT_INDEX`: The index of the timed event shown in the timed event list (must be a positive integer).
* `STUDENT_INDEX`: The index of the student from the displayed student list (must be a positive integer).

Examples:
* `assign 1 2`
  Assigns the first timed event to the 2nd student in the list.

* `assign 2 A1234567B`
  Assigns the second timed event to the student with student ID A1234567B.

* `assign 1 T01`
  Assigns the first timed event to all students in class T01.

### Unassigning and Removing a Timed Event: `unassign`

Removes a timed event from all assigned students and deletes the event from the timed event list.

Format: `unassign TIMED_EVENT_INDEX`

* `TIMED_EVENT_INDEX`: The index of the timed event shown in the timed event list (must be a positive integer).
* Unassigns the specified timed event from all students who were previously assigned to it.
* Removes the timed event entirely from the list.

Example:
* `unassign 1`
  Unassigns the first timed event from all students and deletes the event from the list.

### Deleting a student : `delete`

Deletes the specified student from the student list.

Format: `delete INDEX`

* Deletes the student at the specified `INDEX`, with a confirmation step to prevent accidental deletions.
* The index refers to the index number shown in the displayed student list.
* The index **must be a positive integer** 1, 2, 3, …​
* After entering the command you will be prompted to confirm the deletion by typing:
  * Y to confirm
  * N to cancel
  * Anything else prompts: `Invalid response. Please enter Y/N.`

Examples:
* `list` followed by `delete 2` deletes the 2nd student from the list after confirmation.
* `find Betsy` followed by `delete 1` deletes the 1st student in the search results after confirmation.

### Clearing all entries : `clear`

Clears all entries from the student list.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

TAssist data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

TAssist data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, TAssist will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause TAssist to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v1.5]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous TAssist home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME p/PHONE_NUMBER e/EMAIL s/STUDENT_ID [g/GITHUB_URL] [pt/TEAM] [c/CLASS_NUMBER] [t/TAG]…​ [pr/PROGRESS]` <br> e.g., `add n/John Doe p/98765432 e/johnd@example.com s/A0000000B g/https://github.com/username c/T02 t/friends t/owesMoney pr/50`
**Clear** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit** | `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [s/STUDENTID] [g/GITHUB_URL] [pt/TEAM] [c/CLASS_NUMBER] [t/TAG]…​ [pr/PROGRESS]`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**Find** | `find KEYWORD [MORE_KEYWORDS]` or `find STUDENT_ID` <br> e.g., `find James Jake`, `find A1234567B`
**List** | `list [f/FILTER_TYPE fv/FILTER_VALUE] [s/SORT_TYPE o/SORT_ORDER]`<br> e.g.,`list f/progress fv/50 s/name o/des`
**Class** | `class INDEX c/CLASS_NUMBER` or `class STUDENT_ID c/CLASS_NUMBER` <br> e.g.,`class 1 c/T01`, `class A7654321B c/T02`
**Github** | `github INDEX g/GITHUB_URL` or `github STUDENT_ID g/GITHUB_URL` <br> e.g.,`github 2 g/https://github.com/alice`, `github A1234567B g/https://github.com/alice`
**Open** | `open INDEX` or `open STUDENT_ID` <br> e.g., `open 3`, `open A7654321B`
**Assignment** | `assignment n/NAME d/DATE` <br> e.g.,`assignment n/CS2103T Project d/30-01-2025`
**View** | `view`
**Assign** | `assign TIMED_EVENT_INDEX STUDENT_INDEX` or `assign TIMED_EVENT_INDEX STUDENT_ID` or `assign TIMED_EVENT_INDEX CLASS_NUMBER` <br> e.g., `assign 1 2`, `assign 2 A1234567B`,`assign 2 T03`
**Unassign** | `unassign TIMED_EVENT_INDEX` <br> e.g., `unassign 1`
**Help** | `help`
