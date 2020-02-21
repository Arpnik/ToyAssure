package com.increff.assure.Controller;

import com.increff.assure.model.Exception.ApiException;
import com.increff.assure.model.data.ErrorData;
import com.increff.assure.model.data.MessageData;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public abstract class AbstractAppRestControllerAdvice {
    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageData handle(ApiException e) {
        MessageData data = new MessageData();
        data.setMessage(e.getMessage());
        return data;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageData handle(MethodArgumentNotValidException e) {
        MessageData data = new MessageData();
        String errors="";
        List<ErrorData> errorList = new ArrayList<>();
        for (final FieldError er : e.getBindingResult().getFieldErrors()) {
            //get index of row having error
            if(!er.getField().contains("]"))
            {
                if(errors.length() > 0)
                    errors += ", ";
                errors += er.getDefaultMessage();
                continue;
            }
            String index=(er.getField().split("\\]")[0]);
            index=index.split("\\[")[1];
            //create error
            ErrorData errorData = new ErrorData(Long.parseLong(index), er.getDefaultMessage());
            errorList.add(errorData);
        }

        if(errorList.size()!=0)
            errors += ErrorData.convert(errorList);

        data.setMessage(errors);
        return data;
    }

    @ExceptionHandler(RestClientResponseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageData handle(RestClientResponseException e) {
        MessageData data = new MessageData();
        data.setMessage(e.getResponseBodyAsString());
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
