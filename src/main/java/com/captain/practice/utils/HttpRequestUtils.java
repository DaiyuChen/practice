package com.captain.practice.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Wujing
 */
public abstract class HttpRequestUtils {


    public static HttpServletRequest httpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest();
        }
        return null;
    }

    public static HttpSession httpSession() {
        HttpServletRequest request = httpRequest();
        return request != null ? request.getSession() : null;
    }

    public static String constructUrl(HttpServletRequest request) {
        String queryString = request.getQueryString();
        return request.getRequestURL() + (queryString == null ? "" : "?" + queryString);
    }

    public static String userAgent() {
        HttpServletRequest request = httpRequest();
        if (request == null) {
            return "";
        }
        return userAgent(request);
    }

    public static String ipAddress(HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        return ip;
    }

    public static String userAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent == null ? "Unknown" : userAgent;
    }
}
