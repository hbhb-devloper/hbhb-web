//package com.hbhb.web.exception.handler;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.hbhb.core.bean.ApiResult;
//import com.hbhb.core.enums.ResultCode;
//
//import org.springframework.core.MethodParameter;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.lang.Nullable;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
//import java.lang.reflect.AnnotatedElement;
//import java.util.Arrays;
//
//import io.swagger.v3.oas.annotations.Operation;
//
///**
// * 全局响应参数处理类（在gateway中失效）
// *
// * @author xiaokang
// * @since 2020-07-27
// */
//@RestControllerAdvice
//@SuppressWarnings(value = {"all"})
//public class ResponseAdviceHandler implements ResponseBodyAdvice<Object> {
//
//    private final ThreadLocal<ObjectMapper> mapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);
//
//    private static final Class[] annoys = {
//            RequestMapping.class,
//            GetMapping.class,
//            PostMapping.class,
//            PutMapping.class,
//            DeleteMapping.class,
//            Operation.class
//    };
//
//    @Override
//    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
//        AnnotatedElement element = returnType.getAnnotatedElement();
//        return Arrays.stream(annoys).anyMatch(annoy ->
//                annoy.isAnnotation() && element.isAnnotationPresent(annoy) && matchOperation(element));
//    }
//
//    /**
//     * 过滤swagger endpoint
//     */
//    private boolean matchOperation(AnnotatedElement element) {
//        Operation operation = element.getAnnotation(Operation.class);
//        return operation != null ? operation.hidden() == false : true;
//    }
//
//    @Override
//    public Object beforeBodyWrite(@Nullable Object body,
//                                  MethodParameter returnType,
//                                  MediaType mediaType,
//                                  Class<? extends HttpMessageConverter<?>> aClass,
//                                  ServerHttpRequest request,
//                                  ServerHttpResponse response) {
//        Object out;
//        ObjectMapper mapper = mapperThreadLocal.get();
//        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
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
//    }
//}
