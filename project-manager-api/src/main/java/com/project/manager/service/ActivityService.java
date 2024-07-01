package com.project.manager.service;

import com.project.manager.constant.StatusEnum;
import com.project.manager.entity.ActivityEntity;
import com.project.manager.entity.ProjectEntity;
import com.project.manager.exception.ActivityExistent;
import com.project.manager.exception.ProjectNonExistent;
import com.project.manager.repository.IActivityRepository;
import com.project.manager.repository.IProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ActivityService {

    private final static String LEGEND_PROJECT_NON_EXISTENT = "Non existent project";
    private final static String LEGEND_ACTIVITY_ALREADY_EXIST = "Activity already exists for this project";
    private final static String LEGEND_ACTIVITY_NON_EXISTENT = "Non existent activity";
    private final static String LEGEND_ACTIVITY_STATUS_UPDATED = "The activity status was updated";

    private final IActivityRepository activityRepository;
    private final IProjectRepository projectRepository;

    public ActivityService(IActivityRepository activityRepository, IProjectRepository projectRepository) {
        this.activityRepository = activityRepository;
        this.projectRepository = projectRepository;
    }

    public Optional<ActivityEntity> findById(UUID id) {
        return activityRepository.findById(id);
    }

    public Page<ActivityEntity> findByProjectIdAndStatus(Pageable pageable, UUID projectId, StatusEnum status) {
        return activityRepository.findAllByProjectIdAndStatus(pageable, projectId, status.getCode());
    }

    public Page<ActivityEntity> findByProjectId(Pageable pageable, UUID projectId) {
        return activityRepository.findAllByProjectId(pageable, projectId);
    }

    @Transactional
    public ActivityEntity add(UUID projectId, ActivityEntity activityEntity) {
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(projectId);
        if (optionalProjectEntity.isEmpty()) {
            throw new ProjectNonExistent(LEGEND_PROJECT_NON_EXISTENT);
        }

        boolean existActivity = activityRepository.existsByProjectIdAndDescription(projectId, activityEntity.getDescription());
        if (existActivity) {
            throw new ActivityExistent(LEGEND_ACTIVITY_ALREADY_EXIST);
        }

        activityEntity.setProject(optionalProjectEntity.get());
        activityEntity.setStatus(StatusEnum.OPEN);
        return activityRepository.save(activityEntity);
    }

    @Transactional
    public ActivityEntity update(ActivityEntity activityEntity) {
        return activityRepository.save(activityEntity);
    }

    @Transactional
    public String updateStatus(UUID id, StatusEnum status) {
        activityRepository.updateStatus(id, status.getCode());

        return LEGEND_ACTIVITY_STATUS_UPDATED;
    }

    @Transactional
    public void delete(ActivityEntity activityEntity) {
        activityRepository.delete(activityEntity);
    }
}
