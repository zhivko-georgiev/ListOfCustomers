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
	
	public static String getContractName(String filename) {
		int startIndex = filename.lastIndexOf('/');
		
		String extractedFileName = filename.substring(startIndex + 1 , filename.length());
		
		extractedFileName = extractedFileName.replace("%20", " ");
		
		return extractedFileName;
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
