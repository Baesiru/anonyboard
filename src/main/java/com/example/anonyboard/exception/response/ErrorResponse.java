package com.example.anonyboard.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {

    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<GetValidationError> get_errors;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final ParameterError param_error;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> errors;

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class GetValidationError {
        private final String field;
        private final String message;

        public static String getFieldName(String path){
            int index = path.lastIndexOf('.');
            return path.substring(index+1);
        }

        public static GetValidationError of(final ConstraintViolation<?> constraintViolation) {
            return GetValidationError.builder()
                    .field(getFieldName(constraintViolation.getPropertyPath().toString()))
                    .message(constraintViolation.getMessage())
                    .build();
        }
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ParameterError {
        private final String param;
        private final String message;

        public static ParameterError of(final String parameterName, final String message) {
            return ParameterError.builder()
                    .param(parameterName)
                    .message(message)
                    .build();
        }
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {
        private final String field;
        private final String message;

        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }

}