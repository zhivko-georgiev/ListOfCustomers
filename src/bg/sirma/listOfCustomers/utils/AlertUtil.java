package bg.sirma.listOfCustomers.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AlertUtil {
	public static void errorAlertFile(String message, String filepath) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Грешка");
		alert.setHeaderText(message);
		alert.setContentText(message + ":\n" + filepath);

		alert.showAndWait();
	}
	
	public static void errorAlertEditCustomer(String errorMessage, Stage stage) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(stage);
		alert.setTitle("Невалидни Полета");
		alert.setHeaderText("Моля коригирайте невалидните полета.");
		alert.setContentText(errorMessage);

		alert.showAndWait();
	}
	
	public static void warningAlertNoCustomerSelected(Stage stage) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.initOwner(stage);
        alert.setTitle("Липсва избран клиент.");
        alert.setHeaderText("Липсва избран клиент.");
        alert.setContentText("Моля изберете клиент от таблицата.");
        
        alert.showAndWait();
	}
}
