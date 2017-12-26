package interfacePackage;

import clientserver.InterfaceMain;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author michel_desktop
 */
public class Home extends Application {

    /**
     * Abstracten methoden voor de javafx scherm. Dit is de home methoden
     *
     * @param primaryStage primary stage
     */
    @Override
    public void start(Stage primaryStage) {

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

        //buttons
        Button btnBalance = new Button("Balance");
        btnBalance.setPrefWidth(150);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        //hbBtn.getChildren().add(btnBalance);
        grid.add(btnBalance, 1, 3);

        btnBalance.setOnAction((ActionEvent e) -> {

            //roep de methoden op die de order pagina laat
            BalanceInterface op = new BalanceInterface();
            op.start(primaryStage);
        });
        
        //krijg orderbook
        Button btnOrderBook = new Button("Order Book");
        btnOrderBook.setPrefWidth(150);
        
        //action
        btnOrderBook.setOnAction((ActionEvent e) -> {
        
            //maak het object getOrderBook en roep hem op
            GetOrderBook gOB = new GetOrderBook();
            gOB.start(primaryStage);
        });
        
        //voeg grid toe aan de root
        root.setCenter(grid);

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
