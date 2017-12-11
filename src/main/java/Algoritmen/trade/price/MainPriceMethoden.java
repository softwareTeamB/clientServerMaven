package Algoritmen.trade.price;

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
    public double calcUsdtPrijs(double prijsRate) {

        return prijsRate + 0.02;

    }

    protected abstract void subCalcMethoden();
}
