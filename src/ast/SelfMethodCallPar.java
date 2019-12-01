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


public class SelfMethodCallPar extends Expr{
	public SelfMethodCallPar(Variable variable, MethodDec method, ArrayList<Expr> expr, String className){
		this.method = method;
		this.expr = expr;
		this.variable = variable;
		this.className = className;
	}
	
	public boolean isOnlyId() {
		return false;
	}
	
	public boolean isObjectCreation() {
		return false;
	}
	
	public void genJava(PW pw) {
		if(method.getType() == Type.voidType) {
			pw.printIdent("this.");
			this.variable.genJava(pw);
			pw.print("." + this.method.getName() + "( ");
			if(this.expr.size() > 0) {
				this.expr.get(0).genJava(pw);
			}
			for(int i = 1; i < this.expr.size(); i++) {
				pw.print(", ");
				this.expr.get(i).genJava(pw);
			}
			pw.println(" );");
		}else {
			pw.print("this.");
			this.variable.genJava(pw);
			pw.print("." + this.method.getName() + "( ");
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
		TypeCianetoClass tc = (TypeCianetoClass) variable.getType();
		int index = tc.findMethod(method.getName());
		Type t = method.getType();
		if(t instanceof TypeCianetoClass) {
			pw.print("( (" + t.getCname() + " *(*)(" + variable.getType().getCname() + "*, ");
		}
		else {
			pw.print("( (" + t.getCname() + "(*)(" + variable.getType().getCname() + "*, ");
		}
		genParamTypes(pw);
		
		pw.print("))self->_" + className + "_" + variable.getName() + "->vt[" + index + "])(self->_" + className + "_" + variable.getName() + ", ");
		genParam(pw);
		pw.print(")");
	
		if(method.getType() == Type.voidType) {
			pw.println(";");
		}
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		if(putParenthesis == true) {
			pw.print("(");
			genC(pw);
			pw.print(")");
		}
		else {
			genC(pw);
		}
	}
	
	public Type getType() {
		return method.getType();
	}
	
	private void genParam(PW pw) {
		if(this.expr.size() > 0) {
			this.expr.get(0).genC(pw);
		}
		for(int i = 1; i < this.expr.size(); i++) {
			pw.print(", ");
			this.expr.get(i).genC(pw);
		}
	}
	
	private void genParamTypes(PW pw) {
		if(this.expr.size() > 0) {
			pw.print(this.expr.get(0).getType().getCname());
			if(this.expr.get(0).getType() instanceof TypeCianetoClass ) {
				pw.print(" *");
			}
		}
		for(int i = 1; i < this.expr.size(); i++) {
			pw.print(", ");
			pw.print(this.expr.get(i).getType().getCname());
			if(this.expr.get(i).getType() instanceof TypeCianetoClass ) {
				pw.print(" *");
			}
		}
	}

	private MethodDec method;
	private ArrayList<Expr> expr;
	private Variable variable; 
	private String className;
}
