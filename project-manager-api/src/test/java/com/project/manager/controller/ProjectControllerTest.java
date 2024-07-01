package com.project.manager.controller;

import com.project.manager.constant.StatusEnum;
import com.project.manager.dto.PageRecordDto;
import com.project.manager.dto.ProjectAddRequestDto;
import com.project.manager.dto.ProjectUpdateRequestDto;
import com.project.manager.entity.ProjectEntity;
import com.project.manager.service.ProjectService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProjectControllerTest {

    @Mock
    private ProjectService projectServiceMock;

    @InjectMocks
    private ProjectController projectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        projectController = new ProjectController(projectServiceMock);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(projectServiceMock);
    }

    @Test
    void should_return_a_project_entity_when_searching_by_id_and_exist() {
        UUID id = UUID.randomUUID();

        when(projectServiceMock.findById(id)).thenReturn(Optional.of(ProjectEntity.builder().build()));

        ResponseEntity<Object> response = projectController.getProjectById(id);

        assertNotNull(response);
        assertThat(response.getStatusCodeValue(), is(HttpStatus.OK.value()));

        var projectEntityResponse = response.getBody();
        assertInstanceOf(ProjectEntity.class, projectEntityResponse);

        verify(projectServiceMock).findById(id);
    }

    @Test
    public void should_return_a_empty_project_entity_when_searching_by_id_and_not_exist() {
        UUID id = UUID.randomUUID();

        when(projectServiceMock.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = projectController.getProjectById(id);

        assertNotNull(response);
        assertThat(response.getStatusCodeValue(), is(HttpStatus.NOT_FOUND.value()));

        var projectEntityResponse = response.getBody();
        assertInstanceOf(String.class, projectEntityResponse);

        verify(projectServiceMock).findById(id);
    }

    @Test
    public void should_return_a_record_page_when_all_project_of_a_client_with_a_specific_status_are_consulted() {
        UUID id = UUID.randomUUID();

        List<ProjectEntity> projetEntityList = List.of(new ProjectEntity());
        Page<ProjectEntity> pageProjects = new PageImpl<>(projetEntityList, PageRequest.of(0, 10),
                projetEntityList.size());

        when(projectServiceMock.findByClientIdAndStatus(any(PageRequest.class), eq(id), eq(StatusEnum.OPEN)))
                .thenReturn(pageProjects);

        ResponseEntity<PageRecordDto> response = projectController.getAllProjectsByClientIdAndStatus(id, StatusEnum.OPEN,
                0, 10);

        PageRecordDto pageRecordDto = response.getBody();

        assert pageRecordDto != null;
        assertThat(pageRecordDto.actualPage(), is(0));
        assertThat(pageRecordDto.totalPages(), is(1));
        assertThat(pageRecordDto.totalRecords(), is(1L));

        var itemList = pageRecordDto.itemList();
        assertThat(itemList.size(), is(1));
        assertThat(response.getStatusCodeValue(), is(HttpStatus.OK.value()));

        verify(projectServiceMock).findByClientIdAndStatus(any(PageRequest.class), eq(id), eq(StatusEnum.OPEN));
    }

    @Test
    public void should_return_a_record_page_with_all_project() {
        List<ProjectEntity> projetEntityList = List.of(new ProjectEntity());
        Page<ProjectEntity> pageProjects = new PageImpl<>(projetEntityList, PageRequest.of(0, 10),
                projetEntityList.size());

        when(projectServiceMock.findAll(any(PageRequest.class)))
                .thenReturn(pageProjects);

        ResponseEntity<PageRecordDto> response = projectController.getAllProjects(0, 10);

        PageRecordDto pageRecordDto = response.getBody();

        assert pageRecordDto != null;
        assertThat(pageRecordDto.actualPage(), is(0));
        assertThat(pageRecordDto.totalPages(), is(1));
        assertThat(pageRecordDto.totalRecords(), is(1L));

        var itemList = pageRecordDto.itemList();
        assertThat(itemList.size(), is(1));
        assertThat(response.getStatusCodeValue(), is(HttpStatus.OK.value()));

        verify(projectServiceMock).findAll(any(PageRequest.class));
    }

    @Test
    public void should_return_a_record_page_with_all_project_when_a_specific_status_are_consulted() {
        List<ProjectEntity> projetEntityList = List.of(new ProjectEntity());
        Page<ProjectEntity> pageProjects = new PageImpl<>(projetEntityList, PageRequest.of(0, 10),
                projetEntityList.size());

        when(projectServiceMock.findAllByStatus(any(PageRequest.class), eq(StatusEnum.OPEN)))
                .thenReturn(pageProjects);

        ResponseEntity<PageRecordDto> response = projectController.getAllProjectsByStatus(StatusEnum.OPEN, 0, 10);

        PageRecordDto pageRecordDto = response.getBody();

        assert pageRecordDto != null;
        assertThat(pageRecordDto.actualPage(), is(0));
        assertThat(pageRecordDto.totalPages(), is(1));
        assertThat(pageRecordDto.totalRecords(), is(1L));

        var itemList = pageRecordDto.itemList();
        assertThat(itemList.size(), is(1));
        assertThat(response.getStatusCodeValue(), is(HttpStatus.OK.value()));

        verify(projectServiceMock).findAllByStatus(any(PageRequest.class), eq(StatusEnum.OPEN));
    }

    @Test
    void should_add_a_project() {
        UUID id = UUID.randomUUID();
        when(projectServiceMock.add(eq(id), any(ProjectEntity.class))).thenReturn(ProjectEntity.builder().build());

        ResponseEntity<Object> response = projectController.addProject(id, ProjectAddRequestDto.builder().build());

        assertThat(response.getStatusCodeValue(), is(HttpStatus.CREATED.value()));
        assertInstanceOf(ProjectEntity.class, response.getBody());

        verify(projectServiceMock).add(eq(id), any(ProjectEntity.class));
    }

    @Test
    public void should_update_a_project_when_exist() {
        UUID id = UUID.randomUUID();

        ProjectEntity projectEntity = ProjectEntity.builder().build();

        when(projectServiceMock.findById(id)).thenReturn(Optional.of(projectEntity));
        when(projectServiceMock.update(any(ProjectEntity.class))).thenReturn(projectEntity);

        ResponseEntity<Object> response = projectController.updateProjectById(id, ProjectUpdateRequestDto.builder().build());

        assertThat(response.getStatusCodeValue(), is(HttpStatus.OK.value()));

        verify(projectServiceMock).findById(id);
        verify(projectServiceMock).update(any(ProjectEntity.class));
    }

    @Test
    public void should_not_update_a_project_when_not_exist() {
        UUID id = UUID.randomUUID();

        when(projectServiceMock.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = projectController.updateProjectById(id, ProjectUpdateRequestDto.builder().build());

        assertThat(response.getStatusCodeValue(), is(HttpStatus.NOT_FOUND.value()));

        verify(projectServiceMock).findById(id);
    }

    @Test
    public void should_update_the_status_of_a_project_when_exist() {
        UUID id = UUID.randomUUID();

        ProjectEntity projectEntity = ProjectEntity.builder().build();

        when(projectServiceMock.findById(id)).thenReturn(Optional.of(projectEntity));
        when(projectServiceMock.updateStatus(id, StatusEnum.FINISHED)).thenReturn("updated");

        ResponseEntity<Object> response = projectController.updateStatusProjectById(id, StatusEnum.FINISHED);

        assertThat(response.getStatusCodeValue(), is(HttpStatus.OK.value()));

        verify(projectServiceMock).findById(id);
        verify(projectServiceMock).updateStatus(id, StatusEnum.FINISHED);
    }

    @Test
    public void should_not_update_the_status_of_a_project_when_not_exist() {
        UUID id = UUID.randomUUID();

        when(projectServiceMock.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = projectController.updateStatusProjectById(id, StatusEnum.FINISHED);

        assertThat(response.getStatusCodeValue(), is(HttpStatus.NOT_FOUND.value()));

        verify(projectServiceMock).findById(id);
    }

    @Test
    public void should_delete_a_project_when_exist() {
        UUID id = UUID.randomUUID();

        ProjectEntity projectEntity = ProjectEntity.builder().build();

        when(projectServiceMock.findById(id)).thenReturn(Optional.of(projectEntity));
        doNothing().when(projectServiceMock).delete(any(ProjectEntity.class));

        ResponseEntity<Object> response = projectController.deleteClientById(id);

        assertThat(response.getStatusCodeValue(), is(HttpStatus.OK.value()));

        verify(projectServiceMock).findById(id);
        verify(projectServiceMock).delete(any(ProjectEntity.class));
    }

    @Test
    public void should_not_delete_a_project_when_not_exist() {
        UUID id = UUID.randomUUID();

        when(projectServiceMock.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = projectController.deleteClientById(id);

        assertThat(response.getStatusCodeValue(), is(HttpStatus.NOT_FOUND.value()));

        verify(projectServiceMock).findById(id);
    }
}