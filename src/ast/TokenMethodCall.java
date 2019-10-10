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

public class TokenMethodCall extends Expr{
	public TokenMethodCall(Token t, MethodDec method){
		this. method = method;
		this.t = t;
	}
	
	public boolean isOnlyId() {
		if(this.t == Token.SELF) {
			return true;
		}
		else {
			return false;
		}
    }
	
	public void genJava(PW pw) {
		pw.printlnIdent(this.t.toString() + "." + method.getName() + "()");
	}
	
	public void genC(PW pw) {
		
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		
	}
	
	public Type getType() {
		return method.getType();
	}

	private MethodDec method;
	private Token t;
}
