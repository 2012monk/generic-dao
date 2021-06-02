package com.monk.genericaccess.manager;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

    private static String url;
    private static String username;
    private static String password;

    static {
        try {
            Class.forName("com.monk.genericaccess.loader.ConnectionInfoLoader");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection createConnection() {
        try{
            return DriverManager.getConnection(url, username, password);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
