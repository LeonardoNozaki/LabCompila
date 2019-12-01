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
	public TokenMethodCall(Token t, MethodDec method, TypeCianetoClass classe){
		this. method = method;
		this.t = t;
		this.classe = classe;
	}
	
	public boolean isObjectCreation() {
		return false;
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
		int index = classe.findMethod(this.method.getName());
		if(method.getQuali().isPrivate() || index == -1) {
			pw.print("_" + method.getClassName() + "_" + method.getName() + "( self )" );
		}
		else {
			String className = "";
			if(t== Token.SELF) {
				className = classe.getName();	
				String returnName = this.method.getType().getCname();
				pw.print("( (" + returnName + " (*)(_class_" + className + " *)) self ");
				pw.print("->vt[" + index + "] ) ( (_class_" + className + " *) self) ");
			}
			else {
				className = classe.getSuperClassNameMethod(index);
				pw.print("_" + className + "_" + this.method.getName());
				pw.print("( (_class_" + className + " *) self)");
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
	private Token t;
	private TypeCianetoClass classe;
}
