package ru.myorder.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.myorder.dtos.JwtDTO;
import ru.myorder.dtos.MessageDTO;
import ru.myorder.exceptions.MeasurementUnitAlreadyExists;
import ru.myorder.models.MeasurementUnit;
import ru.myorder.payloads.AddMeasurementUnitRequest;
import ru.myorder.services.MeasurementUnitService;

import java.util.List;

@Tag(name="MseasurementUnitController")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/measurement_unit")
public class MeasurementUnitController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementUnitController.class);
    @Autowired
    private MeasurementUnitService measurementUnitService;

    @Operation(summary="добавление единицы измерения")
    @PostMapping("/add")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = MessageDTO.class))})
    @ApiResponse(responseCode = "403", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = MessageDTO.class))})
    public ResponseEntity<MessageDTO> addMesUnit(@RequestBody AddMeasurementUnitRequest addMesReq){
        LOGGER.info("ADD MES UNIT");
        try{
            measurementUnitService.addMeasurementUnit(addMesReq);
            return ResponseEntity.ok(new MessageDTO("единица измерения добавлена"));
        }
        catch (MeasurementUnitAlreadyExists muae){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageDTO(muae.getMessage()));
        }
        catch (Exception e){
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageDTO("что-то пошло не так"));
        }

    }

    @Operation(summary="получение всех единиц измерения")
    @GetMapping("/all")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",  array=@ArraySchema(schema=@Schema(implementation = MeasurementUnit.class)))})
    public ResponseEntity<?> getAllMeasurementUnits(){
        LOGGER.info("GET ALL MEASUREMENT UNITS");
        List<MeasurementUnit> measurementUnits =  measurementUnitService.getMeasurementUnits();
        return ResponseEntity.ok(measurementUnits);
    }




    @Operation(summary="удаление единицы измерения")
    @DeleteMapping("/{measurement_id}")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = MessageDTO.class))})
    public ResponseEntity<MessageDTO> deleteMeasurementUnutById(@PathVariable("measurement_id") Long measurementId){
        LOGGER.info("DELETE MEASUREMENT ID");
        return ResponseEntity.ok(new MessageDTO("единица измерения удалена"));
    }
}
