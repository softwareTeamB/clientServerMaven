package interfacePackage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Methoden die de menu bar geeft boven in het scherm
 *
 * @author michel_desktop
 */
public class MenuB {

    /**
     * De menu bar
     * @param primaryStage primaray stage
     * @return return starge
     */
    public MenuBar createMenuB(Stage primaryStage) {

        MenuBar menuBar = new MenuBar();
        //Home home = new Home();
        Login login = new Login();
        //Profile profiel = new Profile();

        // File menu - new, save, exit
        Menu homeB = new Menu("Options");
        Menu options = new Menu("Options");
        MenuItem homePage = new MenuItem("Go to homepage");
        MenuItem logout = new MenuItem("Logout");
        MenuItem exit = new MenuItem("Exit application");

        //linkboven het logo van corendon
        //Image logo = new Image("file:src/images/corendon_logo.png") {};
        //primaryStage.getIcons().add(logo);
        //button event handelers
        exit.setOnAction(actionEvent -> Platform.exit());
        homePage.setOnAction((ActionEvent e) -> {
            //home.start(primaryStage);
        });

        logout.setOnAction((ActionEvent e) -> {
            //login.start(primaryStage);
        });

        //toevoegen actions bij options
        homeB.getItems().addAll(homePage, logout,
                new SeparatorMenuItem(), exit);

        //help button
        Label helpLabel = new Label("Help");
        Menu help = new Menu();
        help.setGraphic(helpLabel);
        helpLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        File myFile = new File("src/images/Handleiding.pdf");
                        Desktop.getDesktop().open(myFile);
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                }
            }
        });

        //profiel button
        Label menuLabel = new Label("Profile");
        Menu profile = new Menu();
        profile.setGraphic(menuLabel);
        menuLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //profiel.star(primaryStage);  
            }
        });

        //alles toevoegen bij menubar
        menuBar.getMenus().addAll(homeB, help, profile);

        return menuBar;

    }
}
