package server.api;


import commons.Event;
import commons.Participant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.EmailService;
import server.Services.EventService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/email")
public class EmailController {

    private EmailService emailService;
    private EventService eventService;

    @Value("${server.url}")
    private String serverUrl;

    /**
     * Constructor for EmailController
     * @param emailService to use
     * @param eventService to ue
     */
    public EmailController(EmailService emailService, EventService eventService) {
        this.emailService = emailService;
        this.eventService = eventService;
    }


    /**
     * Method to send the invite code to a list of
     * participant's emails
     * @param inviteCode code to join event
     * @param emails list of emails to send
     * @param creatorName the name of the person who sends the emails
     * @param creatorEmail the email of the user of the app to cc
     * @return response
     */
    @PostMapping(path = {"/{inviteID}"})
    public ResponseEntity<?> sendInvites(@PathVariable("inviteID") String inviteCode,
                                         @RequestBody List<String> emails,
                                         @RequestParam String creatorName,
                                         @RequestParam String creatorEmail){
        Optional<Event> eventOptional = eventService.findByInviteCode(inviteCode);
        if(!emails.isEmpty() && eventOptional.isPresent()){
            for(String email : emails){
                String body = "";
                if(creatorName.equals("null")){
                    body += "You are invited to " + eventOptional.get().getTitle() + " event. " +
                            "You can connect to this event using the following code: " + inviteCode+
                            ". The server URL is: " + serverUrl;
                }
                else{
                    body += creatorName + " invited you to " + eventOptional.get().getTitle()
                            + " event." +
                            " You can connect to this event using the following code: "
                            + inviteCode + ". The server URL is: " + serverUrl;
                }
                try{
                    emailService.sendEmail(email, eventOptional.get().getTitle()
                                    + " ( " + inviteCode + " ) - Splitty",
                            body, creatorEmail);
                    return ResponseEntity.ok(HttpStatus.OK);
                }
                catch(Exception e){
                    return  ResponseEntity.badRequest().body("Email not valid");
                }
            }
        }
        return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
    }


    /**
     * Method to send the invite code to a list of
     * participant's emails
     * @param participant participant of which we test the credentials
     * @return response
     */
    @PostMapping(path = {"/default"})
    public ResponseEntity<?> sendDefault(@RequestBody Participant participant){
        if(participant != null && !participant.getEmail().equals("null")){
            String body = "This is a default email to check you have input " +
                    "your credentials correctly.\n" +
                    "Name: " + participant.getName() +
                    "\nIBAN: " + participant.getIban() +
                    "\nBIC: " + participant.getBic();
            try{
                emailService.sendEmail(participant.getEmail(), "Default Email",
                        body, participant.getEmail());
                return ResponseEntity.ok(HttpStatus.OK);
            }
            catch (Exception e) {
                return  ResponseEntity.badRequest().body("Email not valid");
            }
        }
        return ResponseEntity.badRequest().body("Email not valid");
    }


    /**
     * Method to send a reminder email to the person who has to pay a debt
     *
     * @param participant the participant of the person who will receive the money
     * @param creatorEmail email of the user using the app
     * @param amount amount that has to be paid
     * @param email email of the person who has to pay
     * @param eventTitle title of the event in which the expense is in
     * @return Response
     */
    @PostMapping(path = {"/"})
    public ResponseEntity<?> sendReminder(@RequestBody Participant participant,
                                          @RequestParam String creatorEmail,
                                          @RequestParam String amount, @RequestParam String email,
                                          @RequestParam String eventTitle){
        String body = "This email is to remind you that you have to pay " + amount +
                " EUR to " + participant.getName() + ".\n You can transfer the money to: " +
                "\nAccount Holder: " + participant.getName() +
                "\nIBAN: " + participant.getIban() +
                "\nBIC: " + participant.getBic();
        try {
            emailService.sendEmail(email, "Reminder - " + eventTitle,
                    body, creatorEmail);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        catch(Exception e){
            return  ResponseEntity.badRequest().body("Email not valid");
        }
    }



}