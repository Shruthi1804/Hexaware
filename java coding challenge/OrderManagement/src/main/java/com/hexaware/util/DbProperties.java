package com.hexaware.util;

import com.hexaware.util.Constants;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class DbProperties {
    private static Properties props = null;
    public Scanner scanner = new Scanner(System.in);
    static {
        props = getDbProperties();
    }
   private static Properties getDbProperties() {
        Properties props = new Properties();
        try {
            props.load(new FileReader(Constants.DB_FILE_NAME));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return props;
    }

 public static String getDriver() {
        return props.getProperty(Constants.DB_DRIVER);

    }
    public static String getDbUrl() {
        return props.getProperty(Constants.DB_URL);

    }
    public static Properties getProps() {
        return props;

    }
}
