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

