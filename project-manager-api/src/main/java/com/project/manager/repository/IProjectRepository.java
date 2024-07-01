package com.project.manager.repository;

import com.project.manager.entity.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface IProjectRepository extends JpaRepository<ProjectEntity, UUID> {

    boolean existsByClientIdAndDescription(UUID clientId, String description);

    @Query(value = "SELECT * FROM tb_project p WHERE p.client_id = ?1 and p.status = ?2 ORDER BY p.creation_date", nativeQuery = true)
    Page<ProjectEntity> findAllByClientIdAndStatus(Pageable pageable, UUID clientId, String statusValue);

    @Query(value = "SELECT * FROM tb_project p WHERE p.status = ?1 ORDER BY p.creation_date", nativeQuery = true)
    Page<ProjectEntity> findAllByStatus(Pageable pageable, String statusValue);

    @Modifying
    @Query(value = "UPDATE tb_project SET status = ?2 WHERE id = ?1", nativeQuery = true)
    void updateStatus(UUID itemId, String statusValue);

}
