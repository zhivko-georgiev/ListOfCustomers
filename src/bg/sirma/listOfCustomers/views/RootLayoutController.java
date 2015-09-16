package bg.sirma.listOfCustomers.views;

import java.io.File;

import bg.sirma.listOfCustomers.MainApp;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

public class RootLayoutController {
	private MainApp mainApp;

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void handleNew() {
		mainApp.getCustomerData().clear();
		mainApp.showCustomerOverview();
		mainApp.setCustomerFilePath(null);
	}

	@FXML
	private void handleOpen() {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

		if (file != null) {
			mainApp.loadCustomerDataFromFile(file);
		}
	}

	@FXML
	private void handleSave() {
		File customerFile = mainApp.getCustomerFilePath();
		if (customerFile != null) {
			mainApp.saveCustomerDataToFile(customerFile);
		} else {
			handleSaveAs();
		}
	}

	@FXML
	private void handleSaveAs() {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

		if (file != null) {
			if (!file.getPath().endsWith(".xml")) {
				file = new File(file.getPath() + ".xml");
			}
			mainApp.saveCustomerDataToFile(file);
		}
	}

	@FXML
	private void handleExit() {
		System.exit(0);
	}
}