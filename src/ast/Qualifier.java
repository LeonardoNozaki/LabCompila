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

public class Qualifier {
	public Qualifier (String quali) {
		this.quali = quali;
	}
	
	public void genC(PW pw) {
		
	}
	
	public String getQualifier() {
		return quali;
	}
	
	public void genJava(PW pw) {
		pw.printIdent(quali);
	}
	
	public boolean getVoidQualifier() {
		if(quali.equals("")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isPrivate() {
		return quali.equals("private");
	}
	
	private String quali;
}
