package br.com.editoraglobo.metas.excel.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mendonca
 *
 * @see Serializable
 */
public class GrupoMetas implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3909311387083479317L;

	/**
	 * 
	 */
	private String grupo;
	
	/**
	 * 
	 */
	private Map<String, Map<MonthType, BigDecimal>> executivoMetasEditora 
						= new HashMap<String, Map<MonthType, BigDecimal>>();
	
	/**
	 * 
	 */
	private Map<String, Map<MonthType, BigDecimal>> executivoMetasDiretor 
						= new HashMap<String, Map<MonthType, BigDecimal>>();

	/**
	 * @param grupo
	 */
	public GrupoMetas(String grupo) {
		this.grupo = grupo;
	}
	
	/**
	 * @return
	 */
	public String getGrupo() {
		return grupo;
	}
	
	/**
	 * @return
	 */
	public Map<String, Map<MonthType, BigDecimal>> getExecutivosMetasEditora() {
		return executivoMetasEditora;
	}
	
	/**
	 * @return
	 */
	public Map<String, Map<MonthType, BigDecimal>> getExecutivosMetasDiretor() {
		return executivoMetasDiretor;
	}
}
