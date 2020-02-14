package com.increff.assure.Controller;

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
public abstract class AbstractAppRestControllerAdvice {
    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageData handle(ApiException e) {
        MessageData data = new MessageData();
        data.setMessage(e.getMessage());
        return data;
    }



//    @ExceptionHandler(ConstraintViolationException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public MessageData handle(ConstraintViolationException e) {
//        MessageData data = new MessageData();
//        data.setMessage(e.getMessage());
//        return data;
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageData handle(MethodArgumentNotValidException e) {
        MessageData data = new MessageData();
        final StringBuffer errors = new StringBuffer();
        for (final FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.append("\n");
            //char index=(error.getField()).toString().charAt(12);
            errors.append(error.getField()+": ");
            errors.append(error.getDefaultMessage());
            System.out.println(error.getArguments());
            System.out.println(error.getObjectName());
        }
        data.setMessage(errors.toString());
        return data;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageData handle(Throwable e) throws Throwable {
        MessageData data = new MessageData();
        data.setMessage("An unknown error has occurred - " +e.getMessage());
        return data;
    }
}
