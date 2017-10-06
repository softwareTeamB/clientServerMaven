package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Http request
 *
 * @author michel
 */
public class Http {

    public String GetHttp(String uri) {
        try {
            URL url = new URL(uri);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String strTemp = "";
            while (null != (strTemp = br.readLine())) {
                System.out.println(strTemp);
                return strTemp;
            }
        } catch (IOException ex) {
            return "false";
        }
        return "false";
    }
}
