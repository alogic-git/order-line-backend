package com.orderline.basic.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.orderline.basic.exception.*;
import com.orderline.basic.model.dto.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ApiCommonAdvice {
    private static final String ERROR_MESSAGE_FORMAT = "[{}] {}";

    // 예기치못한 Exception
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler
//    public ApiResponseDto<String> handleBaseException(Exception e) {
//        ApiResponseDto<String> exception = ApiResponseDto.createException(e.getMessage());
//        log.error(ERROR_MESSAGE_FORMAT, HttpStatus.INTERNAL_SERVER_ERROR+" : GeneralException", exception);
//        return exception;
//    }
//
    // 일반적인 Exception

    // Parameter 값의 Validation 에 실패한 경우
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ApiResponseDto<String> handleValidException(MethodArgumentNotValidException e) {

        String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        ApiResponseDto<String> exception = ApiResponseDto.createException(errorMessage);
        log.error(ERROR_MESSAGE_FORMAT, HttpStatus.BAD_REQUEST+" : MethodArgumentNotValidException", exception);
        return exception;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public ApiResponseDto<String> handleBaseException(ConstraintViolationException e) {
        String errorMessage = e.getConstraintViolations().iterator().next().getMessage();
        ApiResponseDto<String> exception = ApiResponseDto.createException(errorMessage);
        log.error(ERROR_MESSAGE_FORMAT, HttpStatus.BAD_REQUEST+" : ConstraintViolationException", exception);
        return exception;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConversionFailedException.class})
    public ApiResponseDto<String> handleValidException(ConversionFailedException e) {
        String errorMessage = String.format("%s 값에 대한 요청이 잘못됐습니다.", e.getTargetType().getType().getSimpleName());
        ApiResponseDto<String> exception = ApiResponseDto.createException(errorMessage);
        log.error(ERROR_MESSAGE_FORMAT, HttpStatus.BAD_REQUEST+" : ConversionFailedException", errorMessage);
        return exception;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NumberFormatException.class})
    public ApiResponseDto<String> handleValidException(NumberFormatException e) {
        String errorMessage = String.format("%s 값이 숫자값이 되어야 합니다.", e.getMessage());
        ApiResponseDto<String> exception = ApiResponseDto.createException(errorMessage);
        log.error(ERROR_MESSAGE_FORMAT, HttpStatus.BAD_REQUEST+" : NumberFormatException", errorMessage);
        return exception;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BindException.class})
    public ApiResponseDto<ErrorMessageCollection> handleValidException(BindException e) {

        ApiResponseDto<ErrorMessageCollection> exception = ApiResponseDto.createException("",
                new ErrorMessageCollection(e.getBindingResult().getFieldErrors(), e.getBindingResult().getGlobalErrors()));
        log.error(ERROR_MESSAGE_FORMAT, HttpStatus.BAD_REQUEST+" : BindException", exception);
        return exception;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InvalidFormatException.class})
    public ApiResponseDto<String> handleValidException(InvalidFormatException e) {
        String pathReference = e.getPathReference();
        String errorMessage = String.format("%s 요청 파라미터를 확인해주세요.", pathReference.substring(pathReference.indexOf("\"") + 1, pathReference.lastIndexOf("\"")).trim());
        ApiResponseDto<String> exception = ApiResponseDto.createException(errorMessage);
        log.error(ERROR_MESSAGE_FORMAT, HttpStatus.BAD_REQUEST+" : InvalidFormatException", exception);
        return exception;
    }

    // Custom Exception 처리
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public ApiResponseDto<String> handleValidException(NotFoundException e) {
        ApiResponseDto<String> exception = ApiResponseDto.createException(e.getMessage());
        log.error(ERROR_MESSAGE_FORMAT, HttpStatus.NOT_FOUND+" : NotFoundException", exception);
        return exception;
    }
    
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UnauthorizedException.class})
    public ApiResponseDto<String> handleValidException(UnauthorizedException e) {
        ApiResponseDto<String> exception = ApiResponseDto.createException(e.getMessage());
        log.error(ERROR_MESSAGE_FORMAT, HttpStatus.UNAUTHORIZED+" : UnauthorizedException", exception);
        return exception;
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InternalServerErrorException.class})
    public ApiResponseDto<String> handleValidException(InternalServerErrorException e) {
        ApiResponseDto<String> exception = ApiResponseDto.createException(e.getMessage());
        log.error(ERROR_MESSAGE_FORMAT, HttpStatus.BAD_REQUEST+":InternalServerErrorException", exception);
        return exception;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ExternalServerErrorException.class})
    public ApiResponseDto<String> handleServerErrorException(UnauthorizedException e) {
        ApiResponseDto<String> exception = ApiResponseDto.createException(e.getMessage());
        log.error(ERROR_MESSAGE_FORMAT, HttpStatus.BAD_REQUEST+":ExternalServerErrorException", exception);
        return exception;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DuplicateException.class})
    public ApiResponseDto<String> handleValidException(DuplicateException e) {
        ApiResponseDto<String> exception = ApiResponseDto.createException(e.getMessage());
        log.error(ERROR_MESSAGE_FORMAT, HttpStatus.BAD_REQUEST+": DuplicateErrorException", exception);
        return exception;
    }

}
