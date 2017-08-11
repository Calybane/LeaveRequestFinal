package com.example.leaverequest.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice(annotations = RestController.class)
public class ConfigurationExceptionHandler
{
    private final Logger logger = LoggerFactory.getLogger(ConfigurationExceptionHandler.class);
    
    
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    protected ConfigurationException notFound(final Exception exception, final WebRequest request) {
        logger.warn("Entity not found, exception sent to client", exception);
        return newException(exception, request);
    }
    
    
    @ResponseBody
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(EntityBadInformationsException.class)
    protected ConfigurationException badInformations(final Exception exception, final WebRequest request) {
        logger.warn("Entity incorrect, exception sent to client", exception);
        return newException(exception, request);
    }
    
    
    private static ConfigurationException newException(final Exception exception, final WebRequest request) {
        final ConfigurationException result = new ConfigurationException();
        result.setMessage(exception.getMessage());
        // result.setStackTrace(exception.getStackTrace());
        result.setPath(request.getDescription(false));
        result.setException(exception.getClass().toString());
        result.setTimestamp(new Date());
        return result;
    }
}
