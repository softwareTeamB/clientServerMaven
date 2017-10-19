package clientserver;

import global.ConsoleColor;
import global.FileSystem;
import http.Http;
import java.io.File;
import java.io.IOException;
import java.net.ProtocolException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import marktData.MainSaveConstroller;
import privateRouter.BalanceSaver;
import privateRouter.DataGetter;
import privateRouter.Deposit;
import privateRouter.UpdateOrders;
import privateRouter.packageApi.BittrexProtocall;
import terminal.input;
import updater.DatabaseUpdater;

/**
 * Dit is het programma wat er voor zorgt dat de client kant up to data kan blijven
 *
 * @author michel
 */
public class ClientServer {

    private static String url = "127.0.0.1:7090";
    private static String versieCheck = "/versieCheck.txt";
    static FileSystem fileSystem = new FileSystem();
    static Http http = new Http();
    static DatabaseUpdater dataBaseUpdater = new DatabaseUpdater();
    static FileSystem f = new FileSystem();
    static BalanceSaver balanceSaver = new BalanceSaver();

    private static TimerTask task1 = new TimerTask() {

        @Override
        public void run() {

            System.out.println("i");
        }
    };

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        InstallerV2 iv2 = new InstallerV2();
        iv2.main();

        input i = new input();
        Thread thread = new Thread() {
            public void run() {
                i.mainKlasse();
            }
        };

        //great timer
        Timer timer = new Timer();

        //int reloadTime = 1000;
        //timer schema
        //timer.schedule(task1, new Date(), reloadTime);
        thread.start();

        UpdateOrders updateOrrders = new UpdateOrders();
        updateOrrders.updateOrders();

        BittrexProtocall bp = new BittrexProtocall();

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

        ConsoleColor.out(bp.getBalances());
        balanceSaver.balance();

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
        try {
            MainSaveConstroller mainSaveConstroller = new MainSaveConstroller();
        } catch (Exception ex) {
            System.err.println(ex);
            System.exit(0);
        }

        new Installer();

        //run de versieCheck
        //versieCheck();
        //kijk of er nieuwe markt of exchange in de lijst moet staan
        //doe dit om de versie check de vergelijken met jou eigen versie die in het systeem is opgelagen
    }

    /**
     * VersieChcek methoden is om na te gaan of het database nog up to data is
     */
    private static void versieCheck() {

        //kijk of versieCheck.txt bestaat
        String naamVersieCheck = "versieCheck.txt";
        String versieCheckLocal;
        File bestandCheck = new File("config/versieCheck.txt");

        //als het bestand niet bestaat
        if (!bestandCheck.exists()) {

            //run database updater
            dataBaseUpdater.databaseUpdater();

            //run de methoden op de nieuwe versie check op te slaan
            versieCheckUpdate();

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

    }

    /**
     * Deze methoden slaat de nieuwe versieCheck op
     */
    private static void versieCheckUpdate() {

    }
}
