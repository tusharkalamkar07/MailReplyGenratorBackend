package com.tk.Emailer.Controller;


import com.tk.Emailer.Entity.EmailEntity;
import com.tk.Emailer.Service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
@CrossOrigin (origins = "*")
public class GeminiController {
    private final EmailService emailService;
    @PostMapping("/generateResponse")
    public ResponseEntity email(@RequestBody EmailEntity emailEntity){


         return ResponseEntity.ok(emailService.responseGenService(emailEntity));
    }





}
