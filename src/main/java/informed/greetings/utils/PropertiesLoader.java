package informed.greetings.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class PropertiesLoader {
	public static Properties loadPropertiesFile(File propertiesFile) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(propertiesFile));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return prop;
	}

	public static void storeKeyValuePairWithOverride(File propertiesFile, String key, String value)
			throws ConfigurationException {

		Properties prop = loadPropertiesFile(propertiesFile);

		List<Object> keys = new ArrayList<Object>(prop.keySet());
		PropertiesConfiguration config = new PropertiesConfiguration(propertiesFile);

		if (!keys.contains(key)) {
			config.setProperty(key, value);
			config.save();
		} else if (keys.contains(key) && (!prop.getProperty(key).equals(value))) {
			config.clearProperty(key);
			config.setProperty(key, value);
			config.save();
		}
	}

	public static String getSessionUrl() {
		String result = "";
		try {
			Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
			Transferable contents = cb.getContents(null);
			boolean hasStringText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
			if (hasStringText) {
				try {
					result = (String) contents.getTransferData(DataFlavor.stringFlavor);
				} catch (Exception ex) {
					System.out.println(ex);
					ex.printStackTrace();
				}
			}
		} catch (Exception e) {
		}
		return result;
	}

	public static void cleanPropertiesFile(File propertiesFile) throws ConfigurationException {
		PropertiesConfiguration config = new PropertiesConfiguration(propertiesFile);
		config.clear();
		config.save();
	}
	
	public static void copyToClipboard(String val) {
		StringSelection selection = new StringSelection(val);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
	}
	
	public static void loadEnvironmentVariables() throws IOException {
		Properties props = new Properties(System.getProperties());
		InputStream resourceAsStream = PropertiesLoader.class.getClassLoader().getResourceAsStream("environment.properties");
		props.load(resourceAsStream);
		
//		We can use below code also
//		ClassLoader classLoader = PropertiesLoader.class.getClassLoader();
//		File file = new File(classLoader.getResource("environment.properties").getFile());
//		props.load(new FileReader(file));
		System.setProperties(props);
		props.forEach((k, v) -> System.out.println("oooooo   "+k + ":" + v));
	}

//	public static void loadEnvironmentVariablesToSystem() throws IOException {
//		Properties props = new Properties();
//		InputStream resourceAsStream = PropertiesLoader.class.getClassLoader().getResourceAsStream("environment.properties");
//		props.load(resourceAsStream);
//		System.setProperties(props);
//	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		Properties props = new Properties(System.getProperties());
		ClassLoader classLoader = PropertiesLoader.class.getClassLoader();
		File file = new File(classLoader.getResource("environment.properties").getFile());
		props.load(new FileReader(file));
		System.out.println("JJJJJ: "+file.getAbsolutePath());
	}
}