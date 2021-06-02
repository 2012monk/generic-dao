package com.monk.genericaccess.mapper;

import com.monk.genericaccess.exception.IllegalAnnotationException;
import com.monk.genericaccess.filter.FilterOption;
import com.monk.genericaccess.filter.FilterOrder;
import com.monk.genericaccess.filter.ObjectFilter;
import com.monk.genericaccess.annotations.Key;
import com.monk.genericaccess.manager.ConnectionManager;
import com.monk.genericaccess.manager.ConnectionManagerFactory;
import com.monk.genericaccess.util.NameConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class SqlMapper {


    private ObjectMapper mapper;
    private static final NameConverter converter = new NameConverter();
    private static final Map<Class<?>, Field[]> cachedFields = new HashMap<>();
    private static final ConnectionManager manager = ConnectionManagerFactory.getManager();


    public SqlMapper() {}



    // hint generate pojo from resultSet
    public <T> T setPOJO(Class<T> target, ResultSet rs)
            throws InstantiationException, IllegalAccessException {
        T out = target.newInstance();
        cachedFields.computeIfAbsent(target, k -> target.getDeclaredFields());
        Field[] fields = cachedFields.get(target);

        for (Field f: fields) {
            f.setAccessible(true);
            String name = converter.convertToDbNameConvention(f.getName());

            try{
                f.set(out, rs.getObject(name));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return out;
    }

    public <T> void insertMapper (List<T> tList) throws SQLException{
        T t = tList.get(0);
        String tbName = converter.convertClassNameToDbName(t.getClass().getSimpleName());

        Field[] fields = t.getClass().getDeclaredFields();
        StringJoiner columns = new StringJoiner(",","(",")");
        StringJoiner values = new StringJoiner(",","(",")");

        for (Field f:fields) {
            f.setAccessible(true);
            columns.add(f.getName());
            values.add("?");
        }


    }



    public <T, K> boolean deleteByKey(Class<T> tClass, K key) throws IllegalAnnotationException{
        String tbName = converter.convertClassNameToDbName(tClass.getSimpleName());
        String keyName = converter.convertToDbNameConvention(getKeyName(tClass));

        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ").append(tbName).append(" WHERE ").append(keyName).append("=?");
        Connection conn = manager.getConn();
        try {
            assert conn != null;
            try (PreparedStatement preparedStatement = conn.prepareStatement(sb.toString())) {
                preparedStatement.setObject(1, key);
                if (preparedStatement.executeUpdate() > 0) {
                    conn.commit();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            manager.close(conn);
        }

        return false;
    }

    public <T> boolean deleteMapper (T t) throws SQLException, IllegalArgumentException, IllegalAnnotationException {
        String tbName = converter.convertClassNameToDbName(t.getClass().getSimpleName());
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ").append(tbName).append(" WHERE ");
        Object key = null;

        for (Field f:t.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try{
                if (f.getDeclaredAnnotation(Key.class) != null && f.get(t) != null){
                    sb.append(converter.convertToDbNameConvention(f.getName())).append("=?");
                    key = f.get(t);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if (key == null) {
            throw new IllegalArgumentException("KeyAnnotation not found");
        }

        Connection conn = manager.getConn();
        try {
            assert conn != null;
            try (PreparedStatement prst = conn.prepareStatement(sb.toString())) {
                prst.setObject(1, key);
                if (prst.executeUpdate() > 0) {
                    conn.commit();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            manager.close(conn);
        }
        return false;
    }

    public <T> T updateMapper (T t) throws SQLException ,IllegalAnnotationException{
        String tbName = converter.convertClassNameToDbName(t.getClass().getSimpleName());
        String keyName = getKeyName(t.getClass());
        ArrayList<Object> list = new ArrayList<>();

        StringJoiner values = new StringJoiner(",");
        Field[] fields = t.getClass().getDeclaredFields();
        Object keyObj = null;
        for (Field f:fields) {
            f.setAccessible(true);
            try{
                Object o = f.get(t);
                String name = converter.convertToDbNameConvention(f.getName());
                if (o != null) {
                    if (f.getName().equals(keyName)){
                        keyObj = f.get(t);
                    }
                    else {
                        values.add(name + "=?");
                        list.add(o);
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (keyObj == null) {
            throw new IllegalArgumentException("key object should not be null");
        }


        String preSql = "UPDATE {table} SET {values} WHERE {key}=?";
        String sql = preSql.replace("{table}", tbName)
                .replace("{values}", values.toString())
                .replace("{key}", keyName);

        Connection conn = manager.getConn();
        try {
            assert conn != null;
            try (PreparedStatement prst = conn.prepareStatement(sql)){
                for (int i=0;i<list.size();i++) {
                    prst.setObject(i + 1, list.get(i));
                }
                prst.setObject(list.size()+1, keyObj);
                if (prst.executeUpdate() > 0) {
                    conn.commit();
                    return t;
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        finally {
            manager.close(conn);
        }
        return null;
    }


    /**
     * Insert mapper t.
     *
     * @param t   Domain object
     * @return if success return t or null
     *
     */
    public  <T> T insertMapper (T t) {
        String tbName = converter.convertClassNameToDbName(t.getClass().getSimpleName());
        ArrayList<Object> list = new ArrayList<>();
        StringJoiner columns = new StringJoiner(",","(",")");
        StringJoiner values = new StringJoiner(",","(",")");

        Field[] fields = t.getClass().getDeclaredFields();
        for(Field f: fields) {
            f.setAccessible(true);
            Object o = null;
            try {
                o = f.get(t);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            // check reference type
            if (o != null) {
                    columns.add(converter.convertToDbNameConvention(f.getName()));
                    values.add("?");
                    list.add(o);
//                if (o.getClass().equals(String.class) || !o.getClass().isAssignableFrom(ReferenceType.class)){
//                }
            }
        }
        String sqlPre = "INSERT INTO {table} {columns} VALUES {values}";
        String sql = sqlPre.replace("{table}", tbName)
                        .replace("{columns}", columns.toString())
                        .replace("{values}", values.toString());

        Connection conn = manager.getConn();
        try{
            try(PreparedStatement preparedStatement = conn.prepareStatement(sql)){
                for (int i=0;i<list.size();i++){
                    Object o = list.get(i);
                    preparedStatement.setObject(i+1, o);
                }
                if (preparedStatement.executeUpdate() > 0) {
                    conn.commit();
                    return t;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            manager.close(conn);
        }

        return null;
    }

    public <T> String getKeyName(Class<T> tClass) throws IllegalAnnotationException{
        Field[] fields = tClass.getDeclaredFields();
        String name = "";
        for (Field f:fields) {
            f.setAccessible(true);
            if (f.getDeclaredAnnotation(Key.class) != null) {
                name = f.getName();
            }
        }

        if (name.equals("")) {
            throw new IllegalAnnotationException("no key annotation");
        }

        return name;
    }

    public <T> List<T> filteredSelect(Class<T> tClass, ObjectFilter<T> option) {
        String tbName = converter.convertClassNameToDbName(tClass.getSimpleName());

        String sqlp = "SELECT * FROM " + tbName + " WHERE " ;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(tbName).append(" WHERE ");

        List<Object> list = new ArrayList<>();
        List<FilterOption> options = option.getOptions();
        List<FilterOrder> orders = option.getOrders();
        int limit = option.getLimit();
        int offset = option.getOffset();

        int numberOfOptions = options.size();

        for (int i = 0; i< numberOfOptions; i++) {
            FilterOption f = options.get(i);
            sql.append(converter.convertToDbNameConvention(f.getKey()));
            sql.append(f.getOpcode().getOp());

            sql.append("?").append(" ");
            if (i != numberOfOptions - 1) {
                sql.append(f.getLogical()).append(" ");
            }
            list.add(f.getValue());
        }

        if (!orders.isEmpty()){
            sql.append("ORDER BY ");
            StringJoiner joiner = new StringJoiner(",");
            orders.forEach(f -> {
                joiner.add(converter.convertToDbNameConvention(f.getKey()) + " " + f.getOrder());
            });
            sql.append(joiner);
        }

        if (limit != 0) {
            sql.append("LIMIT ?");
            list.add(limit);
            if (offset != 0){
                sql.append(" OFFSET ?");
                list.add(offset);
            }
        }

        Connection conn = manager.getConn();
        try {
            assert conn != null;
            try(PreparedStatement prst = conn.prepareStatement(sql.toString())){
                for (int i=0;i<list.size();i++) {
                    prst.setObject(i+1, list.get(i));
                }
                System.out.println(sql);
                System.out.println(list.get(0));

                ResultSet rs = prst.executeQuery();
                List<T> out = new ArrayList<>();
                while (rs.next()){
                    out.add(setPOJO(tClass, rs));
                }
                return out;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            manager.close(conn);
        }
        return null;
    }


    public <T, K> T select(Class<T> tClass, K key) throws IllegalAnnotationException{
        String tbName = converter.convertClassNameToDbName(tClass.getSimpleName());
        String keyName = converter.convertToDbNameConvention(getKeyName(tClass));
        String sql = "SELECT * FROM " + tbName  + " WHERE " + keyName+ "=?";
        Connection conn = manager.getConn();
        try {
            assert conn != null;
            try(PreparedStatement preparedStatement = conn.prepareStatement(sql)){
                preparedStatement.setObject(1, key);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    return setPOJO(tClass, rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            manager.close(conn);
        }
        return null;
    }

    public <T> List<T> selectAll(Class<T> tClass) {
        String tbName = converter.convertClassNameToDbName(tClass.getSimpleName());
        String sql = "SELECT * FROM " + tbName;

        Connection conn = manager.getConn();
        List<T> list = new ArrayList<>();
        try {
            assert conn != null;
            try(PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    list.add(setPOJO(tClass, rs));
                }

                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            manager.close(conn);
        }
        return null;
    }




}
