package clientserver;

import global.FileSystem;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Installer klasse
 *
 * @author michel
 */
public class Installer {

    //folder naam locatie
    private final String[] FOLDER_NAME = {"config", "temp"};
    private final String[] BESTAND_CHECK = {"apiKeys.properties", "config.properties"};

    //maak object aan
    FileSystem fileSystem = new FileSystem();

    //boolean
    private boolean succes = true;

    /**
     * Constructor die de sub methodes aan roept
     */
    public Installer() {

        //roep de folderCheck op
        folderCheck();
        bestandCheck();

    }

    /**
     * Methoden die na kijkt of de folder bestaat en als die niet bestaat maakt de folder aan
     */
    public void folderCheck() {

        //loop door de array heen
        for (int i = 0; i < FOLDER_NAME.length; i++) {

            //maak het object folder aan
            File folder = new File(FOLDER_NAME[i]);

            //kijk of de folder bestaat
            if (!folder.exists()) {

                try {
                    //maak de folder aan
                    folder.mkdirs();

                    //geef aan de de folder is aangemaakt
                    System.out.println("Folder aangemaakt: " + folder.getName());

                } catch (SecurityException se) {
                    System.err.println("Er is een error bij de installer om folder aan te maken. \n Error is " + se);
                    succes = false;
                }
            } else {
                System.out.println("Folder " + FOLDER_NAME[i] + " bestaat al.");
            }
        }
    }

    /**
     * Methoden die kijkt of de bestanden er zijn
     */
    private void bestandCheck() {

        for (int i = 0; i < BESTAND_CHECK.length; i++) {
            String bestandNaam = BESTAND_CHECK[i];

            //maak de variable bestand naam
            File f = new File("./config/" + bestandNaam);

            //kijk of het bestand bestaat
            if (!f.exists()) {

                //switch
                if ("apiKeys.properties".equals(bestandNaam)) {

                    try {
                        apiKey();
                    } catch (IOException ex) {
                        System.err.println(ex);
                    }

                }
            } else {
                System.out.println("Het bestand ");
            }
        }
    }

    /**
     * Methoden die een bestand aanmaakt en er inhoud in zet
     *
     * @throws FileNotFoundException Als het bestand niet is gevonden
     */
    private void apiKey() throws IOException {
        
        //input file
        Properties prop = new Properties();
        OutputStream output = null;

        output = new FileOutputStream("./config/apiKeys.properties");

        // set the properties value
        prop.setProperty("bittrexApiKey", "");
        prop.setProperty("bittrexApiSecretKey", "");

        // save properties to project root folder
        prop.store(output, null);
    }
}
