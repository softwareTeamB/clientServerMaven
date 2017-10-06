package marktData;

import JSON.JSONArray;
import JSON.JSONObject;
import global.Mysql;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Deze methoden is om de controller welke data er op geslagen / geupdate moet
 * worden
 *
 * @author michel
 */
public class MainSaveConstroller {

    Mysql mysql = new Mysql();

    //array met de data
    private String[] exchangeArray;

    //JSONArray waarvan de key de exchange is en 
    //maak alle exchange objecten aan
    Bitstamp bitstamp;
    //Bittrex bittrex;

    /**
     * Constrcutor
     *
     * @throws java.lang.Exception exception error
     */
    public MainSaveConstroller() throws Exception {
        //roep de methoden op die de array vult
        fillExchangeArray();

        //loop door de fillExchangeArray heen
        for (int i = 0; i < exchangeArray.length; i++) {

            //vul exchange met de variable
            String exchange = exchangeArray[i];
            
            //if staments
            if ("bitstamp".equals(exchange)) {

                //roep de methoden starBistamp op en geeft de return mee van maakMarktLijst
                startBitstamp(maakMarktLijst(exchange));
            }
        }

        System.out.println("MainSaveController is geladen");
    }

    /**
     * Methoden die door de constructor word gebruikt om de exchangeArray de
     * vullen
     *
     * @throws Exception error exception
     */
    private void fillExchangeArray() throws Exception, SQLException {

        //arary nummer
        int index = 0;

        //kijk hoe groot de array moet worden
        String sqlCount = "SELECT count(handelsplaatsNaam) AS total FROM marktlijstvolv1 "
                + "WHERE saveMarktData = 'true' GROUP BY handelsplaatsNaam";
        int arraySize = mysql.mysqlCount(sqlCount);

        //maak de array aan
        exchangeArray = new String[arraySize];

        //sqlSelect
        String sqlSelect = "SELECT handelsplaatsNaam AS total FROM marktlijstvolv1 "
                + "WHERE saveMarktData = 'true' GROUP BY handelsplaatsNaam;";

        //krijg het resultaat uit het database
        ResultSet rs = mysql.mysqlSelect(sqlSelect);

        while (rs.next()) {
            exchangeArray[index] = rs.getString("total");
            index++;
        }
    }

    /**
     * Return de lijst van markten op de exchange niet opgeslagen moet worden
     *
     * @param exchangeNaam exchange naam
     * @return de markt array
     */
    private JSONObject maakMarktLijst(String exchangeNaam) throws SQLException {

        //maak een object aan
        JSONObject marktNaamObject = new JSONObject();
        
        //maak een array
        JSONArray arrayMarken = new JSONArray();

        //kijk of er iets op true staat in de lijst
        String selectSql = "SELECT marktnaamDB, naamMarkt "
                + "FROM marktactief "
                + "WHERE saveMarktData = 'true' "
                + "AND handelsplaatsNaam='" + exchangeNaam + "';";

        //vraag het op uit het database
        ResultSet rs = mysql.mysqlSelect(selectSql);

        //loop door de response heen
        while (rs.next()) {

            //get marktnaamDB
            String marktNaamDB = rs.getString("marktnaamDB");

            //get marktnaam
            String marktNaam = rs.getString("naamMarkt");

            //voeg het toe aan een JSONObject
            marktNaamObject.put(marktNaam, marktNaamDB);
            
            //voeg marktnaam exchange toe even in een array
            arrayMarken.put(marktNaam);
        }
        
        //voeg array to aan het object
        marktNaamObject.put("marktenBekendeExchange",arrayMarken);
        
        //return de array
        return marktNaamObject;
    }

    /**
     * Methoden die marktLijsten
     *
     * @param marktLijsten
     */
    private void startBitstamp(JSONObject marktLijsten) {

        //run cconstructor
        bitstamp = new Bitstamp(marktLijsten);

    }
    
    private void startBittrex(JSONObject marktLijsten){
        
    }
    
}
