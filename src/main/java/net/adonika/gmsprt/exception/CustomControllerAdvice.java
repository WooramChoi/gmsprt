package net.adonika.gmsprt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * RestcontrollerAdvice 와 같이 쓸 수 없다고 한다. *
 */
//@ControllerAdvice
public class CustomControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(CustomControllerAdvice.class);

    @ExceptionHandler(ErrorResp.class)
    public String handleErrorResp(ErrorResp e) {
        logger.debug("ControllerAdvice / handleErrorResp");
        return "redirect:/error";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e) {
        logger.debug("ControllerAdvice / handleRuntimeException");
        return "redirect:/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(RuntimeException e) {
        logger.debug("ControllerAdvice / handleException");
        return "index.html";
    }

}
