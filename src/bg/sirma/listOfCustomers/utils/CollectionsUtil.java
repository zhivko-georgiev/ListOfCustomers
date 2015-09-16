package bg.sirma.listOfCustomers.utils;

import java.util.HashMap;
import java.util.HashSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;

public class CollectionsUtil {
	private static final ObservableSet<String> namesSet = FXCollections.observableSet(new HashSet<String>());
	private static final ObservableMap<String, String> contractsMap = FXCollections.observableMap(new HashMap<>());

	public static ObservableSet<String> getNamesset() {
		return namesSet;
	}

	public static ObservableMap<String, String> getContractsmap() {
		return contractsMap;
	}
}
