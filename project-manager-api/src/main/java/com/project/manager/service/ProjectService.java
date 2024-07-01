package com.project.manager.service;

import com.project.manager.constant.StatusEnum;
import com.project.manager.entity.ClientEntity;
import com.project.manager.entity.ProjectEntity;
import com.project.manager.exception.ClientNonExistent;
import com.project.manager.exception.ProjectExistent;
import com.project.manager.repository.IClientRepository;
import com.project.manager.repository.IProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectService {

    private final static String LEGEND_CLIENT_NON_EXISTENT = "Non existent client";
    private final static String LEGEND_PROJECT_ALREADY_EXIST = "Project already exists for this client";
    private final static String LEGEND_PROJECT_NON_EXISTENT = "Non existent project";
    private final static String LEGEND_PROJECT_STATUS_UPDATED = "The project status was updated";

    private final IProjectRepository projectRepository;
    private final IClientRepository clientRepository;

    public ProjectService(IProjectRepository projectRepository, IClientRepository clientRepository) {
        this.projectRepository = projectRepository;
        this.clientRepository = clientRepository;
    }

    public Optional<ProjectEntity> findById(UUID id) {
        return projectRepository.findById(id);
    }

    public Page<ProjectEntity> findByClientIdAndStatus(Pageable pageable, UUID clientId, StatusEnum status) {
        return projectRepository.findAllByClientIdAndStatus(pageable, clientId, status.getCode());
    }

    public Page<ProjectEntity> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    public Page<ProjectEntity> findAllByStatus(Pageable pageable, StatusEnum status) {
        return projectRepository.findAllByStatus(pageable, status.getCode());
    }

    @Transactional
    public ProjectEntity add(UUID clientId, ProjectEntity projectEntity) {
        Optional<ClientEntity> optionalClientEntity = clientRepository.findById(clientId);
        if (optionalClientEntity.isEmpty()) {
            throw new ClientNonExistent(LEGEND_CLIENT_NON_EXISTENT);
        }

        boolean existProject = projectRepository.existsByClientIdAndDescription(clientId, projectEntity.getDescription());
        if (existProject) {
            throw new ProjectExistent(LEGEND_PROJECT_ALREADY_EXIST);
        }

        projectEntity.setClient(optionalClientEntity.get());
        projectEntity.setStatus(StatusEnum.OPEN);

        return projectRepository.save(projectEntity);
    }

    @Transactional
    public ProjectEntity update(ProjectEntity projectEntity) {
        return projectRepository.save(projectEntity);
    }

    @Transactional
    public String updateStatus(UUID id, StatusEnum status) {
        projectRepository.updateStatus(id, status.getCode());

        return LEGEND_PROJECT_STATUS_UPDATED;
    }

    @Transactional
    public void delete(ProjectEntity projectEntity) {
        projectRepository.delete(projectEntity);
    }
}
