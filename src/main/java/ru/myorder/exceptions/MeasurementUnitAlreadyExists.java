package ru.myorder.exceptions;

public class MeasurementUnitAlreadyExists extends RuntimeException{
    public MeasurementUnitAlreadyExists(String message){
        super(message);
    }
}
