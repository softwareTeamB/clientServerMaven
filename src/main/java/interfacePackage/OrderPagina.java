package interfacePackage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import clientserver.InterfaceMain;

/**
 * Klasse die alle onders aangeeft op de webpagina
 *
 * @author michel
 */
public class OrderPagina {

    /**
     * Interface om order zichtbaar maken
     *
     * @param primaryStage javafx
     */
    public void start(Stage primaryStage) {

        //maak grid aan
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

        primaryStage.setTitle("Corendon Bagage");
        Scene scene = new Scene(grid, 1200, 920);

        //maak zet scene in de primary scene
        primaryStage.setScene(scene);

        //vul screen modes
        primaryStage.setFullScreen(InterfaceMain.fullScreen);

        scene.getStylesheets().add(InterfaceMain.cssNaam);

        primaryStage.show();

    }
}
