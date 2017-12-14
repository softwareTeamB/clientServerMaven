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
     * MarktNaamRow update
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
            String insertAdd = "";

        }

    }
}
