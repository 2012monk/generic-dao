package com.monk.genericaccess.manager;

import java.sql.Connection;

public class ConnectionManagerPool implements ConnectionManager{


    private static final ConnectionPoolHandler poolHandler = new ConnectionPoolHandler();

    public ConnectionManagerPool(){}

    @Override
    public Connection getConn() {
        return poolHandler.getConnection();
    }

    @Override
    public void close(Connection conn) {
        poolHandler.closeConnection(conn);
    }
}
