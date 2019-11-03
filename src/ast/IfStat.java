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

public class IfStat extends Statement {
	public IfStat( Expr expr, ArrayList<Statement> leftPart, ArrayList<Statement> rightPart ) {
		this.expr = expr;
		this.leftPart = leftPart;
		this.rightPart = rightPart;
	}

	public void genC( PW pw ) {
		if(this.expr instanceof SignalFactor) {
			SignalFactor sf = (SignalFactor) this.expr;
			sf.nullOP();
			pw.printIdent("if( ");
			this.expr.genC(pw, true);
			pw.println(" == false ){");
		}
		else {
			pw.printIdent("if( ");
			this.expr.genC(pw, true);
			pw.println(" != false ){");
		}
		
		pw.add();
		for(int i = 0; i < this.leftPart.size(); i++) {
			this.leftPart.get(i).genC(pw);
		}
		pw.sub();
		pw.printlnIdent("}");
		if(rightPart.size() > 0) {
			pw.printlnIdent("else {");
			pw.add();
			for(int i = 0; i < this.rightPart.size(); i++) {
				this.rightPart.get(i).genC(pw);
			}
			pw.sub();
			pw.printlnIdent("}");
		}
	}
	
	public void genJava(PW pw) {
		pw.printIdent("if( ");
		this.expr.genJava(pw);
		pw.println(" ){");
		pw.add();
		for(int i = 0; i < this.leftPart.size(); i++) {
			this.leftPart.get(i).genJava(pw);
		}
		pw.sub();
		pw.printlnIdent("}");
		if(rightPart.size() > 0) {
			pw.printlnIdent("else{");
			pw.add();
			for(int i = 0; i < this.rightPart.size(); i++) {
				this.rightPart.get(i).genJava(pw);
			}
			pw.sub();
			pw.printlnIdent("}");
		}
	}

	private Expr expr;
	private ArrayList<Statement> leftPart = new ArrayList<Statement>();
	private ArrayList<Statement> rightPart = new ArrayList<Statement>();
}