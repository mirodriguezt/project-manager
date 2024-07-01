package com.project.manager.service;

import com.project.manager.entity.ClientEntity;
import com.project.manager.repository.IClientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
class ClientServiceTest {

    @Mock
    private IClientRepository clientRepositoryMock;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        clientService = new ClientService(clientRepositoryMock);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(clientRepositoryMock);
    }

    @Test
    public void should_return_a_client_entity_when_searching_by_id_and_exist() {
        UUID id = UUID.randomUUID();

        when(clientRepositoryMock.findById(id)).thenReturn(Optional.of(ClientEntity.builder().build()));

        Optional<ClientEntity> response = clientService.findById(id);
        assertNotNull(response);

        verify(clientRepositoryMock).findById(id);
    }

    @Test
    public void should_return_a_record_page_when_all_client_are_consulted() {
        List<ClientEntity> clientEntityList = List.of(new ClientEntity());
        PageRequest pageRequest = PageRequest.of(1, 10);
        Page<ClientEntity> pageClients = new PageImpl<>(clientEntityList, pageRequest, clientEntityList.size());
        when(clientRepositoryMock.findAll(any(PageRequest.class))).thenReturn(pageClients);

        Page<ClientEntity> response = clientService.findAll(pageRequest);

        verify(clientRepositoryMock).findAll(pageRequest);
        var returnedPageContentList = response.getContent();
        assertThat(response, is(notNullValue()));
        assertThat(returnedPageContentList.size(), is(1));
    }

    @Test
    public void should_add_a_client() {
        when(clientRepositoryMock.save(any(ClientEntity.class))).thenReturn(ClientEntity.builder().build());

        ClientEntity response = clientService.save(ClientEntity.builder().build());

        assertInstanceOf(ClientEntity.class, response);
        verify(clientRepositoryMock).save(any(ClientEntity.class));
    }

    @Test
    public void should_delete_a_client_when_exist() {
        ClientEntity clientEntity = ClientEntity.builder().build();

        doNothing().when(clientRepositoryMock).delete(any(ClientEntity.class));

        clientService.delete(clientEntity);

        verify(clientRepositoryMock).delete(any(ClientEntity.class));
    }
}