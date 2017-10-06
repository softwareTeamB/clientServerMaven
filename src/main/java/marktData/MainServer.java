package marktData;

import global.Array;
import global.Mysql;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Main programma om alle data op te slaan
 *
 * @author michel
 */
public abstract class MainServer {

    //private
    private final int ARRAY_SIZE_DEAULT = 0;

    //maak objecten aan
    Array arrayModule = new Array();
    Mysql mysql = new Mysql();

    /**
     * Abstracte methoden die in alle exstends gebruikt moet worden om de markt
     * data op te slaan
     */
    public abstract void getData();
    
    /**
     * methoden die de array vuld met de markt naam
     *
     * @param exchange naam
     * @return de array van de markten die galaden moeten worden
     * @throws java.sql.SQLException als er een probleem is met mysql
     */
    public String[] getReloadMarkt(String exchange) throws SQLException {

        //de array
        String[] loadMarktArray = new String[0];

        String sqlSelect = "SELECT naamMarkt FROM marktactief"
                + " WHERE saveMarktData = 'true' AND handelsplaatsNaam='" + exchange + "';";
        ResultSet rs = mysql.mysqlSelect(sqlSelect);

        //loop door de reponse heen
        while (rs.next()) {

            //haal de markt naam op uit de resultset
            String marktNaam = rs.getString("naamMarkt");

            //voeg de marktNaam toe aan de exchange en maak de exchange dus 1 groter
            return arrayModule.addArray(loadMarktArray, marktNaam);
        }
        
        //als er geen enkele data response is
        return loadMarktArray;
    }
    
    
    /**
     * addMarkt
     *
     * @param 
     * @param markt de markt naam
     */
    private String[] addMarkt(String[] array, String markt) {

        //roep de arrayModule op die de nieuwe String toe voegd in de array
        return arrayModule.addArray(array, markt);
    }

    /**
     * Getter
     *
     * @return array size
     */
    public int getARRAY_SIZE_DEAULT() {
        return ARRAY_SIZE_DEAULT;
    }
}
