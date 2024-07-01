package com.project.manager.controller;

import com.project.manager.config.GeneralConfig;
import com.project.manager.constant.StatusEnum;
import com.project.manager.dto.ActivityAddRequestDto;
import com.project.manager.dto.ActivityUpdateRequestDto;
import com.project.manager.dto.PageRecordDto;
import com.project.manager.entity.ActivityEntity;
import com.project.manager.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/activity")
public class ActivityController {

    private final static String LEGEND_ACTIVITY_NOT_FOUND = "Activity not found";
    private final static String LEGEND_ACTIVITY_MODIFIED = "Activity has been modified";
    private final static String LEGEND_ACTIVITY_DELETED = "Activity has been deleted";

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Operation(summary = "Returns an activity given its id", description = "Returns an activity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getActivityById(@PathVariable(value = "id") UUID id) {
        Optional<ActivityEntity> activityEntityOptional = activityService.findById(id);
        return activityEntityOptional.
                <ResponseEntity<Object>>map(activityEntity -> ResponseEntity.status(HttpStatus.OK).body(activityEntity))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(LEGEND_ACTIVITY_NOT_FOUND));
    }

    @Operation(summary = "Returns all activities of a project with a specific status per page", description = "Returns all activity of a project by status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    @GetMapping("/all/{projectid}/{status}")
    public ResponseEntity<PageRecordDto> getAllActivitiesByProjectIdAndStatus(@PathVariable(value = "projectid") UUID projectId,
                                                                              @PathVariable(value = "status") StatusEnum status,
                                                                              @RequestParam(defaultValue = GeneralConfig.PAGINATION_DEFAULT_PAGE_VALUE) int page,
                                                                              @RequestParam(defaultValue = GeneralConfig.PAGINATION_DEFAULT_SIZE_VALUE) int size) {

        Page<ActivityEntity> pagedActivities = activityService.findByProjectIdAndStatus(PageRequest.of(page, size), projectId, status);
        PageRecordDto clientPageRecordDto = new PageRecordDto(pagedActivities.getNumber(), pagedActivities.getTotalElements(),
                pagedActivities.getTotalPages(), pagedActivities.getContent());

        return ResponseEntity.status(HttpStatus.OK).body(clientPageRecordDto);
    }

    @Operation(summary = "Returns all activities of a project per page", description = "Returns all activity of a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    @GetMapping("/all/{projectid}")
    public ResponseEntity<PageRecordDto> getAllActivitiesByProjectId(@PathVariable(value = "projectid") UUID projectId,
                                                                     @RequestParam(defaultValue = GeneralConfig.PAGINATION_DEFAULT_PAGE_VALUE) int page,
                                                                     @RequestParam(defaultValue = GeneralConfig.PAGINATION_DEFAULT_SIZE_VALUE) int size) {

        Page<ActivityEntity> pagedActivities = activityService.findByProjectId(PageRequest.of(page, size), projectId);
        PageRecordDto clientPageRecordDto = new PageRecordDto(pagedActivities.getNumber(), pagedActivities.getTotalElements(),
                pagedActivities.getTotalPages(), pagedActivities.getContent());

        return ResponseEntity.status(HttpStatus.OK).body(clientPageRecordDto);
    }



    @Operation(summary = "Add activity", description = "Allows adding a activity record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Activity created")
    })
    @PostMapping(value = "/add/project/{projectid}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addActivity(@PathVariable(value = "projectid") UUID projectId,
                                              @RequestBody @Valid ActivityAddRequestDto activityDto) {
        ActivityEntity activity = ActivityEntity.builder()
                .description(activityDto.getDescription())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(activityService.add(projectId, activity));
    }

    @Operation(summary = "Modify a activity record given its id", description = "Modify a activity record given its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = LEGEND_ACTIVITY_MODIFIED),
            @ApiResponse(responseCode = "404", description = LEGEND_ACTIVITY_NOT_FOUND),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateActivityById(@PathVariable(value = "id") UUID id,
                                                     @RequestBody @Valid ActivityUpdateRequestDto activityUpdateRequestDto) {
        Optional<ActivityEntity> activityOptional = activityService.findById(id);

        if (activityOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(LEGEND_ACTIVITY_NOT_FOUND);
        }

        ActivityEntity activity = activityOptional.get();
        activity.setDescription(activityUpdateRequestDto.getDescription());
        activity.setStatus(activityUpdateRequestDto.getStatus());

        return ResponseEntity.status(HttpStatus.OK).body(activityService.update(activity));
    }

    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<Object> updateStatusActivityById(@PathVariable(value = "id") UUID id,
                                                           @PathVariable(value = "status") StatusEnum status) {
        Optional<ActivityEntity> activityOptional = activityService.findById(id);

        if (activityOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(LEGEND_ACTIVITY_NOT_FOUND);
        }

        return ResponseEntity.status(HttpStatus.OK).body(activityService.updateStatus(id, status));
    }


    @Operation(summary = "Delete a activity given their id", description = "Delete a activity record by giving its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = LEGEND_ACTIVITY_DELETED),
            @ApiResponse(responseCode = "404", description = LEGEND_ACTIVITY_NOT_FOUND),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteClientById(@PathVariable(value = "id") UUID id) {
        Optional<ActivityEntity> activityOptional = activityService.findById(id);

        if (activityOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(LEGEND_ACTIVITY_NOT_FOUND);
        }

        activityService.delete(activityOptional.get());

        return ResponseEntity.status(HttpStatus.OK).body(LEGEND_ACTIVITY_DELETED);
    }
}
