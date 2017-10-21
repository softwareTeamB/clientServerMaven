package privateRouter;

import JSON.JSONArray;
import JSON.JSONObject;
import global.ConsoleColor;
import global.ErrorMessageResponse;
import mysql.Mysql;
import java.sql.SQLException;
import java.util.Iterator;
import privateRouter.packageApi.BittrexProtocall;
import privateRouter.packageApi.CexIoProtocall;

/**
 * klasse die voor balanceSaver werkt
 *
 * @author michel
 */
public class BalanceSaver {

    //exchange id nummer
    private final int ID_BITTREX;
    private int cexIo;

    Mysql mysql = new Mysql();
    CexIoProtocall cexIoProtocall = new CexIoProtocall();
    BittrexProtocall bittrexPrototcall = new BittrexProtocall();

    /**
     * Constructor
     */
    public BalanceSaver() {

        int tempBittrex = 0;

        try {
            tempBittrex = mysql.mysqlExchangeNummerV2("bittrex");
            cexIo = mysql.mysqlExchangeNummerV2("cexIo");
        } catch (Exception ex) {
            ConsoleColor.err("Er is een error bij balanceSaver in de construcotr. Dit is de error: "
                    + ex + "\n De software wordt afgesloten.");

            System.exit(0);
        }

        this.ID_BITTREX = tempBittrex;

        //terminal bericht
        ConsoleColor.out("Constructor balance saver is geladen.");
    }

    /**
     * Methoden die de balance opvraagd
     */
    public void balance() {

        //roep de methodens op
        balanceBittrex();
        balaneCexIo();
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
        if (!ErrorMessageResponse.bittrex(bittrexReponseObject)) {
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
     * Methodne om de balance van cex.io op te slaan
     */
    public void balaneCexIo() {

        //vraag de balance op
        String balanceString = cexIoProtocall.balance();

        ConsoleColor.out(balanceString);

        //maak er een object van 
        JSONObject object = new JSONObject(balanceString);

        //zorg dat er een key kompt
        Iterator<String> keys = object.keys();
        if (keys.hasNext()) {

            ConsoleColor.out(keys.next());

            //variable
            String cointag = keys.next();

            //object1
            JSONObject object1 = object.getJSONObject(cointag);

            //balance waardes
            double available = object1.getDouble("available");
            double orders = object1.getDouble("orders");
            double balance = available + orders;

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
                + " WHERE idExchangeLijst" + exchangeID + " AND cointag='" + cointag + "'";

        mysql.mysqlExecute(sqlUpdater);
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
}
