package com.littletrips.tripsim.controller.routes;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class PageController {

    @GetMapping
    public String getLandingPage() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/index.html");
        return new String(resource.getInputStream().readAllBytes());
    }

}
