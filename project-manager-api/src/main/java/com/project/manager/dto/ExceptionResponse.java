package com.project.manager.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class ExceptionResponse {

    private String status;
    private List<Map<String, String>> errors;

}
