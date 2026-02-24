package com.jamillyferreira.veterinaryclinic.exception;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ValidationErrorResponse(
        Instant timestamp,
        Integer status,
        String error,
        Map<String, List<String>> validationErrors,
        String path
) {
}
