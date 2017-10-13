package global;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import clientserver.Installer;

/**
 *
 * @author michel
 */
public class LoadPropFile {

    /**
     * methoden om de propties te laden
     *
     * @param fileName bestand naam en de map
     * @return de properties info
     * @throws IOException file exception
     */
    public static Properties loadPropFile(String fileName) throws IOException {
        Properties prop = new Properties();
        InputStream input;

        try {
            input = new FileInputStream("./config/"+fileName+".properties");
        } catch (FileNotFoundException ex) {
            System.err.println(ex);

            //run de installer
            Installer installer = new Installer();

            //reload de funtie
            return loadPropFile(fileName);
        }

        // load a properties file
        prop.load(input);

        //close input
        input.close();

        //return properties file
        return prop;
    }
}
