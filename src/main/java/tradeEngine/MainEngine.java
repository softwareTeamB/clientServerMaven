package tradeEngine;

import JSON.JSONArray;
import JSON.JSONObject;
import global.ConsoleColor;
import mysql.Mysql;
import java.sql.ResultSet;
import java.sql.SQLException;
import clientserver.ClientServer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Deze methoden regelt alle trade die gedaan worden
 *
 * @author michel
 */
public class MainEngine {

    private final double MIN_BTC = 0.0002;

    //JSONObject met de balance data
    JSONObject balanceData = new JSONObject();

    //maak mysql object aan
    Mysql mysql = new Mysql();

    /**
     * Deze methoden haalt alle mogelijk order op en zet het in de MainEnigne
     *
     * @param currency cointag
     * @param exchangeId exchnage nummer
     * @throws java.sql.SQLException sql error exceptie
     */
    public void loadOrderSettings(String currency, int exchangeId) throws SQLException {

        //vraag alle balance data op
        String balanceDataSql = "SELECT * FROM vieuwBalance WHERE exchangeId=" + exchangeId;
        ResultSet rs = mysql.mysqlSelect(balanceDataSql);

        //zet de data in een jsonobject
        while (rs.next()) {
            String cointag = rs.getString("cointag");

            //vraag de balance op uit het object rs
            double balance = rs.getDouble("balance");
            double available = rs.getDouble("available");
            double pending = rs.getDouble("pending");

            //maak een object2 aan zodat je daar balance data in kan zetten
            JSONArray object2 = new JSONArray();

            //zet balance avaialable pending in een double array
            object2.put(balance);
            object2.put(available);
            object2.put(pending);

            //voeg de array toe in het jsonObject. De key is de cointag
            balanceData.put(cointag, object2);
        }

        //vraag alle order settings op
        String getOrderSettingBuy = "SELECT * FROM vieuwopenordersettingbuy WHERE exchangeId=" + exchangeId;
        ConsoleColor.out(getOrderSettingBuy);
        ResultSet rs3 = mysql.mysqlSelect(getOrderSettingBuy);
        while (rs3.next()) {

            //vraag de cointags op
            String baseCoin = rs3.getString("baseCoin");
            String marktCoin = rs3.getString("marktCoin");

            //double max hold
            double maxHold = rs3.getDouble("maxHold");

            //vraag de max prijs op
            double maxPrijs = rs3.getDouble("maxPrijs");

            //bereken de beste prijs
            double prijs = calcPrijsBuy(maxPrijs);

            //kijk of er buy room is
            boolean tradeRoom = buyRoom(balanceData, maxHold, marktCoin);

            if (!tradeRoom) {

                //break de while loop
                continue;
            }

            //krijg de prijs met fee
            double prijsWithFee = calcPrijsWithFees(exchangeId, prijs);

            ConsoleColor.warn(prijs);

            //bereken de buy ruimte
            double buyRoom = calcBuyRoom(balanceData, maxHold, prijsWithFee, baseCoin, marktCoin);

            //kijk of de buy room grote is dat MIN_BUY
            if ((buyRoom * prijs) < MIN_BTC) {

                //de buy room is te laag
                continue;
            }

            //bereken hoeveel alles bij elkaar gaat kosten
            //double buyKost = buyRoom * prijs;
            //roep de methoden op die de order plaats en krijgt vervolgens de uuid terug
            String uuid;
            try {
                uuid = setOrder(exchangeId, prijs, buyRoom, baseCoin, marktCoin, "buy");
            } catch (Exception ex) {
                ConsoleColor.err(ex);
                return;
            }

            //kijk het het succesvol is gelukt
            boolean orderSetSuccesVol = succesVol(exchangeId, uuid);
            if (orderSetSuccesVol) {

                //zet de order in het database
                insertUuid(exchangeId, uuid, "buy", baseCoin, marktCoin, prijs, buyRoom);
            }

        }

        //vraag de data op uit de sql stament
        String getOrderSettingSell = "";

    }

    /* ========================== SET ORDER METHODENS ========================== */
    /**
     *
     * @param exchangeId het exchangeId nummer
     * @param prijs de prijs
     * @param ammount de hoeveelheid
     * @param baseCoin basis coin
     * @param marktCoin de marktcoin
     * @param type het order type
     * @return order uuid
     */
    private String setOrder(
            int exchangeId,
            double prijs,
            double ammount,
            String baseCoin,
            String marktCoin,
            String type
    ) throws Exception {

        //String order nummer
        String orderNummer = null;

        //krijg het verbindings teken
        String verbindingsTeken = ClientServer.exchangeVerbindingsTeken.getString("" + exchangeId);

        //if stament bittrex
        if (exchangeId == ClientServer.exchangeIdJSONObject.getInt("bittrex")) {

            //maak de markt naam
            String marktNaam = baseCoin + verbindingsTeken + marktCoin;

            switch (type) {
                case "buy":
                    //bittrex set order stament
                    orderNummer = ClientServer.bittrexProtocall.buyLimitv2(marktNaam, ammount, prijs);
                    break;
                case "sell":
                    //bittrex set order stament
                    orderNummer = ClientServer.bittrexProtocall.sellLimitv2(marktNaam, ammount, prijs);
                    break;
                default:
                    ConsoleColor.warn("Het order type wordt niet herkend. Dit is de order type: " + type);
                    break;
            }
        } else {
            throw new Exception("Exchange is niet bekend!");
        }

        //return orderNummer
        return orderNummer;

    }

    /* ========================== BUY METHODENS ========================== */
    /**
     * Methoden om de beste prijs uit te rekenen voor buy kant
     *
     * @param maxPrijs de maximale prijs
     * @return de beste prijs
     */
    private double calcPrijsBuy(double maxPrijs) {

        ConsoleColor.warn("Bij de methoden calcPrijs moet er nog de beste prijs berekend worden");

        return maxPrijs;
    }

    /**
     * Bereken de buy prijs
     *
     * @param exchangeId het exchnage nummer
     * @param prijs de maximale prijs
     * @return de prijs met fee
     */
    private double calcPrijsWithFees(int exchangeId, double prijs) {

        //vraag uit het JSONObject in clientServer de fee op bij de exchange. De exchange moet een string type zijn
        double exchangeFee = clientserver.ClientServer.exchangeFeeJSONObject.getDouble("" + exchangeId);

        //bereken exchangeFee + 1 zodat je prijs * (1+exchangefee) krijgt
        ConsoleColor.warn(1 + exchangeFee);
        exchangeFee = 1 + exchangeFee;

        //bereken de nieuwe prijs
        return prijs * exchangeFee;
    }

    /**
     * Bereken hoeveel er maximaal gekockt kan worden
     *
     * @param balanceData object met de balance data
     * @param maxBuy de maximale coins die gekockt worden
     * @param prijs de kost prijs per stuk
     * @return
     */
    private double calcBuyRoom(JSONObject balanceData, double maxBuy, double prijs, String baseCoin, String marktCoin) {

        //vraag de available balanec op van baseCoin
        double availableBaseCoin = balanceData.getJSONArray(baseCoin).getDouble(1);
        ConsoleColor.out(availableBaseCoin);

        //vraag de balance data op van marktCoin
        double balanceMarktCoin = balanceData.getJSONArray(marktCoin).getDouble(0);

        //bereken de hoeveelheid wat kan worden aangeschaft
        //doet dit door de beschikbare balance te delen door de prijs
        double totaalBuyAvailable = availableBaseCoin / prijs;

        //kijk of totaalBuyAvailable groter is dan maxBuy
        if (maxBuy < totaalBuyAvailable) {

            //maxBuy is kleinder dan totaalBuyAvailable. Bereken hoeveel maxBuy - balanceMarktCoin
            //update totaalBuyAvailable hoeveel coins er gekockt kan worden
            totaalBuyAvailable = maxBuy - balanceMarktCoin;

        }

        //return de hoeveelheid coins die gekockt moeten worden
        return totaalBuyAvailable;
    }

    /**
     * Methoden die na gaat kijken of er ruimte is om coins bij te kopen
     *
     * @param balanceData de balance data van alle coins
     * @param maxBuy hoeveel er maximaal op de balance mag staan
     * @param marktCoin marktcoin
     * @return of het true of false is
     */
    private boolean buyRoom(JSONObject balanceData, double maxBuy, String marktCoin) {

        //kijk of de marktCoin in de lijst staat
        // als de coin er niet in staat dan is er genoeg ruimte omdat er anders wel balance data in zou staan
        if (balanceData.has(marktCoin)) {

            //haal de JSONArray uit het object
            JSONArray balanceArray = balanceData.getJSONArray(marktCoin);

            //haal de data er uit
            double balance = balanceArray.getDouble(0);
            double available = balanceArray.getDouble(1);
            double pending = balanceArray.getDouble(2);

            //kijk of er balance ruimte is
            if (balance < maxBuy) {

                //console bericht dat er ruimte is
                ConsoleColor.out("Er is ruimte om coins bij te kopen!");

                //return of er ruimte is
                return true;
            } else {

                //return dat er geen ruimte is
                return false;
            }
        } else {

            //return dat er ruimte is om dingen bij te kopen
            return true;
        }
    }

    /* ========================== SELL METHODENS ========================== */
    /**
     * Methoden die na gaat kijken of er ruimte is om coins bij te kopen
     *
     * @param balanceData de balance data van alle coins
     * @param maxHold hoeveel er maximaal op de balance mag staan
     * @param marktCoin marktcoin
     * @return of het true of false is
     */
    private boolean sellRoom(JSONObject balanceData, double minHold, String marktCoin) {

        //kijk of de marktCoin in de lijst staat
        // als de coin er niet in staat dan is er genoeg ruimte omdat er anders wel balance data in zou staan
        if (balanceData.has(marktCoin)) {

            //haal de JSONArray uit het object
            JSONArray balanceArray = balanceData.getJSONArray(marktCoin);

            //haal de data er uit
            double balance = balanceArray.getDouble(0);
            double available = balanceArray.getDouble(1);
            double pending = balanceArray.getDouble(2);

            //kijk of er balance ruimte is
            if (available < minHold) {

                //console bericht dat er ruimte is
                ConsoleColor.out("Er is ruimte om coins te verkopen!");

                //return of er ruimte is
                return true;
            } else {

                //return dat er geen ruimte is
                return false;
            }

        } else {

            //return dat er ruimte is om dingen bij te kopen
            return true;
        }

    }

    /* ========================== ANDERE METHODENS ========================== */
    /**
     * Voeg de order toe in de deatabase van openOrders
     *
     * @param exchangeId exchange ID
     * @param uuid order nummer op de exchange
     * @param type order type
     * @param baseCoin basis coin
     * @param marktCoin markt coin
     * @param prijs de prijs van de coin
     * @param hoeveelheid de hoeveelheid coins
     */
    private void insertUuid(int exchangeId, String uuid, String type, String baseCoin, String marktCoin,
            double prijs, double hoeveelheid) throws SQLException {

        //Insert stament
        String sqlStament = "INSERT INTO openOrders(exchangeId, uuid, type, baseCoin, marktCoin, prijs, hoeveelheid) "
                + "VALUES(" + exchangeId + ", '" + uuid + "', '" + type + "', '" + baseCoin + "', '" + marktCoin + "', "
                + "" + prijs + ", " + hoeveelheid + ")";
        mysql.mysqlExecute(sqlStament);
    }

    /**
     *
     * @param exchangeId exchange db nummer
     * @param uuid uuid van de exchange
     * @return of de order succesvol is werkt
     */
    private boolean succesVol(int exchangeId, String uuid) {

        //maak de string naar een jsonobject
        JSONObject object = new JSONObject(uuid);

        //kijk welke exchange het is
        if (exchangeId == ClientServer.exchangeIdJSONObject.getInt("bittrex")) {

            //haal uit het object of het succesvol is
            boolean succesVol = object.getBoolean("success");

            //if stament
            if (succesVol) {

                //return dat het goed gelukt is
                return true;
            } else {

                //er is een probleem. Print de error uit
                ConsoleColor.err("Er is een probleem bij order te plaatsen. Het probleem doet zich voor bij bittrex. "
                        + "Dit is de error: " + object.getString("message") +"\n"+object);

                //return dat het mislukt is
                return false;
            }

        } else {
            return false;
        }
    }
}
