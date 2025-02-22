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

public class SignalFactor extends Expr{
	public SignalFactor(Token op, Expr expr) {
		this.op = op;
		this.expr = expr;
	}
	
	public boolean isOnlyId() {
		if(this.op == Token.NOT) {
			return false;
		}
    	return expr.isOnlyId();
    }
	
	public boolean isObjectCreation() {
		return false;
	}
	
	public void nullOP() {
		this.op = null;
	}
	
	@Override
	public void genC( PW pw, boolean putParenthesis ) {
		if(putParenthesis == true) {
			pw.print("( ");
		}
		
    	if(this.op == null) {
    		this.expr.genC(pw, false);
    	}
    	else {
    		pw.print(this.op.toString());
    		this.expr.genC(pw, false);
    	}
    	
    	if(putParenthesis == true) {
			pw.print(" )");
		}
    }
	
	@Override
	public void genC( PW pw ) {
    	if(this.op == null) {
    		this.expr.genC(pw, false);
    	}
    	else {
    		pw.print(this.op.toString());
    		this.expr.genC(pw, false);
    	}
    }
	
	@Override
	public void genJava(PW pw) {
		pw.print(this.op.toString());
		this.expr.genJava(pw);
	}
	
	@Override
	public Type getType() {
		if(this.op == Token.NOT) {
			return Type.booleanType;
		}
		return expr.getType(); 
	}
	
	private Token op;
	private Expr expr;
}
