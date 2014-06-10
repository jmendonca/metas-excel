package br.com.editoraglobo.metas.excel;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import br.com.editoraglobo.metas.excel.config.MetasExecelSheetHandler;


public class MetasExcelService {
	
//	/**
//	 * 
//	 */
//	private static final Logger LOG = Logger.getLogger(MetasExcelService.class);
	
	/**
	 *
	 */
	private static final String REGEX_NUMBER = "\\d+";
	
	/**
	 * 
	 */
	private static final String REGEX_STRING = "[a-zA-Z\\s_\\p{L}]+";
    
	/**
	 *
	 */
	private static final Pattern PATTERN_REGEX_NUMBER = Pattern.compile(REGEX_NUMBER);
	
	/**
	 *
	 */
	private static final Pattern PATTERN_REGEX_STRING = Pattern.compile(REGEX_STRING);
	
	/**
	 * @param sheet
	 * @return
	 */
	private final boolean isValidSheetName(final String sheetName) {
		return PATTERN_REGEX_NUMBER.matcher(sheetName).find();
	}
	
//	/**
//	 * TODO lancar uma excecao verificada ???
//	 * 
//	 * @param sheet
//	 * @return
//	 */ 
//	private final int getCodProduto(final String sheetName) throws IllegalArgumentException { 
//		if (isValidSheetName(sheetName)) {
//			Matcher m = PATTERN_REGEX_NUMBER.matcher(sheetName);
//			String strCodProduto = null;
//			if (m.find()) {
//				strCodProduto = m.group();
//			}
//			return Integer.parseInt(strCodProduto);
//		}
//		throw new IllegalArgumentException("A Sheet tem um "
//				+ "nome em um formato desconhecido para leitura.");
//	}

	/**
	 * @param filename
	 * @throws Exception
	 */
	public final void processAllSheets(String filename) throws Exception {
		final OPCPackage pkg = OPCPackage.open(filename);
		final XSSFReader r   = new XSSFReader(pkg);
		
		final SharedStringsTable sst = r.getSharedStringsTable();
		final XMLReader parser		 = fetchSheetParser(sst);

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
					"org.apache.xerces.parsers.SAXParser");
		ContentHandler handler = new MetasExecelSheetHandler(sst);
		parser.setContentHandler(handler);
		return parser;
	}

	
	public static void main( String[] args ) {
		String a = "EPOCA-5419";
		Matcher m1 = PATTERN_REGEX_NUMBER.matcher(a);
		Matcher m2 = PATTERN_REGEX_STRING.matcher(a);
		if (m1.find()) {
			System.out.println(m1.group());
		}
		if (m2.find()) {
			System.out.println(m2.group());
		}
    }
}
