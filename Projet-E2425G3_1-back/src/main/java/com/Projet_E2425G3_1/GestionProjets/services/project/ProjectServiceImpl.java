package com.Projet_E2425G3_1.GestionProjets.services.project;


import com.Projet_E2425G3_1.GestionProjets.dao.ProjectRepository;
import com.Projet_E2425G3_1.GestionProjets.dao.UserProjectRepository;
import com.Projet_E2425G3_1.GestionProjets.dao.UserRepository;
import com.Projet_E2425G3_1.GestionProjets.entities.Project;
import com.Projet_E2425G3_1.GestionProjets.entities.User;
import com.Projet_E2425G3_1.GestionProjets.entities.UserProject;
import com.Projet_E2425G3_1.GestionProjets.entities.ids.UserProjectId;
import com.Projet_E2425G3_1.GestionProjets.exception.ProjectNotFoundException;
import com.Projet_E2425G3_1.GestionProjets.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserProjectRepository userProjectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository, UserProjectRepository userProjectRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.userProjectRepository = userProjectRepository;
    }

    @Override
    @Transactional
    public Project createProject(Long idManager, String name, Date startDate, Date endDate) {
        User manager = userRepository.findById(idManager)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + idManager + " does not exist."));

        Project project = Project.builder()
                .managerId(idManager)
                .nom(name)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        return projectRepository.save(project);
    }


    @Override
    public Optional<Project> findProjectById(Long id) {
        Optional<Project> project = projectRepository.findById(id).isPresent() ? projectRepository.findById(id) : Optional.empty();
        if (project.isEmpty()) {
            throw new ProjectNotFoundException("Project ID :" + id + " not found");
        }
        return project;
    }


    @Override
    public Project updateProject(Long id, String name, Date startDate, Date endDate) {
        Project existingProject = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException("Project ID : " + id +" not found"));
        existingProject.setNom(name != null ? name : existingProject.getNom());
        existingProject.setStartDate(startDate != null ? startDate : existingProject.getStartDate());
        existingProject.setEndDate(endDate != null ? endDate : existingProject.getEndDate());

        return projectRepository.save(existingProject);
    }

    @Override
    public boolean deleteProject(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(""));
        projectRepository.delete(project);
        return true;
    }

    @Override
    @Transactional
    public ResponseEntity<?> addUserToProject(Long userId, Long projectId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Project> project = projectRepository.findById(projectId);
        if (user.isEmpty() || project.isEmpty()) {
            return ResponseEntity.status(404).body("User or Project not found");
        }
        if (userProjectRepository.existsByUserIdAndProjectId(userId, projectId) || Objects.equals(project.get().getManagerId(), userId)) {
            return ResponseEntity.status(400).body("This user already linked to this project");
        }
        if (userProjectRepository.countByUserId(userId) + projectRepository.countByManagerId(userId) > 5) {
            return ResponseEntity.status(400).body("This user already linked to 5 project");
        }
        UserProjectId userProjectId = new UserProjectId(userId, projectId);
        UserProject userProject = UserProject.builder()
                .id(userProjectId)
                .project(project.get())
                .user(user.get())
                .build();
        userProjectRepository.save(userProject);
        return ResponseEntity.status(200).body(
                Map.of(
                        "message", "user ID :" + userId + " is added successfully to project ID :" + projectId
                )
        );

    }

}