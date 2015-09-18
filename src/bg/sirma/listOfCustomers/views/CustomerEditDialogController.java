package bg.sirma.listOfCustomers.views;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Pattern;

import bg.sirma.listOfCustomers.models.City;
import bg.sirma.listOfCustomers.models.Customer;
import bg.sirma.listOfCustomers.utils.AlertUtil;
import bg.sirma.listOfCustomers.utils.CollectionsUtil;
import bg.sirma.listOfCustomers.utils.DateUtil;
import bg.sirma.listOfCustomers.utils.FileUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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
	private TextArea notesField;
	@FXML
	private Hyperlink contractField;
	@FXML
	private Button contractFileChooserBtn;
	@FXML
	private Button logoFileChooserBtn;
	@FXML
	private ImageView customerEditDialogLogo;
	@FXML
	private Button removeCurrentContractBtn;

	private Stage dialogStage;
	private Customer customer;
	private boolean okClicked = false;

	@FXML
	private void initialize() {
		contractSignDateField.focusedProperty().addListener(listener -> {
			if (contractSignDateField.getText() != null && contractSignDateField.getText() != "") {
				String[] splittedDate = contractSignDateField.getText().split(Pattern.quote("."));
				if (splittedDate.length != 0) {
					int day = Integer.parseInt(splittedDate[0]);
					int month = Integer.parseInt(splittedDate[1]);

					String finalDate = "";

					if (day >= 1 && day <= 9) {
						finalDate += "0" + day + ".";
					} else {
						finalDate += day + ".";
					}

					if (month >= 1 && month <= 9) {
						finalDate += "0" + month + ".";
					} else {
						finalDate += month + ".";
					}
					
					if (splittedDate.length > 2) {
						int year = Integer.parseInt(splittedDate[2]);
						finalDate += year;
					} else {
						int currentYear = LocalDate.now().getYear();
						finalDate += currentYear;
					}

					if (DateUtil.validDate(finalDate)) {
						contractSignDateField.setText(finalDate);
						customer.setContractSignDate(DateUtil.parse(finalDate));
					} else {
						contractSignDateField.setText("");
						AlertUtil.errorAlertEditCustomer("Невалидна дата. Използвайте формат дд.мм.гггг!\n", dialogStage);
					}
				}
			}
		});
		
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

		logoFileChooserBtn.setOnMouseClicked((event) -> {
			handleChooseLogoFile();
		});

		contractFileChooserBtn.setOnMouseClicked((event) -> {
			handleChooseContractFile();
		});

		customerEditDialogLogo.setOnMouseClicked((event) -> {
			try {
				String noLogoUrl = FileUtil.parseFilePath(NO_LOGO_IMAGE);

				if (customer.getLogo() != null) {
					String imageUrl = FileUtil.parseFilePath(customer.getLogo());

					if (FileUtil.fileExists(customer.getLogo())) {
						if (!imageUrl.contains("No-Logo-Available.png")) {
							Desktop.getDesktop().open(new File(imageUrl));
						} else {
							Desktop.getDesktop().open(new File(noLogoUrl));
						}
					} else {
						Desktop.getDesktop().open(new File(noLogoUrl));
					}
				} else {
					Desktop.getDesktop().open(new File(noLogoUrl));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		removeCurrentContractBtn.setVisible(false);

		contractField.setOnAction((event) -> {
			try {
				String contractFilePath = customer.getContract();
				
				if (FileUtil.fileExists(contractFilePath)) {
					Desktop.getDesktop().open(new File(contractFilePath));
				} else {
					throw new IOException();
				}

			} catch (IOException e) {
				AlertUtil.errorAlertFile("Файлът не съществува", customer.getContract());
				removeCurrentContract();
				e.printStackTrace();
			}
		});

		contractSignDateField.setOnKeyPressed((event) -> {
			if (contractSignDateField.getText() != null) {
				if (event.getCode() == KeyCode.DOWN && event.isControlDown()) {

					customer.setContractSignDate(DateUtil.parse(contractSignDateField.getText()).minusDays(1));
					contractSignDateField.setText(DateUtil.format(customer.getContractSignDate()));

				} else if (event.getCode() == KeyCode.UP && event.isControlDown()) {
					customer.setContractSignDate(DateUtil.parse(contractSignDateField.getText()).plusDays(1));
					contractSignDateField.setText(DateUtil.format(customer.getContractSignDate()));
				}
			}
		});

		removeCurrentContractBtn.setOnMouseClicked((event) -> {
			removeCurrentContract();
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
			contractFileChooserBtn.setVisible(false);
			removeCurrentContractBtn.setVisible(true);
		}
		contractField.setText(CollectionsUtil.getContractsmap().get(customer.getContract()));

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

			if (townField.getSelectionModel() != null) {
				customer.setTown(townField.getSelectionModel().getSelectedItem());
			}

			if (contractSignDateField.getText() != null) {
				customer.setContractSignDate(DateUtil.parse(contractSignDateField.getText()));
			}

			if (notesField.getText() != null) {
				customer.setNotes(notesField.getText());
			}

			// if (contractField.getText() != null) {
			// customer.setContract(contractField.getText());
			// }

			if (customer.getLogo() == null) {
				customer.setLogo(NO_LOGO_IMAGE);
			}

			okClicked = true;
			CollectionsUtil.getNamesset().add(nameField.getText().toLowerCase());
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

		if (name == null || name.length() == 0) {
			errorMessage += "Име е задължително поле!\n";

			AlertUtil.errorAlertEditCustomer(errorMessage, dialogStage);
			return false;
		}

		if (CollectionsUtil.getNamesset().contains(name.trim().toLowerCase())) {
			errorMessage += "Това име вече е добавено в списъка - " + name + "\n";
		}

		if (name.length() > 50) {
			errorMessage += "Име трябва да е с дължина до 50 символа!\n";
		}

		if (notes != null && notes.length() > 2000) {
			errorMessage += "Бележки трябва да е с дължина до 2000 символа!\n";
		}

		if (contractSignDate != null && !DateUtil.validDate(contractSignDate) && !contractSignDate.equals("")) {
			errorMessage += "Невалидна дата. Използвайте формат дд.мм.гггг!\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
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
			customer.setLogo(file.toURI().toString());
		}
	}

	@FXML
	private void handleChooseContractFile() {
		FileChooser fileChooser = new FileChooser();

		File file = fileChooser.showOpenDialog(dialogStage);

		if (file != null) {
			String contractName = FileUtil.getContractName(file.toURI().toString());
			String contractFilePath = FileUtil.parseFilePath(file.toURI().toString());

			CollectionsUtil.getContractsmap().put(contractFilePath, contractName);
			customer.setContract(contractFilePath);

			contractField.setText(contractName);
			contractField.setAlignment(Pos.CENTER);

			removeCurrentContractBtn.setVisible(true);
			contractFileChooserBtn.setVisible(false);
		}
	}

	@FXML
	private void removeCurrentContract() {
		customer.setContract(null);
		removeCurrentContractBtn.setVisible(false);
		contractFileChooserBtn.setVisible(true);
	}
}