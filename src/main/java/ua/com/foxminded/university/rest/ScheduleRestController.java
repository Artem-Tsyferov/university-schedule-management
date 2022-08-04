package ua.com.foxminded.university.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.com.foxminded.university.dto.ScheduleDTO;
import ua.com.foxminded.university.models.Course;

import java.util.List;

@Tag(name="Schedules", description="Interaction with Schedules")
public interface ScheduleRestController {

    @Operation(summary = "Get Schedule for a month for a student by it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedules found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Schedules not found")})
    ResponseEntity<List<ScheduleDTO>> findForMonthForStudent(@PathVariable("id") int id);

    @Operation(summary = "Get Schedule for a month for a teacher by it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedules found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Schedules not found")})
    ResponseEntity<List<ScheduleDTO>> findForMonthForTeacher(@PathVariable("id") int id);
}
