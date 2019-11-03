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

public class ObjectCreation extends Expr{
	public ObjectCreation(Type type) {
		this.type = type;
	}
	
	public boolean isObjectCreation() {
		return true;
	}
	
	public void genJava(PW pw) {
		pw.print("new " + this.type.getJavaname() + "()");
	}
	
	public boolean isOnlyId() {
    	return false;
    }
	
	public void genC( PW pw, boolean putParenthesis ) {
		pw.print("new_" + this.type.getCname() + "()");
	}
	
	public Type getType() {
		return this.type;
	}
	private Type type;
}
