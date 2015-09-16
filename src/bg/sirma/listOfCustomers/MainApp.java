package bg.sirma.listOfCustomers;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import bg.sirma.listOfCustomers.models.Customer;
import bg.sirma.listOfCustomers.models.CustomerListWrapper;
import bg.sirma.listOfCustomers.utils.AlertUtil;
import bg.sirma.listOfCustomers.utils.CollectionsUtil;
import bg.sirma.listOfCustomers.utils.FileUtil;
import bg.sirma.listOfCustomers.views.CustomerEditDialogController;
import bg.sirma.listOfCustomers.views.CustomerOverviewController;
import bg.sirma.listOfCustomers.views.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {
	private static final String APP_TITLE = "Списък с Клиенти";
	private static final String EDIT_DIALOG_TITLE = "Редактирай Клиент";

	private Stage primaryStage;
	private BorderPane rootLayout;

	private ObservableList<Customer> customersData = FXCollections.observableArrayList();

	public ObservableList<Customer> getCustomerData() {
		return customersData;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(APP_TITLE);

		this.primaryStage.getIcons().add(new Image("file:resources/images/User-Group-icon.png"));

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

		File file = getCustomerFilePath();
		if (file != null) {
			loadCustomerDataFromFile(file);
		}
	}

	public void showCustomerOverview() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("views/CustomerOverview.fxml"));
			AnchorPane customerOverview = (AnchorPane) loader.load();

			rootLayout.setCenter(customerOverview);

			CustomerOverviewController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean showCustomerEditDialog(Customer customer) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("views/CustomerEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle(EDIT_DIALOG_TITLE);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			CustomerEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setCustomer(customer);

			dialogStage.getIcons().add(new Image("file:resources/images/edit-icon.png"));

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public File getCustomerFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("filePath", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	public void setCustomerFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());

			primaryStage.setTitle(APP_TITLE + " - " + file.getName());
		} else {
			prefs.remove("filePath");

			primaryStage.setTitle(APP_TITLE);
		}
	}

	public void loadCustomerDataFromFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(CustomerListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			CustomerListWrapper wrapper = (CustomerListWrapper) um.unmarshal(file);

			customersData.clear();
			customersData.addAll(wrapper.getCustomers());
			
			for (Customer customer : customersData) {
				if (customer.getContract() != null) {
					CollectionsUtil.getContractsmap().put(customer.getContract(), FileUtil.getContractName(customer.getContract()));
				}
				
				CollectionsUtil.getNamesset().add(customer.getName());
			}

			setCustomerFilePath(file);

		} catch (Exception e) {
			String errorMessage = "Не може да се зареди информация от файл.";
			AlertUtil.errorAlertFile(errorMessage, file.getPath());
		}
	}

	public void saveCustomerDataToFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(CustomerListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			CustomerListWrapper wrapper = new CustomerListWrapper();
			wrapper.setCustomers(customersData);

			m.marshal(wrapper, file);

			setCustomerFilePath(file);
		} catch (Exception e) { 
			String errorMessage = "Не може да се запише информация във файл.";
			AlertUtil.errorAlertFile(errorMessage, file.getPath());
		}
	}
}