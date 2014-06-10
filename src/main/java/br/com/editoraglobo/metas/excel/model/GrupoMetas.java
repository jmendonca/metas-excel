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
	
	@Override
	public String toString() {
		return "GrupoMetas [grupo=" + grupo + ", executivoMetasEditora="
				+ executivoMetasEditora + ", executivoMetasDiretor="
				+ executivoMetasDiretor + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((executivoMetasDiretor == null) ? 0 : executivoMetasDiretor
						.hashCode());
		result = prime
				* result
				+ ((executivoMetasEditora == null) ? 0 : executivoMetasEditora
						.hashCode());
		result = prime * result + ((grupo == null) ? 0 : grupo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GrupoMetas other = (GrupoMetas) obj;
		if (executivoMetasDiretor == null) {
			if (other.executivoMetasDiretor != null)
				return false;
		} else if (!executivoMetasDiretor.equals(other.executivoMetasDiretor))
			return false;
		if (executivoMetasEditora == null) {
			if (other.executivoMetasEditora != null)
				return false;
		} else if (!executivoMetasEditora.equals(other.executivoMetasEditora))
			return false;
		if (grupo == null) {
			if (other.grupo != null)
				return false;
		} else if (!grupo.equals(other.grupo))
			return false;
		return true;
	}
}
