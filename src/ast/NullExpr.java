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

public class NullExpr extends Expr {
    
	@Override
	public void genC( PW pw, boolean putParenthesis ) {
	   if(putParenthesis == true) {
		   pw.printIdent("(NULL)");
	   }
	   else {
		   pw.printIdent("NULL");
	   }
	}
   
	@Override
	public void genC( PW pw ) {
		pw.printIdent("NULL");
	}
	public boolean isObjectCreation() {
		return false;
	}
   
	public void genJava(PW pw) {
	   pw.print("null");
	}
   
	public boolean isOnlyId() {
		return false;
	}
   
	public Type getType() {
		return Type.nullType;
	}
}