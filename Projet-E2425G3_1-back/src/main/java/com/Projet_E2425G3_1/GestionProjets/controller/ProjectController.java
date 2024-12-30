package com.Projet_E2425G3_1.GestionProjets.controller;

import com.Projet_E2425G3_1.GestionProjets.dao.ProjectRepository;
import com.Projet_E2425G3_1.GestionProjets.dao.UserProjectRepository;
import com.Projet_E2425G3_1.GestionProjets.entities.Project;
import com.Projet_E2425G3_1.GestionProjets.entities.User;
import com.Projet_E2425G3_1.GestionProjets.entities.UserProject;
import com.Projet_E2425G3_1.GestionProjets.exception.ProjectNotFoundException;
import com.Projet_E2425G3_1.GestionProjets.exception.UserNotFoundException;
import com.Projet_E2425G3_1.GestionProjets.services.project.ProjectService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectService projectService;
    private final UserProjectRepository userProjectRepository;
    private final ProjectRepository projectRepository;

    public ProjectController(ProjectService projectService, UserProjectRepository userProjectRepository, ProjectRepository projectRepository) {
        this.projectService = projectService;
        this.userProjectRepository = userProjectRepository;
        this.projectRepository = projectRepository;
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestParam("idManager") Long idManager, @RequestParam("name") String name, @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate, @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        if (userProjectRepository.countByUserId(idManager) + projectRepository.countByManagerId(idManager) > 4) {
            return ResponseEntity.status(400).body("This user already linked to 5 project");
        }
        try {
            Project createdProject = projectService.createProject(idManager, name, startDate, endDate);
            return ResponseEntity.status(201).body(Map.of(
                    "message", "Project created successfully",
                    "projectId", createdProject.getId()
            ));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "An unexpected error occurred: " + e.getMessage()));
        }
    }


    @GetMapping("{id}")
    public ResponseEntity<?> getProjectById(@RequestParam("id") Long id) {
        try {
            Optional<Project> project = projectService.findProjectById(id);
            List<User> users = userProjectRepository.findAllByProjectId(id).stream().map(UserProject::getUser).toList();
            return ResponseEntity.status(200).body(Map.of(
                    "project :", project,
                    "users", users
            ));
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "An unexpected error occurred: " + e.getMessage()));
        }


    }

    @PostMapping("/addUser")
    public ResponseEntity<?> addUserToProject(
            @RequestParam Long userId,
            @RequestParam Long projectId
    ) {
        if (userProjectRepository.countByProjectId(projectId) <= 15) {
            return projectService.addUserToProject(userId, projectId);
        } else {
            return ResponseEntity.status(400).body("This project has the maximum number of members");
        }

    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@RequestParam(value = "id") Long id,
                                           @RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "StartDate", required = false) Date startDate,
                                           @RequestParam(value = "EndDate", required = false) Date endDate
    ) {
        try {
            Project updatedProject = projectService.updateProject(id, name, startDate, endDate);
            return ResponseEntity.status(200).body(Map.of(
                    "message", "Project updated successfully",
                    "projectId", updatedProject.getId()
            ));
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "An unexpected error occurred: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        try {
            boolean isDeleted = projectService.deleteProject(id);
            return ResponseEntity.status(200).body(Map.of("message", "Project deleted successfully"));
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "An unexpected error occurred: " + e.getMessage()));
        }
    }


}