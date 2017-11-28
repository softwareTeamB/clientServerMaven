package clientserver;

import JSON.JSONException;
import JSON.JSONObject;
import frameWork.ArrayListDriver;
import global.ConsoleColor;
import global.FileSystem;
import http.Http;
import http.HttpPost;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mysql.Mysql;
import privateRouter.BalanceSaverV2;
import privateRouter.DataGetter;
import privateRouter.Deposit;
import privateRouter.Poloniex;
import privateRouter.packageApi.BittrexProtocall;
import terminal.inputf;
import tradeEngine.MainEngine;
import updater.DatabaseUpdater;

/**
 * Dit is het programma wat er voor zorgt dat de client kant up to data kan blijven
 *
 * @author michel
 */
public class ClientServer {

    /**
     * JavaDoc voor de nodejsUrl. Dit is een url om het nodejs systeem met de exchange te communiseren
     */
    public static final String NODE_JS_URL = "http://127.0.0.1:9091";

    private static String url = "127.0.0.1:7090";
    private static String versieCheck = "/versieCheck.txt";

    public static FileSystem fileSystem;
    public static DatabaseUpdater dataBaseUpdater;
    public static ArrayListDriver arrayListDriver;

    public static Http http;
    public static HttpPost httpPost = new HttpPost();

    //maak bittrex private router
    public static BittrexProtocall bittrexProtocall;

    //private routers poloniex
    public static Poloniex poloniex;

    /**
     * Basis url van belangrijke websites
     */
    public static String cexIo = "https://cex.io/api";
    public static String coinmarketcapUrl = "https://api.coinmarketcap.com/v1";
    public static String clientUrlServer = "http://127.0.0.1:9091";

    //een jsonObject van een lijst met exchange id nummers
    public static JSONObject exchangeIdJSONObject = new JSONObject();
    public static JSONObject exchangeFeeJSONObject = new JSONObject();
    public static JSONObject exchangeVerbindingsTeken = new JSONObject();

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

        //mysqlCheck
        mysqlExchangeCheck();

        //maak de poloniex private router aan
        poloniex = new Poloniex();

        //maak bittrex private router
        bittrexProtocall = new BittrexProtocall();

        /*
         * Methoden werkt
        try {
            poloniex.setServerBalance();
        } catch (Exception ex) {
            Logger.getLogger(ClientServer.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        /**
         * Methoden werkt try { poloniex.setOrder("buy", "USDT", "XRP", 0.1, 100); } catch (Exception ex) {
         * Logger.getLogger(ClientServer.class.getName()).log(Level.SEVERE, null, ex); } / /* Methoden werkt
         * poloniex.cancelOrder("64081504477","USDT" ,"XRP");
         */
        //http object
        http = new Http();

        //maak alle objecten aan
        fileSystem = new FileSystem();
        DatabaseUpdater dataBaseUpdater = new DatabaseUpdater();
        arrayListDriver = new ArrayListDriver();

        Deposit deposit = new Deposit();
        deposit.mainDeposit();

        //blancee engine
        BalanceSaverV2 bV2 = new BalanceSaverV2();
        bV2.balance();

        //order engine
        MainEngine mainEngine = new MainEngine();
        try {
            mainEngine.loadOrderSettings("bla", 2);
        } catch (SQLException ex) {
            Logger.getLogger(ClientServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        DataGetter dataGetter = new DataGetter();
        try {
            dataGetter.getBalance("btc", "ltc", "bittrex");
        } catch (SQLException ex) {
            ConsoleColor.err("" + ex);
        } catch (Exception ex) {
            ConsoleColor.err("" + ex);
        }
        
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
    /**
     * methoden die na kijkt op handelsplaats goed werkt
     */
    private static void mysqlExchangeCheck() {

        //mysql object
        Mysql mysql = new Mysql();

        //string array voor exchangeLijst
        String[] exchangeNaamArray = {"poloniex", "bittrex"};
        String[] verbindingsTeken = {"_", "-"};
        String[] exchangeFeeArray = {"0.002", "0.0025"};

        //loop door de exchangeNaam heen
        for (int i = 0; i < exchangeNaamArray.length; i++) {

            //exchange naam
            String exchangeNaam = exchangeNaamArray[i];

            //count string
            String countSql = "SELECT COUNT(*) AS total FROM exchangeLijst "
                    + "WHERE handelsplaats ='" + exchangeNaam + "' ";
            //vraag kijk of de markt er in staat
            int count;
            try {
                //count stament
                count = mysql.mysqlCount(countSql);
            } catch (Exception ex) {
                ConsoleColor.err(ex);

                //fatale error sluit het systeem af
                System.exit(0);

                //dit is gedaan omdat java gewoon een domme taal is
                continue;
            }

            //stament als count 0 is anders naar de else stament
            if (count == 0) {

                //voeg de exchange toe en de verbindings teken
                String sqlInsert = "INSERT INTO exchangeLijst (handelsplaats, verbindingsTeken, exchangeFee) "
                        + "VALUES('" + exchangeNaam + "', '" + verbindingsTeken[i] + "', '" + exchangeFeeArray[i] + "')";

                //voer het stament uit
                try {
                    mysql.mysqlExecute(sqlInsert);
                } catch (SQLException ex) {
                    ConsoleColor.err(ex);

                    //fatale error sluit het systeem af
                    System.exit(0);
                }

            } else {

                //kijk of de exchangeNaam bekend is
                String countSql2 = "SELECT COUNT(*) AS total FROM exchangeLijst "
                        + "WHERE handelsplaats ='" + exchangeNaam + "' "
                        + "AND verbindingsTeken='" + verbindingsTeken[i] + "' "
                        + "AND exchangeFee='" + exchangeFeeArray[i] + "'";

                //vraag kijk of de markt er in staat
                int count2;
                try {
                    //count stament
                    count2 = mysql.mysqlCount(countSql2);
                } catch (Exception ex) {
                    ConsoleColor.err(ex);

                    //fatale error sluit het systeem af
                    System.exit(0);
                    continue;
                }

                //als het 1 is doe niks als het 0 is run het update stament
                if (count2 == 0) {

                    //update sql stament voor verbindigsTeken
                    String updateSql = "UPDATE exchangeLijst SET verbindingsTeken='" + verbindingsTeken[i] + ""
                            + "', exchangeFee='" + exchangeFeeArray[i] + "' "
                            + "WHERE handelsplaats ='" + exchangeNaam + "'";

                    //voer het stament uit
                    try {
                        mysql.mysqlExecute(updateSql);
                    } catch (SQLException ex) {
                        ConsoleColor.err(ex);

                        //fatale error sluit het systeem af
                        System.exit(0);
                    }

                }

            }

            try {
                //haal het exchange nummer op uit het database
                ResultSet rs = mysql.mysqlSelect("SELECT * FROM exchangeLijst "
                        + "WHERE handelsplaats='" + exchangeNaam + "'");

                while (rs.next()) {

                    //krijg het exchangeId nummer
                    int exchangeId = rs.getInt("idExchangeLijst");

                    //vraag de fee op
                    String exchangeFee = "" + rs.getDouble("exchangeFee");

                    //voeg de exchange to in de JSONobject toe
                    exchangeIdJSONObject.put(exchangeNaam, exchangeId);

                    //voeg de exchangeFee op
                    exchangeFeeJSONObject.put("" + exchangeId, exchangeFee);

                    exchangeVerbindingsTeken.put("" + exchangeId, rs.getString("verbindingsteken"));

                }
            } catch (JSONException | SQLException ex) {
                ConsoleColor.err(ex);

                //sluit de applicatei! Fatale error
                System.exit(0);
            }

            //bericht
            ConsoleColor.out("MysqlExchangeCheck is doorlopen.");
            ConsoleColor.out(exchangeIdJSONObject);
        }
    }
}
