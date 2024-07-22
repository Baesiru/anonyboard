package com.example.anonyboard2.exception.handler;

import com.example.anonyboard2.exception.errorCode.CommonErrorCode;
import com.example.anonyboard2.exception.errorCode.ErrorCode;
import com.example.anonyboard2.exception.exception.RestApiException;
import com.example.anonyboard2.exception.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // RuntimeException 처리
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handleCustomException(RestApiException e) {
        return handleExceptionInternal(e.getErrorCode());
    }

    // IllegalArgumentException 에러 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
        return handleExceptionInternal(CommonErrorCode.INVALID_PARAMETER, e.getMessage());
    }

    // BadCredentialsException 에러 처리
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentials(BadCredentialsException e) {
        return handleExceptionInternal(CommonErrorCode.INVALID_PARAMETER, e.getMessage());
    }


    // Get 요청의 파라미터 검증 애네터이션들에서 넘어오는 에러 처리
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e) {
        return handleExceptionInternal(e, CommonErrorCode.INVALID_PARAMETER);
    }

    // RequestParam 애너테이션에서 파라미터를 등록하지 않았을 때의 에러 처리
    @Override
    public ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        //log.warn("handleIllegalArgument", ex);
        return handleExceptionInternal(ex, CommonErrorCode.MISSING_PARAMETER);
    }

    // @Valid 어노테이션으로 넘어오는 에러 처리
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        //log.warn("handleIllegalArgument", ex);
        return handleExceptionInternal(ex, CommonErrorCode.INVALID_PARAMETER);
    }

    //NoSuchElementException 예외 처리
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchException(NoSuchElementException e) {
        return handleExceptionInternal(CommonErrorCode.RESOURCE_NOT_FOUND);
    }

    // 대부분의 에러 처리
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(Exception ex) {
        //log.warn("handleAllException", ex);
        return handleExceptionInternal(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }

    // RuntimeException과 대부분의 에러 처리 메세지를 보내기 위한 메소드
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    // 코드 가독성을 위해 에러 처리 메세지를 만드는 메소드 분리
    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, message));
    }

    // 코드 가독성을 위해 에러 처리 메세지를 만드는 메소드 분리
    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(message)
                .build();
    }

    // get 요청 검증에서 넘어오는 에러 처리 메세지를 보내기 위한 메소드
    private ResponseEntity<Object> handleExceptionInternal(ConstraintViolationException e, ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(e, errorCode));
    }

    // @RequestParam 애너테이션으로 넘어오는 에러 처리 메세지를 보내기 위한 메소드
    private ResponseEntity<Object> handleExceptionInternal(MissingServletRequestParameterException e, ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(e, errorCode));
    }

    // @Valid 애너테이션으로 넘어오는 에러 처리 메세지를 보내기 위한 메소드
    private ResponseEntity<Object> handleExceptionInternal(MethodArgumentNotValidException e, ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(e, errorCode));
    }

    // get 요청 검증 코드 가독성을 위해 에러 처리 메세지를 만드는 메소드 분리
    private ErrorResponse makeErrorResponse(ConstraintViolationException e, ErrorCode errorCode) {
        List<ErrorResponse.GetValidationError> getvalidationErrorList = e.getConstraintViolations()
                .stream()
                .map(ErrorResponse.GetValidationError::of)
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .get_errors(getvalidationErrorList)
                .build();
    }

    // RequestParam 애너테이션에서 코드 가독성을 위해 에러 처리 메세지를 만드는 메소드 분리
    private ErrorResponse makeErrorResponse(MissingServletRequestParameterException e, ErrorCode errorCode) {
        String parameterName = e.getParameterName();
        String message = "해당 파라미터가 존재하지 않습니다.";

        ErrorResponse.ParameterError parameterError = ErrorResponse.ParameterError.of(parameterName, message);
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .param_error(parameterError)
                .build();
    }

    // @Valid 애너테이션에서 코드 가독성을 위해 에러 처리 메세지를 만드는 메소드 분리
    private ErrorResponse makeErrorResponse(MethodArgumentNotValidException e, ErrorCode errorCode) {
        List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ErrorResponse.ValidationError::of)
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .errors(validationErrorList)
                .build();
    }


}
