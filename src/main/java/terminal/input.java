/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terminal;

import global.ConsoleColor;
import java.util.Scanner;

/**
 *
 * @author root
 */
public class input {
    public void test(){
    Scanner sc = new Scanner(System.in);
        String i = sc.next();
        ConsoleColor.out(""+i);
        if("exit".equals(i)){
            ConsoleColor.warn("Systeem sluit zich zelf af");
                System.exit(0);
        }
        
        test();
    }
}
