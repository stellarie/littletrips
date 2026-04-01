package com.littletrips.api.controller.routes;

import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class ErrorPageController implements ErrorController {

    @RequestMapping(value = "/error", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> handleError() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/index.html");
        String html = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return ResponseEntity.status(404).body(html);
    }
}
