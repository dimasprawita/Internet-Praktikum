package com.locationaware.model;

/**
 * Model for normal json response from
 * the backend. The response consists of message
 * token and path
 */
public class Response {

    private String message;
    private String token;
    private String path;

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public String getPath() { return path; }
}
