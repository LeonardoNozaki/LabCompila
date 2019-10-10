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
	public TokenMethodCallPar(Token t, String methodName, ArrayList<Expr> expr){
		this.methodName = methodName;
		this.expr = expr;
		this.t = t;
	}
	
	public boolean isOnlyId() {
		return false;
	}
	
	public void genJava(PW pw) {
		pw.print(this.t.toString() + "." + methodName + "( ");
		if(this.expr.size() > 0) {
			this.expr.get(0).genJava(pw);
		}
		for(int i = 1; i < this.expr.size(); i++) {
			pw.print(", ");
			this.expr.get(i).genJava(pw);
		}
		pw.print(" )");
	}
	
	public void genC(PW pw) {
		
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		
	}
	
	public Type getType() {
		return Type.undefinedType;
	}

	private String methodName;
	private ArrayList<Expr> expr;
	private Token t;
}
