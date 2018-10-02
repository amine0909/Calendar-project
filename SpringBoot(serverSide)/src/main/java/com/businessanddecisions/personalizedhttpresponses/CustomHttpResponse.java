package com.businessanddecisions.personalizedhttpresponses;

public class CustomHttpResponse {
    private String message;
    private CustomResponseType type;

    public CustomHttpResponse(String message,CustomResponseType type){
        this.message = message;
        this.type = type;
    }

    public CustomHttpResponse() {}

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(CustomResponseType type) {
        this.type = type;
    }

    public CustomResponseType getType() {
        return this.type;
    }

}
