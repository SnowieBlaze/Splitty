package server.api;

import commons.Debt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.DebtService;

import java.util.List;

@RestController
@RequestMapping("/api/debts")
public class DebtController {
    private DebtService debtService;

    /**
     * Constructor for the debt controller
     * @param debtService - the debt service for the debt repository
     */
    public DebtController(DebtService debtService) {
        this.debtService = debtService;
    }

    /**
     * Get all debts
     * @return all debts
     */
    @GetMapping(path = {"", "/"})
    public List<Debt> getAll() {
        return debtService.getAll();
    }

    /**
     * Return payment instructions associated with a specific event
     * @param eventId - the id of event we retrieve the debts for
     * @return a response entity with the correct debts,
     * bad request response otherwise
     */
    @GetMapping(path = {"event/{eventId}"})
    public ResponseEntity<List<Debt>> getPaymentInstructions(
        @PathVariable("eventId") long eventId) {
        List<Debt> debts = debtService.getPaymentInstructions(eventId);
        if(debts == null) {
            return ResponseEntity.badRequest().build();
        }
        else {
            return ResponseEntity.ok(debts);
        }
    }

    /**
     * Save a new debt if it is valid and returns a response
     * @param debt - the debt to be saved
     * @return response entity with "ok" status message if it is successful,
     * "bad request" message otherwise
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Debt> add(@RequestBody Debt debt) {
        Debt debtSaved = debtService.add(debt);
        if(debtSaved == null) {
            return ResponseEntity.badRequest().build();
        }
        else {
            return ResponseEntity.ok(debtSaved);
        }
    }

    /**
     * Deletes a debt by id if it exists
     * @param id - the id of the debt we search for
     * @return "Successful delete" response
     * if delete was successful, "Bad request" otherwise
     */
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        Debt deletedDebt = debtService.delete(id);
        if (deletedDebt == null) {
            return ResponseEntity.badRequest().build();
        }
        else {
            return ResponseEntity.ok().body("Successful delete");
        }
    }

    /**
     * Returns a debt by its id if it exists
     * @param id - the id to be searched with
     * @return response with either "ok" or "bad request"
     * response message
     */
    @GetMapping("/{id}")
    public ResponseEntity<Debt> getById(@PathVariable("id") long id) {
        Debt debt = debtService.getById(id);
        if (debt == null) {
            return ResponseEntity.badRequest().build();
        }
        else {
            return ResponseEntity.ok(debt);
        }
    }
}
