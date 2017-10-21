package mysql;

import global.ConsoleColor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Klasse die voor de verbinding met het database zorgt
 * @author michel
 */
public class Mysql {

    //variable
    private final String USERNAME = "root";
    private final String PASSWORD = "Pulsar11";
    private final String IPADDRESS = "localhost";
    private final String POORT = "3306";
    private final String DATABASENAAM = "clientproject";
    private final boolean AUTORECONNECT = true;
    private final boolean SSL = false;
    private final String CONN_STRING = "jdbc:mysql://" + IPADDRESS + ":" + POORT + "/" + DATABASENAAM + "?autoReconnect=" + AUTORECONNECT + "&useSSL=" + SSL;
    private Statement stmt;

    /**
     * Constructor waar het properties bestand wordt opgehaald en de connectie wordt gemaakt
     */
    public Mysql() {
        try {
            Connection conn;
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            this.stmt = (Statement) conn.createStatement();
        } catch (SQLException ex) {
            
            //console color
            ConsoleColor.err("Er is een error opgetreden met het aanmaken van connenctie in mysql. Dit is de error: "+ ex
                    + ".\n De applicatie wordt veilig afgesloten.");
            System.exit(0);
        }
    }

    /**
     * Select stament
     *
     * @param sqlString mysql string
     * @return return de velden
     * @throws SQLException als er iets fout gaat
     */
    public ResultSet mysqlSelect(String sqlString) throws SQLException {

        //return
        return stmt.executeQuery(sqlString);
    }

    /**
     * Run sql staments zonder return
     *
     * @param sqlString sql stament
     * @throws SQLException als er iets fout gaat
     */
    public void mysqlExecute(String sqlString) throws SQLException {

        //run stament
        stmt.execute(sqlString);
    }

    public int mysqlCount(String sqlString) throws SQLException, Exception {

        //return
        ResultSet rs = stmt.executeQuery(sqlString);
        while (rs.next()) {
            return rs.getInt("total");
        }

        //als er een error optreed
        throw new Exception("Mysql count error. Mogelijk geen connectie of er is geen geldig colum ingevuld.");
    }

    /**
     * Return nummer wat er is
     *
     * @param sqlString nummer
     * @return return 1 nummer
     * @throws SQLException sql error
     * @throws Exception andere exceptions
     */
    public int mysqlExchangeNummer(String sqlString) throws SQLException, Exception {

        //return
        ResultSet rs = stmt.executeQuery(sqlString);
        while (rs.next()) {
            return rs.getInt("nummer");
        }

        //als er een error optreed
        throw new Exception("Mysql kan geen nummer return geven");
    }

    /**
     * Methoden die de exchange nummer geeft
     *
     * @param exchange exchange naam
     * @return return 1 nummer
     * @throws SQLException sql error
     * @throws Exception andere exceptions
     */
    public int mysqlExchangeNummerV2(String exchange) throws SQLException, Exception {
        
        //return
        ResultSet rs = stmt.executeQuery("select getExchangeNummer('"+exchange+"') AS nummer;");
        while (rs.next()) {
            return rs.getInt("nummer");
        }

        //als er een error optreed
        throw new Exception("Mysql kan geen nummer return geven");
    }

    /**
     * Return nummer wat er is
     *
     * @param sqlString nummer
     * @return return 1 nummer
     * @throws SQLException sql error
     * @throws Exception andere exceptions
     */
    public int mysqlNummer(String sqlString) throws SQLException, Exception {

        //return
        ResultSet rs = stmt.executeQuery(sqlString);
        while (rs.next()) {
            return rs.getInt("nummer");
        }

        //als er een error optreed
        throw new Exception("Mysql kan geen nummer return geven");
    }

    /**
     * Return het nummer van de marktID
     *
     * @param sqlString sql string het count object met nummer heten
     * @return het nummer
     * @throws SQLException als de database response leeg is
     */
    public int mysqlIdMarktNaam(String sqlString) throws SQLException {

        //return
        ResultSet rs = stmt.executeQuery(sqlString);
        while (rs.next()) {
            return rs.getInt("nummer");
        }
        throw new SQLDataException("De database reponse is leeg");
    }
}
