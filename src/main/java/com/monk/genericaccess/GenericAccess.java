package com.monk.genericaccess;

import com.monk.genericaccess.exception.IllegalAnnotationException;
import com.monk.genericaccess.filter.ObjectFilter;

import java.sql.SQLException;
import java.util.List;

public interface GenericAccess<T> {

    List<T> selectAll();

    List<T> selectWithFilter(ObjectFilter<T> keyOptions) throws IllegalAnnotationException;

    <K> T select(K key) throws IllegalAnnotationException;

    T insert(T t) throws SQLException, IllegalAnnotationException;


    T update(T t) throws SQLException, IllegalAnnotationException;


    boolean delete(T t) throws SQLException, IllegalAnnotationException;

    <K> boolean deleteById(K key) throws SQLException, IllegalAnnotationException;

    ObjectFilter<T> getFilter();


}
