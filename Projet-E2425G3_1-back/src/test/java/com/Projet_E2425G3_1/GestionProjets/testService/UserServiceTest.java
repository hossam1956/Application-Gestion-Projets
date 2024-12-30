package com.Projet_E2425G3_1.GestionProjets.testService;

import com.Projet_E2425G3_1.GestionProjets.dao.ProjectRepository;
import com.Projet_E2425G3_1.GestionProjets.dao.UserProjectRepository;
import com.Projet_E2425G3_1.GestionProjets.dao.UserRepository;
import com.Projet_E2425G3_1.GestionProjets.dto.PageResponse;
import com.Projet_E2425G3_1.GestionProjets.entities.Project;
import com.Projet_E2425G3_1.GestionProjets.entities.User;
import com.Projet_E2425G3_1.GestionProjets.exception.EmailAlreadyUsedException;
import com.Projet_E2425G3_1.GestionProjets.exception.UserNotFoundException;
import com.Projet_E2425G3_1.GestionProjets.services.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserProjectRepository userProjectRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() {
        // Arrange
        String username = "testUser";
        String email = "test@example.com";
        String password = "password";
        String profileImageUrl = "profile.jpg";
        User mockUser = User.builder().id(1L).username(username).email(email).password(password).profileImageUrl(profileImageUrl).build();

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        User createdUser = userService.createUser(username, email, password, profileImageUrl);

        // Assert
        assertNotNull(createdUser);
        assertEquals(username, createdUser.getUsername());
        assertEquals(email, createdUser.getEmail());
        verify(userRepository, times(1)).existsByEmail(email);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_EmailAlreadyUsed() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // Act & Assert
        assertThrows(EmailAlreadyUsedException.class, () -> userService.createUser("username", email, "password", "profile.jpg"));
    }

    @Test
    void testUpdateUser_Success() {
        // Arrange
        Long userId = 1L;
        String newUsername = "updatedUser";
        User mockUser = User.builder().id(userId).username("oldUser").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        User updatedUser = userService.updateUser(userId, newUsername, null, null, null);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(newUsername, updatedUser.getUsername());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userId, "newUsername", null, null, null));
    }

  /*  @Test
    void testFindUserById_Success() {
        // Arrange
        Long userId = 1L;
        User mockUser = User.builder().id(userId).username("testUser").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        Optional<User> foundUser = userService.findUserById(userId);

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUsername());
        verify(userRepository, times(1)).findById(userId);
    }
*/
    @Test
    void testDeleteUserById_Success() {
        // Arrange
        Long userId = 1L;
        List<Project> mockProjects = new ArrayList<>();
        mockProjects.add(Project.builder().id(1L).build());

        when(userRepository.existsById(userId)).thenReturn(true);
        when(projectRepository.findAllByManagerId(userId)).thenReturn(mockProjects);

        // Act
        userService.deleteUserById(userId);

        // Assert
        verify(userProjectRepository, times(1)).deleteByProjectId(1L);
        verify(projectRepository, times(1)).delete(mockProjects.get(0));
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUserById_UserNotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(userId));
    }

   /* @Test
    void testFindAllUsers_WithSearch() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = Collections.singletonList(User.builder().id(1L).username("testUser").build());
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());

        when(userRepository.findAllByUsernameContainingIgnoreCase("test", pageable)).thenReturn(userPage);

        // Act
        PageResponse<User> result = userService.findAllUsers(10, 0, "test");

        // Assert
        assertNotNull(result); // Ensure result is not null
        assertEquals(1, result.getContent().size()); // Check size of content
        assertEquals("testUser", result.getContent().get(0).getUsername()); // Validate content data

        // Verify the repository interaction
        verify(userRepository, times(1)).findAllByUsernameContainingIgnoreCase("test", pageable);
    }
*/

}