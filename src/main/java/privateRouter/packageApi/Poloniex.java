package privateRouter.packageApi;

import JSON.JSONArray;
import JSON.JSONObject;
import global.ConsoleColor;
import http.Http;
import privateRouter.mainPrivateRouter;

/**
 * Klassie die alle public en private actie kan aansturen
 *
 * @author michel
 */
public class Poloniex extends mainPrivateRouter implements exchangeInterface {

    Http http = new Http();

    private final int EXCHANGE_ID;
    private final String EXCHANGE_NAAM = "poloniex";

    //url poloniex url nodejs
    String POLONIEX_NODEJS_URL = clientserver.ClientServer.NODE_JS_URL + "/" + EXCHANGE_NAAM;

    /**
     * Constructor op idExchange te vullen
     */
    public Poloniex() {

        //krijg het exchange id
        this.EXCHANGE_ID = clientserver.ClientServer.exchangeIdJSONObject.getInt(EXCHANGE_NAAM);
    }

    /**
     * ==================== Methodens voor het order system ==================== *
     */
    /**
     * Methoden op order te plaatsen
     *
     * @param baseCoin basis coin
     * @param marktCoin marktcoin
     * @param prijs de prijs
     * @param hoeveelheid hoeveelheid
     * @param type buy of sell
     * @return de exchange response
     */
    @Override
    public String setOrder(String type, String baseCoin, String marktCoin, double prijs, double hoeveelheid)
            throws Exception {

        //als type niet bij is ga dan door naar de volgende
        if (!"buy".equals(type)) {

            //als het niet sell is activeer de exceptie
            if (!"sell".equals(type)) {
                //maak een exceptie aan
                throw new Exception("Er is een error om een onder te plaatsen bij poloniex. Type is niet geldig: " + type);

            }
        }

        /*if (!"buy".equals(type) || !"sell".equals(type)) {
            //maak een exceptie aan
            throw new Exception("Er is een error om een onder te plaatsen bij poloniex. Type is niet geldig: " + type);
        }*/
        //maak een basis url
        String url2 = POLONIEX_NODEJS_URL + "/makeOrder";

        //voeg alle parameters toe
        url2 += "/" + type;
        url2 += "/" + baseCoin;
        url2 += "/" + marktCoin;
        url2 += "/" + prijs;
        url2 += "/" + hoeveelheid;

        System.out.println(url2);

        //roep de htpp methoden op via de static begin klasse
        String nodejsUrlReponse = http.getHttpObject(url2);
        System.out.println(nodejsUrlReponse);

        //maak een json 
        JSONObject objectReponse = new JSONObject(nodejsUrlReponse);

        //krijg het orderNummer
        double orderNummer = objectReponse.getDouble("orderNumber");
        String uuId = "" + orderNummer;

        //sla alle data op
        super.insertOrder(uuId, EXCHANGE_ID, prijs, hoeveelheid, baseCoin, marktCoin, type);

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

    /**
     * Methoden om een specifieke order te cancelen
     *
     * @param uuid oderid
     * @return of de order succes is gecanceld of niet
     */
    @Override
    public boolean cancelOrder(String uuid, String baseCoin, String marktCoin) {

         //verwidjerd order via de http request aan de nodejs server
        String url2 = POLONIEX_NODEJS_URL + "/cancelOrder/" + uuid+"/"+baseCoin+"/"+ marktCoin;
        http.GetHttp(url2);
        
        //
        return true;
    }

    @Override
    public String orderStatus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean cancelOrder() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * ==================== Methodens voor het balance het system ==================== *
     */
    /**
     * Vraag de balance op aan de nodejs server
     *
     * @throws java.lang.Exception error afvang systeem
     */
    @Override
    public void setServerBalance() throws Exception {

        //krijg de balance data van poloniex terug in een string
        String reponseString = http.getHttpObject(POLONIEX_NODEJS_URL + "/getBalance");

        //maak er een jsonobject van
        JSONObject object = new JSONObject(reponseString);
        
        //boolean of er een balanceUpdate is
        boolean balanceUpdate = false;

        //loop door alle keys heen
        for (int i = 0; i < object.names().length(); i++) {

            //vraag keyNaam op
            String keyNaam = object.names().getString(i);

            //vraag het object aan
            JSONObject object2 = object.getJSONObject(keyNaam);

            //vraag de balance op
            double available = object2.getDouble("available");
            double orders = object2.getDouble("onOrders");
            double balance = orders + available;

            //roep de balanceChecker op en kijk of er een verandering is
            boolean tempChange = super.balanceChecker(EXCHANGE_ID, keyNaam, balance, orders, available);
            
            //als temChange true is update balanceUpdate. Ook moet balanceUpdate op false staan
            if(tempChange == true && balanceUpdate == false){
                
                //zet balanceUpdate in true
                balanceUpdate = true;
            }
        }
    }

    /**
     * Methoden om de balance op te vragen
     *
     * @param currency cointag
     * @return
     */
    @Override
    public double[] getBalance(String currency) throws Exception {

        //kijk of currency in de lijst staat
        if (!super.keyCheck(currency, "balanceDB")) {
            ConsoleColor.out("De " + currency + " staat niet in de balanceDB. Nieuwe balance "
                    + "wordt aan de exchange opvraagd.");

            //vraag alle nieuwe balance op aan de server
            setServerBalance();
        }

        //vraag de string array op uit het jsonObject met de juiste key
        JSONArray balanceDataJSONArray = super.balanceDB.getJSONArray(currency);

        //maak het van JSONArray naar een string[]
        double[] balanceData = new double[3];
        balanceData[0] = balanceDataJSONArray.getDouble(0);
        balanceData[1] = balanceDataJSONArray.getDouble(1);
        balanceData[2] = balanceDataJSONArray.getDouble(2);

        //return de string array
        return balanceData;

    }

    /**
     * Methoden om de balande te updaten
     *
     * @param balanceArray balance array
     * @param currency cointag
     * @throws Exception error exceptie
     */
    @Override
    public void setBalance(double[] balanceArray, String currency) throws Exception {

        //haal alle data uit de array
        double balance = balanceArray[0];
        double available = balanceArray[1];
        double pending = balanceArray[2];

        //roep de methoden op die de data verwerkt
        super.balanceChecker(EXCHANGE_ID, currency, balance, available, pending);

    }
}
