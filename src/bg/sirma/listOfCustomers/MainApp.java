package bg.sirma.listOfCustomers;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import bg.sirma.listOfCustomers.models.Customer;
import bg.sirma.listOfCustomers.models.CustomerListWrapper;
import bg.sirma.listOfCustomers.views.CustomerEditDialogController;
import bg.sirma.listOfCustomers.views.CustomerOverviewController;
import bg.sirma.listOfCustomers.views.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	private ObservableList<Customer> customersData = FXCollections.observableArrayList();

	public MainApp() {
	}

	public ObservableList<Customer> getCustomerData() {
		return customersData;
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Списък с клиенти - Desktop App");

		this.primaryStage.getIcons().add(new Image("file:resources/images/address_book_32.png"));

		initRootLayout();

		showCustomerOverview();
	}

	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("views/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);

			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Try to load last opened person file.
		File file = getCustomerFilePath();
		if (file != null) {
			loadCustomerDataFromFile(file);
		}
	}

	/**
	 * Shows the person overview inside the root layout.
	 */
	public void showCustomerOverview() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("views/CustomerOverview.fxml"));
			AnchorPane customerOverview = (AnchorPane) loader.load();

			// Set person overview into the center of root layout.
			rootLayout.setCenter(customerOverview);

			// Give the controller access to the main app.
			CustomerOverviewController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens a dialog to edit details for the specified person. If the user
	 * clicks OK, the changes are saved into the provided person object and true
	 * is returned.
	 * 
	 * @param customer
	 *            the person object to be edited
	 * @return true if the user clicked OK, false otherwise.
	 */
	public boolean showCustomerEditDialog(Customer customer) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("views/CustomerEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Редактирай Клиент");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			CustomerEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setCustomer(customer);

			// Set the dialog icon.
			dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Returns the person file preference, i.e. the file that was last opened.
	 * The preference is read from the OS specific registry. If no such
	 * preference can be found, null is returned.
	 * 
	 * @return
	 */
	public File getCustomerFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("filePath", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	/**
	 * Sets the file path of the currently loaded file. The path is persisted in
	 * the OS specific registry.
	 * 
	 * @param file
	 *            the file or null to remove the path
	 */
	public void setCustomerFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());

			// Update the stage title.
			primaryStage.setTitle("Списък с клиенти - " + file.getName());
		} else {
			prefs.remove("filePath");

			// Update the stage title.
			primaryStage.setTitle("Списък с клиенти - Desktop App");
		}
	}

	/**
	 * Loads person data from the specified file. The current person data will
	 * be replaced.
	 * 
	 * @param file
	 */
	public void loadCustomerDataFromFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(CustomerListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			// Reading XML from the file and unmarshalling.
			CustomerListWrapper wrapper = (CustomerListWrapper) um.unmarshal(file);

			customersData.clear();
			customersData.addAll(wrapper.getCustomers());

			// Save the file path to the registry.
			setCustomerFilePath(file);

		} catch (Exception e) { // catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n" + file.getPath());

			alert.showAndWait();
		}
	}

	/**
	 * Saves the current person data to the specified file.
	 * 
	 * @param file
	 */
	public void saveCustomerDataToFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(CustomerListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Wrapping our person data.
			CustomerListWrapper wrapper = new CustomerListWrapper();
			wrapper.setCustomers(customersData);

			// Marshalling and saving XML to the file.
			m.marshal(wrapper, file);

			// Save the file path to the registry.
			setCustomerFilePath(file);
		} catch (Exception e) { // catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + file.getPath());

			alert.showAndWait();
		}
	}

	/**
	 * Returns the main stage.
	 * 
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}