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

public class LocalVar extends Variable{
	public LocalVar(String id) {
		this.id = id;
		this.type = Type.undefinedType;
	}
	public LocalVar(String id, Type type) {
		this.type = type;
		this.id = id;
	}
	
	public boolean isOnlyId() {
    	return true;
    }
	
	public void genC(PW pw) {
		
	}
	
	public void genJava(PW pw) {
		pw.print(id);
	}
	
	public Type getType() {
		return type;
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		
	}
	
	private String id;
	private Type type;
}
