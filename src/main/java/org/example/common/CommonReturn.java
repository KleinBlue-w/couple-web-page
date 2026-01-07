package org.example.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonReturn<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> CommonReturn<T> ok(T data) {
        return new CommonReturn<>(200, "success", data);
    }

    public static <T> CommonReturn<T> fail(Integer code, String msg) {
        return new CommonReturn<>(code, msg, null);
    }
}