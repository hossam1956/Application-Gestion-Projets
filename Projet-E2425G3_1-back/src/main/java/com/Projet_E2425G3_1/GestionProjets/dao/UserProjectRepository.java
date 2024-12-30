package com.Projet_E2425G3_1.GestionProjets.dao;

import com.Projet_E2425G3_1.GestionProjets.entities.UserProject;
import com.Projet_E2425G3_1.GestionProjets.entities.ids.UserProjectId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProject, UserProjectId> {
    @Query("SELECT up FROM UserProject up WHERE up.user.id = :userId")
    List<UserProject> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(up) FROM UserProject up WHERE up.project.id = :projectId")
    Long countByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT CASE WHEN COUNT(up) > 0 THEN true ELSE false END " +
            "FROM UserProject up " +
            "WHERE up.user.id = :userId AND up.project.id = :projectId")
    boolean existsByUserIdAndProjectId(@Param("userId") Long userId, @Param("projectId") Long projectId);

    @Query("SELECT COUNT(up) FROM UserProject up WHERE up.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);

    @Query("SELECT up FROM UserProject up WHERE up.project.id = :projectId")
    List<UserProject> findAllByProjectId(@Param("projectId") Long projectId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserProject up WHERE up.project.id = :projectId")
    void deleteByProjectId(@Param("projectId") Long projectId);
}
