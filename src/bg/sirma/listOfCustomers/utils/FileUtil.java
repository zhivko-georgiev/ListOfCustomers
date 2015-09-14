package bg.sirma.listOfCustomers.utils;

import java.io.File;

public class FileUtil {
	public static boolean fileExists(String filepath) {
		if (filepath != null) {
			String trimmedFilePath = trimFilePath(filepath);
			File f = new File(trimmedFilePath);

			if (f.exists() && !f.isDirectory()) {
				return true;
			}

			return false;
		}
		
		return false;
	}
	
	public static String parseFilePath(String filepath) {
		return trimFilePath(filepath);
	}
	
	private static String trimFilePath(String filepath) {
		String trimmedFielPath = filepath.substring(5, filepath.length());
		
		if (trimmedFielPath.startsWith("/")) {
			trimmedFielPath = trimmedFielPath.substring(1, trimmedFielPath.length());
		}
		
		trimmedFielPath = trimmedFielPath.replace("%20", " ");
		
		return trimmedFielPath;
	}
}
