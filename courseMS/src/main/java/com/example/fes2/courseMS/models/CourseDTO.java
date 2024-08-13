package com.example.fes2.courseMS.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseDTO {
    @NotEmpty
    private String courseName;
    @NotNull
    private int courseDuration;
    @NotEmpty
    private String courseDescription;
    @NotEmpty
    private String technology;
    @NotEmpty
    private String launchUrl;
}
