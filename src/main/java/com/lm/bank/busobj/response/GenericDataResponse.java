package com.lm.bank.busobj.response;

import java.util.Optional;

public class GenericDataResponse<T> extends GeneralResponse {
    private final T data;


    private GenericDataResponse(boolean success, String errorMessage, T data) {
        super(success, errorMessage);
        this.data = data;
    }

    public static <T> GenericDataResponse<T> of(T data) {
        return new GenericDataResponse<>(true, null, data);
    }

    public static <T> GenericDataResponse<T> of(boolean success, T data) {
        return new GenericDataResponse<>(success, null, data);
    }

    public static <T> GenericDataResponse<T> of(boolean success, String error) {
        return new GenericDataResponse<>(success, error, null);
    }

    public static <T> GenericDataResponse<T> of(String error) {
        return new GenericDataResponse<>(false, error, null);
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "GenericDataResponse{" +
                "data=" + data +
                ", success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                '}';
    }
}
