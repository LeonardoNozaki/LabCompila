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

public class MethodDec {
	
	public MethodDec(Qualifier qualifier, String id, ArrayList<ParamDec> paramDec, Type type, ArrayList<Statement> stat) {
		this.qualifier = qualifier;
		this.id = id;
		this.paramDec = paramDec;
		this.type = type;
		this.stat = stat;
	}
	
	public void genC( PW pw ) {
		
	}
	
	public void genJava(PW pw) {
		this.qualifier.genJava(pw);
		pw.print(this.type.getJavaname() + " " + id + "( ");
		if(paramDec != null) {
			if(this.paramDec.size() > 0) {
				this.paramDec.get(0).genJava(pw);
			}
			for(int i = 1; i < this.paramDec.size(); i++) {
				pw.print(", ");
				this.paramDec.get(i).genJava(pw);
			}
		}
		pw.println("){");
		pw.add();
		for(int i = 0; i < this.stat.size(); i++) {
			this.stat.get(i).genJava(pw);
		}
		pw.sub();
		pw.printlnIdent("}");
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
	
	public Type getType() {
		return this.type;
	}
	
	private Qualifier qualifier;
	private String id;
	private ArrayList<ParamDec> paramDec;
	private Type type;
	private ArrayList<Statement> stat; 
}
