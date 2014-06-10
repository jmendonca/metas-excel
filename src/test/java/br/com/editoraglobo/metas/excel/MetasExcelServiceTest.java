package br.com.editoraglobo.metas.excel;

import br.com.editoraglobo.metas.excel.MetasExcelService;



/**
 * @author mendonca
 *
 */
public class MetasExcelServiceTest {
	
	/**
	 * 
	 */
	private static final String FILE_PATH = "/Users/mendonca/Desktop/excel para importar/metas2-2014.xlsx";
   
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		MetasExcelService metasService = new MetasExcelService();
		metasService.processAllSheets(FILE_PATH);
	}
}
