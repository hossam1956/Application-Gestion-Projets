package com.Projet_E2425G3_1.GestionProjets.testService;


import com.Projet_E2425G3_1.GestionProjets.dao.ProjectRepository;
import com.Projet_E2425G3_1.GestionProjets.dao.UserProjectRepository;
import com.Projet_E2425G3_1.GestionProjets.dao.UserRepository;
import com.Projet_E2425G3_1.GestionProjets.entities.Project;
import com.Projet_E2425G3_1.GestionProjets.entities.User;
import com.Projet_E2425G3_1.GestionProjets.entities.UserProject;
import com.Projet_E2425G3_1.GestionProjets.exception.ProjectNotFoundException;
import com.Projet_E2425G3_1.GestionProjets.exception.UserNotFoundException;
import com.Projet_E2425G3_1.GestionProjets.services.project.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserProjectRepository userProjectRepository;

    private ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        projectService = new ProjectServiceImpl(projectRepository, userRepository, userProjectRepository);
    }

    @Test
    void createProject_Success() {
        // Arrange
        Long managerId = 1L;
        String name = "Test Project";
        Date startDate = new Date();
        Date endDate = new Date();

        User manager = User.builder().id(managerId).build();
        Project project = Project.builder()
                .managerId(managerId)
                .nom(name)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        when(userRepository.findById(managerId)).thenReturn(Optional.of(manager));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Act
        Project result = projectService.createProject(managerId, name, startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getNom());
        assertEquals(managerId, result.getManagerId());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void createProject_UserNotFound() {
        // Arrange
        Long managerId = 1L;
        when(userRepository.findById(managerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () ->
                projectService.createProject(managerId, "Test", new Date(), new Date())
        );
    }

    @Test
    void findProjectById_Success() {
        // Arrange
        Long projectId = 1L;
        Project project = Project.builder().id(projectId).build();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // Act
        Optional<Project> result = projectService.findProjectById(projectId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(projectId, result.get().getId());
    }

    @Test
    void findProjectById_NotFound() {
        // Arrange
        Long projectId = 1L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProjectNotFoundException.class, () ->
                projectService.findProjectById(projectId)
        );
    }

    @Test
    void updateProject_Success() {
        // Arrange
        Long projectId = 1L;
        String newName = "Updated Project";
        Project existingProject = Project.builder()
                .id(projectId)
                .nom("Old Name")
                .build();

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenReturn(existingProject);

        // Act
        Project result = projectService.updateProject(projectId, newName, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(newName, result.getNom());
    }

    @Test
    void addUserToProject_Success() {
        // Arrange
        Long userId = 1L;
        Long projectId = 1L;
        User user = User.builder().id(userId).build();
        Project project = Project.builder().id(projectId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userProjectRepository.existsByUserIdAndProjectId(userId, projectId)).thenReturn(false);
        when(userProjectRepository.countByUserId(userId)).thenReturn(0L);

        // Act
        ResponseEntity<?> response = projectService.addUserToProject(userId, projectId);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        verify(userProjectRepository).save(any(UserProject.class));
    }

    @Test
    void deleteProject_Success() {
        // Arrange
        Long projectId = 1L;
        Project project = Project.builder().id(projectId).build();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // Act
        boolean result = projectService.deleteProject(projectId);

        // Assert
        assertTrue(result);
        verify(projectRepository).delete(project);
    }
}