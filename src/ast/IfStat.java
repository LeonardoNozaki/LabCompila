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
		
	}

	private Expr expr;
	private ArrayList<Statement> leftPart = new ArrayList<Statement>();
	private ArrayList<Statement> rightPart = new ArrayList<Statement>();
}