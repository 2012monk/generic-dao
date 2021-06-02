package com.monk.genericaccess.filter;

public class FilterOrder {

    private final String key;
    private final FilterOpcode.Order order;


    private FilterOrder(String key, FilterOpcode.Order order) {
        this.key = key;
        this.order = order;
    }

    public static FilterOrder asc(String key) {
        return new FilterOrder(key, FilterOpcode.Order.ASC);
    }

    public static FilterOrder desc (String key){
        return new FilterOrder(key, FilterOpcode.Order.DESC);
    }

    public String getKey() {
        return key;
    }


    public FilterOpcode.Order getOrder() {
        return order;
    }
}
