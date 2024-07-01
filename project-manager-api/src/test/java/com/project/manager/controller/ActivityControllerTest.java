package com.project.manager.controller;

import com.project.manager.constant.StatusEnum;
import com.project.manager.dto.PageRecordDto;
import com.project.manager.dto.ActivityAddRequestDto;
import com.project.manager.dto.ActivityUpdateRequestDto;
import com.project.manager.entity.ActivityEntity;
import com.project.manager.service.ActivityService;
import com.project.manager.service.ActivityService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ActivityControllerTest {

    @Mock
    private ActivityService activityServiceMock;

    @InjectMocks
    private ActivityController activityController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        activityController = new ActivityController(activityServiceMock);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(activityServiceMock);
    }

    @Test
    void should_return_a_activity_entity_when_searching_by_id_and_exist() {
        UUID id = UUID.randomUUID();

        when(activityServiceMock.findById(id)).thenReturn(Optional.of(ActivityEntity.builder().build()));

        ResponseEntity<Object> response = activityController.getActivityById(id);

        assertNotNull(response);
        assertThat(response.getStatusCodeValue(), is(HttpStatus.OK.value()));

        var projectEntityResponse = response.getBody();
        assertInstanceOf(ActivityEntity.class, projectEntityResponse);

        verify(activityServiceMock).findById(id);
    }

    @Test
    public void should_return_a_empty_activity_entity_when_searching_by_id_and_not_exist() {
        UUID id = UUID.randomUUID();

        when(activityServiceMock.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = activityController.getActivityById(id);

        assertNotNull(response);
        assertThat(response.getStatusCodeValue(), is(HttpStatus.NOT_FOUND.value()));

        var projectEntityResponse = response.getBody();
        assertInstanceOf(String.class, projectEntityResponse);

        verify(activityServiceMock).findById(id);
    }

    @Test
    public void should_return_a_record_page_when_all_activities_of_a_project_with_a_specific_status_are_consulted() {
        UUID id = UUID.randomUUID();

        List<ActivityEntity> projetEntityList = List.of(new ActivityEntity());
        Page<ActivityEntity> pageActivities = new PageImpl<>(projetEntityList, PageRequest.of(0, 10),
                projetEntityList.size());

        when(activityServiceMock.findByProjectIdAndStatus(any(PageRequest.class), eq(id), eq(StatusEnum.OPEN)))
                .thenReturn(pageActivities);

        ResponseEntity<PageRecordDto> response = activityController.getAllActivitiesByProjectIdAndStatus(id, StatusEnum.OPEN,
                0, 10);

        PageRecordDto pageRecordDto = response.getBody();

        assert pageRecordDto != null;
        assertThat(pageRecordDto.actualPage(), is(0));
        assertThat(pageRecordDto.totalPages(), is(1));
        assertThat(pageRecordDto.totalRecords(), is(1L));

        var itemList = pageRecordDto.itemList();
        assertThat(itemList.size(), is(1));
        assertThat(response.getStatusCodeValue(), is(HttpStatus.OK.value()));

        verify(activityServiceMock).findByProjectIdAndStatus(any(PageRequest.class), eq(id), eq(StatusEnum.OPEN));
    }

    @Test
    public void should_return_a_record_page_with_all_activities_of_a_project() {
        UUID id = UUID.randomUUID();

        List<ActivityEntity> projetEntityList = List.of(new ActivityEntity());
        Page<ActivityEntity> pageActivities = new PageImpl<>(projetEntityList, PageRequest.of(0, 10),
                projetEntityList.size());

        when(activityServiceMock.findByProjectId(any(PageRequest.class), eq(id)))
                .thenReturn(pageActivities);

        ResponseEntity<PageRecordDto> response = activityController.getAllActivitiesByProjectId(id, 0, 10);

        PageRecordDto pageRecordDto = response.getBody();

        assert pageRecordDto != null;
        assertThat(pageRecordDto.actualPage(), is(0));
        assertThat(pageRecordDto.totalPages(), is(1));
        assertThat(pageRecordDto.totalRecords(), is(1L));

        var itemList = pageRecordDto.itemList();
        assertThat(itemList.size(), is(1));
        assertThat(response.getStatusCodeValue(), is(HttpStatus.OK.value()));

        verify(activityServiceMock).findByProjectId(any(PageRequest.class), eq(id));
    }

    @Test
    void should_add_an_activity() {
        UUID id = UUID.randomUUID();
        when(activityServiceMock.add(eq(id), any(ActivityEntity.class))).thenReturn(ActivityEntity.builder().build());

        ResponseEntity<Object> response = activityController.addActivity(id, ActivityAddRequestDto.builder().build());

        assertThat(response.getStatusCodeValue(), is(HttpStatus.CREATED.value()));
        assertInstanceOf(ActivityEntity.class, response.getBody());

        verify(activityServiceMock).add(eq(id), any(ActivityEntity.class));
    }

    @Test
    public void should_update_an_activity_when_exist() {
        UUID id = UUID.randomUUID();

        ActivityEntity projectEntity = ActivityEntity.builder().build();

        when(activityServiceMock.findById(id)).thenReturn(Optional.of(projectEntity));
        when(activityServiceMock.update(any(ActivityEntity.class))).thenReturn(projectEntity);

        ResponseEntity<Object> response = activityController.updateActivityById(id, ActivityUpdateRequestDto.builder().build());

        assertThat(response.getStatusCodeValue(), is(HttpStatus.OK.value()));

        verify(activityServiceMock).findById(id);
        verify(activityServiceMock).update(any(ActivityEntity.class));
    }

    @Test
    public void should_not_update_an_activity_when_not_exist() {
        UUID id = UUID.randomUUID();

        when(activityServiceMock.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = activityController.updateActivityById(id, ActivityUpdateRequestDto.builder().build());

        assertThat(response.getStatusCodeValue(), is(HttpStatus.NOT_FOUND.value()));

        verify(activityServiceMock).findById(id);
    }

    @Test
    public void should_update_the_status_of_an_activity_when_exist() {
        UUID id = UUID.randomUUID();

        ActivityEntity projectEntity = ActivityEntity.builder().build();

        when(activityServiceMock.findById(id)).thenReturn(Optional.of(projectEntity));
        when(activityServiceMock.updateStatus(id, StatusEnum.FINISHED)).thenReturn("updated");

        ResponseEntity<Object> response = activityController.updateStatusActivityById(id, StatusEnum.FINISHED);

        assertThat(response.getStatusCodeValue(), is(HttpStatus.OK.value()));

        verify(activityServiceMock).findById(id);
        verify(activityServiceMock).updateStatus(id, StatusEnum.FINISHED);
    }

    @Test
    public void should_not_update_the_status_of_an_activity_when_not_exist() {
        UUID id = UUID.randomUUID();

        when(activityServiceMock.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = activityController.updateStatusActivityById(id, StatusEnum.FINISHED);

        assertThat(response.getStatusCodeValue(), is(HttpStatus.NOT_FOUND.value()));

        verify(activityServiceMock).findById(id);
    }

    @Test
    public void should_delete_an_activity_when_exist() {
        UUID id = UUID.randomUUID();

        ActivityEntity projectEntity = ActivityEntity.builder().build();

        when(activityServiceMock.findById(id)).thenReturn(Optional.of(projectEntity));
        doNothing().when(activityServiceMock).delete(any(ActivityEntity.class));

        ResponseEntity<Object> response = activityController.deleteClientById(id);

        assertThat(response.getStatusCodeValue(), is(HttpStatus.OK.value()));

        verify(activityServiceMock).findById(id);
        verify(activityServiceMock).delete(any(ActivityEntity.class));
    }

    @Test
    public void should_not_delete_an_activity_when_not_exist() {
        UUID id = UUID.randomUUID();

        when(activityServiceMock.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = activityController.deleteClientById(id);

        assertThat(response.getStatusCodeValue(), is(HttpStatus.NOT_FOUND.value()));

        verify(activityServiceMock).findById(id);
    }
}