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

public class SimpleAssign extends AssignExpr {
	public SimpleAssign(Expr left) {
		this.left = left;
	}
	public void genC(PW pw) {
		pw.printIdent("");
		this.left.genC(pw, false);
		pw.println(";");
	}
	
	public void genJava(PW pw) {
		pw.printIdent("");
		this.left.genJava(pw);
	}
	
	private Expr left;
}

