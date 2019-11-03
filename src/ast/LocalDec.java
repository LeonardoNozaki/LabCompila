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

public class LocalDec extends Statement{
	public LocalDec(Variable variable) {
		this.variable = variable;
	}
	
	@Override
	public void genC(PW pw) {
		this.variable.genC(pw, false);
	}
	
	public void genJava(PW pw) {
		this.variable.genJava(pw);
	}
	
	private Variable variable;
}
