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
		if(quali.equals("override") || quali.equals("override public")) {
			pw.printlnIdent("@Override");
			pw.printIdent("public ");
		}
		else if(quali.equals("final override") || quali.equals("final override public")) {
			pw.printlnIdent("@Override");
			pw.printIdent("final public ");
		}
		else {
			pw.printIdent(this.quali + " ");
		}
	}
	
	public boolean hasOverride() {
		if(quali.equals("override") || quali.equals("override public") || quali.equals("final override") || quali.equals("final override public")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isVoid() {
		if(quali.equals("")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean hasFinal() {
		if(quali.equals("final") || quali.equals("final public") || quali.equals("final override") || quali.equals("final override public")) {
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
