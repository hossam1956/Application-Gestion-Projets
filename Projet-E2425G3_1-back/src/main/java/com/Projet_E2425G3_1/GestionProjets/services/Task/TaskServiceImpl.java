package com.Projet_E2425G3_1.GestionProjets.services.Task;

import com.Projet_E2425G3_1.GestionProjets.dao.ProjectRepository;
import com.Projet_E2425G3_1.GestionProjets.dao.TaskRepository;
import com.Projet_E2425G3_1.GestionProjets.dao.UserProjectRepository;
import com.Projet_E2425G3_1.GestionProjets.dao.UserRepository;
import com.Projet_E2425G3_1.GestionProjets.entities.Project;
import com.Projet_E2425G3_1.GestionProjets.entities.Task;
import com.Projet_E2425G3_1.GestionProjets.entities.User;
import com.Projet_E2425G3_1.GestionProjets.exception.*;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserProjectRepository userProjectRepository;


    public TaskServiceImpl(TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository,UserProjectRepository userProjectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.userProjectRepository=userProjectRepository;
    }

    @Override
    public Task createTask(String title, String priority, String status,String desc, LocalDateTime deadline, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project with ID " + projectId + " not found"));

        Task task = Task.builder()
                .title(title)
                .priority(priority)
                .status(status)
                .description(desc)
                .userAssignee(null)
                .deadline(deadline)
                .project(project)
                .build();

        return taskRepository.save(task);
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
    }

    @Override
    public List<Task> getTasksByProjectId(Long projectId){
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        if (tasks.isEmpty()) {
            throw new IllegalArgumentException("No tasks found for Project ID " + projectId);
        }
        return tasks;
    }

    @Transactional
    @Override
    public Task assignTaskUser(Long userId,Long taskId){
        Optional<User> user = userRepository.findById(userId);
        Optional<Task> task = taskRepository.findById(taskId);
        boolean alreadyAssigned = taskRepository.existsByTaskIdAndUserId(taskId,userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with ID " + userId + " does not exist.");
        }
        if (task.isEmpty()) {
            throw new TaskNotFoundException("Task with ID " + taskId + " does not exist.");
        }
        boolean userNotAddedToProject = userProjectRepository.existsByUserIdAndProjectId(userId,task.get().getProject().getId());
        if(!userNotAddedToProject){
            throw new UserNotAddedToProjectException("This user ID:"+userId+"is not added to this project ID: "+task.get().getProject().getId());

        }
        if (alreadyAssigned) {
            throw new TaskAlreadyAssignedException("Task ID " + taskId + " is already assigned to User ID " + userId + ".");
        }
        task.get().setUserAssignee(user.get());
        return taskRepository.save(task.get());


    }

    @Override
    public Task updateTask(Long id, String title, String priority, String status,String desc, LocalDateTime deadline, Long projectId, Long userAssigneeId) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));

        Project project = (projectId != null) ? projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + projectId + " not found"))
                : existingTask.getProject();
        User userAssignee = (userAssigneeId != null) ? userRepository.findById(userAssigneeId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userAssigneeId + " not found"))
                : existingTask.getUserAssignee();

        Task updatedTask = Task.builder()
                .id(existingTask.getId())
                .title(title != null ? title : existingTask.getTitle())
                .priority(priority != null ? priority : existingTask.getPriority())
                .status(status != null ? status : existingTask.getStatus())
                .description(desc != null ? desc:existingTask.getDescription())
                .userAssignee(userAssignee != null ? userAssignee : existingTask.getUserAssignee())
                .deadline(deadline != null ? deadline : existingTask.getDeadline())
                .project(project)
                .build();

        return taskRepository.save(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Task with ID " + id + " not found");
        }
        taskRepository.deleteById(id);
    }
}
