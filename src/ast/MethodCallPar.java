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
	public MethodCallPar(Variable variable, String methodName, ArrayList<Expr> expr, boolean retorno){
		this.variable = variable;
		this.methodName = methodName;
		this.expr = expr;
		this.retorno = retorno;
	}
	
	public boolean isObjectCreation() {
		return false;
	}
	
	public void genJava(PW pw) {
		if(retorno == true) {
			this.variable.genJava(pw);
			pw.print("." + methodName.substring(0,  methodName.length()-1) + "(");
			this.expr.get(0).genJava(pw);	
			for(int i = 1; i < this.expr.size(); i++) {
				pw.print(", ");
				this.expr.get(i).genJava(pw);	
			}
			pw.print(") ");
		}
		else {
			pw.printIdent("");
			this.variable.genJava(pw);
			pw.print("." + methodName.substring(0,  methodName.length()-1) + "(");
			this.expr.get(0).genJava(pw);	
			for(int i = 1; i < this.expr.size(); i++) {
				pw.print(", ");
				this.expr.get(i).genJava(pw);	
			}
			pw.println(");");
		}
	}
	
	public boolean isOnlyId() {
    	return false;
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
	private boolean retorno;
}
