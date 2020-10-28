package com.hbhb.web.resolver;

import com.hbhb.core.constants.AuthConstant;
import com.hbhb.core.utils.JsonUtil;
import com.hbhb.web.annotation.UserId;

import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiaokang
 * @since 2020-10-28
 */
@Component
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class);
    }

    @Override
    public Object resolveArgument(@Nullable MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @Nullable NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(servletRequestAttributes).getRequest();
        String payload = request.getHeader(AuthConstant.JWT_PAYLOAD_KEY.value());
        Object userId = JsonUtil.findByKey(payload, AuthConstant.JWT_USER_ID_KEY.value());
        if (userId == null) {
            throw new MissingServletRequestPartException(AuthConstant.JWT_USER_ID_KEY.value());
        }
        return Integer.parseInt((String) userId);
    }
}
