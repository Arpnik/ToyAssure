package com.increff.assure.util;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConvertGeneric{

    //get all fields even from superclasses
    private static <T> List<Field> getFields(T t) {
        List<Field> fields = new ArrayList<>();
        Class clazz = t.getClass();
        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    public static <T,From> T convert(From from,Class<T> To ){
        T to= null;
        try {
            to = To.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            //expected to never happen
        }

        List<Field> toList=getFields(to);

        //hashMap fieldName to field
        HashMap<String,Field> fromFieldsMap = new HashMap<>();
        for(Field f:getFields(from))
        {
            fromFieldsMap.put(f.getName(),f);
        }

        //traverse new object and set common fields
        for (Field toField : toList)
        {
            String fieldName=toField.getName();
            if(fromFieldsMap.containsKey(fieldName))
            {
                toField.setAccessible(true);
                Field fromField= fromFieldsMap.get(fieldName);
                fromField.setAccessible(true);
                ReflectionUtils.setField(toField,to, ReflectionUtils.getField(fromFieldsMap.get(fieldName),from));
            }
        }
        return to;
    }
}

