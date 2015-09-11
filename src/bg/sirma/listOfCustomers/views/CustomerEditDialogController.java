package bg.sirma.listOfCustomers.views;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import bg.sirma.listOfCustomers.models.City;
import bg.sirma.listOfCustomers.models.Customer;
import bg.sirma.listOfCustomers.utils.DateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CustomerEditDialogController {
	@FXML
	private TextField nameField;
	@FXML
	private ComboBox<City> townField;
	@FXML
	private TextField contractSignDateField;
	@FXML
	private TextArea notesField;
	@FXML
	private TextField contractField;
	@FXML
	private Button logoFileChooserButton;
	@FXML
	private ImageView customerEditDialogLogo;
	

	private Stage dialogStage;
	private Customer customer;
	private boolean okClicked = false;

	@FXML
	private void initialize() {
		ObservableList<City> cities = FXCollections.observableArrayList(City.values());
		townField.setItems(cities);

		townField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				String keyPressed = ke.getText();
				switch (keyPressed) {
				case "1":
					townField.getSelectionModel().select(City.�����);
					break;
				case "2":
					townField.getSelectionModel().select(City.�������);
					break;
				case "3":
					townField.getSelectionModel().select(City.�����);
					break;
				case "4":
					townField.getSelectionModel().select(City.������);
					break;
				default:
					break;
				}
			}
		});

		logoFileChooserButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				handleChooseLogoFile();
			}
		});
		
		customerEditDialogLogo.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					String imageUrl = customer.getLogo().replace("file:", "");
					Desktop.getDesktop().open(new File(imageUrl));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		if (customer.getLogo() != null) {
			customerEditDialogLogo.setImage(new Image(customer.getLogo()));
		} else {
			customerEditDialogLogo.setImage(new Image("file:resources/images/No-Logo-Available.png"));
		}
		
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
			customer.setLogo(logoFileChooserButton.getText());
			


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
		if (townField.getSelectionModel().getSelectedItem().toString() == null
				|| townField.getSelectionModel().getSelectedItem().toString().length() == 0) {
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

		if (logoFileChooserButton.getText() == null || logoFileChooserButton.getText().length() == 0) {
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

	@FXML
	private void handleChooseLogoFile() {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.jpg,*.png, etc.)",
				"*.jpg", "*.png", "*.jpeg");
		fileChooser.getExtensionFilters().add(extFilter);

		File file = fileChooser.showOpenDialog(dialogStage);

		if (file != null) {
			customerEditDialogLogo.setImage(new Image(file.toURI().toString()));
			logoFileChooserButton.setText(file.toURI().toString());
			logoFileChooserButton.setVisible(false);
		}
	}
}