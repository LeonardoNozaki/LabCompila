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

public class ParamDec extends Variable{
	public ParamDec(String id, Type type){
		this.type = type;
		this.id = id;
		this.init = false;
	}
	
	public boolean getInit() {
		return this.init;
	}
	
	public boolean isObjectCreation() {
		return false;
	}
	
	public void genC( PW pw ) {
		pw.print("_" + id);
	}
	
	public void genJava(PW pw) {
		pw.print(id);
	}
	
	public Type getType() {
		return type;
	}
	
	public boolean isOnlyId() {
    	return false;
    }
	
	public String getName() {
		return id;
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		if(putParenthesis == true) {
			pw.print("(_" + id + ")");
		}
		else {
			pw.print("_" + id);
		}
	}

	private String id;
	private Type type;
	private boolean init;
}
