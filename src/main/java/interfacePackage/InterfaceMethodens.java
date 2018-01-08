package interfacePackage;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.ComboBox;
import mysql.Mysql;

/**
 * In deze klasse staan alle belangrijke methoden
 * @author michel_desktop
 */
public class InterfaceMethodens {
    
    //maak objecten aan
    private static Mysql mysql = new Mysql();
    
    /**
     * Methoden om de combox te vullen
     * @param exchangeSelect checkbox moet er in
     * @return return stament
     * @throws SQLException sql error 
     */
    public static ComboBox exhchangeLijstCombox (ComboBox exchangeSelect) throws SQLException{
    
        //sql stament
        String sqlSelect ="SELECT handelsplaats FROM exchangelijst";
        
        //result set mysql
        ResultSet rs = mysql.mysqlSelect(sqlSelect);
        
        //loop door de resultset
        while(rs.next()){
            
            exchangeSelect.getItems().add(rs.getString("handelsplaats"));
        }
        
        return exchangeSelect;
    }
    
}
