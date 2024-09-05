## Week 2 agenda & minutes

This is a template agenda. It gives an overview of what could be in your weekly agenda.
In the 'Points of action' part you will also find some topics to cover in/after your first meeting.

---

Date:           {27.02.2024}\
Main focus:     {Wrapping up main classes and next division of tasks}\
Chair:          {Radoslav Georgiev}\
Note taker:     {Maria Burlacu}

# Opening
*Here you check that everybody is present.* ✓

# Approval of the agenda
*Make sure everything that needs to be discussed is in the agenda or add it if something is missing.* ✓

# Points of action
*The items below are things you should look into after the first meeting. During the meeting you can divide (some of) the work between the team members, so that everybody has something to do afterwards.*

- Introduction and check-in(2 min) ✓
- Discussing progress so far:
    - Everyone presents the progress on their task from last week and receives feedback(10 min) ✓
         - Georgiev: OpenAPI specifications (yaml file) + Participant class
         - Martim: Debt Class + Schema design
         - George: UML diagram + utils.Admin class
         - Maria: Issues in Gitlab, division of issues + Expense class
         - Alexander: Event class + schema
         - Alexandru: Config class + checkstyle (fails pipeline for now); readFromFile method should be in Client class

    - Ensuring all tasks of last week are completed and cleaning up remaining branches and merge requests(2 min) ✓
- Discussing management of merge requests(5 min) ✓
    - decided to wait for 2 members' approval and a running pipeline and then merge it
- Finishing words on Code of Conduct(5 min) ✓
    - need to add management of MR there
    - add Merge Conflicts - resolved by two people who created the conflict, if extra opinion is needed discuss online (discord) for fast responses by a third party decision (from the team) + always do things on separate branch then main, after pulling (git pull)
    - need 100 more words
    - everyone reads the Code of Conduct 
    - removed questions ✓
- Reviewing Checkstyle rules(5 min) ✓
    - rule: any if, else makes braces to ensure consistency
    - everyone agreed on it
- Analyzing and dividing tasks for the week(10 min) ✓

# Action points for next week (Scrum board)
*Every week you fill the Scrum board with new action points for that week. See the to do list for the items you should implement.*
- (Remainder) Fill the scrum board after division of tasks

Tasks for next week:
- refactoring by Thursday (from diagram); also Tests - George
- checkstyle fix - Alexandru
- read Code of Conduct + Submission - all
- Controllers & Scenes
     - StartScreen - Alexandru
     - Invitation - Alexandru
     - Overview Event - Alexander
     - Add/Edit Expense - Maria
     - SettleDebts - Georgiev
     - OpenDebts - Georgiev
     - EditParticipant - George
     - Login(utils.Admin) - Martim
     - ManagementOverview(utils.Admin) - Martim
- start working on Server side - all
- discuss database on Thursday - all
- move 1 test for readingFromFile

###
- next chair: Alexander
- minute taker: Martim

# Any other business
*If anybody has something that should be discussed but came up with that after the agenda was finalized (in point 2), he/she should bring that up now so that it can be discussed after all.*

# Questions for the TA
*Your TA will visit you in the second half of the lab session. Note down all questions that you have so that you can ask them then.*

COC:
- overall good
- doesn't count for final grade
- should not spend so much time
- used for self reflection
- we will get feedback after submission of draft

###

- review Git assignments
- check for correct email set

# Question round
*If there are any questions, now is the time to ask them.* 

# Closing
*Now you can start working on the project. Good luck!*