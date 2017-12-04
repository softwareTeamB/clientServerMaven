package privateRouter;

import JSON.JSONArray;
import JSON.JSONObject;
import clientserver.ClientServer;
import global.ConsoleColor;
import java.util.HashMap;

/**
 * Deze klasse is om alle order te updaten in de database
 *
 * @author michel
 */
public class OrdersGetter {
    
    //maak de hashmap aan
    private HashMap<String, JSONObject> orderHashMap = new HashMap<>();
    private HashMap<String, JSONObject>orderHistory = new HashMap<>();
    
    /**
     * Methoden die alles orders moet updaten
     */
    public void updateOrders(){
        
        ordersBittrex();
    }

    /**
     * Vraag alle orders op van bittrex
     */
    private void ordersBittrex() {

        //vraag alle bittrex orders op
        String ordersLijstString = ClientServer.bittrexProtocall.getOpenOrders();

        //zet de reponse of in een jsonobject
        JSONObject orderObject = new JSONObject(ordersLijstString);

        ConsoleColor.out(ordersLijstString);

        //kijk of de request succesvol was
        if (!orderObject.getBoolean("success")) {

            //consoleColor
            ConsoleColor.err("Er is een probleem om orders op te vragen bij bittrex.");
        }

        //maak een JSONArray
        JSONArray orderLijst = orderObject.getJSONArray("result");

        //loop door de JSONArray
        for (int i = 0; i < orderLijst.length(); i++) {

            //pak de eerst volgende order
            JSONObject object = orderLijst.getJSONObject(i);

            //kijk of de order in de hashMap staat
            
            
            
            
            
            
        }
    }
    
    /**
     * return order hashmap
     * @return hashmap met alle orders
     */
    public HashMap getOrderHashMap(){
        return orderHashMap;
    };
    
    /**
     * Return order history hashmap
     * @return Return order history hashmap
     */
    public HashMap orderHistory(){
        return orderHistory;
    };

}
