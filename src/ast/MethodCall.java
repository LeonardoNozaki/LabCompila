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
	public MethodCall(Variable variable, MethodDec methodDec, boolean retorno){
		this.variable = variable;
		this.methodDec = methodDec;
		this.retorno = retorno;
	}
	
	public boolean getRetorno() {
		return this.retorno;
	}
	
	public String getName() {
		return this.variable.getName() + "." + this.methodDec.getName();
	}
	
	public boolean isObjectCreation() {
		return false;
	}
	
	public void genJava(PW pw) {
		if(retorno == true) {
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
	
	public void genC(PW pw) {
		
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		
	}
	
	public Type getType() {
		return methodDec.getType();
	}
	
	private Variable variable;
	private MethodDec methodDec;
	private boolean retorno;
}
