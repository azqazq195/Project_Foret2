package com.project.danggeun;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Greeting {
    @GetMapping("/greeting")
    public int greeting() {
        return 1;
    }
}
