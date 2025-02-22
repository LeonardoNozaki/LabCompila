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

public class LiteralString extends Expr {
    
    public LiteralString( String literalString ) { 
        this.literalString = literalString;
    }
    
    public boolean isObjectCreation() {
		return false;
	}
    
    public boolean isOnlyId() {
    	return false;
    }
    
    @Override
    public void genC( PW pw, boolean putParenthesis ) {
    	if(putParenthesis == true) {
	    	pw.print("(\"");
	    	pw.print(literalString);
	    	pw.print("\")");
    	}
    	else {
    		pw.print("\"");
        	pw.print(literalString);
        	pw.print("\"");
    	}
    }
    
    @Override
    public void genC(PW pw) {
    	pw.print("\"");
    	pw.print(literalString);
    	pw.print("\"");
    }
    
    @Override
    public void genJava(PW pw) {
    	pw.print("\"");
    	pw.print(literalString);
    	pw.print("\"");
    }

    @Override
    public Type getType() {
        return Type.stringType;
    }
    
    private String literalString;
}

