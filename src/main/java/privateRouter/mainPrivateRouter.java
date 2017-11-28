package privateRouter;

import JSON.JSONObject;
import global.ConsoleColor;
import java.sql.SQLException;
import mysql.Mysql;

/**
 *
 * @author michel
 */
public class mainPrivateRouter {

    /**
     * Maak object aan
     */
    public Mysql mysql = new Mysql();

    //private JSONObject
    public JSONObject balanceDB = new JSONObject();

    /**
     * Methoden om de trade op slaan
     *
     * @param uuID order nummer
     * @param exchangeId exchange d
     * @param prijs prijs
     * @param hoeveelheid hoeveelheid
     * @param baseCoin de basis coin
     * @param marktCoin de markt coin
     * @param type vraag het type op
     * @throws java.sql.SQLException sqlException error
     */
    public void insertOrder(String uuID, double exchangeId, double prijs, double hoeveelheid,
            String baseCoin, String marktCoin, String type) throws SQLException {

        //insert stament
        String insertStament = "INSERT INTO openOrders "
                + "(exchangeId, uuid, baseCoin, marktCoin, type, prijs, hoeveelheid)"
                + "VALUES (" + exchangeId + ", " + uuID + ", '" + baseCoin + "', '" + marktCoin + "', '" + type + "', "
                + prijs + ", " + hoeveelheid + ")";
        mysql.mysqlExecute(insertStament);
    }

    /**
     * Balance updaten
     *
     * @param currency cointag
     * @param exchangeID exchange nummer
     * @param balance balance
     * @param available hoeveelheid balance dat beschikbaar is
     * @param pending hoeveel er binnenkort beschikbaar is
     */
    public void updateBalance(String currency, int exchangeID, double balance,
            double available, double pending) {

    }

    /**
     * Deze methoden kijkt na of de key in het JSONObject staat
     *
     * @param key key naam
     * @param memoryDBNaam memory db naam
     * @return
     */
    public boolean keyCheck(String key, String memoryDBNaam) {

        //kijk of de key bestaat in de juiste memoryDB
        return balanceDB.has(key);

    }

    /**
     * ==================== Methodens voor het balance het system ==================== *
     */
    /**
     * Methoden die na kijkt of de balance in mysql en of die geupdate moet worden
     *
     * @param exchangeID id van de exchange
     * @param cointag cointag naam
     * @param balance balance
     * @param pending hoeveel er in order staat
     * @param available hoeveel er beschikbaar is
     * @return of er een verandere was. Als er een verandere is moet het true zijn
     * @throws java.lang.Exception als er in het proces een error optreed
     */
    public boolean balanceChecker(int exchangeID, String cointag, double balance, double available, double pending)
            throws Exception {

        //kijk of de balance het zelfde is gebleven
        String sqlStament = "SELECT COUNT(*) AS total FROM balance "
                + "WHERE idExchangeLijst=" + exchangeID
                + " AND cointag='" + cointag + "' AND balance=" + balance + " AND pending= "
                + pending + " AND available=" + available + ";";
        int count = mysql.mysqlCount(sqlStament);

        //kijk het het zelfde is
        if (count == 0) {

            //zet data in een array
            String[] putArray = new String[3];

            //put balanceDB
            putArray[0] = "" + balance;
            putArray[1] = "" + available;
            putArray[2] = "" + pending;

            //jsonobjct put
            balanceDB.put(cointag, putArray);

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
                
                //return true dat er een verandering is
                return true;
            }
        } else {
            
            //return false omdat er niks is aangepast
            return false;
        }
        
        //return stament moet toegevoegd worden om java dom is
        return false;
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
    private void balanceUpdater(int exchangeID, String cointag, double balance, double available, 
            double pending) throws SQLException {

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
    private void insertBalance(int exchangeID, String cointag, double balance, double available,
            double pending) throws SQLException {

        //voeg de coin toe
        String sqlInsert = "INSERT INTO balance (idExchangeLijst, cointag, balance, available, pending) "
                + "VALUES(" + exchangeID + ", '" + cointag + "', " + balance + ", " + available + ", " + pending + ")";
        mysql.mysqlExecute(sqlInsert);

        //maak een string array
        String balanceArray[] = new String[3];

        //voeg de balance data toe in de array volgens de standaart
        balanceArray[0] = "" + balance;
        balanceArray[1] = "" + available;
        balanceArray[2] = "" + pending;

        //update JSONObject
        balanceDB.put(cointag, balanceArray);

        ConsoleColor.out("De balance is toegevoegd van cointag: " + cointag);
    }

}
