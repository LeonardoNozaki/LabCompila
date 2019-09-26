/* ==========================================================================
 * Universidade Federal de Sao Carlos - Campus Sorocaba
 * Disciplina: Laboratorio de Compiladores
 * Prof. Jose Guimaraes
 *
 * Trabalho de Laboratorio de Compiladores - The Cianeto Language
 *
 * Aluno: Bruno Rizzi       RA: 743515
 * Aluno: Leonardo Nozaki   RA: 743561
 * ========================================================================== */

package ast;

import java.util.ArrayList;

/** This class represents a metaobject annotation as <code>{@literal @}ce(...)</code> in <br>
 * <code>
 * @ce(5, "'class' expected") <br>
 * clas Program <br>
 *     public void run() { } <br>
 * end <br>
 * </code>
 *
   @author José

 */
public class MetaobjectAnnotation {

	public MetaobjectAnnotation(String name, ArrayList<Object> paramList) {
		this.name = name;
		this.paramList = paramList;
	}

	public ArrayList<Object> getParamList() {
		return paramList;
	}
	public String getName() {
		return name;
	}


	private String name;
	private ArrayList<Object> paramList;

}
