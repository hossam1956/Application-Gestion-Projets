package com.Projet_E2425G3_1.GestionProjets.testController;

import com.Projet_E2425G3_1.GestionProjets.controller.UserController;
import com.Projet_E2425G3_1.GestionProjets.dao.ProjectRepository;
import com.Projet_E2425G3_1.GestionProjets.dao.UserProjectRepository;
import com.Projet_E2425G3_1.GestionProjets.dto.PageResponse;
import com.Projet_E2425G3_1.GestionProjets.entities.Project;
import com.Projet_E2425G3_1.GestionProjets.entities.User;
import com.Projet_E2425G3_1.GestionProjets.entities.UserProject;
import com.Projet_E2425G3_1.GestionProjets.exception.EmailAlreadyUsedException;
import com.Projet_E2425G3_1.GestionProjets.exception.UserNotFoundException;
import com.Projet_E2425G3_1.GestionProjets.services.s3.S3Service;
import com.Projet_E2425G3_1.GestionProjets.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private S3Service s3Service;

    @Mock
    private UserService userService;

    @Mock
    private UserProjectRepository userProjectRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        try (AutoCloseable mocks = MockitoAnnotations.openMocks(this)) {
            // Initialization done
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize mocks", e);
        }
    }

    @Test
    void getAllUser_shouldReturnPageResponse() {
        PageResponse<User> pageResponse = new PageResponse<User>();
        when(userService.findAllUsers(10, 1, "")).thenReturn(pageResponse);

        ResponseEntity<PageResponse<User>> response = userController.getAllUser(10, 1, "");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(pageResponse, response.getBody());
    }

  /*  @Test
    void getUserById_shouldReturnUserAndProjects() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        UserProject userProject = new UserProject();
        userProject.setProject(new Project());
        List<UserProject> userProjects = List.of(userProject);
        List<Project> managedProjects = List.of(new Project());

        when(userService.findUserById(userId)).thenReturn(Optional.of(user));
        when(userProjectRepository.findAllByUserId(userId)).thenReturn(userProjects);
        when(projectRepository.findAllByManagerId(userId)).thenReturn(managedProjects);

        ResponseEntity<?> response = userController.getUserById(userId);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(user, body.get("user"));
        assertTrue(((Set<?>) body.get("projects")).size() > 0);
    }
*/
    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getContentType()).thenReturn("image/jpeg");
        when(s3Service.isSupportedImageFormat("image/jpeg")).thenReturn(true);
        when(s3Service.uploadFile(mockFile)).thenReturn("mockUrl");

        User user = new User();
        user.setId(1L);
        when(userService.createUser(anyString(), anyString(), anyString(), anyString())).thenReturn(user);

        ResponseEntity<?> response = userController.createUser("username", "email@test.com", "password", mockFile);

        assertEquals(201, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(1L, body.get("userId"));
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        Long userId = 1L;
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getContentType()).thenReturn("image/jpeg");
        when(s3Service.isSupportedImageFormat("image/jpeg")).thenReturn(true);
        when(s3Service.uploadFile(mockFile)).thenReturn("mockUrl");

        User user = new User();
        user.setId(userId);
        when(userService.updateUser(eq(userId), any(), any(), any(), any())).thenReturn(user);

        ResponseEntity<?> response = userController.updateUser(userId, "username", "email@test.com", "password", mockFile);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(userId, body.get("userId"));
    }

    @Test
    void deleteUser_shouldReturnSuccessMessage() {
        Long userId = 1L;
        when(userService.findUserById(userId)).thenReturn(Optional.of(new User()));

        ResponseEntity<?> response = userController.deleteUser(userId);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(userId, body.get("userId"));
    }


}
