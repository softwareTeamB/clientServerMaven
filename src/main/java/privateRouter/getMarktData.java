package privateRouter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author michel
 */
public class getMarktData {
    
    /**
     * Open socket stream
     * @param requestData in de string staat wat er opgevraagd moet worden
     * @return de data van de server terug
     * @throws IOException 
     */
    public String sendData(String requestData) throws IOException {
        int port = 9099;
        String serverName = "127.0.0.1";

        System.out.println("Connecting to " + serverName + " on port " + port);
        java.net.Socket client = new java.net.Socket(serverName, port);

        System.out.println("Just connected to " + client.getRemoteSocketAddress());
        OutputStream outToServer = client.getOutputStream();
        DataOutputStream out = new DataOutputStream(outToServer);

        out.writeUTF(requestData);
        InputStream inFromServer = client.getInputStream();
        DataInputStream in = new DataInputStream(inFromServer);

        System.out.println("Server says " + in.readUTF());
        client.close();
        
        //return de dataa
        return in.readUTF();
    }

}
