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
	
	public String getJavaToken() {
		if(t == Token.SELF) {
			return "this";
		}
		else {
			return "super";
		}
	}
	
	public void genJava(PW pw) {
		if(method.getType() == Type.voidType) {
			if(method.getName().charAt(method.getName().length()-1)  == ':') {
				pw.printlnIdent(this.getJavaToken() + "." + method.getName().substring(0, method.getName().length()-1) + "();");
			}
			else {
				pw.printlnIdent(this.getJavaToken() + "." + method.getName() + "();");
			}
		}
		else {
			if(method.getName().charAt(method.getName().length()-1)  == ':') {
				pw.print(this.getJavaToken() + "." + method.getName().substring(0, method.getName().length()-1) + "()");
			}
			else {
				pw.print(this.getJavaToken() + "." + method.getName() + "()");
			}
		}
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
