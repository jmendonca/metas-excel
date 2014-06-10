package br.com.editoraglobo.metas.excel.config;

import java.math.BigDecimal;
import java.util.HashMap;

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
public class MetasExecelSheetHandler extends DefaultHandler {
	
	/**
	 * 
	 */
	private SharedStringsTable sst;
	
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
	private final MetasExcelConfig config = new MetasExcelConfig();
	
	/**
	 * 
	 */
	private GrupoMetas grupoMetas;
	
	/**
	 * 
	 */
	private String currentGrupo;
	
	/**
	 * 
	 */
	private String currentExecutivo;
	
	
	/**
	 * @param sst
	 */
	public MetasExecelSheetHandler(SharedStringsTable sst) {
		this.sst = sst;
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
					&& config.getCurrentCell().getRow() <= config.getMaxRow()
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
				&& config.getCurrentCell().getRow() <= config.getMaxRow()
				&& !config.isLinhaEmBranco()) {
			

			if (nextIsString) {
				int idx = Integer.parseInt(lastContents);
				lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
				nextIsString = false;
			}
			
		
			if (config.isNovoGrupo() && name.equals("v")) {
				// cria novo grupo
				currentGrupo = lastContents;
				this.grupoMetas = new GrupoMetas(currentGrupo);
			
			} else if (config.isNovoExecutivo() && name.equals("v")) {
				// cria novo executivo
				currentExecutivo = lastContents;
				
				this.grupoMetas.getExecutivosMetasEditora().put(
						currentExecutivo, new HashMap<MonthType, BigDecimal>());
				
				this.grupoMetas.getExecutivosMetasDiretor().put(
						currentExecutivo, new HashMap<MonthType, BigDecimal>());
			
			} else if (config.isMetaEditora() && name.equals("v")) {
				
				// adiciona meta editora
				if (currentExecutivo != null 
						&& this.grupoMetas.getExecutivosMetasEditora().containsKey(currentExecutivo)) {
					
					this.grupoMetas.getExecutivosMetasEditora().get(currentExecutivo).put(
							config.getMonthHeaderEditora(), new BigDecimal(lastContents.replaceAll(",", "")));
				}

			} else if (config.isMetaDiretor() && name.equals("v")) {
				// adiciona meta diretor
				if (currentExecutivo != null && this.grupoMetas.getExecutivosMetasDiretor().containsKey(currentExecutivo)) {
					this.grupoMetas.getExecutivosMetasDiretor().get(currentExecutivo).put(
						config.getMonthHeaderDiretor(), new BigDecimal(lastContents.replaceAll(",", "")));
				}

			} else if (config.isFimGrupo() && name.equals("v")) {
				// TODO copia a metaTO para o repositorio de metas parseads
				// TODO resetar metaTO
				// TODO chechar se name.equals("v") será necessário
				System.out.println(grupoMetas);
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