package privateRouter;

import global.ConsoleColor;
import global.Mysql;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author michel
 */
public class DataGetter {

    //object aan
    Mysql myql = new Mysql();

    /**
     * Methoden die de balance data op vraagt uit het database
     *
     * @param mainCoinString de naam van de main coin
     * @param marktCoinString de naam van de markt coin
     * @param handelsplaats de naam van de exchange
     * @return array met balance data de positief [0] is de main coin [1] is de markt coin
     * @throws java.sql.SQLException als er een sql error op treed
     * @throws java.Exception als er een algemen error optreed
     */
    public double[][] getBalance(String mainCoinString, String marktCoinString, String handelsplaats) throws SQLException, Exception {

        //vraag het exchangeNummer op
        String sqlStament = "SELECT idExchangeLijst AS nummer FROM exchangeLijst WHERE handelsplaats='" + handelsplaats + "'";
        int idExchange = myql.mysqlExchangeNummerV2(sqlStament);

        //maak de array aan
        double[][] returnArray = new double[2][3];
        double[] mainCoin = new double[3];
        double[] marktCoin = new double[3];

        //begin van de select stament
        String sqlStament2 = "SELECT balance, pending, available FROM balance "
                + "WHERE idExchangeLijst='" + idExchange + "' AND cointag='" + mainCoinString + "';";
        ConsoleColor.out(sqlStament2);
        ResultSet rs = myql.mysqlSelect(sqlStament2);

        while (rs.next()) {

            double balance = rs.getDouble("balance");
            double pending = rs.getDouble("pending");
            double available = rs.getDouble("available");

            //vul de tijdelijke array
            mainCoin[0] = balance;
            mainCoin[1] = pending;
            mainCoin[2] = available;

            //zet de array in de return array
            returnArray[0] = mainCoin;
        }

        //begin van de select stament
        String sqlStament3 = "SELECT balance, pending, available FROM balance "
                + "WHERE idExchangeLijst='" + idExchange + "' AND cointag='" + marktCoinString + "'";
        ConsoleColor.out(sqlStament3);
        ResultSet rs2 = myql.mysqlSelect(sqlStament3);

        while (rs2.next()) {

            double balance = rs2.getDouble("balance");
            double pending = rs2.getDouble("pending");
            double available = rs2.getDouble("available");

            //vul de tijdelijke array
            marktCoin[0] = balance;
            marktCoin[1] = pending;
            marktCoin[2] = available;

            //vul de hoofd array
            returnArray[1] = marktCoin;

        }

        for (int i = 0; i < returnArray.length; i++) {
            for (int j = 0; j < returnArray.length; j++) {
                double d = returnArray[i][j];
                System.out.println(d);
                
            }System.out.println(returnArray[i]);
        }
        //return array
        return returnArray;
    }
}
