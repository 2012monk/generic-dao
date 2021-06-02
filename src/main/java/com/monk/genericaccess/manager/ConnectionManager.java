package com.monk.genericaccess.manager;

import java.sql.Connection;

public interface ConnectionManager {

    Connection getConn();

    void close(Connection conn);

}
