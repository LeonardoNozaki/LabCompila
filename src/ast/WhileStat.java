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

	private Expr expr;
	private ArrayList<Statement> stat = new ArrayList<Statement>();
}
