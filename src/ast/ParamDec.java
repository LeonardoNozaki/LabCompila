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
	}
	
	public void genC( PW pw ) {
		
	}
	public void genJava(PW pw) {
		pw.print(this.type.getJavaname() + " " + id);
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
		
	}

	private String id;
	private Type type;
}
