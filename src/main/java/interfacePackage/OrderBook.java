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
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Methoden die het order book terug geeft
 *
 * @author michel
 */
public class OrderBook extends Application {

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
            = FXCollections.observableArrayList(
                    new Person("t", "Jacob", "Smith", "jacob.smith@example.com"),
                    new Person("t", "Isabella", "Johnson", "isabella.johnson@example.com"),
                    new Person("t", "Ethan", "Williams", "ethan.williams@example.com"),
                    new Person("t", "Emma", "Jones", "emma.jones@example.com"),
                    new Person("t", "Michael", "Brown", "michael.brown@example.com")
            );

    /**
     * Constructor
     */
    public OrderBook() {
        
        try {
            fillDataObservableList();
        } catch (SQLException ex) {
            Logger.getLogger(OrderBook.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void start(Stage stage) {
        
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(InterfaceMain.getXas());
        stage.setHeight(InterfaceMain.getYas());

        final Label label = new Label("Address Book");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("firstName"));

        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setMinWidth(100);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("lastName"));

        TableColumn emailCol = new TableColumn("Email");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("email")
        );

        TableColumn availableCol = new TableColumn("Email");
        availableCol.setMinWidth(200);
        availableCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("available")
        );

        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol, availableCol);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
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
            String balanceString = ""+rs.getDouble("balance");
            String availableString = ""+rs.getDouble("available");
            
            data.add(new Person(exchangeNaam, cointag, balanceString, availableString));
        }

        
    }

    /**
     * Person static classes
     */
    public static class Person {

        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName2;
        private final SimpleStringProperty email;
        private final SimpleStringProperty available;

        private Person(String fName, String lName, String email, String avaiableString) {
            this.firstName = new SimpleStringProperty(fName);
            this.lastName2 = new SimpleStringProperty(lName);
            this.email = new SimpleStringProperty(email);
            this.available = new SimpleStringProperty(avaiableString);
        }

        public String getFirstName() {
            return firstName.get();
        }

        public void setFirstName(String fName) {
            firstName.set(fName);
        }

        public String getLastName() {
            return lastName2.get();
        }

        public void setLastName(String fName) {
            lastName2.set(fName);
        }

        public String getEmail() {
            return email.get();
        }

        public void setEmail(String fName) {
            email.set(fName);
        }

        public String getAvailable() {
            return available.get();
        }

        public void setAvailable(String avaiableString) {
            available.set(avaiableString);
        }
    }

}
