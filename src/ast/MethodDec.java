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
		
	}
	
	private Qualifier qualifier;
	private String id;
	private ArrayList<ParamDec> paramDec;
	private Type type;
	private ArrayList<Statement> stat; 
}
