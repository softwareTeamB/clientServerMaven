package privateRouter.packageApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import security.EncryptionUtility;

import global.ConsoleColor;
import global.FileSystem;
import global.LoadPropFile;

public class BittrexProtocall {

    private final String ORDERBOOK_BUY = "buy", ORDERBOOK_SELL = "sell", ORDERBOOK_BOTH = "both";
    private final Exception InvalidStringListException = new Exception("Must be in key-value pairs");
    private final String API_VERSION = "1.1", INITIAL_URL = "https://bittrex.com/api/";
    private final String PUBLIC = "public", MARKET = "market", ACCOUNT = "account";
    private final String encryptionAlgorithm = "HmacSHA512";
    private String API_KEY;
    private String API_SECRET;
    private ConsoleColor consoleColor;

    //maak object
    FileSystem filesystem = new FileSystem();

    /**
     * Constuctor
     */
    public BittrexProtocall() {

        try {
            Properties config = LoadPropFile.loadPropFile("apiKeys");
            this.API_KEY = config.getProperty("bittrexApiKey");
            this.API_SECRET = config.getProperty("bittrexApiSecretKey");
        } catch (IOException ex) {
            ConsoleColor.error("Er is een error in de contructor van bittrexProtocall. Dit is de error: " + ex
                    + "\nDit software wordt opgesloten.");

            //Sluit het systeem af
            System.exit(0);
        }
    }

    public String getMarkets() { // Returns all markets with their metadata

        return getJson(API_VERSION, PUBLIC, "getmarkets");
    }

    public String getCurrencies() { // Returns all currencies currently on Bittrex with their metadata

        return getJson(API_VERSION, PUBLIC, "getcurrencies");
    }

    public String getTicker(String market) { // Returns current tick values for a specific market

        return getJson(API_VERSION, PUBLIC, "getticker", returnCorrectMap("market", market));
    }

    public String getMarketSummaries() { // Returns a 24-hour summary of all markets

        return getJson(API_VERSION, PUBLIC, "getmarketsummaries");
    }

    public String getMarketSummary(String market) { // Returns a 24-hour summar for a specific market

        return getJson(API_VERSION, PUBLIC, "getmarketsummary", returnCorrectMap("market", market));
    }

    public String getOrderBook(String market, String type) { // Returns the orderbook for a specific market

        return getJson(API_VERSION, PUBLIC, "getorderbook", returnCorrectMap("market", market, "type", type));
    }

    public String getMarketHistory(String market) { // Returns latest trades that occurred for a specific market

        return getJson(API_VERSION, PUBLIC, "getmarkethistory", returnCorrectMap("market", market));
    }

    public String buyLimit(String market, String quantity, String rate) { // Places a limit buy in a specific market; returns the UUID of the order

        return getJson(API_VERSION, MARKET, "buylimit", returnCorrectMap("market", market, "quantity", quantity, "rate", rate));
    }

    /**
     * Double enz word omgezet
     *
     * @param market handels plaats
     * @param quantityDouble de hoeveelheid
     * @param prijsDouble de prijs
     * @return uuid
     */
    public String buyLimitv2(String market, double quantityDouble, double prijsDouble) {

        //convert alles to string
        String quantity = Double.toString(quantityDouble);
        String rate = Double.toString(prijsDouble);

        //String market, String quantity, String rate
        return getJson(API_VERSION, MARKET, "buylimit", returnCorrectMap("market", market, "quantity", quantity, "rate", rate));
    }

    public String buyMarket(String market, String quantity) { // Places a market buy in a specific market; returns the UUID of the order

        return getJson(API_VERSION, MARKET, "buymarket", returnCorrectMap("market", market, "quantity", quantity));
    }

    public String sellLimit(String market, String quantity, String rate) { // Places a limit sell in a specific market; returns the UUID of the order

        return getJson(API_VERSION, MARKET, "selllimit", returnCorrectMap("market", market, "quantity", quantity, "rate", rate));
    }

    /**
     * @see Double enz word omgezet
     * @param market handels plaats
     * @param quantityDouble de hoeveelheid
     * @param prijsDouble de prijs
     * @return uuid
     */
    public String sellLimitv2(String market, double quantityDouble, double prijsDouble) {

        //convert alles to string
        String quantity = Double.toString(quantityDouble);
        String rate = Double.toString(prijsDouble);

        //String market, String quantity, String rate
        return getJson(API_VERSION, MARKET, "selllimit", returnCorrectMap("market", market, "quantity", quantity, "rate", rate));
    }

    public String sellMarket(String market, String quantity) { // Places a market sell in a specific market; returns the UUID of the order

        return getJson(API_VERSION, MARKET, "sellmarket", returnCorrectMap("market", market, "quantity", quantity));
    }

    public String cancelOrder(String uuid) { // Cancels a specific order based on its UUID

        return getJson(API_VERSION, MARKET, "cancel", returnCorrectMap("uuid", uuid));
    }

    public String getOpenOrders(String market) { // Returns your currently open orders in a specific market

        String method = "getopenorders";

        if (market.equals("")) {
            return getJson(API_VERSION, MARKET, method);
        }

        return getJson(API_VERSION, MARKET, method, returnCorrectMap("market", market));
    }

    public String getOpenOrders() { // Returns all your currently open orders

        return getOpenOrders("");
    }

    public String getBalances() { // Returns all balances in your account

        return getJson(API_VERSION, ACCOUNT, "getbalances");
    }

    public String getBalance(String currency) { // Returns a specific balance in your account

        return getJson(API_VERSION, ACCOUNT, "getbalance", returnCorrectMap("currency", currency));
    }

    public String getDepositAddres(String currency) { // Returns the deposit address for a specific currency - if one is not found, it will be generated

        return getJson(API_VERSION, ACCOUNT, "getdepositaddress", returnCorrectMap("currency", currency));
    }

    public String withdraw(String currency, String quantity, String address, String paymentId) { // Withdraw a certain amount of a specific coin to an address, and add a payment id

        String method = "withdraw";

        if (paymentId.equals("")) {
            return getJson(API_VERSION, ACCOUNT, method, returnCorrectMap("currency", currency, "quantity", quantity, "address", address));
        }

        return getJson(API_VERSION, ACCOUNT, method, returnCorrectMap("currency", currency, "quantity", quantity, "address", address, "paymentid", paymentId));
    }

    public String withdraw(String currency, String quantity, String address) { // Withdraw a certain amount of a specific coin to an address

        return withdraw(currency, quantity, address, "");
    }

    public String getOrder(String uuid) { // Returns information about a specific order (by UUID)

        return getJson(API_VERSION, ACCOUNT, "getorder", returnCorrectMap("uuid", uuid));
    }

    public String getOrderHistory(String market) { // Returns your order history for a specific market

        String method = "getorderhistory";

        if (market.equals("")) {
            return getJson(API_VERSION, ACCOUNT, method);
        }

        return getJson(API_VERSION, ACCOUNT, method, returnCorrectMap("market", market));
    }

    public String getOrderHistory() { // Returns all of your order history

        return getOrderHistory("");
    }

    public String getWithdrawalHistory(String currency) { // Returns your withdrawal history for a specific currency

        String method = "getwithdrawalhistory";

        if (currency.equals("")) {
            return getJson(API_VERSION, ACCOUNT, method);
        }

        return getJson(API_VERSION, ACCOUNT, method, returnCorrectMap("currency", currency));
    }

    public String getWithdrawalHistory() { // Returns all of your withdrawal history

        return getWithdrawalHistory("");
    }

    public String getDepositHistory(String currency) { // Returns your deposit history for a specific currency

        String method = "getdeposithistory";

        if (currency.equals("")) {
            return getJson(API_VERSION, ACCOUNT, method);
        }

        return getJson(API_VERSION, ACCOUNT, method, returnCorrectMap("currency", currency));
    }

    public String getDepositHistory() { // Returns all of your deposit history

        return getDepositHistory("");
    }

    private HashMap<String, String> returnCorrectMap(String... parameters) { // Handles the exception of the generateHashMapFromStringList() method gracefully as to not have an excess of try-catch statements

        HashMap<String, String> map = null;

        try {

            map = generateHashMapFromStringList(parameters);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return map;
    }

    private HashMap<String, String> generateHashMapFromStringList(String... strings) throws Exception { // Method to easily create a HashMap from a list of Strings

        if (strings.length % 2 != 0) {
            throw InvalidStringListException;
        }

        HashMap<String, String> map = new HashMap<String, String>();

        for (int i = 0; i < strings.length; i += 2) // Each key will be i, with the following becoming its value
        {
            map.put(strings[i], strings[i + 1]);
        }

        return map;
    }

    private String getJson(String apiVersion, String type, String method) {

        return getResponseBody(generateUrl(apiVersion, type, method));
    }

    private String getJson(String apiVersion, String type, String method, HashMap<String, String> parameters) {

        return getResponseBody(generateUrl(apiVersion, type, method, parameters));
    }

    private String generateUrl(String apiVersion, String type, String method) {

        return generateUrl(apiVersion, type, method, new HashMap<String, String>());
    }

    private String generateUrl(String apiVersion, String type, String method, HashMap<String, String> parameters) {

        String url = INITIAL_URL;

        url += "v" + apiVersion + "/";
        url += type + "/";
        url += method;
        url += generateUrlParameters(parameters);

        return url;
    }

    private String generateUrlParameters(HashMap<String, String> parameters) { // Returns a String with the key-value pairs formatted for URL

        String urlAttachment = "?";

        Object[] keys = parameters.keySet().toArray();

        for (Object key : keys) {
            urlAttachment += key.toString() + "=" + parameters.get(key) + "&";
        }

        return urlAttachment;
    }

    /*public static HashMap<String, String> getMapFromResponse(String response) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        final HashMap<String, String> map = gson.fromJson(response.substring(response.lastIndexOf("\"result\"") + "\"result\"".length() + 2, response.indexOf("}") + 1), new TypeToken<HashMap<String, String>>() {
        }.getType()); // Sorry.

        return map;
    }*/
    private String getResponseBody(String url) {

        String result = null;
        url += "apikey=" + API_KEY + "&nonce=" + EncryptionUtility.generateNonce();

        try {

            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);

            request.addHeader("apisign", EncryptionUtility.calculateHash(API_SECRET, url, encryptionAlgorithm)); // Attaches signature as a header

            HttpResponse httpResponse = client.execute(request);

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

            StringBuffer resultBuffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                resultBuffer.append(line);
            }

            result = resultBuffer.toString();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return result;
    }
}
