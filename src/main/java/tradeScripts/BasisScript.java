package tradeScripts;

/**
 * Script voor een basis trades
 * @author michel
 */
public class BasisScript {
    
    
    private double maxPrijs;
    private double maxAmmount;
    private double exchangeFee;
    private int exchangeId;
    private String baseCoin;
    private String marktCoin;
    

    /**
     * Constructor
     * @param maxPrijs de maximale buy prijs
     * @param maxAmmount de maximale hoeveelheid
     * @param exchangeId de exchangeID
     * @param baseCoin de basis coin
     * @param marktCoin de markt coin
     */
    public BasisScript(double maxPrijs, double maxAmmount, double exchangeFee, int exchangeId, String baseCoin, String marktCoin) {
        this.maxPrijs = maxPrijs;
        this.maxAmmount = maxAmmount;
        this.exchangeFee = exchangeFee;
        this.exchangeId = exchangeId;
        this.baseCoin = baseCoin;
        this.marktCoin = marktCoin;
        
        //roep de eerst methoden op zodat de order geplaats kan worden
        getMarktData();
    }
    
    private void getMarktData(){
        
    }
    
    
    
    
    
    
    
    
}
