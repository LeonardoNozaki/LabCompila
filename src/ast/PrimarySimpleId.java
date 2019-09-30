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

public class PrimarySimpleId extends Expr{
	public PrimarySimpleId(Variable variable, Type type) {
		this.variable = variable;
		this.type = type;
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		
	}
	
	public void genJava(PW pw) {
		
	}
	
	public Type getType() {
		return variable.getType();
	}
	
	private Variable variable;
	private Type type;
}
