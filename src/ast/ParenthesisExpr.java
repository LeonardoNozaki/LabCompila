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

public class ParenthesisExpr extends Expr{
	public ParenthesisExpr(Expr expr) {
		this.expr = expr;
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		pw.print("( ");
		this.expr.genC(pw, putParenthesis);
		pw.print(" )");
    }

	public boolean isObjectCreation() {
		return false;
	}
	
	public boolean isOnlyId() {
    	return expr.isOnlyId();
    }
	
	public void genJava(PW pw) {
		pw.print("( ");
		this.expr.genJava(pw);
		pw.print(" )");
	}
	
	@Override
	public Type getType() {
		return expr.getType();
	}
	
	private Expr expr;
}
