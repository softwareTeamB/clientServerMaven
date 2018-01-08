package Algoritmen.trade.price;

import JSON.JSONObject;

/**
 * Methoden wat een extends moet zijn
 *
 * @author michel
 */
public abstract class MainPriceMethoden {

    /**
     * Calc de methoden btc prijs
     *
     * @param prijsRate de prijs
     * @return return de prijs met 1 satoshi er bij
     */
    public double calcBtcPrijs(double prijsRate) {
        return prijsRate + 0.00000001;
    }

    /**
     * Bereken de dollars prijs
     *
     * @param prijsRate de prijs
     * @return de nieuwe prijs
     */
    public double calcBuyUsdtPrijs(double prijsRate) {

        return prijsRate + 0.01;

    }

    /**
     * main methoden
     *
     * @return de prijs
     * @throws java.lang.Exception als er een error is
     */
    protected abstract void mainMethoden() throws Exception;

    /**
     * Methoden om de beste buy prijs uit te rekenen
     *
     * @param objectResult object van de exchange
     * @return de prijs
     */
    protected abstract double calcBuyPrijs(JSONObject objectResult) ;

    /**
     * Methoden op de beste sell Prijs uit te rekenen
     *
     * @param objectResult object van de exchange
     * @return de prijs
     */
    protected abstract double calcSellPrijs(JSONObject objectResult);

    /**
     * Methoden om de buy prijs uit te rekenen
     *
     * @param prijs de prijs
     * @return de nieuwe buy prijs
     */
    public double buyPrijs(double prijs) {
        return prijs + 0.00000001;
    }

    /**
     * Methoden om de buy prijs uit te rekenen
     *
     * @param prijs de prijs
     * @return de nieuwe sell prijs
     */
    public double sellPrijs(double prijs) {
        return prijs - 0.00000001;
    }
}
