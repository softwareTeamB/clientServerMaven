package clientserver;

/**
 * Methoden waar de marktnaam in elkaar gezet word
 *
 * @author michel
 */
public class greatMarktNaam {

    /**
     * Methoden die de marktnaam return
     *
     * @param baseCoin basis coin
     * @param marktCoin marktNaamCoin
     * @param exchangeIdString exchangeId in
     * @return
     */
    public String getMarktNaam(String baseCoin, String marktCoin,
            String exchangeIdString) {

        String verbindingsTeken
                = ClientServer.exchangeVerbindingsTeken.getString(
                        "" + exchangeIdString
                );

        //return de marktnaam
        return baseCoin + verbindingsTeken + marktCoin;
    }
}
