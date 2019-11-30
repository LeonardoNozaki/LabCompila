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
import java.util.*;

public class LocalDecList extends Variable{
	public LocalDecList(Type type, ArrayList<Variable> id) {
		this.id = id;
		this.type = type;
		this.init = false;
	}
	
	public boolean getInit() {
		return this.init;
	}
	
	public boolean isObjectCreation() {
		return false;
	}
	
	public boolean isOnlyId() {
    	return false;
    }
	
	@Override
	public void genC( PW pw, boolean putParenthesis ) {
		this.genC(pw);
	}

	@Override
	public void genC(PW pw) {
		if(this.type instanceof TypeCianetoClass) {
			pw.printIdent(this.type.getCname() + " *");
			this.id.get(0).genC(pw, false);
			for(int i = 1; i < this.id.size(); i++) {
				pw.print(", *");
				this.id.get(i).genC(pw, false);
			}
			pw.println(";");
			return;
		}
		if(this.type instanceof TypeString) {
			for(int i = 0; i < this.id.size(); i++) {
				pw.printIdent(this.type.getCname());
				this.id.get(i).genC(pw, false);
				pw.println(" = (char*)malloc(sizeof(char)*1000);");
			}
			return;
		}
		pw.printIdent(this.type.getCname() + " ");
		this.id.get(0).genC(pw, false);
		for(int i = 1; i < this.id.size(); i++) {
			pw.print(", ");
			this.id.get(i).genC(pw, false);
		}
		pw.println(";");
	}
	
	public void genJava(PW pw) {
		pw.printIdent(this.type.getJavaname() + " ");
		this.id.get(0).genJava(pw);
		for(int i = 1; i < this.id.size(); i++) {
			pw.print(", ");
			this.id.get(i).genJava(pw);
		}
		pw.println(";");
	}
	public Type getType() {
		return type;
	}
	
	public String getName() {
		return "";
	}
	
	private Type type;
	private ArrayList<Variable> id;
	private boolean init;
}
