package bg.sirma.listOfCustomers.views;

import java.time.LocalDate;

import bg.sirma.listOfCustomers.MainApp;
import bg.sirma.listOfCustomers.models.City;
import bg.sirma.listOfCustomers.models.Customer;
import bg.sirma.listOfCustomers.utils.DateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CustomerOverviewController {
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, City> townColumn;
    @FXML
    private TableColumn<Customer, LocalDate> contractSignDateColumn;
    @FXML
    private TableColumn<Customer, String> notesColumn;

    @FXML
    private Label nameLabel;
    @FXML
    private Label townLabel;
    @FXML
    private Label contractSignDateLabel;
    @FXML
    private Label notesLabel;
    @FXML
    private Label contractLabel;
    @FXML
    private ImageView logoImage;

    private MainApp mainApp;

    public CustomerOverviewController() {
    }


    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        townColumn.setCellValueFactory(cellData -> cellData.getValue().townProperty());
        contractSignDateColumn.setCellValueFactory(cellData -> cellData.getValue().contractSignDateProperty());
        notesColumn.setCellValueFactory(cellData -> cellData.getValue().notesProperty());
        
        showCustomerDetails(null);

        customerTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showCustomerDetails(newValue));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        customerTable.setItems(mainApp.getCustomerData());
    }
    
    private void showCustomerDetails(Customer customer) {
        if (customer != null) {
            nameLabel.setText(customer.getName());
            townLabel.setText(customer.getTown().toString());
            contractSignDateLabel.setText(DateUtil.format(customer.getContractSignDate()));
            notesLabel.setText(customer.getNotes());
            contractLabel.setText(customer.getContract());
            logoImage.setImage(new Image(customer.getLogo()));
        } else {
            nameLabel.setText("");
            townLabel.setText("");
            contractSignDateLabel.setText("");
            notesLabel.setText("");
            contractLabel.setText("");
            logoImage.setImage(new Image("file:resources/images/No-Logo-Available.png"));
        }
    }
    
    @FXML
    private void handleDeleteCustomer() {
        int selectedIndex = customerTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            customerTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("Please select a customer in the table.");
            
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleNewCustomer() {
        Customer tempCustomer = new Customer(null);
        boolean okClicked = mainApp.showCustomerEditDialog(tempCustomer);
        if (okClicked) {
            mainApp.getCustomerData().add(tempCustomer);
        }
    }

    @FXML
    private void handleEditCustomer() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            boolean okClicked = mainApp.showCustomerEditDialog(selectedCustomer);
            if (okClicked) {
                showCustomerDetails(selectedCustomer);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("Please select a customer in the table.");
            
            alert.showAndWait();
        }
    }
}