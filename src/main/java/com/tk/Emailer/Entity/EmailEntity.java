package com.tk.Emailer.Entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data

public class EmailEntity {
    private String mailContent;
    private String tone;
}
