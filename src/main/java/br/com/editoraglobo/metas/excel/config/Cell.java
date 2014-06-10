package br.com.editoraglobo.metas.excel.config;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mendonca
 *
 * @see Serializable
 */
class Cell implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8862315375150805183L;

	/**
	 * 
	 */
	private static final String REGEX_NUMBER = "\\d+";

	/**
	 * 
	 */
	private static final Pattern PATTERN_REGEX_NUMBER = Pattern.compile(REGEX_NUMBER);

	/**
	 *
	 */
	private int row;

	/**
	 *
	 */
	private String col;

	/**
	 * @param row
	 * @param col
	 */
	public void setReference(String reference) {
		Matcher m = PATTERN_REGEX_NUMBER.matcher(reference);

		int row = 0;
		if (m.find()) {
			row = Integer.parseInt(m.group());
		}
		String col = reference.replaceAll("" + row + "", "");

		this.row = row;
		this.col = col;
	}

	/**
	 * @return
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return
	 */
	public String getCol(){
		return col;
	}
	
	@Override
	public String toString() {
		return "Cell [row=" + row + ", col=" + col + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((col == null) ? 0 : col.hashCode());
		result = prime * result + row;
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
		Cell other = (Cell) obj;
		if (col == null) {
			if (other.col != null)
				return false;
		} else if (!col.equals(other.col))
			return false;
		if (row != other.row)
			return false;
		return true;
	}
}
