package com.project.manager.controller;

import com.project.manager.dto.ClientDto;
import com.project.manager.dto.PageRecordDto;
import com.project.manager.entity.ClientEntity;
import com.project.manager.service.ClientService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
class ClientControllerTest {

    @Mock
    private ClientService clientServiceMock;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        clientController = new ClientController(clientServiceMock);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(clientServiceMock);
    }

    @Test
    public void should_return_a_client_entity_when_searching_by_id_and_exist() {
        UUID id = UUID.randomUUID();

        when(clientServiceMock.findById(id)).thenReturn(Optional.of(ClientEntity.builder().build()));

        ResponseEntity<Object> response = clientController.getClientById(id);

        assertNotNull(response);
        assertThat(response.getStatusCodeValue(), is(HttpStatus.OK.value()));

        var clientEntityResponse = response.getBody();
        assertInstanceOf(ClientEntity.class, clientEntityResponse);

        verify(clientServiceMock).findById(id);
    }

    @Test
    public void should_return_a_empty_client_entity_when_searching_by_id_and_not_exist() {
        UUID id = UUID.randomUUID();

        when(clientServiceMock.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = clientController.getClientById(id);

        assertNotNull(response);
        assertThat(response.getStatusCodeValue(), is(HttpStatus.NOT_FOUND.value()));

        var clientEntityResponse = response.getBody();
        assertInstanceOf(String.class, clientEntityResponse);

        verify(clientServiceMock).findById(id);
    }

    @Test
    public void should_return_a_record_page_when_all_client_are_consulted() {
        List<ClientEntity> clientEntityList = List.of(new ClientEntity());
        Page<ClientEntity> pageClients = new PageImpl<>(clientEntityList, PageRequest.of(0, 10), clientEntityList.size());

        when(clientServiceMock.findAll(any(PageRequest.class))).thenReturn(pageClients);

        ResponseEntity<PageRecordDto> response = clientController.getAllClients(0, 10);

        PageRecordDto pageRecordDto = response.getBody();

        assert pageRecordDto != null;
        assertThat(pageRecordDto.actualPage(), is(0));
        assertThat(pageRecordDto.totalPages(), is(1));
        assertThat(pageRecordDto.totalRecords(), is(1L));

        var itemList = pageRecordDto.itemList();
        assertThat(itemList.size(), is(1));
        assertThat(response.getStatusCodeValue(), is(HttpStatus.OK.value()));

        verify(clientServiceMock).findAll(any(PageRequest.class));
    }

    @Test
    public void should_add_a_client() {
        when(clientServiceMock.save(any(ClientEntity.class))).thenReturn(ClientEntity.builder().build());

        ResponseEntity<Object> response = clientController.addClient(ClientDto.builder().build());

        assertThat(response.getStatusCodeValue(), is(HttpStatus.CREATED.value()));
        assertInstanceOf(ClientEntity.class, response.getBody());

        verify(clientServiceMock).save(any(ClientEntity.class));
    }

    @Test
    public void should_update_a_client_when_exist() {
        UUID id = UUID.randomUUID();

        ClientEntity clientEntity = ClientEntity.builder().build();

        when(clientServiceMock.findById(id)).thenReturn(Optional.of(clientEntity));
        when(clientServiceMock.save(any(ClientEntity.class))).thenReturn(clientEntity);

        ResponseEntity<Object> response = clientController.updateClientById(id, ClientDto.builder().build());

        assertThat(response.getStatusCodeValue(), is(HttpStatus.OK.value()));

        verify(clientServiceMock).findById(id);
        verify(clientServiceMock).save(any(ClientEntity.class));
    }

    @Test
    public void should_not_update_a_client_when_not_exist() {
        UUID id = UUID.randomUUID();

        when(clientServiceMock.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = clientController.updateClientById(id, ClientDto.builder().build());

        assertThat(response.getStatusCodeValue(), is(HttpStatus.NOT_FOUND.value()));

        verify(clientServiceMock).findById(id);
    }

    @Test
    public void should_delete_a_client_when_exist() {
        UUID id = UUID.randomUUID();

        ClientEntity clientEntity = ClientEntity.builder().build();

        when(clientServiceMock.findById(id)).thenReturn(Optional.of(clientEntity));
        doNothing().when(clientServiceMock).delete(any(ClientEntity.class));

        ResponseEntity<Object> response = clientController.deleteClientById(id);

        assertThat(response.getStatusCodeValue(), is(HttpStatus.OK.value()));

        verify(clientServiceMock).findById(id);
        verify(clientServiceMock).delete(any(ClientEntity.class));
    }

    @Test
    public void should_not_delete_a_client_when_not_exist() {
        UUID id = UUID.randomUUID();

        when(clientServiceMock.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = clientController.deleteClientById(id);

        assertThat(response.getStatusCodeValue(), is(HttpStatus.NOT_FOUND.value()));

        verify(clientServiceMock).findById(id);
    }
}