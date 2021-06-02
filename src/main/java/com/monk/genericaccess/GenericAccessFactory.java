package com.monk.genericaccess;

import com.monk.genericaccess.exception.IllegalAnnotationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericAccessFactory {

    private static final Map<Class<?>, GenericDao<?>> list = new HashMap<>();

    public static  <T> GenericDao<T> getInstance(Class<T> tClass) {
        if (list.get(tClass) == null){
            GenericDao<T> genericAccess = new GenericDao<>();
            try {
                genericAccess.set(tClass);
            } catch (IllegalAnnotationException e) {
                e.printStackTrace();
            }
            list.put(tClass, genericAccess);
        }

        return (GenericDao<T>) list.get(tClass);
    }

}
