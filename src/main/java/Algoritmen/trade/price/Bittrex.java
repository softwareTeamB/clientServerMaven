package Algoritmen.trade.price;

import JSON.JSONObject;
import JSON.JSONArray;
import global.ConsoleColor;
import global.ErrorMessageResponse;
import http.Http;

/**
 * Methoden om de prijs te berekenen
 *
 * @author michel_desktop
 */
public class Bittrex extends MainPriceMethoden {

    //http request
    private Http http = new Http();
    private ErrorMessageResponse eMRS = new ErrorMessageResponse();

    //alle variable
    private final String MARKT;
    private final String MARKT_TYPE;
    private final double PRIJS;
    private final String apiUrl;
    private String BestPrijs;

    /**
     * Constructor
     *
     * @param MARKT markt
     * @param MARKT_TYPE markt type
     * @param PRIJS
     */
    public Bittrex(String MARKT, String MARKT_TYPE, double PRIJS) {
        this.MARKT = MARKT;
        this.MARKT_TYPE = MARKT_TYPE;
        this.PRIJS = PRIJS;

        //haal de variable van de clientServer
        this.apiUrl = clientserver.ClientServer.getBittrexUrl();
    }

    /**
     * Main methoden
     *
     * @throws java.lang.Exception Error afvang systeem
     */
    @Override
    protected void mainMethoden() throws Exception {

        //kijk of de markt goed is
        if ("buy" != MARKT_TYPE || "sell" != MARKT_TYPE) {
            throw new Exception("De markt type is niet bekend. Dit is het mark type: " + MARKT_TYPE);
        }

        //maak de url
        String url = apiUrl + "https://bittrex.com/api/v1.1/public/getorderbook?market=" + MARKT + "type=" + MARKT_TYPE;

        //request de api
        String httpRs = http.GetHttp(url);

        //zet de reponse in een jsonobject
        JSONObject objectReponse = new JSONObject(httpRs);

        //kijk of er een error is
        boolean requestSuccesVol = ErrorMessageResponse.bittrexCheck(objectReponse);

        //als de request niet zinvol is stop dan de methoden
        if (!requestSuccesVol) {
            throw new Exception("Er is een error bij bittrex");
        }

        //jsonObject
        JSONObject objectResult = objectReponse.getJSONObject("result");

        //selecteer de juiste markt type
        if ("buy".equals(MARKT_TYPE)) {

            this.BestPrijs = "" + calcBuyPrijs(objectResult);

        } else if ("sell".equals(MARKT_TYPE)) {

            JSONArray buyArray = objectResult.getJSONArray("sell");
        }
    }

    /**
     * Methoden om de beste buy prijs uit te rekenen
     *
     * @param objectResult object van de exchange
     * @return de prijs
     */
    @Override
    protected double calcBuyPrijs(JSONObject objectResult) {

        //vul de array
        JSONArray buyArray = objectResult.getJSONArray("buy");

        //loop door de array heen
        for (int i = 0; i < buyArray.length(); i++) {

            //loop er door heen en zoek de juiste positie voor de order
            JSONObject objectCounter = buyArray.getJSONObject(i);

            //haal de eerste prijs op
            double prijsCounter = objectCounter.getDouble("Rate");

            //if stament
            if (prijsCounter < PRIJS) {
                ConsoleColor.out("Beste prijs gevonden!");

                //return stament
                return super.buyPrijs(prijsCounter);
            }
        }

        //return de default prijs
        return PRIJS;
    }

    @Override
    protected double calcSellPrijs(JSONObject objectResult) {
        //vul de array
        JSONArray buyArray = objectResult.getJSONArray("sell");

        //loop door de array heen
        for (int i = 0; i < buyArray.length(); i++) {

            //loop er door heen en zoek de juiste positie voor de order
            JSONObject objectCounter = buyArray.getJSONObject(i);

            //haal de eerste prijs op
            double prijsCounter = objectCounter.getDouble("Rate");

            //if stament
            if (prijsCounter < PRIJS) {
                ConsoleColor.out("Beste prijs gevonden!");

                //return stament
                return prijsCounter - 0.00000001;
            }
        }

        //return de default prijs
        return PRIJS;
    }
}
