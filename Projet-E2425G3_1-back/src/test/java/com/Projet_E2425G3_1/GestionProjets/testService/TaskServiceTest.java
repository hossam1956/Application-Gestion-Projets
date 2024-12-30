package com.Projet_E2425G3_1.GestionProjets.testService;

import com.Projet_E2425G3_1.GestionProjets.dao.ProjectRepository;
import com.Projet_E2425G3_1.GestionProjets.dao.TaskRepository;
import com.Projet_E2425G3_1.GestionProjets.dao.UserProjectRepository;
import com.Projet_E2425G3_1.GestionProjets.dao.UserRepository;
import com.Projet_E2425G3_1.GestionProjets.entities.Project;
import com.Projet_E2425G3_1.GestionProjets.entities.Task;
import com.Projet_E2425G3_1.GestionProjets.entities.User;
import com.Projet_E2425G3_1.GestionProjets.exception.*;
import com.Projet_E2425G3_1.GestionProjets.services.Task.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProjectRepository userProjectRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask_Success() {
        Project mockProject = new Project();
        mockProject.setId(1L);

        Task mockTask = new Task();
        mockTask.setTitle("New Task");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(mockProject));
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);

        Task createdTask = taskService.createTask("New Task", "High", "Open", "Description", LocalDateTime.now(), 1L);

        assertNotNull(createdTask);
        assertEquals("New Task", createdTask.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testCreateTask_ProjectNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask("New Task", "High", "Open", "Description", LocalDateTime.now(), 1L);
        });

        assertEquals("Project with ID 1 not found", exception.getMessage());
    }

    @Test
    void testGetTaskById_Success() {
        Task mockTask = new Task();
        mockTask.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(mockTask));

        Task task = taskService.getTaskById(1L);

        assertNotNull(task);
        assertEquals(1L, task.getId());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTaskById_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskById(1L);
        });

        assertEquals("Task with ID 1 not found", exception.getMessage());
    }

    @Test
    void testGetTasksByProjectId_Success() {
        List<Task> mockTasks = new ArrayList<>();
        mockTasks.add(new Task());
        mockTasks.add(new Task());

        when(taskRepository.findByProjectId(1L)).thenReturn(mockTasks);

        List<Task> tasks = taskService.getTasksByProjectId(1L);

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findByProjectId(1L);
    }

    @Test
    void testGetTasksByProjectId_NoTasksFound() {
        when(taskRepository.findByProjectId(1L)).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.getTasksByProjectId(1L);
        });

        assertEquals("No tasks found for Project ID 1", exception.getMessage());
    }

    @Test
    void testAssignTaskUser_Success() {
        Task mockTask = new Task();
        User mockUser = new User();
        Project mockProject = new Project();
        mockProject.setId(1L);
        mockTask.setProject(mockProject);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(mockTask));
        when(userProjectRepository.existsByUserIdAndProjectId(1L, 1L)).thenReturn(true);
        when(taskRepository.existsByTaskIdAndUserId(1L, 1L)).thenReturn(false);
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);

        Task assignedTask = taskService.assignTaskUser(1L, 1L);

        assertNotNull(assignedTask);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testAssignTaskUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            taskService.assignTaskUser(1L, 1L);
        });

        assertEquals("User with ID 1 does not exist.", exception.getMessage());
    }

    @Test
    void testAssignTaskUser_TaskNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.assignTaskUser(1L, 1L);
        });

        assertEquals("Task with ID 1 does not exist.", exception.getMessage());
    }

    @Test
    void testDeleteTask_Success() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTask_NotFound() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.deleteTask(1L);
        });

        assertEquals("Task with ID 1 not found", exception.getMessage());
    }
}
