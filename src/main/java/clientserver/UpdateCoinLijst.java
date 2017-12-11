package clientserver;

import JSON.JSONObject;
import global.ConsoleColor;
import global.ErrorMessageResponse;
import http.Http;

/**
 * klasse voor updateCoinLijst
 *
 * @author michel
 */
public class UpdateCoinLijst {

    private static Http http = new Http();

    public static void mainUpdateCoinLijst() {

        try {
            cexIo();
        } catch (Exception ex) {
            ConsoleColor.err(ex);
        }
    }

    /**
     * Maak de lijst van cointags
     */
    private static void cexIo() throws Exception {

        String httpUrl = ClientServer.cexIo + "/currency_limits";
        String httpReponse = http.getHttpBrowser(httpUrl);

        //zet de reponse in een object
        JSONObject object = new JSONObject(httpReponse);

        //kijk of het is gelukt
        boolean succes = ErrorMessageResponse.cexIoCheck(object);

        //als er een error is stop er dan mee
        if (!succes) {

            //stop de methoden
            return;
        }

    }
}
