| Key | Value                        |
| --- |------------------------------|
| Date: | 09/04/2024                 |
| Time: | 16:45 - 17:30              |
| Location: | DW PC1 Cubicle 2       |
| Chair | Alexandru Theodor Tarcuta  |
| Minute Taker | George Kalliakmanis Danassis     |
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
2. Any bugs that we still need to fix?
3. Talk about and agree about a day to stop adding content and just focus on polishing(can even be today). Can we look to finish/polish an extra feature?
4. Ask TA about thoughts and what we could still change/improve.
5. Are we SURE all basic functionalities are met? Otherwise it can lead to failing although app is overall good.
6. Let's start looking over ALL the rubrics and try to see what we have/miss. Maybe we can easily finish up something that can gain extra points.
7. Lastly, are we happy with the (almost) final state of the app?

## Closing remarks
- Feedback round: What went well (3 min)
- Question round: Does anyone have anything to add before the meeting closes? (3 min)
- Questions to the TA (3 min)
- Thanks to everybody for this, since it's our last 'official' meeting.
- Closure (1 min)

## Meeting Minutes
### Discussing what we did for week 8:

- Everyone has progressed on their respective tasks

### Current encountered issue:
- Admin import of events does not work when event has debts and expenses
- Delete of a participant does not work
- Issue in the admin testing of web functionality. There is a mismatch in response type
- Tests are at 60% line coverage should be improved
- Event overview is very slow, because we are constantly call the currency API
- When you select a tag, the participant is reset to the first one
- How are we managing expenses with different currencies?
- tags and statistics windows do not have keyboard navigation

### Non - basic requirements progress:
- Email functionality should be done
- Live language switch should be done
- Detailed Expenses: should be done, a bit of UI part is missing
- Foreign currency:  We are not caching in a local file the exchange rates used. api key issue remains an issues
- Open Debts: should be done
- Statistics: should be done

### Discussing next steps:
- Update README file
- Finish tests (ServerUtils, admin controllers, client controllers, server controllers)
- Fix admin
- Improve how we fetch the currencies
- We should try to finish everything by at latest friday, so that we can add the tests by Sunday

### Distributing tasks:
- Alex - Admin testing, finish client tests, help Maria with serverUtils tests, finish translation of elements
- Alexander - Add more icons in the buttons, and beautify the windows
- George - FIX ADMIN IMPORT, email valid before saving
  and add more informative errors for email
- Maria - Implement better the foreign currencies
- Radoslav - Testing of the server side
- Martim - Make UI adaptive, add keyboard navigation in tags and statistics(also add these in the README file)
- Everyone - Test the application to find errors.


### END

