package global;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Methoden die alle exchange id oproept vanaf de server en static maakt
 *
 * @author michel
 */
public class GetExchangeId {

    private static int idExchange;

    Mysql mysql = new Mysql();

    /**
     * Constructor
     */
    public GetExchangeId() {

        try {
            //Vraag alle gegevens op uit de exchangeLijst
            String sqlSelect = "SELECT * FROM exchangeLijst";
            ResultSet rs = mysql.mysqlSelect(sqlSelect);

            while (rs.next()) {
                String exchangeNaam = rs.getString("handelsplaats");

                if ("bittrex".equals(exchangeNaam)) {
                    idExchange = rs.getInt("idExchangeLijst");
                }
            }
        } catch (SQLException ex) {
            ConsoleColor.err("Er is een probleem bij getExchangeID. De error is: " + ex
                    + " .Het programma wordt veilig afgesloten.");
            System.exit(0);
        }
    }

    /**
     * Methoden die het bittrex nummer return geeft
     *
     * @return bittrex nummer
     */
    public static int bittrexNummer() {
        return idExchange;
    }
}
