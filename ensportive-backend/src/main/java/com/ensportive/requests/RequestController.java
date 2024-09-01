package com.ensportive.requests;

import com.ensportive.configs.security.AuthenticationUtils;
import com.ensportive.requests.dtos.AbsenceRequestDTO;
import com.ensportive.requests.dtos.RegisterRequestDTO;
import com.ensportive.requests.dtos.RequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RequestController {

        @Autowired
        private RequestService requestService;

        @Autowired
        private AuthenticationUtils authenticationUtils;

        @Operation(summary = "Get all requests", description = "Get a list of all requests")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "List of Requests", content = {
                        @Content(mediaType = "application/json", schema = @Schema(implementation = RequestDTO.class))})})
        @GetMapping
        public List<RequestDTO> getAllRequests() {
                if (authenticationUtils.hasRole("ADMIN"))
                        return requestService.getAllRequests();
                return requestService.getRequestsByUserId(authenticationUtils.getAuthenticatedUserId());

        }

        @Operation(summary = "Get request by id", description = "Get a request by its ID")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Request found and returned successfully", content = @Content(mediaType = "application/json")),
                @ApiResponse(responseCode = "404", description = "Request not found by id")})
        @GetMapping("/{id}")
        public ResponseEntity<RequestDTO> getRequestById(@PathVariable Long id) {
                if (authenticationUtils.hasRole("ADMIN"))
                        return ResponseEntity.ok(requestService.getRequestById(id));
                return ResponseEntity.ok(requestService.getRequestById(id, authenticationUtils.getAuthenticatedUserId()));
        }

        @Operation(summary = "Create absence request", description = "Create a new absence request")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "201", description = "Request created successfully", content = @Content(mediaType = "application/json"))})
        @PostMapping("/absence")
        public ResponseEntity<Long> createAbsenceRequest(@RequestBody AbsenceRequestDTO requestRequestDTO) {
                if (!authenticationUtils.hasRole("STUDENT"))
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

                Long userId = authenticationUtils.getAuthenticatedUserId();
                try {
                return new ResponseEntity<>(requestService.createAbsenceRequest(requestRequestDTO, userId),
                        HttpStatus.CREATED);
                } catch (Exception e) {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
        }

        @Operation(summary = "Create registration request", description = "Create a new registration request")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "201", description = "Request created successfully", content = @Content(mediaType = "application/json"))})
        @PostMapping("/register")
        public ResponseEntity<Long> createRegistrationRequest(@RequestBody RegisterRequestDTO requestRequestDTO) {
                try {
                        return new ResponseEntity<>(requestService.createRegistrationRequest(requestRequestDTO),
                                HttpStatus.CREATED);
                } catch (Exception e) {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
        }

        @Operation(summary = "Delete request by ID", description = "Delete a request by its ID")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "204", description = "Request deleted successfully"),
                @ApiResponse(responseCode = "404", description = "Request not found by ID")})

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
                if (!authenticationUtils.hasRole("ADMIN"))
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

                requestService.delete(id);
                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Approve request by ID", description = "Approve a request by its ID")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "204", description = "Request approved successfully"),
                @ApiResponse(responseCode = "404", description = "Request not found by ID")})
        @PutMapping("/approve/{id}")
        public ResponseEntity<HttpStatus> approveRequest(@PathVariable Long id) {
                if (!authenticationUtils.hasRole("ADMIN"))
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

                requestService.approveRequest(id);
                return new ResponseEntity<>(HttpStatus.OK);
        }

        @Operation(summary = "Deny request by ID", description = "Deny a request by its ID")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "204", description = "Request denied successfully"),
                @ApiResponse(responseCode = "404", description = "Request not found by ID")})
        @PutMapping("/deny/{id}")
        public ResponseEntity<HttpStatus> denyRequest(@PathVariable Long id) {
                if (!authenticationUtils.hasRole("ADMIN"))
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

                requestService.denyRequest(id);
                return new ResponseEntity<>(HttpStatus.OK);
        }

        @Operation(summary = "Get Requests by User id")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "List of Request", content = {
                        @Content(mediaType = "application/json", schema = @Schema(implementation = RequestDTO.class))})})
        @GetMapping("/student/{userId}")
        public ResponseEntity<List<RequestDTO>> getRequestsByUserId(@PathVariable Long userId) {
                if (!authenticationUtils.hasRole("ADMIN") && !userId.equals(authenticationUtils.getAuthenticatedUserId()))
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

                return new ResponseEntity<>(requestService.getRequestsByUserId(userId), HttpStatus.OK);
        }
}