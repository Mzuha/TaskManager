package com.mzuha.taskmanager.controller;

import com.mzuha.taskmanager.model.ProjectDto;
import com.mzuha.taskmanager.service.ProjectsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectsController {
    private final ProjectsService projectsService;

    public ProjectsController(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @GetMapping()
    public ResponseEntity<List<ProjectDto>> allProjects() {
        return new ResponseEntity<>(projectsService.getAllProjects(), HttpStatus.OK);
    }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProjectDto> createProject(@RequestBody @Valid ProjectDto projectDto) {
        return new ResponseEntity<>(projectsService.createProject(projectDto), HttpStatus.CREATED);
    }
}
