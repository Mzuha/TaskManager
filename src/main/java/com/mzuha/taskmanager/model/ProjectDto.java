package com.mzuha.taskmanager.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProjectDto {
    @Null
    private Long id;

    @NotEmpty
    @Size(min = 2, max = 32)
    private String name;
}
