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
	
	public void genJava(PW pw) {
		
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
