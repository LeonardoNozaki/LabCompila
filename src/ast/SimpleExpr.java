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

public class SimpleExpr extends Expr{
	public SimpleExpr(Expr expr) {
		this.expr = expr;
	}
	
	public boolean isOnlyId() {
    	return true;
    }
	
	@Override
	public void genC( PW pw, boolean putParenthesis ) {
		this.expr.genC(pw, putParenthesis);
    }

	@Override
	public void genC( PW pw ) {
		this.expr.genC(pw, false);
    }
	
	public boolean isObjectCreation() {
		return false;
	}
	
	@Override
	public void genJava(PW pw) {
		this.expr.genJava(pw);
	}
	
	@Override
	public Type getType() {
		return expr.getType();
	}
	
	private Expr expr;
}
