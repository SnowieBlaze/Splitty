# Splitty
## Project Overview

First, please navigate in the terminal to the main folder of the project (named Splitty).

### Application Structure
This application consists of three distinct modules:

- Client: Responsible for client-side functionalities.
- Admin: Manages administrative tasks.
- Server: Handles all requests from client and admin instances.

### Running the application
To initiate the server, execute the following command in the terminal
<pre>gradlew.bat bootrun </pre>
To run an instance of the client module, execute the subsequent command:
<pre>gradlew.bat -p client run </pre>
For an admin instance, use the command:
<pre>gradlew.bat -p admin run  </pre>

Upon running the admin module, you will be prompted to generate a password that you can use to login.
This password is randomly generated and can be located within the terminal of the running server instance.

### Using the tag menu:
> The tag menu in its simplicity may have some ambiguity. Here are some useful instructions
> * To open the tag menu press the Tags button in the add expense or edit expense window
> * To select a tag for an expense just choose one from the menu and then press select
> * To add a new tag edit the tag you are currently on and press add - an entirely new tag will be created
> * To edit a tag make changes to the current tag and press edit
> * To delete a tag select the desired tag and press delete

## Debts design choices:
> Here are some design decisions that are implemented for open debts.
> * When you mark a debt as received it is not present in the future(To add it anew you can create an expense between the same people)
> * When you delete a participant all of their related debts are removed
> * If you mark a debt as received but then delete the expenses it originates from - a new debt will be displayed, one that reverses the change and makes sure the net amount of the participants is fair

## Keyboard Shortcuts:
### Client:
> Start screen 
> * Ctrl + W: close window
> * Enter: depending on where the focus is on create an event or join an event

> Invitation
> * Ctrl + W: close window
> * Enter: send invites
> * Esc: go back to start screen

> User Settings
> * Ctrl + W: close window
> * Ctrl + S: save changes
> * Tab: go to next field
> * Esc: go back to start screen (changes are not saved)

> Event Overview 
> * Ctrl + W: close window
> * Esc: go back to start screen

> Add participant
> * Ctrl + W: close window
> * Ctrl + S: save changes
> * Tab: go to next field
> * Esc: go back to Event Overview (changes are not saved)

> Edit participant
> * Ctrl + W: close window
> * Ctrl + S: save changes
> * Tab: go to next field
> * Esc: go back to Event Overview (changes are not saved)

> Add Expense
> * Ctrl + W: close window
> * Ctrl + S: save changes
> * Tab: go to next field
> * Esc: go back to Event Overview (changes are not saved)

> Edit Expense
> * Ctrl + W: close window
> * Ctrl + S: save changes
> * Tab: go to next field
> * Esc: go back to Event Overview (changes are not saved)

> Open Debts
> * Ctrl + W: close window
> * Esc: go back to Event Overview 

> Statistics
> * Ctrl + W: close window
> * Esc: go back to Event Overview 

> Tags
> * Ctrl + W: close window
> * Esc: go back to Event Overview 

### Admin:

> Login
> * Ctrl + W: close window
> * Enter: login  

> Overview
> * Ctrl + W: close window
> * Ctrl + R: refresh events' table
> * Ctrl + UP: select previous event
> * Ctrl + DOWN: select next event