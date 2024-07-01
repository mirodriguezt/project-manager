package com.project.manager.repository;

import com.project.manager.entity.ActivityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface IActivityRepository extends JpaRepository<ActivityEntity, UUID> {

    boolean existsByProjectIdAndDescription(UUID projectId, String description);

    @Query(value = "SELECT * FROM tb_activity WHERE project_id = ?1 and status = ?2 ORDER BY creation_date", nativeQuery = true)
    Page<ActivityEntity> findAllByProjectIdAndStatus(Pageable pageable, UUID projectId, String statusValue);

    @Query(value = "SELECT * FROM tb_activity WHERE project_id = ?1 ORDER BY creation_date", nativeQuery = true)
    Page<ActivityEntity> findAllByProjectId(Pageable pageable, UUID projectId);

    @Modifying
    @Query(value = "UPDATE tb_activity SET status = ?2 WHERE id = ?1", nativeQuery = true)
    void updateStatus(UUID itemId, String statusValue);
}
