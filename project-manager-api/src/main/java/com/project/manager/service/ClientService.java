package com.project.manager.service;

import com.project.manager.entity.ClientEntity;
import com.project.manager.repository.IClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {

    private final IClientRepository clientRepository;

    public ClientService(IClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Optional<ClientEntity> findById(UUID id) {
        return clientRepository.findById(id);
    }

    public Page<ClientEntity> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    @Transactional
    public ClientEntity save(ClientEntity clientEntity) {
        return clientRepository.save(clientEntity);
    }

    @Transactional
    public void delete(ClientEntity clientEntity) {
        clientRepository.delete(clientEntity);
    }
}
