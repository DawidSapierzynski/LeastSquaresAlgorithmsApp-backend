package pl.leastsquaresalgorithms.dataseries.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.leastsquaresalgorithms.dataseries.configuration.exception.ForbiddenException;
import pl.leastsquaresalgorithms.dataseries.configuration.exception.ResourceNotFoundException;
import pl.leastsquaresalgorithms.dataseries.configuration.exception.SizeException;
import pl.leastsquaresalgorithms.dataseries.dto.ResponseMessage;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    private final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseMessage> handleException(ResourceNotFoundException e) {
        logger.error("{}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessage(e.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ResponseMessage> handleException(ForbiddenException e) {
        logger.error("{}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ResponseMessage(e.getMessage()));
    }

    @ExceptionHandler(SizeException.class)
    public ResponseEntity<ResponseMessage> handleException(SizeException e) {
        logger.error("{}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseMessage(e.getMessage()));
    }
}
