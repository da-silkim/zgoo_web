package zgoo.cpos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import zgoo.cpos.service.EmailService;

@Slf4j
@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-email")
    public String sendEmail(@RequestParam(value = "to", required = true) String to,
            @RequestParam(value = "subject", required = true) String subject,
            @RequestParam(value = "text", required = true) String text) {
        log.info("=== Send Mail ===");
        emailService.sendMailMessage(to, subject, text);
        return "Email send successfully";
    }
}
