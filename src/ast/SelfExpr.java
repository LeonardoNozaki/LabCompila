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

public class SelfExpr extends Expr {
	public SelfExpr(TypeCianetoClass type) {
		this.type = type;
	}
	
	public void genJava(PW pw) {
		pw.print("this");
	}
	
	public boolean isObjectCreation() {
		return false;
	}
	
	public boolean isOnlyId() {
    	return true;
    }
	
	public void genC(PW pw) {
		pw.print("self");
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		if(putParenthesis == true) {
			pw.print("(self)");
		}
		else {
			pw.print("self");
		}
	}
	
	public Type getType() {
		return type;
	}

	private TypeCianetoClass type;
}
