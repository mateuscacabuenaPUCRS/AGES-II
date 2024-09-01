package com.ensportive.utils;

import java.lang.reflect.Field;

import org.springframework.stereotype.Component;

@Component
public class Patcher {
    public static <T> void patcher(T existingObject, T patchObject) throws IllegalAccessException {

        Class<?> clazz = existingObject.getClass();
        Field[] internFields = clazz.getDeclaredFields();
        for (Field field : internFields) {
            field.setAccessible(true);
            Object value = field.get(patchObject);
            if (value != null) {
                field.set(existingObject, value);
            }
            field.setAccessible(false);
        }
    }
}
