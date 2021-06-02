package com.monk.genericaccess.demo;


import com.monk.genericaccess.annotations.Key;

import java.util.List;

public class Store {

    @Key
    private String storeId;
    private String userId;
    private List<OrderInfo> orderList;


}
