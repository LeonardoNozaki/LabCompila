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

public class SelfMethodCall extends Expr{
	public SelfMethodCall(Variable variable, MethodDec method){
		this.method = method;
		this.variable = variable;
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
			pw.println("." + this.method.getName() + "();");
		}
		else {
			pw.print("this.");
			this.variable.genJava(pw);
			pw.print("." + this.method.getName() + "()");
		}
	}
	
	public void genC(PW pw) {
		this.genC(pw, false);
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		if(putParenthesis == true) {
			pw.print("(");
		}
		TypeCianetoClass tc = (TypeCianetoClass) variable.getType();
		int index = tc.findMethod(method.getName());
		pw.print("(self->_" + variable.getType().getName() + "_" + variable.getName() + "->vt[" + index + "])(self->_" + variable.getType().getName() + "_" + variable.getName() + ")");
		if(putParenthesis == true) {
			pw.print(")");
		}
		if(method.getType() == Type.voidType) {
			pw.println(";");
		}
	}
	
	public Type getType() {
		return method.getType();
	}

	private MethodDec method;
	private Variable variable;
}
