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

public class SelfMethodCall extends Expr{
	public SelfMethodCall(Variable variable, MethodDec method){
		this.method = method;
		this.variable = variable;
	}
	
	public boolean isOnlyId() {
		return false;
	}
	
	public void genJava(PW pw) {
		pw.print("this.");
		this.variable.genJava(pw);
		pw.print("." + this.method.getName());
	}
	
	public void genC(PW pw) {
		
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		
	}
	
	public Type getType() {
		return Type.undefinedType;
	}

	private MethodDec method;
	private Variable variable;
}
