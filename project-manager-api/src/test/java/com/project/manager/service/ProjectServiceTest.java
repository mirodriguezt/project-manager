package com.project.manager.service;

import com.project.manager.constant.StatusEnum;
import com.project.manager.entity.ClientEntity;
import com.project.manager.entity.ProjectEntity;
import com.project.manager.exception.ClientNonExistent;
import com.project.manager.exception.ProjectExistent;
import com.project.manager.repository.IClientRepository;
import com.project.manager.repository.IProjectRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProjectServiceTest {

    @Mock
    private IProjectRepository projectRepositoryMock;

    @Mock
    private IClientRepository clientRepositoryMock;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        projectService = new ProjectService(projectRepositoryMock, clientRepositoryMock);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(projectRepositoryMock);
        verifyNoMoreInteractions(clientRepositoryMock);
    }

    @Test
    public void should_return_a_project_entity_when_searching_by_id() {
        UUID id = UUID.randomUUID();

        when(projectRepositoryMock.findById(id)).thenReturn(Optional.of(ProjectEntity.builder().build()));

        Optional<ProjectEntity> response = projectService.findById(id);
        assertNotNull(response);

        verify(projectRepositoryMock).findById(id);
    }

    @Test
    public void should_return_a_record_page_when_all_project_of_a_client_with_a_specific_status_are_consulted() {
        UUID id = UUID.randomUUID();

        List<ProjectEntity> projetEntityList = List.of(new ProjectEntity());
        PageRequest pageRequest = PageRequest.of(1, 10);
        Page<ProjectEntity> pageProjects = new PageImpl<>(projetEntityList, pageRequest,
                projetEntityList.size());

        when(projectRepositoryMock.findAllByClientIdAndStatus(any(PageRequest.class), eq(id), eq(StatusEnum.OPEN.getCode())))
                .thenReturn(pageProjects);

        Page<ProjectEntity> response = projectService.findByClientIdAndStatus(pageRequest, id, StatusEnum.OPEN);

        verify(projectRepositoryMock).findAllByClientIdAndStatus(any(PageRequest.class), eq(id), eq(StatusEnum.OPEN.getCode()));
        var returnedPageContentList = response.getContent();
        assertThat(response, is(notNullValue()));
        assertThat(returnedPageContentList.size(), is(1));
    }

    @Test
    public void should_return_a_record_page_with_all_project() {
        List<ProjectEntity> projectEntityList = List.of(new ProjectEntity());
        PageRequest pageRequest = PageRequest.of(1, 10);
        Page<ProjectEntity> pageProjects = new PageImpl<>(projectEntityList, pageRequest, projectEntityList.size());
        when(projectRepositoryMock.findAll(any(PageRequest.class))).thenReturn(pageProjects);

        Page<ProjectEntity> response = projectService.findAll(pageRequest);

        verify(projectRepositoryMock).findAll(pageRequest);
        var returnedPageContentList = response.getContent();
        assertThat(response, is(notNullValue()));
        assertThat(returnedPageContentList.size(), is(1));
    }

    @Test
    public void should_return_a_record_page_when_all_project_with_a_specific_status_are_consulted() {
        List<ProjectEntity> projetEntityList = List.of(new ProjectEntity());
        PageRequest pageRequest = PageRequest.of(1, 10);
        Page<ProjectEntity> pageProjects = new PageImpl<>(projetEntityList, pageRequest,
                projetEntityList.size());

        when(projectRepositoryMock.findAllByStatus(any(PageRequest.class), eq(StatusEnum.OPEN.getCode())))
                .thenReturn(pageProjects);

        Page<ProjectEntity> response = projectService.findAllByStatus(pageRequest, StatusEnum.OPEN);

        verify(projectRepositoryMock).findAllByStatus(any(PageRequest.class), eq(StatusEnum.OPEN.getCode()));
        var returnedPageContentList = response.getContent();
        assertThat(response, is(notNullValue()));
        assertThat(returnedPageContentList.size(), is(1));
    }

    @Test
    public void should_add_a_project_when_client_exist_and_proyect_descripcion_is_unique() {
        UUID id = UUID.randomUUID();

        when(clientRepositoryMock.findById(id)).thenReturn(Optional.of(ClientEntity.builder().build()));
        when(projectRepositoryMock.existsByClientIdAndDescription(eq(id), any(String.class))).thenReturn(false);
        when(projectRepositoryMock.save(any(ProjectEntity.class))).thenReturn(ProjectEntity.builder().build());

        ProjectEntity response = projectService.add(id, ProjectEntity.builder().description("fake").build());

        assertInstanceOf(ProjectEntity.class, response);

        verify(clientRepositoryMock).findById(id);
        verify(projectRepositoryMock).existsByClientIdAndDescription(eq(id), any(String.class));
        verify(projectRepositoryMock).save(any(ProjectEntity.class));
    }

    @Test
    public void should_not_add_a_project_when_client_not_exist() {
        UUID id = UUID.randomUUID();

        when(clientRepositoryMock.findById(id)).thenReturn(Optional.empty());

        assertThrows(ClientNonExistent.class, () -> projectService.add(id, ProjectEntity.builder().description("fake").build()));
        verify(clientRepositoryMock).findById(id);
    }

    @Test
    public void should_not_add_a_project_when_already_exist() {
        UUID id = UUID.randomUUID();

        when(clientRepositoryMock.findById(id)).thenReturn(Optional.of(ClientEntity.builder().build()));
        when(projectRepositoryMock.existsByClientIdAndDescription(eq(id), any(String.class))).thenReturn(true);

        assertThrows(ProjectExistent.class, () -> projectService.add(id, ProjectEntity.builder().description("fake").build()));
        verify(clientRepositoryMock).findById(id);
        verify(projectRepositoryMock).existsByClientIdAndDescription(eq(id), any(String.class));
    }

    @Test
    public void should_update_a_project() {
        when(projectRepositoryMock.save(any(ProjectEntity.class))).thenReturn(ProjectEntity.builder().build());

        ProjectEntity response = projectService.update(ProjectEntity.builder().build());

        assertInstanceOf(ProjectEntity.class, response);

        verify(projectRepositoryMock).save(any(ProjectEntity.class));
    }

    @Test
    public void should_update_the_status_of_a_project() {
        UUID id = UUID.randomUUID();

        doNothing().when(projectRepositoryMock).updateStatus(id, StatusEnum.OPEN.getCode());

        String response = projectService.updateStatus(id, StatusEnum.OPEN);

        assertInstanceOf(String.class, response);
        assertTrue("The project status was updated".equals(response));

        verify(projectRepositoryMock).updateStatus(id, StatusEnum.OPEN.getCode());
    }

    @Test
    public void should_delete_a_project() {
        doNothing().when(projectRepositoryMock).delete(any(ProjectEntity.class));

        projectService.delete(ProjectEntity.builder().id(UUID.randomUUID()).build());

        verify(projectRepositoryMock).delete(any(ProjectEntity.class));
    }
}