package com.increff.assure.controller;

import com.increff.assure.Controller.AbstractAppRestControllerAdvice;
import com.increff.assure.model.data.MessageData;
import com.increff.assure.service.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class AppRestControllerAdvice extends AbstractAppRestControllerAdvice {

}