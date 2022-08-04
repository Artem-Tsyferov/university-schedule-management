package ua.com.foxminded.university.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.dto.CourseDTO;
import ua.com.foxminded.university.models.Course;

import javax.validation.Valid;
import java.util.List;

@Tag(name="Courses", description="Interaction with Courses")
public interface CourseRestController {

    @Operation(summary = "Save Course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")})
    ResponseEntity<CourseDTO> create(@RequestBody @Valid CourseDTO courseDTO);

    @Operation(summary = "Get a course by it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Course not found")})
    ResponseEntity<CourseDTO> find(@PathVariable("id") int id);

    @Operation(summary = "Get all Courses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Courses not found")})
    ResponseEntity<List<CourseDTO>> findAll();

    @Operation(summary = "Update Course info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")})
    ResponseEntity<CourseDTO> update(@RequestBody @Valid CourseDTO courseDTO);

    @Operation(summary = "Delete course by it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")})
    ResponseEntity<Void> delete(@PathVariable("id") int id);
}
