| Key | Value |
| --- | --- |
| Date: | 5/03/2024 |
| Time: | 16:45 - 17:30 |
| Location: | DW PC1 Cubicle 2 |
| Chair | Alexander Oey |
| Minute Taker | Martim Lopes Cardeira |
| Attendees: | Everyone |

Agenda Items:

- Opening by chair (1 min) ✓
- Check -in: How is everyone doing? (1 min) ✓
    - Everyone was present
    - Everybody was making good progress and on track
- Announcements by the team (5 min)
    - No announcements of importance, anything urgent was dealt with before the meeting
- Approval of the agenda - Does anyone have any additions? (1 min)
    - The agenda was approved by everyone
- Approval of last minutes - Did everyone read the minutes from the previous meeting? (1 min)
    - Everyone read the minutes from last week and was satisfied with what was noted


- Announcements by the TA (2 min)
    - Buddycheck is due on the 8th of March this friday. REMINDER for all to complete it (formative assesment)

- Presentation of the current app to TA (12 min)
    - Each member presented their work from the previous week and everyone demonstrated that they were on track


- Talking Points: (Inform/ brainstorm/ decision making/ discuss)
  - Edits on existing classes (5 min)
    - We agreed that Event to Participant relationship is one-to-many, a relationship that will need to be denoted in JPA
    - Agreed on storing Debts as a relation on the database instead of them being interpreted by the client app (decreases complexity of program at the cost of performance). In other words debts are persistent
    - IMPORTANT: All of our classes in commons require an empty constructor so that they can be used by SpringBoot properly, and there should be no getters nor setters for the ID in any of the commons classes (e.g. Participant, Expense, Event, etc.)
    - REMINDER: "Add Expenses" window is missing, that must be implemented soon
    - JPA on all common classes should be finished this week
  - Controller classes (5 min)
    - It was agreed that we have to have 4 controllers:
        - Events
        - Expenses
        - Participants
        - Debts
    - We will work on those 4 controllers and code all the needed endpoints and then generate the YAML that corresponds to our endpoints
  - Client / Admin details (5 min)
    - Data regarding "recently viewed events" should be stored in the config class of the user, in other words it is persistent locally in the config file
    - For the admin JSON dump/import functionality, the dump should be an endpoint in the api. It will be a simple get request that returns a JSON response that contains 4 arrays: one for Participant, Event, Expense and Debt
  - GUI (5 min)
    - Need to figure out in terms of our wireframe, which buttons take us where in the GUI, this can be done in the meeting this Thursday
  - General Remarks (5 min)
    - Milestones and issues are outdated, discuss them on discord, our goal is to do them by this Thursday either in or just after our meeting
    - We agreed to try(!) and achieve functionality by week 6, meaning all basic requirements are met
    - Language should be the last basic task that we complete
    - Focus on the client working first, then move onto the admin functionality.
  - Next meeting(s) (5 min)
    - It was agreed by all that we will meet this Thursday at 3pm online on Discord as per usual, we will also meet on Thursday on the week of the midterms, with the time or physical/online to be decided     
- Summarize action points: Who , what , when? (5 min)
    - Individual tasks
        - Create controller classes in the server, this week
            - Expenses: Martim and Maria
            - Events: The two Alex's
            - Debts: Radoslav
            - Participants: George
    - Shared tasks
        - Update issues on gitlab, this Thursday
        - Finish JPA annotations on all common classes (including Debt class), this week
        - Sort out empty constructors, remove getters and setters for ID on all common classes, this week
        - Create Add Expense window, fxml & controller, this week
        - Sort out the wireframe and how the GUI will work in terms of accessing different scenes through buttons, this Thursday


- Feedback round: What went well and what can be improved next time? (5 min)
    - Overall everyone contributed a lot to this meeting and we managed to cover a lot of different aspects of the project which is nice and gives us a good idea of where we are in relation to the finished product
    - Perhaps we can use those last 10 minutes of silence to actually get a little bit of work done
- Planned meeting duration != actual duration? Where/why did you mis -estimate? (1 min)
    - The meeting was slightly shorter than expected, also because there wasn't much in the agenda. As was suggested we can use that time to work
- Question round: Does anyone have anything to add before the meeting closes? (1 min)
    - There weren't any further remarks or questions
- Closure (1 min) 
    - Next meeting
        - Chair: George
        - Minute taker: Radoslav

