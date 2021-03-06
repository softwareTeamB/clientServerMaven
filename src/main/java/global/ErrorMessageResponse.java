package global;

import JSON.JSONObject;
import java.util.Iterator;

/**
 * Methoden die error van de exchange verwerken
 *
 * @author michel
 */
public class ErrorMessageResponse {

    /**
     *
     * @param object
     * @return
     */
    public static boolean bittrexCheck(JSONObject object) {

        //vraag op het de request succesvol was
        boolean succes = object.getBoolean("success");

        //if loop
        if (succes) {
            //termianl bericht
            ConsoleColor.out("Bittrex request is succesvol verwerkt.");

            //return true
            return true;
        } else if (!succes) {

            //vraag het bericht op van bittrex
            String message = object.getString("message");

            //termianl bericht
            ConsoleColor.error("Er is een error bij bittrex reponse. Het error bericht is: " + message + ".");

            //return false
            return false;
        } else {
            ConsoleColor.err("Bij bittrex success is de reponse niet true of false! Return standaart false.");

            //return false
            return false;
        }
    }

    /**
     *
     * @param object
     * @return
     */
    public static boolean cexIoCheck(JSONObject object) {

        //loop door alles keys heen
        Iterator<String> keys = object.keys();
        if (keys.hasNext()) {

            //als er een error is
            if ("error".equals(keys.next())) {
                //termianl bericht
                ConsoleColor.error("Er is een error bij cex.io reponse.");

                //return false
                return false;
            };
        }

        //termianl bericht
        ConsoleColor.out("Bittrex request is succesvol verwerkt.");

        //return true
        return true;
    }
}
