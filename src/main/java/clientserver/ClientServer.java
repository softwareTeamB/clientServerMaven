package clientserver;

import JSON.JSONException;
import JSON.JSONObject;
import frameWork.ArrayListDriver;
import global.ConsoleColor;
import global.FileSystem;
import global.LoadPropFile;
import http.Http;
import http.HttpPost;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import mysql.Mysql;
import mysql.MysqlServer;
import privateRouter.BalanceSaverV2;
import privateRouter.Deposit;
import privateRouter.OrdersGetter;
import privateRouter.Poloniex;
import privateRouter.packageApi.BittrexProtocall;
import terminal.mainTerminal;
import updater.DatabaseUpdater;

/**
 * Dit is het programma wat er voor zorgt dat de client kant up to data kan
 * blijven
 *
 * @author michel
 */
public class ClientServer{

    /**
     * JavaDoc voor de nodejsUrl. Dit is een url om het nodejs systeem met de
     * exchange te communiseren
     */
    public static final String NODE_JS_URL = "http://127.0.0.1:9091";
        
    //mysql
    public static Mysql mysql = new Mysql();
    public static MysqlServer mysqlServer = new MysqlServer();

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

    //klassen voor die belangirjk zijn
    private static GreatMarktNaam greatMarktNaam = new GreatMarktNaam();

    /**
     * Basis url van belangrijke websites
     */
    private static final String bittrexUrl = "https://bittrex.com/api/v1.1";
    public static String cexIo = "https://cex.io/api";
    public static String coinmarketcapUrl = "https://api.coinmarketcap.com/v1";
    public static String clientUrlServer = "http://127.0.0.1:9091";

    //een jsonObject van een lijst met exchange id nummers
    public static JSONObject exchangeIdJSONObject = new JSONObject();
    public static JSONObject exchangeFeeJSONObject = new JSONObject();
    public static JSONObject exchangeVerbindingsTeken = new JSONObject();

    public static Properties config;

    /**
     * De main methoden
     *
     * @param args command-line
     */
    public static void main(String[] args) {

        try {
            config = LoadPropFile.loadPropFile("config");
            ConsoleColor.out(config);
        } catch (IOException ex) {
            ConsoleColor.err(ex);
            System.exit(0);
        }

        //de nieiwe thread
        Thread thread = new Thread() {
            @Override
            public void run() {
                backgroundSystem(args);
            }
        };

        ConsoleColor.out("test");
        thread.start();

        //roep de interface methoden op
        InterfaceMain.interfaceMethoden(args);
    }

    /**
     * Alle methoden die de background van het systeem dienen
     *
     * @param args command-line
     */
    private static void backgroundSystem(String[] args) {

        //boolean 
        //maak het object aan voor de installerV2
        //InstallerV2 iv2 = new InstallerV2();
        //iv2.main();
        // hier wodt de terminal input aangemaakt en opgestart in een apart thread
        mainTerminal i = new mainTerminal();
        Thread thread = new Thread() {
            @Override
            public void run() {
                i.mainKlasse();
            }
        };

        ConsoleColor.out("test");
        thread.start();

        //mysqlCheck
        mysqlExchangeCheck();
        ConsoleColor.out("test");
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
         * Methoden werkt try { poloniex.setOrder("buy", "USDT", "XRP", 0.1,
         * 100); } catch (Exception ex) {
         * Logger.getLogger(ClientServer.class.getName()).log(Level.SEVERE,
         * null, ex); } / /* Methoden werkt
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

        OrdersGetter og = new OrdersGetter();
        og.updateOrders();

        //blancee engine
        BalanceSaverV2 bV2 = new BalanceSaverV2();
        bV2.balance();

        //order engine
        /*MainEngine mainEngine = new MainEngine();
        try {
            mainEngine.loadOrderSettings("bla", 2);
        } catch (SQLException ex) {
            Logger.getLogger(ClientServer.class.getName()).log(Level.SEVERE, null, ex);
        }*/
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

    /**
     * Methoden die de marktnaam return
     *
     * @param baseCoin basis coin
     * @param marktCoin marktNaamCoin
     * @param exchangeIdString exchangeId in
     * @return marktnaam
     */
    public static String getMarktNaam(String baseCoin, String marktCoin,
            String exchangeIdString) {

        //roep de methoden op die de marktnaam in elkaar zet
        return greatMarktNaam.getMarktNaam(baseCoin, marktCoin, exchangeIdString);
    }

    /**
     * Getter voor de bittrex url
     *
     * @return bittrex url
     */
    public static String getBittrexUrl() {

        //return variable
        return bittrexUrl;
    }
}
