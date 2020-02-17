package com.increff.assure.model.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter @AllArgsConstructor
public class ErrorData {
    private long index;
    private String message;

    public static String convert(List<ErrorData> dataList){
        ObjectMapper mapper=new ObjectMapper();
        try {
           return mapper.writeValueAsString(dataList);
        }
        catch(JsonProcessingException e)
        {
            //expected to never happen
            return "Cannot convert error message to json";
        }
    }
}
