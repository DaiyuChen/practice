package com.captain.practice;

import com.captain.practice.utils.HttpRequestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PerformanceInterceptor extends HandlerInterceptorAdapter implements Filter {
    private static final Logger log = LogManager.getLogger(PerformanceInterceptor.class);


    @Value("${application.environment}")
    String environment;

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        startTimer(request);

        try {
            chain.doFilter(request, response);
        } finally {
            if (request instanceof HttpServletRequest) {
                stopTimer((HttpServletRequest) request, (HttpServletResponse) response);
            }
        }
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        startTimer(request);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        stopTimer(request, response);
    }

    private void startTimer(ServletRequest request) {
        long startTimeMills = System.currentTimeMillis();
        request.setAttribute("__start_time", startTimeMills);
    }

    private void stopTimer(HttpServletRequest request, HttpServletResponse response) {
        try {
            String ipAddress = HttpRequestUtils.ipAddress(request);
//            String username = SecurityUtils.currentUserShort().username();
            String username = "Anonymous";
            Long startTimeMillis = (Long) request.getAttribute("__start_time");
            long now = System.currentTimeMillis();
            long usedTime = now - startTimeMillis;
            String url = HttpRequestUtils.constructUrl(request);
            int status = response.getStatus();
            if (log.isDebugEnabled()) {
                String logMessage = "Took " + usedTime + " ms " + "[" + ipAddress + "] [" + username + "] " + "[" + status + "] " + request.getMethod() + " " + url;
                if (usedTime > 5000) {
                    log.error(logMessage);
                } else if (usedTime > 2000) {
                    warn(logMessage);
                } else if (usedTime > 100) {
                    info(logMessage);
                } else if (usedTime > 30) {
                    debug(logMessage);
                } else {
                    info(logMessage);
                }
            }
        } catch (Exception e) {
            log.error("Could not performance log request. Ignoring problem, since this interceptor should not influence the normal application flow", e);
        }
    }

    protected void debug(String logMessage) {
        log.debug(logMessage);
    }

    protected void info(String logMessage) {
        log.info(logMessage);
    }

    protected void warn(String logMessage) {
        log.warn(logMessage);
    }

    private boolean shouldPushToMonitor() {
        return "prod".equalsIgnoreCase(environment);
    }


}
