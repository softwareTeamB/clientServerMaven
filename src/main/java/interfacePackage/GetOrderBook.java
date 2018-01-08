package interfacePackage;

import clientserver.InterfaceMain;
import http.Http;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mysql.Mysql;

/**
 * Maak een order book
 *
 * @author michel
 */
public class GetOrderBook extends Application {

    //maak object aan
    Http http = new Http();
    Mysql mysql = new Mysql();

    private ComboBox exchangeSelect = new ComboBox();
    
    /**
     * Construcotr
     */
    public GetOrderBook() {
    }
    
    
    
    
    
    
    

    @Override
    public void start(Stage primaryStage) {
        
        int yPosition = 2;

        //menu balk
        MenuB menuB = new MenuB();
        MenuBar menuBar = menuB.createMenuB(primaryStage);
        BorderPane root = new BorderPane();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        root.setTop(menuBar);

        //maak grid aan
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

         //voeg grid toe aan de root
        root.setCenter(grid);
        
        //labels en comboBoxen en checkboxen
        Text grafieken = new Text("OrderBook");
        grafieken.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25));
        grid.add(grafieken, 0, 0, 2, 1);

        Text selecteerData = new Text("Select data");
        selecteerData.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        grid.add(selecteerData, 0, 1, 2, 1);       
        
        
          
        Label selectExchangLabel = new Label("Selecteer exchange");
        grid.add(selectExchangLabel, 0, yPosition);
        
        ComboBox exchangeSelect = new ComboBox();
        exchangeSelect.getItems().addAll(
            "2012", "2013", "2014", "2015", "2016", "2017"
        );
        //voeg excgabge select to aan de grid
        grid.add(exchangeSelect, 1,  yPosition);
        
        
        ComboBox beginMaand = new ComboBox();
        beginMaand.getItems().addAll(
            "01", "02", "03" , "04", "05", "06", "07", "08", "09", "10", "11", "12"
        );
        grid.add(beginMaand, 2, yPosition);
        
        
        

        //maak scene aan en voeg de root toe
        Scene scene = new Scene(root, InterfaceMain.getXas(), InterfaceMain.getYas());

        //css load
        scene.getStylesheets().add(InterfaceMain.getCss());

        //set title
        primaryStage.setTitle(InterfaceMain.getTitle());

        //voeg scene toe aan de primary stage
        primaryStage.setScene(scene);

        //full screen modus
        primaryStage.setFullScreen(InterfaceMain.getFullScreen());

        primaryStage.show();
    }
    

}
