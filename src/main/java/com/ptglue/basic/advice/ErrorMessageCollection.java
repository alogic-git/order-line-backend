package com.ptglue.basic.advice;

import lombok.Getter;
import lombok.ToString;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
public class ErrorMessageCollection {

    private final List<ErrorMessage> errors = new ArrayList<>();

    ErrorMessageCollection(List<FieldError> fieldErrors, List<ObjectError> globalErrors) {
        errors.addAll(getFieldErrors(fieldErrors));
        errors.addAll(getObjectErrors(globalErrors));
    }

    private List<ErrorMessage> getFieldErrors(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
            .map(f -> ErrorMessage.builder()
                .field(f.getField())
                .message(f.getDefaultMessage())
                .build())
            .collect(Collectors.toList());
    }

    private List<ErrorMessage> getObjectErrors(List<ObjectError> globalErrors) {
        return globalErrors.stream()
            .map(o -> ErrorMessage.builder()
                .field(o.getObjectName())
                .message(o.getDefaultMessage())
                .build())
            .collect(Collectors.toList());
    }
}
