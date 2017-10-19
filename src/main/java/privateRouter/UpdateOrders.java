package privateRouter;

import JSON.JSONObject;
import global.ConsoleColor;
import global.ErrorMessageResponse;
import global.GetExchangeId;
import mysql.Mysql;
import java.util.Stack;
import privateRouter.packageApi.BittrexProtocall;

/**
 * Methoden die bij houdt de status van de orders
 *
 * @author michel
 */
public class UpdateOrders {

    private final int ID_BITTREX = GetExchangeId.bittrexNummer();

    BittrexProtocall bp = new BittrexProtocall();
    Mysql mydwl = new Mysql();

    /**
     * Methoden die alles door loopt
     */
    public void updateOrders() {

        //roep de methoden op
        bittrexUpdateOrders();

    }

    private void bittrexUpdateOrders() {

        //kijk alle open orders van bittrex
        String bittrexReponse = bp.getOpenOrders();
        ConsoleColor.warn(bittrexReponse);

        //zet het in een object
        JSONObject bittexReponseObject = new JSONObject(bittrexReponse);

        //kijk of het succesvol is gelukt
        if (!ErrorMessageResponse.bittrex(bittexReponseObject)) {
        }

    }

    private void updateStatus() {

    }

    private void uuIDCheck(int idExchange, String uuId, double quantity, double quantityRemaining, double limit, String opened) {

    }
}
