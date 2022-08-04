package ua.com.foxminded.university.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.dto.StudentDTO;
import ua.com.foxminded.university.models.Course;

import javax.validation.Valid;
import java.util.List;

@Tag(name="Students", description="Interaction with Students")
public interface StudentRestController {

    @Operation(summary = "Save Student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")})
    ResponseEntity<StudentDTO> create(@RequestBody @Valid StudentDTO studentDTO);

    @Operation(summary = "Get a student by it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Student not found")})
    ResponseEntity<StudentDTO> find(@PathVariable("id") int id);

    @Operation(summary = "Get all Students")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Students not found")})
    ResponseEntity<List<StudentDTO>> findAll();

    @Operation(summary = "Update Student info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")})
    ResponseEntity<StudentDTO> update(@RequestBody @Valid StudentDTO studentDTO);

    @Operation(summary = "Delete student by it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")})
    ResponseEntity<Void> delete(@PathVariable("id") int id);
}
