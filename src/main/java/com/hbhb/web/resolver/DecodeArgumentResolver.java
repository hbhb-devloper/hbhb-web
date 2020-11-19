package com.hbhb.web.resolver;

import com.hbhb.core.utils.UriUtil;
import com.hbhb.web.annotation.Decode;

import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

/**
 * @author xiaokang
 * @since 2020-10-28
 */
@Component
public class DecodeArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Decode.class);
    }

    @Override
    public Object resolveArgument(@Nullable MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @Nullable NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        String parameterName = String.valueOf(Objects.requireNonNull(parameter).getParameter());
        String argument = Objects.requireNonNull(webRequest).getParameter(parameterName);
        return UriUtil.decode(argument);
    }
}
