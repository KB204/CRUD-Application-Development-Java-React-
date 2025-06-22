package net.demo.backendservice.dtos;

import java.util.List;

public record ErrorResponse(String error, List<String> details) {}
