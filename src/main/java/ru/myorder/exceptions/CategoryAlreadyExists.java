package ru.myorder.exceptions;

public class CategoryAlreadyExists extends RuntimeException{
    public CategoryAlreadyExists(String message){
        super(message);
    }
}
