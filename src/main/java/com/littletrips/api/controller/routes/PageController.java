package com.littletrips.api.controller.routes;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
public class PageController {

    @GetMapping({"","search", "trips", "trips/"})
    public ResponseEntity<String> getLandingPage(@RequestParam(required = false) String pan) throws IOException {
        ClassPathResource resource = new ClassPathResource("static/index.html");
        String html = new String(resource.getInputStream().readAllBytes());
        return ResponseEntity.ok(html);
    }

}
