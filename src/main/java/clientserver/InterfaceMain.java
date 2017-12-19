package clientserver;

import static clientserver.ClientServer.config;
import global.ConsoleColor;
import interfacePackage.OrderPagina;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Main methoden van de interface
 *
 * @author michel
 */
public class InterfaceMain extends Application {

    /**
     * Naam van het css file
     */
    public static String cssNaam = "global2.css";

    /**
     * Of fullscreen enable is of niet
     */
    public static boolean fullScreen = false;

    /**
     * Assen van het window
     */
    public static int xAs = 1200;
    public static int yAs = 920;

    /**
     * Methoden om de interface op te starten
     *
     * @param args command-line
     */
    public static void interfaceMethoden(String[] args) {

        //vraag het properties bestand op
        String loadInterfaceString = config.getProperty("loadInterface");

        //als het if stament true is dat de interface gestart word
        if ("true".equals(loadInterfaceString)) {

            //start de applicatie methoden op
            launch(args);
        } else {
            ConsoleColor.warn("De interface wordt niet geladen");
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        //Welkom + Letter type
        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        //Username text
        Label userNameLabel = new Label("Username:");
        grid.add(userNameLabel, 0, 1);

        //Text veld na Username
        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        //De Sign in button
        Button btn = new Button("Log in");
        btn.setPrefWidth(150);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 3);

        btn.setOnAction((ActionEvent e) -> {

            //roep de methoden op die de order pagina laat
            OrderPagina op = new OrderPagina();
            op.start(primaryStage);

        });

        primaryStage.setTitle("Corendon Bagage");
        Scene scene = new Scene(grid, 1200, 920);
        primaryStage.setScene(scene);
        
        //full screen modus
        primaryStage.setFullScreen(fullScreen);
        
        //css load
        scene.getStylesheets().add(cssNaam);
        primaryStage.show();
    }

}
