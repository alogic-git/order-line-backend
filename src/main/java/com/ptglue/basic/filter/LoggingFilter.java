package com.ptglue.basic.filter;

import com.ptglue.basic.exception.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class LoggingFilter extends OncePerRequestFilter {

  private static final Logger requestResponseLogger = LoggerFactory.getLogger(LoggingFilter.class);
  public static final String REQUEST_ID_ATTRIBUTE = "requestId";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    // UUID 기반 ID 생성
    request.setAttribute(REQUEST_ID_ATTRIBUTE, UUID.randomUUID().toString());

    String requestURI = request.getRequestURI();
    long startTime = System.currentTimeMillis(); // 요청이 시작된 시간

    HttpServletRequest requestToUse = request;
    if (!(request instanceof ContentCachingRequestWrapper)) {
      requestToUse = new ContentCachingRequestWrapper(request);
    }

    HttpServletResponse responseToUse = response;
    if (!(response instanceof ContentCachingResponseWrapper)) {
      responseToUse = new ContentCachingResponseWrapper(response);
    }

    try {
      filterChain.doFilter(requestToUse, responseToUse);
    } finally {
      long duration = System.currentTimeMillis() - startTime; // 요청 처리 시간
      if (!requestURI.contains("/swagger") && !requestURI.contains("/v2/api-docs") && !requestURI.equals("/")) {
        logRequest(requestToUse);
        logResponse(requestToUse, responseToUse, duration);
      }
      updateResponse(responseToUse);
    }
  }

  private void logRequest(HttpServletRequest request) {
    String requestId = (String) request.getAttribute(LoggingFilter.REQUEST_ID_ATTRIBUTE);
    ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
    if (wrapper != null) {

      String payload = new String(wrapper.getContentAsByteArray());
      String queryString = request.getQueryString() == null ? "" : "?"+request.getQueryString();
      String fullUrl = request.getRequestURI() + queryString;
      String method = request.getMethod();
      String remoteAddr = request.getRemoteAddr();

      String userAgent = request.getHeader("User-Agent");
      String xAuthToken = request.getHeader("X-Auth-Token");

      StringBuilder headers = new StringBuilder();
      headers.append("Accept").append(": ").append(request.getHeader("Accept")).append(", ");
      headers.append("Referer").append(": ").append(request.getHeader("Referer")).append(", ");

      requestResponseLogger.info("[{}][RequestFilter-Url-Header-Payload][{}] [Url]::{}, [Headers]:: {} [Payload]:: {}", requestId, method, fullUrl, headers, payload);
      requestResponseLogger.info("[{}][RequestFilter-Additional-Info] [X-Auth-Token]:: {}, [User-Agent]:: {}, [Remote Addr]:: {}", requestId, xAuthToken, userAgent, remoteAddr);
    }
  }

  private void logResponse(HttpServletRequest request, HttpServletResponse response, long duration) {
    String requestId = (String) request.getAttribute(LoggingFilter.REQUEST_ID_ATTRIBUTE);
    ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
    if (wrapper != null) {
      String payload = new String(wrapper.getContentAsByteArray());
      requestResponseLogger.info("[{}][ResponseFilter {}ms] [Status]:: {}, [Payload]:: {}", requestId, response.getStatus(), duration, payload);
    }
  }

  private void updateResponse(HttpServletResponse response) throws IOException {
    ContentCachingResponseWrapper responseWrapper =
            WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
    if (responseWrapper == null) {
      throw new InternalServerErrorException("서버 응답 도중 오류가 발생했습니다.");
    }
    responseWrapper.copyBodyToResponse();
  }
}