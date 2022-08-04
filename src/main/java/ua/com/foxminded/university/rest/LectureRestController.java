package ua.com.foxminded.university.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.dto.LectureDTO;
import ua.com.foxminded.university.models.Course;

import javax.validation.Valid;
import java.util.List;

@Tag(name="Lectures", description="Interaction with Lectures")
public interface LectureRestController {
    @Operation(summary = "Save Lecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")})
    ResponseEntity<LectureDTO> create(@RequestBody @Valid LectureDTO lectureDTO);

    @Operation(summary = "Get a lecture by it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lecture found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Lecture not found")})
    ResponseEntity<LectureDTO> find(@PathVariable("id") int id);

    @Operation(summary = "Get all Lectures")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lectures found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Lectures not found")})
    ResponseEntity<List<LectureDTO>> findAll();

    @Operation(summary = "Update Lecture info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")})
    ResponseEntity<LectureDTO> update(@RequestBody @Valid LectureDTO lectureDTO);

    @Operation(summary = "Delete lecture by it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")})
    ResponseEntity<Void> delete(@PathVariable("id") int id);
}
