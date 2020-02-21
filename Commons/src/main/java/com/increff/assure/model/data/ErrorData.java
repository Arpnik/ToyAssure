package com.increff.assure.model.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ErrorData {
    private Long index;
    private String message;

    public static String convert(List<ErrorData> dataList) {
        try {
            return JSONUtil.serialize(dataList);
        } catch (JSONException e) {
            //expected to never happen
            System.out.println("This serialize didn't work");
            return "Cannot convert error message to json";
        }
    }
}
