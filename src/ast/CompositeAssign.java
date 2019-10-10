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
	
	public void genJava(PW pw) {
		pw.printIdent("");
		this.left.genJava(pw);
		pw.print(" = ");
		this.right.genJava(pw);
		pw.println("");
	}
	
	private Expr left, right;
}


