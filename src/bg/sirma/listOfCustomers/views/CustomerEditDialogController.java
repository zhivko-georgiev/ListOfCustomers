package bg.sirma.listOfCustomers.views;

import bg.sirma.listOfCustomers.models.Customer;
import bg.sirma.listOfCustomers.utils.DateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CustomerEditDialogController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField townField;
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
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;

        nameField.setText(customer.getName());
        townField.setText(customer.getTown());
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
            customer.setTown(townField.getText());
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
        if (townField.getText() == null || townField.getText().length() == 0) {
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