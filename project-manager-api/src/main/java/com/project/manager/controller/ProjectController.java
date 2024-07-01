package com.project.manager.controller;

import com.project.manager.config.GeneralConfig;
import com.project.manager.constant.StatusEnum;
import com.project.manager.dto.PageRecordDto;
import com.project.manager.dto.ProjectAddRequestDto;
import com.project.manager.dto.ProjectUpdateRequestDto;
import com.project.manager.entity.ProjectEntity;
import com.project.manager.service.ProjectService;
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
@RequestMapping("/project")
public class ProjectController {

    private final static String LEGEND_PROJECT_NOT_FOUND = "Project not found";
    private final static String LEGEND_PROJECT_MODIFIED = "Project has been modified";
    private final static String LEGEND_PROJECT_DELETED = "Project has been deleted";

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Returns a project given its id", description = "Returns a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getProjectById(@PathVariable(value = "id") UUID id) {
        Optional<ProjectEntity> projectEntityOptional = projectService.findById(id);
        return projectEntityOptional.
                <ResponseEntity<Object>>map(projectEntity -> ResponseEntity.status(HttpStatus.OK).body(projectEntity))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(LEGEND_PROJECT_NOT_FOUND));
    }

    @Operation(summary = "Returns all projects of a client with a specific status per page", description = "Returns all project of a client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    @GetMapping("/all/{clientid}/{status}")
    public ResponseEntity<PageRecordDto> getAllProjectsByClientIdAndStatus(@PathVariable(value = "clientid") UUID clientId,
                                                                           @PathVariable(value = "status") StatusEnum status,
                                                                           @RequestParam(defaultValue = GeneralConfig.PAGINATION_DEFAULT_PAGE_VALUE) int page,
                                                                           @RequestParam(defaultValue = GeneralConfig.PAGINATION_DEFAULT_SIZE_VALUE) int size) {

        Page<ProjectEntity> pagedProjects = projectService.findByClientIdAndStatus(PageRequest.of(page, size), clientId, status);
        PageRecordDto clientPageRecordDto = new PageRecordDto(pagedProjects.getNumber(), pagedProjects.getTotalElements(),
                pagedProjects.getTotalPages(), pagedProjects.getContent());

        return ResponseEntity.status(HttpStatus.OK).body(clientPageRecordDto);
    }

    @Operation(summary = "Returns all projects", description = "Returns all project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    @GetMapping("/all")
    public ResponseEntity<PageRecordDto> getAllProjects(@RequestParam(defaultValue = GeneralConfig.PAGINATION_DEFAULT_PAGE_VALUE) int page,
                                                        @RequestParam(defaultValue = GeneralConfig.PAGINATION_DEFAULT_SIZE_VALUE) int size) {

        Page<ProjectEntity> pagedProjects = projectService.findAll(PageRequest.of(page, size));
        PageRecordDto clientPageRecordDto = new PageRecordDto(pagedProjects.getNumber(), pagedProjects.getTotalElements(),
                pagedProjects.getTotalPages(), pagedProjects.getContent());

        return ResponseEntity.status(HttpStatus.OK).body(clientPageRecordDto);
    }

    @GetMapping("/all/status/{status}")
    public ResponseEntity<PageRecordDto> getAllProjectsByStatus(@PathVariable(value = "status") StatusEnum status,
                                                                @RequestParam(defaultValue = GeneralConfig.PAGINATION_DEFAULT_PAGE_VALUE) int page,
                                                                @RequestParam(defaultValue = GeneralConfig.PAGINATION_DEFAULT_SIZE_VALUE) int size) {

        Page<ProjectEntity> pagedProjects = projectService.findAllByStatus(PageRequest.of(page, size), status);
        PageRecordDto clientPageRecordDto = new PageRecordDto(pagedProjects.getNumber(), pagedProjects.getTotalElements(),
                pagedProjects.getTotalPages(), pagedProjects.getContent());

        return ResponseEntity.status(HttpStatus.OK).body(clientPageRecordDto);
    }

    @Operation(summary = "Add project", description = "Allows adding a project record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created")
    })
    @PostMapping(value = "/add/client/{clientid}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addProject(@PathVariable(value = "clientid") UUID clientId,
                                             @RequestBody @Valid ProjectAddRequestDto projectDto) {
        ProjectEntity project = ProjectEntity.builder()
                .description(projectDto.getDescription())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.add(clientId, project));
    }

    @Operation(summary = "Modify a project record given its id", description = "Modify a project record given its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = LEGEND_PROJECT_MODIFIED),
            @ApiResponse(responseCode = "404", description = LEGEND_PROJECT_NOT_FOUND),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProjectById(@PathVariable(value = "id") UUID id,
                                                    @RequestBody @Valid ProjectUpdateRequestDto projectUpdateRequestDto) {
        Optional<ProjectEntity> projectOptional = projectService.findById(id);

        if (projectOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(LEGEND_PROJECT_NOT_FOUND);
        }

        ProjectEntity project = projectOptional.get();
        project.setDescription(projectUpdateRequestDto.getDescription());
        project.setStatus(projectUpdateRequestDto.getStatus());

        return ResponseEntity.status(HttpStatus.OK).body(projectService.update(project));
    }

    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<Object> updateStatusProjectById(@PathVariable(value = "id") UUID id,
                                                          @PathVariable(value = "status") StatusEnum status) {
        Optional<ProjectEntity> projectOptional = projectService.findById(id);

        if (projectOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(LEGEND_PROJECT_NOT_FOUND);
        }

        return ResponseEntity.status(HttpStatus.OK).body(projectService.updateStatus(id, status));
    }


    @Operation(summary = "Delete a project given their id", description = "Delete a project record by giving its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = LEGEND_PROJECT_DELETED),
            @ApiResponse(responseCode = "404", description = LEGEND_PROJECT_NOT_FOUND),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteClientById(@PathVariable(value = "id") UUID id) {
        Optional<ProjectEntity> projectOptional = projectService.findById(id);

        if (projectOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(LEGEND_PROJECT_NOT_FOUND);
        }

        projectService.delete(projectOptional.get());

        return ResponseEntity.status(HttpStatus.OK).body(LEGEND_PROJECT_DELETED);
    }
}
