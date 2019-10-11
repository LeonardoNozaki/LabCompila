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

import java.util.ArrayList;

import lexer.Token;

public class TokenMethodCallPar extends Expr{
	public TokenMethodCallPar(Token t, MethodDec method, ArrayList<Expr> expr){
		this.method = method;
		this.expr = expr;
		this.t = t;
	}
	
	public boolean isOnlyId() {
		return false;
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
				pw.printIdent(this.getJavaToken() + "." + method.getName().substring(0, method.getName().length()-1) + "( ");
			}
			else {
				pw.printIdent(this.getJavaToken() + "." + method.getName() + "( ");
			}
			if(this.expr.size() > 0) {
				this.expr.get(0).genJava(pw);
			}
			for(int i = 1; i < this.expr.size(); i++) {
				pw.print(", ");
				this.expr.get(i).genJava(pw);
			}
			pw.println(" );");
		}
		else {
			if(method.getName().charAt(method.getName().length()-1)  == ':') {
				pw.print(this.getJavaToken() + "." + method.getName().substring(0, method.getName().length()-1) + "( ");
			}
			else {
				pw.print(this.getJavaToken() + "." + method.getName() + "( ");
			}
			if(this.expr.size() > 0) {
				this.expr.get(0).genJava(pw);
			}
			for(int i = 1; i < this.expr.size(); i++) {
				pw.print(", ");
				this.expr.get(i).genJava(pw);
			}
			pw.print(" )");
		}
	}
	
	public void genC(PW pw) {
		
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		
	}
	
	public Type getType() {
		return Type.undefinedType;
	}

	private MethodDec method;
	private ArrayList<Expr> expr;
	private Token t;
}
