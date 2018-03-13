package com.lm.bank.busobj.response;

public class GeneralCreateResponse<I> extends GeneralResponse {
    private final I id;

    private GeneralCreateResponse(I id, boolean success, String errorMessage) {
        super(success, errorMessage);
        this.id = id;
    }

    public I getId() {
        return id;
    }

    public static <I> GeneralCreateResponse<I> of(I id) {
        return new GeneralCreateResponse<>(id, true, null);
    }

    public static <I> GeneralCreateResponse<I> error(String err) {
        return new GeneralCreateResponse<>(null, false, err);
    }

    @Override
    public String toString() {
        return "GeneralCreateResponse{" +
                "id=" + id +
                ", success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                '}';
    }
}
