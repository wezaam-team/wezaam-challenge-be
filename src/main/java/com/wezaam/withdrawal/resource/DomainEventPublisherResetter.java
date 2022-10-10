package com.wezaam.withdrawal.resource;

import com.wezaam.common.domain.model.DomainEventPublisher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class DomainEventPublisherResetter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        DomainEventPublisher.instance().reset();

        return true;
    }
}
