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

public class FieldDec extends Variable {
	public FieldDec(Qualifier qualifier, String id, Type type){
		this.type = type;
		this.id = id;
		this.qualifier = qualifier;
	}
	
	public void genC( PW pw ) {
		
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		
	}
	public void genJava(PW pw) {
		this.qualifier.genJava(pw);
		pw.print(" " + this.type.getJavaname() + " ");
		pw.println(this.id + ";" );
	}
	
	public Type getType() {
		return type;
	}
	
	private String id;
	private Qualifier qualifier;
	private Type type;
}
 