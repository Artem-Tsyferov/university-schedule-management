package ua.com.foxminded.university.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.dto.GroupDTO;
import ua.com.foxminded.university.models.Course;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Groups", description = "Interaction with Groups")
public interface GroupRestController {

    @Operation(summary = "Save Group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")})
    ResponseEntity<GroupDTO> create(@RequestBody @Valid GroupDTO groupDTO);

    @Operation(summary = "Get a group by it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Group",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Group not found")})
    ResponseEntity<GroupDTO> find(@PathVariable("id") int id);

    @Operation(summary = "Get all Groups")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Groups found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Groups not found")})
    ResponseEntity<List<GroupDTO>> findAll();

    @Operation(summary = "Update Group info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")})
    ResponseEntity<GroupDTO> update(@RequestBody @Valid GroupDTO groupDTO);

    @Operation(summary = "Delete Group by it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")})
    ResponseEntity<Void> delete(@PathVariable("id") int id);
}
