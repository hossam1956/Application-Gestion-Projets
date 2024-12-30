package com.Projet_E2425G3_1.GestionProjets.testController;


import com.Projet_E2425G3_1.GestionProjets.controller.TaskController;
import com.Projet_E2425G3_1.GestionProjets.entities.Task;
import com.Projet_E2425G3_1.GestionProjets.exception.*;
import com.Projet_E2425G3_1.GestionProjets.services.Task.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask_Success() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Sample Task");

        when(taskService.createTask(
                anyString(), anyString(), anyString(), anyString(), any(LocalDateTime.class), anyLong()))
                .thenReturn(task);

        ResponseEntity<?> response = taskController.createTask("Sample Task", "High", "Pending", "Description",
                LocalDateTime.now(), 1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(taskService, times(1)).createTask(anyString(), anyString(), anyString(), anyString(), any(LocalDateTime.class), anyLong());
    }

    @Test
    void testCreateTask_Exception() {
        when(taskService.createTask(
                anyString(), anyString(), anyString(), anyString(), any(LocalDateTime.class), anyLong()))
                .thenThrow(new RuntimeException());

        ResponseEntity<?> response = taskController.createTask("Sample Task", "High", "Pending", "Description",
                LocalDateTime.now(), 1L);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error while creating task", response.getBody());
    }

    @Test
    void testGetTaskById_Success() {
        Task task = new Task();
        task.setId(1L);
        when(taskService.getTaskById(1L)).thenReturn(task);

        ResponseEntity<?> response = taskController.getTaskById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(task, response.getBody());
    }

    @Test
    void testGetTaskById_TaskNotFound() {
        when(taskService.getTaskById(1L)).thenThrow(new TaskNotFoundException("Task not found"));

        ResponseEntity<?> response = taskController.getTaskById(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("This Task is not found", response.getBody());
    }

    @Test
    void testGetTasksByProjectId_Success() {
        List<Task> tasks = Arrays.asList(new Task(), new Task());
        when(taskService.getTasksByProjectId(1L)).thenReturn(tasks);

        ResponseEntity<?> response = taskController.getTasksByProjectId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(tasks, response.getBody());
    }

    @Test
    void testUpdateTask_Success() {
        Task task = new Task();
        task.setId(1L);

        when(taskService.updateTask(anyLong(), anyString(), anyString(), anyString(), anyString(), any(LocalDateTime.class), anyLong(), anyLong()))
                .thenReturn(task);

        ResponseEntity<?> response = taskController.updateTask(1L, "Updated Title", "Low", "In Progress", "Updated Description",
                2L, LocalDateTime.now(), 1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(task, response.getBody());
    }



  /*  @Test
    void testAssignTaskUser_Success() {
        // Mock the void method to do nothing
        doNothing().when(taskService).assignTaskUser(1L, 1L);

        // Call the controller method
        ResponseEntity<?> response = taskController.assignTaskUser(1L, 1L);

        // Assert the correct response
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Task ID: 1 is assigned successfully to this User ID : 1", response.getBody());
    }
*/



    @Test
    void testAssignTaskUser_Exception() {
        doThrow(new TaskNotFoundException("Task not found"))
                .when(taskService).assignTaskUser(1L, 1L);

        ResponseEntity<?> response = taskController.assignTaskUser(1L, 1L);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Task not found", response.getBody());
    }

    @Test
    void testDeleteTask_Success() {
        Task task = new Task();
        when(taskService.getTaskById(1L)).thenReturn(task);
        doNothing().when(taskService).deleteTask(1L);

        ResponseEntity<?> response = taskController.deleteTask(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }


}
