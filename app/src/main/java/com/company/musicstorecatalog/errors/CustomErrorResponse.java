package com.company.musicstorecatalog.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

//Code from Class on October 5 2022 Regarding custom handlers
public class CustomErrorResponse {

    private String errorMsg;
    private int status;
    private String errorCode;

    //    Describes how the object will be defined
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyy-MM-dd hh:mm:ss")
    private LocalDateTime timeStamp;

//    constructor;


    public CustomErrorResponse(HttpStatus status, String message) {
        this.errorMsg = message;
        this.status = status.value();
        this.errorCode = status.toString();
        this.timeStamp = LocalDateTime.now();
    }

    //    GETTERS AND SETTERS
    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
