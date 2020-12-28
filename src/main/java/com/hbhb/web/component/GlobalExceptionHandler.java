package com.hbhb.web.component;

import com.hbhb.core.bean.ApiResult;
import com.hbhb.core.enums.ResultCode;
import com.hbhb.web.exception.BusinessException;
import com.netflix.client.ClientException;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理类
 *
 * @author xiaokang
 * @since 2020-06-19
 */
@Slf4j
@RestControllerAdvice
@SuppressWarnings(value = {"rawtypes"})
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ApiResult handleAccessDeniedException(AccessDeniedException e) {
        outPutWarn(AccessDeniedException.class, ResultCode.FORBIDDEN, e);
        return ApiResult.error(ResultCode.FORBIDDEN);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResult handleNoFoundException(NoHandlerFoundException e) {
        outPutWarn(NoHandlerFoundException.class, ResultCode.NOT_FOUND, e);
        return ApiResult.error(ResultCode.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        outPutWarn(HttpRequestMethodNotSupportedException.class, ResultCode.METHOD_NOT_ALLOWED, e);
        return ApiResult.error(ResultCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ApiResult handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        outPutWarn(HttpMediaTypeNotSupportedException.class, ResultCode.UNSUPPORTED_MEDIA_TYPE, e);
        return ApiResult.error(ResultCode.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * 自定义异常
     */
    @ExceptionHandler(BusinessException.class)
    private ApiResult handleBusinessException(BusinessException e) {
        return ApiResult.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ApiResult handleException(Exception e) {
        outPutError(Exception.class, ResultCode.EXCEPTION, e);
        if (e instanceof ClientException) {
            return ApiResult.error(ResultCode.RPC_ERROR);
        } else if (e instanceof FeignException) {
            return ApiResult.error(ResultCode.RPC_ERROR);
        } else if (e.getCause() instanceof BusinessException) {
            return ApiResult.error(((BusinessException) e.getCause()).getCode(), e.getMessage());
        } else {
            return ApiResult.error(ResultCode.EXCEPTION);
        }
    }

    /**
     * HttpMessageNotReadableException 参数错误异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        outPutError(HttpMessageNotReadableException.class, ResultCode.PARAM_ERROR, e);
        String msg = String.format("%s : 错误详情( %s )",
                ResultCode.PARAM_ERROR.getMsg(),
                Objects.requireNonNull(e.getRootCause()).getMessage());
        return ApiResult.error(ResultCode.PARAM_ERROR.getCode(), msg);
    }

    /**
     * ConstraintViolationException 参数错误异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResult handleConstraintViolationException(ConstraintViolationException e) {
        String msg = "";
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        if (log.isDebugEnabled()) {
            for (ConstraintViolation error : constraintViolations) {
                log.error("{} -> {}", error.getPropertyPath(), error.getMessageTemplate());
                msg = error.getMessageTemplate();
            }
        }
        if (constraintViolations.isEmpty()) {
            log.error("validExceptionHandler error fieldErrors is empty");
            ApiResult.error(ResultCode.BUSINESS_ERROR.getCode(), "");
        }
        return ApiResult.error(ResultCode.PARAM_ERROR.getCode(), msg);
    }

    /**
     * MethodArgumentNotValidException 参数错误异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        outPutError(MethodArgumentNotValidException.class, ResultCode.PARAM_ERROR, e);
        return getBindResultDTO(e.getBindingResult());
    }

    /**
     * BindException 参数错误异常
     */
    @ExceptionHandler(BindException.class)
    public ApiResult handleBindException(BindException e) {
        outPutError(BindException.class, ResultCode.PARAM_ERROR, e);
        return getBindResultDTO(e.getBindingResult());
    }

    private ApiResult getBindResultDTO(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (log.isDebugEnabled()) {
            for (FieldError error : fieldErrors) {
                log.error("{} -> {}", error.getDefaultMessage(), error.getDefaultMessage());
            }
        }
        if (fieldErrors.isEmpty()) {
            log.error("validExceptionHandler error fieldErrors is empty");
            ApiResult.error(ResultCode.BUSINESS_ERROR.getCode(), "");
        }
        return ApiResult.error(ResultCode.PARAM_ERROR.getCode(), fieldErrors.get(0).getDefaultMessage());
    }

    public void outPutError(Class errorType, Enum secondaryErrorType, Throwable throwable) {
        log.error("[{}] {}: {}", errorType.getSimpleName(), secondaryErrorType, throwable.getMessage(), throwable);
    }

    public void outPutWarn(Class errorType, Enum secondaryErrorType, Throwable throwable) {
        log.warn("[{}] {}: {}", errorType.getSimpleName(), secondaryErrorType, throwable.getMessage());
    }
}
