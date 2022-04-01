package com.mikulicmateo.plm.exception.handler;

import com.mikulicmateo.plm.dto.response.ResponseMessageDto;
import com.mikulicmateo.plm.exception.ResponseException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.EntityNotFoundException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ProductResourceExceptionHandler {

    @ExceptionHandler(ResponseException.class)
    protected ResponseEntity<?> handleResponseException(Exception exception, WebRequest request){
        return new ResponseEntity<>(new ResponseMessageDto(false, exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    protected ResponseEntity<?> handleNoDbDataException(Exception exception, WebRequest request){
        return new ResponseEntity<>(new ResponseMessageDto(false, "Product does not exist."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<?> handleNoDbDataNotFoundException(Exception exception, WebRequest request){
        return new ResponseEntity<>(new ResponseMessageDto(false, "Product does not exist."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<?> handleBodyMissing(Exception exception, WebRequest request){
        return new ResponseEntity<>(new ResponseMessageDto(false, "Body not readable."), HttpStatus.BAD_REQUEST);
    }
}
