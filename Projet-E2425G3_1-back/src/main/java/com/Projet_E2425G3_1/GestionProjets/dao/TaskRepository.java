package com.Projet_E2425G3_1.GestionProjets.dao;


import com.Projet_E2425G3_1.GestionProjets.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END FROM Task t WHERE t.id = :taskId AND t.userAssignee.id = :userId")
    boolean existsByTaskIdAndUserId(@Param("taskId") Long taskId, @Param("userId") Long userId);

}