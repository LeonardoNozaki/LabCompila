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
	public MethodCall(Variable variable, String methodName, boolean retorno){
		this.variable = variable;
		this.methodName = methodName;
		this.retorno = retorno;
	}
	
	public void genJava(PW pw) {
		if(retorno == true) {
			this.variable.genJava(pw);
			pw.print("." + methodName + "() ");
		}
		else {
			pw.printIdent("");
			this.variable.genJava(pw);
			pw.println("." + methodName + "();");
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
	private boolean retorno;
}
