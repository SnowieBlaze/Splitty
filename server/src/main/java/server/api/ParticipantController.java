package server.api;

import commons.Participant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.ParticipantService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {
    private ParticipantService participantService;

    /**
     * Constructor for the controller
     * @param participantService - the participant service for the participant repository
     */
    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    /**
     * Get all participants
     * @return all participants
     */
    @GetMapping(path = {"", "/"})
    public List<Participant> getAll() {
        return participantService.getAll();
    }

    /**
     * Update a participant
     * @param updatedParticipant
     * @return all participants
     */
    @PutMapping(path = {"", "/"})
    public ResponseEntity<?> update(@RequestBody Participant updatedParticipant) {
        long participantId = updatedParticipant.getId(); // Assuming Participant has an ID field
        Participant savedParticipant = participantService.update(participantId, updatedParticipant);
        if (savedParticipant != null){
            return ResponseEntity.ok(savedParticipant);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Participant not found");
    }

    /**
     *
     * @param newParticipant
     * @return the new participant
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<?> create(@RequestBody Participant newParticipant){
        Participant savedParticipant = participantService.create(newParticipant);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedParticipant);
    }

    /**
     *
     * @param participantId
     * @return if participant was deleted or not
     */
    @DeleteMapping(path = {"", "/{id}"})
    public ResponseEntity<?> delete(@PathVariable("id") long participantId){
        boolean success = participantService.delete(participantId);
        if (success){
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Participant not found!");
        }
    }

    /**
     *
     * @param participantId
     * @return participant with the id or "No participant by id"
     */
    @GetMapping(path = {"/{id}"})
    public ResponseEntity<?> getById(@PathVariable("id") long participantId){
        Optional<Participant> participantOptional = participantService.getById(participantId);
        if (participantOptional.isPresent()){
            return ResponseEntity.ok(participantOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No participant by id");
        }
    }
}