package com.monk.genericaccess.manager;

public class ConnectionManagerFactory {


    private static ConnectionManager manager;


    public static ConnectionManager getManager(){
        return getManager(true);
    }

    public static ConnectionManager getManager(boolean pooled) {
        if (manager != null) {
            return manager;
        }
        if (pooled){
            manager = new ConnectionManagerPool();
        }
        else {
            manager = new ConnectionManagerImpl();
        }
        return manager;
    }
}
