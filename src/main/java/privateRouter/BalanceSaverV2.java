package privateRouter;

import JSON.JSONArray;
import JSON.JSONObject;
import frameWork.ArrayListDriver;
import global.ConsoleColor;
import global.ErrorMessageResponse;
import global.GetCointags;
import http.Http;
import java.sql.SQLException;
import java.util.ArrayList; 
import mysql.Mysql;
import privateRouter.packageApi.BittrexProtocall;
import privateRouter.packageApi.CexIoProtocall;

/**
 * BalanceSaver v2
 *
 * @author michel
 */
public class BalanceSaverV2 {

    //coinTagLijst
    private ArrayList coinTagLijst;

    //exchange id nummer
    private int ID_BITTREX;
    private int cexIo;
    private int poloniexID;

    //array
    private Object[] cexIoArray;
    //array
    //private String[] cexIoArray;
    Mysql mysql = new Mysql();
    CexIoProtocall cexIoProtocall = new CexIoProtocall();
    BittrexProtocall bittrexPrototcall = new BittrexProtocall();
    GetCointags getCointags = new GetCointags();
    ArrayListDriver arrayListDriver = new ArrayListDriver();
    Http http = new Http();

    /**
     * Constructor
     */
    public BalanceSaverV2() {

        int tempBittrex = 0;

        try {
            ID_BITTREX = mysql.mysqlExchangeNummerV2("bittrex");
        } catch (Exception ex) {
            ConsoleColor.err("Er is een error bij balanceSaver in de construcotr. Dit is de error: "
                    + ex + "\n De software wordt afgesloten.");

            System.exit(0);
        }

        try {
            //poloniex nummer
            poloniexID = mysql.mysqlExchangeNummerV2("poloniex");
        } catch (Exception ex) {
            ConsoleColor.err("Er is een error bij balanceSaver in de construcotr. Dit is de error: "
                    + ex + "\n De software wordt afgesloten.");

            System.exit(0);
        }

        //roep de cexIoConstructor op
        cexIoConstructor();

        //terminal bericht
        ConsoleColor.out("Constructor balance saver is geladen.");
    }

    /**
     * Methoden die de balance opvraagd
     */
    public void balance() {

        //roep de methodens op
        balanceBittrex();
        balancePoloniex();
        //balanceCexIo();
    }

    /**
     * Methoden die de bittrex balance opslaat
     */
    public void balanceBittrex() {

        //vraag alle balance data op
        String bittrexReponse = bittrexPrototcall.getBalances();

        //maak er een jsonObject van
        JSONObject bittrexReponseObject = new JSONObject(bittrexReponse);

        //kijk of de reponse geldig is
        if (!ErrorMessageResponse.bittrexCheck(bittrexReponseObject)) {
            //zorg er voor dat het porgramma niet meer verder gaat
            return;
        };

        //maak een JSONArray
        JSONArray array = bittrexReponseObject.getJSONArray("result");

        //for loop
        for (int i = 0; i < array.length(); i++) {

            //maak er een object van
            JSONObject object = array.getJSONObject(i);

            String cointag = object.getString("Currency");
            double balance = object.getDouble("Balance");
            double available = object.getDouble("Available");
            double pending = object.getDouble("Pending");

            try {
                //roep de methoden op die er voor zorgt dat alles goed verwerkt wordt
                balanceChecker(ID_BITTREX, cointag, balance, pending, available);
            } catch (Exception ex) {
                ConsoleColor.err("" + ex);
            }
        }
    }

    /**
     * Poloniex
     */
    public void balancePoloniex() {

        //vraag de balance op
        String url = clientserver.ClientServer.clientUrlServer + "/poloniex/getBalance";
        String httpReponse;
        try {
            httpReponse = http.getHttpBrowser(url);

            //maak er een jsonobject van
            JSONObject object = new JSONObject(httpReponse);

            //loop door alle keys heen
            for (int i = 0; i < object.names().length(); i++) {

                //vraag keyNaam op
                String keyNaam = object.names().getString(i);

                //vraag het object aan
                JSONObject object2 = object.getJSONObject(keyNaam);

                //vraag de balance op
                double available = object2.getDouble("available");
                double orders = object2.getDouble("onOrders");
                double balance = orders + available;

                //roep de balanceChecker op
                balanceChecker(poloniexID, keyNaam, balance, orders, available);
            }
        } catch (Exception ex) {

            //ConsoleColor error bericht
            ConsoleColor.err(ex);

            //return object
            return;
        }
        ConsoleColor.out(httpReponse);

    }

    /**
     * Methoden die cex.io balance op slaat
     */
    public void balanceCexIo() {

        String httpBalanceReponse = cexIoProtocall.balance();
        JSONObject objectReponse = new JSONObject(httpBalanceReponse);
        ConsoleColor.out(objectReponse);

        //for loop
        for (int i = 0; i < cexIoArray.length; i++) {
            String cointag = cexIoArray[i].toString();

            //haal het eerst volgende object op
            JSONObject specifiekeObject = objectReponse.getJSONObject(cointag);

            ConsoleColor.warn(specifiekeObject);

            //double default on -1
            double available = -1;
            double orders = -1;
            double balance = -1;

            //als available en orders bestaat
            if (specifiekeObject.has("available") && specifiekeObject.has("orders")) {

                //sla de balance waarde op
                available = specifiekeObject.getDouble("available");
                orders = specifiekeObject.getDouble("orders");
                balance = available + orders;
            } else {

                //als available belemd is
                if (specifiekeObject.has("available")) {

                    //sla de balance waarde op
                    available = specifiekeObject.getDouble("available");
                }

                //als orders bekend is
                if (specifiekeObject.has("orders")) {

                    //vraag orders op
                    orders = specifiekeObject.getDouble("orders");
                }
            }

            try {
                //roep de methoden op die er voor zorgt dat alles goed verwerkt wordt
                balanceChecker(cexIo, cointag, balance, orders, available);
            } catch (Exception ex) {
                ConsoleColor.err("" + ex);
            }
        }
    }

    /**
     * Methoden die na kijkt of de balance in mysql en of die geupdate moet worden
     *
     * @param exchangeID id van de exchange
     * @param cointag cointag naam
     * @param balance balance
     * @param pending hoeveel er in order staat
     * @param available hoeveel er beschikbaar is
     * @throws java.lang.Exception als er in het proces een error optreed
     */
    public void balanceChecker(int exchangeID, String cointag, double balance, double pending, double available)
            throws Exception {

        //kijk of de balance het zelfde is gebleven
        String sqlStament = "SELECT COUNT(*) AS total FROM balance "
                + "WHERE idExchangeLijst=" + exchangeID
                + " AND cointag='" + cointag + "' AND balance=" + balance + " AND pending= "
                + pending + " AND available=" + available + ";";
        int count = mysql.mysqlCount(sqlStament);

        //kijk het het zelfde is
        if (count == 0) {

            //kijk of de pm keys er in staan
            String sqlStament2 = "SELECT COUNT(*) AS total FROM balance "
                    + "WHERE idExchangeLijst=" + exchangeID
                    + " AND cointag='" + cointag + "'";
            int count2 = mysql.mysqlCount(sqlStament2);

            //kijk of de balance geupdate moet worden
            if (count2 == 0) {

                //terminal bericht
                ConsoleColor.out("De balance moet toegevoegd worden van de coin: " + cointag);

                //roep de insert methoden op
                insertBalance(exchangeID, cointag, balance, pending, available);
            } else if (count == 1) {

                //termianl bericht
                ConsoleColor.out("Balance data moet geupdate worden van coin: " + cointag + ".");

                //roep de methoden op om de balance data te updaten
                balanceUpdater(exchangeID, cointag, balance, pending, available);
            }
        }
    }

    /**
     * Methoden die de balance data update
     *
     * @param exchangeID id van de exchange
     * @param cointag cointag naam
     * @param balance balance
     * @param pending hoeveel er in order staat
     * @param available hoeveel er beschikbaar is
     * @throws SQLException sql error afvangen
     */
    private void balanceUpdater(int exchangeID, String cointag, double balance, double pending, double available) throws SQLException {

        //sql updater
        String sqlUpdater = "UPDATE balance SET "
                + "balance=" + balance + ", pending=" + pending + ", available=" + available
                + " WHERE idExchangeLijst=" + exchangeID + " AND cointag='" + cointag + "'";

        mysql.mysqlExecute(sqlUpdater);
        ConsoleColor.out("De balance van cointag " + cointag + " is toegevoegd.");
    }

    /**
     * Methoden die de coin balance toevoegd in de database
     *
     * @param exchangeID id van de exchange
     * @param cointag cointag naam
     * @param balance balance
     * @param pending hoeveel er in order staat
     * @param available hoeveel er beschikbaar is
     * @throws SQLException sql error afvangen
     */
    private void insertBalance(int exchangeID, String cointag, double balance, double pending, double available) throws SQLException {

        //voeg de coin toe
        String sqlInsert = "INSERT INTO balance (idExchangeLijst, cointag, balance, available, pending) "
                + "VALUES(" + exchangeID + ", '" + cointag + "', " + balance + ", " + available + ", " + pending + ")";

        mysql.mysqlExecute(sqlInsert);

        ConsoleColor.out("De balance is toegevoegd van cointag: " + cointag);
    }

    /**
     * cexIoConstrucot
     */
    private void cexIoConstructor() {

        //tijdelijke arrayList
        ArrayList tempArrayLijst = new ArrayList();

        //voeg standaart toe die niet in coinmarketcap staan
        tempArrayLijst.add("EUR");
        tempArrayLijst.add("USD");
        tempArrayLijst.add("GHS");
        //arrayLijst
        coinTagLijst = getCointags.getCointags();

        //vraag de balance op
        String balanceString = cexIoProtocall.balance();

        //maak er een jsonObject van
        JSONObject object = new JSONObject(balanceString);

        //loop
        for (int i = 0; i < coinTagLijst.size(); i++) {
            String cointag = coinTagLijst.get(i).toString();

            //kijk of de key bestaat
            if (object.has(cointag)) {

                //voeg de coin in de tempArrayLijst
                tempArrayLijst.add(cointag);
            }
        }

        ConsoleColor.warn(tempArrayLijst);

        //kijk hoe groot de tempArrayLijst is
        int arrayLijstGrote = tempArrayLijst.size();

        ConsoleColor.warn(arrayLijstGrote);

        //cointag arrayLijst
        cexIoArray = new Object[arrayLijstGrote];

        //ze de tijdelijke tempArrayLijst om in een array
        cexIoArray = clientserver.ClientServer.arrayListDriver.makeArray(tempArrayLijst);
    }
}
