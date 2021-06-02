package com.monk.genericaccess.loader;

import com.monk.genericaccess.manager.ConnectionFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

public class ConnectionInfoLoader {

    private static Properties properties = new Properties();
    private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(ConnectionInfoLoader.class);

    static {
        try{
            setProperties();
            load();
        }catch (IOException | URISyntaxException e) {
            log.warn("load failed file not exist", e);
            throw new IllegalArgumentException("file does not exist");
        } catch (ClassNotFoundException e) {
            log.warn("Class not found", e);
        } catch (IllegalAccessException e) {
            log.warn("");
        }
    }

    private static void setProperties() throws URISyntaxException, IOException {
        File file;
        // set caller class
        URL url = ConnectionInfoLoader.class.getClassLoader().getResource("connection.properties");
        if (url != null) {
            file = new File(url.toURI());
        }else {
            file = new File("./src/main/resources/connection.properties");
        }

        try(FileReader fr = new FileReader(file)){
            properties.load(fr);
        }
    }

    private static void load() throws IllegalAccessException, ClassNotFoundException {
        Field[] fields = ConnectionFactory.class.getDeclaredFields();
        for (Field f:fields) {
            f.setAccessible(true);
            String propName = properties.getProperty(f.getName());
            if (propName != null) {
                f.set(String.class, properties.get(f.getName()));
                if (f.getName().toUpperCase(Locale.ROOT).equals("DRIVER")) {
                    Class.forName(properties.getProperty(f.getName()));
                }
            }

        }
    }
}
