package com.monk.genericaccess.filter;

public class FilterOption {

    private String key;
    private Object value;
    private FilterOpcode opcode;
    private FilterOpcode.Logical logical;

    private FilterOption() {
    }

    private FilterOption(String key, Object value, FilterOpcode opcode) {
        this.key = key;
        this.value = value;
        this.opcode = opcode;
    }

    public FilterOption(String key, Object value, FilterOpcode opcode, FilterOpcode.Logical logical) {
        this.key = key;
        this.value = value;
        this.opcode = opcode;
        this.logical = logical;
    }

    private FilterOption(String key, Object value) {
        this(key, value, null);
    }

    public static FilterOption like(String key, String regex) {
        return new FilterOption(key, regex, FilterOpcode.LIKE, FilterOpcode.Logical.OR);
    }

    public static <T> FilterOption eq(String key, T value) {
        return new FilterOption(key, value, FilterOpcode.EQUAL, FilterOpcode.Logical.AND);
    }

    public static <T> FilterOption greaterThan(String key, T value) {
        return new FilterOption(key, value, FilterOpcode.GREATER, FilterOpcode.Logical.AND);
    }

    public static <T> FilterOption smallerThan(String key, T value) {
        return new FilterOption(key, value, FilterOpcode.SMALLER, FilterOpcode.Logical.AND);
    }

    public static FilterOption and(FilterOption option) {
        option.logical = FilterOpcode.Logical.AND;
        return option;
    }

    public static FilterOption or(FilterOption option) {
        option.logical = FilterOpcode.Logical.OR;
        return option;
    }

    public static <T> FilterOption[] between(String key, T startCondition, T endCondition) {
        return new FilterOption[]{
                new FilterOption(key, startCondition, FilterOpcode.GREATER),
                new FilterOption(key, endCondition, FilterOpcode.SMALLER)
            };
    }


    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public FilterOpcode getOpcode() {
        return opcode;
    }

    public FilterOpcode.Logical getLogical() {
        return logical;
    }
}
