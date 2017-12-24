package interfacePackage;

import clientserver.InterfaceMain;
import global.ConsoleColor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Methoden die het order book terug geeft
 *
 * @author michel
 */
public class BalanceInterface extends Application {

    /**
     * Als er geen balance is dan moet deze op false blijven Hierdoor word er een melding geven dat er geen balance beschikbaar is
     */
    private boolean balanceAvailable = false;

    /**
     * Table vieuw
     */
    private TableView<Person> table = new TableView<Person>();

    /**
     * ObserverableList
     */
    private final ObservableList<Person> data
            = FXCollections.observableArrayList();

    /**
     * Constructor
     */
    public BalanceInterface() {

        try {
            fillDataObservableList();
        } catch (SQLException ex) {
            Logger.getLogger(BalanceInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void start(Stage primaryStage) {

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

        final Label label = new Label("Address Book");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        TableColumn exchangeCol = new TableColumn("exchange");
        exchangeCol.setMinWidth(300);
        exchangeCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("firstName"));

        TableColumn cointagCol = new TableColumn("cointag");
        cointagCol.setMinWidth(300);
        cointagCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("lastName"));

        TableColumn balanceCol = new TableColumn("balance");
        balanceCol.setMinWidth(300);
        balanceCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("email")
        );

        TableColumn availableCol = new TableColumn("available");
        availableCol.setMinWidth(300);
        availableCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("available")
        );

        table.setItems(data);
        table.getColumns().addAll(exchangeCol, cointagCol, balanceCol, availableCol);

        grid.add(table, 0, 0);

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

    /**
     * Deze methoden vult de observelijsts
     *
     * @throws SQLException sql error afvangen
     */
    private void fillDataObservableList() throws SQLException {

        //vraag alle balance op
        String sqlStament = "SELECT "
                + "exchangeLijst.handelsplaats, vieuwbalance.cointag, vieuwbalance.balance, "
                + "vieuwbalance.available, vieuwbalance.pending "
                + "FROM vieuwbalance "
                + "INNER JOIN exchangeLijst ON vieuwbalance.exchangeId = exchangeLijst.idExchangeLijst "
                + "WHERE vieuwbalance.balance  != 0 AND  vieuwbalance.available  != 0";

        //vraag alle balance data op waar het balance niet 0 is
        ResultSet rs = clientserver.ClientServer.mysql.mysqlSelect(sqlStament);

        //kijk hoeveel rows er in de resultset zit
        int nummer = clientserver.ClientServer.mysql.rsSize(rs);

        //als het nummer 0 is stop de methoden met een bericht
        if (nummer == 0) {

            //bericht
            ConsoleColor.out("Er is geen andere data balance dat 0 is");

            //stop de methoden
            return;
        } else {

            //update boolean
            balanceAvailable = true;
        }

        //zet de result zet weer goed
        rs.beforeFirst();

        //loop door de result zet heen
        while (rs.next()) {

            //haal de data uit de resultset
            String exchangeNaam = rs.getString("handelsplaats");
            String cointag = rs.getString("cointag");
            String balanceString = "" + rs.getDouble("balance");
            String availableString = "" + rs.getDouble("available");

            data.add(new Person(exchangeNaam, cointag, balanceString, availableString));
        }

    }

    /**
     * Person static classes
     */
    public static class Person {

        private final SimpleStringProperty exchange;
        private final SimpleStringProperty cointag;
        private final SimpleStringProperty email;
        private final SimpleStringProperty available;

        /**
         * Constructor
         *
         * @param exchange exchange naam
         * @param cointag coin tag
         * @param balance hoeveel balance
         * @param available beschikbare balance
         */
        private Person(String exchange, String cointag, String balance, String available) {
            this.exchange = new SimpleStringProperty(exchange);
            this.cointag = new SimpleStringProperty(cointag);
            this.email = new SimpleStringProperty(balance);
            this.available = new SimpleStringProperty(available);
        }

        public String getFirstName() {
            return exchange.get();
        }

        public void setFirstName(String sExchange) {
            exchange.set(sExchange);
        }

        public String getLastName() {
            return cointag.get();
        }

        public void setLastName(String sCointag) {
            cointag.set(sCointag);
        }

        public String getEmail() {
            return email.get();
        }

        public void setEmail(String sBalance) {
            email.set(sBalance);
        }

        public String getAvailable() {
            return available.get();
        }

        public void setAvailable(String sAvailable) {
            available.set(sAvailable);
        }
    }

}
