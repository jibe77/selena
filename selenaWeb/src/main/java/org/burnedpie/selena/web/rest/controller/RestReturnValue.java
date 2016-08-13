package org.burnedpie.selena.web.rest.controller;

/**
 * Created by jibe on 13/08/16.
 */
public class RestReturnValue {

    private String message;
    private String status;

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
        return "RestReturnValue{status="+ status+", message="+ message+"}";
    }
}
