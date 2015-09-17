package bg.sirma.listOfCustomers.models;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import bg.sirma.listOfCustomers.utils.LocalDateAdapter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customer {
	private final SimpleStringProperty name;
	private final SimpleObjectProperty<City> town;
	private final SimpleObjectProperty<LocalDate> contractSignDate;
	private final SimpleStringProperty notes;
	private final SimpleStringProperty contractFilePath;
	private final SimpleStringProperty logoFilePath;

	public Customer(String name, City town, LocalDate contractSignDate, String notes, String contractFilePath, String logoFilePath) {
		this.name = new SimpleStringProperty(name);

		City parsedTown = town != null ? town : null;
		LocalDate parsedContractSignDate = contractSignDate != null ? contractSignDate : null;
		String parsedNotes = notes != null ? notes : null;
		String parsedContractFilePath = contractFilePath != null ? contractFilePath : null;
		String parsedLogoFilePath = logoFilePath != null ? logoFilePath : null;

		this.town = new SimpleObjectProperty<City>(parsedTown);
		this.contractSignDate = new SimpleObjectProperty<LocalDate>(parsedContractSignDate);
		this.notes = new SimpleStringProperty(parsedNotes);
		this.contractFilePath = new SimpleStringProperty(parsedContractFilePath);
		this.logoFilePath = new SimpleStringProperty(parsedLogoFilePath);
	}
	
	public Customer() {
		this(null, null, null, null, null, null);
	}

	public String getName() {
		return name.get();
	}

	public void setName(String firstName) {
		this.name.set(firstName);
	}

	public SimpleStringProperty nameProperty() {
		return name;
	}

	public City getTown() {
		return town.get();
	}

	public void setTown(City town) {
		this.town.set(town);
	}

	public SimpleObjectProperty<City> townProperty() {
		return town;
	}

	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public LocalDate getContractSignDate() {
		return contractSignDate.get();
	}

	public void setContractSignDate(LocalDate contractSignDate) {
		this.contractSignDate.set(contractSignDate);
	}

	public SimpleObjectProperty<LocalDate> contractSignDateProperty() {
		return contractSignDate;
	}

	public String getNotes() {
		return notes.get();
	}

	public void setNotes(String notes) {
		this.notes.set(notes);
	}

	public SimpleStringProperty notesProperty() {
		return notes;
	}

	public String getContract() {
		return contractFilePath.get();
	}

	public void setContract(String contract) {
		this.contractFilePath.set(contract);
	}

	public SimpleStringProperty contractProperty() {
		return contractFilePath;
	}

	public String getLogo() {
		return logoFilePath.get();
	}

	public void setLogo(String logo) {
		this.logoFilePath.set(logo);
	}

	public SimpleStringProperty logoProperty() {
		return logoFilePath;
	}
}