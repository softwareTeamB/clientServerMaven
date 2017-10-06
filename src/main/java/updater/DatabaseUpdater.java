package updater;

import JSON.JSONArray;
import JSON.JSONObject;
import global.Config;
import http.Http;
import global.Mysql;
import java.sql.SQLException;

/**
 * Deze klasse is voor alles wat te maken heeft met de database
 *
 * @author michel
 */
public class DatabaseUpdater {

    //maak klasse aan
    Http http = new Http();
    Config config = new Config();
    Mysql mysql = new Mysql();

    public void databaseUpdater() {

        //updater
        exchange();
        marktNaam();
        marktLijst();
    }

    /**
     * Exchange naam checker
     */
    private void exchange() {
        //url
        String url = config.getServerUrl() + "/api/getExchange";

        //maak van alle nieuwe data een JSONARRAY
        String httpReponse = http.GetHttp(url);

        //kijk of de methoden false is
        if ("false".equals(httpReponse)) {

            //error melding
            System.err.println("Er is een error in databaesUpdater. Hierdoor kan de database niet helemaal geupdate worden");

            //als het false is stop dan de methoden
            return;
        }

        JSONArray array = new JSONArray(httpReponse);

        for (int i = 0; i < array.length(); i++) {

            JSONObject arrayObject = array.getJSONObject(i);
            String exchange = arrayObject.getString("handelsplaatsNaam");
            int idExchangeNummer = arrayObject.getInt("idHandelsplaats");

            //kijk of de exchange in de lijst staat
            String sqlCount = "SELECT COUNT(idExchangeLijst) AS total FROM exchangeLijst WHERE handelsplaats='" + exchange + "'";
            int count;
            try {
                count = mysql.mysqlCount(sqlCount);
            } catch (Exception ex) {
                System.err.println("Er is een error op getreden bij databaseUpdater. Error is: " + ex);
                break;
            }

            // als het 0 is
            if (count == 0) {
                String insertSql = "INSERT INTO exchangeLijst (idExchangeLijst, handelsplaats)"
                        + " VALUES(" + idExchangeNummer + ", '" + exchange + "')";
                try {
                    mysql.mysqlExecute(insertSql);
                } catch (SQLException ex) {
                    System.err.println("Er is een error op getreden bij databaseUpdater. Error is: " + ex);
                    return;
                }

            }
        }

        System.out.println("Alle exchange zijn geupdate of waren al up to data");
    }

    /**
     * Methoden om de kijken of de markt naam in de lijst staat
     */
    private void marktNaam() {
        //url
        String url = config.getServerUrl() + "/api/getMarktNaam";

        //maak van alle nieuwe data een JSONARRAY
        String httpReponse = http.GetHttp(url);

        //kijk of de methoden false is
        if ("false".equals(httpReponse)) {

            //error melding
            System.err.println("Er is een error in databaesUpdater. Hierdoor kan de database niet helemaal geupdate worden");

            //als het false is stop dan de methoden
            return;
        }

        JSONArray array = new JSONArray(httpReponse);

        //loop door de data heen
        for (int i = 0; i < array.length(); i++) {

            JSONObject arrayObject = array.getJSONObject(i);

            //marktnaamDb
            String marktNaamDb = arrayObject.getString("marktnaamDb");

            //als bji of count
            String countSql = "SELECT COUNT(marktnaamDb) AS total FROM marktnaam WHERE marktnaamDb='" + marktNaamDb + "'";
            int count;
            try {
                count = mysql.mysqlCount(countSql);
            } catch (Exception ex) {
                System.err.println("Er is een error op getreden bij databaseUpdater. Error is: " + ex);
                break;
            }

            //als count  is draait de code anders doe niks
            if (count == 0) {

                //voeg de coin toe in de lijst
                String baseCoin = arrayObject.getString("baseCoin");
                String marktCoin = arrayObject.getString("marktCurrency");

                //isnsert stament
                String insertSql = "INSERT INTO marktnaam(idMarktNaam, MarktnaamDb, baseCoin, MarktCurrency)"
                        + " values (" + arrayObject.getInt("idMarktNaam") + ", '" + marktNaamDb + "', '"
                        + baseCoin + "' ,'" + marktCoin + "')";
                try {
                    mysql.mysqlExecute(insertSql);
                } catch (SQLException ex) {
                    System.err.println("Er is een error op getreden bij databaseUpdater. Error is: " + ex);
                    return;
                }
            }
        }
        System.out.println("Alle marktnaam zijn geupdate of waren al up to data");
    }

    /**
     * Update de lijsten
     */
    private void marktLijst() {

        //url
        String url = config.getServerUrl() + "/api/getMarktLijst";

        //maak van alle nieuwe data een JSONARRAY
        String httpReponse = http.GetHttp(url);

        //kijk of de methoden false is
        if ("false".equals(httpReponse)) {

            //error melding
            System.err.println("Er is een error in databaesUpdater. Hierdoor kan de database niet helemaal geupdate worden");

            //als het false is stop dan de methoden
            return;
        }

        //maak er een JSONArray van
        JSONArray array = new JSONArray(httpReponse);

        for (int i = 0; i < array.length(); i++) {

            //jsonobject
            JSONObject countObject = array.getJSONObject(i);

            String sqlSelect = "SELECT COUNT(*) AS total FROM marktlijstvolv1"
                    +" WHERE handelsplaatsNaam='" + countObject.getString("handelsplaatsNaam") + "' AND naamMarkt='" + countObject.getString("naamMarkt")+"';";
            try {
                //krijg het exchange nummer
                int count = mysql.mysqlCount(sqlSelect);

                //kijk de markt bekend is
                if (count == 0) {

                    //Krijg het exchange nummer uit het database
                    String sqlStament2 = "SELECT idExchangeLijst AS nummer FROM exchangeLijst WHERE handelsplaats='" + countObject.getString("handelsplaatsNaam") + "';";
                    int exchangeNummer = mysql.mysqlExchangeNummer(sqlStament2);

                    //krijg markt nummer
                    String sqlStament3 = "SELECT idMarktNaam AS nummer FROM clientproject.marktNaam WHERE marktnaamDb = '" + countObject.getString("marktnaamDb") + "';";
                    int idMarktNaam = mysql.mysqlExchangeNummer(sqlStament3);

                    //insert stament
                    String sqlInsert = "INSERT INTO marktlijsten (naamHandelsplaats, idMarktnaam, idHandelsplaats)"
                            + " values ('" + countObject.getString("naamMarkt") + "', " + idMarktNaam + ", "
                            + exchangeNummer + ")";
                    System.out.println(sqlInsert);
                    //mysql
                    mysql.mysqlExecute(sqlInsert);
                }
            } catch (Exception ex) {
                System.err.println("Er is een error op getreden bij databaseUpdater. Error is: " + ex);
                return;
            }

        }
        
        //print de melding dat overal door heen is geloopt
        System.out.println("Alle marktLijsten zijn geupdate of waren al up to data");
    }

}
