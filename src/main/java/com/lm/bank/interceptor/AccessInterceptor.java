package com.lm.bank.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lm.bank.busobj.LoginNotRequired;
import com.lm.bank.busobj.SessionDataContainer;
import com.lm.bank.busobj.response.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private SessionDataContainer sessionDataContainer;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (handlerMethod.getMethod().getAnnotation(LoginNotRequired.class) != null || handlerMethod.getBeanType().getAnnotation(LoginNotRequired.class) != null) {
            return true;
        }
        boolean authorized = sessionDataContainer.getUserDto() != null;
        if (!authorized) {
            ObjectMapper mapper = new ObjectMapper();
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(mapper.writeValueAsString(new GeneralResponse(false, "UNAUTHORIZED")));
        }

        return authorized;
    }
}
