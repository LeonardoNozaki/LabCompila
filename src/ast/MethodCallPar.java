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

public class MethodCallPar extends Expr{
	public MethodCallPar(Variable variable, String methodName, ArrayList<Expr> expr){
		this.variable = variable;
		this.methodName = methodName;
		this.expr = expr;
	}
	public void genJava(PW pw) {
		this.variable.genJava(pw);
		pw.print("." + methodName + "(");
		this.expr.get(0).genJava(pw);	
		for(int i = 1; i < this.expr.size(); i++) {
			pw.print(", ");
			this.expr.get(i).genJava(pw);	
		}
		pw.print(") ");
	}
	public void genC(PW pw) {
		
		
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		
	}
	
	public Type getType() {
		return Type.undefinedType;
	}
	private Variable variable;
	private String methodName;
	private ArrayList<Expr> expr;
}
