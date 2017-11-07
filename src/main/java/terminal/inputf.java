package terminal;

import global.ConsoleColor;
import java.util.Scanner;

/**
 *
 * @author michel
 */
public class inputf {

    //Het object scanner
    private Scanner sc;

    /**
     * Construcor
     */
    public inputf() {

        //maak de scanner aan
        sc = new Scanner(System.in);
    }

    /**
     * De methoden die de scanner aanstuurd
     */
    public void mainKlasse() {

        //het object inputf
        Object inputObject = sc.next();

        switch (inputObject.toString()) {

            //als het command help is
            case "help":
                //update de help methoden
                help();
                break;

            //sluit het systeem af
            case "exit":
                //sluit de applicatie af
                sluitApplicatie();
                break;
                
                //als het 0 us
            case "0":
                //roep de wijzigPropertiesFile methoden op
                wijzigPropertiesFile();
                break;
            //als het 1 is
            case "1":
                //exchange apiKeys
                exchangeApiKeys();
                break;

            //als het 2 is
            case "2":
                //methoden voor de balance
                balance();
                break;
            case "3":
                
                
                break;
            default:
                ConsoleColor.out("U heeft geen geldige input gegevens. Typ help in om alle commands te zien.");
        }

        //roep de main klasse opnieuw op
        mainKlasse();
    }

    /**
     * Help command
     */
    private void help() {

        ConsoleColor.out("Help optie  response");

        String format = "%-30s%s%n";
        System.out.printf(format, "Opties", "code");
        System.out.print("\n");
        System.out.printf(format, "Wijzig properties bestanden" , 0);
        System.out.printf(format, "Wijzig apiKeys", 1);
        System.out.printf(format, "Laat balance zien", 2);
        System.out.printf(format, "orders", 3);
        
        
        
        System.out.printf(format, "Sluiten het systeem af:", "exit");
    }
    
    private void orderSetting(){
    
    }

    /**
     * Methoden voor exchangeApiKeys
     */
    private void exchangeApiKeys() {
        ConsoleColor.err("De exchangeApiKeys moeten nog gebouwd worden.");
    }
    
    private void wijzigPropertiesFile(){
        ConsoleColor.err("Om de properties bestanden aan de passen moet nog gebouwd worden");
    }

    /**
     * Methoden om de applicatie af tesluiten
     */
    private void sluitApplicatie() {

        ConsoleColor.warn("Het systeem wordt afgesloten.");
        System.exit(0);
    }

    /**
     * Methoden voor de balance
     */
    private void balance() {

        ConsoleColor.err("De balance laten zien moeten nog gebouwd worden.");
    }
    
    /**
     * orderSettings
     */
    private void orderSettings(){
        
        
        
    }
}
