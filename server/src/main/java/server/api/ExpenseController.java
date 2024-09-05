package server.api;

import commons.Expense;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import server.Services.ExpenseService;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private ExpenseService expenseService;

    /**
     * Constructor for the controller
     * @param expenseService - the expense service for the expense repository
     */
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    /**
     * Get all expenses
     * @return all expenses
     */
    @GetMapping(path = {"", "/"})
    public List<Expense> getAll() {
        return expenseService.getAll();
    }


    /*TODO a method that returns all expenses of a specific event
     * method that adds a new expense in a specific event
     * method that updates an expense
     */

    /**
     * Get all expenses of a specific event
     * @param eventId of event
     * @return all expenses
     */
    @GetMapping(path = "/event/{eventId}")
    public List<Expense> getAllEvent(@PathVariable("eventId") long eventId) {
        return expenseService.findByEventId(eventId);
    }

    /**
     * Get expense
     * @param id
     * @return all expenses
     */
    @GetMapping(path = "/id/{id}")
    public ResponseEntity<Expense> getExpense(@PathVariable("id") long id) {
        if (id >= 0 && expenseService.findById(id).isPresent()){
            return ResponseEntity.ok(expenseService.findById(id).get());
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Mapping to create an expense
     * @param expense the expense to add to database
     * @return Response when expense created
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expense){
        Expense savedExpense = expenseService.create(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExpense);
    }

    /**
     * Mapping to create an expense
     * @param eventId of the event where the new expense is added
     * @param expense the expense to add to database
     * @return Response when expense created
     */
    @PostMapping(path = "/event/{eventId}")
    public ResponseEntity<Expense> createEventExpense(@PathVariable("eventId") long eventId,
                                                @RequestBody Expense expense){
        Expense savedExpense = expenseService.create(eventId,expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExpense);
    }

    /**
     * Method to update an expense
     * @param eventId of expense
     * @param updatedExpense the changed expense
     * @return response when expense updated or not found
     */
    @PutMapping(path = {"", "/event/{eventId}"})
    public ResponseEntity<?> updateEventExpense(@PathVariable("eventId") long eventId,
                                                @RequestBody Expense updatedExpense) {
        //long expenseId = updatedExpense.getId();

        Expense existingExpense = expenseService.updateEvent(eventId, updatedExpense);
        if (existingExpense != null) {
            return ResponseEntity.ok(existingExpense);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense not found");
    }

    /**
     * Method to update an expense
     * @param id of expense
     * @param updatedExpense the changed expense
     * @return response when expense updated or not found
     */
    @PutMapping(path = {"", "/id/{id}"})
    public ResponseEntity<?> update(@PathVariable("id") long id,
                                    @RequestBody Expense updatedExpense) {
        //long expenseId = updatedExpense.getId();

        Expense existingExpense = expenseService.update(id, updatedExpense);
        if (existingExpense != null) {
            return ResponseEntity.ok(existingExpense);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense not found");
    }

    /**
     * Method to update an expense
     * @param eventId of expense
     * @param updatedExpense the changed expense
     * @return response when expense updated or not found
     */
    @PutMapping(path = {"", "/remove/{eventId}"})
    public ResponseEntity<?> remove(@PathVariable("eventId") long eventId,
                                    @RequestBody Expense updatedExpense) {
        //long expenseId = updatedExpense.getId();

        boolean del = expenseService.deleteByEventId(eventId, updatedExpense);
        expenseService.flush();
        if (del) {
            return ResponseEntity.ok("Deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense not found");
    }

    /**
     * Deletes an expense by id if it exists
     * @param id - the expense id of the expense we search for
     * @return "Successful delete" response
     * if delete was successful, "Bad request" otherwise
     */
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if (!expenseService.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Expense expense = expenseService.findById(id).get();
        expenseService.deleteById(id);
        expenseService.flush();
        return ResponseEntity.ok().body("Successful delete");
    }

    /**
     * Get exchange rate of expense
     * @param from starting (base) currency
     * @param to new currency
     * @param date of expense
     * @return rate
     */
    @GetMapping(path = { "/date/{date}/rate/{from}/{to}"})
    public Double getRate(@PathVariable String from,
                          @PathVariable String to, @PathVariable String date) {
        String url = "http://data.fixer.io/api/";
        String key = "488b2c548074f3e5d9e15ba3013a152d";
        if(date != null && date.length() == 10){
            url += date;
        }
        else{
            url += "latest";
        }
        url += "?access_key=" + key +"&base=" + from + "&symbols=" + to;
        RestTemplate restTemplate = new RestTemplate();
        Object res = restTemplate.getForObject(url, Object.class);
        String rate = res.toString().split(to+"=")[1].split("}")[0];
        return Double.parseDouble(rate);
    }

    /**
     * Get exchange rate of expense
     * @param from starting (base) currency
     * @param to new currency
     * @return rate
     */
    @GetMapping(path = { "/rate/{from}/{to}", "/rate/{from}/{to}/"})
    public Double getRateNow(@PathVariable String from, @PathVariable String to) {
        String url = "http://data.fixer.io/api/latest";
        String key = "488b2c548074f3e5d9e15ba3013a152d";
        url += "?access_key=" + key +"&base=" + from + "&symbols=" + to;
        RestTemplate restTemplate = new RestTemplate();
        Object res = restTemplate.getForObject(url, Object.class);
        String rate = res.toString().split(to+"=")[1].split("}")[0];
        return Double.parseDouble(rate);
    }

    /**
     * Route for JSON import
     * @param exp expense that was imported
     * @return expense that has been added to DB
     */
    @MessageMapping("/expenses")
    @SendTo("/topic/expenses")
    public Expense addExpense(Expense exp){
        createExpense(exp);
        return exp;
    }

}