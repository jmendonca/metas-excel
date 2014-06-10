package br.com.editoraglobo.metas.excel.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import br.com.editoraglobo.metas.excel.model.MetaType;
import br.com.editoraglobo.metas.excel.model.MonthType;


/**
 * @author mendonca
 *
 */
public class MetasExcelConfig implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2651215856038287512L;

//	/**
//	 * 
//	 */
//	private static final Logger LOG = Logger.getLogger(MetasExcelConfig.class);	 
	
	/**
	 * A leitura começa a partir da linha 42 (inclusive)
	 */
	private static final int SKIP_ROWS_DEFAULT = 41;
	
	/**
	 * Onde termina a leitura de linhas (inclusive)
	 */
	private static final int LIMIT_ROW_DEFAULT = 239;
	
	/**
	 * Cada grupo executivo/meta tem 17 linhas
	 */
	private static final int ROWS_PER_GROUP_DEFAULT = 17;
	
	/**
	 * Em qual coluna a leitura começa
	 */
	private static final String STARTS_ON_COL = "B";
	
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
	private int limitRow;
	
	/**
	 * 
	 */
	private int rowsPerGroup;
	
	/**
	 * 
	 */
	private String startsOnCol;
	
	
	/**
	 * String = cell coll
	 */
	private Map<MetaType, Map<String, MonthType>> header;
	
	/**
	 * 
	 */
	public MetasExcelConfig() {
		this(SKIP_ROWS_DEFAULT, LIMIT_ROW_DEFAULT, 
				ROWS_PER_GROUP_DEFAULT, STARTS_ON_COL);
	}
	
	/**
	 * @param skipRows
	 * @param limitRow
	 * @param rowsPerGroup
	 * @param startsOnCol
	 */
	public MetasExcelConfig(int skipRows, int limitRow, int rowsPerGroup, String startsOnCol) {
		this.skipRows		= skipRows;
		this.limitRow 		= limitRow;	
		this.rowsPerGroup 	= rowsPerGroup;
		this.startsOnCol 	= startsOnCol;
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
	final int getLimitRow() {
		return limitRow;
	}
	
	/**
	 * @return
	 */
	final int getRowsPerGroup() {
		return rowsPerGroup;
	}
	
	/**
	 * @return
	 */
	final String getStartsOnCol() {
		return startsOnCol;
	}

	/**
	 * @return
	 */
	private final Map<MetaType, Map<String, MonthType>> getHeader() {
		if (header == null) {
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
			
			header = new HashMap<MetaType, Map<String, MonthType>>();
			header.put(MetaType.EDITORA, headerEditora);
			header.put(MetaType.DIRETOR, headerDiretor);
		}
		return header;
	}
	
	/**
	 * linha onde fica o total
	 * 
	 * @return 
	 */
	private final boolean isLinhaFooter() {
		return currentCell.getRow() % (getRowsPerGroup() + 1) == 4;
	}
	
	/**
	 * @return
	 */
	final boolean isLinhaEmBranco() {
		return currentCell.getRow() % (getRowsPerGroup() + 1) == 5;
	}
	
	/**
	 * @return
	 */
	private boolean isLinhaHeader() {
		return (currentCell.getRow() % (getRowsPerGroup() + 1) == 6);
	}
	
	/**
	 * @return
	 */
	final boolean isNovoGrupo() {
		return (isLinhaHeader()
				&& currentCell.getCol().equalsIgnoreCase(
						getStartsOnCol()));
	}

	/**
	 * A coluna B da linha em que fica o total (linhaFooter) 
	 * foi elegida como fim do grupo
	 * 
	 * @return 
	 */
	final boolean isFimGrupo() {
		return (isLinhaFooter() 
				&& currentCell.getCol().equalsIgnoreCase(
						getStartsOnCol()));
	}
	
	/**
	 * @return
	 */
	final boolean isMetaEditora() {
		return (!isLinhaHeader() && !isLinhaFooter() && !isLinhaEmBranco()
				&& getHeader().get(MetaType.EDITORA).containsKey(
						currentCell.getCol()));
	}

	/**
	 * @return
	 */
	final boolean isMetaDiretor() {
		return (!isLinhaHeader() && !isLinhaFooter() && !isLinhaEmBranco()
				&& getHeader().get(MetaType.DIRETOR).containsKey(
						currentCell.getCol()));
	}

	/**
	 * @return
	 */
	final boolean isNovoExecutivo() {
		return (!isLinhaHeader() && !isLinhaFooter() && !isLinhaEmBranco()
				&& currentCell.getRow() > getSkipRows()
				&& currentCell.getRow() <= getLimitRow() 
				&& currentCell.getCol().equalsIgnoreCase(
						getStartsOnCol()));
	}

	/**
	 * @return
	 */
	final MonthType getMonthHeaderEditora() {
		return getHeader().get(MetaType.EDITORA).get(
				getCurrentCell().getCol());
	}
	
	/**
	 * @return
	 */
	final MonthType getMonthHeaderDiretor() {
		return getHeader().get(MetaType.DIRETOR).get(
				getCurrentCell().getCol());
	}
}