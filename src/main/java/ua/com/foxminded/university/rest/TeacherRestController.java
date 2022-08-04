package ua.com.foxminded.university.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.dto.TeacherDTO;
import ua.com.foxminded.university.models.Course;

import javax.validation.Valid;
import java.util.List;

@Tag(name="Teachers", description="Interaction with Teachers")
public interface TeacherRestController {

    @Operation(summary = "Save Teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")})
    ResponseEntity<TeacherDTO> create(@RequestBody @Valid TeacherDTO teacherDTO);

    @Operation(summary = "Get a teacher by it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Teacher not found")})
    ResponseEntity<TeacherDTO> find(@PathVariable("id") int id);

    @Operation(summary = "Get all Teachers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teachers found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Teachers not found")})
    ResponseEntity<List<TeacherDTO>> findAll();

    @Operation(summary = "Update Teacher info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")})
    ResponseEntity<TeacherDTO> update(@RequestBody @Valid TeacherDTO teacherDTO);

    @Operation(summary = "Delete teacher by it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")})
    ResponseEntity<Void> delete(@PathVariable("id") int id);
}
