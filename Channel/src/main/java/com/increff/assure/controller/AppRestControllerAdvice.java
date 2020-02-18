package com.increff.assure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.increff.assure.Controller.AbstractAppRestControllerAdvice;
import com.increff.assure.model.data.MessageData;
import com.increff.assure.service.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class AppRestControllerAdvice extends AbstractAppRestControllerAdvice {

}
