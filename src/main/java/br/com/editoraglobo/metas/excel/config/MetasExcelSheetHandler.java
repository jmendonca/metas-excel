package br.com.editoraglobo.metas.excel.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.com.editoraglobo.metas.excel.model.GrupoMetas;
import br.com.editoraglobo.metas.excel.model.MonthType;

/** 
 * See org.xml.sax.helpers.DefaultHandler javadocs 
 */ 
public class MetasExcelSheetHandler extends DefaultHandler {
	
	/**
	 * 
	 */
	private SharedStringsTable sst;
	
	/**
	 * 
	 */
	private MetasExcelConfig config;
	
	/**
	 * 
	 */
	private String lastContents;
	
	/**
	 * 
	 */
	private boolean nextIsString;
	
	/**
	 * 
	 */
	private GrupoMetas currentGrupoMetas;
	
	/**
	 * 
	 */
	private String currentGrupo;
	
	/**
	 * 
	 */
	private String currentExecutivo;
	
	// TODO analisar se será necessário manter essa lista
	List<GrupoMetas> repositorioMetas = new ArrayList<GrupoMetas>();
	
	
	/**
	 * @param sst
	 */
	public MetasExcelSheetHandler(SharedStringsTable sst, MetasExcelConfig config) {
		this.sst = sst;
		this.config = config;
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		// c => cell
		if (name.equals("c")) {
			final String cellReference = attributes.getValue("r");
			// armazena qual celula está sendo processad
			config.setCurrentCell(cellReference);
			
			// verificando em qual linha deve-se começar a leitura 
			// e se a linha está no intervalo permitido
			// e se não não é linha em branco no fim de cada grupo
			if (config.getCurrentCell().getRow() > config.getSkipRows()
					&& config.getCurrentCell().getRow() <= config.getLimitRow()
					&& !config.isLinhaEmBranco()) {
				
				final String cellType = attributes.getValue("t");
				if (cellType != null && cellType.equals("s")) {
					 nextIsString = true;
				}
				
				if (config.isNovoGrupo()) {
					// resetando nome do grupo
					currentGrupo = ""; 
					System.out.println("grupo " + cellReference);
				
				} else if (config.isNovoExecutivo()) {
					// resetando nome do executivo
					currentExecutivo = ""; 
					System.out.println("executivo " + cellReference);
				
				} else if (config.isMetaEditora()) {
					System.out.println("meta.editora " + cellReference);
					
				} else if (config.isMetaDiretor()) {
					System.out.println("meta.diretor " + cellReference);
				} 
			}
		}

		// Clear contents cache
		lastContents = "";
	}
	
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String name) throws SAXException {
		// verificando em qual linha deve-se começar a leitura 
		// e se a linha está no intervalo permitido
		// e se não não é linha em branco no fim de cada grupo
		if (config.getCurrentCell().getRow() > config.getSkipRows()
				&& config.getCurrentCell().getRow() <= config.getLimitRow()
				&& !config.isLinhaEmBranco()) {
			

			if (nextIsString) {
				int idx = Integer.parseInt(lastContents);
				lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
				nextIsString = false;
			}
			
		
			if (config.isNovoGrupo() && name.equals("v")) {
				// cria novo grupo
				currentGrupo = lastContents;
				this.currentGrupoMetas = new GrupoMetas(currentGrupo);
			
			} else if (config.isNovoExecutivo() && name.equals("v")) {
				// cria novo executivo
				currentExecutivo = lastContents;
				
				this.currentGrupoMetas.getExecutivosMetasEditora().put(
						currentExecutivo, new HashMap<MonthType, BigDecimal>());
				
				this.currentGrupoMetas.getExecutivosMetasDiretor().put(
						currentExecutivo, new HashMap<MonthType, BigDecimal>());
			
			} else if (config.isMetaEditora() && name.equals("v")) {
				
				// adiciona meta editora
				if (currentExecutivo != null 
						&& this.currentGrupoMetas.getExecutivosMetasEditora().containsKey(currentExecutivo)) {
					
					this.currentGrupoMetas.getExecutivosMetasEditora().get(currentExecutivo).put(
							config.getMonthHeaderEditora(), new BigDecimal(lastContents.replaceAll(",", "")));
				}

			} else if (config.isMetaDiretor() && name.equals("v")) {
				// adiciona meta diretor
				if (currentExecutivo != null && this.currentGrupoMetas.getExecutivosMetasDiretor().containsKey(currentExecutivo)) {
					this.currentGrupoMetas.getExecutivosMetasDiretor().get(currentExecutivo).put(
						config.getMonthHeaderDiretor(), new BigDecimal(lastContents.replaceAll(",", "")));
				}

			} else if (config.isFimGrupo() && name.equals("v")) {
				// adiciona grupoMetas na lista parseada
				repositorioMetas.add(currentGrupoMetas);
				// resetando currentGrupoMetas
				currentGrupoMetas = null;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		lastContents += new String(ch, start, length);
	}
}