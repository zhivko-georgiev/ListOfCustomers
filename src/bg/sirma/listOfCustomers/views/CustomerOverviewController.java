package bg.sirma.listOfCustomers.views;

import java.time.LocalDate;

import bg.sirma.listOfCustomers.MainApp;
import bg.sirma.listOfCustomers.models.City;
import bg.sirma.listOfCustomers.models.Customer;
import bg.sirma.listOfCustomers.utils.AlertUtil;
import bg.sirma.listOfCustomers.utils.CollectionsUtil;
import bg.sirma.listOfCustomers.utils.DateUtil;
import bg.sirma.listOfCustomers.utils.FileUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CustomerOverviewController {
	private static final String NO_LOGO_IMAGE = "file:resources/images/No-Logo-Available.png";

	private MainApp mainApp;

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

	@FXML
	private void initialize() {
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		townColumn.setCellValueFactory(cellData -> cellData.getValue().townProperty());
		contractSignDateColumn.setCellValueFactory(cellData -> cellData.getValue().contractSignDateProperty());
		notesColumn.setCellValueFactory(cellData -> cellData.getValue().notesProperty());

		showCustomerDetails(null);

		customerTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showCustomerDetails(newValue));
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		customerTable.setItems(mainApp.getCustomerData());
	}

	private void showCustomerDetails(Customer customer) {
		if (customer != null) {
			nameLabel.setText(customer.getName());

			if (customer.getTown() != null) {
				townLabel.setText(customer.getTown().toString());
			} else {
				townLabel.setText("");
			}

			if (customer.getContractSignDate() != null) {
				contractSignDateLabel.setText(DateUtil.format(customer.getContractSignDate()));
			} else {
				contractSignDateLabel.setText("");
			}

			if (customer.getNotes() != null) {
				notesLabel.setText(customer.getNotes());
			} else {
				notesLabel.setText("");
			}

			if (customer.getContract() != null) {
				contractLabel.setText(CollectionsUtil.getContractsmap().get(customer.getContract()));
			} else {
				contractLabel.setText("");
			}

			if (customer.getLogo() != null && FileUtil.fileExists(customer.getLogo())) {
				logoImage.setImage(new Image(customer.getLogo()));
			} else {
				logoImage.setImage(new Image(NO_LOGO_IMAGE));
			}
		}
	}

	@FXML
	private void handleDeleteCustomer() {
		int selectedIndex = customerTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			CollectionsUtil.getNamesset()
					.remove(customerTable.getSelectionModel().getSelectedItem().getName().toLowerCase());
			CollectionsUtil.getContractsmap().remove(customerTable.getSelectionModel().getSelectedItem().getContract());
			customerTable.getItems().remove(selectedIndex);
		} else {
			AlertUtil.warningAlertNoCustomerSelected(mainApp.getPrimaryStage());
		}
	}

	@FXML
	private void handleNewCustomer() {
		Customer tempCustomer = new Customer(null, null, null, null, null, null);
		boolean okClicked = mainApp.showCustomerEditDialog(tempCustomer);
		if (okClicked) {
			mainApp.getCustomerData().add(tempCustomer);
		}
	}

	@FXML
	private void handleEditCustomer() {
		Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
		if (selectedCustomer != null) {
			CollectionsUtil.getNamesset().remove(selectedCustomer.getName().toLowerCase());
			boolean okClicked = mainApp.showCustomerEditDialog(selectedCustomer);

			if (okClicked) {
				showCustomerDetails(selectedCustomer);
			}
		} else {
			AlertUtil.warningAlertNoCustomerSelected(mainApp.getPrimaryStage());
		}
	}
}