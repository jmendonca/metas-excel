package br.com.editoraglobo.metas.excel.config;

import java.util.HashMap;
import java.util.Map;

import br.com.editoraglobo.metas.excel.model.MetaType;
import br.com.editoraglobo.metas.excel.model.MonthType;


/**
 * @author mendonca
 *
 */
public class MetasExcelConfig {
	
//	/**
//	 * 
//	 */
//	private static final Logger LOG = Logger.getLogger(MetasExcelConfig.class);	 
	
	/**
	 * A leitura começa a partir da linha 42 (inclusive), então é necessário 
	 * ignorar as primeiras 41 linhas 
	 */
	private static final int SKIP_ROWS_DEFAULT = 41;
	
	/**
	 * Numero maximo que será considerado para leitura
	 * de linhas (inclusive)
	 */
	private static final int MAX_ROW = 238;
	
	/**
	 * 
	 */
	private Cell currentCell = new Cell();
	
	/**
	 * 
	 */
	private int skipRows;
	
	/**
	 * 
	 */
	private int maxRow;
	
	/**
	 * String = cell coll
	 */
	private Map<MetaType, Map<String, MonthType>> header = new HashMap<MetaType, Map<String, MonthType>>();
	
	/**
	 * 
	 */
	public MetasExcelConfig() {
		this(SKIP_ROWS_DEFAULT, MAX_ROW);
	}
	
	/**
	 * @param skipRows
	 * @param maxRow
	 */
	public MetasExcelConfig(int skipRows, int maxRow) {
		this.skipRows = skipRows;
		this.maxRow = maxRow;
		initializeHeader();	
	}
	
	/**
	 * 
	 */
	private void initializeHeader() {
		Map<String, MonthType> headerEditora = new HashMap<String, MonthType>();
		headerEditora.put("C", MonthType.JANEIRO);
		headerEditora.put("E", MonthType.FEVEREIRO);
		headerEditora.put("G", MonthType.MARCO);
		headerEditora.put("I", MonthType.ABRIL);
		headerEditora.put("K", MonthType.MAIO);
		headerEditora.put("M", MonthType.JUNHO);
		headerEditora.put("O", MonthType.JULHO);
		headerEditora.put("Q", MonthType.AGOSTO);
		headerEditora.put("S", MonthType.SETEMBRO);
		headerEditora.put("U", MonthType.OUTUBRO);
		headerEditora.put("W", MonthType.NOVEMBRO);
		headerEditora.put("Y", MonthType.DEZEMBRO);
		
		Map<String, MonthType> headerDiretor = new HashMap<String, MonthType>();
		headerDiretor.put("AD", MonthType.JANEIRO);
		headerDiretor.put("AF", MonthType.FEVEREIRO);
		headerDiretor.put("AH", MonthType.MARCO);
		headerDiretor.put("AJ", MonthType.ABRIL);
		headerDiretor.put("AL", MonthType.MAIO);
		headerDiretor.put("AN", MonthType.JUNHO);
		headerDiretor.put("AP", MonthType.JULHO);
		headerDiretor.put("AR", MonthType.AGOSTO);
		headerDiretor.put("AT", MonthType.SETEMBRO);
		headerDiretor.put("AV", MonthType.OUTUBRO);
		headerDiretor.put("AX", MonthType.NOVEMBRO);
		headerDiretor.put("AZ", MonthType.DEZEMBRO);
		
		header.put(MetaType.EDITORA, headerEditora);
		header.put(MetaType.DIRETOR, headerDiretor);
	}
	
	 
	
//	/**
//	 * 
//	 */
//	private List<String> COLS_METAS_EDITORA = Arrays.asList(
//			"B", "C", "E", "G", "I,", "K", "M", "O", "Q", "S", "U", "W", "Y") ;
//	
//	/**
//	 * 
//	 */
//	private List<String> COLS_METAS_EXECUTIVO = Arrays.asList(
//			"AD", "AF","AH", "AJ,", "AL", "AN", "AP", "AR", "AT", "AV", "AX", "AZ") ;
	
	/**
	 * @return
	 */
	private boolean isLineHeader() {
		return (currentCell.getRow() % 18 == 6);
	}
	
	/**
	 * @return
	 */
	final boolean isLinhaEmBranco() {
		return currentCell.getRow() % 18 == 5;
	}
	
	/**
	 * @return
	 */
	final boolean isLinhaFimGrupo() {
		return currentCell.getRow() % 18 == 4;
	}
	
	/**
	 * @return
	 */
	final boolean isNovoGrupo() {
		return (isLineHeader()
				&& currentCell.getCol().equalsIgnoreCase("B"));
	}
	
	/**
	 * @return
	 */
	final boolean isFimGrupo() {
		return (currentCell.getRow() % 18 == 4
				&& currentCell.getCol().equalsIgnoreCase("B"));
	}
	
	/**
	 * @param reference
	 * @return
	 */
	final boolean isMetaEditora() {
//		return COLS_METAS_EDITORA.contains(currentCell.getCol());
		return (!isLineHeader() && !isNovoGrupo() && !isLinhaFimGrupo()
				&& header.get(MetaType.EDITORA).containsKey(
						currentCell.getCol()));
	}
	
	/**
	 * @param reference
	 * @return
	 */
	final boolean isMetaDiretor() {
//		return COLS_METAS_EXECUTIVO.contains(currentCell.getCol());
		return (!isLineHeader() && !isNovoGrupo() && !isLinhaFimGrupo()
				&& header.get(MetaType.DIRETOR).containsKey(
						currentCell.getCol()));
	}
	
	/**
	 * @return
	 */
	final boolean isNovoExecutivo() {
		return (!isLineHeader() && !isLinhaFimGrupo()
				&& currentCell.getRow() > getSkipRows()
				&& currentCell.getRow() <= getMaxRow() 
				&& currentCell.getCol().equalsIgnoreCase("B"));
	}
	
	/**
	 * @param reference
	 */
	final void setCurrentCell(String reference) {
		currentCell.setReference(reference);
	}

	/**
	 * @return
	 */
	final Cell getCurrentCell() {
		return currentCell;
	}
	
	/**
	 * @return
	 */
	final int getSkipRows() {
		return skipRows;
	}
	
	/**
	 * @return
	 */
	final int getMaxRow() {
		return maxRow;
	}
	
//	/**
//	 * @return
//	 */
//	Map<MetaType, Map<String, MonthType>> getHeader() {
//		return header;
//	}
//	
//	/**
//	 * @return
//	 */
//	Map<String, MonthType> getHeaderEditora() {
//		return header.get(MetaType.EDITORA);
//	}
	
	/**
	 * @return
	 */
	MonthType getMonthHeaderEditora() {
		return header.get(MetaType.EDITORA).get(getCurrentCell().getCol());
	}
	
	/**
	 * @return
	 */
	MonthType getMonthHeaderDiretor() {
		return header.get(MetaType.DIRETOR).get(getCurrentCell().getCol());
	}
}