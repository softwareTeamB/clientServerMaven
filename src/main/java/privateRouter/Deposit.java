package privateRouter;

import JSON.JSONArray;
import JSON.JSONObject;
import global.ConsoleColor;
import global.ErrorMessageResponse;
import global.GetExchangeId;
import mysql.Mysql;
import java.util.logging.Level;
import java.util.logging.Logger;
import privateRouter.packageApi.BittrexProtocall;
import Mail.MailServer;
import javax.mail.MessagingException;

/**
 * Klasse die kijkt of er nieuwe deposit
 *
 * @author michel
 */
public class Deposit {

    BittrexProtocall bp = new BittrexProtocall();

    private static int ID_EXCHANGE = GetExchangeId.bittrexNummer();
    Mysql mysql = new Mysql();
    MailServer mail = new MailServer();

    /**
     * Main methoden
     */
    public void mainDeposit() {
        bittrexDeposit();
    }

    /**
     * Bittrex deposit
     */
    private void bittrexDeposit() {

        //vraag alle data op
        String bittrexReponse = bp.getDepositHistory();

        //zet de reponse in een object
        JSONObject bittrexResponseObject = new JSONObject(bittrexReponse);

        //kijk of alles is gelukt
        if (!ErrorMessageResponse.bittrex(bittrexResponseObject)) {
            //stop de methoden
            return;
        }

        //zet het in een JSONArray
        JSONArray array = bittrexResponseObject.getJSONArray("result");

        //for loop
        for (int i = 0; i < array.length(); i++) {

            //pak het eerst volgende object
            JSONObject object = array.getJSONObject(i);

            //vraag txId op
            String txId = object.getString("TxId");

            //amount
            double amount = object.getDouble("Amount");

            //cointg
            String cointag = object.getString("Currency");

            try {
                //roep de depositCheck in
                depositCheck(txId, ID_EXCHANGE, amount, cointag);
            } catch (Exception ex) {
                ConsoleColor.err("Er is een error op getreden bij depositCheck. Dit is de error: " + ex);
            }

        }
    }

    /**
     * Methoden die de deposit check doet
     *
     * @param txId tx code
     * @param idExchange exchangenummer
     * @param amount de hoeveelheid
     * @param cointag cointag
     */
    private void depositCheck(String txId, int idExchange, double amount, String cointag) throws Exception {

        //sqlcount string
        String sqlCount = "SELECT COUNT(*) AS total FROM depositLijst WHERE txid='" + txId + "' AND idExchangeLijst='" + idExchange + "'";
        int count = mysql.mysqlCount(sqlCount);
        if (count == 0) {

            //voeg de transactie in de database
            String sqlInsert = "INSERT INTO depositLijst (txid, idExchangeLijst, cointag, hoeveelheid) "
                    + "VALUES ('" + txId + "', '" + idExchange + "', '" + cointag + "', '" + amount + "')";
            mysql.mysqlExecute(sqlInsert);
            ConsoleColor.out(sqlInsert);

            ConsoleColor.out("De transactie is toegevoegd in de database.");

            String htmlBody = "<p> txId: " + txId + "</p><p>exchange naam: " + idExchange + "</p>"
                    + "<p> Cointag: " + cointag + "</p><p>Hoeveelheid: " + amount + "</p>";
            
            mail.generateAndSendEmail("", htmlBody);
            
        }

    }
}
