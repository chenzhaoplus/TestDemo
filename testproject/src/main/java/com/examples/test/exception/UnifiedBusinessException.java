package com.examples.test.exception;

/**
 * @author chenz
 */
public class UnifiedBusinessException extends RuntimeException {

    private String code;
    private String message;

    public UnifiedBusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}