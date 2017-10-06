package privateRouter;

import global.Mysql;
import java.sql.ResultSet;
import java.sql.SQLDataException;
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
     */
    public double[][] getBalance(String mainCoinString, String marktCoinString, String handelsplaats) throws SQLException {

        //maak de array aan
        double[][] returnArray = new double[1][2];
        double[] mainCoin = new double[2];
        double[] marktCoin = new double[2];

        //begin van de select stament
        String sqlStament = "SELECT balance, pending, available FROM balance "
                + "WHERE handelsplaats='" + handelsplaats + "' AND cointag='" + mainCoinString + "' || cointag='"+marktCoinString+"'";

        ResultSet rs = myql.mysqlSelect(sqlStament);

        while (rs.next()) {

            //get cointag
            String cointagDB = rs.getString("cointag");

            double balance = rs.getDouble("balance");
            double pending = rs.getDouble("pending");
            double available = rs.getDouble("available");

            //kijk welke coinNaam als eerste is
            if (cointagDB.equals(mainCoinString)) {

                //vul de tijdelijke array
                mainCoin[0] = balance;
                mainCoin[1] = pending;
                mainCoin[2] = available;

                //zet de array in de return array
                returnArray[0] = mainCoin;

            } else if (cointagDB.equals(marktCoinString)) {
                
                //vul de tijdelijke array
                marktCoin[0] = balance;
                marktCoin[1] = pending;
                marktCoin[2] = available;
                
                //vul de hoofd array
                returnArray[1]= marktCoin;
                
            } else {
                throw new SQLDataException("De database response is niet correct!");
            }
        }
        
        System.out.println(""+returnArray);
        
        //return array
        return returnArray;
    }

}
