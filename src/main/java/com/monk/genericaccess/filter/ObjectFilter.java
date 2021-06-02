package com.monk.genericaccess.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectFilter<T>{

    private Class<T> tClass;
    private T domain;
    private final List<FilterOption> options = new ArrayList<>();;
    private final List<FilterOrder> orders = new ArrayList<>();;

    private int offset;
    private int limit;


    public ObjectFilter() {
    }



    public void limit(int offset, int limit) {
        limit(limit);
        this.offset = offset;
    }

    public void limit(int limit){
        this.limit = limit;
    }

    public ObjectFilter<T> add(FilterOption...option) {
        options.addAll(Arrays.asList(option));
        return this;
    }

    public ObjectFilter<T> add(FilterOrder...order) {
        orders.addAll(Arrays.asList(order));
        return this;
    }

    public List<FilterOption> getOptions() {
        return options;
    }

    public List<FilterOrder> getOrders() {
        return orders;
    }


    public int getLimit(){
        return this.limit;
    }

    public int getOffset() {
        return offset;
    }
}
