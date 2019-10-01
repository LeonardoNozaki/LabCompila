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

public class ReturnStat extends Statement{
	public ReturnStat( Expr expr ) {
		this.expr = expr;
	}

	public void genC( PW pw ) {
		
	}
	
	public void genJava(PW pw) {
		pw.printIdent("return ");
		this.expr.genJava(pw);
		pw.println(";");
	}

	private Expr expr;
}
