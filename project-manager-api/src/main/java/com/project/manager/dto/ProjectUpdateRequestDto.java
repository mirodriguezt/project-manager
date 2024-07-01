package com.project.manager.dto;

import com.project.manager.constant.StatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProjectUpdateRequestDto {
    @NotBlank
    @Size(max = 100)
    private String description;

    private StatusEnum status;
}
