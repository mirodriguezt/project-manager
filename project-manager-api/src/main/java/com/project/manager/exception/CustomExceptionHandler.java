package com.project.manager.exception;

import com.project.manager.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ClientNonExistent.class)
    public ResponseEntity<ExceptionResponse> handleClientNonExistent(ClientNonExistent ex) {
        return new ResponseEntity<>(createExceptionResponse(HttpStatus.UNPROCESSABLE_ENTITY.toString(), ex.getMessage()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ProjectExistent.class)
    public ResponseEntity<ExceptionResponse> handleProjectExistent(ProjectExistent ex) {
        return new ResponseEntity<>(createExceptionResponse(HttpStatus.UNPROCESSABLE_ENTITY.toString(), ex.getMessage()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ProjectNonExistent.class)
    public ResponseEntity<ExceptionResponse> handleProjectNonExistent(ProjectNonExistent ex) {
        return new ResponseEntity<>(createExceptionResponse(HttpStatus.UNPROCESSABLE_ENTITY.toString(), ex.getMessage()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ActivityExistent.class)
    public ResponseEntity<ExceptionResponse> handleActivityExistent(ActivityExistent ex) {
        return new ResponseEntity<>(createExceptionResponse(HttpStatus.UNPROCESSABLE_ENTITY.toString(), ex.getMessage()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ActivityNonExistent.class)
    public ResponseEntity<ExceptionResponse> handleActivityNonExistent(ActivityNonExistent ex) {
        return new ResponseEntity<>(createExceptionResponse(HttpStatus.UNPROCESSABLE_ENTITY.toString(), ex.getMessage()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private ExceptionResponse createExceptionResponse(String exceptionId, String message) {
        Map<String, String> response = new HashMap<>();
        response.put("id", exceptionId);
        response.put("message", message);

        return ExceptionResponse
                .builder()
                .status(exceptionId)
                .errors(Collections.singletonList(response))
                .build();
    }
}
