package clientserver;

/**
 * Methoden waar de marktnaam in elkaar gezet word
 *
 * @author michel
 */
public class GreatMarktNaam {

    /**
     * Methoden die de marktnaam return
     *
     * @param baseCoin basis coin
     * @param marktCoin marktNaamCoin
     * @param exchangeIdString exchangeId in
     * @return marktnaam
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
