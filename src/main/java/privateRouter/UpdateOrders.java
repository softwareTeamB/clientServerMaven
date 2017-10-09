package privateRouter;

import global.GetExchangeId;
import privateRouter.packageApi.BittrexProtocall;

/**
 *Methoden die bij houdt de status van de orders
 * @author michel
 */
public class UpdateOrders {
    
    private final int ID_BITTREX = GetExchangeId.bittrexNummer();
    
    BittrexProtocall bp = new BittrexProtocall();
    
    /**
     * Methoden die alles door loopt
     */
    public void updateOrders(){
    
        //roep de methoden op
        bittrexUpdateOrders();
    
    
    }
    
    public void bittrexUpdateOrders(){
    
        //kijk alle open orders van bittrex
        String bittrexReponse = bp.getOpenOrders();
        
    
    }
    
    
    private void updateStatus(){
    
    }
}
