package com.project.manager.repository;

import com.project.manager.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IClientRepository extends JpaRepository<ClientEntity, UUID> {
}
