package terminal;

import java.sql.ResultSet;
import java.sql.SQLException;
import mysql.MysqlServer;
import mysql.Mysql;

/**
 *
 * @author michel
 */
public class MysqlRowUpdate {

    //mysql server connectoin
    MysqlServer mysqlServer = new MysqlServer();
    Mysql mysql = new Mysql();

    /**
     * Run de void methoden
     *
     * @throws java.sql.SQLException als er een java error is
     */
    public void run() throws SQLException {

        marktNaamRow();
        marktLijsten();
    }

    /**
     * MarktNaamRow update
     *
     * @throws SQLException
     */
    private void marktNaamRow() throws SQLException {

        //String
        String selectStament = "SELECT * FROM marktnaam";

        //result set
        ResultSet rs = mysqlServer.mysqlSelect(selectStament);

        //while loop
        while (rs.next()) {

            //alle data word uit de colum gehaald
            int marktPositieDb = rs.getInt("idMarktNaam");
            String marktNaamDB = rs.getString("marktnaamDb");
            String baseCoin = rs.getString("baseCoin");
            String marktCoin = rs.getString("marktCurrency");

            //insert add
            String insertAdd = "INSERT INTO marktNaam(idMarktNaam, marktnaamDb, "
                    + "baseCoin, marktCurrency) "
                    + "VALUES ('" + marktPositieDb + "', '" + marktNaamDB + "', "
                    + "'" + baseCoin + "', '" + marktCoin + "')";
            //insert in database
            mysql.mysqlExecute(insertAdd);
        }
    }

    /**
     * Haal de data op uit de server schema en voeg het toe aan de client schema
     *
     * @throws SQLException
     */
    private void marktLijsten() throws SQLException {

        //String
        String selectStament = "SELECT * FROM marktLijsten";

        //result set
        ResultSet rs = mysqlServer.mysqlSelect(selectStament);

        //while loop
        while (rs.next()) {

            //alle data uit de colummen gehaald
            String idHandelsplaats = "" + rs.getInt("idHandelsplaats");
            String idMarktNaam = "" + rs.getInt("idMarktNaam");
            String minumTrade = "" + rs.getInt("minimumTrade");
            String naamMarkt = "" + rs.getString("naamMarkt");

            //insert string mysql
            String insertAdd = "INSERT INTO marktLijsten (idHandelsplaats, "
                    + "idMarktNaam, minimumTrade, naamMarkt) "
                    + "VALUES (" + idHandelsplaats + "," + idMarktNaam + ", "
                    + "" + minumTrade + ", '" + naamMarkt + "')";

            //voeg het toe in de database
            mysql.mysqlExecute(insertAdd);
        }
    }

}

//ubic, substratum, staller, xrp
