package com.example.trekmatenepal.model;

import java.util.List;

public class ErrorResponse {
    private boolean success;
    private String message;
    private List<ValidationError> errors;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public static class ValidationError {
        private String type;
        private String value;
        private String msg;
        private String path;
        private String location;

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

        public String getMsg() {
            return msg;
        }

        public String getPath() {
            return path;
        }

        public String getLocation() {
            return location;
        }
    }
}
