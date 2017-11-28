package privateRouter;

/**
 * Hier in staat belangrijke methoden die de communicatie moeten regelen met de exchange
 *
 * @author michel
 */
public interface exchangeInterface {

    /**
     * Order status
     *
     * @return string met de order status
     * @throws java.lang.Exception error afvang systeem
     */
    public String orderStatus() throws Exception;

    /**
     * Cancel order
     *
     * @return of het succes vol de order is gecancelt
     * @throws java.lang.Exception error afvang systeem
     */
    public boolean cancelOrder() throws Exception;

    /**
     * Plaats order
     *
     * @param type buy of sell
     * @param baseCoin basis coin
     * @param marktCoin markt coin
     * @param prijs de prijs zonder fee
     * @param hoeveelheid hoeveel er gekockt moet worden
     * @return UUID komt terug als het goed gaat
     * @throws java.lang.Exception error afvang systeem
     */
    public String setOrder(String type, String baseCoin, String marktCoin, double prijs, double hoeveelheid) throws Exception;

    /**
     * Vraag de orders op die uitstaan bij de exchange
     *
     * @return
     * @throws java.lang.Exception error afvang systeem
     */
    public String getOrders() throws Exception;

    /**
     * Vraag de orders op die uitstaan bij de exchange
     *
     * @param uuid order id
     * @return de order status
     * @throws java.lang.Exception error afvang systeem
     */
    public String getOrder(String uuid) throws Exception;

    /**
     * Cancel order
     *
     * @param uuid order id
     * @param baseCoin basis coin
     * @param marktCoin markt currency
     * @return order succusvol is
     */
    public boolean cancelOrder(String uuid, String baseCoin, String marktCoin);

    /**
     * Krijg de balance. Positie [0] is balance. Positie [1] available. Positie [2] pending balance
     *
     * @param currency cointag
     * @return return string array
     * @throws java.lang.Exception error afvang
     */
    public double[] getBalance(String currency) throws Exception;

    /**
     * Haal de balance op van de server
     *
     * @throws Exception Error
     */
    public void setServerBalance() throws Exception;

    /**
     *
     * @param balanceArray String array met de balance waarde.Positie[0] balance Positie [1] available. Positie [2]
     * pending balance
     * @param currency cointag
     * @throws Exception error exception
     */
    public void setBalance(double[] balanceArray, String currency) throws Exception;
}
