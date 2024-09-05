package server.Services;


import jakarta.mail.internet.AddressException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class EmailService {


    private final JavaMailSender mailSender;

    /**
     * Constructor for EmailService
     * @param mailSender
     */
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * This method sends an email from the email set in application
     * properties
     * @param to email to send
     * @param subject of the email
     * @param body of the email
     * @param creatorEmail email of the user
     */

    public void sendEmail(String to, String subject, String body,
                          String creatorEmail) throws AddressException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        if(!creatorEmail.equals("null") && !creatorEmail.equals("")
                && creatorEmail != null){ //If the creators email is not set we have an empty cc
            message.setCc(creatorEmail);
        }
        mailSender.send(message);
    }
}

