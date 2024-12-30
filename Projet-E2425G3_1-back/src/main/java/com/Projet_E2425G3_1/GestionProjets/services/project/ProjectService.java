package com.Projet_E2425G3_1.GestionProjets.services.project;

import com.Projet_E2425G3_1.GestionProjets.entities.Project;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Optional;

public interface ProjectService {

    Project createProject(Long idManager, String name, Date startDate, Date endDate);

    ResponseEntity<?> addUserToProject(Long userId, Long projectId);

    Project updateProject(Long id, String name, Date startDate, Date endDate);

    boolean deleteProject(Long id);

    Optional<Project> findProjectById(Long id);
}
