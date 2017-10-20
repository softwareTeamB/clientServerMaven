package global;

import java.util.Date;

/**
 * klasse om de timestamp te krijgen
 * @author michel
 */
public class Tijd {
        /**
     * Die de timestamp opvraagd
     *
     * @return
     */
    private static long timeStamp() {

        //krijg de time stamp
        Date date = new Date();
        long time = date.getTime();

        return time;
    }

    /**
     * methoden die de timestamp geeft
     *
     * @return timestamp in string
     */
    public static int getTimeStamp() {
        return (int) timeStamp() / 1000;
    }
}
