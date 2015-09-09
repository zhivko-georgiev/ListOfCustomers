package bg.sirma.listOfCustomers.models;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import bg.sirma.listOfCustomers.utils.DateUtil;
import bg.sirma.listOfCustomers.utils.LocalDateAdapter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customer {

	private final SimpleStringProperty name;
	private final SimpleObjectProperty<City> town;
	private final SimpleObjectProperty<LocalDate> contractSignDate;
	private final SimpleStringProperty notes;
	private final SimpleStringProperty contract;
	private final SimpleStringProperty logo;

	public Customer(String name, String... params) {
		this.name = new SimpleStringProperty(name);

		City town = params.length > 0 ? City.valueOf(params[0]) : null;
		String contractSignDate = params.length > 1 ? params[1] : LocalDate.now().toString();
		String notes = params.length > 2 ? params[2] : null;
		String contract = params.length > 3 ? params[3] : null;
		String logo = params.length > 4 ? params[4] : null;

		this.town = new SimpleObjectProperty<City>(town);
		this.contractSignDate = new SimpleObjectProperty<LocalDate>(DateUtil.parse(contractSignDate));
		this.notes = new SimpleStringProperty(notes);
		this.contract = new SimpleStringProperty(contract);
		this.logo = new SimpleStringProperty(logo);
	}
	
	public Customer() {
		this(null);
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
		return contract.get();
	}

	public void setContract(String contract) {
		this.contract.set(contract);
	}

	public SimpleStringProperty contractProperty() {
		return contract;
	}

	public String getLogo() {
		return logo.get();
	}

	public void setLogo(String logo) {
		this.logo.set(logo);
	}

	public SimpleStringProperty logoProperty() {
		return logo;
	}
}