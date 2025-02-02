| Key | Value                        |
| --- |------------------------------|
| Date: | 02/04/2024                 |
| Time: | 16:45 - 17:30              |
| Location: | DW PC1 Cubicle 2       |
| Chair | Martim Cardeira            |
| Minute Taker | Alexander Oey       |
| Attendees: | Everyone              |

# Agenda Items:

## Opening
- Opening by chair (1 min)
- Check -in: How is everyone doing? (1 min)
- Announcements by the team (3 min)
- Approval of the agenda - Does anyone have any additions? (1 min)
- Approval of last minutes - Did everyone read the minutes from the previous meeting? (1 min)
- Announcements by the TA (3 min)

## Tasks Completion and Team Updates
- Presentation of the current app to TA (10 min)
- Ensure all team member have started their assigned tasks and are up-to-date with the project

## Discussion Points (25 min)

1. Testing: How are testFX and Controller / client & admin test coming along?
2. Bug when adding Participant, has that been fixed?
3. Keyboard shortcuts HCI
4. Language feature progress & UI elements
5. Issues on gitlab, make sure they are up to date
6. Admin-side: deletion of events requires bug-fixing
7. Discuss extra features, are they feasible?
8. General testing of DB
9. Distribute tasks for next week.

## Closing remarks
- Feedback round: What went well and what can be improved next time? (3 min)
- Question round: Does anyone have anything to add before the meeting closes? (3 min)
- Questions to the TA (3 min)
- Closure (1 min)

## Meeting Minutes
### Discussing what we did for week 6:

- Everyone has progressed on their respective tasks

### Current encountered issue:
- Testing controllers encountering issues related to gradle
- TestFX
- JSON import doesn't seem to work with the TA (maybe an issue with the filePath)
- Long-polling related to the EventOverview (automatically switches events on other clients)
- Issues with the file path

### Non - basic requirements progress:
- HCI: more keyboard shortcuts have been added
- UI: now most fxml files are finished, we can start on making everything look nice
- Longpolling: almost done, only need to figure out and fix the issues related to Events

- Detailed expenses: almost done, only needs to look a bit better
- Statistics: not started yet
- Currency change: Works, only needs to be able to fetch currencies from config file
- Language change: Works, plans to add Romanian to the list of languages

### Discussing next steps:
- Starting on styling all the fxml files
- Add more keyboard shortcuts
- Start testing controllers
- Fix the issues listed in the Formative feedback
- Adding a way to see how much each participant owes (in EventOverview)

### Distributing tasks:
- Alex - Add a 3rd language (Romanian), TestFX and fix file path with Alexander, when creating event the creator gets added automatically to the event
- Alexander - Work on adding icons, finish service tests for Participant, add a way to see participants required payment amount in Event Overview
- George - Add tests for admin fxml, fix admin imports (related to expenses), add error handling for server url
- Maria - Work on server utils test, finish getting path for foreign currencies (from config client)
- Radoslav - Testing controllers (2hr max), finish open debts, work on statistics, work on longpolling issue related to events
- Martim - Test admin, changes in EventOverview ui (related to AddExpense)
- Everyone - Add tests, check for bugs/issues

#### General information:
- Next chair - Alex
- Next minute taker - George

### END