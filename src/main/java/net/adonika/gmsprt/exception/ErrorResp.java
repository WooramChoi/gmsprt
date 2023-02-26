package net.adonika.gmsprt.exception;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorResp extends RuntimeException{

    private HttpStatus status;
    private final List<FieldError> errors = new ArrayList<>();

    private ErrorResp() {}
    public ErrorResp(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public static ErrorResp getBadRequest() {
        return new ErrorResp(HttpStatus.BAD_REQUEST, "exception.bad_request");  // TODO MessageSource
    }
    public static ErrorResp getUnauthorized() {
        return new ErrorResp(HttpStatus.UNAUTHORIZED, "exception.unauthorized");  // TODO MessageSource
    }
    public static ErrorResp getForbidden() {
        return new ErrorResp(HttpStatus.FORBIDDEN, "exception.forbidden");  // TODO MessageSource
    }
    public static ErrorResp getNotFound() {
        return new ErrorResp(HttpStatus.NOT_FOUND, "exception.not_found");  // TODO MessageSource
    }
    public static ErrorResp getConflict() {
        return new ErrorResp(HttpStatus.CONFLICT, "exception.conflict");  // TODO MessageSource
    }
    public static ErrorResp getInternalServerError() {
        return new ErrorResp(HttpStatus.INTERNAL_SERVER_ERROR, "exception.internal_server_error");  // TODO MessageSource
    }

    private static class FieldError {
        private String field;
        private Object value;
        private String reason;

        private FieldError(){}
        public FieldError(String field, Object value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

    public void addError(String field, Object value, String reason) {
        errors.add(new FieldError(field, value, reason));
    }

    public Map<String, Object> toData() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("message", super.getMessage());
        data.put("errors", errors);
        return data;
    }
}
