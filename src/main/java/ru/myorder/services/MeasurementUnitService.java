package ru.myorder.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.myorder.exceptions.MeasurementUnitAlreadyExists;
import ru.myorder.models.MeasurementUnit;
import ru.myorder.payloads.AddMeasurementUnitRequest;
import ru.myorder.repositories.MeasurementUnitRepository;

import java.util.List;

@Service
public class MeasurementUnitService {

    private final Logger LOGGER = LoggerFactory.getLogger(MeasurementUnitService.class);

    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;

    public List<MeasurementUnit> getMeasurementUnits(){
        LOGGER.info("GET MEASUREMENT UNITS");
        return measurementUnitRepository.findAll().stream().toList();

    }

    public void addMeasurementUnit(AddMeasurementUnitRequest addMesReq){
        String measurementUnitName = addMesReq.getName().strip().toLowerCase();
        if(measurementUnitRepository.existsByName(measurementUnitName)){
            throw new MeasurementUnitAlreadyExists("данная единица измерения уже существует");
        }
        MeasurementUnit mesUnit = new MeasurementUnit(measurementUnitName);
        measurementUnitRepository.save(mesUnit);
    }



}

