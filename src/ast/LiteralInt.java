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

public class LiteralInt extends Expr {
    
    public LiteralInt( int value ) { 
        this.value = value;
    }
    
    public boolean isObjectCreation() {
		return false;
	}
    
    public boolean isOnlyId() {
    	return false;
    }
    
    public int getValue() {
        return value;
    }
    
    public void genJava(PW pw) {
    	pw.print("" + this.value);
    }
    
    @Override
    public void genC( PW pw, boolean putParenthesis ) {
        if(putParenthesis == true) {
        	pw.print("( " + this.value + " )");
        }
        else {
        	pw.print("" + this.value);
        }
    }
    
    @Override
    public Type getType() {
        return Type.intType;
    }
    
    private int value;
}
