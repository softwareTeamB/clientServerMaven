package privateRouter;

import JSON.JSONObject;
import global.ConsoleColor;
import http.Http;

/**
 * Klassie die alle public en private actie kan aansturen
 *
 * @author michel
 */
public class Poloniex implements exchangeInterface {

    Http htpp = new Http();

    //private url
    private final String POLONIEX_NODEJS_URL = clientserver.ClientServer.nodeJsUrl + "/poloniex";

    /**
     * Vraag de balance op aan de nodejs server
     *
     * @return de string waarin de balance gegevens staat
     * @throws java.lang.Exception error afvang systeem
     */
    @Override
    public String setBalance() throws Exception {

        //krijg de balance data van poloniex terug in een string
        String url = clientserver.ClientServer.nodeJsUrl;
        String reponseString = htpp.getHttpBrowser(url + "/getBalance");

        //return de string
        return reponseString;
    }

    /**
     * Methoden om de balance op te vragen
     *
     * @param currency cointag
     * @return
     */
    @Override
    public String getBalance(String currency) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Methoden op order te plaatsen
     *
     * @param baseCoin basis coin
     * @param marktCoin marktcoin
     * @param prijs de prijs
     * @param hoebeelheid hoeveelheid
     * @param type buy of sell
     * @return
     */
    @Override
    public String setOrder(String baseCoin, String marktCoin, double prijs, double hoebeelheid, String type)
            throws Exception {

        //if stament of type goed ingevuld is
        if ("buy".equals(type) || "sell".equals(type)) {
            //maak een exceptie aan
            throw new Exception("Er is een error om een onder te plaatsen bij poloniex. Type is niet geldig: " + type);
        }

        //maak jsonobject
        JSONObject object = new JSONObject();

        //voeg string toe
        object.put("type", type);
        object.put("baseCoin", baseCoin);
        object.put("marktCoin", marktCoin);
        object.put("prijs", prijs);
        object.put("hoeveelheid", hoebeelheid);

        //maak een 
        String url = POLONIEX_NODEJS_URL + "/makeOrder";

        //maak van het object een string
        String postString = object.toString();

        //roep de httpPost methoden op via de static begin klasse
        String nodejsUrlReponse = clientserver.ClientServer.httpPost.sendingPostRequest(url, postString);

        //return stament
        return nodejsUrlReponse;
    }

    @Override
    public String getOrders() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getOrder(String uuid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean cancelOrder(String uuid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String orderStatus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean cancelOrder() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
