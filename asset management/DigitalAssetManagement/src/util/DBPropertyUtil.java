package util;

import java.io.InputStream;
import java.util.Properties;

public class DBPropertyUtil {

    public static Properties getProperties(String fileName) {
        Properties prop = new Properties();
        try (InputStream is = DBPropertyUtil.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                System.out.println("Property file not found in classpath: " + fileName);
                return null;
            }
            prop.load(is);
        } catch (Exception e) {
            System.out.println("Error loading DB properties: " + e.getMessage());
        }
        return prop;
    }
}

