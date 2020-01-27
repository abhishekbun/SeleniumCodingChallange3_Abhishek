package excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument.List;

/*import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration

import internal.GlobalVariable*/

public class Excel {


	public static String excelFileName="";//GlobalVariable.excelFileName // Global Variable in Katalon
	public static String sheetName; // Global Variable in Katalon

	
	public static void CopySheet() throws Throwable {

		sheetName=createExcel_Sheet(excelFileName, "JP"); // excel sheeted created, for e.g. : QM 13-Nov-18 (Tue) 09;37;03 AM
		//GlobalVariable.sheetName = sheetName

		String copySheetName = "JP_JA_PERF_SHEET";
		//System.out.println("EXCEL FILE NAME: "+excelFileName);


		Excel ces = new Excel();
		List<List<String>> selectedRowDataList = ces.getExcelData(excelFileName, copySheetName, 1, 28);
		ces.copyExcelSheetWithData(excelFileName, selectedRowDataList);


	}

	/* Get the data in excel file. 
	 * Return: 2D String list contains specified row data.
	 * excelFilePath :  The exist excel file path need to copy.
	 * excelSheetName : Which sheet to copy.
	 * startRow : Start row number.
	 * endRow : End row number.
	 * */
	private List<List<String>> getExcelData(String excelFilePath, String excelSheetName, int startRow, int endRow)
	{

		String fileName=excelFilePath;
		//commented knowingly --->fileName=RunConfiguration.getProjectDir()+"\\Data Files\\"+fileName+".xlsx";

		List<List<String>> ret = new ArrayList();

		try{
			/* Open the excel file input stream. */
			FileInputStream fis = new FileInputStream(fileName.trim());

			/* Get excel workbook. */
			Workbook excelWookBook = new XSSFWorkbook(fis);

			/* Get excel sheet by name. */
			Sheet copySheet = excelWookBook.getSheet(excelSheetName);

			int fRowNum = copySheet.getFirstRowNum();
			int lRowNum = copySheet.getLastRowNum();

			/*  First row is excel file header, so read data from row next to it. */
			for(int i=fRowNum+1; i<lRowNum+1; i++)
			{
				/* Only get desired row data. */
				if(i>=startRow && i<=endRow)
				{
					Row row = copySheet.getRow(i);

					int fCellNum = row.getFirstCellNum();
					int lCellNum = row.getLastCellNum();

					/* Loop in row cells, add each cell value to the list.*/
					List<String> rowDataList = new ArrayList<String>();
					for(int j = fCellNum; j < lCellNum; j++)
					{
						String cValue = row.getCell(j).getStringCellValue();
						rowDataList.add(cValue);
					}

					ret.add(rowDataList);
				}
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}

		return ret;
	}


	

	private void copyExcelSheetWithData(String excelFilePath, List<List<String>> dataList)
	{


		try
		{

			String fileName=excelFileName;
			//commented knowingly --->fileName=RunConfiguration.getProjectDir()+"\\Data Files\\"+fileName+".xlsx";
			//System.err.println(fileName);
			//System.out.println(sheetName);
			//System.out.println(fileName);


			FileInputStream file = new FileInputStream (new File(fileName));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet newSheet = workbook.getSheet(sheetName);	// GET SHEET

			// Get workbook sheet iterator.
			Iterator sheetIterator = workbook.iterator();

			// If has existing sheet.
			if(sheetIterator.hasNext())
			{
				newSheet = workbook.getSheet(sheetName); ////////////////////////////GET SHEET //////////////////////////////

				// Select this new cloned sheet.
				newSheet.setSelected(true);

				// Because this is a cloned sheet, should remove all the cloned rows in it.
				Iterator<Row> rowIterator = newSheet.iterator();
				while(rowIterator.hasNext())
				{
					// Get the clone row.
					Row cloneRow = rowIterator.next();

					// Remove the clone row.
					newSheet.removeRow(cloneRow);

					// Because after remove the clone row, the iterator changed,
					// * so get it again, else java.util.ConcurrentModificationException will be thrown .
					rowIterator = newSheet.iterator();
				}
			}

			// Create header row.
			Row headerRow = newSheet.createRow(0);

			// To ADD ---Cell Fore Ground Yellow color
			CellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			//headerRow.setRowStyle(style);

			// To ADD ---FONT Style
			Font font=workbook.createFont();
			font.setBold(true);  // This added to make the bold font
			style.setFont(font);

			style.setBorderBottom(CellStyle.BORDER_MEDIUM);
			style.setBorderTop(CellStyle.BORDER_MEDIUM);
			style.setBorderRight(CellStyle.BORDER_MEDIUM);
			style.setBorderLeft(CellStyle.BORDER_MEDIUM);

			headerRow.createCell(0).setCellValue("Test Steps");
			headerRow.getCell(0).setCellStyle(style); // This will add foreground Yelllow color to cell 0,0

			headerRow.createCell(1).setCellValue("Time Taken in Sec");
			headerRow.getCell(1).setCellStyle(style); // This will add foreground Yelllow color to cell 0,1

			headerRow.createCell(2).setCellValue("Remarks");
			headerRow.getCell(2).setCellStyle(style); // This will add foreground Yelllow color to cell 0,2

			// Loop in the row data list, add each row data into the new sheet.
			if(dataList!=null)
			{
				int size = dataList.size();
				for(int i=0;i<size;i++)
				{
					List<String> cellDataList = dataList.get(i);

					// Create row to save the copied data.
					Row row = newSheet.createRow(i+1);

					int columnNumber = cellDataList.size();

					for(int j=0;j<columnNumber;j++)
					{
						String cellValue = cellDataList.get(j);
						row.createCell(j).setCellValue(cellValue);
					}
				}
			}
			// Autosize Column width
			newSheet.autoSizeColumn(0);
			newSheet.autoSizeColumn(1);
			newSheet.autoSizeColumn(2);

			// Write the new sheet data to excel file
			FileOutputStream fOut = new FileOutputStream(fileName);
			workbook.write(fOut);
			fOut.close();

			System.out.println("All cells from zero index sheet is copied to below sheet path successfully. : ");
			System.err.println(fileName);
			System.out.println("");

		}catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}

	


	
	public static void writeToExcel(String excelFileName, String sheetName,int iRow, int iCell, String iText ) throws Throwable{

		String fileName=excelFileName;
		//commented knowingly --->fileName=RunConfiguration.getProjectDir()+"\\Data Files\\"+fileName+".xlsx";
		//System.err.println(fileName);


		FileInputStream file = new FileInputStream (new File(fileName));
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheet(sheetName);

		/*	sheet.autoSizeColumn(0);
		 sheet.autoSizeColumn(1);
		 sheet.autoSizeColumn(2);*/

		//Write data to excel'
		Row oRow;
		oRow = sheet.getRow(iRow-1);
		if(oRow == null){
			sheet.createRow(iRow-1);
			oRow = sheet.getRow(iRow-1);
		}
		Cell oCell;
		oCell = oRow.getCell(iCell - 1);
		if(oCell == null ){
			oRow.createCell(iCell - 1);
			oCell = oRow.getCell(iCell - 1);
		}
		oCell.setCellValue(iText);

		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);

		FileOutputStream outFile =new FileOutputStream(new File(fileName));
		workbook.write(outFile);
		outFile.close();

		Thread.sleep(500);  // It is important to delay

		System.out.println("Method \"writeToExcel\" successful: " + iText + " Row: " +iRow  + " Column: " +iCell);
	}


	
	public static void setCellData(String excelFileName, String sheetName,int iRow, int iCell, String iText ) throws Throwable{

		String fileName=excelFileName;
		//commented knowingly --->fileName=RunConfiguration.getProjectDir()+'\\' +fileName+".xlsx";
		//System.err.println(fileName);


		FileInputStream file = new FileInputStream (new File(fileName));
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheet(sheetName);

		/*	sheet.autoSizeColumn(0);
		 sheet.autoSizeColumn(1);
		 sheet.autoSizeColumn(2);*/

		//Write data to excel'
		Row oRow;
		oRow = sheet.getRow(iRow-1);
		if(oRow == null){
			sheet.createRow(iRow-1);
			oRow = sheet.getRow(iRow-1);
		}
		Cell oCell;
		oCell = oRow.getCell(iCell - 1);
		if(oCell == null ){
			oRow.createCell(iCell - 1);
			oCell = oRow.getCell(iCell - 1);
		}
		oCell.setCellValue(iText);

		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);

		FileOutputStream outFile =new FileOutputStream(new File(fileName));
		workbook.write(outFile);
		outFile.close();

		Thread.sleep(500);  // It is important to delay

		System.out.println("Method \"writeToExcel\" successful: " + iText + " Row: " +iRow  + " Column: " +iCell);
	}


	public static String createExcel_Sheet(String excelFileName,String sheetName) throws IOException, InvalidFormatException, Throwable
	{
		// ***************************Add a sheet into Existing  workbook***********************************************

		//commented knowingly --->excelFileName=RunConfiguration.getProjectDir()+"\\Data Files\\"+excelFileName+".xlsx";
		//System.err.println("Excel Sheet Path: "+excelFileName);

		Date date = new Date();
		SimpleDateFormat customDate;
		customDate = new SimpleDateFormat("dd-MMM-yy (EEE) hh;mm;ss a");
		String dateOutput = customDate.format(date);
		//System.out.println(dateOutput); // Output: 04-Aug-18 (Sat) 06;37;31 PM
		sheetName = sheetName + " " + dateOutput;
		//System.err.println("New Sheet Name is:  "+ sheetName); // Output : [String sheetname] 04-Aug-18 (Sat) 06;37;31 PM

		FileInputStream fileinput = new FileInputStream(excelFileName);
		XSSFWorkbook workboook = new XSSFWorkbook(fileinput);

		workboook.createSheet(sheetName);

		FileOutputStream fileOut = new FileOutputStream(excelFileName);
		workboook.write(fileOut);
		fileOut.close();


		System.out.println("Method \"createExcel_Sheet\" executed successfully and created below sheet name ... ");
		System.err.println(sheetName);
		System.out.println("");

		Thread.sleep(1500L);  // It is important to delay
		return sheetName;

	}
	public static boolean setCellDataColumnName(String excelRelativePath, String sheetName, String columnName, String setCellValue, int rowNo) throws Throwable {


		/**
		 *
		 * RunConfiguration.getProjectDir() ---> C:/Users/XXXXX/FOCiS_Automation_Script/FOCiS_OF_AF_JA
		 *
		 */


		try
		{

			String fileName=excelRelativePath;
			//commented knowingly --->fileName=RunConfiguration.getProjectDir()+'\\' +fileName+".xlsx";
			System.err.println(fileName);

			FileInputStream fis = new FileInputStream(fileName);
			FileOutputStream fos = null;
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheet(sheetName);  // Sheet Name
			XSSFRow row = null;
			XSSFCell cell = null;

			int col_Num = -1;
			row = sheet.getRow(0);
			for(int i = 0; i < row.getLastCellNum(); i++)
			{
				if(row.getCell(i).getStringCellValue().trim().equals(columnName))  // Column Name
				{
					col_Num = i;
				}
			}
			sheet.autoSizeColumn(col_Num); // Auto Size Column Width
			row = sheet.getRow(rowNo);     // row no:  you want to write data.
			if(row == null)
				row = sheet.createRow(rowNo);

			cell = row.getCell(col_Num);
			if(cell == null)
				cell = row.createCell(col_Num);

			cell.setCellValue(setCellValue);  // Set Data

			fos = new FileOutputStream(fileName);
			workbook.write(fos);
			fos.close();

			System.err.println("Method \"writeToExcel\" successful: " + setCellValue + " Row: " +rowNo  + " Column Name: " +columnName);

			Thread.sleep(500);  // It is important to delay

		}
		catch (Exception e)
		{
			System.out.println("------- <<<<<<< Ensure EXCEL is not OPEN >>>>>> -------------");
			System.out.println("------- <<<<<<< Ensure Workbook Name or Sheet Name or Column Name is CORRECT >>>>>> -------------");
			e.printStackTrace();

			//throw new StepErrorException("*** TEST FORCED STOPPED DUE TO MANUAL CONTRACT ***")
			throw new Exception("*** TEST FORCED STOPPED DUE TO Above Reason***");
			return  false;
		}

		return true;

	}


}


//https://www.dev2qa.com/copy-rows-between-excel-sheet-use-apache-poi/