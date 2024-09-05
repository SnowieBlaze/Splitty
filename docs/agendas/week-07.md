| Key | Value                        |
| --- |------------------------------|
| Date: | 26/03/2024                   |
| Time: | 16:45 - 17:30                |
| Location: | DW PC1 Cubicle 2             |
| Chair | Alexandru Theodor Tarcuta |
| Minute Taker | George Kalliakmanis Danassis       |
| Attendees: | Everyone                     |

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

1. Did we manage to create services/web-sockets? long-polling?
2. Discuss connecting to different servers
3. Are there any basic functionalities that are not fully completed? Do a read through.
4. Discuss about testing. We only have 20-30%. It's 1 point of our grade.
5. Product pitch draft.
6. How are extra features coming along? Any unexpected difficulties?
7. Distribute tasks for next week.

## Closing remarks
- Feedback round: What went well and what can be improved next time? (3 min)
- Question round: Does anyone have anything to add before the meeting closes? (3 min)
- Questions to the TA (3 min)
1. Is testing throughout the project relevant for final grade, or is having high percentage at later weeks good enough?
2. Thoughts about progress and grading rubrics.
- Closure (1 min)

## Meeting Minutes
### Discussing what we did for week 4 and 5:

- Everyone seemed to have done their tasks, discussed in the previous meeting.
- The project has progressed since last meeting.

### Current encountered issues:
- Expenses are not persisted/added in the database.
- We do not implement websockets and long polling(requirement)
- If you add something to the frontend - fxmls add an id and add them in the language
- There are issues which should be closed
- There are unused branches

### Non - Basic Requirements progress
- Detailed expenses: cannot be implemented yet since it requires working expenses
- Email: send invite codes is done. Send payment instructions needs to be done
- Statistics: not started yet
- Currency change: Maria is researching on it
- Language change: seems to work but needs polishing

### Discussing next steps:
- We should try to write more tests in general - every merge request should add to the test coverage
- Add keyboard shortcuts in README file
- We should implement websockets in admin
- We should implement long polling for some of the endpoints in client
- Handle multiple clients at the same time
- Connect to different server for client / on startup the user should be asked which server they want to connect
- Fix the add expense
- Think about product pitch
- HCI - discuss style issues (error handling and icons)

### Distributing tasks:
- Alex - Test the frontend FXML classes
- Alexander - Create a draft product pitch, finish event overview window, check Dutch language template
- George - Change server url for client, add the keyboard shortcuts in README
- Maria - Fix the add expense
- Radoslav - Implement an example template for long polling for client, test server utils, Statistics
- Martim - Implement an example for websockets for admin, Detailed expenses
- Everyone should write tests for their Service class, write issues (with time estimate)

#### General information:
- Next chair - Martim
- Next minute taker - Alexander

### END