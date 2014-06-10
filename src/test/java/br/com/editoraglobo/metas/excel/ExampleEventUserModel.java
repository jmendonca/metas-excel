package br.com.editoraglobo.metas.excel;

import java.io.InputStream;
import java.util.regex.Pattern;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class ExampleEventUserModel {
	
	/**
	 * 
	 */
	private static final String FILE_PATH = "/Users/mendonca/Desktop/excel para importar/metas2-2014.xlsx";
	
	/**
	 *
	 */
	private static final String REGEX_NUMBER = "\\d+";
   
	/**
	 *
	 */
	private static final Pattern PATTERN_REGEX_NUMBER = Pattern.compile(REGEX_NUMBER);
	
	/**
	 * @param sheet
	 * @return
	 */
	private final boolean isValidSheetName(final String sheetName) {
		return PATTERN_REGEX_NUMBER.matcher(sheetName).find();
	}

	/**
	 * @param filename
	 * @throws Exception
	 */
	public void processOneSheet(String filename) throws Exception {
		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader r = new XSSFReader( pkg );
		SharedStringsTable sst = r.getSharedStringsTable();

		XMLReader parser = fetchSheetParser(sst);

		// rId2 found by processing the Workbook
		// Seems to either be rId# or rSheet#
		InputStream sheet2 = r.getSheet("ÉPOCA-5419");
		InputSource sheetSource = new InputSource(sheet2);
		parser.parse(sheetSource);
		sheet2.close();
	}

	public void processAllSheets(String filename) throws Exception {
		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader r = new XSSFReader( pkg );
		SharedStringsTable sst = r.getSharedStringsTable();
		
		XMLReader parser = fetchSheetParser(sst);
		
		final XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator) r.getSheetsData();
		while (sheets.hasNext()) {
			final InputStream sheet = sheets.next();

			if (isValidSheetName(sheets.getSheetName())) {
				System.out.println(String.format("Processing sheet [ %s ] \n", sheets.getSheetName()));
				
				final InputSource sheetSource = new InputSource(sheet);
				parser.parse(sheetSource);
				sheet.close();
			
			} else {
				System.out.println(String.format("Ignoring sheet [ %s ] \n", sheets.getSheetName()));
			}
		}
	}

	/**
	 * @param sst
	 * @return
	 * @throws SAXException
	 */
	public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
		XMLReader parser =
			XMLReaderFactory.createXMLReader(
					"org.apache.xerces.parsers.SAXParser"
			);
		ContentHandler handler = new SheetHandler(sst);
		parser.setContentHandler(handler);
		return parser;
	}

	/** 
	 * See org.xml.sax.helpers.DefaultHandler javadocs 
	 */
	private static class SheetHandler extends DefaultHandler {
		private SharedStringsTable sst;
		private String lastContents;
		private boolean nextIsString;
		
		private SheetHandler(SharedStringsTable sst) {
			this.sst = sst;
		}
		
		public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
			
			// c => cell
			if (name.equals("c")) {
				// Print the cell reference
				System.out.print(attributes.getValue("r") + " - ");
				// Figure out if the value is an index in the SST
				String cellType = attributes.getValue("t");
				if(cellType != null && cellType.equals("s")) {
					nextIsString = true;
				} else {
					nextIsString = false;
				}
			}
			// Clear contents cache
			lastContents = "";
		}
		
		public void endElement(String uri, String localName, String name) throws SAXException {
			// Process the last contents as required.
			// Do now, as characters() may be called more than once
			if (nextIsString) {
				int idx = Integer.parseInt(lastContents);
				lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
				nextIsString = false;
			}

			// v => contents of a cell
			// Output after we've seen the string contents
			if (name.equals("v")) {
				System.out.println(lastContents);
			}
		}

		public void characters(char[] ch, int start, int length) throws SAXException {
			lastContents += new String(ch, start, length);
		}
	}
	
	public static void main(String[] args) throws Exception {
		ExampleEventUserModel example = new ExampleEventUserModel();
//		example.processOneSheet(args[0]);
		example.processAllSheets(FILE_PATH);
	}
}