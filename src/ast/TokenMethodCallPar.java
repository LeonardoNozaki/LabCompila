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
	public TokenMethodCallPar(Token t, MethodDec method, ArrayList<Expr> expr, TypeCianetoClass classe){
		this.method = method;
		this.expr = expr;
		this.t = t;
		this.classe = classe;
	}
	
	public boolean isObjectCreation() {
		return false;
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
	
	public void genC(PW pw) {
		int index = classe.findMethod(this.method.getName());
		String methodName = method.getName();
		if(methodName.endsWith(":")) {
			methodName = methodName.substring(0, methodName.length()-1);
			methodName = methodName + "$";
		}
		if(method.getQuali().isPrivate() || index == -1) {
			pw.print("_" + method.getClassName() + "_" + methodName + "( self, ");
			genParam(pw);
			pw.print(")");
		}
		else {
			String className = "";
			if(t == Token.SELF) {
				className = classe.getName();
				String returnName;
				if(this.method.getType() instanceof TypeCianetoClass) {
					returnName = this.method.getType().getCname() + " *";
				}
				else{
					returnName = this.method.getType().getCname();
				}
				pw.print("( (" + returnName + " (*)(_class_" + className + " *, ");
				genParamTypes(pw);
				pw.print(")) self->vt[" + index + "] ) ( (_class_" + className + " *) self, ");
				genParam(pw);
				pw.print(")");
			}
			else {
				className = classe.getSuperClassNameMethod(index);
				pw.print("_" + className + "_" + methodName);
				pw.print("( (_class_" + className + " *) self, ");
				genParam(pw);
				pw.print(")");
			}			
		}
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		if(putParenthesis == true && method.getType() == Type.voidType) {
			pw.printIdent("(");
		}
		else if(putParenthesis == true) {
			pw.print("(");
		}
		this.genC(pw);
		if(putParenthesis == true && method.getType() == Type.voidType) {
			pw.printIdent(")");
		}
		else if(putParenthesis == true) {
			pw.print(")");
		}
	}
	
	public Type getType() {
		return method.getType();
	}

	private MethodDec method;
	private ArrayList<Expr> expr;
	private Token t;
	private TypeCianetoClass classe;
}
