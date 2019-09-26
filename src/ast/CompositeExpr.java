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
import lexer.*;

public class CompositeExpr extends Expr {
	public CompositeExpr(Token op, Expr left, Expr right) {
		this.op = op;
		this.left = left;
		this.right = right;
	}
	
    public void genC( PW pw, boolean putParenthesis ) {
    	
    }
    
    public void genJava( PW pw ) {
    	left.genJava(pw);
    	if(op != null && right != null) {
    		pw.print(op.toString());
    		right.genJava(pw);
    	}
    }   
    
	@Override
	public void genC(PW pw) {
		//this.genC(pw, false);
	}
	
	@Override
	public Type getType() {
		if(op == Token.EQ || op == Token.LT || op == Token.GT || op == Token.LE || op == Token.GE || op == Token.NEQ) {
			return Type.booleanType;
		}
		else {
			return Type.undefinedType;
		}
	}
	
	private Token op;
	private Expr left, right;
}