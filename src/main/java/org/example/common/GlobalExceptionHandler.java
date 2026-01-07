package org.example.common;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* 1. 业务异常 */
    @ExceptionHandler(BusinessException.class)
    public CommonReturn<Void> handleBusiness(BusinessException ex) {
        return CommonReturn.fail(ex.getCode(), ex.getMsg());
    }

    /* 2. 参数校验失败（@Valid） */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonReturn<Void> handleValid(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
        return CommonReturn.fail(400, msg);
    }

    /* 3. 其他未知异常 */
    @ExceptionHandler(Exception.class)
    public CommonReturn<Void> handleOthers(Exception ex) {
        return CommonReturn.fail(500, "服务器内部错误");
    }
}
