package com.mzuha.taskmanager.service;

import com.mzuha.taskmanager.model.ProjectDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectsService {
    private final List<ProjectDto> projectDtos = new ArrayList<>();

    public ProjectDto createProject(ProjectDto projectDto) {
        projectDtos.add(projectDto);
        return projectDto;
    }

    public List<ProjectDto> getAllProjects() {
        return projectDtos;
    }
}
