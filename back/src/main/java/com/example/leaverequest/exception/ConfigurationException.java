package com.example.leaverequest.exception;

import java.util.Date;

public class ConfigurationException
{
    private String path;

    private String message;
    
    private StackTraceElement[] stackTrace;

    private String exception;

    private Date timestamp;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}
    
    public void setStackTrace(StackTraceElement[] stackTrace) {
		this.stackTrace = stackTrace;
	}

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
