package bg.sirma.listOfCustomers.views;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import bg.sirma.listOfCustomers.models.City;
import bg.sirma.listOfCustomers.models.Customer;
import bg.sirma.listOfCustomers.utils.AlertUtil;
import bg.sirma.listOfCustomers.utils.DateUtil;
import bg.sirma.listOfCustomers.utils.FileUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CustomerEditDialogController {
	private static final String NO_LOGO_IMAGE = "file:resources/images/No-Logo-Available.png";

	@FXML
	private TextField nameField;
	@FXML
	private ComboBox<City> townField;
	@FXML
	private TextField contractSignDateField;
	@FXML
	private TextField notesField;
	@FXML
	private Hyperlink contractField;
	@FXML
	private Button contractFileChooserButton;
	@FXML
	private Button logoFileChooserButton;
	@FXML
	private ImageView customerEditDialogLogo;

	private ObservableSet<String> namesSet = FXCollections.observableSet(new HashSet<>());
	private ObservableMap<String, String> contractsMap = FXCollections.observableMap(new HashMap<>());
	
	private Stage dialogStage;
	private Customer customer;
	private boolean okClicked = false;

	@FXML
	private void initialize() {
		ObservableList<City> cities = FXCollections.observableArrayList(City.values());
		townField.setItems(cities);

		townField.setOnKeyPressed((event) -> {
			String keyPressed = event.getText();
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
		});

		logoFileChooserButton.setOnMouseClicked((event) -> {
			handleChooseLogoFile();
		});

		contractFileChooserButton.setOnMouseClicked((event) -> {
			handleChooseContractFile();
		});

		customerEditDialogLogo.setOnMouseClicked((event) -> {
			try {
				String imageUrl = FileUtil.parseFilePath(customer.getLogo());
				String noLogoUrl = FileUtil.parseFilePath(NO_LOGO_IMAGE);

				if (FileUtil.fileExists(customer.getLogo())) {
					if (!imageUrl.contains("No-Logo-Available.png")) {
						Desktop.getDesktop().open(new File(imageUrl));
					} else {
						Desktop.getDesktop().open(new File(noLogoUrl));
					}
				} else {
					Desktop.getDesktop().open(new File(noLogoUrl));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		contractField.setOnAction((event) -> {
			try {
				String contract = customer.getContract();
				String contractFilePath = contractsMap.get(contract);
				System.out.println(contractsMap);

				System.out.println(contract);
				System.out.println(contractFilePath);
				System.out.println(FileUtil.fileExists(contractFilePath));

				if (FileUtil.fileExists(contractFilePath)) {
					Desktop.getDesktop().open(new File(contractFilePath));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		if (customer.getContract() != null) {
			contractFileChooserButton.setVisible(false);
		}
		contractField.setText(customer.getContract());

		if (customer.getLogo() != null && FileUtil.fileExists(customer.getLogo())) {
			customerEditDialogLogo.setImage(new Image(customer.getLogo()));
		} else {
			customerEditDialogLogo.setImage(new Image(NO_LOGO_IMAGE));
		}
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() {
		if (isInputValid()) {
			customer.setName(nameField.getText());
			namesSet.add(nameField.getText());

			if (townField.getSelectionModel() != null) {
				customer.setTown(townField.getSelectionModel().getSelectedItem());
			}

			if (contractSignDateField.getText() != null) {
				customer.setContractSignDate(DateUtil.parse(contractSignDateField.getText()));
			}

			if (notesField.getText() != null) {
				customer.setNotes(notesField.getText());
			}

			if (contractField.getText() != null) {
				customer.setContract(contractField.getText());
			}

			if (!logoFileChooserButton.getText().equalsIgnoreCase("Избери Лого")) {
				customer.setLogo(logoFileChooserButton.getText());
			}

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

		String name = nameField.getText();
		String notes = notesField.getText();
		String contractSignDate = contractSignDateField.getText();

		// Name Validation
		if (name == null || name.length() == 0) {
			errorMessage += "Име е задължително поле!\n";

			AlertUtil.errorAlertEditCustomer(errorMessage, dialogStage);
			return false;
		}

		if (namesSet.contains(name)) {
			errorMessage += "Това име вече е добавено в списъка - " + name + "\n";
		}

		if (name.length() > 50) {
			errorMessage += "Име трябва да е с дължина до 50 символа!\n";
		}

		// Notes Validation
		if (notes != null && notes.length() > 2000) {
			errorMessage += "Бележки трябва да е с дължина до 2000 символа!\n";
		}

		// Date of Signing the contract Validation
		if (contractSignDate != null && !DateUtil.validDate(contractSignDate) && !contractSignDate.equals("")) {
			errorMessage += "Невалидна дата. Използвайте формат дд.мм.гггг!\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			AlertUtil.errorAlertEditCustomer(errorMessage, dialogStage);

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

	@FXML
	private void handleChooseContractFile() {
		FileChooser fileChooser = new FileChooser();

		File file = fileChooser.showOpenDialog(dialogStage);

		if (file != null) {
			String contractName = FileUtil.getContractName(file.toURI().toString());
			String contractFilePath = FileUtil.parseFilePath(file.toURI().toString());

			this.contractsMap.put(contractName, contractFilePath);

			contractField.setText(contractName);
			contractField.setAlignment(Pos.CENTER);
			contractFileChooserButton.setVisible(false);
		}
	}
}