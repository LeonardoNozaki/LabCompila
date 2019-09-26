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

abstract public class Expr extends Statement {
    abstract public void genC( PW pw, boolean putParenthesis );
    abstract public void genJava(PW pw);
    
    // new method: the type of the expression
    abstract public Type getType();
    
	@Override
	public void genC(PW pw) {
		this.genC(pw, false);
	}
}