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

public class SelfField extends Expr{
	public SelfField(FieldDec fd, String className){
		this.fieldDec = fd;
		this.className = className;
	}
	
	public boolean isOnlyId() {
			return true;	
    }
	
	public boolean isObjectCreation() {
		return false;
	}
	
	public void genJava(PW pw) {
		pw.print("this." + fieldDec.getName());
	}
	
	public void genC(PW pw) {
		pw.print("self->_" + className + "_" + fieldDec.getName());
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		if(putParenthesis == true) {
			pw.print("(self->_" + className + "_" + fieldDec.getName() + ")");
		}
		else {
			pw.print("self->_" + className + "_" + fieldDec.getName());
		}
	}
	
	public Type getType() {
		return fieldDec.getType();
	}

	private FieldDec fieldDec;
	private String className;
}
