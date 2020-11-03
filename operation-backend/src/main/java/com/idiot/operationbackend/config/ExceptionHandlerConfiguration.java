package com.idiot.operationbackend.config;


import com.idiot.operationbackend.support.CustomException;
import com.idiot.operationbackend.support.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wang xiao
 * @date Created in 15:38 2020/9/10
 */
@RestControllerAdvice
public class ExceptionHandlerConfiguration {

    private final Logger log = LoggerFactory.getLogger("ExceptionHandler");

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<JsonResult<String>> orderingExceptionHandler(CustomException e) {
        JsonResult<String> jsonResult = JsonResult.build(e.getCode());
        jsonResult.message(e.getMessage());
        log.error("发生业务异常==========>异常code:{},异常原因:{}", e.getCode(), e.getMessage());
        return ResponseEntity.ok(jsonResult);
    }



    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<JsonResult<String>> exceptionHandler(RuntimeException e) {
        JsonResult<String> jsonResult = JsonResult.build(500);
        jsonResult.message(e.getMessage());
        log.error("发系统异常==========>异常code: 500,异常原因:{}", e.getMessage());
        String errorStr = Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n"));
        log.error(errorStr);
        return ResponseEntity.ok(jsonResult);
    }

    /**
     * Bean 校验异常
     * @param ex
     * @return ResponseEntity<JsonResult>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonResult<String>> validationErrorHandler(MethodArgumentNotValidException ex) {
        List<String> errorInformation = ex.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        JsonResult<String> jsonResult = JsonResult.build(400);
        jsonResult.message(errorInformation.toString());
        log.warn("检测到校验参数异常==========>异常code: 400,异常原因:{}", errorInformation.toString());
        return ResponseEntity.ok(jsonResult);
    }


    /**
     * 参数类型转换错误
     * @param e 错误
     * @return 错误信息
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<JsonResult<String>> parameterTypeException(HttpMessageConversionException e) {
        log.error("参数类型异常==========>异常code: 400,异常原因:{}", e.getCause().getLocalizedMessage());
        JsonResult<String> jsonResult = JsonResult.build(400);
        jsonResult.message(e.getCause().getLocalizedMessage());
        return ResponseEntity.ok(jsonResult);
    }


    /**
     * 参数类型转换错误
     * @param e 错误
     * @return 错误信息
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<JsonResult<String>> parameterTypeException(MissingServletRequestParameterException e) {
        log.error("参数类型异常==========>异常code: 400,异常原因:{}", e.getMessage());
        JsonResult<String> jsonResult = JsonResult.build(400);
        jsonResult.message(e.getMessage());
        return ResponseEntity.ok(jsonResult);
    }

    /**
     * 参数类型转换错误
     * @param e 错误
     * @return 错误信息
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<JsonResult<String>> parameterTypeException(MissingRequestHeaderException e) {
        log.error("参数类型异常==========>异常code: 400,异常原因:{}", e.getMessage());
        JsonResult<String> jsonResult = JsonResult.build(400);
        jsonResult.message(e.getMessage());
        return ResponseEntity.ok(jsonResult);
    }


}
