package terminal;

import global.ConsoleColor;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.SQLException;
import mysql.Mysql;

/**
 *
 * @author michel
 */
public class mainTerminal {

    //Het object scanner
    private Scanner sc;

    //maak mysql aan
    Mysql mysql = new Mysql();

    /**
     * Construcor
     */
    public mainTerminal() {

        //maak de scanner aan
        sc = new Scanner(System.in);
    }

    /**
     * De methoden die de scanner aanstuurd
     */
    public void mainKlasse() {

        //het object mainTerminal
        Object inputObject = sc.next();

        switch (inputObject.toString()) {

            //als het command help is
            case "help":
                //update de help methoden
                help();
                break;

            //sluit het systeem af
            case "exit":
                //sluit de applicatie af
                sluitApplicatie();
                break;

            //als het 0 us
            case "0":
                //roep de wijzigPropertiesFile methoden op
                wijzigPropertiesFile();
                break;
            //als het 1 is
            case "1":
                //exchange apiKeys
                exchangeApiKeys();
                break;

            //als het 2 is
            case "2":
                //methoden voor de balance
                balance();
                break;
            case "3":

                break;
            case "Build_markt_again":
            case "4":
                try {
                    buildMarktData();
                } catch (SQLException ex) {
                    ConsoleColor.err("Er is een error opgetreden bij de "
                            + "clean build van de data.");
                }

                break;
            default:
                ConsoleColor.out("U heeft geen geldige input gegevens. Typ "
                        + "help in om alle commands te zien.");
        }

        //roep de main klasse opnieuw op
        mainKlasse();
    }

    /**
     * Help command
     */
    private void help() {

        ConsoleColor.out("Help optie  response");

        String format = "%-30s%s%n";
        System.out.printf(format, "Opties", "code");
        System.out.print("\n");
        System.out.printf(format, "Wijzig properties bestanden", 0);
        System.out.printf(format, "Wijzig apiKeys", 1);
        System.out.printf(format, "Laat balance zien", 2);
        System.out.printf(format, "orders", 3);
        System.out.printf(format, "Build_markt_again", 4);

        System.out.printf(format, "Sluiten het systeem af:", "exit");
        
        /*ConsoleColor.outTable(format, "Opties", "code");
        //System.out.print("\n");
        ConsoleColor.outTable(format, "Wijzig properties bestanden", 0);
        ConsoleColor.outTable(format, "Wijzig apiKeys", 1);
        ConsoleColor.outTable(format, "Laat balance zien", 2);
        ConsoleColor.outTable(format, "orders", 3);
        ConsoleColor.outTable(format, "Build_markt_again", 4);
        ConsoleColor.outTable(format, "Sluiten het systeem af:", "exit");*/
    }

    private void orderSetting() {

    }

    /**
     * Cleare build marktdata
     *
     * @throws SQLException als er een mysql error is
     */
    private void buildMarktData() throws SQLException {

        //delete alls rows in tables
        String[] tableLijst = {"marktnaam", "marktupdate", "marktlijsten",
            "orderSetting", "orderSettingBuy", "orderSettingSell"};
        for (int i = 0; i < tableLijst.length; i++) {

            //verwijdere alle rows
            mysql.mysqlExecute("DELETE FROM " + tableLijst[i]);
        }

        //maak object aan
        MysqlRowUpdate mRU = new MysqlRowUpdate();
        mRU.run();

    }

    /**
     * Methoden voor exchangeApiKeys
     */
    private void exchangeApiKeys() {
        ConsoleColor.err("De exchangeApiKeys moeten nog gebouwd worden.");
    }

    private void wijzigPropertiesFile() {
        ConsoleColor.err("Om de properties bestanden aan de passen moet nog gebouwd worden");
    }

    /**
     * Methoden om de applicatie af tesluiten
     */
    private void sluitApplicatie() {

        ConsoleColor.warn("Het systeem wordt afgesloten.");
        System.exit(0);
    }

    /**
     * Methoden voor de balance
     */
    private void balance() {

        try {
            //boolean of er data in mysql staat
            boolean dataCheck = false;

            //vraag alle balance op
            String sqlStament = "SELECT "
                    + "exchangeLijst.handelsplaats, vieuwbalance.cointag, vieuwbalance.balance, "
                    + "vieuwbalance.available, vieuwbalance.pending "
                    + "FROM vieuwbalance "
                    + "INNER JOIN exchangeLijst ON vieuwbalance.exchangeId = exchangeLijst.idExchangeLijst "
                    + "WHERE vieuwbalance.balance  != 0 AND  vieuwbalance.available  != 0";

            //vraag alle balance data op waar het balance niet 0 is
            ResultSet rs = mysql.mysqlSelect(sqlStament);

            //print het begin van de melding
            String format = "%-10s %-10s %-30s %-30s %-30s\n";
            System.out.printf(format, "exchange", "cointag", "balance", "available", "pending");
            System.out.print("\n");

            //while loop door de data heen
            while (rs.next()) {

                //kijk of dataCheck op true staat. Zo niet zet hem op true
                if (!dataCheck) {

                    //Zet dataCheck op true
                    dataCheck = true;
                }

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

    /**
     * orderSettings
     */
    private void orderSettings() {

    }
}
