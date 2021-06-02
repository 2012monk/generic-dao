package com.monk.genericaccess;

import com.monk.genericaccess.exception.IllegalAnnotationException;
import com.monk.genericaccess.filter.ObjectFilter;
import com.monk.genericaccess.mapper.SqlMapper;


import java.sql.SQLException;
import java.util.List;

public abstract class GenericAccessImpl<T> implements GenericAccess<T> {

    private Class<T> entity;
    protected SqlMapper mapper = new SqlMapper();
    private String keyName;


    public void set(Class<T> tClass) throws NullPointerException, IllegalAnnotationException{
        if (tClass == null) {
            throw new NullPointerException("class should not be null");
        }
        // key annotation 이 없으면 exception 발생
        keyName = mapper.getKeyName(tClass);
        entity = tClass;
    }


    @Override
    public List<T> selectAll() {
        return mapper.selectAll(entity);
    }

    @Override
    public <K> T select(K key) throws IllegalAnnotationException {
        return mapper.select(entity, key);
    }

    @Override
    public T insert(T t) throws SQLException, IllegalAnnotationException {
        return mapper.insertMapper(t);
    }

    @Override
    public T update(T t) throws SQLException, IllegalAnnotationException {
        return mapper.updateMapper(t);
    }

    @Override
    public boolean delete(T t) throws SQLException, IllegalAnnotationException {
        return mapper.deleteMapper(t);
    }

    @Override
    public <K> boolean deleteById(K key) throws SQLException, IllegalAnnotationException {
        return mapper.deleteByKey(entity, key);
    }

    @Override
    public List<T> selectWithFilter(ObjectFilter<T> keyOptions) throws IllegalAnnotationException {
        return mapper.filteredSelect(entity, keyOptions);
    }

    @Override
    public ObjectFilter<T> getFilter() {
        return new ObjectFilter<>();
    }
}
