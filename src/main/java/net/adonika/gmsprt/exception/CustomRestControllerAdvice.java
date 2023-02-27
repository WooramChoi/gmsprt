package net.adonika.gmsprt.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomRestControllerAdvice {
	
	private final Logger logger = LoggerFactory.getLogger(CustomRestControllerAdvice.class);
	
	@ExceptionHandler(ErrorResp.class)
	public ResponseEntity<Map<String, Object>> handleErrorResp(ErrorResp e) {
		logger.debug("RestControllerAdvice / handleErrorResp");
		return ResponseEntity.status(e.getStatus()).body(e.toData());
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
		logger.debug("RestControllerAdvice / handleRuntimeException");
		Map<String, Object> data = new HashMap<>();
		data.put("message", e.getMessage());
		data.put("errors", null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleException(RuntimeException e) {
		logger.debug("RestControllerAdvice / handleException");
		Map<String, Object> data = new HashMap<>();
		data.put("message", e.getMessage());
		data.put("errors", null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
	}

}
