package global;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Waar alles wat met io te maken heeft
 *
 * @author michel
 */
public class FileSystem {

    private final String LOCATIE_FOLDER = "config/";

    /**
     * Read het bestand
     *
     * @param naamBestand Naam van het bestand
     * @return betand inhoud
     * @throws IOException als er een error is
     */
    public String readFile(String naamBestand) throws IOException {
        return new String(Files.readAllBytes(Paths.get(LOCATIE_FOLDER + naamBestand)), StandardCharsets.UTF_8);
    }

    public void whriteFile(String fileLocatie, String data) throws IOException {

        //close the buffering
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileLocatie));

        //writter
        bw.write(data);

        //close the writer
        bw.close();

        PrintWriter writer = new PrintWriter(fileLocatie);
        writer.println(data);
        writer.close();
    }
}
