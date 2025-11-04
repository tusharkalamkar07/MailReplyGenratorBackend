package com.tk.Emailer.Controller;

import com.tk.Emailer.Entity.EmailEntity;
import com.tk.Emailer.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(
        origins = {
                "http://localhost:5173",          // local React frontend
                "https://mailreplygenratorfrontend.vercel.app" // deployed frontend (change if different)
        },
        allowedHeaders = "*",
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS}
)
public class GeminiController {

    private final EmailService emailService;

    @Autowired
    public GeminiController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/generateResponse")
    public ResponseEntity<String> email(@RequestBody EmailEntity emailEntity) {
        return ResponseEntity.ok(emailService.responseGenService(emailEntity));
    }
}
