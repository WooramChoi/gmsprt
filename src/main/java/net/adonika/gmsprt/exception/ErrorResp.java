package net.adonika.gmsprt.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class ErrorResp extends RuntimeException {

    private HttpStatus status;
    private final List<FieldError> errors = new ArrayList<>();

    private ErrorResp() {
    }

    public ErrorResp(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public static ErrorResp getBadRequest(FieldError... fieldErrors) {
        return getInstance(HttpStatus.BAD_REQUEST, "exception.bad_request", fieldErrors);
    }

    public static ErrorResp getUnauthorized(FieldError... fieldErrors) {
        return getInstance(HttpStatus.UNAUTHORIZED, "exception.unauthorized", fieldErrors);
    }

    public static ErrorResp getForbidden(FieldError... fieldErrors) {
        return getInstance(HttpStatus.FORBIDDEN, "exception.forbidden", fieldErrors);
    }

    public static ErrorResp getNotFound(FieldError... fieldErrors) {
        return getInstance(HttpStatus.NOT_FOUND, "exception.not_found", fieldErrors);
    }

    public static ErrorResp getConflict(FieldError... fieldErrors) {
        return getInstance(HttpStatus.CONFLICT, "exception.conflict", fieldErrors);
    }

    public static ErrorResp getInternalServerError(FieldError... fieldErrors) {
        return getInstance(HttpStatus.INTERNAL_SERVER_ERROR, "exception.internal_server_error", fieldErrors);
    }

    public static ErrorResp getInstance(HttpStatus status, String message, FieldError... fieldErrors) {
        ErrorResp errorResp = new ErrorResp(status, message);
        if (fieldErrors != null) {
            for (FieldError fieldError : fieldErrors) {
                errorResp.addError(fieldError.getField(), fieldError.getValue(), fieldError.getReason());
            }
        }
        return errorResp;
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
