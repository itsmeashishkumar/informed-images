package informed.images.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

public class ExcelReader {

	public static HashMap<String, String> readExcelWithFileSheetName(String fileName, String sheetName) {
		HashMap<String, String> testData = new HashMap<>();
		testData.clear();
		try {
			String keyName = "";
			String value = "";
			String pathName = Paths.get(System.getProperty("user.dir")).getParent() + "\\src\\test\\resources\\"
					+ fileName;
			FileInputStream fis = new FileInputStream(new File(pathName));

			// creating workbook instance that refers to .xls file
			HSSFWorkbook wb = new HSSFWorkbook(fis);

			// creating a Sheet object to retrieve the object
			HSSFSheet sheet = wb.getSheet(sheetName);
			FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();

			for (Row row : sheet) // iteration over row using for each loop
			{
				int rowcounter = 1;
				int counter = 0;

				for (Cell cell : row) // iteration over cell using for each loop
				{
					if (counter == 0) {

						try {
							keyName = cell.getStringCellValue();
							if (cell.getStringCellValue().trim().isEmpty())
								break;
						} catch (Exception e) {
							if (Double.toString(cell.getNumericCellValue()).isEmpty())
								break;
							keyName = Double.toString(cell.getNumericCellValue());
						}
					}
					if (counter == 1) {
						try {
							value = cell.getStringCellValue();
						} catch (Exception e) {
							value = Double.toString(cell.getNumericCellValue());
						}
					}
					counter = counter + 1;
					
				}
				System.out.print("KeyName is: '" + keyName + "', and the Value is: '" + value + "'\n");
				testData.put(keyName, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return testData;
	}
}
