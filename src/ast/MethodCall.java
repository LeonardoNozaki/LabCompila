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

public class MethodCall extends Expr{
	public MethodCall(Variable variable, MethodDec methodDec){
		this.variable = variable;
		this.methodDec = methodDec;
	}
	
	public String getName() {
		return this.variable.getName() + "." + this.methodDec.getName();
	}
	
	public boolean isObjectCreation() {
		return false;
	}
	
	@Override
	public void genJava(PW pw) {
		if(this.methodDec.getType() != Type.voidType) {
			this.variable.genJava(pw);
			pw.print("." + methodDec.getName() + "() ");
		}
		else {
			pw.printIdent("");
			this.variable.genJava(pw);
			pw.println("." + methodDec.getName() + "();");
		}
	}
	
	public boolean isOnlyId() {
    	return false;
    }
	
	@Override
	public void genC(PW pw) {
		String className = this.variable.getType().getCname();
		String returnName = this.methodDec.getType().getCname();
		TypeCianetoClass classe = (TypeCianetoClass) this.variable.getType();
		int index = classe.findMethod(this.methodDec.getName());
		
		if(this.methodDec.getType() != Type.voidType) {
			pw.print("( (" + returnName + " (*)(_class_" + className + " *)) ");
			this.variable.genC(pw, false);
			pw.print("->vt[" + index + "] )");
			this.variable.genC(pw, true);
			pw.print(" ");
		}
		else {
			pw.print("( (" + returnName + " (*)(_class_" + className + " *)) ");
			this.variable.genC(pw, false);
			pw.print("->vt[" + index + "] )");
			this.variable.genC(pw, true);
			pw.println(";");
		}
	}
	
	@Override
	public void genC( PW pw, boolean putParenthesis ) {
		this.genC(pw);
	}
	
	public Type getType() {
		return methodDec.getType();
	}
	
	private Variable variable;
	private MethodDec methodDec;
}
