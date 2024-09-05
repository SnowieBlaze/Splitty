| Key | Value                        |
| --- |------------------------------|
| Date: | 19/03/2024                   |
| Time: | 16:45 - 17:30                |
| Location: | DW PC1 Cubicle 2             |
| Chair | George Kalliakmanis Danassis |
| Minute Taker | Radoslav Georgiev       |
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
- Ensure all team member have completed their assigned tasks and are up-to-date with the project

## Discussion Points (25 min)

1. Admin password authentication should be in server module.
2. Delete template project classes
3. Discuss window flow from the user perspective
4. Language change feature 
5. Integrate config file configurations to the app
6. Discuss non-basic features
7. Distribute tasks for next week 
8. Ask about product presentation (product pitch, slides...) and when is the deadline for the code


## Closing remarks
- Feedback round: What went well and what can be improved next time? (3 min)
- Question round: Does anyone have anything to add before the meeting closes? (3 min)
- Questions to the TA (3 min)
- Closure (1 min)


## Meeting minutes
- Discussing what we did for week 4 and 5:
  - Everyone seems to have done their tasks since the last official meeting(there was some bug fixing in person before the meeting)
  - The progress on the project seems to be on track
- Current encountered issues:
  - Inviting by email does not work yet and therefore also sending remainders about debts
  - We need to add AddExpense controller(has been worked on already)
  - Need admin import/export
  - Make copying the invite code easier
  - Edit title of event feature needs to be implemented
  - We distributed the remaining of the basic requirements
- Discussing next steps: 
  - Deleting template project classes:  
    - We agreed to delete the given classes Quote and Person and their related functionality
    - Finally utilizing our checkstyle for the pipeline after removal
  - Live-language switch:
    - We should make a template for language switch
    - For now most of us agreed to implement the language-switch only on the first page
    - There was a good argument to later implement it on every window
  - Realizing the configuration of the config of the client:
    - language, email(for the send invites functionality)
  - Implementing HTTP long-polling and websockets:
    - We need to implement rest(already done), HTTP long-polling and websockets
    - Everyone agreed for long-polling as the main method
    - Suggested websocket use for client
  - The functionality for edit event should use a textfield instead of button
  - Radoslav suggested we add services in between repositories and controllers(Radoslav will create a template service for one of the controllers by Thursday)
  - Martim suggested we look into Stonk(dependency in SpringBoot/others team use it)
- Distributing tasks:
  - Alex - language-switch feature(with Alexander), ExpenceOverview basic functionality, recently viewed events functionality fix, remove old classes/fix checkstyle
  - Alexander - help with language-switch(with Alex), finishing Event functionality
  - George - email notification non-basic feature, import and export in admin(with Martim), edit event title feature
  - Maria - foreign currency non-basic feature, addExpence basic functionality
  - Radoslav - statistics non-basic feature, OpenDebts non-basic feature finish, Service class example for some endpoint controller
  - Martim - detailed expenses non-basic feature, import and export in admin(with George)
  - All - research for their assigned non-basic requirement and sketch the main functionality, research websockets(there is now a posted self-study)/Stock(dependency in SpringBoot)/Jackson
- General information:
  - We will check progress on the Discord meeting on Thursday
  - The deadline for finishing the project is week 9
  - Exam unavailability TODAY(19/03) - everyone should send an email today if they are unavailable on 16th, 18th or 19th april
  - Next chair - Alex
  - Next minute taker - George
- END
