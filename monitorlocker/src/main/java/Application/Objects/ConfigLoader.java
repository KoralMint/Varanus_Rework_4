package Application.Objects;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private Properties properties = new Properties();
    private boolean doOutput = false;

    public void setDoOutput(boolean doOutput) {
        this.doOutput = doOutput;
    }

    public void loadConfig(String filePath) {
        try (InputStream input = new FileInputStream(filePath)) {
            if (doOutput) System.out.println("Loading config from " + filePath);
            properties.load(input);
        } catch (IOException ex) {
            // make new file
            try {
                properties.store(new java.io.FileOutputStream(filePath), "auto generated config file");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (doOutput) System.out.println("- " + key + " : " + value);
        return value;
    }

    public String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key, defaultValue);
        if (doOutput) System.out.println("- " + key + " : " + value);
        return value;
    }
}
