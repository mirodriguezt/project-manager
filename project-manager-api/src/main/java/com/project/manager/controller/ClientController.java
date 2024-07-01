package com.project.manager.controller;

import com.project.manager.config.GeneralConfig;
import com.project.manager.dto.ClientDto;
import com.project.manager.dto.PageRecordDto;
import com.project.manager.entity.ClientEntity;
import com.project.manager.service.ClientService;
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
@RequestMapping("/client")
public class ClientController {

    private final static String LEGEND_CLIENT_NOT_FOUND = "Client not found";
    private final static String LEGEND_CLIENT_MODIFIED = "Client has been modified";
    private final static String LEGEND_CLIENT_DELETED = "Client has been deleted";
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(summary = "Returns a client given its id", description = "Returns a client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getClientById(@PathVariable(value = "id") UUID id) {
        Optional<ClientEntity> clientEntityOptional = clientService.findById(id);
        return clientEntityOptional.
                <ResponseEntity<Object>>map(userEntity -> ResponseEntity.status(HttpStatus.OK).body(userEntity))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(LEGEND_CLIENT_NOT_FOUND));
    }

    @Operation(summary = "Returns a list with all client per page", description = "Returns all client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    @GetMapping("/all")
    public ResponseEntity<PageRecordDto> getAllClients(@RequestParam(defaultValue = GeneralConfig.PAGINATION_DEFAULT_PAGE_VALUE) int page,
                                                       @RequestParam(defaultValue = GeneralConfig.PAGINATION_DEFAULT_SIZE_VALUE) int size) {

        Page<ClientEntity> pagedClients = clientService.findAll(PageRequest.of(page, size));
        PageRecordDto pageRecordDto = new PageRecordDto(pagedClients.getNumber(), pagedClients.getTotalElements(),
                pagedClients.getTotalPages(), pagedClients.getContent());

        return ResponseEntity.status(HttpStatus.OK).body(pageRecordDto);
    }

    @Operation(summary = "Add client", description = "Allows adding a client record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created")
    })
    @PostMapping(value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addClient(@RequestBody @Valid ClientDto clientDto) {
        ClientEntity client = ClientEntity.builder()
                .name(clientDto.getName())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.save(client));
    }

    @Operation(summary = "Modify a client record given its id", description = "Modify a client record given its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = LEGEND_CLIENT_MODIFIED),
            @ApiResponse(responseCode = "404", description = LEGEND_CLIENT_NOT_FOUND),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateClientById(@PathVariable(value = "id") UUID id,
                                                   @RequestBody @Valid ClientDto clientDto) {
        Optional<ClientEntity> clientOptional = clientService.findById(id);

        if (clientOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(LEGEND_CLIENT_NOT_FOUND);
        }

        ClientEntity client = clientOptional.get();
        client.setName(clientDto.getName());
        return ResponseEntity.status(HttpStatus.OK).body(clientService.save(client));
    }

    @Operation(summary = "Delete a client given their id", description = "Delete a client record by giving its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = LEGEND_CLIENT_DELETED),
            @ApiResponse(responseCode = "404", description = LEGEND_CLIENT_NOT_FOUND),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteClientById(@PathVariable(value = "id") UUID id) {
        Optional<ClientEntity> clientOptional = clientService.findById(id);

        if (clientOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(LEGEND_CLIENT_NOT_FOUND);
        }

        clientService.delete(clientOptional.get());

        return ResponseEntity.status(HttpStatus.OK).body(LEGEND_CLIENT_DELETED);
    }
}
