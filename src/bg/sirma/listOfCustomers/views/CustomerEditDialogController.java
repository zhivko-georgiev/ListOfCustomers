package bg.sirma.listOfCustomers.views;

import java.awt.Choice;

import bg.sirma.listOfCustomers.models.City;
import bg.sirma.listOfCustomers.models.Customer;
import bg.sirma.listOfCustomers.utils.DateUtil;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class CustomerEditDialogController {
    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<City> townField;
    @FXML
    private TextField contractSignDateField;
    @FXML
    private TextField notesField;
    @FXML
    private TextField contractField;
    @FXML
    private TextField logoField;

    private Stage dialogStage;
    private Customer customer;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
    	ObservableList<City> cities = FXCollections.observableArrayList(City.values());
    	townField.setItems(cities);
    	
    	townField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                String keyPressed = ke.getText();
                switch (keyPressed) {
				case "1":
					townField.getSelectionModel().select(City.София);
					break;
				case "2":
					townField.getSelectionModel().select(City.Пловдив);
					break;
				case "3":
					townField.getSelectionModel().select(City.Варна);
					break;
				case "4":
					townField.getSelectionModel().select(City.Бургас);
					break;
				default:
					break;
				}
            }
        });
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;

        nameField.setText(customer.getName());
        townField.getSelectionModel().select(customer.getTown());
        contractSignDateField.setText(DateUtil.format(customer.getContractSignDate()));
        notesField.setText(customer.getNotes());
        contractField.setText(customer.getContract());
        logoField.setText(customer.getLogo());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            customer.setName(nameField.getText());
            customer.setTown(townField.getSelectionModel().getSelectedItem());
            customer.setContractSignDate(DateUtil.parse(contractSignDateField.getText()));
            customer.setNotes(notesField.getText());
            customer.setContract(contractField.getText());
            customer.setLogo(logoField.getText());
            
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "No valid name!\n"; 
        }
        if (townField.getSelectionModel().getSelectedItem().toString() == null || townField.getSelectionModel().getSelectedItem().toString().length() == 0) {
            errorMessage += "No valid town name!\n"; 
        }
        if (contractSignDateField.getText() == null || contractSignDateField.getText().length() == 0) {
            errorMessage += "No valid contract sign date!\n"; 
        } else {
            if (!DateUtil.validDate(contractSignDateField.getText())) {
                errorMessage += "No valid contract sign date. Use the format dd.mm.yyyy!\n";
            }
        }
    
        if (notesField.getText() == null || notesField.getText().length() == 0) {
            errorMessage += "No valid notes!\n"; 
        } 

        if (contractField.getText() == null || contractField.getText().length() == 0) {
            errorMessage += "No valid contract file path!\n"; 
        }
        
        if (logoField.getText() == null || logoField.getText().length() == 0) {
            errorMessage += "No valid logo file path!\n"; 
        }


        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            
            alert.showAndWait();
            
            return false;
        }
    }
}