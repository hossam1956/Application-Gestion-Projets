package com.Projet_E2425G3_1.GestionProjets.services.Task;

import com.Projet_E2425G3_1.GestionProjets.entities.Task;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {
    Task createTask(String title, String priority, String status, String desc, LocalDateTime deadline, Long projectId);

    Task getTaskById(Long id);

    Task assignTaskUser(Long taskId, Long userId);

    List<Task> getTasksByProjectId(Long projectId);

    Task updateTask(Long id, String title, String priority, String status, String desc,  LocalDateTime deadline, Long projectId, Long userAssigneeId);

    void deleteTask(Long id);
}
