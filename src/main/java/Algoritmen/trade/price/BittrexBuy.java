package Algoritmen.trade.price;

import JSON.JSONArray;
import JSON.JSONObject;
import global.ConsoleColor;
import mysql.Mysql;
import clientserver.ClientServer;
import global.ErrorMessageResponse;

/**
 * Methoden die de beste buy prijs bittrex terug geeft
 *
 * @author michel
 */
public final class BittrexBuy extends MainPriceMethoden {

    private final double MAX_PRIJS;
    private final int EXCHANGE_ID;
    private final String baseCoin;
    private final String marktCoin;
    private final String typePrijs;
    private final String orderType;
    private double prijs;

    private Mysql mysql = new Mysql();

    /**
     * Constructor
     *
     * @param MAX_PRIJS de maximale prijs
     * @param EXCHANGE_ID handelsplaats
     * @param baseCoin de basiscoin
     * @param marktCoin de marktcoin
     * @param typePrijs of het usdt of btc is
     */
    public BittrexBuy(double MAX_PRIJS,
            int EXCHANGE_ID,
            String baseCoin,
            String marktCoin,
            String typePrijs,
            String orderType) {

        //vul de variable
        this.MAX_PRIJS = MAX_PRIJS;
        this.EXCHANGE_ID = EXCHANGE_ID;
        this.baseCoin = baseCoin;
        this.marktCoin = marktCoin;
        this.typePrijs = typePrijs;
        this.orderType = orderType;

        //methoden calc
        subCalcMethoden();
    }

    /**
     * De sub methoden om het orderbook op te vragen
     */
    @Override
    protected void subCalcMethoden() {

        //vraag de marktnaam op uit het database
        String marktNaam = ClientServer.getMarktNaam(baseCoin,
                marktCoin, "" + EXCHANGE_ID);

        //vraag het onder book
        String url = ClientServer.getBittrexUrl() + "/public/getorderbook? "
                + "market=" + marktNaam + "&type="+orderType;

        //vraag de reponse op van bittrex
        String httpReponse = ClientServer.http.GetHttp(url);
        ConsoleColor.out("Bij bittrex is het orderbook opvraagd.");

        //jsonobject
        JSONObject object = new JSONObject(httpReponse);

        //check als er een error is
        boolean succesVol = ErrorMessageResponse.bittrexCheck(object);

        //stop de methoden als het false is
        if (succesVol) {
            return;
        }

        //zet result in een JSONArray
        JSONArray array = object.getJSONArray("result");

        //array
        for (int i = 0; i < array.length(); i++) {

            //pak het eerst volgende object
            JSONObject object2 = array.getJSONObject(i);

            //vraag de prijs op
            double prijsRate = object2.getDouble("Rate");

            //kijk of compare positief is
            if (prijsRate < MAX_PRIJS) {

                //bereken de prijs uit
                //kijk naar het if stament welke type prijs het is
                if ("btc".equals(typePrijs)) {

                    //de prijs in de private variable zetten en de prijs
                    prijs = super.calcBtcPrijs(prijsRate);
                } else if ("usdt".equals(typePrijs)) {

                    //de prijs in de private variable zetten en de prijs
                    prijs = super.calcUsdtPrijs(prijsRate);
                } else {
                    ConsoleColor.err("De beste prijs kan niet terug geven worden! Dit omdat het type baseCoin niet herkend wordt.");
                }
                
                //stop de for loop
                break;

            }

        }
    }

    /**
     * ToString methoden
     *
     * @return de prijs
     */
    @Override
    public String toString() {
        return "" + prijs;
    }
}
