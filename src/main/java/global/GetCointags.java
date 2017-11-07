package global;

import JSON.JSONArray;
import JSON.JSONObject;
import static clientserver.ClientServer.coinmarketcapUrl;
import http.Http;
import java.util.ArrayList;

/**
 * cointagLijst
 * @author michel
 */
public class GetCointags {
    
    //maak objecten aan
    Http http = new Http();
    
    public ArrayList getCointags(){
        
        //maak een tijdelijke arrayLijst
        ArrayList tempArrayLijst = new ArrayList();
        
        //vul de cointagMarketRequest
        String urlTickerCoinmarketcap = coinmarketcapUrl+"/ticker/";
        try {
            
            //vraag alle data op
            String reponseHttp = http.getHttpObject(urlTickerCoinmarketcap);
            
            //zet het in een arrayLijst
            JSONArray array = new JSONArray(reponseHttp);
            
            for (int i = 0; i < array.length(); i++) {
                
                JSONObject object = array.getJSONObject(i);
                String cointag = object.getString("symbol");
                
                //add het in de arrayList
                tempArrayLijst.add(cointag);
            }
            
        } catch (Exception ex) {
            ConsoleColor.err("Er is een error bij getCointags. Dit is de error: "+ex 
                    +". Het systeem wordt afgesloten omdat het een fatale error is.");
            
            ArrayList temp = new ArrayList();
            
            System.exit(0);
            return temp;
        }
        
        //return array
        return tempArrayLijst;
    }
}
