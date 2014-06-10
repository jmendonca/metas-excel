package br.com.editoraglobo.metas.excel.model;

public enum MonthType {
	
	JANEIRO, FEVEREIRO, MARCO, ABRIL, MAIO, JUNHO, JULHO, AGOSTO, SETEMBRO, OUTUBRO, NOVEMBRO, DEZEMBRO;
	
	/**
	 * @param month
	 * @return
	 * TODO rever necessidade de ter esse metodo 
	 */
	public static final MonthType getType(String month) {
		for (MonthType m : MonthType.values()) {
			if (m.toString().equalsIgnoreCase(month)) {
				return m;
			}
		}
		return null;
	}
}
