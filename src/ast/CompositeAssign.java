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

public class CompositeAssign extends AssignExpr {
	public CompositeAssign(Expr left, Expr right) {
		this.left = left;
		this.right = right;
	}
	public void genC(PW pw) {
		
	}
	
	private Expr left, right;
}
