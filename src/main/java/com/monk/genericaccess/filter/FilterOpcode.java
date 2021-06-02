package com.monk.genericaccess.filter;

public enum FilterOpcode {

    EQUAL("="),
    LIKE(" LIKE "),
    GREATER(">="),
    SMALLER("<=");
    String op;

    public String getOp(){
        return op;
    }

    FilterOpcode(String s) {
        op = s;
    }

    public enum Logical{
        OR,
        AND
    }

    public enum Order {
        ASC,
        DESC
    }
}
