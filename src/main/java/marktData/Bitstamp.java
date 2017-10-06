package marktData;

import JSON.JSONArray;
import JSON.JSONObject;
import global.Mysql;
import http.Http;

/**
 * Bitstamp api om alle markt data op te vragen
 *
 * @author michel
 */
public class Bitstamp extends MainServer {

    Mysql mysql = new Mysql();
    Http http = new Http();

    //array dit is gedaan zodat het programma alle markten kent die door de loop gedaan zijn
    private String[] marktenInArray;
    private final String EXCHANGE_NAAM;

    /*maak een jsonobject zodat je het via de jsonobject key
     *het nummer op kan vragen
     */
    private JSONObject getMarkten = new JSONObject();

    /**
     * Constructor
     *
     * @param marktObject object met de namen hoe het in de database staat en de
     * marktnaam
     */
    public Bitstamp(JSONObject marktObject) {
        
        //de positie waar de array is
        int indexPositieArray= 0;

        //this stament
        this.EXCHANGE_NAAM = "bitstamp";
        this.getMarkten = marktObject;
        
        //vraag de JSONArray op uit het object
        JSONArray marktLijstArray = marktObject.getJSONArray("marktenBekendeExchange");
        
        //marktLijstArray size
        int arraySize = marktLijstArray.length();
        
        //maak de array aan
        marktenInArray = new String[arraySize];
        
        //loop door de JSONArray heen
        for (int i = 0; i < marktenInArray.length; i++) {
            
            //haal de marktnaam op
            String marktNaam = marktLijstArray.getString(i);
            
            //voeg het toe in de array
            marktenInArray[indexPositieArray] = marktNaam;
        }

        System.out.println(getMarkten);
        System.out.println(marktLijstArray);

    }

    @Override
    public void getData() {

        String sqlString = "";

    }
}
