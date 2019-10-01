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

import lexer.Token;

public class SelfMethodCall {
	public SelfMethodCall(Variable variable, String methodName){
		this.methodName = methodName;
		this.variable = variable;
	}
	
	public void genJava(PW pw) {
		pw.print("self.");
		this.variable.genJava(pw);
		pw.print("." + this.methodName);
	}
	
	public void genC(PW pw) {
		
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		
	}
	
	public Type getType() {
		return Type.undefinedType;
	}

	private String methodName;
	private Variable variable;
}
