package net.adonika.gmsprt.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class ErrorResp extends RuntimeException {

    private static final long serialVersionUID = 3078820401247480145L;
    private final HttpStatus status;
    private final List<FieldError> errors;

    public ErrorResp(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.errors = new ArrayList<>();
    }
    
    private static class FieldError {
        private final String field;
        private final Object value;
        private final String reason;

        public FieldError(String field, Object value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public String getField() {
            return field;
        }

        public Object getValue() {
            return value;
        }

        public String getReason() {
            return reason;
        }
    }

    public static ErrorResp getBadRequest() {
        return getInstance(HttpStatus.BAD_REQUEST, "exception.bad_request");
    }

    public static ErrorResp getUnauthorized() {
        return getInstance(HttpStatus.UNAUTHORIZED, "exception.unauthorized");
    }

    public static ErrorResp getForbidden() {
        return getInstance(HttpStatus.FORBIDDEN, "exception.forbidden");
    }

    public static ErrorResp getNotFound() {
        return getInstance(HttpStatus.NOT_FOUND, "exception.not_found");
    }

    public static ErrorResp getConflict() {
        return getInstance(HttpStatus.CONFLICT, "exception.conflict");
    }

    public static ErrorResp getInternalServerError() {
        return getInstance(HttpStatus.INTERNAL_SERVER_ERROR, "exception.internal_server_error");
    }

    public static ErrorResp getInstance(HttpStatus status, String message) {
        return new ErrorResp(status, message);
    }

    public void addError(String field, Object value, String reason) {
        errors.add(new FieldError(field, value, reason));
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Map<String, Object> toData() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("message", super.getMessage());
        data.put("errors", errors);
        return data;
    }
}
