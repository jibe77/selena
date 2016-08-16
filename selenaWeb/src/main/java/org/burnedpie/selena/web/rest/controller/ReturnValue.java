package org.burnedpie.selena.web.rest.controller;

/**
 * Returned value.
 *
 * Created by jibe on 13/08/16.
 */
public class ReturnValue {

    private String status;
    private String message;

    public ReturnValue() {

    }

    public ReturnValue(String status, String message, String ... params) {
        this.status = status;

        for (int i = 0; i < params.length ; i++) {
            message = message.replace("{" + i + "}", params[i]);
        }

        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ReturnValue{status="+ status+", message="+ message+"}";
    }
}
