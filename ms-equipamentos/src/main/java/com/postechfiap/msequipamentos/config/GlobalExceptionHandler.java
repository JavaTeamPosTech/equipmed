package com.postechfiap.msequipamentos.config;

import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String TRACE_ID_KEY = "traceId";

    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail handleResponseStatusException(ResponseStatusException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getReason());
        problem.setTitle("Erro na Requisição");
        problem.setProperty(TRACE_ID_KEY, MDC.get(TRACE_ID_KEY));
        return problem;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "Conflito de dados: possivel duplicidade de Tag de Patrimonio ou erro de integridade.");
        problem.setTitle("Conflito de Integridade");
        problem.setType(URI.create("https://equipmed.com/errors/conflito-de-dados"));
        problem.setProperty(TRACE_ID_KEY, MDC.get(TRACE_ID_KEY));
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro interno inesperado.");
        problem.setTitle("Erro Interno do Servidor");
        problem.setProperty(TRACE_ID_KEY, MDC.get(TRACE_ID_KEY));
        return problem;
    }
}