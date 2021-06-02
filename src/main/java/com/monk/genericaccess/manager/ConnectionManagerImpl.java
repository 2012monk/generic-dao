package com.monk.genericaccess.manager;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManagerImpl implements ConnectionManager{

    private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(ConnectionManagerImpl.class);

    @Override
    public Connection getConn(){
        Connection conn = null;

        try {
            conn = ConnectionFactory.createConnection();
            if (conn != null) {
                log.debug("Connection loaded");
                return conn;
            }
        } catch (Exception e) {
            log.warn("connection load failed");
            e.printStackTrace();
        }
        return conn;
    }

    @Override
    public void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public ConnectionManagerImpl get(){
        return this;
    }
}
