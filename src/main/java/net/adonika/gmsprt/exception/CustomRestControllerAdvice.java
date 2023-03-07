package net.adonika.gmsprt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class CustomRestControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(CustomRestControllerAdvice.class);

    @ExceptionHandler(ErrorResp.class)
    public ResponseEntity<Map<String, Object>> handleErrorResp(ErrorResp errorResp) {
        logger.debug("RestControllerAdvice / handleErrorResp");
        errorResp.printStackTrace();
        return ResponseEntity.status(errorResp.getStatus()).body(errorResp.toData());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        logger.debug("RestControllerAdvice / handleException");
        e.printStackTrace();

        HttpStatus status;
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            status = responseStatus.code();
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        String message = (e.getMessage() != null) ? e.getMessage() : e.toString();

        ErrorResp errorResp = ErrorResp.getInstance(status, message);
        return ResponseEntity.status(errorResp.getStatus()).body(errorResp.toData());
    }

}
