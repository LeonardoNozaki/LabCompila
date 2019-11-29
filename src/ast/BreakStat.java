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

public class BreakStat extends Statement{
	@Override
	public void genC( PW pw) {
	   pw.printlnIdent("break;");
	}
   
	@Override
	public void genJava( PW pw ) {
	   pw.printlnIdent("break;");
	}   
}
