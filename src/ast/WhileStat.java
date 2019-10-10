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

public class WhileStat extends Statement{
	public WhileStat( Expr expr, ArrayList<Statement> stat ) {
		this.expr = expr;
		this.stat = stat;
	}

	public void genC( PW pw ) {
		
	}
	
	public void genJava(PW pw) {
		pw.printIdent("while( ");
		this.expr.genJava(pw);
		pw.println(" ){");
		pw.add();
		for(int i = 0; i < this.stat.size(); i++) {
			this.stat.get(i).genJava(pw);
		}
		pw.sub();
		pw.printlnIdent("}");
	}

	private Expr expr;
	private ArrayList<Statement> stat = new ArrayList<Statement>();
}
