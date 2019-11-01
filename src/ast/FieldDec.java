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
		this.init = false;
	}
	
	public boolean getInit() {
		return this.init;
	}
	
	public boolean isObjectCreation() {
		return false;
	}
	
	public boolean isOnlyId() {
		return true;
	}
	
	public void genC( PW pw ) {
		pw.print(id);
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		if(putParenthesis == true) {
			pw.print("(" + id + ")");
		}
		else{
			pw.print(id);
		}
	}
	
	public void genJava(PW pw) {
		pw.print(id);
	}
	
	public Type getType() {
		return this.type;
	}
	
	public String getName() {
		return this.id;
	}
	
	public boolean isEqualField(FieldDec f) {
		if(this.id.equals(f.getName())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isEqualMethod(MethodDec f) {
		if(this.id.equals(f.getName())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String getTypeName() {
		return type.getName();
	}
	
	public Qualifier getQuali() {
		return qualifier;
	}
	
	private String id;
	private Qualifier qualifier;
	private Type type;
	private boolean init;
}
 