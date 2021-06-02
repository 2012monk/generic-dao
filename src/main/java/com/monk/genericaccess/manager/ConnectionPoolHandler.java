package com.monk.genericaccess.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionPoolHandler {

    private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(ConnectionPoolHandler.class);

    private static BlockingQueue<Connection> pool;

    // prevent connection leak
    private static List<Connection> shutdownQueue;

    private final int initSize;

    private final int maxActive;

    private final int maxIdle;

    private final int minIdle;

    private final int wait = 20;

    private static final AtomicInteger getCalled = new AtomicInteger(0);

    private static final AtomicInteger returnCalled = new AtomicInteger(0);

    private static final AtomicInteger active = new AtomicInteger(0);

    private final TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    private final PoolConfig config = PoolConfig.WAIT_WITH_TIMEOUT;



    public ConnectionPoolHandler(int initSize, int maxActive, int maxIdle, int minIdle) {
        this.maxActive = maxActive;
        this.maxIdle = maxIdle;
        this.minIdle = minIdle;
        this.initSize = initSize;
        pool = new LinkedBlockingQueue<>(maxActive);
        shutdownQueue = new ArrayList<>();
        init();
    }

    public ConnectionPoolHandler(int initSize) {
        this(initSize, 30, 15, 10);
    }


    public ConnectionPoolHandler(){
        this(15);
    }

    public Connection getConnectionBlock(){
        try{
            return pool.take();
        }catch (Exception e) {
            e.printStackTrace();
        }
        log.debug(getCalled.incrementAndGet());
        return null;
    }

    public Connection getConnection(){
        log.debug("Total Active Connection Count : {}", active.get());
        if (config == PoolConfig.WAIT_WITH_TIMEOUT) {
            return getConnection(wait, timeUnit);
        }

        if (config == PoolConfig.WAIT_UNTIL_POSSIBLE) {
            return getConnectionBlock();
        }

        return getConnectionBlock();
    }

    public Connection getConnection(long timeout, TimeUnit timeUnit1) {
        try{
            Connection conn = pool.poll(timeout, timeUnit1);
            if (conn == null) {
                if (active.get() <= maxActive) {
                    conn = setConnection();
                }else {
                    return pool.take();
                }
            }
            return conn;
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            log.debug("total called count :{}", getCalled.incrementAndGet());
        }
        return null;
    }

    private Connection setConnection() throws InterruptedException {
        Connection conn = ConnectionFactory.createConnection();
        assert conn != null;
        pool.put(conn);
        shutdownQueue.add(conn);
        active.incrementAndGet();
        return conn;
    }


    public void closeConnection(Connection connection) {
        try{
            if (!pool.add(connection)){
                connection.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        log.debug("Total Released couned :{}", returnCalled.incrementAndGet());
        checkIdle();
    }

    protected void init() {
        for (int i=0; i < initSize; i++) {
            try {
                setConnection();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized int checkIdle() {
        if (pool.size() < minIdle) {
            while (pool.size() < minIdle) {
                try {
                    setConnection();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        else if (pool.size() > maxIdle) {
            while (pool.size() >= maxIdle) {
                try{
                    pool.take().close();
                    active.decrementAndGet();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    public int checkIdleConnections() {
        return pool.size();
    }

    public synchronized void shutdown() {
        List<Connection> list  = new ArrayList<>(pool.size() + 10);
        pool.drainTo(list);
        list.forEach(c -> {
            try {
                c.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        shutdownQueue.forEach(c -> {
            try {
                if (!c.isClosed()) {
                    c.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }


}
