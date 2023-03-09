package net.adonika.gmsprt.exception;

import java.util.Locale;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import net.adonika.gmsprt.util.ObjectUtil;

@RestControllerAdvice
public class CustomRestControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(CustomRestControllerAdvice.class);
    
    private final MessageSource messageSource;
    
    public CustomRestControllerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    
    /**
     * TODO messageSource 를 static class 로 등록하는게 귀찮아서 이런식으로 처리함. 근데 결국 Util 처럼 사용하려면 언젠가 해야할일이다.
     * @param data
     * @return
     */
    @Deprecated
    private Map<String, Object> convertDataMessage(Map<String, Object> data) {
        String message = String.valueOf(data.getOrDefault("message", ""));
        data.put("message", messageSource.getMessage(message, null, Locale.getDefault()));
        return data;
    }

    @ExceptionHandler(ErrorResp.class)
    public ResponseEntity<Map<String, Object>> handleErrorResp(ErrorResp errorResp) {
        // logger.error("handleErrorResp", errorResp);
        logger.error("handleErrorResp");
        
        // TODO 중복 코드
        Map<String, Object> data = convertDataMessage(errorResp.toData());
        logger.error(ObjectUtil.toJson(data));
        
        return ResponseEntity.status(errorResp.getStatus()).body(data);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValid(MethodArgumentNotValidException e) {
        logger.error("handleValid");
        BindingResult bindingResult = e.getBindingResult();
        
        ErrorResp errorResp = ErrorResp.getBadRequest();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorResp.addError(fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
        }
        
        // TODO 중복 코드
        Map<String, Object> data = convertDataMessage(errorResp.toData());
        logger.error(ObjectUtil.toJson(data));
        
        return ResponseEntity.status(errorResp.getStatus()).body(data);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleValidated(ConstraintViolationException e) {
        logger.error("handleValidated");
        
        ErrorResp errorResp = ErrorResp.getBadRequest();
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            errorResp.addError(constraintViolation.getPropertyPath().toString(), constraintViolation.getInvalidValue(), constraintViolation.getMessage());
        }
        
        // TODO 중복 코드
        Map<String, Object> data = convertDataMessage(errorResp.toData());
        logger.error(ObjectUtil.toJson(data));
        
        return ResponseEntity.status(errorResp.getStatus()).body(data);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        logger.error("handleException", e);

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
