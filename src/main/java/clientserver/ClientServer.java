package clientserver;

import JSON.JSONArray;
import frameWork.ArrayListDriver;
import global.ConsoleColor;
import global.FileSystem;
import global.GetCointags;
import http.Http;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import marktData.MainSaveConstroller;
import privateRouter.BalanceSaverV2;
import privateRouter.DataGetter;
import privateRouter.Deposit;
import privateRouter.UpdateOrders;
import privateRouter.packageApi.Bitfinex;
import privateRouter.packageApi.BittrexProtocall;
import terminal.inputf;
import updater.DatabaseUpdater;

/**
 * Dit is het programma wat er voor zorgt dat de client kant up to data kan blijven
 *
 * @author michel
 */
public class ClientServer {

    private static String url = "127.0.0.1:7090";
    private static String versieCheck = "/versieCheck.txt";

    public static FileSystem fileSystem;
    public static DatabaseUpdater dataBaseUpdater;
    public static ArrayListDriver arrayListDriver;

    public static Http http;

    /**
     * Basis url van belangrijke websites
     */
    public static String cexIo = "https://cex.io/api";
    public static String coinmarketcapUrl = "https://api.coinmarketcap.com/v1";
    public static String clientUrlServer = "http://127.0.0.1:9091";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //maak het object aan voor de installerV2
        //InstallerV2 iv2 = new InstallerV2();
        //iv2.main();
        // hier wodt de terminal input aangemaakt en opgestart in een apart thread
        inputf i = new inputf();
        Thread thread = new Thread() {
            @Override
            public void run() {
                i.mainKlasse();
            }
        };

        thread.start();
        
        
        
        
        
        
        
        
        
        
        

        //http object
        http = new Http();

        //maak alle objecten aan
        fileSystem = new FileSystem();
        DatabaseUpdater dataBaseUpdater = new DatabaseUpdater();
        arrayListDriver = new ArrayListDriver();

        UpdateOrders updateOrrders = new UpdateOrders();
        updateOrrders.updateOrders();

        Deposit deposit = new Deposit();
        deposit.mainDeposit();

        DataGetter dataGetter = new DataGetter();
        try {
            dataGetter.getBalance("btc", "ltc", "bittrex");
        } catch (SQLException ex) {
            ConsoleColor.err("" + ex);
        } catch (Exception ex) {
            ConsoleColor.err("" + ex);
        }

        BalanceSaverV2 bV2 = new BalanceSaverV2();
        bV2.balance();

        //kijk of config folder bestaat
        String bestandLocatie = "config/";
        Path path = Paths.get(bestandLocatie);
        if (!Files.exists(path)) {

            //maak de folder aan
            try {
                Files.createDirectories(Paths.get(bestandLocatie));

            } catch (IOException ex) {
                System.err.println("Er is een error op getreden met het aanmaken van de map. " + ex);
                System.exit(0);
            }
        }

        //laat mainSaveController
        /* try {
            MainSaveConstroller mainSaveConstroller = new MainSaveConstroller();
        } catch (Exception ex) {
            System.err.println(ex);
            System.exit(0);
        }*/
    }

    /**
     * VersieChcek methoden is om na te gaan of het database nog up to data is
     */
    /*private static void versieCheck() {

        //Http http = new Http();
        //kijk of versieCheck.txt bestaat
        String naamVersieCheck = "versieCheck.txt";
        String versieCheckLocal;
        File bestandCheck = new File("config/versieCheck.txt");

        //als het bestand niet bestaat
        if (!bestandCheck.exists()) {

            //run database updater
            dataBaseUpdater.databaseUpdater();

            //run de methoden op de nieuwe versie check op te slaan
            //versieCheckUpdate();
        } else {

            //vraag aan de server op welke versie er bij de server staat
            String versieServer = http.GetHttp(url + versieCheck);

            //vraag de versieCheck op van de server
            //lees de bestand inhoud
            try {
                versieCheckLocal = fileSystem.readFile(naamVersieCheck);
            } catch (IOException ex) {
                System.err.println("Er is een rror opgetreden bij het lezen van de file " + naamVersieCheck);
                return;
            }

            //if stament
            if (!versieCheckLocal.equals(versieServer)) {
                //roep de klasse op die alle data updaten
                dataBaseUpdater.databaseUpdater();
            }

        }
    }*/
}
