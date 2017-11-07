/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privateRouter.packageApi;

import com.loopj.android.http.Base64;
import android.util.Log;

import JSON.JSONException;
import JSON.JSONObject;
import global.ConsoleColor;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author root
 */
public class Bitfinex {

    private static final String TAG = Bitfinex.class.getSimpleName();

    private static final String ALGORITHM_HMACSHA384 = "HmacSHA384";

    private String apiKey = "";
    private String apiKeySecret = "";
    private long nonce = System.currentTimeMillis();

    /**
     * public access only
     */
    public Bitfinex() {
        apiKey = null;
        apiKeySecret = null;
    }

    /**
     * public and authenticated access
     *
     * @param apiKey
     * @param apiKeySecret
     */
    public Bitfinex(String apiKey, String apiKeySecret) {
        this.apiKey = apiKey;
        this.apiKeySecret = apiKeySecret;
    }

    /**
     * Vraag de balance info op
     *
     * @return return een string met de ba;ance waarde
     * @throws IOException file exception
     *
    public String v1Balances() throws IOException {

        //urlPath
        String urlPath = "/balances";

        return autthSystem(urlPath);
    }
    
    **
     * Auth system
     *
     * @param urlPath url locatie
     * @return return de reponse
     * @throws IOException io exceptions
     *
    private String autthSystem(String urlPath) throws IOException {

        String sResponse;

        HttpURLConnection conn = null;
        String method = "POST";

        try {
            URL url = new URL("https://api.bitfinex.com/v1/balances");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);

            if (isAccessPublicOnly()) {
                String msg = "Authenticated access not possible, because key and secret was not initialized: use right constructor.";
                Log.e(TAG, msg);
                return msg;
            }

            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject jo = new JSONObject();
            jo.put("request", urlPath);
            jo.put("nonce", Long.toString(getNonce()));

            // API v1
            String payload = jo.toString();

            // this is usage for Base64 Implementation in Android. For pure java you can use java.util.Base64.Encoder
            // Base64.NO_WRAP: Base64-string have to be as one line string
            String payload_base64 = Base64.encodeToString(payload.getBytes(), Base64.NO_WRAP);

            String payload_sha384hmac = hmacDigest(payload_base64, apiKeySecret, ALGORITHM_HMACSHA384);

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.addRequestProperty("X-BFX-APIKEY", apiKey);
            conn.addRequestProperty("X-BFX-PAYLOAD", payload_base64);
            conn.addRequestProperty("X-BFX-SIGNATURE", payload_sha384hmac);

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            return convertStreamToString(in);

        } catch (MalformedURLException e) {
            throw new IOException(e.getClass().getName(), e);
        } catch (ProtocolException e) {
            throw new IOException(e.getClass().getName(), e);
        } catch (IOException e) {

            String errMsg = e.getLocalizedMessage();

            if (conn != null) {
                try {
                    sResponse = convertStreamToString(conn.getErrorStream());
                    errMsg += " -> " + sResponse;
                    Log.e(TAG, errMsg, e);
                    return sResponse;
                } catch (IOException e1) {
                    errMsg += " Error on reading error-stream. -> " + e1.getLocalizedMessage();
                    Log.e(TAG, errMsg, e);
                    throw new IOException(e.getClass().getName(), e1);
                }
            } else {
                throw new IOException(e.getClass().getName(), e);
            }
        } catch (JSONException e) {
            String msg = "Error on setting up the connection to server";
            throw new IOException(msg, e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                ConsoleColor.err(e);
            }
        }
        return sb.toString();
    }

    public long getNonce() {
        return ++nonce;
    }

    public boolean isAccessPublicOnly() {
        return apiKey == null;
    }

    public static String hmacDigest(String msg, String keyString, String algo) {
        String digest = null;
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), algo);
            Mac mac = Mac.getInstance(algo);
            mac.init(key);

            byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));

            StringBuffer hash = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            digest = hash.toString();
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        } catch (InvalidKeyException e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return digest;
    }*/
    
     public String sendRequestV1Balances() throws IOException {
        String sResponse;

        HttpURLConnection conn = null;

        String urlPath = "/v1/balances";
        // String method = "GET";
        String method = "POST";

        try {
            URL url = new URL("https://api.bitfinex.com" + urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);

            if (isAccessPublicOnly()) {
                String msg = "Authenticated access not possible, because key and secret was not initialized: use right constructor.";
                Log.e(TAG, msg);
                return msg;
            }

            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject jo = new JSONObject();
            jo.put("request", urlPath);
            jo.put("nonce", Long.toString(getNonce()));

            // API v1
            String payload = jo.toString();

			// this is usage for Base64 Implementation in Android. For pure java you can use java.util.Base64.Encoder
          // Base64.NO_WRAP: Base64-string have to be as one line string
            String payload_base64 = Base64.encodeToString(payload.getBytes(), Base64.NO_WRAP);

			
            String payload_sha384hmac = hmacDigest(payload_base64, apiKeySecret, ALGORITHM_HMACSHA384);

           conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.addRequestProperty("X-BFX-APIKEY", apiKey);
            conn.addRequestProperty("X-BFX-PAYLOAD", payload_base64);
            conn.addRequestProperty("X-BFX-SIGNATURE", payload_sha384hmac);

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            return convertStreamToString(in);

        } catch (MalformedURLException e) {
            throw new IOException(e.getClass().getName(), e);
        } catch (ProtocolException e) {
            throw new IOException(e.getClass().getName(), e);
        } catch (IOException e) {

            String errMsg = e.getLocalizedMessage();

            if (conn != null) {
                try {
                    sResponse = convertStreamToString(conn.getErrorStream());
                    errMsg += " -> " + sResponse;
                    Log.e(TAG, errMsg, e);
                    return sResponse;
                } catch (IOException e1) {
                    errMsg += " Error on reading error-stream. -> " + e1.getLocalizedMessage();
                    Log.e(TAG, errMsg, e);
                    throw new IOException(e.getClass().getName(), e1);
                }
            } else {
                throw new IOException(e.getClass().getName(), e);
            }
        } catch (JSONException e) {
            String msg = "Error on setting up the connection to server";
            throw new IOException(msg, e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public long getNonce() {
        return ++nonce;
    }

    public boolean isAccessPublicOnly() {
        return apiKey == null;
    }

    public static String hmacDigest(String msg, String keyString, String algo) {
        String digest = null;
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), algo);
            Mac mac = Mac.getInstance(algo);
            mac.init(key);

            byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));

            StringBuffer hash = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            digest = hash.toString();
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        } catch (InvalidKeyException e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return digest;
    }

}
