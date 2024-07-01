package com.project.manager.service;

import com.project.manager.constant.StatusEnum;
import com.project.manager.entity.ActivityEntity;
import com.project.manager.entity.ProjectEntity;
import com.project.manager.exception.ActivityExistent;
import com.project.manager.exception.ProjectNonExistent;
import com.project.manager.repository.IActivityRepository;
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
class ActivityServiceTest {
    @Mock
    private IActivityRepository activityRepositoryMock;

    @Mock
    private IProjectRepository projectRepositoryMock;

    @InjectMocks
    private ActivityService activityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        activityService = new ActivityService(activityRepositoryMock, projectRepositoryMock);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(activityRepositoryMock);
        verifyNoMoreInteractions(projectRepositoryMock);
    }

    @Test
    public void should_return_a_activity_entity_when_searching_by_id() {
        UUID id = UUID.randomUUID();

        when(activityRepositoryMock.findById(id)).thenReturn(Optional.of(ActivityEntity.builder().build()));

        Optional<ActivityEntity> response = activityService.findById(id);
        assertNotNull(response);

        verify(activityRepositoryMock).findById(id);
    }

    @Test
    public void should_return_a_record_page_when_all_activities_of_a_client_with_a_specific_status_are_consulted() {
        UUID id = UUID.randomUUID();

        List<ActivityEntity> activityEntityList = List.of(new ActivityEntity());
        PageRequest pageRequest = PageRequest.of(1, 10);
        Page<ActivityEntity> pageActivity = new PageImpl<>(activityEntityList, pageRequest,
                activityEntityList.size());

        when(activityRepositoryMock.findAllByProjectIdAndStatus(any(PageRequest.class), eq(id), eq(StatusEnum.OPEN.getCode())))
                .thenReturn(pageActivity);

        Page<ActivityEntity> response = activityService.findByProjectIdAndStatus(pageRequest, id, StatusEnum.OPEN);

        verify(activityRepositoryMock).findAllByProjectIdAndStatus(any(PageRequest.class), eq(id), eq(StatusEnum.OPEN.getCode()));
        var returnedPageContentList = response.getContent();
        assertThat(response, is(notNullValue()));
        assertThat(returnedPageContentList.size(), is(1));
    }

    @Test
    public void should_return_a_record_page_when_all_activities_of_a_client() {
        UUID id = UUID.randomUUID();

        List<ActivityEntity> activityEntityList = List.of(new ActivityEntity());
        PageRequest pageRequest = PageRequest.of(1, 10);
        Page<ActivityEntity> pageActivity = new PageImpl<>(activityEntityList, pageRequest,
                activityEntityList.size());

        when(activityRepositoryMock.findAllByProjectId(any(PageRequest.class), eq(id)))
                .thenReturn(pageActivity);

        Page<ActivityEntity> response = activityService.findByProjectId(pageRequest, id);

        verify(activityRepositoryMock).findAllByProjectId(any(PageRequest.class), eq(id));
        var returnedPageContentList = response.getContent();
        assertThat(response, is(notNullValue()));
        assertThat(returnedPageContentList.size(), is(1));
    }


    @Test
    public void should_add_an_activity_when_the_project_exist_and_activity_descripcion_is_unique() {
        UUID id = UUID.randomUUID();

        when(projectRepositoryMock.findById(id)).thenReturn(Optional.of(ProjectEntity.builder().build()));
        when(activityRepositoryMock.existsByProjectIdAndDescription(eq(id), any(String.class))).thenReturn(false);
        when(activityRepositoryMock.save(any(ActivityEntity.class))).thenReturn(ActivityEntity.builder().build());

        ActivityEntity response = activityService.add(id, ActivityEntity.builder().description("fake").build());

        assertInstanceOf(ActivityEntity.class, response);

        verify(projectRepositoryMock).findById(id);
        verify(activityRepositoryMock).existsByProjectIdAndDescription(eq(id), any(String.class));
        verify(activityRepositoryMock).save(any(ActivityEntity.class));
    }

    @Test
    public void should_not_add_an_activity_when_client_not_exist() {
        UUID id = UUID.randomUUID();

        when(projectRepositoryMock.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProjectNonExistent.class, () -> activityService.add(id, ActivityEntity.builder().description("fake").build()));
        verify(projectRepositoryMock).findById(id);
    }

    @Test
    public void should_not_add_an_activity_when_already_exist() {
        UUID id = UUID.randomUUID();

        when(projectRepositoryMock.findById(id)).thenReturn(Optional.of(ProjectEntity.builder().build()));
        when(activityRepositoryMock.existsByProjectIdAndDescription(eq(id), any(String.class))).thenReturn(true);

        assertThrows(ActivityExistent.class, () -> activityService.add(id, ActivityEntity.builder().description("fake").build()));
        verify(projectRepositoryMock).findById(id);
        verify(activityRepositoryMock).existsByProjectIdAndDescription(eq(id), any(String.class));
    }

    @Test
    public void should_update_an_activity() {
        when(activityRepositoryMock.save(any(ActivityEntity.class))).thenReturn(ActivityEntity.builder().build());

        ActivityEntity response = activityService.update(ActivityEntity.builder().build());

        assertInstanceOf(ActivityEntity.class, response);

        verify(activityRepositoryMock).save(any(ActivityEntity.class));
    }

    @Test
    public void should_update_the_status_of_an_activity() {
        UUID id = UUID.randomUUID();

        doNothing().when(activityRepositoryMock).updateStatus(id, StatusEnum.OPEN.getCode());

        String response = activityService.updateStatus(id, StatusEnum.OPEN);

        assertInstanceOf(String.class, response);
        assertTrue("The activity status was updated".equals(response));

        verify(activityRepositoryMock).updateStatus(id, StatusEnum.OPEN.getCode());
    }

    @Test
    public void should_delete_an_activity() {
        doNothing().when(activityRepositoryMock).delete(any(ActivityEntity.class));

        activityService.delete(ActivityEntity.builder().id(UUID.randomUUID()).build());

        verify(activityRepositoryMock).delete(any(ActivityEntity.class));
    }
}