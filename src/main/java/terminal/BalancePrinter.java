package terminal;

import global.ConsoleColor;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe die de balance print in de terminal
 *
 * @author michel_desktop
 */
public class BalancePrinter {

    /**
     * Constructors
     */
    public BalancePrinter() {
        try {

            //vraag alle balance op
            String sqlStament = "SELECT "
                    + "exchangeLijst.handelsplaats, vieuwbalance.cointag, vieuwbalance.balance, "
                    + "vieuwbalance.available, vieuwbalance.pending "
                    + "FROM vieuwbalance "
                    + "INNER JOIN exchangeLijst ON vieuwbalance.exchangeId = exchangeLijst.idExchangeLijst "
                    + "WHERE vieuwbalance.balance  != 0 AND  vieuwbalance.available  != 0";

            //vraag alle balance data op waar het balance niet 0 is
            ResultSet rs = clientserver.ClientServer.mysql.mysqlSelect(sqlStament);

            //kijk hoeveel rows er in de resultset zit
            int nummer = clientserver.ClientServer.mysql.rsSize(rs);
            
            //als het nummer 0 is stop de methoden met een bericht
            if (nummer == 0) {

                //bericht
                ConsoleColor.out("Er is geen balance data beschikbaar");

                //return stament
                return;
            }

            //print het begin van de melding
            String format = "%-10s %-10s %-30s %-30s %-30s\n";
            System.out.printf(format, "exchange", "cointag", "balance", "available", "pending");
            System.out.print("\n");

            //while loop door de data heen
            while (rs.next()) {

                //vraag double data
                String exchangeNaam = rs.getString("handelsplaats");
                String cointag = rs.getString("cointag");
                double balance = rs.getDouble("balance");
                double available = rs.getDouble("available");
                double pending = rs.getDouble("pending");

                System.out.printf(format, exchangeNaam, cointag, balance, available, pending);

            }
        } catch (SQLException ex) {
            ConsoleColor.err("Er is een probleem bij terminal print balance. Dit is de error: " + ex);
        }
    }
}
