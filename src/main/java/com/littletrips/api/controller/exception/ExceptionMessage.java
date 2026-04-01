package com.littletrips.api.controller.exception;

import org.springframework.http.HttpStatus;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

public class ExceptionMessage {
    public static String getGenericExceptionMessage() {
        return new ObjectMapper()
                .writeValueAsString(
                        createExceptionMessage(
                                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                "Please contact the maintainer regarding this exception and include the URL you tried to access."
                        )
                );
    }

    private static Map<String, String> createExceptionMessage(String status, String message) {
        return Map.of("status", status, "message", message);
    }
}
