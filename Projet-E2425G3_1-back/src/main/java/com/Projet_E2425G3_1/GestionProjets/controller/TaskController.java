package com.Projet_E2425G3_1.GestionProjets.controller;

import com.Projet_E2425G3_1.GestionProjets.entities.Task;
import com.Projet_E2425G3_1.GestionProjets.exception.*;
import com.Projet_E2425G3_1.GestionProjets.services.Task.TaskService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<?> createTask(
            @RequestParam String title,
            @RequestParam String priority,
            @RequestParam String status,
            @RequestParam(name = "description", required = false) String desc,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime deadline,
            @RequestParam Long projectId) {
        try {
            Task createdTask = taskService.createTask(title, priority, status, desc, deadline, projectId);
            return ResponseEntity.ok(createdTask);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error while creating task");
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        try {
            Task task = taskService.getTaskById(id);
            return ResponseEntity.ok(task);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(404).body("This Task is not found");
        }

    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getTasksByProjectId(@PathVariable Long projectId) {
        try {
            List<Task> tasks = taskService.getTasksByProjectId(projectId);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Maybe this project doesn't exist or doesn't have any tasks");

        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(
            @PathVariable Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String status,
            @RequestParam(name = "description", required = false) String desc,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime deadline,
            @RequestParam Long projectId) {
        try {
            Task updatedTask = taskService.updateTask(id, title, priority, status, desc, deadline, projectId, userId);
            return ResponseEntity.ok(updatedTask);
        } catch (UserNotFoundException | TaskNotFoundException | ProjectNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }

    }

    @PostMapping("/addUser")
    public ResponseEntity<?> assignTaskUser(
            @RequestParam Long userId,
            @RequestParam Long taskId
    ) {
        try {
            taskService.assignTaskUser(userId, taskId);
            return ResponseEntity.status(200).body("Task ID: " + taskId + " is assigned successfully to this User ID : " + userId);

        } catch (UserNotFoundException | TaskNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (UserNotAddedToProjectException | TaskAlreadyAssignedException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTask(@RequestParam("id") Long id) {
        Task task = taskService.getTaskById(id);
        if (task != null) {
            taskService.deleteTask(id);
            return ResponseEntity.status(200).body(Map.of(
                    "message", "task deleted successfully",
                    "task ID", id
            ));
        } else {
            return ResponseEntity.status(404).body(Map.of(
                    "message", "task not found "
            ));
        }

    }
}