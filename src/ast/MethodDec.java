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
	
	public boolean comparePar(ArrayList<Expr> expr) {
		if(expr.size() != paramDec.size()) {
			return false;
		}
		else {
			for(int i = 0; i < expr.size(); i++) {
				if(expr.get(i).getType().getClass() == paramDec.get(i).getType().getClass()) {
					if(expr.get(i).getType() instanceof TypeCianetoClass) {
						TypeCianetoClass classtype = (TypeCianetoClass)expr.get(i).getType();
						if(!classtype.searchType(paramDec.get(i).getType().getName())) {
							return false;
						}
					}
				}
				else {
					return false;
				}
			}
			return true;
		}
	}
	
	public void genJava(PW pw) {
		if(this.qualifier.isVoid()) {
			pw.printIdent("public ");
		}
		else {
			this.qualifier.genJava(pw);
		}
		if(id.charAt(id.length() -1) == ':') {
			pw.print(this.type.getJavaname() + " " + id.substring(0, id.length()-1) + "( ");
		}
		else {
			pw.print(this.type.getJavaname() + " " + id + "( ");
		}
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
