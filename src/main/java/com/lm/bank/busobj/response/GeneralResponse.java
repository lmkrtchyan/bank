package com.lm.bank.busobj.response;

/**
 *   General response containing success and error message(if exists)
 */
public class GeneralResponse {
    private final boolean success;
    private final String errorMessage;

    public GeneralResponse(boolean success) {
        this.success = success;
        errorMessage = null;
    }

    public GeneralResponse(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "GeneralResponse{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
