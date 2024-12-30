package com.Projet_E2425G3_1.GestionProjets.testController;

import com.Projet_E2425G3_1.GestionProjets.controller.ProjectController;
import com.Projet_E2425G3_1.GestionProjets.dao.ProjectRepository;
import com.Projet_E2425G3_1.GestionProjets.dao.UserProjectRepository;
import com.Projet_E2425G3_1.GestionProjets.entities.Project;
import com.Projet_E2425G3_1.GestionProjets.entities.User;
import com.Projet_E2425G3_1.GestionProjets.entities.UserProject;
import com.Projet_E2425G3_1.GestionProjets.exception.ProjectNotFoundException;
import com.Projet_E2425G3_1.GestionProjets.exception.UserNotFoundException;
import com.Projet_E2425G3_1.GestionProjets.services.project.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectControllerTest {

    private ProjectService projectService;
    private UserProjectRepository userProjectRepository;
    private ProjectRepository projectRepository;
    private ProjectController projectController;

    @BeforeEach
    void setUp() {
        projectService = mock(ProjectService.class);
        userProjectRepository = mock(UserProjectRepository.class);
        projectRepository = mock(ProjectRepository.class);
        projectController = new ProjectController(projectService, userProjectRepository, projectRepository);
    }

    @Test
    void testCreateProject_Success() {
        // Arrange
        Long managerId = 1L;
        String projectName = "Test Project";
        Date startDate = new Date();
        Date endDate = new Date();
        Project project = Project.builder().id(1L).build();

        when(userProjectRepository.countByUserId(managerId)).thenReturn(0L);
        when(projectRepository.countByManagerId(managerId)).thenReturn(0L);
        when(projectService.createProject(managerId, projectName, startDate, endDate)).thenReturn(project);

        // Act
        ResponseEntity<?> response = projectController.createProject(managerId, projectName, startDate, endDate);

        // Assert
        assertEquals(201, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("message"));
        verify(projectService, times(1)).createProject(managerId, projectName, startDate, endDate);
    }

    @Test
    void testCreateProject_UserAlreadyLinkedTo5Projects() {
        // Arrange
        Long managerId = 1L;

        when(userProjectRepository.countByUserId(managerId)).thenReturn(3L);
        when(projectRepository.countByManagerId(managerId)).thenReturn(2L);

        // Act
        ResponseEntity<?> response = projectController.createProject(managerId, "Test", new Date(), new Date());

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("This user already linked to 5 project", response.getBody());
    }

    @Test
    void testGetProjectById_Success() {
        // Arrange
        Long projectId = 1L;
        Project project = Project.builder().id(projectId).build();
        User user = User.builder().id(1L).username("Test User").build();
        UserProject userProject = UserProject.builder().user(user).build();

        when(projectService.findProjectById(projectId)).thenReturn(Optional.of(project));
        when(userProjectRepository.findAllByProjectId(projectId)).thenReturn(List.of(userProject));

        // Act
        ResponseEntity<?> response = projectController.getProjectById(projectId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("project :"));
        verify(projectService, times(1)).findProjectById(projectId);
        verify(userProjectRepository, times(1)).findAllByProjectId(projectId);
    }

    @Test
    void testGetProjectById_NotFound() {
        // Arrange
        Long projectId = 1L;

        when(projectService.findProjectById(projectId)).thenThrow(new ProjectNotFoundException("Project not found"));

        // Act
        ResponseEntity<?> response = projectController.getProjectById(projectId);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Project not found", response.getBody());
    }

    @Test
    void testAddUserToProject_Success() {
        // Arrange
        Long userId = 1L;
        Long projectId = 2L;

        ResponseEntity<String> expectedResponse = ResponseEntity.ok("User added to project");

        // Fix: Use `thenAnswer` to resolve the issue with generics
        when(userProjectRepository.countByProjectId(projectId)).thenReturn(10L);
        when(projectService.addUserToProject(eq(userId), eq(projectId))).thenAnswer(invocation -> expectedResponse);

        // Act
        ResponseEntity<?> actualResponse = projectController.addUserToProject(userId, projectId);

        // Assert
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals("User added to project", actualResponse.getBody());
        verify(projectService, times(1)).addUserToProject(userId, projectId);
    }



    @Test
    void testAddUserToProject_MaxMembersReached() {
        // Arrange
        Long projectId = 2L;

        when(userProjectRepository.countByProjectId(projectId)).thenReturn(16L);

        // Act
        ResponseEntity<?> response = projectController.addUserToProject(1L, projectId);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("This project has the maximum number of members", response.getBody());
    }

    @Test
    void testUpdateProject_Success() {
        // Arrange
        Long projectId = 1L;
        Project updatedProject = Project.builder().id(projectId).build();

        when(projectService.updateProject(eq(projectId), any(), any(), any())).thenReturn(updatedProject);

        // Act
        ResponseEntity<?> response = projectController.updateProject(projectId, "New Name", new Date(), new Date());

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("message"));
        verify(projectService, times(1)).updateProject(eq(projectId), any(), any(), any());
    }

    @Test
    void testDeleteProject_Success() {
        // Arrange
        Long projectId = 1L;

        when(projectService.deleteProject(projectId)).thenReturn(true);

        // Act
        ResponseEntity<?> response = projectController.deleteProject(projectId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("message"));
        verify(projectService, times(1)).deleteProject(projectId);
    }

    @Test
    void testDeleteProject_NotFound() {
        // Arrange
        Long projectId = 1L;

        when(projectService.deleteProject(projectId)).thenThrow(new ProjectNotFoundException("Project not found"));

        // Act
        ResponseEntity<?> response = projectController.deleteProject(projectId);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Project not found", response.getBody());
    }
}
