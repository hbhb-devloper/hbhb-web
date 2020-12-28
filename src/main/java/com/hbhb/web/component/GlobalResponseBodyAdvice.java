package com.hbhb.web.component;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;

import io.swagger.v3.oas.annotations.Operation;

/**
 * 全局响应参数处理类（在gateway中失效）
 *
 * @author xiaokang
 * @since 2020-07-27
 */
@RestControllerAdvice
@SuppressWarnings(value = {"all"})
public class GlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final ThreadLocal<ObjectMapper> mapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);

    /**
     * 标注了以下注解，则需要进行全局响应体处理
     */
    private static final Class[] ANNOYS = {
            RequestMapping.class,
            GetMapping.class,
            PostMapping.class,
            PutMapping.class,
            DeleteMapping.class,
            Operation.class
    };

    /**
     * 不需要做响应体处理的url白名单
     */
    private static final String[] EXCLUDE_PATH = {
            "/download"
    };

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
        AnnotatedElement element = returnType.getAnnotatedElement();
        return Arrays.stream(ANNOYS).anyMatch(annoy -> annoy.isAnnotation() && element.isAnnotationPresent(annoy));
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object body,
                                  MethodParameter returnType,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        Object out;
        ObjectMapper mapper = mapperThreadLocal.get();
        // 网关已设置只对application/json的类型进行响应体封装
        if (Arrays.stream(EXCLUDE_PATH).noneMatch(request.getURI().getPath()::contains)) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        }
        // 加了网关后，下列代码不适用
//        if (body instanceof ApiResult) {
//            out = body;
//        } else if (body instanceof String) {
//            try {
//                out = mapper.writeValueAsString(ApiResult.success(body));
//            } catch (JsonProcessingException e) {
//                out = ApiResult.error(String.valueOf(ResultCode.EXCEPTION.code()), e.getMessage());
//            }
//        } else {
//            out = ApiResult.success(body);
//        }
//        return out;
        return body == null ? "" : body;
    }
}
