package com.postechfiap.msequipamentos.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@Order(1)
public class TrackingFilter implements Filter {

    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    private static final String TRACE_MDC_KEY = "traceId";
    private static final String CORRELATION_MDC_KEY = "correlationId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String traceId = Optional.ofNullable(httpRequest.getHeader(TRACE_ID_HEADER))
                .orElse(UUID.randomUUID().toString());

        String correlationId = Optional.ofNullable(httpRequest.getHeader(CORRELATION_ID_HEADER))
                .orElse(UUID.randomUUID().toString());

        try {
            MDC.put(TRACE_MDC_KEY, traceId);
            MDC.put(CORRELATION_MDC_KEY, correlationId);

            httpResponse.setHeader(TRACE_ID_HEADER, traceId);
            httpResponse.setHeader(CORRELATION_ID_HEADER, correlationId);

            long startTime = System.currentTimeMillis();
            chain.doFilter(request, response);
            long duration = System.currentTimeMillis() - startTime;

            LoggerFactory.getLogger(TrackingFilter.class).info(
                    "Metodo={} URI={} Status={} Tempo={}ms",
                    httpRequest.getMethod(), httpRequest.getRequestURI(),
                    httpResponse.getStatus(), duration
            );

        } finally {
            MDC.clear();
        }
    }
}