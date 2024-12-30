package com.Projet_E2425G3_1.GestionProjets.dao;

import com.Projet_E2425G3_1.GestionProjets.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    @Query("SELECT COUNT(p) FROM Project p WHERE p.managerId = :managerId")
    Long countByManagerId(@Param("managerId") Long managerId);

    List<Project> findAllByManagerId(Long managerId);
}