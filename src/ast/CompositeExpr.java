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
	
	public boolean isObjectCreation() {
		return false;
	}
	
	public boolean isOnlyId() {
		return false;
	}
	
	@Override
	public void genC( PW pw) {
		this.genC(pw, false);
	}
	
	@Override
    public void genC( PW pw, boolean putParenthesis ) {
    	if(putParenthesis == true) {
    		pw.print("( ");
    	}
    	Type l = this.left.getType();
		Type r = this.right.getType();
    	if(l instanceof TypeString && r instanceof TypeString) {
    		pw.print("strcmp(");
			this.left.genC(pw, false);
			pw.print(", ");
			this.right.genC(pw, false);
			pw.print(") == 0");
			if(putParenthesis == true) {
	    		pw.print(" )");
	    	}
			return;
    	}
    	
    	this.left.genC(pw, false);
    	if(this.op != null && this.right != null) {
    		pw.print(" " + this.op.toString() + " ");
    		this.right.genC(pw, false);
    	}
    	if(putParenthesis == true) {
    		pw.print(" )");
    	}
    }
    
    public void genJava( PW pw ) {
    	this.left.genJava(pw);
    	if(this.op != null && this.right != null) {
    		pw.print(" " + this.op.toString() + " ");
    		this.right.genJava(pw);
    	}
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