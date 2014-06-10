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
}
